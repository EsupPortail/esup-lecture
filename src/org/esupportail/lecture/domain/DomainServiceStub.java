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
import org.esupportail.lecture.exceptions.domain.ManagedCategoryNotLoadedException;
import org.esupportail.lecture.exceptions.domain.SourceNotLoadedException;

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
	 * @see org.esupportail.lecture.domain.DomainService#getConnectedUser(java.lang.String)
	 */
	public UserBean getConnectedUser(final UserProfile userProfile) {
		UserBean user = new UserBean(userProfile.getUserId());
		return user;
	}
	

	/**
	 * @see org.esupportail.lecture.domain.DomainService#getContext(java.lang.String, java.lang.String)
	 */
	public ContextBean getContext(@SuppressWarnings("unused") final UserProfile userProfile, final String contextId) {
		ContextBean ret = null;
		if (context.getId().equals(contextId)) {
			ret = context;
		}
		return ret;
	}
	
	/**
	 * @see org.esupportail.lecture.domain.DomainService#getDisplayedCategories(
	 *   java.lang.String, java.lang.String)
	 */
	public List<CategoryBean> getDisplayedCategories(final UserProfile userProfile, final String contextId) {
		return null;
	}
	
	/**
	 * @see org.esupportail.lecture.domain.DomainService#getDisplayedSources(
	 * java.lang.String, java.lang.String)
	 */
	public List<SourceBean> getDisplayedSources(final UserProfile userProfile, final String categoryId) {
		return null;
	}
	
	
	/**
	 * @see org.esupportail.lecture.domain.DomainService#getItems(
	 *   java.lang.String, java.lang.String)
	 */
	public List<ItemBean> getItems(@SuppressWarnings("unused") final String sourceId, 
			@SuppressWarnings("unused") final UserProfile userProfile) {
		return items;
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#marckItemReadMode(
	 *   java.lang.String, java.lang.String, java.lang.String, boolean)
	 */
	@SuppressWarnings("unused")
	public UserProfile marckItemReadMode(
			final UserProfile userProfile, final String itemId, final String sourceId, final boolean isRead) {
		// nothing
		return null;
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#getVisibleCategories(
	 *   java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unused")
	public List<CategoryBean> getVisibleCategories(
			final UserProfile userProfile, final String contextId) {
		List<CategoryBean> ret = null;
		ret = categories;
		return ret;
	}
	
	
	/**
	 * @param uid 
	 * @param categoryId 
	 * @return sources
	 * @see org.esupportail.lecture.domain.DomainService#getDisplayedSources(
	 *   java.lang.String, java.lang.String)
	 */
	public List<SourceBean> getVisibleSources(final UserProfile userProfile, final String categoryId) {
		List<SourceBean> ret = null;
		ret = sources;
		return ret;
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#setTreeSize(java.lang.String, java.lang.String, int)
	 */
	@SuppressWarnings("unused")
	public void setTreeSize(final UserProfile userProfile, final String contextId, final int size) {
		// nothing	
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#foldCategory(
	 *   java.lang.String, java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unused")
	public void foldCategory(final UserProfile userProfile, final String cxtId, final String catId) {
		// nothing
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#unfoldCategory(
	 *   java.lang.String, java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unused")
	public void unfoldCategory(final UserProfile userProfile, final String cxtId, final String catId) {
		// nothing
	}

	
	/**
	 * @see org.esupportail.lecture.domain.DomainService#subscribeToSource(
	 *   java.lang.String, java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unused")
	public void subscribeToSource(
		final UserProfile userProfile, final String categorieId, final String sourceId) {
		// nothing
	}
	
	/**
	 * @see org.esupportail.lecture.domain.DomainService#subscribeToCategory(
	 *   java.lang.String, java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unused")
	public void subscribeToCategory(final UserProfile userProfile, final String contextId, 
			final String categoryId) {
		// nothing
	}
		
	/**
	 * @see org.esupportail.lecture.domain.DomainService#unsubscribeToCategory(
	 *   java.lang.String, java.lang.String, java.lang.String)
	 */
	public void unsubscribeToCategory(
			@SuppressWarnings("unused") final UserProfile userProfile, 
			@SuppressWarnings("unused") final String contextId, 
			@SuppressWarnings("unused") final String categoryId) {
//		 nothing
	}
	/**
	 * @see org.esupportail.lecture.domain.DomainService#unsubscribeToSource(
	 *   java.lang.String, java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unused")
	public void unsubscribeToSource(
			final UserProfile userProfile, final String categorieId, final String sourceId) {
		// nothing
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#markItemDisplayMode(
	 *   java.lang.String, java.lang.String, org.esupportail.lecture.domain.model.ItemDisplayMode)
	 */
	@SuppressWarnings("unused")
	public void markItemDisplayMode(final UserProfile userProfile, final String sourceId, final ItemDisplayMode mode) 
	throws InternalDomainException {
		// nothing
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
	@SuppressWarnings("unused")
	public void setDatabaseVersion(final String version) {
		// nothing
		
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#isGuestMode()
	 */
	public boolean isGuestMode() {
		return false;
	}

	@Override
	public List<ItemBean> getItems(UserProfile userProfile, String sourceId)
			throws SourceNotLoadedException, InternalDomainException,
			ManagedCategoryNotLoadedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserProfile getUserProfile(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	



	

	/*
	 *************************** ACCESSORS ******************************** */	


}
