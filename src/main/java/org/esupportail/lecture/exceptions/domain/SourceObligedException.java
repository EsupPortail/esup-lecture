/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.exceptions.domain;

/**
 * Request impossible on an obliged source
 */
public class SourceObligedException extends InfoDomainException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param errorMsg
	 */
	public SourceObligedException(String errorMsg) {
		super(errorMsg);
	}

}
