/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.exceptions.domain;


/**
 * @author gbouteil
 * The Context is not found in Channel
 */
public class ContextNotFoundException extends ElementNotFoundException {

	/**
	 * @param string
	 */
	public ContextNotFoundException(String string) {
		super(string);
	}

	/**
	 * 
	 */
	public ContextNotFoundException() {
		super();
	}
	

}
