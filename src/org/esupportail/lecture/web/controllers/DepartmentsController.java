/**
 * ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
 * http://sourcesup.cru.fr/projects/esup-lecture
 */
package org.esupportail.lecture.web.controllers;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import org.esupportail.lecture.domain.beans.Department;
import org.esupportail.lecture.domain.beans.DepartmentManager;
import org.esupportail.lecture.domain.beans.User;
import org.esupportail.commons.exceptions.UserNotFoundException;
import org.esupportail.commons.services.ldap.LdapService;
import org.esupportail.commons.web.controllers.LdapSearchCaller;
import org.springframework.util.Assert;

/**
 * A bean to manage departments.
 */
public class DepartmentsController extends AbstractContextAwareController implements LdapSearchCaller {

	/**
	 * The LDAP service.
	 */
	private LdapService ldapService;
	
	/**
	 * The department to add.
	 */
	private Department departmentToAdd;
	
	/**
	 * The department to update.
	 */
	private Department departmentToUpdate;
	
	/**
	 * The department manager to add.
	 */
	private DepartmentManager departmentManagerToAdd;
	
	/**
	 * The department manager to update.
	 */
	private DepartmentManager departmentManagerToUpdate;
	
	/**
	 * The id of the user to give manager privileges.
	 */
	private String ldapUid;
	
	/**
	 * Bean constructor.
	 */
	public DepartmentsController() {
		super();
	}

	/**
	 * @see org.esupportail.lecture.web.controllers.AbstractContextAwareController#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		Assert.notNull(this.ldapService, 
				"property ldapService of class " + this.getClass().getName() + " can not be null");
	}

	/**
	 * @see org.esupportail.commons.web.controllers.Resettable#reset()
	 */
	public void reset() {
		departmentToAdd = new Department();
		departmentManagerToAdd = new DepartmentManager();
		ldapUid = null;
		departmentManagerToUpdate = null;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getClass().getSimpleName() + "#" + hashCode() + "[departmentToAdd=" + departmentToAdd
		+ ", departmentToUpdate=" + departmentToUpdate + ", departmentManagerToAdd=" + departmentManagerToAdd
		+ ", departmentManagerToUpdate=" + departmentManagerToUpdate + "]";
	}

	/**
	 * @return true if the current user is allowed to acces the view.
	 */
	public boolean isPageAuthorized() {
		User currentUser = getCurrentUser();
		if (currentUser == null) {
			return false;
		}
		if (isCurrentUserCanAddDepartment()) {
			return true;
		}
		return !getDomainService().getManagedDepartments(currentUser).isEmpty();
	}
	
	/**
	 * @return the departments managed by the current user.
	 */
	public List <Department> getDepartments() {
		return getDomainService().getManagedDepartments(getCurrentUser());
	}

	/**
	 * @return true if the current user can add a department
	 */
	public boolean isCurrentUserCanAddDepartment() {
		return getDomainService().userCanAddDepartment(getCurrentUser());
	}

	/**
	 * @return true if the current user is allowed to view the current department.
	 */
	public boolean isCurrentUserCanViewDepartment() {
		return getDomainService().userCanViewDepartment(getCurrentUser(), getSessionController().getDepartment());
	}
	
	/**
	 * @return true if the current user is allowed to delete the current department.
	 */
	public boolean isCurrentUserCanDeleteDepartment() {
		return getDomainService().userCanDeleteDepartment(getCurrentUser(), getSessionController().getDepartment());
	}
	
	/**
	 * @return true if the current user can add a department manager
	 */
	public boolean isCurrentUserCanAddDepartmentManager() {
		return getDomainService().userCanAddDepartmentManager(
				getCurrentUser(), getSessionController().getDepartment());
	}

	/**
	 * @return the departmentManagers
	 */
	public List<DepartmentManager> getDepartmentManagers() {
		return getDomainService().getDepartmentManagers(getSessionController().getDepartment());
	}

