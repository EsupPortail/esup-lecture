/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.exceptions.domain;

/**
 * @author gbouteil
 * An error occured when computing element features
 */
public class ComputeFeaturesException extends PrivateException {

	/**
	 * @param e
	 */
	public ComputeFeaturesException(CategoryNotLoadedException e) {
		super(e);
	}

	/**
	 * @param string
	 * @param e
	 */
	public ComputeFeaturesException(String string, CategoryNotLoadedException e) {
		super(string,e);
	}

}
