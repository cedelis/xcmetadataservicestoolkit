/**
 * Copyright (c) 2009 eXtensible Catalog Organization
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the MIT/X11 license. The text of the
 * license can be found at http://www.opensource.org/licenses/mit-license.php and copy of the license can be found on the project
 * website http://www.extensiblecatalog.org/.
 *
 */

package xc.mst.action.user;

import java.util.List;

import org.apache.log4j.Logger;

import xc.mst.action.BaseActionSupport;
import xc.mst.bo.user.Group;
import xc.mst.bo.user.User;
import xc.mst.constants.Constants;
import xc.mst.dao.DataException;
import xc.mst.dao.DatabaseConfigException;

/**
 * This action method is used to edit the details of a user
 * 
 * @author Tejaswi Haramurali
 */
public class EditLocalUser extends BaseActionSupport {
    /** Serial id */
    private static final long serialVersionUID = -7257803486933942854L;

    /** The ID of the user whose details are to be edited */
    private int userId;

    /** A user object that is used to pre-fill JSP form fields. */
    private User temporaryUser;

    /** List of all the groups in the system */
    private List<Group> groupList;

    /** The email ID of the user */
    private String email;

    /** The password of the user */
    private String password;

    /** The first Name of the user */
    private String firstName;

    /** The last Name of the user */
    private String lastName;

    /** The groups that have been assigned to the new user */
    private String[] groupsSelected;

    /** A reference to the logger for this class */
    static Logger log = Logger.getLogger(Constants.LOGGER_GENERAL);

    /** Error type */
    private String errorType;

    /**
     * Overrides default implementation to view the edit local user page.
     * 
     * @return {@link #SUCCESS}
     */
    @Override
    public String execute() {
        try {
            setGroupList(getGroupService().getAllGroups());
            User user = getUserService().getUserById(userId);
            setTemporaryUser(user);
            return SUCCESS;
        } catch (DatabaseConfigException dce) {
            log.error(dce.getMessage(), dce);
            this.addFieldError("editLocalUserError", "Unable to connect to the database. Database Configuration may be incorrect");
            errorType = "error";
            return ERROR;
        }
    }

    /**
     * The action method that actually does the task of editing the details of a local user
     * 
     * @return returns status of the edit operation
     */
    public String editLocalUser() {
        try {

            setGroupList(getGroupService().getAllGroups());
            User user = getUserService().getUserById(userId);
            user.setServer(getServerService().getServerByName("Local"));
            user.setEmail(email);
            user.setFailedLoginAttempts(0);
            user.setFirstName(firstName);
            user.setLastName(lastName);

            if (password != null && password.length() > 0) {
                user.setPassword(getUserService().encryptPassword(password));
            }

            // Check if user has permissions
            boolean hasPermission = false;
            if (user.getGroups() != null && user.getGroups().size() > 0) {
                hasPermission = true;
            }

            user.removeAllGroups();
            for (int i = 0; i < groupsSelected.length; i++) {
                Group group = getGroupService().getGroupById(Integer.parseInt(groupsSelected[i]));
                user.addGroup(group);
            }

            User similarEmail = getUserService().getUserByEmail(email, getServerService().getServerByName("Local"));
            if (similarEmail != null) {
                if (similarEmail.getId() != userId) {
                    if (similarEmail.getServer().getName().equalsIgnoreCase("Local")) {
                        this.addFieldError("editLocalUserError", "Email ID already exists");
                        errorType = "error";
                        setGroupList(getGroupService().getAllGroups());
                        setTemporaryUser(user);
                        return INPUT;
                    }
                }
            }

            getUserService().updateUser(user);

            // Email user that permissions has been added.
            if (!hasPermission) {
                getUserService().sendEmailToUserWithPermissions(user);
            }

            return SUCCESS;
        } catch (DatabaseConfigException dce) {
            log.error(dce.getMessage(), dce);
            this.addFieldError("editLocalUserError", "Unable to connect to the database. Database Configuration may be incorrect");
            errorType = "error";
            return ERROR;
        } catch (DataException de) {
            log.error(de.getMessage(), de);
            this.addFieldError("editLocalUserError", "Error in editing local user. An email has been sent to the administrator");
            getUserService().sendEmailErrorReport();
            errorType = "error";
            return ERROR;
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
     * @param errorType
     *            error type
     */
    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    /**
     * Returns the first name of the user
     * 
     * @return first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the user
     * 
     * @param firstName
     *            first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName.trim();
    }

    /**
     * Returns the last name of the user
     * 
     * @return last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the user
     * 
     * @param lastName
     *            last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName.trim();
    }

    /**
     * Sets the email ID of the user
     * 
     * @param email
     *            email ID
     */
    public void setEmail(String email) {
        this.email = email.trim();
    }

    /**
     * Returns the email ID of the user
     * 
     * @return email ID
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the password of the user
     * 
     * @param password
     *            The password to be assigned
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the password of the user
     * 
     * @return user's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the list of groups that the new user has been assigned
     * 
     * @param selectedGroupList
     *            list of selected groups
     */
    public void setGroupsSelected(String[] groupsSelected) {
        this.groupsSelected = groupsSelected;
    }

    /**
     * Returns the list of groups that have been assigned to the user
     * 
     * @return list of selected groups
     */
    public String[] getGroupsSelected() {
        return groupsSelected;
    }

    /**
     * Sets the user whose details are to be edited
     * 
     * @param user
     *            user object
     */
    public void setTemporaryUser(User user) {
        this.temporaryUser = user;
    }

    /**
     * Returns the user whose details are to be edited.
     * 
     * @return user object
     */
    public User getTemporaryUser() {
        return temporaryUser;
    }

    /**
     * Sets the ID of the user whose details are to be edited.
     * 
     * @param userId
     *            user ID
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Returns the ID of the user whose details are to be edited.
     * 
     * @return user ID
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Assigns the list of groups that a user can belong to
     * 
     * @param groupList
     *            list of groups
     */
    public void setGroupList(List<Group> groupList) {
        this.groupList = groupList;
    }

    /**
     * Returns a list of groups that a user can belong to
     * 
     * @return list of groups
     */
    public List<Group> getGroupList() {
        return groupList;
    }

}
