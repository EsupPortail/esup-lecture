/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.exceptions.domain;

import org.esupportail.lecture.exceptions.dao.TimeoutException;

/**
 * @author gbouteil
 * Remote server providing source is in Time Out.
 * 
 */
public class SourceTimeOutException extends InfoDomainException {

	public SourceTimeOutException(String errorMsg, Exception e) {
		super(errorMsg,e);
	}
	

}
