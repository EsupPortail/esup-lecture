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
	 * @param userId
	 * @return UserBean
	 * @see FacadeService#getConnectedUser()
	 */
	UserBean getConnectedUser(String userId);
	/**
	 * @param contextId
	 * @return ContextBean
	 * @see FacadeService#getContext(String)
	 */
	ContextBean getContext(String uid,String contextId);

	/**
	 * @param contextId 
	 * @param uid 
	 * @return List<CategoryBean>
	 * @see FacadeService#getCategories(String, String)
	 */
	List<CategoryBean> getCategories(String uid,String contextId);

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
