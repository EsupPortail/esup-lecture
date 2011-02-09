/**
 * ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
 * For any information please refer to http://esup-helpdesk.sourceforge.net
 * You may obtain a copy of the licence at http://www.esup-portail.org/license/
 */
package org.esupportail.lecture.web.controllers;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.beans.CategoryBean;
import org.esupportail.lecture.domain.beans.ItemBean;
import org.esupportail.lecture.domain.beans.SourceBean;
import org.esupportail.lecture.domain.model.AvailabilityMode;
import org.esupportail.lecture.domain.model.UserProfile;
import org.esupportail.lecture.exceptions.domain.DomainServiceException;
import org.esupportail.lecture.exceptions.domain.InternalDomainException;
import org.esupportail.lecture.exceptions.domain.ManagedCategoryNotLoadedException;
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
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
	private TwoPanesController homeController;
	/**
	 * Default constructor.
	 */
	public EditController() {
		super();
	}

	/**
	 * JSF action : Change subscription status of a source.
	 * @return JSF from-outcome
	 */
	public String toogleSourceSubcribtion() {
		ContextWebBean currentContext = getContext();
		CategoryWebBean selectedCategory = currentContext.getSelectedCategory();
		SourceWebBean selectedSource = getUalSource();
		AvailabilityMode type = null;
		try {
			UserProfile userProfile = getUserProfile(); 
			if (selectedSource.isNotSubscribed()) {
				userProfile = getFacadeService().subscribeToSource(
						userProfile, selectedCategory.getId(), selectedSource.getId());
				type = AvailabilityMode.SUBSCRIBED;
			}
			if (selectedSource.isSubscribed()) {
				userProfile = getFacadeService().unsubscribeToSource(
						userProfile, selectedCategory.getId(), selectedSource.getId());
				type = AvailabilityMode.NOTSUBSCRIBED;
			}
			setUserProfile(userProfile);
		} catch (DomainServiceException e) {
			throw new WebException("Error in toogleSourceSubcribtion", e);
		}
		if (type != null) {
			selectedSource.setType(type);
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
		CategoryWebBean currentCategory = getUalCategory();
		AvailabilityMode availabilityMode = currentCategory.getAvailabilityMode();
		try {
			UserProfile userProfile = getUserProfile();
			if (availabilityMode == AvailabilityMode.NOTSUBSCRIBED) {
				userProfile = getFacadeService().subscribeToCategory(userProfile, 
					getContext().getId(), currentCategory.getId());
			}
			if (availabilityMode ==  AvailabilityMode.SUBSCRIBED) {
				userProfile = getFacadeService().unsubscribeToCategory(userProfile, 
						getContext().getId(), currentCategory.getId());
			}
			setUserProfile(userProfile);
		} catch (DomainServiceException e) {
			throw new WebException("Error in toogleCategorySubcribtion", e);
		}
		//invalidate home (and edit because of children sources modifications) page cache
		if (LOG.isDebugEnabled()) {
			LOG.debug("invalidate home page cache");
		}
		//clear information form session for homeController and this (EditController)
		homeController.flushContextFormVirtualSession();
		this.flushContextFormVirtualSession();
		//set selectedCategory to currentCategory (but just with Id in order to have a fresh category)
		getContext().setSelectedCategoryById(currentCategory.getId());
		return "OK";		
	}
	
	/**
	 * @return list of visible sources
	 */
	public List<SourceWebBean> getVisibleSources() {
		List<SourceWebBean> ret = null;
		CategoryWebBean categoryBean = getSelectedCategory();
		if (categoryBean != null) {
			ret = categoryBean.getSources();
		}
		return ret;
	}

	/**
	 * @see org.esupportail.lecture.web.controllers.TwoPanesController#getSources(org.esupportail.lecture.domain.beans.CategoryBean)
	 * used to populate edit context with all visible sources
	 */
	@Override
	protected List<SourceBean> getSources(final CategoryBean categoryBean)
			throws DomainServiceException {
		List<SourceBean> sources = getFacadeService().getVisibleSources(getUserProfile(), categoryBean.getId());
		return sources;

	}

	/**
	 * @return list of visible categories
	 */
	public List<CategoryWebBean> getVisibleCategories() {
		ContextWebBean ctx = getContext();
		return ctx.getCategories();
	}

	/**  
	 * @throws InternalDomainException 
	 * @see org.esupportail.lecture.web.controllers.TwoPanesController#getCategories(java.lang.String)
	 * used to populate edit context with all visible categories
	 */
	@Override
	protected List<CategoryBean> getCategories(final String ctxtId) 
	throws InternalDomainException {
		List<CategoryBean> categories;
		try {
			categories = getFacadeService().getVisibleCategories(getUserProfile(), ctxtId);
		} catch (ManagedCategoryNotLoadedException e) {
			throw new WebException("Error in getCategories", e);
		}
		return categories;
	}
	
	/**
	 * @see org.esupportail.lecture.web.controllers.TwoPanesController#getContextName()
	 */
	protected String getContextName() {
		return CONTEXT;
	}

	/**
	 * @param homeController
	 */
	public void setHomeController(final TwoPanesController homeController) {
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
	 * @see org.esupportail.lecture.web.controllers.TwoPanesController#getContextKey()
	 */
	@Override
	String getContextKey() {
		return CONTEXT;
	}

	/**
	 * @param sourceBean
	 * @return null
	 */
	@Override
	protected List<ItemBean> getItems(final SourceBean sourceBean) {
				//must be overwritten in edit mode 
				//(return null and not items because user isn't 
				//probably not already subscribed to source)
				return null;
			}
}
