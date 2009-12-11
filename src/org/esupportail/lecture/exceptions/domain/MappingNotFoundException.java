/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.exceptions.domain;

/**
 * The mapping is not found 
 * @author gbouteil
 */
public class MappingNotFoundException extends PrivateException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param e
	 */
	public MappingNotFoundException(Exception e) {
		super(e);
	}

	/**
	 * @param string
	 */
	public MappingNotFoundException(String string) {
		super(string);
	}

	/**
	 * 
	 */
	public MappingNotFoundException() {
		super();
	}

}
