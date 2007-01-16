/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.exceptions.domain;

/**
 * @author gbouteil
 * The CustomSource is not found in UserProfile
 */
public class CustomSourceNotFoundException extends CustomElementNotFoundException {

	/**
	 * @param string
	 */
	public CustomSourceNotFoundException(String string) {
		super(string);
	}

}
