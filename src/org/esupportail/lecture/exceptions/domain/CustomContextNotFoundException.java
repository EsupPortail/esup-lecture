/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.exceptions.domain;

/**
 * The CustomContext is not found in UserProfile
 * @author gbouteil
 */
public class CustomContextNotFoundException extends
		CustomElementNotFoundException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param string
	 */
	public CustomContextNotFoundException(String string) {
		super(string);
	}

}
