/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.utils;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.commons.services.authentication.AuthenticationService;
import org.esupportail.commons.utils.Assert;
import org.esupportail.lecture.domain.DomainTools;
import org.esupportail.lecture.exceptions.domain.NoExternalValueException;
import org.esupportail.portal.ws.client.PortalService;
import org.esupportail.portal.ws.client.exceptions.PortalGroupNotFoundException;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author gbouteil
 * Access to external in serlvet mode
 */
public class ServletService implements ModeService, InitializingBean {

	/*
	 ************************** PROPERTIES ******************************** */	
	/**
	 * Log instance. 
	 */
	protected static final Log LOG = LogFactory.getLog(ServletService.class);
	/**
	 * Portal service.
	 */
	private PortalService portalService;
	/**
	 * Authentication service.
	 */
	private AuthenticationService authenticationService;

	/* 
	 ************************** INIT ****************************************/
	/**
	 * Default constructor.
	 */
	public ServletService() {
		super();
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(portalService, "property portalService of class " 
				+ this.getClass().getName() + " can not be null");
		Assert.notNull(authenticationService, "property authenticationService of class " 
				+ this.getClass().getName() + " can not be null");
	}


	/* 
	 ************************** METHODS *************************************/

	/**
	 * @see org.esupportail.lecture.domain.utils.ModeService#getPreference(java.lang.String)
	 */
	public String getPreference(final String name) {
		String ret = name;
		if (name.equals(DomainTools.CONTEXT)) {
			ret = "default";
		}
		return ret;
	}

	/**
	 * @see org.esupportail.lecture.domain.utils.ModeService#getUserAttribute(java.lang.String)
	 */
	public String getUserAttribute(final String attribute) throws NoExternalValueException {
		String ret = null;
		String userId = authenticationService.getCurrentUserId();
		List<String> attributeList = portalService.getUserAttributeValues(userId, attribute);
		if (attributeList != null && attributeList.size() >= 1) {
			ret = attributeList.get(0);
			if (attributeList.size() > 1) {
				LOG.warn("getUserAttribute(" + attribute + ") for userId " + userId
						+ "return more than 1 value. Just first one is used !");
			}
		}
		if (ret == null) {
			throw new NoExternalValueException("User Attribute \""
				+ attribute + "\" not found ! See your portal attributes definition " 
				+ "(with ant test-portal for example)"
				+ " and verify your \"regular\" tags in esup-lecture.xml config file.");
		}
		return ret;
	}

	/**
	 * @see org.esupportail.lecture.domain.utils.ModeService#isUserInGroup(java.lang.String)
	 */
	public boolean isUserInGroup(final String group) {
		String userId = authenticationService.getCurrentUserId();
		boolean ret = false;
		try {
			ret = portalService.isUserMemberOfGroup(userId, group);			
		} catch (PortalGroupNotFoundException e) {
			LOG.info("Group " + group + " not found. Return false in isUserInGroup().");
		}
		return ret;
	}


	/* 
	 ************************** ACCESSORS *************************************/
	/**
	 * @param portalService the portalService to set
	 */
	public void setPortalService(final PortalService portalService) {
		this.portalService = portalService;
	}

	/**
	 * @param authenticationService the authenticationService to set
	 */
	public void setAuthenticationService(final AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}


	


}
