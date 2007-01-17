/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.exceptions.domain;

/**
 * Exceptions throwed by external services, in order to be catched by calling layer
 * This exception provides information about exception type :
 * This kind of exception is not used as so much general : it is sub-classes of this that are used 
 * @author gbouteil
 * 
 */
public class InfoExternalException extends ExternalServiceException {
	/**
	 * @param e
	 */
	public InfoExternalException(Exception e) {
		super(e);
	}
	
	/**
	 * @param message
	 */
	public InfoExternalException(String message) {
		super(message);
	}
	
	/**
	 * @param message
	 * @param e
	 */
	public InfoExternalException(String message,Exception e) {
		super(message,e);
	}
}
