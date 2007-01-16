/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.DomainServiceImpl;

/**
 * @author gbouteil
 * Access to external in serlvet mode
 */
// TODO (GB/RB) make better
public class ServletService implements ModeService {

	/*
	 ************************** PROPERTIES ******************************** */	
	/**
	 * Log instance 
	 */
	protected static final Log log = LogFactory.getLog(DomainServiceImpl.class);

	/* 
	 ************************** INIT ****************************************/

	/* 
	 ************************** METHODS *************************************/

	/**
	 * @see org.esupportail.lecture.domain.utils.ModeService#getPreference(java.lang.String)
	 */
	public String getPreference(String name) {
//		default return value in case of serlvet use case (not normal mode)
		return name;
	}

	/**
	 * @see org.esupportail.lecture.domain.utils.ModeService#getUserAttribute(java.lang.String)
	 */
	public String getUserAttribute(String attribute) {
//		default return value in case of serlvet use case (not normal mode)
		return attribute;
	}

	/**
	 * @see org.esupportail.lecture.domain.utils.ModeService#isUserInGroup(java.lang.String)
	 */
	public boolean isUserInGroup(String group) {
//		default return value in case of serlvet use case (not normal mode)
		return false;
	}
	
	/* 
	 ************************** ACCESSORS *************************************/


}
