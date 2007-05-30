/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.DomainTools;

/**
 * @author bourges
 * an implementation of ExternalService for tests
 */
public class ExternalServiceTest implements ExternalService {

	/*
	 *************************** PROPERTIES ******************************** */	
	/**
	 * Log instance 
	 */
	protected static final Log log = LogFactory.getLog(ExternalServiceTest.class); 

	/*
	 *************************** INIT ******************************** */	

	/*
	 *************************** METHODS ******************************** */	

	
	/**
	 * @see org.esupportail.lecture.domain.ExternalService#getConnectedUserId()
	 */
	public String getConnectedUserId() {
		return getUserAttribute(DomainTools.getUserID());
	}

	/**
	 * @see org.esupportail.lecture.domain.ExternalService#getCurrentContextId()
	 */
	public String getCurrentContextId() {
		return getPreferences(DomainTools.CONTEXT);
	}

	/**
	 * @see org.esupportail.lecture.domain.ExternalService#getPreferences(java.lang.String)
	 */
	public String getPreferences(String name) {
		if (name.equalsIgnoreCase(DomainTools.CONTEXT)){
			return "c1";
		}
		return null;
	}

	/**
	 * @see org.esupportail.lecture.domain.ExternalService#getUserAttribute(java.lang.String)
	 */
	public String getUserAttribute(String attribute) {
		if (attribute.equalsIgnoreCase(DomainTools.getUserID())){
			return "bourges";
		}else if (attribute.equalsIgnoreCase("")){
			return "";
		}else if(attribute.equalsIgnoreCase("sn")){
			return "User";
		}
		return "";
	}

	/**
	 * @see org.esupportail.lecture.domain.ExternalService#getUserProxyTicketCAS()
	 */
	public String getUserProxyTicketCAS() {
		// non encore utilisée
		return null;
	}

	/**
	 * @see org.esupportail.lecture.domain.ExternalService#isUserInGroup(java.lang.String)
	 */
	public boolean isUserInGroup(String group) {
		if(group.equalsIgnoreCase("")){
			return false;
		}else if(group.equalsIgnoreCase("local.0")){
			return true;
		}
		return false;
		
	}
	
	/* 
	 ************************* ACCESSORS ******************************** */	


}
