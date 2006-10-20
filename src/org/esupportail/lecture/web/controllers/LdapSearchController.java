/**
 * ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
 * http://sourcesup.cru.fr/projects/esup-lecture
 */
package org.esupportail.lecture.web.controllers;

import java.util.Locale;

import org.springframework.util.Assert;

/**
 * The bean used for LDAP searches.
 */
public class LdapSearchController extends
		org.esupportail.commons.web.controllers.LdapSearchController {
	
	/**
	 * The context bean.
	 */
	private SessionController sessionController;

	/**
	 * Bean constructor.
	 */
	public LdapSearchController() {
		super();
	}

	/**
	 * @see org.esupportail.commons.web.controllers.LdapSearchController#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		Assert.notNull(this.sessionController, 
				"property sessionController of class " + this.getClass().getName() + " can not be null");
	}

	/**
	 * @param sessionController the sessionController to set
	 */
	public void setSessionController(final SessionController sessionController) {
		this.sessionController = sessionController;
	}

	/**
	 * @see org.esupportail.commons.domain.beans.AbstractI18nAwareBean#getCurrentUserLocale()
	 */
	@Override
	protected Locale getCurrentUserLocale() {
		return sessionController.getCurrentUserLocale();
	}

}
