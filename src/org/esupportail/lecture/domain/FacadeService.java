/**
 * ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
 * http://sourcesup.cru.fr/projects/esup-lecture
 */
package org.esupportail.lecture.domain;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.beans.CategoryBean;
import org.esupportail.lecture.domain.beans.ContextBean;
import org.esupportail.lecture.domain.beans.ItemBean;
import org.esupportail.lecture.domain.beans.SourceBean;
import org.esupportail.lecture.domain.beans.UserBean;
import org.esupportail.lecture.domain.model.ItemDisplayMode;
import org.esupportail.lecture.domain.model.UserProfile;
import org.esupportail.lecture.exceptions.domain.CategoryNotVisibleException;
import org.esupportail.lecture.exceptions.domain.CategoryObligedException;
import org.esupportail.lecture.exceptions.domain.CategoryTimeOutException;
import org.esupportail.lecture.exceptions.domain.InternalDomainException;
import org.esupportail.lecture.exceptions.domain.InternalExternalException;
import org.esupportail.lecture.exceptions.domain.ManagedCategoryNotLoadedException;
import org.esupportail.lecture.exceptions.domain.NoExternalValueException;
import org.esupportail.lecture.exceptions.domain.SourceNotLoadedException;
import org.esupportail.lecture.exceptions.domain.SourceNotVisibleException;
import org.esupportail.lecture.exceptions.domain.SourceObligedException;
import org.esupportail.lecture.exceptions.domain.TreeSizeErrorException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
// test svn

/**
 * The facade service.
 * It provide services from DomainService and external service
 */
/**
 * @author gbouteil
 *
 */
public class FacadeService implements InitializingBean {

	/*
	 ************************** PROPERTIES *********************************/	
	/**
	 * Log instance.
	 */
	protected static final Log LOG = LogFactory.getLog(FacadeService.class);
	
	/**
	 * External service  : used to access information from external service like portlet or servlet (or else).
	 */
	private ExternalService externalService;
	
	/**
	 * Domain service : used to access domain information.
	 */
	private DomainService domainService;

	/* 
	 ************************** INIT **********************************/

