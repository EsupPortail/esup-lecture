/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.exceptions.domain;

/**
 * @author gbouteil
 * The ele;ent visibility for user is not found
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
	public VisibilityNotFoundException(ElementNotLoadedException e) {
		super(e);
	}

}
