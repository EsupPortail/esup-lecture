package org.esupportail.lecture.domain;

import org.esupportail.lecture.domain.beans.UserBean;

/**
 * @author bourges
 * external (portlet or servlet) service interface
 */
public interface ExternalService {

	/**
	 * @return String
	 * @see FacadeService#getConnectedUser()
	 */
	public String getConnectedUserId();

	/**
	 * @return string
	 * @see FacadeService#getCurrentContextId()
	 */
	public String getCurrentContextId();
	/**
	 * Return the proxy ticket CAS of the user
	 * @return the proxy ticket CAS
	 */
	public String getUserProxyTicketCAS();

	/**
	 * Return true if the current user of the is in "group" of the external service
	 * @param group
	 * @return true or false
	 */
	public boolean isUserInGroup(String group);

	/**
	 * Get attribute value from the external service
	 * @param attribute
	 * @return the attribute value defined by "attributeNAme"
	 */
	public String getUserAttribute(String attribute);

	/**
	 * Get preference value by given the preference name
	 * @param name name of the preference
	 * @return the value of the preference
	 */
	public String getPreferences(String name);

}
