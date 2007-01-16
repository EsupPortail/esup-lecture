/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.exceptions.domain;

/**
 * @author gbouteil
 * The category is not visible for requesting user
 */
public class CategoryNotVisibleException extends InfoDomainException {

	/**
	 * @param e
	 */
	public CategoryNotVisibleException(Exception e) {
		super(e);
	}
	/**
	 * @param message
	 */
	public CategoryNotVisibleException(String message) {
		super(message);
	}

}
