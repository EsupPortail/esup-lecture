/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.exceptions.domain;

/**
 * The CustomElement is not found in UserProfile
 * @author gbouteil
 */
public class CustomElementNotFoundException extends PrivateException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param string
	 */
	public CustomElementNotFoundException(String string) {
		super(string);
	}
}
