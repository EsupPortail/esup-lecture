/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-lecture.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.exceptions.domain;

/**
 * The category is not reachable for requesting user : it cannot be refered by a context reachable par user
 * @author gbouteil
 */
public class CategoryOutOfReachException extends InfoDomainException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @param e
	 */
	public CategoryOutOfReachException(Exception e) {
		super(e);
	}
	/**
	 * @param message
	 */
	public CategoryOutOfReachException(String message) {
		super(message);
	}

}
