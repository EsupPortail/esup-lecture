/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.exceptions.domain;

/**
 * The user is not subscribed to Category requested
 * @author gbouteil
 *
 */
public class UserNotSubscribedToCategoryException extends InfoDomainException {

	/**
	 * @param errorMsg
	 * @param e
	 */
	public UserNotSubscribedToCategoryException(String errorMsg, CustomCategoryNotFoundException e) {
		super(errorMsg,e);
	}
	

}
