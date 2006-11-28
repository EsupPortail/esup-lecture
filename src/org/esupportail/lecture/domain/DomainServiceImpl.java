package org.esupportail.lecture.domain;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.beans.CategoryBean;
import org.esupportail.lecture.domain.beans.ContextBean;
import org.esupportail.lecture.domain.beans.ItemBean;
import org.esupportail.lecture.domain.beans.SourceBean;
import org.esupportail.lecture.domain.beans.UserBean;
import org.esupportail.lecture.domain.model.Channel;
import org.esupportail.lecture.domain.model.Context;
import org.esupportail.lecture.domain.model.CustomCategory;
import org.esupportail.lecture.domain.model.CustomContext;
import org.esupportail.lecture.domain.model.CustomSource;
import org.esupportail.lecture.domain.model.Item;
import org.esupportail.lecture.domain.model.UserProfile;
import org.esupportail.lecture.exceptions.CategoryNotLoadedException;
import org.esupportail.lecture.exceptions.CategoryProfileNotFoundException;
import org.esupportail.lecture.exceptions.ElementNotLoadedException;
import org.esupportail.lecture.exceptions.ContextNotFoundException;
import org.esupportail.lecture.exceptions.CustomCategoryNotFoundException;
import org.esupportail.lecture.exceptions.CustomSourceNotFoundException;
import org.esupportail.lecture.exceptions.ManagedCategoryProfileNotFoundException;
import org.esupportail.lecture.exceptions.ServiceException;
import org.esupportail.lecture.exceptions.SourceNotLoadedException;

/**
 * Service implementation offered by domain layer
 * @author gbouteil
 *
 */
public class DomainServiceImpl implements DomainService {
	/** 
	 * Main domain model class
	 */
	static Channel channel; 
	
	/**
	 * Log instance 
	 */
	protected static final Log log = LogFactory.getLog(DomainServiceImpl.class);

	/*
	 ************************** Initialization ************************************/
	
	//TODO (GB) setAfterProperties

	
	/*
	 ************************** Methodes - services  ************************************/

