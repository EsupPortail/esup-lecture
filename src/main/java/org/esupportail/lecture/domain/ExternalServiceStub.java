/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author bourges
 * an implementation of ExternalService for domain tests
 */
public class ExternalServiceStub implements ExternalService {

	/*
	 *************************** PROPERTIES ******************************** */	
	
	/**
	 * Log instance.
	 */
	private static final Log LOG = LogFactory.getLog(ExternalServiceStub.class); 

	/*
	 *************************** INIT ******************************** */	
	/**
	 * Default constructor.
	 */
	public ExternalServiceStub() {
		super();
	}

	/*
	 *************************** METHODS ******************************** */	

	
	/**
	 * @see org.esupportail.lecture.domain.ExternalService#getConnectedUserId()
	 */
	public String getConnectedUserId() {
		return "bourges";
	}

	/**
	 * @see org.esupportail.lecture.domain.ExternalService#getCurrentContextId()
	 */
	public String getCurrentContextId() {
		return getPreferences(DomainTools.getContext());
	}

	/**
	 * @see org.esupportail.lecture.domain.ExternalService#getPreferences(java.lang.String)
	 */
	public String getPreferences(final String name) {
		String ret = null;
		if (name.equalsIgnoreCase(DomainTools.getContext())) {
			ret = "c1";
		}
		if (LOG.isTraceEnabled()) {
			LOG.trace("getPreferences(" + name + ") return " );
		}
		return ret;
	}

	/**
	 * @see org.esupportail.lecture.domain.ExternalService#getUserAttribute(java.lang.String)
	 */
	public String getUserAttribute(final String attribute) {
		String ret = null;
		if (attribute.equalsIgnoreCase("uid")) {
			ret = "bourges";
		} else if (attribute.equalsIgnoreCase("")) {
			ret = "";
		} else if (attribute.equalsIgnoreCase("sn")) {
			ret = "User";
		}
		return ret;
	}

	/** 
	 * @see org.esupportail.lecture.domain.ExternalService#getUserProxyTicketCAS(String)
	 */
	public String getUserProxyTicketCAS(@SuppressWarnings("unused") final String casTargetService) {
		// non encore utilisée
		return null;
	}

	/**
	 * @see org.esupportail.lecture.domain.ExternalService#isUserInGroup(java.lang.String)
	 */
	public boolean isUserInGroup(final String group) {
		boolean ret = false;
		if (group.equalsIgnoreCase("local.0")) {
			ret = true;
		}
		return ret;
		
	}
	
	/* 
	 ************************* ACCESSORS ******************************** */	


}
