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
import org.esupportail.lecture.exceptions.domain.CategoryNotLoadedException;
import org.esupportail.lecture.exceptions.domain.CategoryNotVisibleException;
import org.esupportail.lecture.exceptions.domain.CategoryObligedException;
import org.esupportail.lecture.exceptions.domain.CategoryOutOfReachException;
import org.esupportail.lecture.exceptions.domain.CategoryProfileNotFoundException;
import org.esupportail.lecture.exceptions.domain.CategoryTimeOutException;
import org.esupportail.lecture.exceptions.domain.ContextNotFoundException;
import org.esupportail.lecture.exceptions.domain.InternalDomainException;
import org.esupportail.lecture.exceptions.domain.InternalExternalException;
import org.esupportail.lecture.exceptions.domain.ManagedCategoryProfileNotFoundException;
import org.esupportail.lecture.exceptions.domain.NoExternalValueException;
import org.esupportail.lecture.exceptions.domain.SourceNotLoadedException;
import org.esupportail.lecture.exceptions.domain.SourceNotVisibleException;
import org.esupportail.lecture.exceptions.domain.SourceObligedException;
import org.esupportail.lecture.exceptions.domain.SourceProfileNotFoundException;
import org.esupportail.lecture.exceptions.domain.SourceTimeOutException;
import org.esupportail.lecture.exceptions.domain.TreeSizeErrorException;
import org.esupportail.lecture.exceptions.domain.UserNotSubscribedToCategoryException;
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
	 * Return ID of the current connected user to exetrnalService.
	 * @return the user ID
	 * @throws InternalExternalException
	 */
	public String getConnectedUserId() throws InternalExternalException {
		try {
			return externalService.getConnectedUserId();
		} catch (NoExternalValueException e) {
			String errorMsg = "Service getConnectedUserId not available : (NoExternalValueException)";
			LOG.error(errorMsg);
			throw new InternalExternalException(errorMsg, e);
		} 
	}
	
	/**	
	 * Return the userBean identified by uid.
	 * @param uid id of connected user
	 * @return a UserBean 
	 */
	public UserBean getConnectedUser(final String uid) {
		return domainService.getConnectedUser(uid);
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
	 * @throws ContextNotFoundException 
	 */
	public ContextBean getContext(final String uid, final String contextId) throws ContextNotFoundException  {
		return domainService.getContext(uid, contextId);
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
	 * @throws ContextNotFoundException
	 */
	public List<CategoryBean> getDisplayedCategories(final String uid, final String contextId) 
		throws ContextNotFoundException  {
		return domainService.getDisplayedCategories(uid, contextId, externalService);
	}
	
	/**
	 * Returns a list of sourceBean - corresponding to categories to display on interface.
	 * into category categoryId for user userId
	 * Displayed sources are one that user : 
	 * - is subscribed to (obliged or allowed or autoSubscribe)
	 * - has created (personal sources)
	 * @param categoryId id of category
	 * @param uid user ID
	 * @return List of SourceBean
	 * @throws InternalDomainException 
	 * @throws CategoryNotVisibleException 
	 * @throws CategoryProfileNotFoundException
	 * @throws UserNotSubscribedToCategoryException 
	 * @throws CategoryTimeOutException 
	 * @throws CategoryOutOfReachException 
	 */
	public List<SourceBean> getAvailableSources(final String uid, final String categoryId) 
		throws CategoryProfileNotFoundException, CategoryNotVisibleException, InternalDomainException, 
			UserNotSubscribedToCategoryException, CategoryTimeOutException, CategoryOutOfReachException {
		return domainService.getAvailableSources(uid, categoryId, externalService);
	}
	

	/**
	 * Returns a list of itemBean.
	 * Corresponding to items containing in source sourceId,
	 * in order to be displayed on user interface for user uid
	 * @param uid user ID
	 * @param sourceId id of source
	 * @return List of ItemBean in a source
	 * @throws InternalDomainException 
	 * @throws SourceNotLoadedException 
	 * @throws CategoryNotLoadedException 
	 * @throws ManagedCategoryProfileNotFoundException 
	 * @throws SourceTimeOutException 
	 */
	public List<ItemBean> getItems(final String uid, final String sourceId) 
	throws SourceNotLoadedException, InternalDomainException, ManagedCategoryProfileNotFoundException, 
			CategoryNotLoadedException, SourceTimeOutException {
		return domainService.getItems(uid, sourceId, externalService);
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
	public void marckItemReadMode(final String uid, final String sourceId, 
			final String itemId, final boolean isRead)
			throws InternalDomainException {
		domainService.marckItemReadMode(uid, sourceId, itemId, isRead);
	}

	/**
	 * Mark the item display mode for source sourceId.
	 * @param uid user ID
	 * @param sourceId sourceID
	 * @param mode the item display mode to set
	 * @throws InternalDomainException 
	 */
	public void marckItemDisplayMode(final String uid, final String sourceId, final ItemDisplayMode mode) 
			throws InternalDomainException {
		domainService.markItemDisplayMode(uid, sourceId, mode);
	}
	
	/**
	 * Set the tree size of the customContext.
	 * @param uid user ID
	 * @param contextId context ID 
	 * @param size size of the tree between 0 - 100
	 * @throws TreeSizeErrorException 
	 * @throws ContextNotFoundException 
	 */
	public void setTreeSize(final String uid, final String contextId, final int size) 
			throws ContextNotFoundException, TreeSizeErrorException {
		domainService.setTreeSize(uid, contextId, size);
	}

	/**
	 * Set category identified by catId as fold in the customContext ctxId.
	 * for user uid
	 * @param uid  user ID
	 * @param cxtId context ID 
	 * @param catId catId
	 * set category catId folded in customContext cxtId
	 * @throws ContextNotFoundException 
	 */
	public void foldCategory(final String uid, final String cxtId, final String catId) 
			throws ContextNotFoundException {
		domainService.foldCategory(uid, cxtId, catId);
	}
	
	/**
	 * Set category identified by catId as unfold in the customContext ctxId.
	 * for user uid
	 * @param uid  user ID
	 * @param cxtId context ID 
	 * @param catId catId
	 * set category catId unfolded in customContext cxtId
	 * @throws ContextNotFoundException 
	 */
	public void unfoldCategory(final String uid, final String cxtId, final String catId)
			throws ContextNotFoundException {
		domainService.unfoldCategory(uid, cxtId, catId);
	}



	/** 
	 * Return visible categories.
	 * Obliged, subscribed, obliged for managed category or personal category.
	 * This for a contextId for user uid (for EDIT mode)
	 * @param uid
	 * @param contextId
	 * @return List of CategoryBean
	 * @throws ContextNotFoundException
	 * @throws CategoryNotLoadedException 
	 */
	public List<CategoryBean> getVisibleCategories(final String uid, final String contextId) 
			throws ContextNotFoundException, CategoryNotLoadedException {
		return domainService.getVisibleCategories(uid, contextId, externalService);
	}
	
	/**
	 * Return visible sources.
	 * Obliged, subscribed, obliged for managed source or personal source.
	 * This for a categoryId for user uid (for EDIT mode)
	 * @param categoryId id of category
	 * @param uid user ID
	 * @return List of SourceBean
	 * @throws InternalDomainException 
	 * @throws CategoryNotVisibleException 
	 * @throws CategoryOutOfReachException 
	 * @throws CategoryProfileNotFoundException 
	 * @throws ManagedCategoryProfileNotFoundException 
	 * @throws UserNotSubscribedToCategoryException 
	 * @throws CategoryTimeOutException 
	 */
	public List<SourceBean> getVisibleSources(final String uid, final String categoryId) 
	throws ManagedCategoryProfileNotFoundException, CategoryProfileNotFoundException,
	CategoryOutOfReachException, CategoryNotVisibleException, InternalDomainException, 
	UserNotSubscribedToCategoryException, CategoryTimeOutException {
		return domainService.getVisibleSources(uid, categoryId, externalService);
	}
	
	/** 
	 * Subscribes category categoryId in Context contextId to user uid.
	 * @param uid
	 * @param contextId id of the context containing category
	 * @param categoryId id of the categoy
	 * @throws InternalDomainException 
	 * @throws CategoryOutOfReachException 
	 * @throws CategoryNotVisibleException 
	 * @throws CategoryTimeOutException 
	 * @throws ContextNotFoundException 
	 * @throws ManagedCategoryProfileNotFoundException 
	 */
	public void subscribeToCategory(final String uid, final String contextId, final String categoryId) 
	throws ManagedCategoryProfileNotFoundException, ContextNotFoundException, 
	CategoryTimeOutException, CategoryNotVisibleException, CategoryOutOfReachException, 
	InternalDomainException {
		domainService.subscribeToCategory(uid, contextId, categoryId, externalService);
	}
	
	
	/**
	 * Subscribes user uid to source sourceId in Category categoryId.
	 * @param uid - user ID
	 * @param categorieId - categorie ID
	 * @param sourceId - Source ID
	 * @throws InternalDomainException 
	 * @throws SourceNotVisibleException 
	 * @throws SourceProfileNotFoundException
	 * @throws CategoryNotVisibleException 
	 * @throws UserNotSubscribedToCategoryException 
	 * @throws CategoryOutOfReachException 
	 * @throws CategoryProfileNotFoundException 
	 * @throws ManagedCategoryProfileNotFoundException 
	 * @throws CategoryTimeOutException 
	 */
	public void subscribeToSource(final String uid, final String categorieId, final String sourceId) 
	throws ManagedCategoryProfileNotFoundException, CategoryProfileNotFoundException, 
	CategoryOutOfReachException, UserNotSubscribedToCategoryException, 
	CategoryNotVisibleException, SourceProfileNotFoundException, SourceNotVisibleException, 
	InternalDomainException, CategoryTimeOutException {
		domainService.subscribeToSource(uid, categorieId, sourceId, externalService);
	}
	
	/** 
	 * Unsubscribes category categoryId in Context contextId to user uid.
	 * @param uid
	 * @param contextId id of the context containing category
	 * @param categoryId id of the categoy
	 * @throws InternalDomainException 
	 * @throws CategoryOutOfReachException 
	 * @throws CategoryNotVisibleException 
	 * @throws CategoryTimeOutException 
	 * @throws ContextNotFoundException 
	 * @throws ManagedCategoryProfileNotFoundException 
	 * @throws CategoryObligedException 
	 */
	public void unsubscribeToCategory(final String uid, final String contextId, final String categoryId) 
	throws ManagedCategoryProfileNotFoundException, ContextNotFoundException, 
	CategoryTimeOutException, CategoryNotVisibleException, CategoryOutOfReachException, 
	InternalDomainException, CategoryObligedException {
		domainService.unsubscribeToCategory(uid, contextId, categoryId, externalService);
	}

	/**
	 * Unsubscribes source sourceId in Category categoryId to user uid.
	 * @param uid - user ID
	 * @param categorieId - categorie ID
	 * @param sourceId - Source ID
	 * @throws UserNotSubscribedToCategoryException 
	 * @throws CategoryNotVisibleException 
	 * @throws InternalDomainException 
	 * @throws SourceObligedException 
	 * @throws CategoryOutOfReachException 
	 * @throws CategoryProfileNotFoundException 
	 * @throws CategoryTimeOutException 
	 */
	public void unsubscribeToSource(final String uid, final String categorieId, final String sourceId) 
	throws CategoryNotVisibleException, UserNotSubscribedToCategoryException, 
	CategoryProfileNotFoundException, CategoryOutOfReachException, SourceObligedException, 
	InternalDomainException, CategoryTimeOutException {
		domainService.unsubscribeToSource(uid, categorieId, sourceId, externalService);
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