	/**
	 * @return true if the current user can edit the department properties
	 */
	public boolean isCurrentUserCanEditDepartmentProperties() {
		return getDomainService().userCanEditDepartmentProperties(
				getCurrentUser(), getSessionController().getDepartment());
	}

	/**
	 * JSF validator.
	 * @param context 
	 * @param component 
	 * @param object 
	 */
	public void validateFilter(
			@SuppressWarnings("unused")
			final FacesContext context, 
			@SuppressWarnings("unused")
			final UIComponent component, 
			final Object object) {
		String ldapFilter = (String) object;
		if (ldapFilter == null || "".equals(ldapFilter)) {
			return;
		}
		String error = ldapService.testLdapFilter(ldapFilter); 
		if (error != null) {
			throw new ValidatorException(getFacesErrorMessage("_.MESSAGE.BAD_LDAP_FILTER", error));
		}
	}

	/**
	 * @return a String.
	 */
	public String addDepartment() {
		if (!isCurrentUserCanAddDepartment()) {
			addUnauthorizedActionMessage();
			return null;
		}
		if (getDomainService().isDepartmentLabelUsed(departmentToAdd.getLabel())) {
			addWarnMessage("label", "DEPARTMENTS.MESSAGE.LABEL_ALREADY_USED", departmentToAdd.getLabel());
			return null;
		}
		getDomainService().addDepartment(departmentToAdd);
		getSessionController().setDepartment(departmentToAdd);
		addInfoMessage(null, "DEPARTMENTS.MESSAGE.DEPARTMENT_ADDED", departmentToAdd.getLabel());
		departmentToAdd = new Department();
		return "departmentAdded";
	}

	/**
	 * JSF callback.
	 * @return a String
	 */
	public String updateDepartment() {
		if (!isCurrentUserCanEditDepartmentProperties()) {
			addUnauthorizedActionMessage();
			return null;
		}
		if (!getSessionController().getDepartment().getLabel().equals(departmentToUpdate.getLabel())) {
			// the label has changed, check if it is not used
			if (getDomainService().isDepartmentLabelUsed(departmentToUpdate.getLabel())) {
				addWarnMessage("label", "DEPARTMENTS.MESSAGE.LABEL_ALREADY_USED", 
						departmentToUpdate.getLabel());
				return null;
			}
		}
		getDomainService().updateDepartment(departmentToUpdate);
		addInfoMessage(null, "DEPARTMENTS.MESSAGE.DEPARTMENT_UPDATED", departmentToUpdate.getLabel());
		getSessionController().setDepartment(departmentToUpdate);
		return "departmentUpdated";
	}
	
	/**
	 * @return a String.
	 */
	public String addDepartmentManager() {
		if (!isCurrentUserCanAddDepartmentManager()) {
			addUnauthorizedActionMessage();
			return null;
		}
		User user;
		try {
			user = getDomainService().getUser(ldapUid);
		} catch (UserNotFoundException e) {
			addWarnMessage("ldapUid", "_.MESSAGE.USER_NOT_FOUND", ldapUid);
			return null;
		}
		if (getDomainService().isDepartmentManager(getSessionController().getDepartment(), user)) {
			addWarnMessage("ldapUid", "DEPARTMENTS.MESSAGE.USER_ALREADY_MANAGER", ldapUid);
			return null;
		}
		departmentManagerToAdd.setDepartment(getSessionController().getDepartment());
		departmentManagerToAdd.setUser(user);
		getDomainService().addDepartmentManager(departmentManagerToAdd);
		addInfoMessage(
				null, "DEPARTMENTS.MESSAGE.DEPARTMENT_MANAGER_ADDED", user.getDisplayName(), 
				user.getId(), getSessionController().getDepartment().getLabel());
		setDepartmentManagerToUpdate(departmentManagerToAdd);
		departmentManagerToAdd = new DepartmentManager();
		ldapUid = "";
		return "departmentManagerAdded";			
	}
	
