/**
 * ESUP-Portail Blank Application - Copyright (c) 2006 ESUP-Portail consortium
 * http://sourcesup.cru.fr/projects/esup-blank
 */
package org.esupportail.lecture.web.controllers;

import org.esupportail.lecture.domain.beans.User;
import org.esupportail.commons.web.jsf.ScopeAware;
import org.springframework.util.Assert;


/**
 * An abstract class inherited by all the beans for them to get:
 * - the context of the application (sessionController).
 * - the domain service (domainService).
 * - the application service (applicationService).
 * - the i18n service (i18nService).
 */
public abstract class AbstractContextAwareController extends AbstractDomainAwareBean implements ScopeAware {

	/**
	 * The SessionController.
	 */
	private SessionController sessionController;

	/**
	 * Constructor.
	 */
	protected AbstractContextAwareController() {
		super();
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
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
	 * @return the sessionController
	 */
	public SessionController getSessionController() {
		return sessionController;
	}

	/**
	 * @see org.esupportail.blank.web.controllers.AbstractDomainAwareBean#getCurrentUser()
	 */
	@Override
	protected User getCurrentUser() {
		return sessionController.getCurrentUser();
	}

	/**
	 * @see org.esupportail.commons.web.jsf.ScopeAware#getScope()
	 */
	public String getScope() {
		return ScopeAware.SESSION_SCOPE;
	}

}
