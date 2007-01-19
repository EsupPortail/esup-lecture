/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.beans.CategoryBean;
import org.esupportail.lecture.domain.beans.CategoryDummyBean;
import org.esupportail.lecture.domain.beans.ContextBean;
import org.esupportail.lecture.domain.beans.ItemBean;
import org.esupportail.lecture.domain.beans.SourceBean;
import org.esupportail.lecture.domain.beans.SourceDummyBean;
import org.esupportail.lecture.domain.beans.UserBean;
import org.esupportail.lecture.domain.model.Channel;
import org.esupportail.lecture.domain.model.CustomCategory;
import org.esupportail.lecture.domain.model.CustomContext;
import org.esupportail.lecture.domain.model.CustomSource;
import org.esupportail.lecture.domain.model.Item;
import org.esupportail.lecture.domain.model.ProfileAvailability;
import org.esupportail.lecture.domain.model.SourceProfile;
import org.esupportail.lecture.domain.model.UserProfile;
import org.esupportail.lecture.exceptions.domain.CategoryNotLoadedException;
import org.esupportail.lecture.exceptions.domain.CategoryNotVisibleException;
import org.esupportail.lecture.exceptions.domain.CategoryProfileNotFoundException;
import org.esupportail.lecture.exceptions.domain.ComputeItemsException;
import org.esupportail.lecture.exceptions.domain.ContextNotFoundException;
import org.esupportail.lecture.exceptions.domain.CustomCategoryNotFoundException;
import org.esupportail.lecture.exceptions.domain.CustomSourceNotFoundException;
import org.esupportail.lecture.exceptions.domain.InfoDomainException;
import org.esupportail.lecture.exceptions.domain.InternalDomainException;
import org.esupportail.lecture.exceptions.domain.ManagedCategoryProfileNotFoundException;
import org.esupportail.lecture.exceptions.domain.MappingNotFoundException;
import org.esupportail.lecture.exceptions.domain.SourceNotLoadedException;
import org.esupportail.lecture.exceptions.domain.SourceProfileNotFoundException;
import org.esupportail.lecture.exceptions.domain.TreeSizeErrorException;
import org.esupportail.lecture.exceptions.domain.Xml2HtmlException;
import org.springframework.util.Assert;

/**
 * Service implementation provided by domain layer
 * All of services are available for a user only if 
 * he has a customContext defined in his userProfile.
 * To have a customContext defined in a userProfile, the service
 * getContext must have been called one time (over several user session)
 * @author gbouteil
 * 
 * 
 *
 */
public class DomainServiceImpl implements DomainService {
	
	/*
	 ************************** PROPERTIES ******************************** */	
	
	/** 
	 * Main domain model class
	 */
	static Channel channel; 
	
	/**
	 * Log instance 
	 */
	protected static final Log log = LogFactory.getLog(DomainServiceImpl.class);

	/* 
	 ************************** INIT **********************************/

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() {
		Assert.notNull(channel,"property channel can not be null");
	}

	
	/*
	 ************************** Methodes - services - mode NORMAL ************************************/

	/**
	 * Return the user identified by userId
	 * @param userId user Id
	 * @return userBean
	 * @see org.esupportail.lecture.domain.DomainService#getConnectedUser(java.lang.String)
	 */
	public UserBean getConnectedUser(String userId) {
		if (log.isDebugEnabled()){
			log.debug("getConnectedUser("+userId+")");
		}
		
		/* User profile creation */
		UserProfile userProfile = channel.getUserProfile(userId);
		
		/* userBean creation */
		UserBean user = new UserBean(userProfile);
		
		return user;
	}

	/**
	 * Returns the contextBean corresponding to the context identified by contextId for user userId
	 * @param userId id of the current user
	 * @param contextId id of the context to get
	 * @return contextBean
	 * @throws ContextNotFoundException 
	 * @see org.esupportail.lecture.domain.DomainService#getContext(String,String)
	 */
	public ContextBean getContext(String userId,String contextId) throws ContextNotFoundException {
		if (log.isDebugEnabled()){
			log.debug("getContext("+userId+","+contextId+")");
		}
		
		ContextBean contextBean;
		/* Get current user profile and customContext */
		UserProfile userProfile = channel.getUserProfile(userId);
		CustomContext customContext = userProfile.getCustomContext(contextId);
		
		/* Make the contextUserBean to display */
		contextBean = new ContextBean(customContext);
		
		return contextBean;		
	}

