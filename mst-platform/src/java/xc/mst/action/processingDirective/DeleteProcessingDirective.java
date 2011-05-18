

/**
  * Copyright (c) 2009 eXtensible Catalog Organization
  *
  * This program is free software; you can redistribute it and/or modify it under the terms of the MIT/X11 license. The text of the
  * license can be found at http://www.opensource.org/licenses/mit-license.php and copy of the license can be found on the project
  * website http://www.extensiblecatalog.org/.
  *
  */

package xc.mst.action.processingDirective;

import java.util.List;

import org.apache.log4j.Logger;

import xc.mst.action.BaseActionSupport;
import xc.mst.bo.processing.Job;
import xc.mst.bo.processing.ProcessingDirective;
import xc.mst.bo.provider.Provider;
import xc.mst.bo.service.Service;
import xc.mst.dao.DatabaseConfigException;
import xc.mst.repo.Repository;
import xc.mst.utils.MSTConfiguration;

/**
 * This action method deletes a Processing Directive
 *
 * @author Tejaswi Haramurali
 */
public class DeleteProcessingDirective extends BaseActionSupport
{
	 /** Serial ID*/
	private static final long serialVersionUID = -3203721703516486193L;

	/** The ID of the Processing Directive to be deleted */
    private int processingDirectiveId;

    /** A reference to the logger for this class */
    static Logger log = Logger.getLogger(DeleteProcessingDirective.class);
    
	/** Error type */
	private String errorType;
	
	/** Message explaining why the processing rule cannot be deleted */
	private String message;
    
    /** Determines whether processing rule is deleted */
	private boolean deleted;

    /**
     * Sets the Processing directive ID
     *
     * @param processingDirectiveId Processing Directive ID
     */
    public void setProcessingDirectiveId(int processingDirectiveId)
    {

        this.processingDirectiveId = processingDirectiveId;
    }

    /**
     * Returns the ID of the processing directive
     *
     * @return processing directive ID
     */
    public int getProcessingDirectiveId()
    {
        return processingDirectiveId;
    }

    /**
     * Overrides default implementation to delete a processing directive.
     *
     * @return {@link #SUCCESS}
     */
    @Override
    public String execute()
    {
        try
        {
            ProcessingDirective tempProcDir = getProcessingDirectiveService().getByProcessingDirectiveId(processingDirectiveId);
            if(tempProcDir==null)
            {
            	deleted = false;
                this.addFieldError("DeleteDirectiveError", "Error Deleting Processing Rule. An email has been sent to the administrator.");
                getUserService().sendEmailErrorReport();
                errorType = "error";
                return SUCCESS;
            }
            //      - if no processing is occurring &&
            //        if all records on input side are marked deleted &&
            //        if all successors of the of the records from the input side are marked deleted
            //        then   ---->  the processing rule will be deleted immediately.
            	
        	if (!hasNonDeletedSourceRecords(tempProcDir)) {
        		if (!hasNonDeletedDestinationRecords(tempProcDir)) {
                    if (!isProcessingDirectiveInUse(tempProcDir)) {
                    	// The "Processing occurring..." error 
                    	// should only come up if the records are all marked deleted, AND processing is occurring.
                    	
                        getProcessingDirectiveService().deleteProcessingDirective(tempProcDir);
                        deleted = true;
                        
                        return SUCCESS;
                    }
                	else {
                        deleted = false; // this flag will be used to decide whether to show the 2nd dialog.

                        Object[] messageArguments = {tempProcDir.getId()}; 
                        message = MSTConfiguration.getMSTString("message.processingRuleInUse", messageArguments);
                        log.debug("**** action error: message set to: "+message);                        
                       
                        return INPUT;
                	}
        		}
            	else {
                    deleted = false; // this flag will be used to decide whether to show the 2nd dialog.

                    Object[] messageArguments = {getOutputRepository(tempProcDir).getName()}; 
                    message = MSTConfiguration.getMSTString("message.processingRuleNonDeletedDestRecords", messageArguments);
                    log.debug("**** action error: message set to: "+message);
                    
                    return INPUT;
            	}
        	}
        	else {
                deleted = false; // this flag will be used to decide whether to show the 2nd dialog.

                Object[] messageArguments = {getInputRepository(tempProcDir).getName()}; 
                message = MSTConfiguration.getMSTString("message.processingRuleNonDeletedSrcRecords", messageArguments);
                log.debug("**** action error: message set to: "+message);                        
                
                return INPUT;
        	}	
        }
        // any other uncaught exception will get shown to the user through a javascript alert 
        //                                              - list_processingdirectives -> handleError
        //                                                (this is the desired behavior)
        catch(DatabaseConfigException e)
        {
            log.error("Deletion of processing rule Unsuccessful",e);
            this.addFieldError("listServicesError", "Deletion of processing rule Unsuccessful");
            errorType = "error";
            return SUCCESS;
        }
    }

    private Repository getOutputRepository(ProcessingDirective tempProcDir) {
    	Repository outgoingRepo;
		try {
			Service outputS = tempProcDir.getService();  // the service used to produce the output
			outgoingRepo = getRepository(outputS);
		} 
		catch (NullPointerException e) {
			return null;
		}
    	
    	return  outgoingRepo;
    }

    private Repository getInputRepository(ProcessingDirective tempProcDir) {
    	Repository incomingRepo;
		try {
			Provider provider = tempProcDir.getSourceProvider();
			incomingRepo = getRepositoryService().getRepository(provider);
		} 
		catch (NullPointerException e) {
			return null;
		}
    	
    	return incomingRepo;
    }
    
    private Repository getRepository(Service outputS) {
    	if (outputS == null) {
    		return null;
    	}
    	try {
			List<Repository> list =  getRepositoryService().getAll();
			for (Repository r: list) {
				if (r != null) {
					if (r.getService().equals(outputS)) {
						return r;
					}
				}
			}
		} 
    	catch (NullPointerException e) {
			return null;
		}
    	return null;
    }
    
    private boolean hasNonDeletedSourceRecords(ProcessingDirective tempProcDir){
    	Repository r = getInputRepository(tempProcDir);
    	if (r == null) {
    		return false;
    	}
    	return hasNonDeletedRecords(r);
    }

    private boolean hasNonDeletedDestinationRecords(ProcessingDirective tempProcDir){
    	Repository r = getOutputRepository(tempProcDir);
    	if (r == null) {
    		return false;
    	}
    	return hasNonDeletedRecords(r);
    }
    
    private boolean hasNonDeletedRecords(Repository r){
    	if (!getRepositoryDAO().exists(r.getName())) {
    		return false;
    	}
    	else if (getRepositoryDAO().hasOnlyRecordsOfStatus(r.getName(), 'D')) {
    		return false;
    	}
    	return true;
    }

    private boolean isProcessingDirectiveInUse(ProcessingDirective tempProcDir)
			throws DatabaseConfigException {
		List<Job> jobs = getJobService().getAllJobs();
		for (Job j:jobs) {
			ProcessingDirective pd = j.getProcessingDirective();
			if (pd != null) {
				if (pd.getId() == tempProcDir.getId()) {
					return true;
				}
			}
		}
		return false;
	}

    /**
     * Returns error type
     *
     * @return error type
     */
	public String getErrorType() {
		return errorType;
	}

    /**
     * Sets error type
     * 
     * @param errorType error type
     */
	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}

	/**
	 * Returns the error message
	 * 
	 * @return error message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Returns true if repository deleted, else false
	 * 
	 * @return Returns true if repository deleted, else false  (for JSON object)
	 */
	public boolean isDeleted() {
		return deleted;
	}
}
