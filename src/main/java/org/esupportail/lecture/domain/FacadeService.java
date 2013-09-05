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
import org.esupportail.lecture.web.beans.ContextWebBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

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
	 * return the user profile identified by "userId". 
	 * @param userId : identifient of the user profile
	 * @return the user profile
	 */ 
	public UserProfile getUserProfile(final String userId) {
		return domainService.getUserProfile(userId);
	}
	
	/**	
	 * Return the userBean identified by userProfile.
	 * @param userProfile id of connected user
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
	public String getCurrentContextId()  {
		try {
			return externalService.getCurrentContextId();
		} catch (NoExternalValueException e) {
			throw new RuntimeException(e);
		} catch (InternalExternalException e) {
			throw new RuntimeException(e);
		}
	}
	

	/**
	 * @param userId : User ID
	 * @param ctxId : Context ID
	 * @return Computed web bean context of the connected user.
	 */
	public ContextWebBean getContext(String userId, String ctxId) {
		return domainService.getContext(userId, ctxId);
	}

	/**
	 * Returns a list of categoryBean - corresponding to categories to display on interface.
	 * into context contextId for user userId
	 * Displayed categories are one that user : 
	 * - is subscribed to (obliged or allowed or autoSubscribe)
	 * - has created (personal categories)
	 * @param contextId id of context
	 * @param userProfile user ID
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
	 * @param userProfile user ID
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
	 * in order to be displayed on user interface for user userProfile
	 * @param userProfile user ID
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
	 * Mark item as read for user userProfile.
	 * @param userProfile user ID
	 * @param itemId item id
	 * @param sourceId source if
	 * @param isRead boolean : true = item is read | false = item is not read
	 * marck a Item form a source for a user as read
	 * @throws InternalDomainException 
	 */
	public void markItemReadMode(final String userId, final String sourceId, 
			final String itemId, final boolean isRead)
	throws InternalDomainException {
		domainService.markItemReadMode(userId, sourceId, itemId, isRead);
	}

	/**
	 * Mark the item display mode for source sourceId.
	 * @param userProfile user ID
	 * @param sourceId sourceID
	 * @param mode the item display mode to set
	 * @return userProfile
	 * @throws InternalDomainException 
	 */
	public UserProfile markItemDisplayMode(final UserProfile userProfile, final String sourceId, final ItemDisplayMode mode) 
			throws InternalDomainException {
		return domainService.markItemDisplayMode(userProfile, sourceId, mode);
	}
	
	/**
	 * Set the tree size of the customContext.
	 * @param userProfile user ID
	 * @param contextId context ID 
	 * @param size size of the tree 
	 * @return userProfile
	 * @throws InternalDomainException 
	 * @throws TreeSizeErrorException 
	 */
	public UserProfile setTreeSize(final UserProfile userProfile, final String contextId, final int size) 
	throws InternalDomainException, TreeSizeErrorException {
		return domainService.setTreeSize(userProfile, contextId, size);
	}

	/**
	 * Set category identified by catId as fold in the customContext ctxId.
	 * for user userProfile
	 * @param userProfile  user ID
	 * @param cxtId context ID 
	 * @param catId catId set category catId folded in customContext cxtId
	 * @return userProfile
	 * @throws InternalDomainException 
	 */
	public UserProfile foldCategory(final UserProfile userProfile, final String cxtId, final String catId) 
	throws InternalDomainException {
		return domainService.foldCategory(userProfile, cxtId, catId);
	}
	
	/**
	 * Set category identified by catId as unfold in the customContext ctxId.
	 * for user userProfile
	 * @param userProfile  user ID
	 * @param cxtId context ID 
	 * @param catId catId
	 * set category catId unfolded in customContext cxtId
	 * @return userProfile
	 * @throws InternalDomainException 
	 */
	public UserProfile unfoldCategory(final UserProfile userProfile, final String cxtId, final String catId) 
	throws InternalDomainException {
		return domainService.unfoldCategory(userProfile, cxtId, catId);
	}



	/** 
	 * Return visible categories.
	 * Obliged, subscribed, obliged for managed category or personal category.
	 * This for a contextId for user userProfile (for EDIT mode)
	 * @param userProfile
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
	 * This for a categoryId for user userProfile (for EDIT mode)
	 * @param categoryId id of category
	 * @param userProfile user ID
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
	 * Subscribes category categoryId in Context contextId to user userProfile.
	 * @param userProfile
	 * @param contextId id of the context containing category
	 * @param categoryId id of the categoy
	 * @return userProfile
	 * @throws InternalDomainException 
	 * @throws CategoryNotVisibleException 
	 */
	public UserProfile subscribeToCategory(final UserProfile userProfile, final String contextId, final String categoryId) 
	throws CategoryNotVisibleException, InternalDomainException {
		return domainService.subscribeToCategory(userProfile, contextId, categoryId);
	}
	
	
	/**
	 * Subscribes user userProfile to source sourceId in Category categoryId.
	 * @param userProfile - user ID
	 * @param categorieId - categorie ID
	 * @param sourceId - Source ID
	 * @return userProfile
	 * @throws InternalDomainException 
	 * @throws SourceNotVisibleException 
	 * @throws CategoryTimeOutException 
	 * @throws CategoryNotVisibleException 
	 */
	public UserProfile subscribeToSource(final UserProfile userProfile, final String categorieId, final String sourceId) 
	throws CategoryNotVisibleException, CategoryTimeOutException, 
	SourceNotVisibleException, InternalDomainException {
		return domainService.subscribeToSource(userProfile, categorieId, sourceId);
	}
	
	/** 
	 * Unsubscribes category categoryId in Context contextId to user userProfile.
	 * @param userProfile
	 * @param contextId id of the context containing category
	 * @param categoryId id of the categoy
	 * @return userProfile
	 * @throws InternalDomainException 
	 * @throws CategoryObligedException 
	 * @throws CategoryNotVisibleException 
	 */
	public UserProfile unsubscribeToCategory(final UserProfile userProfile, final String contextId, final String categoryId) 
	throws CategoryNotVisibleException, CategoryObligedException, InternalDomainException {
		return domainService.unsubscribeToCategory(userProfile, contextId, categoryId);
	}

	/**
	 * Unsubscribes source sourceId in Category categoryId to user userProfile.
	 * @param userProfile - user ID
	 * @param categorieId - categorie ID
	 * @param sourceId - Source ID
	 * @return userProfile
	 * @throws InternalDomainException 
	 * @throws SourceObligedException 
	 * @throws CategoryTimeOutException 
	 * @throws CategoryNotVisibleException 
	 */
	public UserProfile unsubscribeToSource(final UserProfile userProfile, final String categorieId, final String sourceId) 
	throws CategoryNotVisibleException, CategoryTimeOutException, SourceObligedException, InternalDomainException {
		return domainService.unsubscribeToSource(userProfile, categorieId, sourceId);
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
