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

/**
 * Implémentation des services offerts par la couche métier
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
		ContextBean contextBean = new ContextBean(customContext);
		
		// TODO mise à jour du DAO ?
//		DomainTools.getDaoService().updateCustomContext(customContext);
//		DomainTools.getDaoService().updateUserProfile(userProfile);
		
		return contextBean;		
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#getCategories(java.lang.String, java.lang.String)
	 */
	// TODO getVisibleCategories ?
	public List<CategoryBean> getCategories(String userId, String contextId,ExternalService externalService) {
		
		/* Get current user profile and customContext */
		UserProfile userProfile = channel.getUserProfile(userId);
		CustomContext customContext = userProfile.getCustomContext(contextId);
		
		List<CategoryBean> listCategoryBean = new ArrayList<CategoryBean>();
		
		List<CustomCategory> customCategories = customContext.getSortedCustomCategories(externalService);
		for(CustomCategory customCategory : customCategories){
			CategoryBean category = new CategoryBean(customCategory);
			listCategoryBean.add(category);
			// TODO mise à jour du DAO ?
//			DomainTools.getDaoService().updateCustomCategory(customCategory);
		}
		
		// TODO mise à jour du DAO ?
//		DomainTools.getDaoService().updateCustomContext(customContext);
//		DomainTools.getDaoService().updateUserProfile(userProfile);
		return listCategoryBean;
	}
	
	/**
	 * @see org.esupportail.lecture.domain.DomainService#getSources(java.lang.String, java.lang.String, org.esupportail.lecture.domain.ExternalService)
	 */
	// TODO getVisibleSource
	public List<SourceBean> getSources(String uid, String categoryId,ExternalService externalService) {
		
		/* Get current user profile and customCoategory */
		UserProfile userProfile = channel.getUserProfile(uid);
		// TODO why not customCategories ?
		CustomCategory customCategory = userProfile.getCustomManagedCategory(categoryId);
		
		List<SourceBean> listSourceBean = new ArrayList<SourceBean>();
		
		List<CustomSource> customSources = customCategory.getSortedCustomSources(externalService);
		int nbSources = customSources.size();
		log.info("NB sources : "+nbSources);
		for(CustomSource customSource : customSources){
			SourceBean source = new SourceBean(customSource);
			listSourceBean.add(source);
			// TODO mise à jour du DAO ?
//			DomainTools.getDaoService().updateCustomSource(customSource);
			
		}
		// TODO mise à jour du DAO ?
//		DomainTools.getDaoService().updateUserProfile(userProfile);
//		DomainTools.getDaoService().updateCustomCategory(customCategory);		
		return listSourceBean;
	}
	
	

	/* see later */
	
	public List<ItemBean> getItems(String uid, String sourceId,ExternalService externalService) {
		/* Get current user profile and customCoategory */
		UserProfile userProfile = channel.getUserProfile(uid);
		// TODO why not customCategories ?
		CustomSource customSource = userProfile.getCustomSource(sourceId);
		
		if(customSource!=null){
			List<ItemBean> listItemBean = new ArrayList<ItemBean>();
		
			List<Item> items = customSource.getItems(externalService);
			for(Item item : items){
				ItemBean itemBean = new ItemBean(item);
				listItemBean.add(itemBean);
			// 	TODO mise à jour du DAO ?
//				DomainTools.getDaoService().updateCustomSource(customSource);
			
			}
		// 	TODO mise à jour du DAO ?
//			DomainTools.getDaoService().updateUserProfile(userProfile);
//			DomainTools.getDaoService().updateCustomCategory(customCategory);		
			return listItemBean;
		}
		log.error("CustomSource of source "+sourceId+" is null");
		return null;
		
	}



	public void marckItemasRead(String uid, String sourceId, String itemId) {
		// TODO Auto-generated method stub
		
	}
	
	public void marckItemasUnread(String uid, String sourceId, String itemId) {
		// TODO Auto-generated method stub
		
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
