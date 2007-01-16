/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.exceptions.domain;

/**
 * @author gbouteil 
 * Exceptions throwed by domain services, in order to be catched by calling layer
 * This exception does not provide any information about exception type :
 * This kind of exception is used as so much general : there is not any subclasses
 * (It is used in opposite to InfoDomainException) 
 * 
 */
public class InternalDomainException extends DomainServiceException {

	/**
	 * @param e
	 */
	public InternalDomainException(Exception e) {
		super(e);
	}

	/**
	 * @param errorMsg
	 * @param e
	 */
	public InternalDomainException(String errorMsg, Exception e) {
		super(errorMsg,e);
	}

                                                                                                                                                                                                                                                                                                                                                                   
	
}
