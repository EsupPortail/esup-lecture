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
import org.esupportail.commons.exceptions.ConfigException;
import org.esupportail.commons.services.application.Version;
import org.esupportail.commons.services.authentication.AuthenticationService;
import org.esupportail.commons.services.i18n.I18nService;
import org.esupportail.lecture.domain.beans.CategoryBean;
import org.esupportail.lecture.domain.beans.CategoryDummyBean;
import org.esupportail.lecture.domain.beans.ContextBean;
import org.esupportail.lecture.domain.beans.ItemBean;
import org.esupportail.lecture.domain.beans.ItemDummyBean;
import org.esupportail.lecture.domain.beans.SourceBean;
import org.esupportail.lecture.domain.beans.SourceDummyBean;
import org.esupportail.lecture.domain.beans.UserBean;
import org.esupportail.lecture.domain.model.Channel;
import org.esupportail.lecture.domain.model.CustomCategory;
import org.esupportail.lecture.domain.model.CustomContext;
import org.esupportail.lecture.domain.model.CustomManagedSource;
import org.esupportail.lecture.domain.model.CustomSource;
import org.esupportail.lecture.domain.model.Item;
import org.esupportail.lecture.domain.model.ItemDisplayMode;
import org.esupportail.lecture.domain.model.ProfileAvailability;
import org.esupportail.lecture.domain.model.UserProfile;
import org.esupportail.lecture.domain.model.VersionManager;
import org.esupportail.lecture.exceptions.domain.CategoryNotLoadedException;
import org.esupportail.lecture.exceptions.domain.CategoryNotVisibleException;
import org.esupportail.lecture.exceptions.domain.CategoryObligedException;
import org.esupportail.lecture.exceptions.domain.CategoryOutOfReachException;
import org.esupportail.lecture.exceptions.domain.CategoryProfileNotFoundException;
import org.esupportail.lecture.exceptions.domain.CategoryTimeOutException;
import org.esupportail.lecture.exceptions.domain.ComputeItemsException;
import org.esupportail.lecture.exceptions.domain.ContextNotFoundException;
import org.esupportail.lecture.exceptions.domain.CustomCategoryNotFoundException;
import org.esupportail.lecture.exceptions.domain.CustomSourceNotFoundException;
import org.esupportail.lecture.exceptions.domain.InfoDomainException;
import org.esupportail.lecture.exceptions.domain.InternalDomainException;
import org.esupportail.lecture.exceptions.domain.ManagedCategoryProfileNotFoundException;
import org.esupportail.lecture.exceptions.domain.MappingNotFoundException;
import org.esupportail.lecture.exceptions.domain.SourceNotLoadedException;
import org.esupportail.lecture.exceptions.domain.SourceNotVisibleException;
import org.esupportail.lecture.exceptions.domain.SourceObligedException;
import org.esupportail.lecture.exceptions.domain.SourceProfileNotFoundException;
import org.esupportail.lecture.exceptions.domain.SourceTimeOutException;
import org.esupportail.lecture.exceptions.domain.TreeSizeErrorException;
import org.esupportail.lecture.exceptions.domain.UserNotSubscribedToCategoryException;
import org.esupportail.lecture.exceptions.domain.Xml2HtmlException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.util.Assert;

/**
 * Service implementation provided by domain layer
 * All of services are available for a user only if 
 * he has a customContext defined in his userProfile.
 * To have a customContext defined in a userProfile, the service
 * getContext must have been called one time (over several user session)
 * This class throws ContextNotFoundException because getting context is not an
 * automatic research, it is leading by higher layer.
 * @author gbouteil
 */
public class DomainServiceImpl implements DomainService, InitializingBean {
	//TODO (GB <-- RB) CheckStyle ? 
	// Note RB : j'ai fait plein de petites modifs pour avoir beaucoup moins de warning
	
	/*
	 ************************** PROPERTIES ******************************** */	
	
	/** 
	 * Main domain model class.
	 */
	private static Channel channel; 
	
	/**
	 * Log instance.
	 */
	private static final Log LOG = LogFactory.getLog(DomainServiceImpl.class);

	/**
	 * The authentication Service.
	 */
	private AuthenticationService authenticationService;

	/**
	 * The i18n Service.
	 */
	private I18nService i18nService;

	/* 
	 ************************** INIT **********************************/

