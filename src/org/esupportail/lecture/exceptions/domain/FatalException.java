/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.exceptions.domain;


/**
 * Exception throwed when a fatal error occured
 * @author gbouteil
 *
 */
public class FatalException extends InfoDomainException {
	/**
	 * Default constructor
	 */
	public FatalException(){
		super();
	}
	/**
	 * @param msg
	 */
	public FatalException(String msg){
		super(msg);
	}
	/**
	 * @param errorMsg
	 * @param e
	 */
	public FatalException(String errorMsg, Exception e) {
		super(errorMsg,e);
	}
}
