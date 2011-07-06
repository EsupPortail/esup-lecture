/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.utils;

import org.esupportail.commons.services.cas.CasException;
import org.esupportail.lecture.exceptions.domain.InternalExternalException;
import org.esupportail.lecture.exceptions.domain.NoExternalValueException;

/**
 * @author gbouteil
 * Interface to access external container
 */
public interface ModeService {

	/**
	 * Get preference value by given the preference name.
	 * @param name name of the preference
	 * @return the value of the preference
	 * @throws InternalExternalException 
	 * @throws NoExternalValueException 
	 */
	String getPreference(String name) throws InternalExternalException, NoExternalValueException;
	/**
	 * Get attribute value from the external service.
	 * @param attribute
	 * @return the attribute value defined by "attributeName"
	 * @throws InternalExternalException 
	 * @throws NoExternalValueException 
	 */
	String getUserAttribute(String attribute) throws InternalExternalException, NoExternalValueException;
	/**
	 * Return true if the current user of the is in "group" of the external service.
	 * @param group
	 * @return true or false
	 * @throws InternalExternalException 
	 */
	boolean isUserInGroup(String group) throws InternalExternalException;

	/**
	 * @param targetService The service the PT should be sent to.
	 * @return a PT.
	 * @throws CasException 
	 */	
	public String getUserProxyTicketCAS(final String casTargetService);

}
