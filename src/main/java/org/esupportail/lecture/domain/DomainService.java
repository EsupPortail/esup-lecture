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
import org.esupportail.lecture.domain.model.UserProfile;
import org.esupportail.lecture.exceptions.domain.CategoryNotVisibleException;
import org.esupportail.lecture.exceptions.domain.CategoryObligedException;
import org.esupportail.lecture.exceptions.domain.CategoryTimeOutException;
import org.esupportail.lecture.exceptions.domain.InternalDomainException;
import org.esupportail.lecture.exceptions.domain.ManagedCategoryNotLoadedException;
import org.esupportail.lecture.exceptions.domain.SourceNotLoadedException;
import org.esupportail.lecture.exceptions.domain.SourceNotVisibleException;
import org.esupportail.lecture.exceptions.domain.SourceObligedException;
import org.esupportail.lecture.exceptions.domain.TreeSizeErrorException;
import org.esupportail.lecture.web.beans.ContextWebBean;

/**
 * @author bourges
 * Domain service interface
 */
public interface DomainService {

	/**
	 * return the user profile identified by "userId". 
	 * @param userId : identifient of the user profile
	 * @return the user profile
	 */ 
	UserProfile getUserProfile(final String userId);

	/**
	 * @param userProfile
	 * @return UserBean
	 * @see FacadeService#getConnectedUser(UserProfile)
	 */	
	UserBean getConnectedUser(UserProfile userProfile);
	
	/**
	 * @param userId : User ID
	 * @param ctxId : Context ID
	 * @return Computed web bean context of the connected user.
	 */
	public ContextWebBean getContext(String userId, String ctxId);

	/**
	 * @param userProfile
	 * @param contextId
	 * @return List(CategoryBean) 
	 * @throws InternalDomainException 
	 * @see FacadeService#getDisplayedCategories(UserProfile, String)
	 */
	List<CategoryBean> getDisplayedCategories(UserProfile userProfile, String contextId) throws InternalDomainException;

	/**
	 * @param categoryId 
	 * @param userProfile 
	 * @return List(SourceBean)
	 * @throws CategoryNotVisibleException  
	 * @throws InternalDomainException 
	 * @throws CategoryTimeOutException 
	 * @see FacadeService#getDisplayedSources(UserProfile, String)
	 */
	List<SourceBean> getDisplayedSources(UserProfile userProfile, String categoryId)  
	throws CategoryNotVisibleException, InternalDomainException, CategoryTimeOutException;

	/**
	 * @param sourceId 
	 * @param userProfile 
	 * @return List(ItemBean)
	 * @throws SourceNotLoadedException 
	 * @throws InternalDomainException 
	 * @throws ManagedCategoryNotLoadedException 
	 * @see FacadeService#getItems(UserProfile, String)
	 */
	List<ItemBean> getItems(UserProfile userProfile, String sourceId)  
	throws SourceNotLoadedException, InternalDomainException, ManagedCategoryNotLoadedException;

	/**
	 * @param userId 
	 * @param itemId 
	 * @param sourceId 
	 * @param isRead
	 * @return hibernate modified UserProfile
	 * @throws InternalDomainException 
	 * @see FacadeService#marckItemReadMode(UserProfile, String, String, boolean)
	 */
	void markItemReadMode(String userId, String sourceId, String itemId, boolean isRead)  
	throws InternalDomainException;

	/**
	 * @param userProfile
	 * @param sourceId
	 * @param mode
	 * @return hibernate modified UserProfile
	 * @throws InternalDomainException 
	 * @see FacadeService#marckItemDisplayMode(UserProfile, String, ItemDisplayMode)
	 */
	UserProfile markItemDisplayMode(UserProfile userProfile, String sourceId, ItemDisplayMode mode) throws InternalDomainException;
	
	
	/**
	 * @param userProfile
	 * @param contextId
	 * @return List(CategoryBean)
	 * @throws InternalDomainException 
	 * @throws ManagedCategoryNotLoadedException 
	 * @see FacadeService#getVisibleCategories(UserProfile, String)
	 */
	List<CategoryBean> getVisibleCategories(final UserProfile userProfile, final String contextId) 
	throws InternalDomainException, ManagedCategoryNotLoadedException;
	
