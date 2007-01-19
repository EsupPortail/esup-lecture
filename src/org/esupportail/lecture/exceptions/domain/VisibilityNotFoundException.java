/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.exceptions.domain;

/**
 * The element visibility for user is not found
 * @author gbouteil
 */
public class VisibilityNotFoundException extends PrivateException {

	/**
	 * @param string
	 */
	public VisibilityNotFoundException(String string) {
		super(string);
	}

	/**
	 * @param e
	 */
	public VisibilityNotFoundException(Exception e) {
		super(e);
	}

	/**
	 * @param errorMsg
	 * @param e
	 */
	public VisibilityNotFoundException(String errorMsg, Exception e) {
		super(errorMsg,e);
	}

}
