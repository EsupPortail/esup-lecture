/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-lecture.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.exceptions.domain;
/**
 * The user is not subscribed to Category requested
 * @author gbouteil
 *
 */
public class ContextNotDefinedInUserProfileException extends Exception {

	/**
	 * @param errorMsg
	 * @param e
	 */
	public ContextNotDefinedInUserProfileException(String errorMsg, CustomContextNotFoundException e) {
		super(errorMsg,e);
	}
	
}
