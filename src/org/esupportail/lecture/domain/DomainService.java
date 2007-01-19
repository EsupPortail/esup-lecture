/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain;

import java.util.List;

import org.esupportail.lecture.domain.beans.CategoryBean;
import org.esupportail.lecture.domain.beans.ContextBean;
import org.esupportail.lecture.domain.beans.ItemBean;
import org.esupportail.lecture.domain.beans.SourceBean;
import org.esupportail.lecture.domain.beans.UserBean;
import org.esupportail.lecture.exceptions.domain.CategoryNotLoadedException;
import org.esupportail.lecture.exceptions.domain.CategoryNotVisibleException;
import org.esupportail.lecture.exceptions.domain.CategoryProfileNotFoundException;
import org.esupportail.lecture.exceptions.domain.ComputeFeaturesException;
import org.esupportail.lecture.exceptions.domain.ContextNotFoundException;
import org.esupportail.lecture.exceptions.domain.InternalDomainException;
import org.esupportail.lecture.exceptions.domain.ManagedCategoryProfileNotFoundException;
import org.esupportail.lecture.exceptions.domain.SourceNotLoadedException;
import org.esupportail.lecture.exceptions.domain.SourceNotVisibleException;
import org.esupportail.lecture.exceptions.domain.SourceProfileNotFoundException;
import org.esupportail.lecture.exceptions.domain.TreeSizeErrorException;
import org.esupportail.lecture.exceptions.domain.UserNotSubscribedToCategoryException;
import org.esupportail.lecture.exceptions.domain.VisibilityNotFoundException;

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
	 * @throws ContextNotFoundException 
	 * @see FacadeService#getContext(String,String)
	 */
	ContextBean getContext(String uid,String contextId) throws ContextNotFoundException ;

	/**
	 * @param uid
	 * @param contextId
	 * @param ex access to external service 
	 * @return List<CategoryBean>
	 * @throws ContextNotFoundException 
	 * @see FacadeService#getAvailableCategories(String, String)
	 */
	List<CategoryBean> getAvailableCategories(String uid,String contextId,ExternalService ex) throws ContextNotFoundException;

	/**
	 * @param categoryId 
	 * @param uid 
	 * @param ex 
	 * @return List<SourceBean>
	 * @throws CategoryNotVisibleException 
	 * @throws CategoryProfileNotFoundException 
	 * @throws InternalDomainException 
	 * @throws CategoryNotLoadedException 
	 * @throws UserNotSubscribedToCategoryException 
	 * @see FacadeService#getAvailableSources(String, String)
	 */
	List<SourceBean> getAvailableSources(String uid,String categoryId,ExternalService ex)  
		throws CategoryNotVisibleException, CategoryProfileNotFoundException, InternalDomainException, CategoryNotLoadedException, UserNotSubscribedToCategoryException ;

	/**
	 * @param sourceId 
	 * @param uid 
	 * @param ex access to external service 
	 * @return List<ItemBean>
	 * @throws SourceNotLoadedException 
	 * @throws InternalDomainException 
	 * @throws SourceProfileNotFoundException 
	 * @throws CategoryNotLoadedException 
	 * @throws ManagedCategoryProfileNotFoundException 
	 * @see FacadeService#getItems(String, String)
	 */
	List<ItemBean> getItems( String uid,String sourceId,ExternalService ex)  
		throws SourceNotLoadedException, InternalDomainException, ManagedCategoryProfileNotFoundException, CategoryNotLoadedException, SourceProfileNotFoundException;

	/**
	 * @param uid 
	 * @param itemId 
	 * @param sourceId 
	 * @throws InternalDomainException 
	 * @see FacadeService#marckItemAsRead(String, String, String)
	 */
	void marckItemAsRead(String uid, String sourceId,String itemId)  throws InternalDomainException;

	/**
	 * @param uid 
	 * @param itemId 
	 * @param sourceId 
	 * @throws InternalDomainException 
	 * @see FacadeService#marckItemAsUnread(String, String, String)
	 */
	void marckItemAsUnread(String uid, String sourceId,String itemId)  throws InternalDomainException;
	
	/**
	 * @param categoryId 
	 * @param uid 
	 * @param ex 
	 * @return List<SourceBean>
	 * @throws CategoryNotVisibleException 
	 * @throws ManagedCategoryProfileNotFoundException 
	 * @throws CategoryNotVisibleException 
	 * @throws ManagedCategoryProfileNotFoundException 
	 * @throws CategoryProfileNotFoundException 
	 * @throws InternalDomainException 
	 * @throws CategoryNotLoadedException 
	 * @throws UserNotSubscribedToCategoryException 
	 * @see FacadeService#getVisibleSources(String, String)
	 */
	List<SourceBean> getVisibleSources(String uid, String categoryId, ExternalService ex) 
		throws ManagedCategoryProfileNotFoundException, CategoryNotVisibleException, CategoryProfileNotFoundException, InternalDomainException, CategoryNotLoadedException, UserNotSubscribedToCategoryException ;
	
	/**
	 * @param uid
	 * @param contextId
	 * @param size
	 * @throws TreeSizeErrorException 
	 * @throws ContextNotFoundException 
	 * @see FacadeService#setTreeSize(String, String, int)
	 */
	void setTreeSize(String uid, String contextId, int size) throws TreeSizeErrorException, ContextNotFoundException;
	
	/**
	 * @param uid
	 * @param cxtId
	 * @param catId
	 * @throws ContextNotFoundException 
	 * @see FacadeService#foldCategory(String, String, String)
	 */
	void foldCategory(String uid, String cxtId, String catId) throws ContextNotFoundException;
	
	/**
	 * @param uid
	 * @param cxtId
	 * @param catId
	 * @throws ContextNotFoundException 
	 * @see FacadeService#unfoldCategory(String, String, String)
	 */
	void unfoldCategory(String uid, String cxtId, String catId) throws ContextNotFoundException;
	
	/**
	 * @param uid 
	 * @param categorieId 
	 * @param sourceId 
	 * @throws UserNotSubscribedToCategoryException 
	 * @throws CategoryNotVisibleException 
	 * @throws ManagedCategoryProfileNotFoundException 
	 * @throws CategoryNotVisibleException 
	 * @throws ManagedCategoryProfileNotFoundException 
	 * @throws SourceNotVisibleException 
	 * @throws SourceProfileNotFoundException 
	 * @throws CategoryNotLoadedException 
	 * @throws CategoryProfileNotFoundException 
	 * @throws ComputeFeaturesException 
	 * @throws SourceNotVisibleException 
	 * @throws SourceProfileNotFoundException 
	 * @throws CategoryNotLoadedException 
	 * @throws CategoryProfileNotFoundException 
	 * @throws VisibilityNotFoundException 
	 * @throws InternalDomainException 
	 * @see FacadeService#subscribeToSource(String, String, String)
	 */
	void subscribeToSource(String uid, String categorieId, String sourceId, ExternalService ex) 
		throws UserNotSubscribedToCategoryException, ManagedCategoryProfileNotFoundException, CategoryNotVisibleException, 
		CategoryProfileNotFoundException, CategoryNotLoadedException, SourceProfileNotFoundException, SourceNotVisibleException, 
		InternalDomainException ;
	/**
	 * @param uid 
	 * @param categorieId 
	 * @param sourceId 
	 * @see FacadeService#unsubscribeToSource(String, String, String)
	 */
	void unsubscribeToSource(String uid, String categorieId, String sourceId);

}
