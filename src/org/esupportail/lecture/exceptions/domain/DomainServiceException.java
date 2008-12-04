/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.exceptions.domain;

import org.esupportail.lecture.exceptions.LectureException;


/**
 * Exceptions throwed by domain services, in order to be catched by calling layer
 * @author gbouteil
 */
public class DomainServiceException extends LectureException {
	
	/**
	 * Default constructor 
	 */
	public DomainServiceException() {
		super();
	}
	/**
	 * @param message
	 */
	public DomainServiceException(String message) {
		super(message);
	}
	/**
	 * @param cause
	 */
	public DomainServiceException(Exception cause) {
		super(cause);
	}
	/**
	 * @param message
	 * @param cause
	 */
	public DomainServiceException(String message,Exception cause) {
		super(message,cause);
	}
}