	/**
	 * Returns a list of categoryBean - corresponding to available categories to display on interface
	 * into context contextId for user userId
	 * Available categories are one that user : 
	 * - is subscribed to (obliged or allowed or autoSubscribe)
	 * - has created (personal categories)
	 * @param userId id of the current user
	 * @param contextId  id of the current context 
	 * @param ex externalService
	 * @return a list of CategoryBean
	 * @throws ContextNotFoundException
	 * @see org.esupportail.lecture.domain.DomainService#getAvailableCategories(java.lang.String, java.lang.String, ExternalService)
	 */
	public List<CategoryBean> getAvailableCategories(String userId, String contextId,ExternalService ex) throws ContextNotFoundException {
		if (log.isDebugEnabled()){
			log.debug("getAvailableCategories("+userId+","+contextId+",externalService)");
		}
		
		/* Get current user profile and customContext */
		UserProfile userProfile = channel.getUserProfile(userId);
		CustomContext customContext = userProfile.getCustomContext(contextId);

		List<CategoryBean> listCategoryBean = new ArrayList<CategoryBean>();
		List<CustomCategory> customCategories = customContext.getSortedCustomCategories(ex);

		for(CustomCategory customCategory : customCategories){
			CategoryBean category;
			try {
				category = new CategoryBean(customCategory,customContext);
				listCategoryBean.add(category);
			} catch (InfoDomainException e) {
				log.error("Error on service 'getAvailableCategories(user "+userId+", context "+contextId+") : creation of a CategoryDummyBean");
				category = new CategoryDummyBean(e);
				listCategoryBean.add(category);
			} 
		}
	
		return listCategoryBean;
	}
	
	/**
	 * Returns a list of sourceBean - corresponding to available categories to display on interface
	 * into category categoryId for user userId
	 * Available sources are one that user : 
	 * - is subscribed to (obliged or allowed or autoSubscribe)
	 * - has created (personal sources)
	 * @param uid Id of the user
	 * @param categoryId id of the category to display sources
	 * @return a list of sourceBean
	 * @throws CategoryNotVisibleException 
	 * @throws CategoryProfileNotFoundException
	 * @throws InternalDomainException 
	 * @throws CategoryNotLoadedException 
	 * @see org.esupportail.lecture.domain.DomainService#getAvailableSources(java.lang.String, java.lang.String, org.esupportail.lecture.domain.ExternalService)
	 */
	public List<SourceBean> getAvailableSources(String uid, String categoryId,ExternalService ex) 
		throws CategoryNotVisibleException, CategoryProfileNotFoundException, InternalDomainException, CategoryNotLoadedException  {
		if (log.isDebugEnabled()){
			log.debug("getAvailableSources("+uid+","+categoryId+",externalService)");
		}
		
		List<SourceBean> listSourceBean = new ArrayList<SourceBean>();
		
		try {
			UserProfile userProfile = channel.getUserProfile(uid);
			CustomCategory customCategory = userProfile.getCustomCategory(categoryId,ex);
			List<CustomSource> customSources = customCategory.getSortedCustomSources(ex);
				
			for(CustomSource customSource : customSources){
				SourceBean source;
				try {
					source = new SourceBean(customSource);
					listSourceBean.add(source);
				} catch (InfoDomainException e) {
					log.error("Error on service 'getAvailableSources(user "+uid+", category "+categoryId+") : creation of a SourceDummyBean");
					source = new SourceDummyBean(e);
					listSourceBean.add(source);
				}
				
			}
		} catch (CustomCategoryNotFoundException e) {
			String errorMsg = "CustomCategoryNotFound for service 'getAvailableSources(user "+uid+", category "+categoryId+ ")";
			log.error(errorMsg);
			throw new InternalDomainException(errorMsg,e);
		}
		
		return listSourceBean;
	}

	

//	/**
//	 * @param customCategory
//	 * @param ex
//	 * @throws CategoryNotVisibleException 
//	 * @throws CategoryProfileNotFoundException 
//	 * @throws CategoryNotLoadedException 
//	 */
//	private List<SourceBean> getSortedCustomSourcesForCustomCategory(CustomCategory customCategory, ExternalService ex) 
//		throws CategoryProfileNotFoundException, CategoryNotVisibleException, CategoryNotLoadedException {
//		List<CustomSource> customSources = customCategory.getSortedCustomSources(ex);
//		int nbSources = customSources.size();
//		List<SourceBean> listSourceBean = new ArrayList<SourceBean>();
//		for(CustomSource customSource : customSources){
//			SourceBean source = new SourceBean(customSource);
//			listSourceBean.add(source);
//		}
//		return listSourceBean;
//	}
	
	

	/* see later */
	
