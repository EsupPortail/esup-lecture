/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.exceptions.domain;

/**
 * Exceptions throwed by external services, in order to be catched by calling layer.
 * This exception does not provide any information about exception type :
 * This kind of exception is used as so much general : there is not any subclasses
 * (It is used in opposite to InfoExternalException) 
 * @author gbouteil 
 * 
 */
public class InternalExternalException extends ExternalServiceException {

	/**
	 * @param e
	 */
	public InternalExternalException(Exception e) {
		super(e);
	}
	
	/**
	 * @param message
	 */
	public InternalExternalException(String message) {
		super(message);
	}
	
	/**
	 * @param message
	 * @param e
	 */
	public InternalExternalException(String message,Exception e) {
		super(message,e);
	}

}
