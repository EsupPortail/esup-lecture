/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.exceptions.domain;

/**
 * @author gbouteil
 * There is not any value returned from external service
 */
public class NoExternalValueException extends InternalExternalException {
	/**
	 * @param e
	 */
	public NoExternalValueException(Exception e) {
		super(e);
	}
	
	/**
	 * @param message
	 */
	public NoExternalValueException(String message) {
		super(message);
	}
	
	/**
	 * @param message
	 * @param e
	 */
	public NoExternalValueException(String message,Exception e) {
		super(message,e);
	}
}
