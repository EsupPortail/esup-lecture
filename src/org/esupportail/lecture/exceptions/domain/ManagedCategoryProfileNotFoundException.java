/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.exceptions.domain;


/**
 * @author gbouteil
 * The ManagedCategoryProfile is not found in Channel
 */
public class ManagedCategoryProfileNotFoundException extends CategoryProfileNotFoundException {

	/**
	 * @param string
	 */
	public ManagedCategoryProfileNotFoundException(String string) {
		super(string);
	}

}
