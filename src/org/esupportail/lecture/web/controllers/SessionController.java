/**
 * ESUP-Portail Blank Application - Copyright (c) 2006 ESUP-Portail consortium
 * http://sourcesup.cru.fr/projects/esup-blank
 */
package org.esupportail.lecture.web.controllers;

import java.io.IOException;
import java.net.URLEncoder;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.myfaces.portlet.PortletUtil;
import org.esupportail.commons.exceptions.ConfigException;
import org.esupportail.commons.services.authentication.AuthenticationService;
import org.esupportail.commons.web.controllers.ExceptionController;
import org.esupportail.commons.web.jsf.ScopeAware;
import org.springframework.util.Assert;

/**
 * A bean to memorize the context of the application.
 */
public class SessionController extends AbstractDomainAwareBean implements ScopeAware {

	/**
	 * The name of the parameter that gives the logout URL.
	 */
	private static final String LOGOUT_URL_PARAM = "edu.yale.its.tp.cas.client.logoutUrl";

	/**
	 * The exception controller (called when logging in/out).
	 */
	private ExceptionController exceptionController;
	
	/**
	 * The authentication service.
	 */
	private AuthenticationService authenticationService;
	
	/**
	 * Constructor.
	 */
	public SessionController() {
		super();
		reset();
	}

	/**
	 * @see AbstractDomainAwareBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		Assert.notNull(this.exceptionController, 
				"property exceptionController of class " + this.getClass().getName() + " can not be null");
		Assert.notNull(this.authenticationService, 
				"property authenticationService of class " + this.getClass().getName() + " can not be null");
	}

	/**
	 * @see org.esupportail.commons.web.controllers.Resettable#reset()
	 */
	public void reset() {
		// nothing
	}
	
	/**
	 * @return boolean true if running as a portlet. 
	 */
	public static boolean isPortlet() {
        return PortletUtil.isPortletRequest(FacesContext.getCurrentInstance());
	}
	
	/**
	 * @return true if running as a servlet. 
	 */
	public boolean isServlet() {
        return !isPortlet();
	}
	
	/**
	 * @return a debug String.
	 */
	public String getDebug() {
		return toString();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SessionController#";
	}

	/**
	 * @param authenticationService the authenticationService to set
	 */
	public void setAuthenticationService(final AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	/**
	 * @see org.esupportail.commons.web.jsf.ScopeAware#getScope()
	 */
	public String getScope() {
		return ScopeAware.SESSION_SCOPE;
	}
	
//	private void debug(final HttpServletRequest request) throws IOException {
//		System.err.println(request);
//		System.err.println(request.getSession());
//		System.err.println("user = [" + HttpUtils.getSessionAttribute(request, CASFilter.CAS_FILTER_USER) + "]");
//		System.err.println(HttpUtils.getSessionAttributesStrings(request));
//	}

	/**
	 * JSF callback.
	 * @return a String.
	 * @throws IOException 
	 */
	public String logout() throws IOException {
		if (isPortlet()) {
			throw new UnsupportedOperationException("logout() should not be called in portlet mode.");
		}
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
		String logoutUrl = externalContext.getInitParameter(LOGOUT_URL_PARAM);
		if (logoutUrl == null) {
			throw new ConfigException("context parameter '" + LOGOUT_URL_PARAM + "' not found");
		}
		String returnUrl = request.getRequestURL().toString().replaceFirst("/stylesheets/[^/]*$", "");
		String forwardUrl = String.format(logoutUrl, URLEncoder.encode(returnUrl, "UTF-8"));
		// note: the session beans will be kept event when invalidating 
		// the session so they have to be reset (by the exception controller).
		// We invalidate the session however for the other attributes.
		request.getSession().invalidate();
		request.getSession(true);
		// calling this method will reset all the beans of the application
		exceptionController.restart();
		externalContext.redirect(forwardUrl);
		facesContext.responseComplete();
		return null;
	}

	/**
	 * @param exceptionController the exceptionController to set
	 */
	public void setExceptionController(final ExceptionController exceptionController) {
		this.exceptionController = exceptionController;
	}

}
