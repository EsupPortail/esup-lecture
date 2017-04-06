/**
 * ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
 * http://sourcesup.cru.fr/projects/esup-lecture
 */
package org.esupportail.lecture.domain;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.model.ItemDisplayMode;
import org.esupportail.lecture.domain.model.UserProfile;
import org.esupportail.lecture.exceptions.domain.CategoryNotVisibleException;
import org.esupportail.lecture.exceptions.domain.CategoryObligedException;
import org.esupportail.lecture.exceptions.domain.CategoryTimeOutException;
import org.esupportail.lecture.exceptions.domain.InternalDomainException;
import org.esupportail.lecture.exceptions.domain.InternalExternalException;
import org.esupportail.lecture.exceptions.domain.NoExternalValueException;
import org.esupportail.lecture.exceptions.domain.SourceNotVisibleException;
import org.esupportail.lecture.exceptions.domain.SourceObligedException;
import org.esupportail.lecture.exceptions.domain.TreeSizeErrorException;
import org.esupportail.lecture.web.beans.ContextWebBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import antlr.collections.List;

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
        @Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(domainService, 
		"property domainService can not be null");
		Assert.notNull(externalService, 
		"property externalService can not be null");
	}
	

	/* 
	 ************************** SERVICES **********************************/
	
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
	public ContextWebBean getContext(String userId, String ctxId, boolean viewDef, int nbreArticle, String lienVue) {
		return domainService.getContext(userId, ctxId, viewDef, nbreArticle,lienVue);
	}

	public ContextWebBean getEditContext(String userId, String ctxId) {
		return domainService.getEditContext(userId, ctxId);
	}

	/**
	 * Mark item as read for user userProfile.
	 * @param userId user ID
	 * @param sourceId source if
	 * @param itemId item id
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
	
	public void markItemDisplayModeContext(final String userId, final String contextId,final boolean isUnreadMode) throws InternalDomainException{
		 domainService.markItemDisplayModeContext(userId, contextId, isUnreadMode);
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
	 * Subscribes category categoryId in Context contextId to user userProfile.
	 * @param userId
	 * @param contextId id of the context containing category
	 * @param categoryId id of the categoy
	 * @return userProfile
	 * @throws InternalDomainException 
	 * @throws CategoryNotVisibleException 
	 */
	public UserProfile subscribeToCategory(final String userId, final String contextId, final String categoryId) 
	throws CategoryNotVisibleException, InternalDomainException {
		return domainService.subscribeToCategory(userId, contextId, categoryId);
	}
	
	
	/**
	 * Subscribes user userProfile to source sourceId in Category categoryId.
	 * @param userId - user ID
	 * @param categorieId - categorie ID
	 * @param sourceId - Source ID
	 * @return userProfile
	 * @throws InternalDomainException s
	 * @throws SourceNotVisibleException 
	 * @throws CategoryTimeOutException 
	 * @throws CategoryNotVisibleException 
	 */
	public UserProfile subscribeToSource(final String userId, final String categorieId, final String sourceId) 
	throws CategoryNotVisibleException, CategoryTimeOutException, 
	SourceNotVisibleException, InternalDomainException {
		return domainService.subscribeToSource(userId, categorieId, sourceId);
	}
	
	/** 
	 * Unsubscribes category categoryId in Context contextId to user userProfile.
	 * @param userId
	 * @param contextId id of the context containing category
	 * @param categoryId id of the categoy
	 * @return userProfile
	 * @throws InternalDomainException 
	 * @throws CategoryObligedException 
	 * @throws CategoryNotVisibleException 
	 */
	public UserProfile unsubscribeToCategory(final String userId, final String contextId, final String categoryId) 
	throws CategoryNotVisibleException, CategoryObligedException, InternalDomainException {
		return domainService.unsubscribeToCategory(userId, contextId, categoryId);
	}

	/**
	 * Unsubscribes source sourceId in Category categoryId to user userProfile.
	 * @param userId - user ID
	 * @param categorieId - categorie ID
	 * @param sourceId - Source ID
	 * @return userProfile
	 * @throws InternalDomainException 
	 * @throws SourceObligedException 
	 * @throws CategoryTimeOutException 
	 * @throws CategoryNotVisibleException 
	 */
	public UserProfile unsubscribeToSource(final String userId, final String categorieId, final String sourceId) 
	throws CategoryNotVisibleException, CategoryTimeOutException, SourceObligedException, InternalDomainException {
		return domainService.unsubscribeToSource(userId, categorieId, sourceId);
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

	public boolean getCurrentViewDef() {
		return externalService.getCurrentViewDef();
	}


	public int getNombreArcticle() {
		return externalService.getNombreArticle();
	}
	
	public String getLienVue() {
		return externalService.getLienVue();
	}

}