	/** 
	 * Returns a list of itemBean - corresponding to items containing in source sourceId,
	 * in order to be displayed on user interface for user uid
	 * @param uid user Id
	 * @param sourceId source Id to display items
	 * @param ex externalService
	 * @return a list of itemBean
	 * @throws SourceNotLoadedException 
	 * @throws InternalDomainException 
	 * @throws SourceProfileNotFoundException 
	 * @throws CategoryNotLoadedException 
	 * @throws ManagedCategoryProfileNotFoundException
	 * @see org.esupportail.lecture.domain.DomainService#getItems(java.lang.String, java.lang.String, org.esupportail.lecture.domain.ExternalService)
	 */
	public List<ItemBean> getItems(String uid, String sourceId,ExternalService ex) 
		throws SourceNotLoadedException, InternalDomainException, ManagedCategoryProfileNotFoundException, CategoryNotLoadedException, SourceProfileNotFoundException {
		if (log.isDebugEnabled()){
			log.debug("getItems("+uid+","+sourceId+",externalService)");
		}
		
		List<ItemBean> listItemBean = new ArrayList<ItemBean>();
		try {
			
			/* Get current user profile and customCoategory */
			UserProfile userProfile = channel.getUserProfile(uid);
			CustomSource customSource;
			customSource = userProfile.getCustomSource(sourceId);
			List<Item> listItems;
			listItems = customSource.getItems(ex);

			for(Item item : listItems){
				ItemBean itemBean = new ItemBean(item,customSource);
				listItemBean.add(itemBean);
			}
			
		} catch (CustomSourceNotFoundException e) {
			String errorMsg = "CustomSourceNotFoundException for service 'getItems(user "+uid+", source "+sourceId+ ")";
			log.error(errorMsg);
			throw new InternalDomainException(errorMsg,e);
		} catch (MappingNotFoundException e) {
			String errorMsg = "MappingNotFoundException for service 'getItems(user "+uid+", source "+sourceId+ ")";
			log.error(errorMsg);
			throw new InternalDomainException(errorMsg,e);
		} catch (ComputeItemsException e) {
			String errorMsg = "ComputeItemsException for service 'getItems(user "+uid+", source "+sourceId+ ")";
			log.error(errorMsg);
			throw new InternalDomainException(errorMsg,e);
		} catch (Xml2HtmlException e) {
			String errorMsg = "Xml2HtmlException for service 'getItems(user "+uid+", source "+sourceId+ ")";
			log.error(errorMsg);
			throw new InternalDomainException(errorMsg,e);
		}
		
		return listItemBean;
	}



