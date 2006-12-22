package org.esupportail.lecture.domain;

import org.esupportail.lecture.domain.beans.UserBean;
import org.esupportail.lecture.exceptions.domain.InternalExternalException;
import org.esupportail.lecture.exceptions.domain.NoExternalValueException;

/**
 * @author bourges
 * external (portlet or servlet) service interface
 */
public interface ExternalService {

	/**
	 * @return String
	 * @throws InternalExternalException 
	 * @throws NoExternalValueException 
	 * @see FacadeService#getConnectedUser()
	 */
	public String getConnectedUserId() throws NoExternalValueException, InternalExternalException;

	/**
	 * @return string
	 * @throws InternalExternalException 
	 * @throws NoExternalValueException 
	 * @see FacadeService#getCurrentContextId()
	 */
	public String getCurrentContextId() throws NoExternalValueException, InternalExternalException;
	/**
	 * Return the proxy ticket CAS of the user
	 * @return the proxy ticket CAS
	 */
	public String getUserProxyTicketCAS();

	/**
	 * Return true if the current user of the is in "group" of the external service
	 * @param group
	 * @return true or false
	 * @throws InternalExternalException 
	 */
	public boolean isUserInGroup(String group) throws InternalExternalException;

	/**
	 * Get attribute value from the external service
	 * @param attribute
	 * @return the attribute value defined by "attributeNAme"
	 * @throws InternalExternalException 
	 * @throws NoExternalValueException 
	 */
	public String getUserAttribute(String attribute) throws NoExternalValueException, InternalExternalException;

	/**
	 * Get preference value by given the preference name
	 * @param name name of the preference
	 * @return the value of the preference
	 * @throws InternalExternalException 
	 * @throws NoExternalValueException 
	 */
	public String getPreferences(String name) throws NoExternalValueException, InternalExternalException;

}
