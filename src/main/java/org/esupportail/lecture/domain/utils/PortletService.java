/**
 * ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
 * For any information please refer to http://esup-helpdesk.sourceforge.net
 * You may obtain a copy of the licence at http://www.esup-portail.org/license/
 */
package org.esupportail.lecture.domain.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.commons.utils.Assert;
import org.esupportail.lecture.domain.AssertionAccessor;
import org.esupportail.lecture.exceptions.domain.InternalExternalException;
import org.esupportail.lecture.exceptions.domain.NoExternalValueException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.portlet.context.PortletRequestAttributes;


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

	private AssertionAccessor assertionHandler;


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
	 * Setter of attribute assertionHandler
	 * @param assertionHandler <code>AssertionAccessor</code> the attribute assertionHandler to set
	 */
	public void setAssertionHandler(AssertionAccessor assertionHandler) {
		this.assertionHandler = assertionHandler;
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
			final PortletRequest request = ((PortletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest(); 
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
	public List<String> getUserAttribute(final String attribute)
			throws InternalExternalException, NoExternalValueException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("getUserAttribute(" + attribute + ")");
		}
		List<String> values;
		try {
			final PortletRequest request = ((PortletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest(); 
			// Portlet Request wich return multivalues attributes doesn't exist, Uportal added his implementation for this case : https://issues.jasig.org/browse/UP-933
			Map<String, ArrayList<String>> userInfo =
					(Map<String, ArrayList<String>>) request.getAttribute("org.jasig.portlet.USER_INFO_MULTIVALUED");
					//(Map<String, ArrayList<String>>) request.getAttribute(PortletRequest.USER_INFO);
			values = userInfo.get(attribute);
		} catch (RuntimeException re) {
			LOG.error("getUserAttribut: "+attribute, re);
			throw re;
		} catch (Exception e) {
			LOG.error("Can't find attribute " + attribute);
			throw new InternalExternalException(e);
		}
		if (values == null) {
			throw new NoExternalValueException("User Attribute "
					+ attribute + " not found! See your portlet.xml file for user-attribute definition.");
		}
		return values;
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
			final PortletRequest request = ((PortletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest(); 
			if (request.isUserInRole(group)) {
				value = Boolean.TRUE;
			}
		} catch (Exception e) {
			throw new InternalExternalException(e);
		}
		return value;
	}

	public String getUserProxyTicketCAS(String casTargetService) {
		return assertionHandler.getAssertion().getPrincipal().getProxyTicketFor(casTargetService);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		/*Assert.notNull(TicketValidator, "property ticketValidator of class "
				+ this.getClass().getName() + " can not be null");
		Assert.notNull(service, "property service of class "
				+ this.getClass().getName() + " can not be null");*/
		Assert.notNull(assertionHandler, "property assertionHandler of class "
				+ this.getClass().getName() + " can not be null");
	}

	/*
	 ************************** Accessors ************************************/


}
