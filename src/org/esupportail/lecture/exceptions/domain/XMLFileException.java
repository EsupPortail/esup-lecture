/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.exceptions.domain;


/**
 * There is an error in xml file
 * @author gbouteil
 */
public class XMLFileException extends PrivateException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param errorMsg
	 */
	public XMLFileException(String errorMsg) {
		super(errorMsg);
	}

	/**
	 * @param errorMsg
	 * @param e
	 */
	public XMLFileException(String errorMsg, Exception e) {
		super(errorMsg,e);
	}

}
