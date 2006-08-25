/**
 * ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
 * For any information please refer to http://esup-helpdesk.sourceforge.net
 * You may obtain a copy of the licence at http://www.esup-portail.org/license/
 */
package org.esupportail.lecture.web;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.model.Category;
import org.esupportail.lecture.domain.model.tmp.CategoryRB;
import org.esupportail.lecture.domain.model.tmp.Item;
import org.esupportail.lecture.domain.model.tmp.SourceRB;

public class HomeBean {
	/**
	 * Log instance 
	 */
	protected static final Log log = LogFactory.getLog(HomeBean.class);
	private List<Category> categories;
	private FacadeWeb facadeWeb;
	private int treeSize=20;
	private boolean treeVisible=true;
	private int currentSourceID=1;
	private Category currentCategory;
	private SourceRB currentSource;
	
	/**
	 * For Spring injection of Service Class
	 * @param facadeWeb facade og Spring Service Class
	 */
	public void setFacadeWeb(FacadeWeb facadeWeb) {
		this.facadeWeb = facadeWeb;
	}
	
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

	public List<Category> getCategories() {
		if (log.isDebugEnabled()) {
			log.debug("In getCategories");
		}
		this.categories = facadeWeb.getFacadeService().getCategories();
		return this.categories;
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

	public void toggleItemReadState(ActionEvent e) {
		if (log.isDebugEnabled()) {
			log.debug("In toggleItemReadState");
		}
		FacesContext context = FacesContext.getCurrentInstance(); 
		Map map = context.getExternalContext().getRequestParameterMap();
		String id = (String)map.get("itemID");
		if (log.isDebugEnabled()) {
			log.debug("itemID = "+id);
		}
		facadeWeb.getFacadeService().toogleItemReadState(this.currentSourceID, Integer.parseInt(id));
	}

	public void selectASource(ActionEvent e) {
		if (log.isDebugEnabled()) {
			log.debug("In selectASource(ActionEvent)");
		}
		FacesContext context = FacesContext.getCurrentInstance(); 
		Map map = context.getExternalContext().getRequestParameterMap();
		String id = (String)map.get("sourceID");
		if (log.isDebugEnabled()) {
			log.debug("sourceID = "+id);
		}
		setCurrentSource(Integer.parseInt(id));
	}

	public List<Item> getItems() {
		if (currentSource == null) {
			currentSource = getCurrentSource();
		}
		if (currentSource != null) {
			SourceRB src = (SourceRB)currentSource;
			return src.getItems();
		}
		return null;
	}
	
	public Category getCurrentCategory() {
		Category ret = null;
		Iterator<Category> iter = categories.iterator();
		while (iter.hasNext()) {
			CategoryRB cat = (CategoryRB) iter.next();
			if (cat.isSelected()) {
				ret = cat;
			}
		}
		return ret;
	}
	
	public SourceRB getCurrentSource() {
		SourceRB ret = null;
		if (currentCategory == null) {
			currentCategory = getCurrentCategory();
		}
		if (currentCategory != null) {
			CategoryRB cat = (CategoryRB)currentCategory;
			Iterator<SourceRB> iter = cat.getSources().iterator();
			while (iter.hasNext()) {
				SourceRB src = (SourceRB) iter.next();
				if (src.isSelected()) {
					ret = src;
				}
			}
		}
		return ret;
	}

	public int getCurrentSourceID() {
		return currentSourceID;
	}

	public void setCurrentSourceID(int currentSourceID) {
		this.currentSourceID = currentSourceID;
	}

	public void setCurrentSource(int sourceID) {
		// find currentSource
		if (currentSource == null) {
			currentSource = getCurrentSource();
		}
		// unselect this CurrentSource
		if (currentSource != null) {
			SourceRB src = (SourceRB)currentSource;
			src.setSelected(false);
		}
		CategoryRB cat = (CategoryRB)currentCategory;
		Iterator<SourceRB> iter = cat.getSources().iterator();
		while (iter.hasNext()) {
			SourceRB src = (SourceRB) iter.next();
			if (src.getId() == sourceID) {
				src.setSelected(true);
				currentSource=src;
			}
		}
	}

}
