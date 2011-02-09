/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.exceptions.domain;


/**
 * The Context is not found in Channel
 * @author gbouteil
 */
public class ContextNotFoundException extends ElementNotFoundException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
