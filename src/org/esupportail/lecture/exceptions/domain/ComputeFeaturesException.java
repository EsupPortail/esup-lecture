/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.exceptions.domain;

/**
 * An error occured when computing element features
 * @author gbouteil
 */
public class ComputeFeaturesException extends PrivateException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param e
	 */
	public ComputeFeaturesException(ManagedCategoryNotLoadedException e) {
		super(e);
	}

	/**
	 * @param string
	 * @param e
	 */
	public ComputeFeaturesException(String string, ManagedCategoryNotLoadedException e) {
		super(string,e);
	}

}
