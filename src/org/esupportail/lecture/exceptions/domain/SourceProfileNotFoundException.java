/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.exceptions.domain;

/**
 * The sourceProfile is not found in Category
 * @author gbouteil
 */
public class SourceProfileNotFoundException extends ElementNotFoundException {

	/**
	 * @param errorMsg
	 */
	public SourceProfileNotFoundException(String errorMsg) {
		super(errorMsg);
	}

}
