package org.esupportail.lecture.domain.service;
/**
 * Interface to access portlet services.
 * @author gbouteil
 *
 */
public interface PortletService {
	
	/**
	 * Get attribute value from the Portlet request
	 * @param attributeName
	 * @return the attribute value defined by "attributeNAme"
	 */
	public String getUserAttribute(String attributeName);



	/**
	 * Get preference value by given the preference name
	 * @param name name of the preference
	 * @return the value of the preference
	 */
	public String getPreferences(String name);
	
	/**
	 * Return true if the current user of the is in "role" of the portlet container
	 * @param role
	 * @return true or false
	 */
	public boolean isUserInRole(String role);



	/**
	 * Return the proxy ticket CAS of the user
	 * @return the proxy ticket CAS
	 */
	public String getUserProxyTicketCAS();
}
