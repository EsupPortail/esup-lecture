/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.utils;

import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.commons.utils.Assert;
import org.esupportail.lecture.exceptions.domain.InfoExternalException;
import org.esupportail.lecture.exceptions.domain.InternalExternalException;
import org.esupportail.lecture.exceptions.domain.NoExternalValueException;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.Cas20ProxyTicketValidator;
import org.jasig.cas.client.validation.TicketValidationException;
import org.jasig.cas.client.validation.TicketValidator;
import org.springframework.beans.factory.InitializingBean;


/**
 * @author gbouteil
 * Access to external in portlet mode
 */
public class PortletService implements ModeService, InitializingBean {


	/*
	 ************************** PROPERTIES ******************************** */	
	/**
	 * Log instance.
	 */
	private static final Log LOG = LogFactory.getLog(PortletService.class);
	
	private TicketValidator ticketValidator;
	
	private String service; 

	/* 
	 ************************** INIT ****************************************/
	/**
	 * Default constructor.
	 */
	public PortletService() {
		super();
	}

	/* 
	 ************************** METHODS *************************************/

	/**
	 * @param ticketValidator the ticketValidator to set
	 */
	public void setTicketValidator(TicketValidator ticketValidator) {
		this.ticketValidator = ticketValidator;
	}

	/**
	 * @param service the service to set
	 */
	public void setService(String service) {
		this.service = service;
	}

	/**
	 * @throws InternalExternalException 
	 * @throws InternalExternalException,NoExternalValueException  
	 * @throws NoExternalValueException 
	 * @see org.esupportail.lecture.domain.utils.ModeService#getPreference(java.lang.String)
	 */
	public String getPreference(final String name) throws InternalExternalException, NoExternalValueException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("getPreference(" + name + ")");
		}
		String value;
		try {
			final FacesContext facesContext = FacesContext.getCurrentInstance();
			final ExternalContext externalContext = facesContext.getExternalContext();
			final PortletRequest request = (PortletRequest) externalContext.getRequest();
			final PortletPreferences portletPreferences = request.getPreferences();
			value = portletPreferences.getValue(name, "default");
		} catch (Exception e) {
			throw new InternalExternalException(e);
		}
		if (value == null) {
			throw new NoExternalValueException("No value for portlet preference '"
				+ name + "' returned by external service");
		}
		return value;
		
	}

	/**
	 * @throws InternalExternalException,NoExternalValueException 
	 * @throws NoExternalValueException 
	 * @see org.esupportail.lecture.domain.utils.ModeService#getUserAttribute(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public String getUserAttribute(final String attribute) 
	throws InternalExternalException, NoExternalValueException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("getUserAttribute(" + attribute + ")");
		}
		String value; 
		try {
			final FacesContext facesContext = FacesContext.getCurrentInstance();
			ExternalContext externalContext = facesContext.getExternalContext();
			PortletRequest request = (PortletRequest) externalContext.getRequest();
			Map<String, String> userInfo = 
				(Map<String, String>) request.getAttribute(PortletRequest.USER_INFO);
			value = userInfo.get(attribute);
		} catch (Exception e) {
			throw new InternalExternalException(e);
		}
		if (value == null) {
			throw new NoExternalValueException("User Attribute "
				+ attribute + " not found ! See your portlet.xml file for user-attribute definition.");
		}
		return value;
	}

	/**
	 * @throws InternalExternalException 
	 * @see org.esupportail.lecture.domain.utils.ModeService#isUserInGroup(java.lang.String)
	 */
	public boolean isUserInGroup(final String group) throws InternalExternalException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("isUserInGroup(" + group + ")");
		}
		boolean value = Boolean.FALSE;
		try {			
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ExternalContext externalContext = facesContext.getExternalContext();
			PortletRequest request = (PortletRequest) externalContext.getRequest();
			if (request.isUserInRole(group)) {
				value = Boolean.TRUE;
			} 
		} catch (Exception e) {
			throw new InternalExternalException(e);
		}
		return value;
	}

	@Override
	public String getUserProxyTicketCAS(String casTargetService) {
		String ret;
		//get ProxyTicket for current portlet from uPortal 
		final FacesContext facesContext = FacesContext.getCurrentInstance();
		final ExternalContext externalContext = facesContext.getExternalContext();
		final PortletRequest request = (PortletRequest) externalContext.getRequest();
		Map<String,String> userinfo = (Map<String,String>) request.getAttribute(PortletRequest.USER_INFO);
		String ticket = (String) userinfo.get("casProxyTicket");
		//get ProxyTicket for casTargetService
		try {
			Assertion assertion = ticketValidator.validate(ticket, service);
			ret = assertion.getPrincipal().getProxyTicketFor(casTargetService);
		} catch (TicketValidationException e) {
			LOG.error("fail to validate a ticket");
			throw new RuntimeException(e);
		}
		return ret;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(ticketValidator, "property ticketValidator of class " 
				+ this.getClass().getName() + " can not be null");
		Assert.notNull(service, "property service of class " 
				+ this.getClass().getName() + " can not be null");
	}

	/*
	 ************************** Accessors ************************************/

	
}