	/**
	 * default constructor.
	 */
	public FacadeService() {
		super();
	}


	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(domainService, 
		"property domainService can not be null");
		Assert.notNull(externalService, 
		"property externalService can not be null");
	}
	

	/* 
	 ************************** SERVICES **********************************/

	/**	
	 * Return the userBean identified by uid.
	 * @param uid id of connected user
	 * @return a UserBean 
	 */
	public UserBean getConnectedUser(final UserProfile userProfile) {
		return domainService.getConnectedUser(userProfile);
	}
	
	/** 
	 * Return ID of the current context in externalService.
	 * @return the id 
	 * @throws InternalExternalException 
	 */
	public String getCurrentContextId() throws InternalExternalException  {
		try {
			return externalService.getCurrentContextId();
		} catch (NoExternalValueException e) {
			String errorMsg = "Service getCurrentContextId not available : (NoExternalValueException)";
			LOG.error(errorMsg);
			throw new InternalExternalException(errorMsg, e);
		} 
	}
	

	/** 
	 * Returns the contextBean corresponding to the context identified by contextId for user uid.
	 * @param uid id of the connected user
	 * @param contextId id of the current context
	 * @return a ContextBean of the current context of the connected user
	 * @throws InternalDomainException 
	 */
	public ContextBean getContext(final UserProfile userProfile, final String contextId) throws InternalDomainException  {
		return domainService.getContext(userProfile, contextId);
	}
	
	/**
	 * Returns a list of categoryBean - corresponding to categories to display on interface.
	 * into context contextId for user userId
	 * Displayed categories are one that user : 
	 * - is subscribed to (obliged or allowed or autoSubscribe)
	 * - has created (personal categories)
	 * @param contextId id of context
	 * @param uid user ID
	 * @return List of CategoryBean
	 * @throws InternalDomainException
	 */
	public List<CategoryBean> getDisplayedCategories(final UserProfile userProfile, final String contextId) 
		throws InternalDomainException  {
		return domainService.getDisplayedCategories(userProfile, contextId);
	}
	
	/**
	 * Returns a list of sourceBean - corresponding to sources to display on interface.
	 * into category categoryId for user userId
	 * Displayed sources are one that user : 
	 * - is subscribed to (obliged or allowed or autoSubscribe)
	 * - has created (personal sources)
	 * @param categoryId id of category
	 * @param uid user ID
	 * @return List of SourceBean
	 * @throws InternalDomainException 
	 * @throws CategoryTimeOutException 
	 * @throws CategoryNotVisibleException 
	 * @throws InternalDomainException 
	 */
	public List<SourceBean> getDisplayedSources(final UserProfile userProfile, final String categoryId) 
	throws CategoryNotVisibleException, CategoryTimeOutException, InternalDomainException  {
		return domainService.getDisplayedSources(userProfile, categoryId);
	}
	

	/**
	 * Returns a list of itemBean.
	 * Corresponding to items containing in source sourceId,
	 * in order to be displayed on user interface for user uid
	 * @param uid user ID
	 * @param sourceId id of source
	 * @return List of ItemBean in a source
	 * @throws InternalDomainException 
	 * @throws ManagedCategoryNotLoadedException 
	 * @throws SourceNotLoadedException 
	 */
	public List<ItemBean> getItems(final UserProfile userProfile, final String sourceId) 
	throws SourceNotLoadedException, ManagedCategoryNotLoadedException, InternalDomainException {
		return domainService.getItems(userProfile, sourceId);
	}

	
	/**
	 * Mark item as read for user uid.
	 * @param uid user ID
	 * @param itemId item id
	 * @param sourceId source if
	 * @param isRead boolean : true = item is read | false = item is not read
	 * marck a Item form a source for a user as read
	 * @throws InternalDomainException 
	 */
	public void marckItemReadMode(final UserProfile userProfile, final String sourceId, 
			final String itemId, final boolean isRead)
	throws InternalDomainException {
		domainService.marckItemReadMode(userProfile, sourceId, itemId, isRead);
	}

	/**
	 * Mark the item display mode for source sourceId.
	 * @param uid user ID
	 * @param sourceId sourceID
	 * @param mode the item display mode to set
	 * @throws InternalDomainException 
	 */
	public void marckItemDisplayMode(final UserProfile userProfile, final String sourceId, final ItemDisplayMode mode) 
			throws InternalDomainException {
		domainService.markItemDisplayMode(userProfile, sourceId, mode);
	}
	
	/**
	 * Set the tree size of the customContext.
	 * @param uid user ID
	 * @param contextId context ID 
	 * @param size size of the tree 
	 * @throws InternalDomainException 
	 * @throws TreeSizeErrorException 
	 */
	public void setTreeSize(final UserProfile userProfile, final String contextId, final int size) 
	throws InternalDomainException, TreeSizeErrorException {
		domainService.setTreeSize(userProfile, contextId, size);
	}

	/**
	 * Set category identified by catId as fold in the customContext ctxId.
	 * for user uid
	 * @param uid  user ID
	 * @param cxtId context ID 
	 * @param catId catId
	 * set category catId folded in customContext cxtId
	 * @throws InternalDomainException 
	 */
	public void foldCategory(final UserProfile userProfile, final String cxtId, final String catId) 
	throws InternalDomainException {
		domainService.foldCategory(userProfile, cxtId, catId);
	}
	
	/**
	 * Set category identified by catId as unfold in the customContext ctxId.
	 * for user uid
	 * @param uid  user ID
	 * @param cxtId context ID 
	 * @param catId catId
	 * set category catId unfolded in customContext cxtId
	 * @throws InternalDomainException 
	 */
	public void unfoldCategory(final UserProfile userProfile, final String cxtId, final String catId) 
	throws InternalDomainException {
		domainService.unfoldCategory(userProfile, cxtId, catId);
	}



	/** 
	 * Return visible categories.
	 * Obliged, subscribed, obliged for managed category or personal category.
	 * This for a contextId for user uid (for EDIT mode)
	 * @param uid
	 * @param contextId
	 * @return List of CategoryBean	 
	 * @throws InternalDomainException 
	 * @throws ManagedCategoryNotLoadedException */
	public List<CategoryBean> getVisibleCategories(final UserProfile userProfile, final String contextId) 
	throws ManagedCategoryNotLoadedException, InternalDomainException  {
		return domainService.getVisibleCategories(userProfile, contextId);
	}
	
	/**
	 * Return visible sources.
	 * Obliged, subscribed, obliged for managed source or personal source.
	 * This for a categoryId for user uid (for EDIT mode)
	 * @param categoryId id of category
	 * @param uid user ID
	 * @return List of SourceBean
	 * @throws InternalDomainException 
	 * @throws CategoryTimeOutException 
	 * @throws CategoryNotVisibleException 
	 */
	public List<SourceBean> getVisibleSources(final UserProfile userProfile, final String categoryId) 
	throws CategoryNotVisibleException, CategoryTimeOutException, InternalDomainException {
		return domainService.getVisibleSources(userProfile, categoryId);
	}
	
	/** 
	 * Subscribes category categoryId in Context contextId to user uid.
	 * @param uid
	 * @param contextId id of the context containing category
	 * @param categoryId id of the categoy
	 * @throws InternalDomainException 
	 * @throws CategoryNotVisibleException 
	 */
	public void subscribeToCategory(final UserProfile userProfile, final String contextId, final String categoryId) 
	throws CategoryNotVisibleException, InternalDomainException {
		domainService.subscribeToCategory(userProfile, contextId, categoryId);
	}
	
	
	/**
	 * Subscribes user uid to source sourceId in Category categoryId.
	 * @param uid - user ID
	 * @param categorieId - categorie ID
	 * @param sourceId - Source ID
	 * @throws InternalDomainException 
	 * @throws SourceNotVisibleException 
	 * @throws CategoryTimeOutException 
	 * @throws CategoryNotVisibleException 
	 */
	public void subscribeToSource(final UserProfile userProfile, final String categorieId, final String sourceId) 
	throws CategoryNotVisibleException, CategoryTimeOutException, 
	SourceNotVisibleException, InternalDomainException {
		domainService.subscribeToSource(userProfile, categorieId, sourceId);
	}
	
	/** 
	 * Unsubscribes category categoryId in Context contextId to user uid.
	 * @param uid
	 * @param contextId id of the context containing category
	 * @param categoryId id of the categoy
	 * @throws InternalDomainException 
	 * @throws CategoryObligedException 
	 * @throws CategoryNotVisibleException 
	 */
	public void unsubscribeToCategory(final UserProfile userProfile, final String contextId, final String categoryId) 
	throws CategoryNotVisibleException, CategoryObligedException, InternalDomainException {
		domainService.unsubscribeToCategory(userProfile, contextId, categoryId);
	}

	/**
	 * Unsubscribes source sourceId in Category categoryId to user uid.
	 * @param uid - user ID
	 * @param categorieId - categorie ID
	 * @param sourceId - Source ID
	 * @throws InternalDomainException 
	 * @throws SourceObligedException 
	 * @throws CategoryTimeOutException 
	 * @throws CategoryNotVisibleException 
	 */
	public void unsubscribeToSource(final UserProfile userProfile, final String categorieId, final String sourceId) 
	throws CategoryNotVisibleException, CategoryTimeOutException, SourceObligedException, InternalDomainException {
		domainService.unsubscribeToSource(userProfile, categorieId, sourceId);
	}
	
	/**
	 * @return if application is used in guest mode
	 */
	public boolean isGuestMode() {
		return domainService.isGuestMode();
	}
	
	
	/* 
	 ************************** ACCESSORS **********************************/

	/**
	 * @param domainService
	 */
	public void setDomainService(final DomainService domainService) {
		this.domainService = domainService;
	}

	/**
	 * @param externalService
	 */
	public void setExternalService(final ExternalService externalService) {
		this.externalService = externalService;
	}

	/**
	 * @return ExternalService
	 */
	public ExternalService getExternalService() {
		return externalService;
	}


}
