/**
  * Copyright (c) 2009 University of Rochester
  *
  * This program is free software; you can redistribute it and/or modify it under the terms of the MIT/X11 license. The text of the
  * license can be found at http://www.opensource.org/licenses/mit-license.php and copy of the license can be found on the project
  * website http://www.extensiblecatalog.org/.
  *
  */

package xc.mst.action.configuration;

import java.util.Iterator;
import java.util.List;

import xc.mst.bo.user.Server;
import xc.mst.manager.user.DefaultServerService;
import xc.mst.manager.user.ServerService;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import xc.mst.constants.Constants;

/**
 * This class is used to add a new LDAP server to the system
 *
 * @author Tejaswi Haramurali
 */
public class AddLDAP extends ActionSupport
{
    /**
	 * Eclipse generated id
	 */
	private static final long serialVersionUID = 531062895841167505L;

    /** A reference to the logger for this class */
	static Logger log = Logger.getLogger(Constants.LOGGER_GENERAL);

	/**Creates a service object for Servers */
    private ServerService serverService = new DefaultServerService();

    /**The display name used for the LDAP server **/
    private String displayName;

    /** The URL of the LDAP server */
    private String serverURL;

    /**The port number on the LDAP server **/
    private String port;

    private String userNameAttribute;

    private String startLocation;

    /**This is a temporary server object that is used to pre-fill JSP form fields */
    private Server server = new Server();

    /**Provides the status of the add Operation in the JSP page */
    private String message;
    
	/** Error type */
	private String errorType; 
	
	/** Indicates whether to show forgot password URL */
	private boolean showForgotPasswordLink;
	
	/** URL to forward the user to get forgot password */
	private String forgotPasswordUrl;

    /**sets the display name of the LDAP server **/
    public void setDisplayName(String displayName)
    {
        this.displayName = displayName.trim();
    }

    /**returns the display name of the LDAP server */
    public String getDisplayName()
    {
        return this.displayName;
    }

    /** sets the URL of the server to the specified value */
    public void setServerURL(String serverURL)
    {
        this.serverURL = serverURL.trim();
    }

    /** returns the server URL */
    public String getServerURL()
    {
        return this.serverURL;
    }

    /** sets the port number of the LDAP server to the specified value */
    public void setPort(String port)
    {
        this.port = port;
    }

    /**returns the port number of the LDAP server */
    public String getPort()
    {
        return port;
    }

    /**
     * Sets the user name attribute
     *
     * @param userNameAttribute username attribute
     */
    public void setUserNameAttribute(String userNameAttribute)
    {
        this.userNameAttribute = userNameAttribute;
    }

    /**
     * returns the user name attribute
     *
     * @return username attribute
     */
    public String getUserNameAttribute()
    {
        return this.userNameAttribute;
    }

    /**
     * sets the start location
     *
     * @param startLocation start location
     */
    public void setStartLocation(String startLocation)
    {
        this.startLocation = startLocation;
    }

    /**
     * returns the start location
     *
     * @return start location
     */
    public String getStartLocation()
    {
        return this.startLocation;
    }


     /**
     * Overrides default implementation to add an LDAP server.
     * @return {@link #SUCCESS}
     */
    @Override
    public String execute()
    {
        try
        {
                
                List<Server> serverList = serverService.getAll();
                Iterator<Server> iter = serverList.iterator();

                while(iter.hasNext())
                   {
                       Server tempServer = (Server)iter.next();
                       if(tempServer.getType()!=4)
                       {
                           setServer(tempServer);
                           break;
                       }

                   }
                return SUCCESS;
        }
        catch(Exception e)
        {
            this.addFieldError("addLDAPError", "LDAP server could not be configured correctly");
            errorType = "error";
            return INPUT;
        }
    }

    /**
     * The method that does the actual task of adding a new LDAP server.
     * @return {@link #SUCCESS}
     */
    public String addLDAP()
    {
        try
        {
            boolean serverExists = false;
         
            List<Server> serverList = serverService.getAll();
            Iterator<Server> iter = serverList.iterator();

            while(iter.hasNext())
            {
                Server tempServer = (Server)iter.next();
                if(tempServer.getType()!=4)
                {
                    serverExists = true;
                    setServer(tempServer);
                    break;
                }

            }
            if(serverExists==false)
            {
                              
                server.setName(getDisplayName());
                server.setUrl(getServerURL());
                server.setPort(Integer.parseInt(getPort()));
                server.setType(1);
                server.setStartLocation(getStartLocation());
                server.setUserNameAttribute(getUserNameAttribute());
                server.setShowForgotPasswordLink(showForgotPasswordLink);
                if (showForgotPasswordLink) {
                	server.setForgotPasswordUrl(forgotPasswordUrl);
                } else {
                	server.setForgotPasswordUrl(null);
                }
                serverService.insertServer(server);
            }
            else
            {
                server.setName(getDisplayName());
                server.setUrl(getServerURL());
                server.setPort(Integer.parseInt(getPort()));
                server.setType(1);
                server.setStartLocation(getStartLocation());
                server.setUserNameAttribute(getUserNameAttribute());
                server.setShowForgotPasswordLink(showForgotPasswordLink);
                if (showForgotPasswordLink) {
                	server.setForgotPasswordUrl(forgotPasswordUrl);
	            } else {
	            	server.setForgotPasswordUrl(null);
	            }
                
                serverService.updateServer(server);
            }

            message = "LDAP Server Information Saved.";
            errorType = "info";
            return SUCCESS;
        }
        catch(Exception e)
        {
            log.debug(e);
            this.addFieldError("addLDAPError", "Error : LDAP server could not be configured correctly");
            errorType = "error";
            return INPUT;
        }
    }

    /**
     * returns the status of the add operation
     *
     * @return information message
     */
	public String getMessage() {
		return message;
	}

    /**
     * sets the status of the add operation
     *
     * @param message information message
     */
	public void setMessage(String message) {
		this.message = message;
	}

    /**
     * returns the temporary server object that is used to display details on the JSP
     *
     * @return server object
     */
	public Server getServer() {
		return server;
	}

    /**
     * sets the temporary server object which is used to display details on the JSP
     *
     * @param server object
     */
	public void setServer(Server server) {
		this.server = server;
	}

    /**
     * returns error type
     *
     * @return error type
     */
	public String getErrorType() {
		return errorType;
	}

    /**
     * sets error type
     *
     * @param errorType error type
     */
	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}

	public boolean isShowForgotPasswordLink() {
		return showForgotPasswordLink;
	}

	public void setShowForgotPasswordLink(boolean showForgotPasswordLink) {
		this.showForgotPasswordLink = showForgotPasswordLink;
	}

	public String getForgotPasswordUrl() {
		return forgotPasswordUrl;
	}

	public void setForgotPasswordUrl(String forgotPasswordUrl) {
		this.forgotPasswordUrl = forgotPasswordUrl;
	}
}