	/**
	 * Mark item as read for user uid
	 * @param uid user Id
	 * @param sourceId sourceId of the item
	 * @param itemId item Id
	 * @throws InternalDomainException 
	 * @see org.esupportail.lecture.domain.DomainService#marckItemAsRead(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void marckItemAsRead(String uid, String sourceId, String itemId) throws InternalDomainException {
		if (log.isDebugEnabled()){
			log.debug("marckItemAsRead("+uid+","+sourceId+","+itemId+")");
		}
		
		try {
			/* Get current user profile and customCoategory */
			UserProfile userProfile = channel.getUserProfile(uid);
			CustomSource customSource;
			customSource = userProfile.getCustomSource(sourceId);
			customSource.setItemAsRead(itemId);
		} catch (CustomSourceNotFoundException e) {
			String errorMsg = "CustomSourceNotFoundException for service 'marckItemAsRead(user "+uid+", source "+sourceId+ ", item "+itemId+ ")";
			log.error(errorMsg);
			throw new InternalDomainException(errorMsg,e);
		}
		
	}
	
	/**
	 * Mark item as unread
	 * @param uid user Id for user uid
	 * @param sourceId sourceId of the item
	 * @param itemId item Id
	 * @throws InternalDomainException 
	 * @see org.esupportail.lecture.domain.DomainService#marckItemAsUnread(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void marckItemAsUnread(String uid, String sourceId, String itemId) throws InternalDomainException {
		if (log.isDebugEnabled()){
			log.debug("marckItemAsUnread("+uid+","+sourceId+","+itemId+")");
		}
		
		try {
			/* Get current user profile and customCategory */
			UserProfile userProfile = channel.getUserProfile(uid);
			CustomSource customSource;
			customSource = userProfile.getCustomSource(sourceId);
			customSource.setItemAsUnRead(itemId);
		} catch (CustomSourceNotFoundException e) {
			String errorMsg = "CustomSourceNotFoundException for service 'marckItemAsUnread(user "+uid+", source "+sourceId+ ", item "+itemId+ ")";
			log.error(errorMsg);
			throw new InternalDomainException(errorMsg,e);
		}
		
	}


	/**
	 * Set the tree size of the customContext
	 * @param uid user Id for user uid
	 * @param contextId context Id
	 * @param size size to set
	 * @throws ContextNotFoundException 
	 * @throws TreeSizeErrorException
	 * @see org.esupportail.lecture.domain.DomainService#setTreeSize(java.lang.String, java.lang.String, int)
	 */
	public void setTreeSize(String uid, String contextId, int size) throws TreeSizeErrorException, ContextNotFoundException {
		if (log.isDebugEnabled()){
			log.debug("setTreeSize("+uid+","+contextId+","+size+")");
		}
		
		/* Get current user profile and customContext */
		UserProfile userProfile = channel.getUserProfile(uid);
		CustomContext customContext = userProfile.getCustomContext(contextId);
		customContext.modifyTreeSize(size);
		
	}


	/**
	 * Set category identified by catId as fold in the customContext ctxId
	 * for user uid
	 * @param uid user Id
	 * @param cxtId context Id 
	 * @param catId category Id
	 * @throws ContextNotFoundException
	 * @see org.esupportail.lecture.domain.DomainService#foldCategory(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void foldCategory(String uid, String cxtId, String catId) throws ContextNotFoundException {
		if (log.isDebugEnabled()){
			log.debug("foldCategory("+uid+","+cxtId+","+catId+")");
		}
		
		/* Get current user profile and customContext */
		UserProfile userProfile = channel.getUserProfile(uid);
		CustomContext customContext = userProfile.getCustomContext(cxtId);
		customContext.foldCategory(catId);
	}
	
	/**
	 * Set category identified by catId as unfold in the customContext ctxId
	 * for user uid
	 * @param uid user Id
	 * @param cxtId context Id 
	 * @param catId category Id
	 * @throws ContextNotFoundException
	 * @see org.esupportail.lecture.domain.DomainService#unfoldCategory(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void unfoldCategory(String uid, String cxtId, String catId) throws ContextNotFoundException {
		if (log.isDebugEnabled()){
			log.debug("unfoldCategory("+uid+","+cxtId+","+catId+")");
		}
		
		/* Get current user profile and customContext */
		UserProfile userProfile = channel.getUserProfile(uid);
		CustomContext customContext = userProfile.getCustomContext(cxtId);
		customContext.unfoldCategory(catId);
	}
	
	/*
	 ************************** Methodes - services - mode EDIT ************************************/
	
	/**
	 * Return visible sources (obliged, subscribed, obliged for managed source or personal source) of categoryId for user uid (for EDIT mode)
	 * @param categoryId id of category
	 * @param uid user ID
	 * @param ex access to externalService
	 * @return List of SourceBean
	 * @throws CategoryNotVisibleException 
	 * @throws CategoryProfileNotFoundException 
	 * @throws InternalDomainException 
	 * @throws CategoryNotLoadedException 
	 */
	public List<SourceBean> getVisibleSources(String uid, String categoryId, ExternalService ex) 
		throws CategoryNotVisibleException, CategoryProfileNotFoundException, InternalDomainException, CategoryNotLoadedException {
		if (log.isDebugEnabled()) {
			log.debug("getVisibleSources("+uid+","+categoryId+",ex)");
		}
		List<SourceBean> listSourceBean = new ArrayList<SourceBean>();
		
		try {
			UserProfile userProfile = channel.getUserProfile(uid);
			CustomCategory customCategory = userProfile.getCustomCategory(categoryId,ex);
			List<ProfileAvailability> couples = customCategory.getVisibleSources(ex);
			
			for(ProfileAvailability couple : couples){
				SourceBean source;
				//try {
					source = new SourceBean(couple);
					listSourceBean.add(source);
					////////////////
//				}catch (InfoDomainException e) {
//					log.error("Error on service 'getVisibleSources(user "+uid+", category "+categoryId+") : creation of a SourceDummyBean");
//					source = new SourceDummyBean(e);
//					listSourceBean.add(source);
//				}
			}			
		} catch (CustomCategoryNotFoundException e) {
			String errorMsg = "CustomCategoryNotFound for service 'getVisibleSources(user "+uid+", category "+categoryId+ ")";
			log.error(errorMsg);
			throw new InternalDomainException(errorMsg,e);
		}
		 return listSourceBean;
		
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#marckSourceAsSubscribed(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void marckSourceAsSubscribed(String uid, String contextId, String categorieId, String sourceId) {
		// TODO (RB --> GB) Auto-generated method stub
		
	}


	/**
	 * @see org.esupportail.lecture.domain.DomainService#marckSourceAsUnsubscribed(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void marckSourceAsUnsubscribed(String uid, String contextId, String categorieId, String sourceId) {
		// TODO (RB --> GB) Auto-generated method stub
		
	}
	
	
	
	/*
	 ************************** Accessors ************************************/
	
	/**
	 * @return channel
	 */
	public Channel getChannel() {
		// It could be static without spring 
		return channel;
	}

	/**
	 * @param channel
	 */
	public void setChannel(Channel channel) {
		// It could be static without spring 
		DomainServiceImpl.channel = channel;
	}

}
