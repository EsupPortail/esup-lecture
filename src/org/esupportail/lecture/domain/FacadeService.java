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
import org.esupportail.lecture.exceptions.domain.DomainServiceException;
import org.esupportail.lecture.exceptions.domain.InternalDomainException;
import org.esupportail.lecture.exceptions.domain.InternalExternalException;
import org.esupportail.lecture.exceptions.domain.NoExternalValueException;
import org.esupportail.lecture.exceptions.domain.TreeSizeErrorException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * The facade service.
 * implement as an abstract class extended by FacadeServiceImpl
 */
/**
 * @author gbouteil
 *
 */
public class FacadeService implements InitializingBean {


	/*
	 ************************** PROPERTIES *********************************/	

	/**
	 * external service used to access portlet or servlet information
	 */
	private ExternalService externalService;
	/**
	 * domain service used to access domain information
	 */
	private DomainService domainService;
	/**
	 * Log instance 
	 */
	protected static final Log log = LogFactory.getLog(DomainServiceImpl.class);
	/* 
	 ************************** INIT **********************************/

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
	 * @return the current connected user
	 * @throws InternalExternalException 
	 */
	public String getConnectedUserId() throws InternalExternalException {
		try {
			return externalService.getConnectedUserId();
		} catch (NoExternalValueException e) {
			log.error("Service getConnectedUserId not available : (NoExternalValueException)"+e.getMessage());
			throw new InternalExternalException(e);
		} 
	}
	
	/**	
	 * @param uid id of connected user
	 * @return a UserBean of the current connected user
	 */
	public UserBean getConnectedUser(String uid) {
		return domainService.getConnectedUser(uid);
	}	
	/**
	 * 
	 * @return the current context id (portlet preference with name "context")
	 * @throws InternalExternalException 
	 */
	public String getCurrentContextId() throws InternalExternalException {
		try {
			return externalService.getCurrentContextId();
		} catch (NoExternalValueException e) {
			log.error("Service getCurrentContextId not available : (NoExternalValueException) "+e.getMessage());
			throw new InternalExternalException(e);
		} 
	}
	

	/**
	 * @param uid id of the connected user
	 * @param contextId id of the current context
	 * @return a ContextBean of the current context of the connected user
	 * @throws DomainServiceException 
	 */
	public ContextBean getContext(String uid,String contextId) throws DomainServiceException {
		return domainService.getContext(uid,contextId);
	}
	
	/**
	 * @param contextId id of context
	 * @param uid user ID
	 * @return List of CategoryBean, bean of a visible category (obliged or subscribed by a user) in a context
	 * @throws InternalDomainException 
	 */
	public List<CategoryBean> getVisibleCategories(String uid,String contextId) throws InternalDomainException {
		return domainService.getVisibleCategories(uid,contextId,externalService);
	}
	
	/**
	 * @param categoryId id of category
	 * @param uid user ID
	 * @return List of SourceBean, bean of a visible source (obliged or subscribed by a user) in a category
	 * @throws DomainServiceException 
	 */
	public List<SourceBean> getVisibleSources(String uid,String categoryId) throws DomainServiceException {
		return domainService.getVisibleSources(uid, categoryId,externalService);
	}
	
	/**
	 * @param categoryId id of category
	 * @param uid user ID
	 * @return List of SourceBean, bean of a available source (obliged, subscribed or notSubscribed by a user) in a category
	 */
	public List<SourceBean> getAvailableSources(String uid,String categoryId) {
		return domainService.getAvailableSources(uid, categoryId,externalService);
	}
	
	/**
	 * @param sourceId id of source
	 * @param uid user ID
	 * @return List of ItemBean in a source
	 * @throws DomainServiceException 
	 */
	public List<ItemBean> getItems(String uid,String sourceId) throws DomainServiceException {
		return domainService.getItems(uid,sourceId,externalService);
	}

	
	/**
	 * @param uid user ID
	 * @param itemId item id
	 * @param sourceId source if
	 * marck a Item form a source for a user as read
	 * @throws DomainServiceException 
	 */
	public void marckItemAsRead(String uid, String sourceId,String itemId) throws DomainServiceException {
		domainService.marckItemAsRead(uid, sourceId, itemId);
	}

	/**
	 * @param uid user ID
	 * @param itemId item id
	 * @param sourceId source if
	 * marck a Item form a source for a user as unread
	 * @throws DomainServiceException 
	 */
	public void marckItemAsUnread(String uid, String sourceId, String itemId) throws DomainServiceException {
		domainService.marckItemAsUnread(uid, sourceId, itemId);
	}

	/**
	 * @param uid user ID
	 * @param contextId context ID 
	 * @param size size of the tree between 0 - 100
	 * set tree size of the customContext refered by contextId 
	 * @throws TreeSizeErrorException 
	 */
	public void setTreeSize(String uid,String contextId,int size) throws TreeSizeErrorException {
		domainService.setTreeSize(uid,contextId, size);
	}

	/**
	 * @param uid  user ID
	 * @param cxtId context ID 
	 * @param catId catId
	 * set category catId folded in customContext cxtId
	 */
	public void foldCategory(String uid,String cxtId, String catId){
		domainService.foldCategory(uid,cxtId,catId);
	}
	
	/**
	 * @param uid  user ID
	 * @param cxtId context ID 
	 * @param catId catId
	 * set category catId unfolded in customContext cxtId
	 */
	public void unFoldCategory(String uid,String cxtId, String catId){
		domainService.unfoldCategory(uid,cxtId,catId);
	}
	
	/* 
	 ************************** ACCESSORS **********************************/

	/**
	 * @param domainService
	 */
	public void setDomainService(DomainService domainService) {
		this.domainService = domainService;
	}

	/**
	 * @param externalService
	 */
	public void setExternalService(ExternalService externalService) {
		this.externalService = externalService;
	}


	
}
