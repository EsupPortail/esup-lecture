/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain;


import org.esupportail.commons.exceptions.ConfigException;
import org.esupportail.commons.services.application.Version;
import org.esupportail.lecture.domain.model.ItemDisplayMode;
import org.esupportail.lecture.domain.model.UserProfile;
import org.esupportail.lecture.exceptions.domain.CategoryNotVisibleException;
import org.esupportail.lecture.exceptions.domain.CategoryObligedException;
import org.esupportail.lecture.exceptions.domain.CategoryTimeOutException;
import org.esupportail.lecture.exceptions.domain.InternalDomainException;
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
	 * @param userId : User ID
	 * @param ctxId : Context ID
	 * @return Computed web bean context of the connected user.
	 */
	public ContextWebBean getContext(String userId, String ctxId, boolean viewDef, int nombreArticle,String lienVue);

	/**
	 * @param userId : User ID
	 * @param ctxId : Context ID
	 * @return Computed web bean context of the connected user (in edit portlet mode).
	 */
	public ContextWebBean getEditContext(String userId, String ctxId);

	/**
	 * @param userId 
	 * @param itemId 
	 * @param sourceId 
	 * @param isRead
	 * @return hibernate modified UserProfile
	 * @throws InternalDomainException 
	 * @see FacadeService#marckItemReadMode(UserProfile, String, String, boolean)
	 */
	void markItemReadMode(String userId, String sourceId, String itemId, boolean isRead, boolean isPubliserMode)  
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
	 * @param userId
	 * @param contextId
	 * @param categoryId
	 * @return hibernate modified UserProfile
	 * @throws InternalDomainException 
	 * @throws CategoryNotVisibleException 
	 * @see FacadeService#subscribeToCategory(UserProfile, String, String)
	 */
	UserProfile subscribeToCategory(String userId, String contextId, String categoryId) 
	throws InternalDomainException, CategoryNotVisibleException;

	/**
	 * @param userId
	 * @param contextId
	 * @param categoryId
	 * @return hibernate modified UserProfile
	 * @throws CategoryNotVisibleException 
	 * @throws InternalDomainException 
	 * @throws CategoryObligedException 
	 * @see FacadeService#unsubscribeToCategory(UserProfile, String, String)
	 */
	UserProfile unsubscribeToCategory(String userId, String contextId, String categoryId)
	throws InternalDomainException, CategoryNotVisibleException, CategoryObligedException;
	/**
	 * @param userId 
	 * @param categorieId 
	 * @param sourceId 
	 * @return hibernate modified UserProfile
	 * @throws CategoryNotVisibleException 
	 * @throws InternalDomainException 
	 * @throws CategoryTimeOutException 
	 * @throws SourceNotVisibleException 
	 * @see FacadeService#subscribeToSource(UserProfile, String, String)
	 */
	UserProfile subscribeToSource(String userId, String categorieId, String sourceId) 
	throws CategoryNotVisibleException, InternalDomainException, 
	CategoryTimeOutException, SourceNotVisibleException;
	
	/**
	 * @param userId 
	 * @param categorieId 
	 * @param sourceId 
	 * @return hibernate modified UserProfile
	 * @throws InternalDomainException 
	 * @throws CategoryNotVisibleException 
	 * @throws CategoryTimeOutException 
	 * @throws SourceObligedException 
	 * @see FacadeService#unsubscribeToSource(UserProfile, String, String)
	 */	
	UserProfile unsubscribeToSource(String userId, String categorieId, String sourceId) 
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
