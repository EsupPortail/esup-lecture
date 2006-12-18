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
import org.esupportail.lecture.exceptions.CategoryNotVisibleException;
import org.esupportail.lecture.exceptions.ElementNotLoadedException;
import org.esupportail.lecture.exceptions.ContextNotFoundException;
import org.esupportail.lecture.exceptions.CustomCategoryNotFoundException;
import org.esupportail.lecture.exceptions.CustomSourceNotFoundException;
import org.esupportail.lecture.exceptions.InfoFacadeServiceException;
import org.esupportail.lecture.exceptions.ManagedCategoryProfileNotFoundException;
import org.esupportail.lecture.exceptions.DomainServiceException;
import org.esupportail.lecture.exceptions.SourceNotLoadedException;
import org.esupportail.lecture.exceptions.TreeSizeErrorException;
import org.springframework.util.Assert;

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
	 ************************** INIT **********************************/

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(channel,"property channel can not be null");
	}

	
	/*
	 ************************** Methodes - services  ************************************/

	/**
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
	 * @see org.esupportail.lecture.domain.DomainService#getContext(java.lang.String, java.lang.String, org.esupportail.lecture.domain.ExternalService)
	 */
	public ContextBean getContext(String userId,String contextId) {
		if (log.isDebugEnabled()){
			log.debug("getContext("+userId+","+contextId+")");
		}
		
		/* Get current user profile and customContext */
		UserProfile userProfile = channel.getUserProfile(userId);
		CustomContext customContext = userProfile.getCustomContext(contextId);
		
		/* Make the contextUserBean to display */
		ContextBean contextBean;
		try {
			contextBean = new ContextBean(customContext);
		} catch (ContextNotFoundException e) {
			log.error("Context not found for service 'getContext(user "+userId+", context "+contextId);
			throw new DomainServiceException(e);
		}
			
		return contextBean;		
	}

	/**
	 * @throws DomainServiceException 
	 * @see org.esupportail.lecture.domain.DomainService#getVisibleCategories(java.lang.String, java.lang.String, ExternalService)
	 */
	public List<CategoryBean> getVisibleCategories(String userId, String contextId,ExternalService externalService) throws DomainServiceException {
		if (log.isDebugEnabled()){
			log.debug("getVisibleCategories("+userId+","+contextId+",externalService)");
		}
		
		/* Get current user profile and customContext */
		UserProfile userProfile = channel.getUserProfile(userId);
		CustomContext customContext = userProfile.getCustomContext(contextId);

		List<CategoryBean> listCategoryBean = new ArrayList<CategoryBean>();
		try {
			List<CustomCategory> customCategories = customContext.getSortedCustomCategories(externalService);

			for(CustomCategory customCategory : customCategories){
				
				CategoryBean category = new CategoryBean(customCategory,customContext);
				listCategoryBean.add(category);
			}
		} catch (ContextNotFoundException e){
			log.error("Context not found for service 'getVisibleCategories(user "+userId+", context "+contextId);
			throw new DomainServiceException(e);
		} catch (ElementNotLoadedException e) {
			log.error("Context not found for service 'getVisibleCategories(user "+userId+", context "+contextId);
			throw new DomainServiceException(e);
		} catch (CategoryProfileNotFoundException e) {
			log.error("ManagedCategoryProfile not found for service 'getVisibleCategories(user "+userId+", context "+contextId);
			throw new DomainServiceException(e);
		} 
		return listCategoryBean;
	}
	
	/**
	 * @throws DomainServiceException 
	 * @see org.esupportail.lecture.domain.DomainService#getVisibleSources(java.lang.String, java.lang.String, org.esupportail.lecture.domain.ExternalService)
	 */
	public List<SourceBean> getVisibleSources(String uid, String categoryId,ExternalService externalService) throws DomainServiceException {
		if (log.isDebugEnabled()){
			log.debug("getVisibleSources("+uid+","+categoryId+",externalService)");
		}
		
		// TODO (GB) getVisibleCategories doit avoir �t� appel� avant pour charger la category : est ce bien ?
		/* Get current user profile and customCoategory */
		UserProfile userProfile = channel.getUserProfile(uid);
		CustomCategory customCategory = null;
		
		try {
			customCategory = userProfile.getCustomCategory(categoryId);
		}
		catch (CustomCategoryNotFoundException e){
			log.info("CustomCategory not directly found  for service 'getVisibleSources(user "+uid+", category "+categoryId+ ")'");
			try {
				// TODO : renseigner la var customCategory => non : faire l'appel � la m�thode ci-dessous dans
				// userProfile.getCustomCategory(categoryId);
				userProfile.updateCustomContextsForOneManagedCategory(categoryId,externalService);
				return getSortedCustomSourcesForCustomCategory(customCategory, externalService);	

			} catch (CategoryProfileNotFoundException e1) {
				log.error("ManagedCategoryProfile not found for service 'getVisibleSources(user "+uid+", category "+categoryId+ ")'");
				throw new DomainServiceException(e);
			} catch (ElementNotLoadedException e1) {
				log.error("Element is not loaded for service 'getVisibleSources(user "+uid+", category "+categoryId+ ")'");
				throw new DomainServiceException(e);
			} catch (CategoryNotVisibleException e1) {
				log.warn("Category is not visible anymore for service 'getVisibleSources(user "+uid+", category "+categoryId+ ")'");
				throw new InfoFacadeServiceException(e);
			}
		} 
		try {
			return getSortedCustomSourcesForCustomCategory(customCategory, externalService);	

		} catch (CategoryNotVisibleException e) {
			log.warn("Category is not visible anymore for service 'getVisibleSources(user "+uid+", category "+categoryId+ ")'");
			throw new InfoFacadeServiceException(e);
		} catch (CategoryProfileNotFoundException e){
			log.error("ManagedCategoryProfile not found for service 'getVisibleSources(user "+uid+", category "+categoryId+ ")'");
			throw new DomainServiceException(e);
		} catch (CategoryNotLoadedException e) {	
			log.error("Category is not loaded for service 'getVisibleSources(user "+uid+", category "+categoryId+ ")'");
			throw new DomainServiceException(e);
		} catch (ElementNotLoadedException e) {
			log.error("Element is not loaded for service 'getVisibleSources(user "+uid+", category "+categoryId+ ")'");
			throw new DomainServiceException(e);
		}
	}


	/**
	 * @param customCategory
	 * @param externalService
	 * @throws CategoryProfileNotFoundException
	 * @throws ElementNotLoadedException
	 * @throws CategoryNotVisibleException
	 */
	private List<SourceBean> getSortedCustomSourcesForCustomCategory(CustomCategory customCategory, ExternalService externalService) throws CategoryProfileNotFoundException, ElementNotLoadedException, CategoryNotVisibleException {
		List<CustomSource> customSources = customCategory.getSortedCustomSources(externalService);
		int nbSources = customSources.size();
		log.trace("NB sources : "+nbSources);
		List<SourceBean> listSourceBean = new ArrayList<SourceBean>();
		for(CustomSource customSource : customSources){
			SourceBean source = new SourceBean(customSource);
			listSourceBean.add(source);
		}
		return listSourceBean;
	}
	
	

	/* see later */
	
	/**
	 * @see org.esupportail.lecture.domain.DomainService#getItems(java.lang.String, java.lang.String, org.esupportail.lecture.domain.ExternalService)
	 */
	public List<ItemBean> getItems(String uid, String sourceId,ExternalService externalService) {
		if (log.isDebugEnabled()){
			log.debug("getItems("+uid+","+sourceId+",externalService)");
		}
		
		/* Get current user profile and customCoategory */
		UserProfile userProfile = channel.getUserProfile(uid);
		CustomSource customSource;
		
		try {
			customSource = userProfile.getCustomSource(sourceId);
		} catch (CustomSourceNotFoundException e) {
			log.error("CustomSource is not found for service 'getItems(user "+uid+", source "+sourceId+ ")'");
			throw new DomainServiceException(e);
		}
	
		List<ItemBean> listItemBean = new ArrayList<ItemBean>();
		
		List<Item> listItems;
		try {
			listItems = customSource.getItems(externalService);
		} catch (SourceNotLoadedException e) {
			log.error("Source is not loaded for service 'getItems(user "+uid+", source "+sourceId+ ")'");
			throw new DomainServiceException(e);
		} catch (ElementNotLoadedException e) {
			log.error("Composant is not loaded for service 'getItems(user "+uid+", source "+sourceId+ ")'");
			throw new DomainServiceException(e);
		}
		for(Item item : listItems){
			ItemBean itemBean = new ItemBean(item,customSource);
			listItemBean.add(itemBean);
		}
		return listItemBean;
	}



	/**
	 * @see org.esupportail.lecture.domain.DomainService#marckItemasRead(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void marckItemAsRead(String uid, String sourceId, String itemId) {
		if (log.isDebugEnabled()){
			log.debug("marckItemAsRead("+uid+","+sourceId+","+itemId+")");
		}
		
		/* Get current user profile and customCoategory */
		UserProfile userProfile = channel.getUserProfile(uid);
		CustomSource customSource;
		try {
			customSource = userProfile.getCustomSource(sourceId);
			customSource.setItemAsRead(itemId);
		} catch (CustomSourceNotFoundException e) {
			log.error("CustomSource is not found for service 'marckItemAsRead(user "+uid+", source "+sourceId+", item"+ itemId+")");
			throw new DomainServiceException(e);
		}
	}
	
	/**
	 * @see org.esupportail.lecture.domain.DomainService#marckItemasUnread(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void marckItemAsUnread(String uid, String sourceId, String itemId) {
		if (log.isDebugEnabled()){
			log.debug("marckItemAsUnread("+uid+","+sourceId+","+itemId+")");
		}
		
		/* Get current user profile and customCategory */
		UserProfile userProfile = channel.getUserProfile(uid);
		CustomSource customSource;
		try {
			customSource = userProfile.getCustomSource(sourceId);
			customSource.setItemAsUnRead(itemId);
		} catch (CustomSourceNotFoundException e) {
			log.error("CustomSource is not found for service 'marckItemAsUnread(user "+uid+", source "+sourceId+", item"+ itemId+")");
			throw new DomainServiceException(e);
		}
	}


	/**
	 * @see org.esupportail.lecture.domain.DomainService#setTreeSize(java.lang.String, java.lang.String, int)
	 */
	public void setTreeSize(String uid, String contextId, int size) throws TreeSizeErrorException {
		if (log.isDebugEnabled()){
			log.debug("setTreeSize("+uid+","+contextId+","+size+")");
		}
		
		/* Get current user profile and customContext */
		UserProfile userProfile = channel.getUserProfile(uid);
		CustomContext customContext = userProfile.getCustomContext(contextId);
		customContext.setTreeSize(size);
		
	}


	public void foldCategory(String uid, String cxtId, String catId) {
		if (log.isDebugEnabled()){
			log.debug("foldCategory("+uid+","+cxtId+","+catId+")");
		}
		
		/* Get current user profile and customContext */
		UserProfile userProfile = channel.getUserProfile(uid);
		CustomContext customContext = userProfile.getCustomContext(cxtId);
		customContext.foldCategory(catId);
	}
	
	public void unfoldCategory(String uid, String cxtId, String catId) {
		if (log.isDebugEnabled()){
			log.debug("unfoldCategory("+uid+","+cxtId+","+catId+")");
		}
		
		/* Get current user profile and customContext */
		UserProfile userProfile = channel.getUserProfile(uid);
		CustomContext customContext = userProfile.getCustomContext(cxtId);
		customContext.unfoldCategory(catId);
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

	public List<SourceBean> getAvailableSources(String uid, String categoryId, ExternalService externalService) {
		// TODO Auto-generated method stub
		return null;
	}


	
}
