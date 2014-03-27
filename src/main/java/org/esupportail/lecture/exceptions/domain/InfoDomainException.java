/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.exceptions.domain;


/**
 * Exceptions throwed by domain services, in order to be catched by calling layer.
 * This exception provides information about exception type :
 * This kind of exception is not used as so much general : it is sub-classes of this that are used 
 * @author gbouteil
 *  
 */
public abstract class InfoDomainException extends DomainServiceException {

	private static final long serialVersionUID = 1L;
	/**
	 * Default Constructor.
	 */
	public InfoDomainException() {
		super();
	}
	/**
	 * @param cause
	 */
	public InfoDomainException(Exception cause) {
		super(cause);
	}
	/**
	 * @param message
	 */
	public InfoDomainException(String message) {
		super(message);
	}
	/**
	 * @param message
	 * @param cause
	 */
	public InfoDomainException(String message,Exception cause) {
		super(message,cause);
	}
	

}
