/**
 * ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
 * http://sourcesup.cru.fr/projects/esup-lecture
 */
package org.esupportail.lecture.domain;

import java.util.List;

import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.lecture.domain.beans.CategoryBean;
import org.esupportail.lecture.domain.beans.ContextBean;
import org.esupportail.lecture.domain.beans.SourceBean;
import org.esupportail.lecture.domain.beans.UserBean;

/**
 * The domain service.
 */
abstract class FacadeService {
	
	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());
	private ExternalService externalService;
	private DomainService domainService;
	

	/**
	 * @return the current connected user
	 */
	UserBean getConnectedUser() {
		return externalService.getConnectedUser();
	}
	
	/**
	 * @return the current context id (portlet preference with name "context")
	 */
	String getCurrentContextId() {
		return externalService.getCurrentContextId();
	}
	
	/**
	 * @param contextId id of context
	 * @return ContextBean
	 */
	ContextBean getContext(String contextId) {
		return domainService.getContext(contextId);
	}
	
	/**
	 * @param contextId id of context
	 * @param uid user ID
	 * @return List of CategoryBean obliged or subscribed by a user in a context
	 */
//	List<CategoryBean> getCategories(String contextId, String uid);
	
	/**
	 * @param categoryId id of category
	 * @param uid user ID
	 * @return List of SourceBean obliged or subscribed by a user in a category
	 */
//	List<SourceBean> getSources(String categoryId, String uid);
	
	/**
	 * @param uid user ID
	 * @param itemId item id
	 * @param sourceId source if
	 * marck a Item form a source for a user as read
	 */
//	void marckItemasRead(String uid, String itemId, String sourceId);

	/**
	 * @param uid user ID
	 * @param itemId item id
	 * @param sourceId source if
	 * marck a Item form a source for a user as unread
	 */
//	void marckItemasUnread(String uid, String itemId, String sourceId);
}
