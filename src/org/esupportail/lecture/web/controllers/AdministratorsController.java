/**
 * ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
 * http://sourcesup.cru.fr/projects/esup-lecture
 */
package org.esupportail.lecture.web.controllers; 

import java.util.ArrayList;
import java.util.List;

import org.esupportail.lecture.domain.beans.User;
import org.esupportail.commons.exceptions.UserNotFoundException;
import org.esupportail.commons.services.ldap.LdapService;
import org.esupportail.commons.web.controllers.LdapSearchCaller;
import org.springframework.util.Assert;

/**
 * Bean to present and manage administrators.
 */
public class AdministratorsController extends AbstractContextAwareController implements LdapSearchCaller {

	/**
	 * The id of the user to give administrator privileges.
	 */
	private String ldapUid;

	/**
	 * The user of whom the administrator's privileges will be revoked.
	 */
	private User userToDelete;
	
	/**
	 * The LDAP service.
	 */
	private LdapService ldapService;

	/**
	 * Bean constructor.
	 */
	public AdministratorsController() {
		super();
	}

	/**
	 * @see org.esupportail.lecture.web.controllers.AbstractContextAwareController#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		Assert.notNull(ldapService, 
				"property ldapService of class " + getClass().getName() 
				+ " can not be null");
	}

	/**
	 * @see org.esupportail.commons.web.controllers.Resettable#reset()
	 */
	public void reset() {
		ldapUid = null;
		userToDelete = null;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getClass().getSimpleName() + "#" + hashCode() + "[ldapUid=[" + ldapUid 
		+ "], userToDelete=" + userToDelete + "]";
	}

	/**
	 * @return true if the current user is allowed to access the view.
	 */
	public boolean isPageAuthorized() {
		User currentUser = getCurrentUser();
		if (currentUser == null) {
			return false;
		}
		return getDomainService().userCanViewAdmins(currentUser);
	}

	/**
	 * @return true if the current user can add an admin.
	 */	
	public boolean isCurrentUserCanAddAdmin() {
		return getDomainService().userCanAddAdmin(getCurrentUser());
	}

	/**
	 * @return true if the current user can delete an admin.
	 */	
	public boolean isCurrentUserCanDeleteAdmin() {
		return getDomainService().userCanDeleteAdmin(getCurrentUser(), userToDelete);
	}

	/**
	 * @return the list of the administrators.
	 */
	public List <User> getAdmins() {
		return getDomainService().getAdmins();
	}

	/**
	 * @return the LDAP statistics.
	 */
	public List <String> getLdapStatistics() {
		if (!ldapService.supportStatistics()) {
			return new ArrayList<String>();
		}
		return ldapService.getStatistics(getSessionController().getLocale());
	}

	/**
	 * @return a String.
	 */
	public String addAdmin() {
		if (!isCurrentUserCanAddAdmin()) {
			addUnauthorizedActionMessage();
			return null;
		}
		try {
			User user = getDomainService().getUser(ldapUid);
			if (user.getAdmin()) {
				addErrorMessage(
						null, "ADMINISTRATORS.MESSAGE.USER_ALREADY_ADMINISTRATOR", ldapUid);
				return null;
			}
			getDomainService().addAdmin(user);
			ldapUid = "";
			addInfoMessage(null, "ADMINISTRATORS.MESSAGE.ADMIN_ADDED", user.getDisplayName(), user.getId());
			return "adminAdded";
		} catch (UserNotFoundException e) {
			addWarnMessage("ldapUid", "_.MESSAGE.USER_NOT_FOUND", ldapUid);
			return null;
		}
	}
	
	/**
	 * @return a String.
	 */
	public String confirmDeleteAdmin() {
		if (!isCurrentUserCanDeleteAdmin()) {
			addUnauthorizedActionMessage();
			return null;
		}
		getDomainService().deleteAdmin(userToDelete);
		addInfoMessage(null, "ADMINISTRATORS.MESSAGE.ADMIN_DELETED", 
				userToDelete.getDisplayName(), userToDelete.getId());
		return "adminDeleted";
	}

	/**
	 * @param userToDelete the userToDelete to set
	 */
	public void setUserToDelete(final User userToDelete) {
		this.userToDelete = userToDelete;
	}

	/**
	 * @return the userToDelete
	 */
	public User getUserToDelete() {
		return userToDelete;
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
