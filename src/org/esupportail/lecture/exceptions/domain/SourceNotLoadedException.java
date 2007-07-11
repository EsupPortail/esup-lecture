/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.exceptions.domain;


/**
 * The Source is not loaded in SourceProfile from remote URL 
 * @author gbouteil
 */
public class SourceNotLoadedException extends ElementNotLoadedException {

	/**
	 * @param string
	 */
	public SourceNotLoadedException(String string) {
		super(string);
	}

	/**
	 * @param e
	 */
	public SourceNotLoadedException(Exception e) {
		super(e);
	}

	/**
	 * @param errorMsg
	 * @param e
	 */
	public SourceNotLoadedException(String errorMsg, Exception e) {
		super(errorMsg,e);
	}

}
