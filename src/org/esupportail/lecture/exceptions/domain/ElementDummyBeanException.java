/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.exceptions.domain;

/**
 * A method is called on an element bean that is in fact a element dummy bean
 * @author gbouteil
 */
public class ElementDummyBeanException extends InfoDomainException {

	/**
	 * @param string
	 */
	public ElementDummyBeanException(String string) {
		super(string);
	}
	/**
	 * @param e
	 */
	public ElementDummyBeanException(Exception e) {
		super(e);
	}
	/**
	 * @param string
	 * @param e
	 */
	public ElementDummyBeanException(String string,Exception e) {
		super(string,e);
	}


}
