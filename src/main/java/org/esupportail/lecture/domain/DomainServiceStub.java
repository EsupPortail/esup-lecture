/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain;

import java.util.List;

import org.esupportail.commons.exceptions.ConfigException;
import org.esupportail.commons.services.application.Version;
import org.esupportail.lecture.domain.beans.CategoryBean;
import org.esupportail.lecture.domain.beans.ContextBean;
import org.esupportail.lecture.domain.beans.ItemBean;
import org.esupportail.lecture.domain.beans.SourceBean;
import org.esupportail.lecture.domain.beans.UserBean;
import org.esupportail.lecture.domain.model.ItemDisplayMode;
import org.esupportail.lecture.domain.model.UserProfile;
import org.esupportail.lecture.exceptions.domain.InternalDomainException;

/**
 * @author bourges
 * an implementation of DomainService for tests
 */
/**
 * @author gbouteil
 *
 */
public class DomainServiceStub implements DomainService {

	/*
	 ************************** PROPERTIES ******************************** */	

	/**
	 * Test context.
	 */
	private ContextBean context;
	/**
	 * List of test categories.
	 */
	private List<CategoryBean> categories;
	/**
	 * List of test sources.
	 */
	private List<SourceBean> sources;
	/**
	 * List of test items.
	 */
	private List<ItemBean> items;
	
	/*
	 ************************** INIT ******************************** */	

	/**
	 * constructor used by Spring to instantiate this Test Class.
	 * @param context
	 * @param categories
	 * @param sources
	 * @param items
	 */
	public DomainServiceStub(final ContextBean context, final List<CategoryBean> categories, 
			final List<SourceBean> sources, final List<ItemBean> items) {
		super();
		this.context = context;
		this.categories = categories;
		this.sources = sources;
		this.items = items;
	}

	/*
	 *************************** METHODS ******************************** */	

	/**
	 * @see org.esupportail.lecture.domain.DomainService#getConnectedUser(org.esupportail.lecture.domain.model.UserProfile)
	 */
	public UserBean getConnectedUser(final UserProfile userProfile) {
		UserBean user = new UserBean(userProfile.getUserId());
		return user;
	}
	

	/**
	 * @see org.esupportail.lecture.domain.DomainService#getContext(org.esupportail.lecture.domain.model.UserProfile, java.lang.String)
	 */
	public ContextBean getContext(final UserProfile userProfile, final String contextId) {
		ContextBean ret = null;
		if (context.getId().equals(contextId)) {
			ret = context;
		}
		return ret;
	}
	
	/**
	 * @see org.esupportail.lecture.domain.DomainService#getDisplayedCategories(org.esupportail.lecture.domain.model.UserProfile, java.lang.String)
	 */
	public List<CategoryBean> getDisplayedCategories(final UserProfile userProfile, final String contextId) {
		return null;
	}
	
	/**
	 * @see org.esupportail.lecture.domain.DomainService#getDisplayedSources(org.esupportail.lecture.domain.model.UserProfile, java.lang.String)
	 */
	public List<SourceBean> getDisplayedSources(final UserProfile userProfile, final String categoryId) {
		return null;
	}
	
	/**
	 * @param sourceId 
	 * @param userProfile 
	 * @return items
	 * @see DomainService#getItems(UserProfile, String)
	 */
	public List<ItemBean> getItems(final String sourceId, 
			final UserProfile userProfile) {
		return items;
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#marckItemReadMode(org.esupportail.lecture.domain.model.UserProfile, java.lang.String, java.lang.String, boolean)
	 */
	public UserProfile markItemReadMode(
			final UserProfile userProfile, final String itemId, final String sourceId, final boolean isRead) {
		// nothing
		return null;
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#getVisibleCategories(org.esupportail.lecture.domain.model.UserProfile, java.lang.String)
	 */
	public List<CategoryBean> getVisibleCategories(
			final UserProfile userProfile, final String contextId) {
		List<CategoryBean> ret = null;
		ret = categories;
		return ret;
	}
	
	
	/**
	 * @see org.esupportail.lecture.domain.DomainService#getVisibleSources(org.esupportail.lecture.domain.model.UserProfile, java.lang.String)
	 */
	public List<SourceBean> getVisibleSources(final UserProfile userProfile, final String categoryId) {
		List<SourceBean> ret = null;
		ret = sources;
		return ret;
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#setTreeSize(org.esupportail.lecture.domain.model.UserProfile, java.lang.String, int)
	 */
	public UserProfile setTreeSize(final UserProfile userProfile, final String contextId, final int size) {
		return null;	
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#foldCategory(org.esupportail.lecture.domain.model.UserProfile, java.lang.String, java.lang.String)
	 */
	public UserProfile foldCategory(final UserProfile userProfile, final String cxtId, final String catId) {
		return null;	
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#unfoldCategory(org.esupportail.lecture.domain.model.UserProfile, java.lang.String, java.lang.String)
	 */
	public UserProfile unfoldCategory(final UserProfile userProfile, final String cxtId, final String catId) {
		return null;
	}

	
	/**
	 * @see org.esupportail.lecture.domain.DomainService#subscribeToSource(org.esupportail.lecture.domain.model.UserProfile, java.lang.String, java.lang.String)
	 */
	public UserProfile subscribeToSource(
		final UserProfile userProfile, final String categorieId, final String sourceId) {
		return null;
	}
	
	/**
	 * @see org.esupportail.lecture.domain.DomainService#subscribeToCategory(org.esupportail.lecture.domain.model.UserProfile, java.lang.String, java.lang.String)
	 */
	public UserProfile subscribeToCategory(final UserProfile userProfile, final String contextId, 
			final String categoryId) {
		return null;
	}
		
	/**
	 * @see org.esupportail.lecture.domain.DomainService#unsubscribeToCategory(org.esupportail.lecture.domain.model.UserProfile, java.lang.String, java.lang.String)
	 */
	public UserProfile unsubscribeToCategory(
			final UserProfile userProfile, 
			final String contextId, 
			final String categoryId) {
		return null;
	}
	/**
	 * @see org.esupportail.lecture.domain.DomainService#unsubscribeToSource(org.esupportail.lecture.domain.model.UserProfile, java.lang.String, java.lang.String)
	 */
	public UserProfile unsubscribeToSource(
			final UserProfile userProfile, final String categorieId, final String sourceId) {
		return null;
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#markItemDisplayMode(org.esupportail.lecture.domain.model.UserProfile, java.lang.String, org.esupportail.lecture.domain.model.ItemDisplayMode)
	 */
	public UserProfile markItemDisplayMode(final UserProfile userProfile, final String sourceId, final ItemDisplayMode mode) 
	throws InternalDomainException {
		return null;
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#getDatabaseVersion()
	 */
	public Version getDatabaseVersion() throws ConfigException {
		// nothing
		return null;
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#setDatabaseVersion(java.lang.String)
	 */
	public void setDatabaseVersion(final String version) {
		// nothing
		
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#isGuestMode()
	 */
	public boolean isGuestMode() {
		return false;
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#getItems(org.esupportail.lecture.domain.model.UserProfile, java.lang.String)
	 */
	public List<ItemBean> getItems(final UserProfile userProfile, final String sourceId) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#getUserProfile(java.lang.String)
	 */
	public UserProfile getUserProfile(final String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param query 
	 * @see org.esupportail.lecture.domain.DomainService#updateSQL(java.lang.String)
	 */
	public void updateSQL(String query) {
		// TODO Auto-generated method stub
		
	}

}
