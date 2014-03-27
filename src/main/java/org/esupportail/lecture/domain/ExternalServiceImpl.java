/**
 * ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
 * For any information please refer to http://esup-helpdesk.sourceforge.net
 * You may obtain a copy of the licence at http://www.esup-portail.org/license/
 */
package org.esupportail.lecture.domain;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.commons.services.authentication.AuthenticationService;
import org.esupportail.commons.utils.Assert;
import org.esupportail.lecture.domain.utils.ModeService;
import org.esupportail.lecture.exceptions.domain.InfoExternalException;
import org.esupportail.lecture.exceptions.domain.InternalExternalException;
import org.esupportail.lecture.exceptions.domain.NoExternalValueException;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author bourges
 * Implementation of interface ExternalService where
 * externalService can be :
 * - portlet service
 * - servlet service
 * The modeService (portlet or servlet) is dynamically defined at every method calls
 */
public class ExternalServiceImpl implements ExternalService, InitializingBean {

	/*
	 *************************** PROPERTIES ******************************** */

	/**
	 * the logger for this class.
	 */
	protected static final Log LOG = LogFactory.getLog(ExternalServiceImpl.class);

	/**
	 * portlet version of ExternalService.
	 */
	private ModeService portletService;

	/**
	 * default version of ExternalService.
	 */
	private ModeService defaultService;

	/**
	 * The authentication Service.
	 */
	private AuthenticationService authenticationService;

	/*
	 *************************** INIT ************************************** */
	/**
	 * Default constructor.
	 */
	public ExternalServiceImpl() {
		super();
		// needed by checkstyle
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(authenticationService, "property authenticationService of class "
				+ this.getClass().getName() + " can not be null");
		Assert.notNull(portletService, "property portletService of class "
				+ this.getClass().getName() + " can not be null");
		if (defaultService == null) {
			defaultService = portletService;
		}
	}

	/*
	 *************************** METHODS *********************************** */

	/**
	 * @see org.esupportail.lecture.domain.ExternalService#getConnectedUserId()
	 */
	public String getConnectedUserId() {
		return DomainTools.getCurrentUserId(authenticationService);
	}
	/**
	 * Return ID of the current context (from channel instantiation : portlet preference with name "context")).
	 * @throws InternalExternalException
	 * @throws NoExternalValueException
	 * @see org.esupportail.lecture.domain.ExternalService#getCurrentContextId()
	 */
	public String getCurrentContextId() throws NoExternalValueException, InternalExternalException {
		return getPreferences(DomainTools.getContext());
	}

	/**
	 * @throws InternalExternalException
	 * @throws NoExternalValueException
	 * @see org.esupportail.lecture.domain.ExternalService#getPreferences(java.lang.String)
	 */
	public String getPreferences(final String name) throws NoExternalValueException, InternalExternalException {
		String ret = getModeService().getPreference(name);

		if (LOG.isTraceEnabled()) {
			LOG.trace("getPreferences(" + name + ") return " + ret);
		}

		return ret;
	}

	/**
	 * @throws InternalExternalException
	 * @throws NoExternalValueException
	 * @see org.esupportail.lecture.domain.ExternalService#getUserAttribute(java.lang.String)
	 */
	public List<String> getUserAttribute(final String attribute)
			throws NoExternalValueException, InternalExternalException {
		List<String> ret = getModeService().getUserAttribute(attribute);

		if (LOG.isTraceEnabled()) {
			LOG.trace("getUserAttribute(" + attribute + ") return " + ret);
		}
		return ret;
	}

	/**
	 * @throws InfoExternalException
	 * @throws
	 * @see org.esupportail.lecture.domain.ExternalService#getUserProxyTicketCAS(String)
	 */
	public String getUserProxyTicketCAS(final String casTargetService) throws InfoExternalException {
		String ret = getModeService().getUserProxyTicketCAS(casTargetService);

		if (LOG.isDebugEnabled()) {
			LOG.debug("getUserProxyTicketCAS(" + casTargetService + ")");
		}
		return ret;

	}

	/**
	 * @throws InternalExternalException
	 * @see org.esupportail.lecture.domain.ExternalService#isUserInGroup(java.lang.String)
	 */
	public boolean isUserInGroup(final String group) throws InternalExternalException {
		boolean ret = getModeService().isUserInGroup(group);

		if (LOG.isDebugEnabled()) {
			LOG.debug("isUserInRole(" + group + ") return " + ret);
		}
		return ret;
	}

	/**
	 * Get the current mode service.
	 * It can be :
	 * 	- portletService
	 *  - servletService
	 *  - defaultService
	 * Computes it dynamically and not in contructor because Spring can't find facesContext at startup
	 * @return ModeService - the current mode service
	 */
	private ModeService getModeService() {
		ModeService ret = null;
		ret = portletService;
		return ret;
	}

	/*
	 *************************** ACCESSORS ********************************* */

	/**
	 * @param authenticationService the authenticationService to set
	 */
	public void setAuthenticationService(final AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	/**
	 * @param portletService the portletService to set
	 */
	public void setPortletService(final ModeService portletService) {
		this.portletService = portletService;
	}

	/**
	 * @param defaultService the defaultService to set
	 */
	public void setDefaultService(final ModeService defaultService) {
		this.defaultService = defaultService;
	}

}
