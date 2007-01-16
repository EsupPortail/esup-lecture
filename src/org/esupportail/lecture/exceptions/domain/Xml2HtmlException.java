/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.exceptions.domain;


/**
 * @author gbouteil
 * Computing items of the source failed 
 * because an error "XML to HTML" occured
 */
public class Xml2HtmlException extends PrivateException {

	/**
	 * @param string
	 */
	public Xml2HtmlException(String string) {
		super(string);
	}

	/**
	 * @param string
	 * @param e
	 */
	public Xml2HtmlException(String string,Exception e) {
		super(string,e);
	}

}
