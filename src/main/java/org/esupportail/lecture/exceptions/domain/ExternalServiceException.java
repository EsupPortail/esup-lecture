/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.exceptions.domain;


/**
 * Exceptions throwed by external services, in order to be catched by calling layer
 * @author gbouteil
 */
public class ExternalServiceException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param e
	 */
	public ExternalServiceException(Exception e) {
		super(e);
	}
	
	/**
	 * @param message
	 */
	public ExternalServiceException(String message) {
		super(message);
	}
	
	/**
	 * @param message
	 * @param e
	 */
	public ExternalServiceException(String message,Exception e) {
		super(message,e);
	}
}
