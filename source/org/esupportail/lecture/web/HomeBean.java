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
	private boolean treeVisible=true;

	public boolean isTreeVisible() {
		return treeVisible;
	}

	public void setTreeVisible(boolean treeVisible) {
		this.treeVisible = treeVisible;
	}

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
		if (id.equals("home:leftSubview:treeSmallerButton")) {
			if (this.treeSize > 10) {
				this.treeSize -= 5;				
			}
		}
		if (id.equals("home:leftSubview:treeLargerButton")) {
			if (this.treeSize < 90) {
				this.treeSize += 5;				
			}
		}
	}
	
	public void toggleTreeVisibility(ActionEvent e) {
		if (log.isDebugEnabled()) {
			log.debug("In toggleTreeVisibility");
		}
		if (isTreeVisible()) {
			this.treeVisible=false;
		} else {
			this.treeVisible=true;
		}
	}
}
