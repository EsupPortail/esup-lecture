/**
 * ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
 * For any information please refer to http://esup-helpdesk.sourceforge.net
 * You may obtain a copy of the licence at http://www.esup-portail.org/license/
 */
package org.esupportail.lecture.web;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.model.Category;
import org.esupportail.lecture.domain.model.ChannelConfig;

public class HomeBean {
	/**
	 * Log instance 
	 */
	protected static final Log log = LogFactory.getLog(ChannelConfig.class);
	private List<Category> categories;
	private FacadeWeb facadeWeb;
	
	public void setFacadeWeb(FacadeWeb facadeWeb) {
		this.facadeWeb = facadeWeb;
	}
	
	public List<Category> getCategories() {
		if (log.isDebugEnabled()) {
			log.debug("In getCategories");
		}
		this.categories = facadeWeb.getFacadeService().getCategories();
		return this.categories;
	}
	
	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}
}
