/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.exceptions.domain;


/**
 * @author gbouteil
 * There is an error in xml channel config file
 */
public class ChannelConfigException extends XMLFileException {

	/**
	 * @param errorMsg
	 */
	public ChannelConfigException(String errorMsg) {
		super(errorMsg);
	}

	/**
	 * @param errorMsg
	 * @param e
	 */
	public ChannelConfigException(String errorMsg, Exception e) {
		super(errorMsg,e);
	}

	

}
