/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-lecture.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.exceptions.domain;

/**
 * Request impossible on an obliged category
 */
public class CategoryObligedException extends InfoDomainException {

	/**
	 * @param errorMsg
	 */
	public CategoryObligedException(String errorMsg) {
		super(errorMsg);
	}

}
