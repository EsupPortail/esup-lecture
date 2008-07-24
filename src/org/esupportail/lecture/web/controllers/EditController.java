/**
 * ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
 * For any information please refer to http://esup-helpdesk.sourceforge.net
 * You may obtain a copy of the licence at http://www.esup-portail.org/license/
 */
package org.esupportail.lecture.web.controllers;

import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.commons.exceptions.EsupException;
import org.esupportail.lecture.domain.beans.CategoryBean;
import org.esupportail.lecture.domain.beans.SourceBean;
import org.esupportail.lecture.domain.model.AvailabilityMode;
import org.esupportail.lecture.exceptions.domain.CategoryNotLoadedException;
import org.esupportail.lecture.exceptions.domain.ContextNotFoundException;
import org.esupportail.lecture.exceptions.domain.DomainServiceException;
import org.esupportail.lecture.exceptions.web.WebException;
import org.esupportail.lecture.web.beans.CategoryWebBean;
import org.esupportail.lecture.web.beans.ContextWebBean;
import org.esupportail.lecture.web.beans.SourceWebBean;
import org.springframework.util.Assert;

/**
 * @author : Raymond 
 */
public class EditController extends TwoPanesController {
	/**
	 * Key used to store the context in virtual session.
	 */
	static final String CONTEXT = "contextInEditMode";
	/**
	 * Log instance.
	 */
	private static final Log LOG = LogFactory.getLog(EditController.class);
	/**
	 * HomeController injected by Spring.
	 */
	private HomeController homeController;
	/**
	 * displayRoot says if root element of context tree is selected for display.
	 */
	private boolean displayRoot = true;
	/**
	 * Default constructor.
	 */
	public EditController() {
		super();
	}
	/**
	 * JSF action : select a category or a source from the tree.
	 * use categoryID and sourceID valued by t:updateActionListener
	 * @return JSF from-outcome
	 */
	public String selectElement() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("in selectElement");
		}
		String catID = this.categoryId;
		CategoryWebBean cat = getCategorieByID(catID);
		// set category focused by user as selected category in the context
		ContextWebBean ctx = getContext();
		ctx.setSelectedCategory(cat);
		displayRoot = false;
		return "OK";
	}

	/**
	 * JSF action : select a root element to edit categories' subscriptions.
	 * @return JSF from-outcome
	 */
	public String displayRoot() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("in dysplayRoot");
		}
		displayRoot = true;
		return "OK";
	}

	/**
	 * JSF action : Change subscription status of a source.
	 * @return JSF from-outcome
	 */
	public String toogleSourceSubcribtion() {
		CategoryWebBean selectedCategory = getContext().getSelectedCategory();
		SourceWebBean src = getSourceByID(selectedCategory, sourceId);
		AvailabilityMode type = null;
		try {
			if (src.isNotSubscribed()) {
				getFacadeService().subscribeToSource(getUID(), selectedCategory.getId(), sourceId);
				type = AvailabilityMode.SUBSCRIBED;
			}
			if (src.isSubscribed()) {
				getFacadeService().unsubscribeToSource(getUID(), selectedCategory.getId(), sourceId);
				type = AvailabilityMode.NOTSUBSCRIBED;
			}
		} catch (DomainServiceException e) {
			throw new WebException("Error in toogleSourceSubcribtion", e);
		}
		if (type != null) {
			src.setType(type);
		}
		//invalidate home page cache
		if (LOG.isDebugEnabled()) {
			LOG.debug("invalidate home page cache");
		}
		homeController.flushContextFormVirtualSession();
		return "OK";		
	}
	
	/**
	 * JSF action : Change subscription status of a category.
	 * @return JSF from-outcome
	 */
	public String toogleCategorySubcribtion() {
		//get selected category with categoryId provided by the actionListener
		CategoryWebBean selectedCategory = getCategorieByID(categoryId);
		AvailabilityMode type = null;
		try {
			if (selectedCategory.isNotSubscribed()) {
				getFacadeService().subscribeToCategory(getUID(), 
					getContext().getId(), selectedCategory.getId());
				type = AvailabilityMode.SUBSCRIBED;
			}
			if (selectedCategory.isSubscribed()) {
				getFacadeService().unsubscribeToCategory(getUID(), 
					getContext().getId(), selectedCategory.getId());
				type = AvailabilityMode.NOTSUBSCRIBED;
			}
		} catch (DomainServiceException e) {
			throw new WebException("Error in toogleCategorySubcribtion", e);
		}
		//invalidate home (and edit because of children sources modifications) page cache
		if (LOG.isDebugEnabled()) {
			LOG.debug("invalidate home page cache");
		}
		homeController.flushContextFormVirtualSession();
		this.flushContextFormVirtualSession();
		return "OK";		
	}
	
	/**
	 * @return list of visible sources
	 * @throws DomainServiceException 
	 */
	public List<SourceWebBean> getVisibleSources() {
		CategoryWebBean categoryBean = getSelectedCategory();
		List<SourceWebBean> ret = categoryBean.getSources();
		return ret;
	}

	/**
	 * @see org.esupportail.lecture.web.controllers.TwoPanesController#getSources(org.esupportail.lecture.domain.beans.CategoryBean)
	 * used to populate edit context with all visible sources
	 */
	@Override
	protected List<SourceBean> getSources(final CategoryBean categoryBean)
			throws DomainServiceException {
		List<SourceBean> sources = getFacadeService().getVisibleSources(getUID(), categoryBean.getId());
		return sources;

	}

	/**
	 * @return list of visible categories
	 * @throws DomainServiceException 
	 */
	public List<CategoryWebBean> getVisibleCategories() {
		ContextWebBean ctx = getContext();
		return ctx.getCategories();
	}

	/**
	 * @throws  
	 * @see org.esupportail.lecture.web.controllers.TwoPanesController#getCategories(java.lang.String)
	 * used to populate edit context with all visible categories
	 */
	@Override
	protected List<CategoryBean> getCategories(final String ctxtId)
			throws ContextNotFoundException {
		List<CategoryBean> categories;
		try {
			categories = getFacadeService().getVisibleCategories(getUID(), ctxtId);
		} catch (CategoryNotLoadedException e) {
			throw new WebException("Error in getCategories", e);
		}
		return categories;
	}
	
	/**
	 * @see org.esupportail.lecture.web.controllers.TwoPanesController#getContextName()
	 */
	@Override
	protected String getContextName() {
		return CONTEXT;
	}

	/**
	 * @param homeController
	 */
	public void setHomeController(final HomeController homeController) {
		this.homeController = homeController;
	}

	/**
	 * @see org.esupportail.lecture.web.controllers.TwoPanesController#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		Assert.notNull(homeController, 
				"property homeController of class " + this.getClass().getName() + " can not be null");
	}
	/**
	 * @return the displayRoot
	 */
	public boolean isDisplayRoot() {
		return displayRoot;
	}

	/**
	 * @see org.esupportail.lecture.web.controllers.TwoPanesController#getContextKey()
	 */
	@Override
	String getContextKey() {
		return CONTEXT;
	}
}
