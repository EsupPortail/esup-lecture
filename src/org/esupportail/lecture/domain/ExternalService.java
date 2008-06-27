/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain;

import org.esupportail.lecture.exceptions.domain.InfoDomainException;
import org.esupportail.lecture.exceptions.domain.InternalExternalException;
import org.esupportail.lecture.exceptions.domain.NoExternalValueException;

/**
 * @author bourges
 * external service interface
 */
public interface ExternalService {

	/**
	 * Return ID of the connected user
	 * @return String
	 * @throws InternalExternalException 
	 * @throws NoExternalValueException 
	 */
	public String getConnectedUserId() throws NoExternalValueException, InternalExternalException;

	/**
	 * Return ID of the current context (from channel instantiation)
	 * @return string
	 * @throws InternalExternalException 
	 * @throws NoExternalValueException 
	 * @see FacadeService#getCurrentContextId()
	 */
	public String getCurrentContextId() throws NoExternalValueException, InternalExternalException;
	/**
	 * Return the proxy ticket CAS of the connected user
	 * @param casTargetService - CAS target service
	 * @return the proxy ticket CAS
	 * @throws InfoDomainException 
	 */
	public String getUserProxyTicketCAS(String casTargetService) throws InfoDomainException;

	/**
	 * Return true if the connected user of the is in the "group" defined in the external service
	 * @param group
	 * @return true or false
	 * @throws InternalExternalException 
	 */
	public boolean isUserInGroup(String group) throws InternalExternalException;

	/**
	 * Get attribute value from the external service for connected user
	 * @param attribute
	 * @return the attribute value defined by "attributeNAme"
	 * @throws InternalExternalException 
	 * @throws NoExternalValueException 
	 */
	public String getUserAttribute(String attribute) throws NoExternalValueException, InternalExternalException;

	/**
	 * Get preference value by given the preference name from channel instantiation
	 * @param name name of the preference
	 * @return the value of the preference
	 * @throws InternalExternalException 
	 * @throws NoExternalValueException 
	 */
	public String getPreferences(String name) throws NoExternalValueException, InternalExternalException;

}