	/**
	 * @param categoryId 
	 * @param userProfile 
	 * @return List(SourceBean)
	 * @throws CategoryNotVisibleException 
	 * @throws InternalDomainException 
	 * @throws CategoryTimeOutException 
	 * @see FacadeService#getVisibleSources(UserProfile, String)
	 */
	List<SourceBean> getVisibleSources(UserProfile userProfile, String categoryId) 
	throws CategoryNotVisibleException, InternalDomainException, CategoryTimeOutException;
	
	/**
	 * @param userProfile
	 * @param contextId
	 * @param size
	 * @return hibernate modified UserProfile
	 * @throws InternalDomainException 
	 * @throws TreeSizeErrorException 
	 * @see FacadeService#setTreeSize(UserProfile, String, int)
	 */
	UserProfile setTreeSize(UserProfile userProfile, String contextId, int size) throws InternalDomainException, TreeSizeErrorException;
	
	/**
	 * @param userProfile
	 * @param cxtId
	 * @param catId
	 * @return hibernate modified UserProfile
	 * @throws InternalDomainException 
	 * @see FacadeService#foldCategory(UserProfile, String, String)
	 */
	UserProfile foldCategory(UserProfile userProfile, String cxtId, String catId) throws InternalDomainException;
	
	/**
	 * @param userProfile
	 * @param cxtId
	 * @param catId
	 * @return hibernate modified UserProfile
	 * @throws InternalDomainException 
	 * @see FacadeService#unfoldCategory(UserProfile, String, String)
	 */
	UserProfile unfoldCategory(UserProfile userProfile, String cxtId, String catId) throws InternalDomainException;
	

	/**
	 * @param userProfile
	 * @param contextId
	 * @param categoryId
	 * @return hibernate modified UserProfile
	 * @throws InternalDomainException 
	 * @throws CategoryNotVisibleException 
	 * @see FacadeService#subscribeToCategory(UserProfile, String, String)
	 */
	UserProfile subscribeToCategory(UserProfile userProfile, String contextId, String categoryId) 
	throws InternalDomainException, CategoryNotVisibleException;

	/**
	 * @param userProfile
	 * @param contextId
	 * @param categoryId
	 * @return hibernate modified UserProfile
	 * @throws CategoryNotVisibleException 
	 * @throws InternalDomainException 
	 * @throws CategoryObligedException 
	 * @see FacadeService#unsubscribeToCategory(UserProfile, String, String)
	 */
	UserProfile unsubscribeToCategory(UserProfile userProfile, String contextId, String categoryId)
	throws InternalDomainException, CategoryNotVisibleException, CategoryObligedException;
	/**
	 * @param userProfile 
	 * @param categorieId 
	 * @param sourceId 
	 * @return hibernate modified UserProfile
	 * @throws CategoryNotVisibleException 
	 * @throws InternalDomainException 
	 * @throws CategoryTimeOutException 
	 * @throws SourceNotVisibleException 
	 * @see FacadeService#subscribeToSource(UserProfile, String, String)
	 */
	UserProfile subscribeToSource(UserProfile userProfile, String categorieId, String sourceId) 
	throws CategoryNotVisibleException, InternalDomainException, 
	CategoryTimeOutException, SourceNotVisibleException;
	
	/**
	 * @param userProfile 
	 * @param categorieId 
	 * @param sourceId 
	 * @return hibernate modified UserProfile
	 * @throws InternalDomainException 
	 * @throws CategoryNotVisibleException 
	 * @throws CategoryTimeOutException 
	 * @throws SourceObligedException 
	 * @see FacadeService#unsubscribeToSource(UserProfile, String, String)
	 */	
	UserProfile unsubscribeToSource(UserProfile userProfile, String categorieId, String sourceId) 
	throws InternalDomainException, CategoryNotVisibleException, CategoryTimeOutException, SourceObligedException;

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

	/**
	 * @param query
	 */
	void updateSQL(String query);
	
}
