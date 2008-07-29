/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain;

import java.util.List;

import org.esupportail.commons.exceptions.ConfigException;
import org.esupportail.commons.services.application.Version;
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
import org.esupportail.lecture.exceptions.domain.ManagedCategoryProfileNotFoundException;
import org.esupportail.lecture.exceptions.domain.SourceNotLoadedException;
import org.esupportail.lecture.exceptions.domain.SourceNotVisibleException;
import org.esupportail.lecture.exceptions.domain.SourceObligedException;
import org.esupportail.lecture.exceptions.domain.SourceProfileNotFoundException;
import org.esupportail.lecture.exceptions.domain.SourceTimeOutException;
import org.esupportail.lecture.exceptions.domain.TreeSizeErrorException;
import org.esupportail.lecture.exceptions.domain.UserNotSubscribedToCategoryException;

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
	ContextBean getContext(String uid, String contextId) throws ContextNotFoundException;

	/**
	 * @param uid
	 * @param contextId
	 * @param ex access to external service 
	 * @return List<CategoryBean>
	 * @throws ContextNotFoundException 
	 * @see FacadeService#getDisplayedCategories(String, String)
	 */
	List<CategoryBean> getDisplayedCategories(String uid, String contextId, ExternalService ex) 
	throws ContextNotFoundException;

	/**
	 * @param categoryId 
	 * @param uid 
	 * @param ex 
	 * @return List<SourceBean>
	 * @throws CategoryNotVisibleException 
	 * @throws CategoryProfileNotFoundException 
	 * @throws InternalDomainException 
	 * @throws UserNotSubscribedToCategoryException 
	 * @throws CategoryTimeOutException 
	 * @throws CategoryOutOfReachException 
	 * @see FacadeService#getAvailableSources(String, String)
	 */
	List<SourceBean> getAvailableSources(String uid, String categoryId, ExternalService ex)  
	throws CategoryNotVisibleException, CategoryProfileNotFoundException, 
	InternalDomainException, UserNotSubscribedToCategoryException, CategoryTimeOutException, 
	CategoryOutOfReachException;

	/**
	 * @param sourceId 
	 * @param uid 
	 * @param ex access to external service 
	 * @return List<ItemBean>
	 * @throws SourceNotLoadedException 
	 * @throws InternalDomainException 
	 * @throws CategoryNotLoadedException 
	 * @throws ManagedCategoryProfileNotFoundException 
	 * @throws SourceTimeOutException 
	 * @see FacadeService#getItems(String, String)
	 */
	List<ItemBean> getItems(String uid, String sourceId, ExternalService ex)  
	throws SourceNotLoadedException, InternalDomainException, 
	ManagedCategoryProfileNotFoundException, CategoryNotLoadedException, SourceTimeOutException;

	/**
	 * @param uid 
	 * @param itemId 
	 * @param sourceId 
	 * @param isRead
	 * @throws InternalDomainException 
	 * @see FacadeService#marckItemReadMode(String, String, String, boolean)
	 */
	void marckItemReadMode(String uid, String sourceId, String itemId, boolean isRead)  
	throws InternalDomainException;

	/**
	 * @param uid
	 * @param sourceId
	 * @param mode
	 * @throws InternalDomainException 
	 * @see FacadeService#marckItemDisplayMode(String,String,ItemDisplayMode)
	 */
	void markItemDisplayMode(String uid, String sourceId, ItemDisplayMode mode) throws InternalDomainException;
	
	
	/**
	 * @param uid
	 * @param contextId
	 * @param ex
	 * @return List<CategoryBean>
	 * @throws ContextNotFoundException 
	 * @throws CategoryNotLoadedException 
	 * @see FacadeService#getVisibleCategories(String, String)
	 */
	public List<CategoryBean> getVisibleCategories(
			final String uid, final String contextId, final ExternalService ex) 
		throws ContextNotFoundException, CategoryNotLoadedException;
	
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
	 * @throws CategoryOutOfReachException 
	 * @throws UserNotSubscribedToCategoryException 
	 * @throws CategoryTimeOutException 
	 * @see FacadeService#getVisibleSources(String, String)
	 */
	List<SourceBean> getVisibleSources(String uid, String categoryId, ExternalService ex) 
	throws ManagedCategoryProfileNotFoundException, CategoryNotVisibleException, 
	CategoryProfileNotFoundException, InternalDomainException, CategoryOutOfReachException, 
	UserNotSubscribedToCategoryException, CategoryTimeOutException;
	
	/**
	 * @param uid
	 * @param contextId
	 * @param size
	 * @throws TreeSizeErrorException 
	 * @throws ContextNotFoundException 
	 * @see FacadeService#setTreeSize(String, String, int)
	 */
	void setTreeSize(String uid, String contextId, int size) 
	throws TreeSizeErrorException, ContextNotFoundException;
	
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
	 * @param contextId
	 * @param categoryId
	 * @param externalService
	 * @throws ManagedCategoryProfileNotFoundException 
	 * @throws ContextNotFoundException 
	 * @throws InternalDomainException 
	 * @throws CategoryOutOfReachException 
	 * @throws CategoryNotVisibleException 
	 * @throws CategoryTimeOutException 
	 * @see FacadeService#subscribeToCategory(String, String, String)
	 */
	void subscribeToCategory(String uid, String contextId, String categoryId, ExternalService externalService) 
		throws ManagedCategoryProfileNotFoundException, ContextNotFoundException, 
		CategoryTimeOutException, CategoryNotVisibleException, CategoryOutOfReachException, 
		InternalDomainException;

	/**
	 * @param uid
	 * @param contextId
	 * @param categoryId
	 * @param externalService
	 * @throws ManagedCategoryProfileNotFoundException 
	 * @throws ContextNotFoundException 
	 * @throws InternalDomainException 
	 * @throws CategoryOutOfReachException 
	 * @throws CategoryNotVisibleException 
	 * @throws CategoryTimeOutException 
	 * @throws CategoryObligedException 
	 * @see FacadeService#unsubscribeToCategory(String, String, String)
	 */
	void unsubscribeToCategory(String uid, String contextId, String categoryId, ExternalService externalService)
		throws ManagedCategoryProfileNotFoundException, ContextNotFoundException, 
		CategoryTimeOutException, CategoryNotVisibleException, CategoryOutOfReachException, 
		InternalDomainException, CategoryObligedException;
	/**
	 * @param uid 
	 * @param categorieId 
	 * @param sourceId 
	 * @param ex 
	 * @throws UserNotSubscribedToCategoryException 
	 * @throws ManagedCategoryProfileNotFoundException 
	 * @throws CategoryNotVisibleException 
	 * @throws SourceNotVisibleException 
	 * @throws SourceProfileNotFoundException 
	 * @throws CategoryProfileNotFoundException 
	 * @throws InternalDomainException 
	 * @throws CategoryTimeOutException 
	 * @throws CategoryOutOfReachException 
	 * @see FacadeService#subscribeToSource(String, String, String)
	 */
	void subscribeToSource(String uid, String categorieId, String sourceId, ExternalService ex) 
		throws UserNotSubscribedToCategoryException, ManagedCategoryProfileNotFoundException, 
		CategoryNotVisibleException, CategoryProfileNotFoundException, 
		SourceProfileNotFoundException, SourceNotVisibleException, InternalDomainException, 
		CategoryTimeOutException, CategoryOutOfReachException;
	/**
	 * @param uid 
	 * @param categorieId 
	 * @param sourceId 
	 * @param ex
	 * @throws CategoryNotVisibleException 
	 * @throws ManagedCategoryProfileNotFoundException 
	 * @throws UserNotSubscribedToCategoryException 
	 * @throws InternalDomainException 
	 * @throws SourceObligedException 
	 * @throws CategoryProfileNotFoundException 
	 * @throws CategoryTimeOutException 
	 * @throws CategoryOutOfReachException 
	 * @see FacadeService#unsubscribeToSource(String, String, String)
	 */	
	void unsubscribeToSource(String uid, String categorieId, String sourceId, ExternalService ex) 
		throws ManagedCategoryProfileNotFoundException, CategoryNotVisibleException, 
		UserNotSubscribedToCategoryException, InternalDomainException, 
		CategoryProfileNotFoundException, SourceObligedException, CategoryTimeOutException, 
		CategoryOutOfReachException;

	
	
	/**
	 * @return the database version.
	 * @throws ConfigException when the database is not initialized
	 */
	Version getDatabaseVersion() throws ConfigException;

	/**
	 * Set the database version.
	 * @param version 
	 */
	void setDatabaseVersion(String version);
	
	/**
	 * @return if application is used in guest mode
	 */
	boolean isGuestMode();
	
}
