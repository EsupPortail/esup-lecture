/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.exceptions.domain;

/**
 * @author gbouteil
 * Exception throwed by domain layer that never catch by other layer.
 * These are private exception for doamin layer
 */
public class PrivateException extends Exception {
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
