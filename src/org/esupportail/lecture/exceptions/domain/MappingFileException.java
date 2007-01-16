/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.exceptions.domain;


/**
 * @author gbouteil
 * There is an error in xml mapping file
 */
public class MappingFileException extends XMLFileException {

	/**
	 * @param string
	 */
	public MappingFileException(String string) {
		super(string);
	}

	/**
	 * @param string
	 * @param e
	 */
	public MappingFileException(String string, Exception e) {
		super(string,e);
	}

}
