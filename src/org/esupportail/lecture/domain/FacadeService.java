/**
 * ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
 * http://sourcesup.cru.fr/projects/esup-lecture
 */
package org.esupportail.lecture.domain;

import java.util.List;

import org.esupportail.lecture.domain.beans.CategoryBean;
import org.esupportail.lecture.domain.beans.ContextBean;
import org.esupportail.lecture.domain.beans.ItemBean;
import org.esupportail.lecture.domain.beans.SourceBean;
import org.esupportail.lecture.domain.beans.UserBean;
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
	

	/* 
	 ************************** SERVICES **********************************/

	/**
	 * @return the current connected user
	 */
	public String getConnectedUserId() {
		return externalService.getConnectedUserId();
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
	 */
	public String getCurrentContextId() {
		return externalService.getCurrentContextId();
	}
	

	/**
	 * @param uid id of the connected user
	 * @param contextId id of the current context
	 * @return a ContextBean of the current context of the connected user
	 */
	public ContextBean getContext(String uid,String contextId) {
		return domainService.getContext(uid,contextId);
	}
	
	/**
	 * @param contextId id of context
	 * @param uid user ID
	 * @return List of CategoryBean obliged or subscribed by a user in a context
	 */
	public List<CategoryBean> getCategories(String uid,String contextId) {
		return domainService.getCategories(uid,contextId,externalService);
	}
	
	/**
	 * @param categoryId id of category
	 * @param uid user ID
	 * @return List of SourceBean obliged or subscribed by a user in a category
	 */
	// TODO tester
	public List<SourceBean> getSources(String uid,String categoryId) {
		return domainService.getSources(uid, categoryId,externalService);
	}
	
	/**
	 * @param sourceId id of source
	 * @param uid user ID
	 * @return List of ItemBean in a source
	 */
	public List<ItemBean> getItems(String sourceId,String uid) {
		return domainService.getItems(uid,sourceId,externalService);
	}

	
	/**
	 * @param uid user ID
	 * @param itemId item id
	 * @param sourceId source if
	 * marck a Item form a source for a user as read
	 */
	public void marckItemasRead(String uid, String sourceId,String itemId) {
		domainService.marckItemasRead(uid, sourceId, itemId);
	}

	/**
	 * @param uid user ID
	 * @param itemId item id
	 * @param sourceId source if
	 * marck a Item form a source for a user as unread
	 */
	public void marckItemasUnread(String uid, String sourceId, String itemId) {
		domainService.marckItemasUnread(uid, itemId, sourceId);
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
	
}