	/**
	 * default constructor.
	 */
	public DomainServiceImpl() {
		super();
	}


	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() {
		Assert.notNull(channel, "property channel of class "
				+ this.getClass().getName() + " can not be null");
		Assert.notNull(authenticationService, "property authenticationService of class " 
				+ this.getClass().getName() + " can not be null");
		Assert.notNull(i18nService, "property i18nService of class " 
				+ this.getClass().getName() + " can not be null");
	}

	
	/*
	 ************************** Methodes - services - mode NORMAL ************************************/

	/**
	 * Return the user identified by userId.
	 * @param userId user Id
	 * @return userBean
	 * @see org.esupportail.lecture.domain.DomainService#getConnectedUser(java.lang.String)
	 */
	public UserBean getConnectedUser(final String userId) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("getConnectedUser(" + userId + ")");
		}
		
		/* User profile creation */
		UserProfile userProfile = channel.getUserProfile(userId);
		
		/* userBean creation */
		UserBean user = new UserBean(userProfile);
		
		return user;
	}

	/**
	 * Returns the contextBean corresponding to the context identified by contextId for user userId.
	 * @param userId id of the current user
	 * @param contextId id of the context to get
	 * @return contextBean
	 * @throws ContextNotFoundException 
	 * @see org.esupportail.lecture.domain.DomainService#getContext(String,String)
	 */
	public ContextBean getContext(final String userId, final String contextId) 
			throws ContextNotFoundException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("getContext(" + userId + "," + contextId + ")");
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
	 * Returns a list of categoryBean - corresponding to available categories to display on interface.
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
	public List<CategoryBean> getAvailableCategories(final String userId, final String contextId, 
			final ExternalService ex) 
			throws ContextNotFoundException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("getAvailableCategories(" + userId + "," + contextId + ",externalService)");
		}
		
		/* Get current user profile and customContext */
		UserProfile userProfile = channel.getUserProfile(userId);
		CustomContext customContext = userProfile.getCustomContext(contextId);

		List<CategoryBean> listCategoryBean = new ArrayList<CategoryBean>();
		List<CustomCategory> customCategories = customContext.getSortedCustomCategories(ex);

		for (CustomCategory customCategory : customCategories) {
			CategoryBean category;
			try {
				category = new CategoryBean(customCategory, customContext);
				listCategoryBean.add(category);
			} catch (CategoryProfileNotFoundException e) {
				LOG.warn("Warning on service 'getAvailableCategories(user " 
					+ userId + ", context " + contextId + ") : clean custom source ");
				//userProfile.cleanCustomCategoryFromProfile(customCategory.getElementId());
				userProfile.removeCustomCategoryFromProfile(customCategory.getElementId());
			} catch (InfoDomainException e) {
				LOG.error("Error on service 'getAvailableCategories(user " 
					+ userId + ", context " + contextId + ") : creation of a CategoryDummyBean");
				category = new CategoryDummyBean(e);
				listCategoryBean.add(category);
			} 
		}
	
		return listCategoryBean;
	}
	
	/**
	 * Returns a list of sourceBean - corresponding to available categories to display on interface.
	 * into category categoryId for user userId
	 * Available sources are one that user : 
	 * - is subscribed to (obliged or allowed or autoSubscribe)
	 * - has created (personal sources)
	 * @param uid Id of the user
	 * @param categoryId id of the category to display sources
	 * @return a list of sourceBean
	 * @throws CategoryNotVisibleException 
	 * @throws InternalDomainException 
	 * @throws CategoryTimeOutException 
	 * @throws CategoryOutOfReachException 
	 * @see org.esupportail.lecture.domain.DomainService#getAvailableSources(java.lang.String, java.lang.String, org.esupportail.lecture.domain.ExternalService)
	 */
	public List<SourceBean> getAvailableSources(final String uid, final String categoryId, final ExternalService ex) 
			throws CategoryNotVisibleException, UserNotSubscribedToCategoryException, 
			InternalDomainException, CategoryTimeOutException, CategoryOutOfReachException  {
		if (LOG.isDebugEnabled()) {
			LOG.debug("getAvailableSources(" + uid + "," + categoryId + ",externalService)");
		}
		
		List<SourceBean> listSourceBean = new ArrayList<SourceBean>();
		UserProfile userProfile = channel.getUserProfile(uid);
		try {
			CustomCategory customCategory = userProfile.getCustomCategory(categoryId, ex);
			List<CustomSource> customSources = customCategory.getSortedCustomSources(ex);
				
			for (CustomSource customSource : customSources) {
				SourceBean source;
				try {
					source = new SourceBean(customSource);
					listSourceBean.add(source);
				} catch (SourceProfileNotFoundException e) {
					LOG.warn("Warning on service 'getAvailableSources(user " 
						+ uid + ", category " + categoryId + ") : clean custom source ");
					userProfile.removeCustomSourceFromProfile(customSource.getElementId());
				} catch (InfoDomainException e) {
					LOG.error("Error on service 'getAvailableSources(user "
						+ uid + ", category " + categoryId + ") : creation of a SourceDummyBean");
					source = new SourceDummyBean(e);
					listSourceBean.add(source);
				}
			}
		} catch	(CategoryProfileNotFoundException e) {
			String errorMsg = "CategoryProfileNotFoundException for service 'getAvailableSources(user "
				+ uid + ", category " + categoryId + ")";
			LOG.error(errorMsg);
			//userProfile.cleanCustomCategoryFromProfile(categoryId);
			userProfile.removeCustomCategoryFromProfile(categoryId);
			throw new InternalDomainException(errorMsg, e);
		} catch (CustomCategoryNotFoundException e) {
			String errorMsg = "CustomCategoryNotFound for service 'getAvailableSources(user "
				+ uid + ", category " + categoryId + ")\n" 
				+ "User " + uid + " is not subscriber of Category " + categoryId;
			LOG.error(errorMsg);
			throw new UserNotSubscribedToCategoryException(errorMsg, e);
		}
		
		return listSourceBean;
	}

	/** 
	 * Returns a list of itemBean.
	 * Corresponding to items containing in source sourceId,
	 * in order to be displayed on user interface for user uid
	 * @param uid user Id
	 * @param sourceId source Id to display items
	 * @param ex externalService
	 * @return a list of itemBean
	 * @throws SourceNotLoadedException 
	 * @throws InternalDomainException 
	 * @throws CategoryNotLoadedException 
	 * @throws SourceTimeOutException 
	 * @see org.esupportail.lecture.domain.DomainService#getItems(java.lang.String, java.lang.String, org.esupportail.lecture.domain.ExternalService)
	 */
	public List<ItemBean> getItems(final String uid, final String sourceId, final ExternalService ex) 
			throws SourceNotLoadedException, InternalDomainException, CategoryNotLoadedException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("getItems(" + uid + "," + sourceId + ",externalService)");
		}
		
		List<ItemBean> listItemBean = new ArrayList<ItemBean>();
		UserProfile userProfile = channel.getUserProfile(uid);
		CustomSource customSource = null;
		try {
			/* Get current user profile and customCoategory */
			customSource = userProfile.getCustomSource(sourceId);
			List<Item> listItems;
			listItems = customSource.getItems(ex);

			for (Item item : listItems) {
				ItemBean itemBean = new ItemBean(item, customSource);
				listItemBean.add(itemBean);
			}
		} catch (ManagedCategoryProfileNotFoundException e) {
			String errorMsg = "ManagedCategoryProfileNotFoundException for service 'getItems(user "
				+ uid + ", source " + sourceId + ")";
			LOG.error(errorMsg);
			CustomManagedSource customManagedSource = (CustomManagedSource) customSource;
			String categoryId = customManagedSource.getManagedSourceProfileParentId();
			//userProfile.cleanCustomCategoryFromProfile(categoryId);
			userProfile.removeCustomCategoryFromProfile(categoryId);
			throw new InternalDomainException(errorMsg, e);
		} catch	(SourceProfileNotFoundException e) {
			String errorMsg = "SourceProfileNotFoundException for service 'getItems(user "
				+ uid + ", source " + sourceId + ")";
			LOG.error(errorMsg);
			userProfile.removeCustomSourceFromProfile(sourceId);
			throw new InternalDomainException(errorMsg, e);
		} catch (CustomSourceNotFoundException e) {
			String errorMsg = "CustomSourceNotFoundException for service 'getItems(user "
				+ uid + ", source " + sourceId + ")";
			LOG.error(errorMsg);
			throw new InternalDomainException(errorMsg, e);
		} catch (MappingNotFoundException e) {
			String errorMsg = "MappingNotFoundException for service 'getItems(user "
				+ uid + ", source " + sourceId + ")";
			LOG.error(errorMsg);
			throw new InternalDomainException(errorMsg, e);
		} catch (ComputeItemsException e) {
			String errorMsg = "ComputeItemsException for service 'getItems(user "
				+ uid + ", source " + sourceId + ")";
			LOG.error(errorMsg);
			throw new InternalDomainException(errorMsg, e);
		} catch (Xml2HtmlException e) {
			String errorMsg = "Xml2HtmlException for service 'getItems(user "
				+ uid + ", source " + sourceId + ")";
			LOG.error(errorMsg);
			throw new InternalDomainException(errorMsg, e);
		} catch (InfoDomainException e) {
			String errorMsg = "InfoDomainException for service 'getItems(user "
				+ uid + ", source " + sourceId + ")";
			LOG.error(errorMsg);
			//get error message form i18n service
			String mes = i18nService.getString("itemDummy");
			//build new exception with this first translated message in stack trace
			InfoDomainException e2 = new InfoDomainException(mes, e); 
			ItemBean itemBean = new ItemDummyBean(e2);
			listItemBean.add(itemBean);
		} 		
		return listItemBean;
	}

	/**
	 * Mark item as read for user uid.
	 * @param uid user Id
	 * @param sourceId sourceId of the item
	 * @param itemId item Id
	 * @param isRead the read Mode (true=item read | false=item not read)
	 * @throws InternalDomainException 
	 * @see org.esupportail.lecture.domain.DomainService#marckItemReadMode(java.lang.String, java.lang.String, java.lang.String, boolean)
	 */
	public void marckItemReadMode(final String uid, final String sourceId, 
			final String itemId, final boolean isRead) 
			throws InternalDomainException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("marckItemReadMode(" + uid + "," + sourceId + "," + itemId + "," + isRead + ")");
		}
		
		try {
			/* Get current user profile and customCoategory */
			UserProfile userProfile = channel.getUserProfile(uid);
			CustomSource customSource;
			customSource = userProfile.getCustomSource(sourceId);
			customSource.setItemReadMode(itemId, isRead);
		} catch (CustomSourceNotFoundException e) {
			String errorMsg = "CustomSourceNotFoundException for service 'marckItemReadMode(user "
				+ uid + ", source " + sourceId + ", item " + itemId + ", isRead " + isRead + ")";
			LOG.error(errorMsg);
			throw new InternalDomainException(errorMsg, e);
		}
		
	}
	
	
	/**
	 * Mark item display mode on source for a user.
	 * @param uid user ID
	 * @param sourceId source ID
	 * @param mode item display mode to set
	 * @throws InternalDomainException 
	 * @see DomainService#markItemDisplayMode(String, String, ItemDisplayMode)
	 */
	public void markItemDisplayMode(final String uid, final String sourceId, 
			final ItemDisplayMode mode) throws InternalDomainException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("markItemDisplayMode(" + uid + "," + sourceId + "," + mode + ")");
		}
		
		try {
			/* Get current user profile and customCategory */
			UserProfile userProfile = channel.getUserProfile(uid);
			CustomSource customSource;
			customSource = userProfile.getCustomSource(sourceId);
			customSource.modifyItemDisplayMode(mode);
		} catch (CustomSourceNotFoundException e) {
			String errorMsg = "CustomSourceNotFoundException for service 'markItemDisplayMode(user "
				+ uid + ", source " + sourceId + ", mode " + mode + ")";
			LOG.error(errorMsg);
			throw new InternalDomainException(errorMsg, e);
		}
		
	}
	
	
	/**
	 * Set the tree size of the customContext.
	 * @param uid user Id for user uid
	 * @param contextId context Id
	 * @param size size to set
	 * @throws ContextNotFoundException 
	 * @throws TreeSizeErrorException
	 * @see org.esupportail.lecture.domain.DomainService#setTreeSize(java.lang.String, java.lang.String, int)
	 */
	public void setTreeSize(final String uid, final String contextId, final int size)
		throws TreeSizeErrorException, ContextNotFoundException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("setTreeSize(" + uid + "," + contextId + "," + size + ")");
		}
		/* Get current user profile and customContext */
		UserProfile userProfile = channel.getUserProfile(uid);
		CustomContext customContext = userProfile.getCustomContext(contextId);
		customContext.modifyTreeSize(size);	
	}

	/**
	 * Set category identified by catId as fold in the customContext ctxId.
	 * for user uid
	 * @param uid user Id
	 * @param cxtId context Id 
	 * @param catId category Id
	 * @throws ContextNotFoundException
	 * @see org.esupportail.lecture.domain.DomainService#foldCategory(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void foldCategory(final String uid, final String cxtId, 
			final String catId) throws ContextNotFoundException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("foldCategory(" + uid + "," + cxtId + "," + catId + ")");
		}
		
		/* Get current user profile and customContext */
		UserProfile userProfile = channel.getUserProfile(uid);
		CustomContext customContext = userProfile.getCustomContext(cxtId);
		customContext.foldCategory(catId);
	}
	
	/**
	 * Set category identified by catId as unfold in the customContext ctxId.
	 * for user uid
	 * @param uid user Id
	 * @param cxtId context Id 
	 * @param catId category Id
	 * @throws ContextNotFoundException
	 * @see org.esupportail.lecture.domain.DomainService#unfoldCategory(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void unfoldCategory(final String uid, final String cxtId,
			final String catId) throws ContextNotFoundException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("unfoldCategory(" + uid + "," + cxtId + "," + catId + ")");
		}
		
		/* Get current user profile and customContext */
		UserProfile userProfile = channel.getUserProfile(uid);
		CustomContext customContext = userProfile.getCustomContext(cxtId);
		customContext.unfoldCategory(catId);
	}
	
	/*
	 ************************** Methodes - services - mode EDIT ************************************/
	
	/**
	 * @throws ContextNotFoundException 
	 * @see org.esupportail.lecture.domain.DomainService#getVisibleCategories(java.lang.String, java.lang.String, org.esupportail.lecture.domain.ExternalService)
	 */
	public List<CategoryBean> getVisibleCategories(final String uid, final String contextId, final ExternalService ex) 
		throws ContextNotFoundException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("getVisibleCategories(" + uid + "," + contextId + ",ex)");
		}
		List<CategoryBean> listCategoryBean = new ArrayList<CategoryBean>();
		UserProfile userProfile = channel.getUserProfile(uid);
		CustomContext customContext = userProfile.getCustomContext(contextId);
		List<ProfileAvailability> couples = customContext.getVisibleCategories(ex);
		for (ProfileAvailability couple : couples) {
			CategoryBean category;
			category = new CategoryBean(couple);
			listCategoryBean.add(category);
		}	
		return listCategoryBean;

	}



	/**
	 * @see org.esupportail.lecture.domain.DomainService#getVisibleSources(java.lang.String, java.lang.String, org.esupportail.lecture.domain.ExternalService)
	 */
	public List<SourceBean> getVisibleSources(final String uid, final String categoryId, final ExternalService ex) 
			throws CategoryNotVisibleException, CategoryOutOfReachException, 
			UserNotSubscribedToCategoryException, InternalDomainException, CategoryTimeOutException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("getVisibleSources(" + uid + "," + categoryId + ",ex)");
		}
		List<SourceBean> listSourceBean = new ArrayList<SourceBean>();
		UserProfile userProfile = channel.getUserProfile(uid);
		try {
			
			CustomCategory customCategory = userProfile.getCustomCategory(categoryId, ex);
			List<ProfileAvailability> couples = customCategory.getVisibleSources(ex);
			for (ProfileAvailability couple : couples) {
				SourceBean source;
				//try {
					source = new SourceBean(couple);
					listSourceBean.add(source);
					////////////////
//				}catch (InfoDomainException e) {
//					log.error("Error on service 'getVisibleSources(user "
//						+uid+", category "+categoryId+") : creation of a SourceDummyBean");
//					source = new SourceDummyBean(e);
//					listSourceBean.add(source);
//				}
			}			
		} catch	(CategoryProfileNotFoundException e) {
			String errorMsg = "CategoryProfileNotFoundException for service 'getVisibleSources(user "
				+ uid + ", category " + categoryId + ")";
			LOG.error(errorMsg);
			//userProfile.cleanCustomCategoryFromProfile(categoryId);
			userProfile.removeCustomCategoryFromProfile(categoryId);
			throw new InternalDomainException(errorMsg, e);
		} catch (CustomCategoryNotFoundException e) {
			String errorMsg = "CustomCategoryNotFound for service 'getVisibleSources(user " 
				+ uid + ", category " + categoryId + ")" 
				+ "User " + uid + " is not subscriber of Category " + categoryId;
			LOG.error(errorMsg);
			throw new UserNotSubscribedToCategoryException(errorMsg, e);
		}
		 return listSourceBean;
		
	}
	
	/**
	 * subscribe user uid to category categoryId in context contextId.
	 * @param uid user ID
	 * @param contextId context ID
	 * @param categoryId category ID
	 * @param externalService access to externalService
	 * @throws ManagedCategoryProfileNotFoundException 
	 * @throws ContextNotFoundException 
	 * @throws InternalDomainException 
	 * @throws CategoryOutOfReachException 
	 * @throws CategoryNotVisibleException 
	 * @throws CategoryTimeOutException 
	 * 
	 */
	public void subscribeToCategory(final String uid, final String contextId, final String categoryId, ExternalService externalService) 
		throws ManagedCategoryProfileNotFoundException, ContextNotFoundException, CategoryTimeOutException, CategoryNotVisibleException, CategoryOutOfReachException, InternalDomainException  {
		if (LOG.isDebugEnabled()) {
			LOG.debug("subscribeToCategory(" + uid + "," + contextId 
				+ "," + categoryId + ")");
		}
		UserProfile userProfile = channel.getUserProfile(uid);
		
		CustomContext customContext;
		customContext = userProfile.getCustomContext(contextId);
		customContext.subscribeToCategory(categoryId, externalService);
	}

	

	/**
	 * subscribe user uid to source sourceId in categoryId, if user is already subscriber of categoryId.
	 * @param uid user ID
	 * @param categoryId category ID
	 * @param sourceId source ID
	 * @param ex access to externalService
	 * @throws UserNotSubscribedToCategoryException 
	 * @throws CategoryNotVisibleException 
	 * @throws SourceNotVisibleException 
	 * @throws SourceProfileNotFoundException 
	 * @throws CategoryOutOfReachException
	 * @throws InternalDomainException 
	 * @throws CategoryTimeOutException 
	 */
	public void subscribeToSource(final String uid, final String categoryId, 
			final String sourceId, final ExternalService ex) 
			throws UserNotSubscribedToCategoryException, CategoryNotVisibleException,
			CategoryOutOfReachException, SourceProfileNotFoundException, SourceNotVisibleException,
			InternalDomainException, CategoryTimeOutException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("subscribeToSource(" + uid + "," + categoryId 
				+ "," + sourceId + ", externalService)");
		}
		UserProfile userProfile = channel.getUserProfile(uid);
		try {
			CustomCategory customCategory = userProfile.getCustomCategory(categoryId, ex);
			customCategory.subscribeToSource(sourceId, ex);
		} catch	(CategoryProfileNotFoundException e) {
			String errorMsg = "CategoryProfileNotFoundException for service 'subscribeToSource(user "
				+ uid + ", category " + categoryId + ", source " + sourceId + ", externalService)";
			LOG.error(errorMsg);
			//userProfile.cleanCustomCategoryFromProfile(categoryId);
			userProfile.removeCustomCategoryFromProfile(categoryId);
			throw new InternalDomainException(errorMsg, e);
		} catch (CustomCategoryNotFoundException e) {
			String errorMsg = "CustomCategoryNotFound for service 'subscribeToSource(user "
				+ uid + ", category " + categoryId + ", source " + sourceId + ", externalService).\n" 
				+ "User " + uid + " is not subscriber of Category " + categoryId;
			LOG.error(errorMsg);
			throw new UserNotSubscribedToCategoryException(errorMsg, e);
		} 
	}
	
	/**
	 * unsubscribe user uid to category categoryId in context contextId.
	 * @param uid user ID
	 * @param contextId context ID
	 * @param categoryId category ID
	 * @param externalService access to externalService
	 * @throws ManagedCategoryProfileNotFoundException 
	 * @throws ContextNotFoundException 
	 * @throws InternalDomainException 
	 * @throws CategoryOutOfReachException 
	 * @throws CategoryNotVisibleException 
	 * @throws CategoryTimeOutException 
	 * @throws CategoryObligedException 
	 * 
	 */
	public void unsubscribeToCategory(String uid, String contextId, String categoryId, ExternalService externalService) 
		throws ManagedCategoryProfileNotFoundException, ContextNotFoundException, CategoryTimeOutException, 
		CategoryNotVisibleException, CategoryOutOfReachException, InternalDomainException, CategoryObligedException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("unsubscribeToCategory(" + uid + "," + contextId + "," + categoryId + ")");
		}
		UserProfile userProfile = channel.getUserProfile(uid);			
		CustomContext customContext;
		customContext = userProfile.getCustomContext(contextId);
		customContext.unsubscribeToCategory(categoryId, externalService);
	}

	/**
	 * unsubscribe user uid to source sourceId in categoryId, if user is already subscriber of categoryId.
	 * @param uid user ID
	 * @param categoryId category ID
	 * @param sourceId source ID
	 * @param ex access to externalService
	 * @throws CategoryNotVisibleException 
	 * @throws UserNotSubscribedToCategoryException 
	 * @throws InternalDomainException 
	 * @throws SourceObligedException 
	 * @throws CategoryOutOfReachException 
	 * @throws CategoryTimeOutException 
	 */
	public void unsubscribeToSource(final String uid, final String categoryId, 
			final String sourceId, final ExternalService ex) 
			throws CategoryNotVisibleException, UserNotSubscribedToCategoryException, InternalDomainException, 
			CategoryOutOfReachException, SourceObligedException, CategoryTimeOutException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("subscribeToSource(" + uid + "," + categoryId + "," 
				+ sourceId + ", externalService)");
		}
		UserProfile userProfile = channel.getUserProfile(uid);
		try {
			CustomCategory customCategory = userProfile.getCustomCategory(categoryId, ex);
			customCategory.unsubscribeToSource(sourceId, ex);
		} catch	(CategoryProfileNotFoundException e) {
			String errorMsg = "CategoryProfileNotFoundException for service 'unsubscribeToSource(user "
				+ uid + ", category " + categoryId + ", source " + sourceId + ", externalService)";
			LOG.error(errorMsg);
			//userProfile.cleanCustomCategoryFromProfile(categoryId);
			userProfile.removeCustomCategoryFromProfile(categoryId);
			throw new InternalDomainException(errorMsg, e);
		} catch (CustomCategoryNotFoundException e) {
			String errorMsg = "CustomCategoryNotFound for service 'unsubscribeToSource(user "
				+ uid + ", category " + categoryId + ", source " + sourceId + ", externalService).\n" 
				+ "User " + uid + " is not subscriber of Category " + categoryId;
			LOG.error(errorMsg);
			throw new UserNotSubscribedToCategoryException(errorMsg, e);
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
	public void setChannel(final Channel channel) {
		// It could be static without spring 
		DomainServiceImpl.channel = channel;
	}


	/**
	 * @see org.esupportail.lecture.domain.DomainService#getDatabaseVersion()
	 */
	public Version getDatabaseVersion() throws ConfigException {
		VersionManager versionManager = getVersionManager();
		if (versionManager == null) {
			return null;
		}
		return new Version(versionManager.getVersion());
	}

	/**
	 * @return the first (and only) VersionManager instance of the database.
	 * @throws ConfigException 
	 */
	private VersionManager getVersionManager() throws ConfigException {
		List<VersionManager> versionManagers = null;
		try {
			versionManagers = DomainTools.getDaoService().getVersionManagers();
		} catch (BadSqlGrammarException e) {
			throw new ConfigException("your database is not initialized, please run 'ant init'", e);
		}
		if (versionManagers.isEmpty()) {
			return null;
		}
		return versionManagers.get(0);
	}


	/**
	 * @see org.esupportail.lecture.domain.DomainService#setDatabaseVersion(java.lang.String)
	 */
	public void setDatabaseVersion(final String version) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("setting database version to '" + version + "'...");
		}
		VersionManager versionManager = getVersionManager();
		if (versionManager == null) {
			versionManager = new VersionManager();
			versionManager.setVersion(version);
			DomainTools.getDaoService().addVersionManager(versionManager);
		} else {
			versionManager.setVersion(version);
			DomainTools.getDaoService().updateVersionManager(versionManager);
		}
		if (LOG.isDebugEnabled()) {
			LOG.debug("database version set to '" + version + "'.");
		}
	}


	/**
	 * @see org.esupportail.lecture.domain.DomainService#isGuestMode()
	 */
	public boolean isGuestMode() {
		boolean ret;
		String connectedUser = authenticationService.getCurrentUserId();
		if (connectedUser.equals(DomainTools.getGuestUser())) {
			ret = true;
		} else {
			ret = false;
		}
		return ret;
	}

	/*
	 *************************** ACCESSORS ********************************* */	
	
	/**
	 * @param authenticationService the authenticationService to set
	 */
	public void setAuthenticationService(final AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}


	/**
	 * @param service the i18nService to set
	 */
	public void setI18nService(I18nService service) {
		i18nService = service;
	}


	



}
