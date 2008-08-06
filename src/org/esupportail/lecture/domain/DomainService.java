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
import org.esupportail.lecture.exceptions.domain.CategoryNotVisibleException;
import org.esupportail.lecture.exceptions.domain.CategoryObligedException;
import org.esupportail.lecture.exceptions.domain.CategoryTimeOutException;
import org.esupportail.lecture.exceptions.domain.InternalDomainException;
import org.esupportail.lecture.exceptions.domain.ManagedCategoryNotLoadedException;
import org.esupportail.lecture.exceptions.domain.SourceNotLoadedException;
import org.esupportail.lecture.exceptions.domain.SourceNotVisibleException;
import org.esupportail.lecture.exceptions.domain.SourceObligedException;
import org.esupportail.lecture.exceptions.domain.TreeSizeErrorException;

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
	 * @throws InternalDomainException 
	 * @see FacadeService#getContext(String,String)
	 */
	ContextBean getContext(String uid, String contextId) throws InternalDomainException;

	/**
	 * @param uid
	 * @param contextId
	 * @return List(CategoryBean) 
	 * @throws InternalDomainException 
	 * @see FacadeService#getDisplayedCategories(String, String)
	 */
	List<CategoryBean> getDisplayedCategories(String uid, String contextId) throws InternalDomainException;

	/**
	 * @param categoryId 
	 * @param uid 
	 * @return List(SourceBean)
	 * @throws CategoryNotVisibleException  
	 * @throws InternalDomainException 
	 * @throws CategoryTimeOutException 
	 * @see FacadeService#getDisplayedSources(String, String)
	 */
	List<SourceBean> getDisplayedSources(String uid, String categoryId)  
	throws CategoryNotVisibleException, InternalDomainException, CategoryTimeOutException;

	/**
	 * @param sourceId 
	 * @param uid 
	 * @return List(ItemBean)
	 * @throws SourceNotLoadedException 
	 * @throws InternalDomainException 
	 * @throws ManagedCategoryNotLoadedException 
	 * @see FacadeService#getItems(String, String)
	 */
	List<ItemBean> getItems(String uid, String sourceId)  
	throws SourceNotLoadedException, InternalDomainException, ManagedCategoryNotLoadedException;

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
	 * @return List(CategoryBean)
	 * @throws InternalDomainException 
	 * @throws ManagedCategoryNotLoadedException 
	 * @see FacadeService#getVisibleCategories(String, String)
	 */
	List<CategoryBean> getVisibleCategories(final String uid, final String contextId) 
	throws InternalDomainException, ManagedCategoryNotLoadedException;
	
	/**
	 * @param categoryId 
	 * @param uid 
	 * @return List(SourceBean)
	 * @throws CategoryNotVisibleException 
	 * @throws InternalDomainException 
	 * @throws CategoryTimeOutException 
	 * @see FacadeService#getVisibleSources(String, String)
	 */
	List<SourceBean> getVisibleSources(String uid, String categoryId) 
	throws CategoryNotVisibleException, InternalDomainException, CategoryTimeOutException;
	
	/**
	 * @param uid
	 * @param contextId
	 * @param size
	 * @throws InternalDomainException 
	 * @throws TreeSizeErrorException 
	 * @see FacadeService#setTreeSize(String, String, int)
	 */
	void setTreeSize(String uid, String contextId, int size) throws InternalDomainException, TreeSizeErrorException;
	
	/**
	 * @param uid
	 * @param cxtId
	 * @param catId
	 * @throws InternalDomainException 
	 * @see FacadeService#foldCategory(String, String, String)
	 */
	void foldCategory(String uid, String cxtId, String catId) throws InternalDomainException;
	
	/**
	 * @param uid
	 * @param cxtId
	 * @param catId
	 * @throws InternalDomainException 
	 * @see FacadeService#unfoldCategory(String, String, String)
	 */
	void unfoldCategory(String uid, String cxtId, String catId) throws InternalDomainException;
	

	/**
	 * @param uid
	 * @param contextId
	 * @param categoryId
	 * @throws InternalDomainException 
	 * @throws CategoryNotVisibleException 
	 * @see FacadeService#subscribeToCategory(String, String, String)
	 */
	void subscribeToCategory(String uid, String contextId, String categoryId) 
	throws InternalDomainException, CategoryNotVisibleException;

	/**
	 * @param uid
	 * @param contextId
	 * @param categoryId
	 * @throws CategoryNotVisibleException 
	 * @throws InternalDomainException 
	 * @throws CategoryObligedException 
	 * @see FacadeService#unsubscribeToCategory(String, String, String)
	 */
	void unsubscribeToCategory(String uid, String contextId, String categoryId)
	throws InternalDomainException, CategoryNotVisibleException, CategoryObligedException;
	/**
	 * @param uid 
	 * @param categorieId 
	 * @param sourceId 
	 * @throws CategoryNotVisibleException 
	 * @throws InternalDomainException 
	 * @throws CategoryTimeOutException 
	 * @throws SourceNotVisibleException 
	 * @see FacadeService#subscribeToSource(String, String, String)
	 */
	void subscribeToSource(String uid, String categorieId, String sourceId) 
	throws CategoryNotVisibleException, InternalDomainException, 
	CategoryTimeOutException, SourceNotVisibleException;
	
	/**
	 * @param uid 
	 * @param categorieId 
	 * @param sourceId 
	 * @throws InternalDomainException 
	 * @throws CategoryNotVisibleException 
	 * @throws CategoryTimeOutException 
	 * @throws SourceObligedException 
	 * @see FacadeService#unsubscribeToSource(String, String, String)
	 */	
	void unsubscribeToSource(String uid, String categorieId, String sourceId) 
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
	
}
