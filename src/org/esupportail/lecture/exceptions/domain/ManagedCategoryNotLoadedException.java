/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.exceptions.domain;

/**
 * The category is not loaded in CategoryProfile from remote URL
 * @author gbouteil
 */
public class ManagedCategoryNotLoadedException extends ElementNotLoadedException {

	/**
	 * @param string
	 */
	public ManagedCategoryNotLoadedException(String string) {
		super(string);
	}

	/**
	 * @param string
	 */
	public ManagedCategoryNotLoadedException(String string, Exception cause) {
		super(string,cause);
	}


}
