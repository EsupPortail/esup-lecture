/**
 * ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
 * For any information please refer to http://esup-helpdesk.sourceforge.net
 * You may obtain a copy of the licence at http://www.esup-portail.org/license/
 */
package org.esupportail.lecture.web.controllers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.DomainTools;
import org.esupportail.lecture.domain.FacadeService;
import org.esupportail.lecture.domain.beans.CategoryBean;
import org.esupportail.lecture.domain.beans.ContextBean;
import org.esupportail.lecture.domain.beans.SourceBean;
import org.esupportail.lecture.domain.beans.UserBean;
import org.esupportail.lecture.exceptions.ErrorException;
import org.esupportail.lecture.exceptions.domain.ContextNotFoundException;
import org.esupportail.lecture.exceptions.domain.DomainServiceException;
import org.esupportail.lecture.exceptions.domain.InternalDomainException;
import org.esupportail.lecture.web.beans.CategoryWebBean;
import org.esupportail.lecture.web.beans.ContextWebBean;
import org.esupportail.lecture.web.beans.SourceWebBean;
import org.springframework.util.Assert;

/**
 * @author : Raymond 
 */
public abstract class twoPanesController extends AbstractContextAwareController {
	/**
	 * Log instance 
	 */
	protected static final Log log = LogFactory.getLog(twoPanesController.class);
	/**
	 * Key used to store the context in virtual session
	 */
	static final String CONTEXT = "context";
	/**
	 * default tree size 
	 */
	private int treeSize=20;
	/**
	 * is tree is visible or not
	 */
	private boolean treeVisible=true;
	/**
	 * Access to facade services (init by Spring)
	 */
	private FacadeService facadeService;
	/**
	 * Access to multiple instance of channel in a one session (contexts)
	 */
	protected VirtualSession virtualSession;
	/**
	 * UID of the connected user
	 */
	private String UID = null;
	/**
	 * Store if a source is selected or not
	 */
	protected boolean isSourceSelected = false;
	/**
	 *  categoryID used by t:updateActionListener
	 */
	protected String categoryId;
	/**
	 *  sourceID used by t:updateActionListener
	 */
	protected String sourceId;
	/**
	 * Controller constructor
	 */
	public twoPanesController() {
		super();
	}

	/*
	 * **************** Action and listener method ****************
	 */	
	/**
	 * JSF action : change treesize
	 * @param e JSF ActionEvent used to know which button is used 
	 */
	public void adjustTreeSize(ActionEvent e) {
		if (log.isDebugEnabled()) {
			log.debug("In adjustTreeSize");
		}
		FacesContext ctx = FacesContext.getCurrentInstance();
		String id = e.getComponent().getClientId(ctx);
		if (id.equals("home:leftSubview:treeSmallerButton")) {
			if (treeSize > 10) {
				treeSize-=5;
			}
		}
		if (id.equals("home:leftSubview:treeLargerButton")) {
			if (treeSize < 90) {
				treeSize+=5;
			}
		}
	}
	
	/**
	 * JSF action : toogle from tree visible to not visible and not visible to visible
	 * @return JSF from-outcome
	 */
	public String toggleTreeVisibility() {
		if (log.isDebugEnabled()) {
			log.debug("In toggleTreeVisibility");
		}
		if (isTreeVisible()) {
			setTreeVisible(false);
		} else {
			setTreeVisible(true);
		}
		return "OK";
	}
	
	/**
	 * @return if tree is visible or not
	 */
	public boolean isTreeVisible() {
		return treeVisible;
	}
	
	/**
	 * @return the size of left tree
	 */
	public int getTreeSize() {
		return treeSize;
	}

	/**
	 * set tree visibility to yes or no
	 * @param treeVisible boolean value for tree visibility
	 */
	public void setTreeVisible(boolean treeVisible) {
		this.treeVisible = treeVisible;
	}

	/*
	 * **************** Getter and Setter ****************
	 */
	
	/**
	 * For Spring injection of Service Class
	 * @param facadeService facade og Spring Service Class
	 */
	public void setFacadeService(FacadeService facadeService) {
		this.facadeService = facadeService;
	}

	public FacadeService getFacadeService() {
		return facadeService;
	}

