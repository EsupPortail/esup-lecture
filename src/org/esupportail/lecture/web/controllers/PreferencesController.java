/**
 * ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
 * http://sourcesup.cru.fr/projects/esup-lecture
 */
package org.esupportail.lecture.web.controllers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.esupportail.lecture.domain.beans.User;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;

/**
 * A bean to manage user preferences.
 */
public class PreferencesController extends AbstractContextAwareController {

	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(this.getClass());
	
	/**
	 * A list of JSF components for the locales.
	 */
	private List<SelectItem> localeItems;

	/**
	 * Bean constructor.
	 */
	public PreferencesController() {
		super();
	}

	/**
	 * @see org.esupportail.commons.web.controllers.Resettable#reset()
	 */
	public void reset() {
		localeItems = null;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getClass().getSimpleName() + "#" + hashCode();
	}

	/**
	 * @return true if the current user is allowed to view the page.
	 */
	public boolean isPageAuthorized() {
		return getCurrentUser() != null;
	}

	/**
	 * @return the localeItems
	 */
	@SuppressWarnings("unchecked")
	public List<SelectItem> getLocaleItems() {
		if (this.localeItems == null) {
			this.localeItems = new ArrayList<SelectItem>();
			Iterator<Locale> iter = 
				FacesContext.getCurrentInstance().getApplication().getSupportedLocales();
			while (iter.hasNext()) {
				Locale locale = iter.next();
				StringBuffer buf = new StringBuffer(locale.getLanguage());
				buf.append(" - ").append(locale.getDisplayLanguage(locale));
				this.localeItems.add(new SelectItem(locale, buf.toString()));
			}
		}
		return this.localeItems;
	}

	/**
	 * @param locale the locale to set
	 */
	public void setLocale(final Locale locale) {

		// store in the session
		setSessionLocale(locale);

		// update the current user
		User currentUser = getCurrentUser();
		if (logger.isDebugEnabled()) {
			logger.debug("set language [" + locale + "] for user '" + currentUser.getId() + "'");
		}
		currentUser.setLanguage(locale.toString());
		getDomainService().updateUser(currentUser);
		addInfoMessage(null, "PREFERENCES.MESSAGE.UPDATED");
	}

}
