/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.exceptions.domain;

/**
 * Size provided for tree in context display is wrong
 * Bounds are :  0 <= size <= 100
 * @author gbouteil
 */
// TODO (GB later) see bound in external file
public class TreeSizeErrorException extends InfoDomainException {

	/**
	 * @param cause
	 */
	public TreeSizeErrorException(Exception cause) {
		super(cause);
	}
	/**
	 * @param message
	 */
	public TreeSizeErrorException(String message) {
		super(message);
	}
	/**
	 * @param message
	 * @param cause
	 */
	public TreeSizeErrorException(String message, Exception cause) {
		super(message,cause);
	}

}
