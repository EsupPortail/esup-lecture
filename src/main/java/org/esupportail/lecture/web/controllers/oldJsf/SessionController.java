/**
 * ESUP-Portail Blank Application - Copyright (c) 2006 ESUP-Portail consortium
 * http://sourcesup.cru.fr/projects/esup-blank
 */
package org.esupportail.lecture.web.controllers.oldJsf;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.esupportail.commons.exceptions.ConfigException;
import org.esupportail.commons.services.authentication.AuthenticationService;
import org.esupportail.commons.utils.Assert;
import org.esupportail.commons.utils.ContextUtils;
import org.esupportail.commons.utils.strings.StringUtils;
import org.esupportail.commons.web.controllers.ExceptionController;
import org.esupportail.lecture.domain.DomainTools;
import org.esupportail.lecture.domain.beans.User;

/**
 * A bean to memorize the context of the application.
 */
public class SessionController extends AbstractDomainAwareBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
	 * @see org.esupportail.lecture.web.controllers.oldJsf.AbstractDomainAwareBean#afterPropertiesSetInternal()
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
			String currentUserId = DomainTools.getCurrentUserId(authenticationService);
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
	 * @param exceptionController the exceptionController to set
	 */
	public void setExceptionController(final ExceptionController exceptionController) {
		this.exceptionController = exceptionController;
	}

}