	/**
	 * @return true if the current user can delete the department manager.
	 */
	public boolean isCurrentUserCanDeleteDepartmentManager() {
		return getDomainService().userCanDeleteManager(getCurrentUser(), departmentManagerToUpdate);
	}

	/**
	 * @return a String.
	 */
	public String confirmDeleteDepartmentManager() {
		if (!isCurrentUserCanDeleteDepartmentManager()) {
			addUnauthorizedActionMessage();
			return null; 
		}
		getDomainService().deleteDepartmentManager(departmentManagerToUpdate);
		addInfoMessage(null, "DEPARTMENTS.MESSAGE.DEPARTMENT_MANAGER_DELETED", 
				departmentManagerToUpdate.getUser().getDisplayName(),
				departmentManagerToUpdate.getUser().getId(),
				departmentManagerToUpdate.getDepartment().getLabel());
		return "departmentManagerDeleted";
	}

	/**
	 * @return true if the current user can edit the department manager.
	 */
	public boolean isCurrentUserCanEditDepartmentManager() {
		return getDomainService().userCanEditDepartmentManagers(
				getCurrentUser(), getSessionController().getDepartment());
	}

	/**
	 * @return a String.
	 */
	public String updateDepartmentManager() {
		if (!isCurrentUserCanEditDepartmentManager()) {
			addUnauthorizedActionMessage();
			return null;
		}
		getDomainService().updateDepartmentManager(departmentManagerToUpdate);
		addInfoMessage(null, "DEPARTMENTS.MESSAGE.DEPARTMENT_MANAGER_UPDATED", 
				departmentManagerToUpdate.getUser().getDisplayName(),
				departmentManagerToUpdate.getUser().getId(),
				departmentManagerToUpdate.getDepartment().getLabel());
		return "departmentManagerUpdated";
	}

	/**
	 * @return a String
	 */
	public String confirmDeleteDepartment() {
		if (!isCurrentUserCanDeleteDepartment()) {
			addUnauthorizedActionMessage();
			return null;
		}
		Department currentDepartment = getSessionController().getDepartment();
		getDomainService().deleteDepartment(currentDepartment);
		addInfoMessage(null, "DEPARTMENTS.MESSAGE.DEPARTMENT_DELETED", currentDepartment.getLabel());
		getSessionController().setDepartment(null);
		return "departmentDeleted";
	}

	/**
	 * @return the departmentToAdd
	 */
	public Department getDepartmentToAdd() {
		return departmentToAdd;
	}

	/**
	 * @return the departmentToUpdate
	 */
	public Department getDepartmentToUpdate() {
		return departmentToUpdate;
	}

	/**
	 * @param departmentToUpdate the departmentToUpdate to set
	 */
	public void setDepartmentToUpdate(final Department departmentToUpdate) {
		this.departmentToUpdate = new Department(departmentToUpdate);
	}

	/**
	 * @return the departmentManagerToUpdate
	 */
	public DepartmentManager getDepartmentManagerToUpdate() {
		return departmentManagerToUpdate;
	}

	/**
	 * @param departmentManagerToUpdate the departmentManagerToUpdate to set
	 */
	public void setDepartmentManagerToUpdate(
			final DepartmentManager departmentManagerToUpdate) {
		this.departmentManagerToUpdate = new DepartmentManager(departmentManagerToUpdate);
	}

	/**
	 * @return the departmentManagerToAdd
	 */
	public DepartmentManager getDepartmentManagerToAdd() {
		return departmentManagerToAdd;
	}

	/**
	 * @return the ldapUid
	 */
	public String getLdapUid() {
		return ldapUid;
	}

	/**
	 * @param ldapUid the ldapUid to set
	 */
	public void setLdapUid(final String ldapUid) {
		this.ldapUid = ldapUid;
	}

	/**
	 * @param ldapService the ldapService to set
	 */
	public void setLdapService(final LdapService ldapService) {
		this.ldapService = ldapService;
	}

}
