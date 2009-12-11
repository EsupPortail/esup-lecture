/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.exceptions.domain;

/**
 * Exception throwed by domain layer that never catch by other layer.
 * These are private exception for domain layer
 * @author gbouteil
 */
public class PrivateException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param e
	 */
	public PrivateException(Exception e) {
		super(e);
	}
	
	/**
	 * @param message
	 */
	public PrivateException(String message) {
		super(message);
	}
	
	/**
	 * @param message
	 * @param e
	 */
	public PrivateException(String message,Exception e) {
		super(message,e);
	}
	
	/**
	 * Default constructor
	 */
	public PrivateException(){
		super();
	}
}
