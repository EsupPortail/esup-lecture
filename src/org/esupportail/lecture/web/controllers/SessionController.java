/**
 * ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
 * http://sourcesup.cru.fr/projects/esup-lecture
 */
package org.esupportail.lecture.web.controllers;

import java.io.IOException;
import java.net.URLEncoder;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.myfaces.portlet.PortletUtil;
import org.esupportail.lecture.domain.beans.Department;
import org.esupportail.lecture.domain.beans.User;
import org.esupportail.commons.exceptions.ConfigException;
import org.esupportail.commons.services.authentication.AuthenticationService;
import org.esupportail.commons.utils.HttpUtils;
import org.esupportail.commons.web.controllers.ExceptionController;
import org.esupportail.commons.web.jsf.ScopeAware;
import org.springframework.util.Assert;

import edu.yale.its.tp.cas.client.filter.CASFilter;

/**
 * A bean to memorize the context of the application.
 */
public class SessionController extends AbstractDomainAwareBean implements ScopeAware {

	/**
	 * The name of the parameter that gives the logout URL.
	 */
	private static final String LOGOUT_URL_PARAM = "edu.yale.its.tp.cas.client.logoutUrl";

	/**
	 * The current department.
	 */
	private Department department;

	/**
	 * The current user.
	 */
	private User user;
	
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
	 * @see org.esupportail.lecture.web.controllers.AbstractDomainAwareBean#afterPropertiesSet()
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
	 * @return the current user, or null if guest.
	 */
	@Override
	public User getCurrentUser() {
		if (user == null) {
			String currentUserId = authenticationService.getCurrentUserId();
			if (currentUserId == null) {
				return null;
			}
			user = getDomainService().getUser(currentUserId);
			// update the information
			getDomainService().updateUserInfo(user);
		}
		return user;
	}

	/**
	 * @see org.esupportail.commons.web.controllers.Resettable#reset()
	 */
	public void reset() {
		department = null;
		user = null;
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
	 * @param department the department to set
	 */
	public void setDepartment(final Department department) {
		this.department = department;
	}

	/**
	 * @return the department
	 */
	public Department getDepartment() {
		return department;
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
		return "SessionController#" + hashCode() + "[department=" + department + ", user=" + user + "]";
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
	
	private void debug(final HttpServletRequest request) throws IOException {
		System.err.println(request);
		System.err.println(request.getSession());
		System.err.println("user = [" + HttpUtils.getSessionAttribute(request, CASFilter.CAS_FILTER_USER) + "]");
		System.err.println(HttpUtils.getSessionAttributesStrings(request));
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
