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
import org.esupportail.lecture.exceptions.domain.CategoryNotVisibleException;
import org.esupportail.lecture.exceptions.domain.CategoryObligedException;
import org.esupportail.lecture.exceptions.domain.CategoryOutOfReachException;
import org.esupportail.lecture.exceptions.domain.CategoryProfileNotFoundException;
import org.esupportail.lecture.exceptions.domain.CategoryTimeOutException;
import org.esupportail.lecture.exceptions.domain.ContextNotFoundException;
import org.esupportail.lecture.exceptions.domain.InternalDomainException;
import org.esupportail.lecture.exceptions.domain.ManagedCategoryProfileNotFoundException;
import org.esupportail.lecture.exceptions.domain.SourceNotVisibleException;
import org.esupportail.lecture.exceptions.domain.SourceProfileNotFoundException;
import org.esupportail.lecture.exceptions.domain.UserNotSubscribedToCategoryException;

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
	public UserBean getConnectedUser(final String userId) {
		UserBean user = new UserBean(userId);
		return user;
	}
	

	/**
	 * @see org.esupportail.lecture.domain.DomainService#getContext(java.lang.String, java.lang.String)
	 */
	public ContextBean getContext(@SuppressWarnings("unused") final String uid, final String contextId) {
		ContextBean ret = null;
		if (context.getId().equals(contextId)) {
			ret = context;
		}
		return ret;
	}
	
	/**
	 * @param uid
	 * @param contextId
	 * @param ex
	 * @return a contextBean
	 */
	public ContextBean getContext(final String uid, final String contextId, final ExternalService ex) {
		return getContext(uid, contextId);
	}


	/**
	 * @param contextId
	 * @param uid
	 * @return list of CategoryBean
	 */
	public List<CategoryBean> getAvailableCategories(final String contextId, final String uid) {
		List<CategoryBean> ret = null;
		ret = categories;
		return ret;
	}
	/**
	 * @see org.esupportail.lecture.domain.DomainService#getAvailableCategories(
	 *   java.lang.String, java.lang.String, org.esupportail.lecture.domain.ExternalService)
	 */
	public List<CategoryBean> getAvailableCategories(final String uid, final String contextId, 
			@SuppressWarnings("unused") final ExternalService ex) {
		return getAvailableCategories(contextId, uid);
	}
	
	/**
	 * @param categoryId
	 * @param uid
	 * @return list of sourceBean
	 */
	public List<SourceBean> getAvailableSources(final String categoryId, final String uid) {
		List<SourceBean> ret = null;
		ret = sources;
		return ret;
	}
	/**
	 * @see org.esupportail.lecture.domain.DomainService#getAvailableSources(
	 * java.lang.String, java.lang.String, org.esupportail.lecture.domain.ExternalService)
	 */
	public List<SourceBean> getAvailableSources(final String uid, final String categoryId, 
			@SuppressWarnings("unused") final ExternalService ex) {
		return getAvailableSources(categoryId, uid);
	}
	
	
	/**
	 * @see org.esupportail.lecture.domain.DomainService#getItems(
	 *   java.lang.String, java.lang.String, org.esupportail.lecture.domain.ExternalService)
	 */
	public List<ItemBean> getItems(@SuppressWarnings("unused") final String sourceId, 
			@SuppressWarnings("unused") final String uid, 
			@SuppressWarnings("unused") final ExternalService ex) {
		return items;
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#marckItemReadMode(
	 *   java.lang.String, java.lang.String, java.lang.String, boolean)
	 */
	@SuppressWarnings("unused")
	public void marckItemReadMode(
			final String uid, final String itemId, final String sourceId, final boolean isRead) {
		// nothing

	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#getVisibleCategories(
	 *   java.lang.String, java.lang.String, org.esupportail.lecture.domain.ExternalService)
	 */
	@SuppressWarnings("unused")
	public List<CategoryBean> getVisibleCategories(
			final String uid, final String contextId, final ExternalService ex) 
	throws ContextNotFoundException {
		List<CategoryBean> ret = null;
		ret = categories;
		return ret;
	}
	
	
	/**
	 * @param uid 
	 * @param categoryId 
	 * @param ex 
	 * @return sources
	 * @see org.esupportail.lecture.domain.DomainService#getAvailableSources(
	 *   java.lang.String, java.lang.String, org.esupportail.lecture.domain.ExternalService)
	 */
	public List<SourceBean> getVisibleSources(final String uid, final String categoryId, final ExternalService ex) {
		List<SourceBean> ret = null;
		ret = sources;
		return ret;
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#setTreeSize(java.lang.String, java.lang.String, int)
	 */
	@SuppressWarnings("unused")
	public void setTreeSize(final String uid, final String contextId, final int size) {
		// nothing	
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#foldCategory(
	 *   java.lang.String, java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unused")
	public void foldCategory(final String uid, final String cxtId, final String catId) {
		// nothing
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#unfoldCategory(
	 *   java.lang.String, java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unused")
	public void unfoldCategory(final String uid, final String cxtId, final String catId) {
		// nothing
	}

	
	/**
	 * @see org.esupportail.lecture.domain.DomainService#subscribeToSource(
	 *   java.lang.String, java.lang.String, java.lang.String, org.esupportail.lecture.domain.ExternalService)
	 */
	@SuppressWarnings("unused")
	public void subscribeToSource(
			final String uid, final String categorieId, final String sourceId, final ExternalService ex)
		throws UserNotSubscribedToCategoryException, ManagedCategoryProfileNotFoundException, 
		CategoryNotVisibleException, CategoryProfileNotFoundException, CategoryOutOfReachException, 
		SourceProfileNotFoundException, SourceNotVisibleException, 
		InternalDomainException {
		// nothing
	}
	
	/**
	 * @see org.esupportail.lecture.domain.DomainService#subscribeToCategory(
	 *   java.lang.String, java.lang.String, java.lang.String, org.esupportail.lecture.domain.ExternalService)
	 */
	@SuppressWarnings("unused")
	public void subscribeToCategory(final String uid, final String contextId, 
			final String categoryId, final ExternalService externalService) {
		// nothing
	}
		
	/**
	 * @see org.esupportail.lecture.domain.DomainService#unsubscribeToCategory(
	 *   java.lang.String, java.lang.String, java.lang.String, org.esupportail.lecture.domain.ExternalService)
	 */
	public void unsubscribeToCategory(
			@SuppressWarnings("unused") final String uid, 
			@SuppressWarnings("unused") final String contextId, 
			@SuppressWarnings("unused") final String categoryId, 
			@SuppressWarnings("unused") final ExternalService externalService) 
	throws ManagedCategoryProfileNotFoundException, ContextNotFoundException, 
	CategoryTimeOutException, CategoryNotVisibleException, CategoryOutOfReachException, 
	InternalDomainException, CategoryObligedException {
//		 nothing
	}
	/**
	 * @see org.esupportail.lecture.domain.DomainService#unsubscribeToSource(
	 *   java.lang.String, java.lang.String, java.lang.String, org.esupportail.lecture.domain.ExternalService)
	 */
	@SuppressWarnings("unused")
	public void unsubscribeToSource(
			final String uid, final String categorieId, final String sourceId, final ExternalService ex) {
		// nothing
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#markItemDisplayMode(
	 *   java.lang.String, java.lang.String, org.esupportail.lecture.domain.model.ItemDisplayMode)
	 */
	@SuppressWarnings("unused")
	public void markItemDisplayMode(final String uid, final String sourceId, final ItemDisplayMode mode) 
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

	



	

	/*
	 *************************** ACCESSORS ******************************** */	


}
