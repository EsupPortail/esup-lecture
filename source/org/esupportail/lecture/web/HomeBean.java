/**
 * ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
 * For any information please refer to http://esup-helpdesk.sourceforge.net
 * You may obtain a copy of the licence at http://www.esup-portail.org/license/
 */
package org.esupportail.lecture.web;

import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.model.Category;

public class HomeBean {
	/**
	 * Log instance 
	 */
	protected static final Log log = LogFactory.getLog(HomeBean.class);
	private List<Category> categories;
	private FacadeWeb facadeWeb;
	private int treeSize=20;

	public int getTreeSize() {
		if (log.isDebugEnabled()) {
			log.debug("In getTreeSize :" + treeSize);
		}
		return treeSize;
	}

	public void setTreeSize(int treeSize) {
		this.treeSize = treeSize;
	}

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
	
	public void adjustTreeSize(ActionEvent e) {
		if (log.isDebugEnabled()) {
			log.debug("In adjustTreeSize");
		}
		FacesContext ctx = FacesContext.getCurrentInstance();
		String id = e.getComponent().getClientId(ctx);
		if (id.equals("home:treeSmaller")) {
			this.treeSize -= 5;			
		}
		if (id.equals("home:treeLarger")) {
			this.treeSize += 5;			
		}
	}
}
