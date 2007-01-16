/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.utils;

import org.esupportail.lecture.exceptions.domain.InternalExternalException;
import org.esupportail.lecture.exceptions.domain.NoExternalValueException;

/**
 * @author gbouteil
 * Interface to access external container
 */
public interface ModeService {

	/**
	 * Get preference value by given the preference name
	 * @param name name of the preference
	 * @return the value of the preference
	 * @throws InternalExternalException 
	 * @throws NoExternalValueException 
	 */
	public String getPreference(String name) throws InternalExternalException,NoExternalValueException;
	/**
	 * Get attribute value from the external service
	 * @param attribute
	 * @return the attribute value defined by "attributeNAme"
	 * @throws NoExternalValueException 
	 * @throws InternalExternalException 
	 */
	public String getUserAttribute(String attribute) throws InternalExternalException, NoExternalValueException;
	/**
	 * Return true if the current user of the is in "group" of the external service
	 * @param group
	 * @return true or false
	 * @throws InternalExternalException 
	 */
	public boolean isUserInGroup(String group) throws InternalExternalException;

}
