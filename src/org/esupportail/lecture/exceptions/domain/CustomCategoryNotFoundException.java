/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.exceptions.domain;

/**
 * @author gbouteil
 * The CustomCategory is not found in UserPRofile
 */
public class CustomCategoryNotFoundException extends CustomElementNotFoundException {

	/**
	 * @param string
	 */
	public CustomCategoryNotFoundException(String string) {
		super(string);
	}

	
}
