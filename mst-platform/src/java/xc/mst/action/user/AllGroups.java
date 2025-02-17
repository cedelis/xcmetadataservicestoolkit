/**
 * Copyright (c) 2009 eXtensible Catalog Organization
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the MIT/X11 license. The text of the
 * license can be found at http://www.opensource.org/licenses/mit-license.php and copy of the license can be found on the project
 * website http://www.extensiblecatalog.org/.
 *
 */

package xc.mst.action.user;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import xc.mst.action.BaseActionSupport;
import xc.mst.bo.user.Group;
import xc.mst.constants.Constants;
import xc.mst.dao.DatabaseConfigException;
import xc.mst.dao.user.GroupDAO;

/**
 * This action method diplays all groups
 * 
 * @author Tejaswi Haramurali
 */
public class AllGroups extends BaseActionSupport {
    /** Serial id */
    private static final long serialVersionUID = -6634790751181787459L;

    /** determines whether the columns are to be sorted in ascending or descending order */
    private boolean isAscendingOrder = true;

    /** determines the column on which the rows are to be sorted */
    private String columnSorted = "GroupName";

    /** A reference to the logger for this class */
    static Logger log = Logger.getLogger(Constants.LOGGER_GENERAL);

    /** The list of groups that a user can belong to */
    private List<Group> groupList;

    /** Error type */
    private String errorType;

    /**
     * Overrides default implementation to view the all groups page.
     * 
     * @return {@link #SUCCESS}
     */
    @Override
    public String execute() {
        try {
            List<Group> tempList = new ArrayList<Group>();

            if (columnSorted.equalsIgnoreCase("GroupName") || (columnSorted.equalsIgnoreCase("GroupDescription"))) {
                if (columnSorted.equalsIgnoreCase("GroupName")) {
                    tempList = getGroupService().getAllGroupsSorted(isAscendingOrder, GroupDAO.COL_NAME);
                } else {
                    tempList = getGroupService().getAllGroupsSorted(isAscendingOrder, GroupDAO.COL_DESCRIPTION);
                }

                List<Group> finalList = new ArrayList<Group>();

                Iterator<Group> iter = tempList.iterator();
                while (iter.hasNext()) {
                    Group group = (Group) iter.next();
                    group.setMemberCount(getUserService().getUserCountForGroup(group.getId()));
                    finalList.add(group);
                }
                setGroupList(finalList);
                setIsAscendingOrder(isAscendingOrder);
                setColumnSorted(columnSorted);
                return SUCCESS;
            } else {
                this.addFieldError("allGroupsError", "The specified column name does not exist");
                errorType = "error";
                return SUCCESS;
            }
        } catch (DatabaseConfigException dce) {
            log.error(dce.getMessage(), dce);
            this.addFieldError("allGroupsError", "Unable to connect to the database. Database Configuration may be incorrect.");
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
     * @param errorType
     *            error type
     */
    public void setErrorType(String errorType) {
        this.errorType = errorType;
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

    /**
     * Sets the boolean value which determines if the rows are to be sorted in ascending order
     * 
     * @param isAscendingOrder
     */
    public void setIsAscendingOrder(boolean isAscendingOrder) {
        this.isAscendingOrder = isAscendingOrder;
    }

    /**
     * Gets the boolean value which determines if the rows are to be sorted in ascending order
     * 
     * @param isAscendingOrder
     */
    public boolean getIsAscendingOrder() {
        return this.isAscendingOrder;
    }

    /**
     * Sets the name of the column on which the sorting should be performed
     * 
     * @param columnSorted
     *            name of the column
     */
    public void setColumnSorted(String columnSorted) {
        this.columnSorted = columnSorted;
    }

    /**
     * Returns the name of the column on which sorting should be performed
     * 
     * @return column name
     */
    public String getColumnSorted() {
        return this.columnSorted;
    }

}
