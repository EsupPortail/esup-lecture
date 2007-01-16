/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.exceptions.domain;


/**
 * @author gbouteil
 * An error occured when computing items of a source
 */
public class ComputeItemsException extends PrivateException {

	/**
	 * @param string
	 */
	public ComputeItemsException(String string) {
		super(string);
	}

	/**
	 * @param string
	 * @param e
	 */
	public ComputeItemsException(String string,Exception e) {
		super(string,e);
	}


}
