package org.esupportail.lecture.domain.beans;
 

/**
 * The class that represents department managers.
 */
public class DepartmentManager {

    /**
     * The unique id.
	 */
    private long id;

    /**
     * The user that manages the department.
     */
	private User user;

	/**
	 * The department managed by the user.
	 */
	private Department department;
	
	/**
	 * Can the user manage other users.
	 */
	private Boolean manageManagers;
	
	/**
	 * Can the user manage departments.
	 */
	private Boolean manageDepartment;
	
	/**
	 * Can the user manage things.
	 */
	private Boolean manageThings;
	
	/**
     * Bean constructor.
     */
    public DepartmentManager() {
    	super();
    	manageManagers = false;
    	manageDepartment = false;
    	manageThings = false;
    }

	/**
     * Clone.
	 * @param departmentManager 
     */
    public DepartmentManager(final DepartmentManager departmentManager) {
    	this();
    	id = departmentManager.getId();
    	user = departmentManager.getUser();
    	department = departmentManager.getDepartment();
    	manageManagers = departmentManager.getManageManagers();
    	manageDepartment = departmentManager.getManageDepartment();
    	manageThings = departmentManager.getManageThings();
    }

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof DepartmentManager)) {
			return false;
		}
		return ((DepartmentManager) obj).getId() == id;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override 
	public int hashCode() { 
		return super.hashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DepartmentManager#" + hashCode() + "[id=[" + id + "], user=" + user 
		+ ", department=" + department + ", manageManagers=[" + manageManagers 
		+ "], manageThings=[" + manageThings + "], manageDepartment=[" + manageDepartment + "]]";
	}

	 /**
	 * @return  Returns the id.
	 * 
	 */
    public long getId() {
        return id;
    }

	/**
	 * @param id The id to set.
	 * 
	 */
    public void setId(final long id) {
        this.id = id;
    }

	/**
	 * @return  Returns the department.
	 */
    public Department getDepartment() {
        return department;
    }

	 /**
	 * @param department The department to set.
	 */
    public void setDepartment(final Department department) {
        this.department = department;
    }

	/**
	 * @return  Returns the manageManagers.
	 * 
	 */
    public Boolean getManageManagers() {
        return manageManagers;
    }
    /**
	 * @param manageManagers  The manageManagers to set.
	 * 
	 */
    public void setManageManagers(final Boolean manageManagers) {
        this.manageManagers = manageManagers;
    }
    
    /**
     * @param user
     */
    public void setUser(final User user) {
    	this.user = user;
    }

    /**
     * @return the user
     */
    public User getUser() {
    	return this.user;
    }

	/**
	 * @return the manageDepartment.
	 */
	public Boolean getManageDepartment() {
		return manageDepartment;
	}

	/**
	 * @param manageDepartment the manageDepartment to set
	 */
	public void setManageDepartment(final Boolean manageDepartment) {
		this.manageDepartment = manageDepartment;
	}

	/**
	 * @return the manageThings
	 */
	public Boolean getManageThings() {
		return manageThings;
	}

	/**
	 * @param manageThings the manageThings to set
	 */
	public void setManageThings(final Boolean manageThings) {
		this.manageThings = manageThings;
	}

}
