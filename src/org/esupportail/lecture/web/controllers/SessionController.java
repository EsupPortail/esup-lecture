/**
 * ESUP-Portail Blank Application - Copyright (c) 2006 ESUP-Portail consortium
 * http://sourcesup.cru.fr/projects/esup-blank
 */
package org.esupportail.lecture.web.controllers;

import java.io.IOException;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.myfaces.portlet.PortletUtil;
import org.esupportail.lecture.domain.DomainTools;
import org.esupportail.lecture.domain.beans.User;
import org.esupportail.commons.exceptions.ConfigException;
import org.esupportail.commons.services.authentication.AuthenticationService;
import org.esupportail.commons.utils.Assert;
import org.esupportail.commons.utils.ContextUtils;
import org.esupportail.commons.utils.strings.StringUtils;
import org.esupportail.commons.web.controllers.ExceptionController;

/**
 * A bean to memorize the context of the application.
 */
public class SessionController extends AbstractDomainAwareBean {

	/**
	 * The name of the parameter that gives the logout URL.
	 */
	private static final String LOGOUT_URL_PARAM = "edu.yale.its.tp.cas.client.logoutUrl";

	/**
	 * The name of the request attribute that holds the current user.
	 */
	private static final String CURRENT_USER_ATTRIBUTE = SessionController.class.getName() + ".currentUser";
	
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
	}

	/**
	 * @see org.esupportail.lecture.web.controllers.AbstractDomainAwareBean#afterPropertiesSetInternal()
	 */
	@Override
	public void afterPropertiesSetInternal() {
		Assert.notNull(this.exceptionController, "property exceptionController of class " 
				+ this.getClass().getName() + " can not be null");
		Assert.notNull(this.authenticationService, "property authenticationService of class " 
				+ this.getClass().getName() + " can not be null");
	}

	/**
	 * @return the current user, or null if guest.
	 */
	@Override
	public User getCurrentUser() {
		if (ContextUtils.getRequestAttribute(CURRENT_USER_ATTRIBUTE) == null) {
			String currentUserId = authenticationService.getAuthInfo().getId();
			if (currentUserId == null) {
				currentUserId = DomainTools.getGuestUser();
				//return null;
			}
			User user = new User();
			user.setDisplayName(currentUserId);
			user.setId(currentUserId);
			user.setAdmin(false);
			resetSessionLocale();
			ContextUtils.setRequestAttribute(CURRENT_USER_ATTRIBUTE, user);
		}
		return (User) ContextUtils.getRequestAttribute(CURRENT_USER_ATTRIBUTE);
	}

	/**
	 * @return true if running as a portlet. 
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
	 * @return true if the login button should be printed. 
	 */
	public boolean isPrintLogin() {
		return isServlet() && getCurrentUser() == null;
	}
	
	/**
	 * @return true if the logout button should be printed. 
	 */
	public boolean isPrintLogout() {
		return isServlet() && getCurrentUser() != null;
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
		return "SessionController#" + hashCode();
	}

	/**
	 * @param authenticationService the authenticationService to set
	 */
	public void setAuthenticationService(final AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

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
		String forwardUrl = String.format(logoutUrl, StringUtils.utf8UrlEncode(returnUrl));
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
