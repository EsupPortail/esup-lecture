/**
 * ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
 * For any information please refer to http://esup-helpdesk.sourceforge.net
 * You may obtain a copy of the licence at http://www.esup-portail.org/license/
 */
package org.esupportail.lecture.web.controllers;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.FacadeService;
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

}
