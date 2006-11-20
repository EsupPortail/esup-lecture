package org.esupportail.lecture.domain;

import java.util.List;

import org.esupportail.lecture.domain.beans.CategoryBean;
import org.esupportail.lecture.domain.beans.ContextBean;
import org.esupportail.lecture.domain.beans.ItemBean;
import org.esupportail.lecture.domain.beans.SourceBean;
import org.esupportail.lecture.domain.beans.UserBean;
import org.esupportail.lecture.exceptions.FatalException;

/**
 * @author bourges
 * Domain service interface
 */
public interface DomainService {

	/**
	 * @param uid
	 * @return UserBean
	 * @see FacadeService#getConnectedUser(String)
	 */
	UserBean getConnectedUser(String uid);
	/**
	 * @param uid
	 * @param contextId
	 * @return ContextBean
	 * @see FacadeService#getContext(String,String)
	 */
	ContextBean getContext(String uid,String contextId);

	/**
	 * @param uid
	 * @param contextId
	 * @param externalService access to external service 
	 * @return List<CategoryBean>
	 * @see FacadeService#getCategories(String, String)
	 */
	List<CategoryBean> getCategories(String uid,String contextId,ExternalService externalService);

	/**
	 * @param categoryId 
	 * @param uid 
	 * @return List<SourceBean>
	 * @see FacadeService#getSources(String, String)
	 */
	List<SourceBean> getSources(String uid,String categoryId);

	/**
	 * @param sourceId 
	 * @param uid 
	 * @return List<ItemBean>
	 * @see FacadeService#getItems(String, String)
	 */
	List<ItemBean> getItems( String uid,String sourceId);

	/**
	 * @param uid 
	 * @param itemId 
	 * @param sourceId 
	 * @see FacadeService#marckItemasRead(String, String, String)
	 */
	void marckItemasRead(String uid, String sourceId,String itemId);

	/**
	 * @param uid 
	 * @param itemId 
	 * @param sourceId 
	 * @see FacadeService#marckItemasUnread(String, String, String)
	 */
	void marckItemasUnread(String uid, String sourceId,String itemId);

}
