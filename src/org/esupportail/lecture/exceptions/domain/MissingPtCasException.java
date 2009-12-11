/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.exceptions.domain;

/**
 * 
 * The Proxy Ticket CAS is missing
 * @author gbouteil
 */
public class MissingPtCasException extends InfoDomainException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @param cause
	 */
	public MissingPtCasException(Exception cause) {
		super(cause);
	}
	/**
	 * @param message
	 */
	public MissingPtCasException(String message) {
		super(message);
	}

	

}