	/**
	 * To display information about the custom Context of the connected user
	 * @return Returns the context.
	 */
	public ContextWebBean getContext() {
		ContextWebBean context = (ContextWebBean) virtualSession.get(CONTEXT);
		if (context == null){
			if (log.isDebugEnabled()) 
				log.debug ("getContext() :  Context not yet loaded : loading...");
			try {
				//We evalute the context and we put it in the virtual session
				context = new ContextWebBean();
				String contextId;
				contextId = getFacadeService().getCurrentContextId();
				ContextBean contextBean = getFacadeService().getContext(getUID(), contextId);
				if (contextBean == null) {
					throw new ErrorException("No context with ID \""+contextId+"\" found in lecture-config.xml file. See this file or portlet preference with name \""+DomainTools.CONTEXT+"\".");
				}
				context.setName(contextBean.getName());
				context.setId(contextBean.getId());
				context.setDescription(contextBean.getDescription());
				//find categories in this context
				List<CategoryBean> categories = getCategories(contextId);
				List<CategoryWebBean> categoriesWeb = new ArrayList<CategoryWebBean>();
				if (categories != null) {
					Iterator<CategoryBean> iter = categories.iterator();
					while (iter.hasNext()) {
						CategoryBean categoryBean = iter.next();
						CategoryWebBean categoryWebBean =  new CategoryWebBean();
						categoryWebBean.setId(categoryBean.getId());
						categoryWebBean.setName(categoryBean.getName());
						categoryWebBean.setDescription(categoryBean.getDescription());
						//find sources in this category
						List<SourceBean> sources = getSources(categoryBean);
						List<SourceWebBean> sourcesWeb = new ArrayList<SourceWebBean>();
						if (sources != null) {
							Iterator<SourceBean> iter2 = sources.iterator();
							while (iter2.hasNext()) {
								SourceBean sourceBean = iter2.next();
								SourceWebBean sourceWebBean = new SourceWebBean();
								sourceWebBean.setId(sourceBean.getId());
								sourceWebBean.setName(sourceBean.getName());
								sourceWebBean.setType(sourceBean.getType());
								sourcesWeb.add(sourceWebBean);
							}
						}
						categoryWebBean.setSources(sourcesWeb);
						categoriesWeb.add(categoryWebBean);
					}
				}
				context.setCategories(categoriesWeb);
				virtualSession.put(CONTEXT,context);
			} catch (Exception e) {
				throw new ErrorException("Error in getContext :"+e.getMessage());
			} 
		}
		return context;
	}

	/**
	 * @param categoryBean
	 * @return list of visible sources
	 * @throws DomainServiceException
	 */
	protected List<SourceBean> getSources(CategoryBean categoryBean) throws DomainServiceException {
		//this method need to be overwrite in edit controller
		List<SourceBean> sources = getFacadeService().getVisibleSources(getUID(), categoryBean.getId());
		return sources;
	}

	/**
	 * @param contextId
	 * @return list of visible categories
	 * @throws InternalDomainException
	 * @throws ContextNotFoundException 
	 */
	protected List<CategoryBean> getCategories(String contextId) throws ContextNotFoundException {
		//this method need to be overwrite in edit controller
		List<CategoryBean> categories = getFacadeService().getVisibleCategories(getUID(), contextId);
		return categories;
	}

	/**
	 * @return the connected user UID
	 */
	protected String getUID() {
		if (UID == null) {
			//init the user
			String userId;
			try {
				userId = getFacadeService().getConnectedUserId();
			} catch (Exception e) {
				throw new ErrorException("Error in getUID :"+e.getMessage());
			}
			UserBean userBean = getFacadeService().getConnectedUser(userId);
			UID = userBean.getUid();
		}
		return UID;
	}

	/**
	 * @return information of any source selected or not
	 */
	public boolean isSourceSelected() {
		return isSourceSelected;
	}

	/**
	 * @return Selection Title
	 */
	public String getSelectionTitle() {
		String ret = null;
		ContextWebBean ctx = getContext();
		CategoryWebBean categoryWebBean = ctx.getSelectedCategory();
		if (categoryWebBean != null) {
			ret = categoryWebBean.getName();
			if (isSourceSelected) {
				SourceWebBean sourceWebBean = categoryWebBean.getSelectedSource();
				if (sourceWebBean != null) {
					ret += " > " + sourceWebBean.getName();
				}
			}
		}
		return ret;
	}

	/**
	 * @param id of catogory to find in the context
	 * @return the finded category
	 */
	protected CategoryWebBean getCategorieByID(String id) {
		CategoryWebBean ret = null;
		ContextWebBean ctx = getContext();
		Iterator<CategoryWebBean> iter = ctx.getCategories().iterator();
		while (iter.hasNext()) {
			CategoryWebBean cat = iter.next();
			if (cat.getId() == id) {
				ret = cat;
			}
		}
		return ret;
	}

	/**
	 * set categoryId from t:updateActionListener in JSF view
	 * @param categoryId
	 */
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	/**
	 * set sourceId from t:updateActionListener in JSF view
	 * @param sourceId
	 */
	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	/**
	 * @param cat where to find source
	 * @param id of source to find
	 * @return the finded source
	 */
	protected SourceWebBean getSourceByID(CategoryWebBean cat, String id) {
		SourceWebBean ret = null;
		Iterator<SourceWebBean> iter = cat.getSources().iterator();
		while (iter.hasNext()) {
			SourceWebBean src = iter.next();
			if (src.getId() == id) {
				ret = src;
			}
		}
		return ret;
	}

}
