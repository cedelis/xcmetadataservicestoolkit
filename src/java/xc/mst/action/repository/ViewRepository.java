/**
  * Copyright (c) 2009 University of Rochester
  *
  * This program is free software; you can redistribute it and/or modify it under the terms of the MIT/X11 license. The text of the
  * license can be found at http://www.opensource.org/licenses/mit-license.php and copy of the license can be found on the project
  * website http://www.extensiblecatalog.org/.
  *
  */

package xc.mst.action.repository;

import org.apache.log4j.Logger;
import xc.mst.action.UserAware;
import xc.mst.bo.provider.Provider;
import xc.mst.bo.user.User;
import xc.mst.constants.Constants;
import xc.mst.harvester.ValidateRepository;
import xc.mst.manager.repository.DefaultProviderService;
import com.opensymphony.xwork2.ActionSupport;

/**
 * This method is used to View the properties of a Repository in the system
 *
 * @author Tejaswi Haramurali
 */
public class ViewRepository extends ActionSupport implements UserAware
{
    /** Serial id	 */
	private static final long serialVersionUID = -6162901340410964175L;

	/** A reference to the logger for this class */
	  static Logger log = Logger.getLogger(Constants.LOGGER_GENERAL);
	
	  /** The ID of the repository to be viewed */
	  private int repositoryId;
	
	  /**The User who is logged in */
	  private User user;
	
	  /**The repository to be viewed */
	  private Provider provider;
	
	  /** Boolean value that denotes success or failure */
	  private String listSets;
	
	  /** Boolean value that denotes success or failure */
	  private String listFormats;
	
	  /** Boolean value that denotes success or failure */
	  private String Identify;
      
	/** Error type */
	private String errorType; 
	
	/** Error messgae */
	private String message; 

      /**
       * Returns the ID of the repository to be viewed
       *
       * @return The ID of the repository
       */
      public int getRepositoryId()
      {
          return repositoryId;
      }

      /**
       * Sets the ID of the Repository to be viewed
       *
       * @param repoId The ID of the repository to be viewed
       */
      public void setRepositoryId(String repoId)
      {

          this.repositoryId = Integer.parseInt(repoId);
      }

      public ViewRepository()
      {
          repositoryId = 0;
      }

      /**
       * Sets the User who is currently logged in
       *
       * @param user The User who is currently logged in
       */
      public void setUser(User user)
      {
          this.user = user;
      }

      /**
       * Gets the User who is currently logged in
       *
       * @return returns the User object
       */
      public User getUser()
      {
          return user;
      }

      /**
       * Sets the Repository Object to be viewed
       *
       * @param provider The Repository object
       */
      public void setProvider(Provider provider)
      {
          this.provider = provider;
      }

      /**
       * Returns the repository object to be viewed
       *
       * @return retuns the repository object
       */
      public Provider getProvider()
      {
          return provider;
      }

      /**
       * A boolean value that is associated with success/failure of Validation of a repository
       *
       * @param listSets
       */
      public void setListSets(String listSets)
      {
          this.listSets = listSets;
      }


      /**
       * Return list sets boolean value
       *
       * @return listSets boolean value
       */
      public String getListSets()
      {
          return listSets;
      }

       /**
       * A boolean value that is associated with success/failure of Validation of a repository
        *
       * @param listSets
       */
      public void setListFormats(String listFormats)
      {
          this.listFormats = listFormats;
      }

      /**
       * Returns the boolean value for listFormats
       *
       * @return listFormats boolean value
       */
      public String getListFormats()
      {
          return listFormats;
      }

       /**
       * A boolean value that is associated with success/failure of Validation of a repository
        *
       * @param listSets
       */
      public void setIdentify(String Identify)
      {
          this.Identify = Identify;
      }

      /**
       * Returns the boolean value for identify
       *
       * @return identify boolean value
       */
      public String getIdentify()
      {
          return Identify;
      }
     

     /**
     * Overrides default implementation to view the details of a repository.
      *
     * @return {@link #SUCCESS}
     */
      @Override
      public String execute()
      {
          try
          {

                provider = new DefaultProviderService().getProviderById(repositoryId);
               
                return SUCCESS;
          }
          catch(Exception e)
          {
              log.error("Repository details cannot be displayed",e);
              this.addFieldError("allRepositoryError", "Repository details cannot be displayed");
              errorType = "error";
              return SUCCESS;
          }


      }

      /**
       * This method validates a Repository/Provider
       * 
       * @return returns the status of the operation and re-directs accordingly
       */
      public String validateRepository()
      {
          try
          {
                ValidateRepository vr = new ValidateRepository();
                vr.validate(getRepositoryId());
                message = "Repository revalidated!";
                errorType = "info";
                execute();
                return SUCCESS;
          }
          catch(Exception e)
          {
              log.error("Repository revalidation was unsuccessful",e);
              this.addFieldError("viewRepositoryError", "Revalidation unsuccessful");
              errorType = "error";
              return SUCCESS;
          }
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
     * Returns the information message which describes the status of the revalidation operation
     * 
     * @return information message
     */
	public String getMessage() {
		return message;
	}
}