	/**
	 * @see org.esupportail.lecture.domain.DomainService#getConnectedUser(java.lang.String)
	 */
	public UserBean getConnectedUser(String userId) {
		/* User profile creation */
		UserProfile userProfile = channel.getUserProfile(userId);
		
		/* userBean creation */
		UserBean user = new UserBean();
		user.setUid(userProfile.getUserId());
		
		return user;
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#getContext(java.lang.String, java.lang.String, org.esupportail.lecture.domain.ExternalService)
	 */
	public ContextBean getContext(String userId,String contextId) {
		
		/* Get current user profile and customContext */
		UserProfile userProfile = channel.getUserProfile(userId);
		CustomContext customContext = userProfile.getCustomContext(contextId);
		
		/* Make the contextUserBean to display */
		ContextBean contextBean;
		try {
			contextBean = new ContextBean(customContext);
		} catch (ContextNotFoundException e) {
			log.error("Context not found for service 'getContext(user "+userId+", context "+contextId);
			throw new ServiceException(e);
		}
		
		// TODO (GB) mise à jour du DAO ?
//		DomainTools.getDaoService().updateCustomContext(customContext);
//		DomainTools.getDaoService().updateUserProfile(userProfile);
		
		return contextBean;		
	}

	/**
	 * @throws ServiceException 
	 * @see org.esupportail.lecture.domain.DomainService#getVisibleCategories(java.lang.String, java.lang.String, ExternalService)
	 */
	public List<CategoryBean> getVisibleCategories(String userId, String contextId,ExternalService externalService) throws ServiceException {
		
		/* Get current user profile and customContext */
		UserProfile userProfile = channel.getUserProfile(userId);
		CustomContext customContext = userProfile.getCustomContext(contextId);

		List<CategoryBean> listCategoryBean = new ArrayList<CategoryBean>();
		try {
			List<CustomCategory> customCategories = customContext.getSortedCustomCategories(externalService);

			for(CustomCategory customCategory : customCategories){
				CategoryBean category = new CategoryBean(customCategory);
				listCategoryBean.add(category);
				// TODO (GB) mise à jour du DAO ?
//				DomainTools.getDaoService().updateCustomCategory(customCategory);
			}
		} catch (ContextNotFoundException e){
			log.error("Context not found for service 'getVisibleCategories(user "+userId+", context "+contextId);
			throw new ServiceException(e);
		} catch (ElementNotLoadedException e) {
			log.error("Context not found for service 'getVisibleCategories(user "+userId+", context "+contextId);
			throw new ServiceException(e);
		} catch (CategoryProfileNotFoundException e) {
			log.error("ManagedCategoryProfile not found for service 'getVisibleCategories(user "+userId+", context "+contextId);
			throw new ServiceException(e);
		} 
		
		// TODO (GB) mise à jour du DAO ?
//		DomainTools.getDaoService().updateCustomContext(customContext);
//		DomainTools.getDaoService().updateUserProfile(userProfile);
		return listCategoryBean;
	}
	
	/**
	 * @throws ServiceException 
	 * @see org.esupportail.lecture.domain.DomainService#getVisibleSources(java.lang.String, java.lang.String, org.esupportail.lecture.domain.ExternalService)
	 */
	public List<SourceBean> getVisibleSources(String uid, String categoryId,ExternalService externalService) throws ServiceException {
		
		/* Get current user profile and customCoategory */
		UserProfile userProfile = channel.getUserProfile(uid);
		CustomCategory customCategory;

		List<SourceBean> listSourceBean = new ArrayList<SourceBean>();
		try {
			customCategory = userProfile.getCustomCategory(categoryId);
			List<CustomSource> customSources = customCategory.getSortedCustomSources(externalService);
			int nbSources = customSources.size();
			log.debug("NB sources : "+nbSources);
			for(CustomSource customSource : customSources){
				SourceBean source = new SourceBean(customSource);
				listSourceBean.add(source);
				// TODO (GB) mise à jour du DAO ?
//				DomainTools.getDaoService().updateCustomSource(customSource);
			}	

			// TODO (GB) mise à jour du DAO ?
//			DomainTools.getDaoService().updateUserProfile(userProfile);
//			DomainTools.getDaoService().updateCustomCategory(customCategory);		
		
		
		}catch(CustomCategoryNotFoundException e){
			log.error("CustomCategory not found for service 'getVisibleSources(user "+uid+", category "+categoryId+ ")'");
			throw new ServiceException(e);
		} catch (CategoryProfileNotFoundException e){
			log.error("ManagedCategoryProfile not found for service 'getVisibleSources(user "+uid+", category "+categoryId+ ")'");
			throw new ServiceException(e);
		} catch (CategoryNotLoadedException e) {	
			log.error("Category is not loaded for service 'getVisibleSources(user "+uid+", category "+categoryId+ ")'");
			throw new ServiceException(e);
		} catch (ElementNotLoadedException e) {
			log.error("Element is not loaded for service 'getVisibleSources(user "+uid+", category "+categoryId+ ")'");
			throw new ServiceException(e);
		}
		return listSourceBean;
	}
	
	

	/* see later */
	
	/**
	 * @see org.esupportail.lecture.domain.DomainService#getItems(java.lang.String, java.lang.String, org.esupportail.lecture.domain.ExternalService)
	 */
	public List<ItemBean> getItems(String uid, String sourceId,ExternalService externalService) {
		/* Get current user profile and customCoategory */
		UserProfile userProfile = channel.getUserProfile(uid);
		// TODO (GB) why not customCategories ?
		CustomSource customSource;
		try {
			customSource = userProfile.getCustomSource(sourceId);
		} catch (CustomSourceNotFoundException e) {
			log.error("CustomSource is not found for service 'getItems(user "+uid+", source "+sourceId+ ")'");
			throw new ServiceException(e);
		}
	
			List<ItemBean> listItemBean = new ArrayList<ItemBean>();
		
			List<Item> items;
			try {
				items = customSource.getItems(externalService);
			} catch (ElementNotLoadedException e) {
				log.error("Composant is not loaded for service 'getItems(user "+uid+", source "+sourceId+ ")'");
				throw new ServiceException(e);
			} catch (SourceNotLoadedException e) {
				log.error("Source is not loaded for service 'getItems(user "+uid+", source "+sourceId+ ")'");
				throw new ServiceException(e);
			}
			for(Item item : items){
				ItemBean itemBean = new ItemBean(item,customSource);
				listItemBean.add(itemBean);
			// 	TODO (GB) mise à jour du DAO ?
//				DomainTools.getDaoService().updateCustomSource(customSource);
			
			}
		// 	TODO (GB) mise à jour du DAO ?
//			DomainTools.getDaoService().updateUserProfile(userProfile);
//			DomainTools.getDaoService().updateCustomCategory(customCategory);		
			return listItemBean;
	}



	/**
	 * @see org.esupportail.lecture.domain.DomainService#marckItemasRead(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void marckItemAsRead(String uid, String sourceId, String itemId) {
		/* Get current user profile and customCoategory */
		UserProfile userProfile = channel.getUserProfile(uid);
		// TODO (GB) why not customCategories ?
		CustomSource customSource;
		try {
			customSource = userProfile.getCustomSource(sourceId);
			customSource.setItemAsRead(itemId);
		} catch (CustomSourceNotFoundException e) {
			log.error("CustomSource is not found for service 'getItems(user "+uid+", source "+sourceId+ ")'");
			throw new ServiceException(e);
		}
	}
	
	/**
	 * @see org.esupportail.lecture.domain.DomainService#marckItemasUnread(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void marckItemAsUnread(String uid, String sourceId, String itemId) {
		/* Get current user profile and customCoategory */
		UserProfile userProfile = channel.getUserProfile(uid);
		// TODO (GB) why not customCategories ?
		CustomSource customSource;
		try {
			customSource = userProfile.getCustomSource(sourceId);
			customSource.setItemAsUnRead(itemId);
		} catch (CustomSourceNotFoundException e) {
			log.error("CustomSource is not found for service 'getItems(user "+uid+", source "+sourceId+ ")'");
			throw new ServiceException(e);
		}
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
