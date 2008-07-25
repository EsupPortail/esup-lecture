/**
 * ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
 * For any information please refer to http://esup-helpdesk.sourceforge.net
 * You may obtain a copy of the licence at http://www.esup-portail.org/license/
 */
package org.esupportail.lecture.web.deepLinking;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.commons.web.deepLinking.DeepLinkingRedirector;
import org.esupportail.lecture.web.controllers.TwoPanesController;

/**
 * @author bourges
 *
 */
public class DeepLinkingRedirectorImpl implements DeepLinkingRedirector {
	/**
	 * Log instance.
	 */
	private static Log log = LogFactory.getLog(DeepLinkingRedirectorImpl.class);
	/**
	 * home Controller.
	 */
	private TwoPanesController homeController;

	/**
	 * default constructor.
	 */
	public DeepLinkingRedirectorImpl() {
		super();
	}

	/**
	 * @see org.esupportail.commons.web.deepLinking.DeepLinkingRedirector#redirect(java.util.Map)
	 */
	public String redirect(@SuppressWarnings("unused")final Map<String, String> params) {
		//call getcontext here to avoid database access during first portlet rendering
		if (log.isDebugEnabled()) {
			log.debug("in redirect()");
		}
		homeController.getContext();
		return null;
	}

	/**
	 * @param controller the controller to set
	 */
	public void setHomeController(final TwoPanesController controller) {
		this.homeController = controller;
	}

}
