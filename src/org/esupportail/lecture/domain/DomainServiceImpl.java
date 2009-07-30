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
import org.esupportail.lecture.domain.beans.SourceBean;
import org.esupportail.lecture.domain.beans.SourceDummyBean;
import org.esupportail.lecture.domain.beans.UserBean;
import org.esupportail.lecture.domain.model.CoupleProfileAvailability;
import org.esupportail.lecture.domain.model.CustomCategory;
import org.esupportail.lecture.domain.model.CustomContext;
import org.esupportail.lecture.domain.model.CustomSource;
import org.esupportail.lecture.domain.model.Item;
import org.esupportail.lecture.domain.model.ItemDisplayMode;
import org.esupportail.lecture.domain.model.ManagedCategoryDummy;
import org.esupportail.lecture.domain.model.UserProfile;
import org.esupportail.lecture.domain.model.VersionManager;
import org.esupportail.lecture.exceptions.domain.CategoryNotVisibleException;
import org.esupportail.lecture.exceptions.domain.CategoryObligedException;
import org.esupportail.lecture.exceptions.domain.CategoryProfileNotFoundException;
import org.esupportail.lecture.exceptions.domain.CategoryTimeOutException;
import org.esupportail.lecture.exceptions.domain.ComputeItemsException;
import org.esupportail.lecture.exceptions.domain.ContextNotFoundException;
import org.esupportail.lecture.exceptions.domain.CustomCategoryNotFoundException;
import org.esupportail.lecture.exceptions.domain.CustomSourceNotFoundException;
import org.esupportail.lecture.exceptions.domain.DomainServiceException;
import org.esupportail.lecture.exceptions.domain.InfoDomainException;
import org.esupportail.lecture.exceptions.domain.InternalDomainException;
import org.esupportail.lecture.exceptions.domain.ManagedCategoryNotLoadedException;
import org.esupportail.lecture.exceptions.domain.SourceNotLoadedException;
import org.esupportail.lecture.exceptions.domain.SourceNotVisibleException;
import org.esupportail.lecture.exceptions.domain.SourceObligedException;
import org.esupportail.lecture.exceptions.domain.SourceProfileNotFoundException;
import org.esupportail.lecture.exceptions.domain.TreeSizeErrorException;
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
	/*
	 ************************** PROPERTIES ******************************** */	
	
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
		Assert.notNull(authenticationService, "property authenticationService of class " 
				+ this.getClass().getName() + " can not be null");
		Assert.notNull(i18nService, "property i18nService of class " 
				+ this.getClass().getName() + " can not be null");
	}

	
	/*
	 ************************** Methodes - services - mode NORMAL ************************************/

	/**
	 * return the user profile identified by "userId". 
	 * It takes it from the dao if exists, else, it create a user profile
	 * @param userId : identifient of the user profile
	 * @return the user profile
	 */ 
	public synchronized UserProfile getUserProfile(final String userId) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("getFreshUserProfile(" + userId + ")");
		}
		UserProfile userProfile = DomainTools.getDaoService().getUserProfile(userId);
		if (userProfile == null) {
			userProfile = new UserProfile(userId);
			//don't call DAO to attach the object because we want a detached object
//			DomainTools.getDaoService().saveUserProfile(userProfile);
//			userProfile = DomainTools.getDaoService().refreshUserProfile(userProfile); 
		}
		return userProfile;
	}
	
	/**
	 * Return the user identified by userId.
	 * @param userProfile
	 * @return userBean
	 * @see org.esupportail.lecture.domain.DomainService#getConnectedUser(UserProfile)
	 */
	public UserBean getConnectedUser(final UserProfile userProfile) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("getConnectedUser(" + userProfile.getUserId() + ")");
		}
		
		/* userBean creation */
		UserBean user = new UserBean(userProfile);
		
		return user;
	}

	/**
	 * Returns the contextBean corresponding to the context identified by contextId for user userId.
	 * @param userProfile current userProfile
	 * @param contextId id of the context to get
	 * @return contextBean
	 * @throws InternalDomainException 
	 * @see org.esupportail.lecture.domain.DomainService#getContext(UserProfile, String)
	 */
	public ContextBean getContext(final UserProfile userProfile, final String contextId) 
		throws InternalDomainException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("getContext(" + userProfile.getUserId() + "," + contextId + ")");
		}
		ContextBean contextBean;
		/* Get customContext */
		try {
			CustomContext customContext = userProfile.getCustomContext(contextId);	
			/* Make the contextUserBean to display */
			contextBean = new ContextBean(customContext);
		} catch (ContextNotFoundException e) {
			String errorMsg = "Unable to getContext because context is not found";
			LOG.error(errorMsg);
			throw new InternalDomainException(errorMsg, e);
		}
		return contextBean;		
	}

	/**
	 * Returns a list of categoryBean - corresponding to categories to display on interface.
	 * into context contextId for user userId
	 * Displayed categories are one that user : 
	 * - is subscribed to (obliged or allowed or autoSubscribe)
	 * - has created (personal categories)
	 * @param userProfile current userProfile
	 * @param contextId  id of the current context 
	 * @return a list of CategoryBean
	 * @throws InternalDomainException
	 * @see org.esupportail.lecture.domain.DomainService#getDisplayedCategories(UserProfile, String)
	 */
	public List<CategoryBean> getDisplayedCategories(final UserProfile userProfile, final String contextId) 
	throws InternalDomainException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("getDisplayedCategories(" + userProfile.getUserId() + "," + contextId + ")");
		}
		
		/* Get customContext */
		CustomContext customContext;
		try {
			customContext = userProfile.getCustomContext(contextId);
		} catch (ContextNotFoundException e1) {
			String errorMsg = "Unable to getDisplayedCategories because context is not found";
			LOG.error(errorMsg);
			throw new InternalDomainException(errorMsg, e1);
		}

		List<CategoryBean> listCategoryBean = new ArrayList<CategoryBean>();
		List<CustomCategory> customCategories = customContext.getSortedCustomCategories();
		for (CustomCategory customCategory : customCategories) {
			CategoryBean category;
			try {
				category = new CategoryBean(customCategory, customContext);
				if (category instanceof CategoryDummyBean) {
					if (LOG.isDebugEnabled()) {
						LOG.debug("CategoryDummyBean created : " + category.getId());
					}
				}
				listCategoryBean.add(category);
			} catch (CategoryProfileNotFoundException e) {
				LOG.warn("Warning on service 'getDisplayedeCategories(user " 
					+ userProfile.getUserId() + ", context " + contextId 
					+ ") : clean custom source ");
				//userProfile.cleanCustomCategoryFromProfile(customCategory.getElementId());
				userProfile.removeCustomCategoryFromProfile(customCategory.getElementId());
			} catch (InfoDomainException e) {
				LOG.error("Error on service 'getDisplayedCategories(user " 
					+ userProfile.getUserId() + ", context " + contextId 
					+ ") : creation of a CategoryDummyBean");
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
	 * @see org.esupportail.lecture.domain.DomainService#getDisplayedSources(UserProfile, String)
	 */
	public List<SourceBean> getDisplayedSources(final UserProfile userProfile, final String categoryId) 
	throws InternalDomainException, CategoryNotVisibleException, CategoryTimeOutException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("getDisplayedSources(" + userProfile.getUserId() + "," + categoryId + ")");
		}
		
		List<SourceBean> listSourceBean = new ArrayList<SourceBean>();
		try {
			CustomCategory customCategory = userProfile.getCustomCategory(categoryId);
			List<CustomSource> customSources = customCategory.getSortedCustomSources();
				
			for (CustomSource customSource : customSources) {
				SourceBean source;
				try {
					source = new SourceBean(customSource);
					if (source instanceof SourceDummyBean) {
						if (LOG.isDebugEnabled()) {
							LOG.debug("SourceDummyBean created : " + source.getId());
						}
					}
					listSourceBean.add(source);
// GB : No needed because of improve of exception management					
//				} catch (SourceProfileNotFoundException e) {
//					LOG.warn("Warning on service 'getDisplayedSources(user " 
//						+ uid + ", category " + categoryId + ") : clean custom source ");
//					userProfile.removeCustomSourceFromProfile(customSource.getElementId());
				} catch (InfoDomainException e) {
					LOG.error("Error on service 'getDisplayedSources(user "
						+ userProfile.getUserId() + ", category " + categoryId + ") : " 
						+ "creation of a SourceDummyBean");
					source = new SourceDummyBean(e);
					listSourceBean.add(source);
				} catch (DomainServiceException e) {
					LOG.error("Error on service 'getDisplayedSources(user "
						+ userProfile.getUserId() + ", category " + categoryId + ") : " 
						+ "creation of a SourceDummyBean");
					source = new SourceDummyBean(e);
					listSourceBean.add(source);
				}
			}
// GB : No needed because of improve of exception management	
//		} catch	(CategoryProfileNotFoundException e) {
//			String errorMsg = "CategoryProfileNotFoundException for service 'getDisplayedSources(user "
//				+ uid + ", category " + categoryId + ")";
//			LOG.error(errorMsg);
//			//userProfile.cleanCustomCategoryFromProfile(categoryId);
//			userProfile.removeCustomCategoryFromProfile(categoryId);
//			throw new InternalDomainException(errorMsg, e);
		} catch (CustomCategoryNotFoundException e) {
			String errorMsg = "CustomCategoryNotFound for service 'getDisplayedSources(user "
				+ userProfile.getUserId() + ", category " + categoryId + ")\n" 
				+ "User " + userProfile.getUserId() + " is not subscriber of Category " + categoryId;
			LOG.error(errorMsg);
			// TODO (GB RB) Remonter une SubsriptionNotFoundForUserException à la place ?
			throw new InternalDomainException(errorMsg, e);
		}
		
		return listSourceBean;
	}

	/** 
	 * Returns a list of itemBean.
	 * Corresponding to items containing in source sourceId,
	 * in order to be displayed on user interface for user uid
	 * @see org.esupportail.lecture.domain.DomainService#getItems(UserProfile, String)
	 */
	public List<ItemBean> getItems(final UserProfile userProfile, final String sourceId) 
	throws InternalDomainException, SourceNotLoadedException, ManagedCategoryNotLoadedException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("getItems(" + userProfile.getUserId() + "," + sourceId + ")");
		}
		
		List<ItemBean> listItemBean = new ArrayList<ItemBean>();
		CustomSource customSource = null;
		try {
			/* Get current user profile and customCoategory */
			customSource = userProfile.getCustomSource(sourceId);
			List<Item> listItems;
			listItems = customSource.getItems();

			for (Item item : listItems) {
				ItemBean itemBean = new ItemBean(item, customSource);
				listItemBean.add(itemBean);
			}
// GB : No needed because of improve of exception management	
//		} catch (ManagedCategoryProfileNotFoundException e) {
//			String errorMsg = "ManagedCategoryProfileNotFoundException for service 'getItems(user "
//				+ uid + ", source " + sourceId + ")";
//			LOG.error(errorMsg);
//			CustomManagedSource customManagedSource = (CustomManagedSource) customSource;
//			String categoryId = customManagedSource.getManagedSourceProfileParentId();
//			//userProfile.cleanCustomCategoryFromProfile(categoryId);
//			userProfile.removeCustomCategoryFromProfile(categoryId);
//			throw new InternalDomainException(errorMsg, e);
//		} catch	(SourceProfileNotFoundException e) {
//			String errorMsg = "SourceProfileNotFoundException for service 'getItems(user "
//				+ uid + ", source " + sourceId + ")";
//			LOG.error(errorMsg);
//			userProfile.removeCustomSourceFromProfile(sourceId);
//			throw new InternalDomainException(errorMsg, e);
		} catch (CustomSourceNotFoundException e) {
			String errorMsg = "CustomSourceNotFoundException for service 'getItems(user "
				+ userProfile.getUserId() + ", source " + sourceId + ")";
			LOG.error(errorMsg);
			// TODO (GB RB) Remonter une SubsriptionNotFoundForUserException à la place ?
			throw new InternalDomainException(errorMsg, e);
		} catch (ComputeItemsException e) {
			String errorMsg = "ComputeItemsException for service 'getItems(user "
				+ userProfile.getUserId() + ", source " + sourceId + ")";
			LOG.error(errorMsg);
			throw new InternalDomainException(errorMsg, e);
		} 		
		return listItemBean;
	}

	/**
	 * Mark item as read for user uid.
	 * @see org.esupportail.lecture.domain.DomainService#marckItemReadMode(UserProfile, String, String, boolean)
	 */
	public UserProfile marckItemReadMode(final UserProfile userProfile, final String sourceId, 
		final String itemId, final boolean isRead) 
	throws InternalDomainException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("marckItemReadMode(" + userProfile.getUserId() + "," + sourceId + "," + itemId + "," + isRead + ")");
		}
		try {
			//merge to attache again userProfile  
			UserProfile ret = DomainTools.getDaoService().mergeUserProfile(userProfile); 
			/* Get customCoategory */
			CustomSource customSource;
			customSource = ret.getCustomSource(sourceId);
			customSource.setItemReadMode(itemId, isRead);
			//No need to call DomainTools.getDaoService().saveUserProfile(userProfile) because of hibernate tracking
			return ret;
		} catch (CustomSourceNotFoundException e) {
			String errorMsg = "CustomSourceNotFoundException for service 'marckItemReadMode(user "
				+ userProfile.getUserId() + ", source " + sourceId + ", item " + itemId + ", isRead " + isRead + ")";
			LOG.error(errorMsg);
			// TODO (GB RB) Remonter une SubsriptionNotFoundForUserException à la place ?
			throw new InternalDomainException(errorMsg, e);
		}
		
	}
	
	
	/**
	 * @see DomainService#markItemDisplayMode(UserProfile, String, ItemDisplayMode)
	 */
	public UserProfile markItemDisplayMode(final UserProfile userProfile, final String sourceId, 
			final ItemDisplayMode mode) throws InternalDomainException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("markItemDisplayMode(" + userProfile.getUserId() + "," + sourceId + "," + mode + ")");
		}
		try {
			//merge to attache again userProfile  
			UserProfile ret = DomainTools.getDaoService().mergeUserProfile(userProfile); 
			/* Get customCategory */
			CustomSource customSource;
			customSource = ret.getCustomSource(sourceId);
			customSource.modifyItemDisplayMode(mode);
			return ret;
		} catch (CustomSourceNotFoundException e) {
			String errorMsg = "CustomSourceNotFoundException for service 'markItemDisplayMode(user "
				+ userProfile.getUserId() + ", source " + sourceId + ", mode " + mode + ")";
			LOG.error(errorMsg);
			// TODO (GB RB) Remonter une SubsriptionNotFoundForUserException à la place ?
			throw new InternalDomainException(errorMsg, e);
		}
		
	}
	
	
	/**
	 * @see org.esupportail.lecture.domain.DomainService#setTreeSize(UserProfile, String, int)
	 */
	public UserProfile setTreeSize(final UserProfile userProfile, final String contextId, final int size) 
	throws InternalDomainException, TreeSizeErrorException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("setTreeSize(" + userProfile.getUserId() + "," + contextId + "," + size + ")");
		}
		//merge to attache again userProfile  
		UserProfile ret = DomainTools.getDaoService().mergeUserProfile(userProfile); 
		/* Get customContext */
		CustomContext customContext;
		try {
			customContext = ret.getCustomContext(contextId);
		} catch (ContextNotFoundException e) {
			String errorMsg = "Unable to setTreeSize because context is not found";
			LOG.error(errorMsg);
			throw new InternalDomainException(errorMsg, e);
		}
		customContext.modifyTreeSize(size);
		return ret;
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#foldCategory(UserProfile, String, String)
	 */
	public UserProfile foldCategory(final UserProfile userProfile, final String cxtId, final String catId) 
	throws InternalDomainException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("foldCategory(" + userProfile.getUserId() + "," + cxtId + "," + catId + ")");
		}
		//merge to attache again userProfile  
		UserProfile ret = DomainTools.getDaoService().mergeUserProfile(userProfile); 
		/* Get customContext */
		CustomContext customContext;
		try {
			customContext = ret.getCustomContext(cxtId);
		} catch (ContextNotFoundException e) {
			String errorMsg = "Unable to foldCategory because context is not found";
			LOG.error(errorMsg);
			throw new InternalDomainException(errorMsg, e);
		}
		customContext.foldCategory(catId);
		return ret;
	}
	
	/**
	 * @see org.esupportail.lecture.domain.DomainService#unfoldCategory(UserProfile, String, String)
	 */
	public UserProfile unfoldCategory(final UserProfile userProfile, final String cxtId, final String catId) 
	throws InternalDomainException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("unfoldCategory(" + userProfile.getUserId() + "," + cxtId + "," + catId + ")");
		}
		//merge to attache again userProfile  
		UserProfile ret = DomainTools.getDaoService().mergeUserProfile(userProfile); 
		/* Get customContext */
		CustomContext customContext;
		try {
			customContext = ret.getCustomContext(cxtId);
		} catch (ContextNotFoundException e) {
			String errorMsg = "Unable to unfoldCategory because context is not found";
			LOG.error(errorMsg);
			throw new InternalDomainException(errorMsg, e);
		}
		customContext.unfoldCategory(catId);
		return ret;
	}
	
	/*
	 ************************** Methodes - services - mode EDIT ************************************/
	
	/**
	 * @see org.esupportail.lecture.domain.DomainService#getVisibleCategories(UserProfile, String)
	 */
	public List<CategoryBean> getVisibleCategories(final UserProfile userProfile, final String contextId) 
	throws InternalDomainException, ManagedCategoryNotLoadedException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("getVisibleCategories(" + userProfile.getUserId() + "," + contextId + ")");
		}
		List<CategoryBean> listCategoryBean = new ArrayList<CategoryBean>();
		CustomContext customContext;
		try {
			customContext = userProfile.getCustomContext(contextId);
		} catch (ContextNotFoundException e) {
			String errorMsg = "Unable to getVisibleCategories because context is not found";
			LOG.error(errorMsg);
			throw new InternalDomainException(errorMsg, e);
		}
		List<CoupleProfileAvailability> couples = customContext.getVisibleCategories();
		for (CoupleProfileAvailability couple : couples) {
			CategoryBean category;
			category = new CategoryBean(couple);
			listCategoryBean.add(category);
		}	
		return listCategoryBean;

	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#getVisibleSources(UserProfile, String)
	 */
	public List<SourceBean> getVisibleSources(final UserProfile userProfile, final String categoryId) 
	throws CategoryNotVisibleException, InternalDomainException, CategoryTimeOutException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("getVisibleSources(" + userProfile.getUserId() + "," + categoryId + ")");
		}
		List<SourceBean> listSourceBean = new ArrayList<SourceBean>();
		try {
			
			CustomCategory customCategory = userProfile.getCustomCategory(categoryId);
			List<CoupleProfileAvailability> couples = customCategory.getVisibleSources();
			for (CoupleProfileAvailability couple : couples) {
				SourceBean source;
//				try {
					source = new SourceBean(couple);
					listSourceBean.add(source);
// GB : No needed because of improve of exception management	
//				}catch (InfoDomainException e) {
//					LOG.error("Error on service 'getVisibleSources(user "
//						+uid+", category "+categoryId+") : creation of a SourceDummyBean");
//					source = new SourceDummyBean(e);
//					listSourceBean.add(source);
//				}
			}	
// GB : No needed because of improve of exception management	
//		} catch	(CategoryProfileNotFoundException e) {
//			String errorMsg = "CategoryProfileNotFoundException for service 'getVisibleSources(user "
//				+ uid + ", category " + categoryId + ")";
//			LOG.error(errorMsg);
//			//userProfile.cleanCustomCategoryFromProfile(categoryId);
//			userProfile.removeCustomCategoryFromProfile(categoryId);
//			throw new InternalDomainException(errorMsg, e);
		} catch (CustomCategoryNotFoundException e) {
			String errorMsg = "CustomCategoryNotFound for service 'getVisibleSources(user " 
				+ userProfile.getUserId() + ", category " + categoryId + ")" 
				+ "User " + userProfile.getUserId() + " is not subscriber of Category " + categoryId;
			LOG.error(errorMsg);
			// TODO (GB RB) Remonter une SubsriptionNotFoundForUserException à la place ?
			throw new InternalDomainException(errorMsg, e);
		}
		 return listSourceBean;
		
	}
	
	/**
	 * @see DomainService#subscribeToCategory(UserProfile, String, String)
	 */
	public UserProfile subscribeToCategory(final UserProfile userProfile, final String contextId, final String categoryId) 
	throws InternalDomainException, CategoryNotVisibleException  {
		if (LOG.isDebugEnabled()) {
			LOG.debug("subscribeToCategory(" + userProfile.getUserId() + "," + contextId 
				+ "," + categoryId + ")");
		}
		//merge to attache again userProfile  
		UserProfile ret = DomainTools.getDaoService().mergeUserProfile(userProfile); 
		CustomContext customContext;
		try {
			customContext = ret.getCustomContext(contextId);
		} catch (ContextNotFoundException e) {
			String errorMsg = "Unable to subscribeToCategory because context is not found";
			LOG.error(errorMsg);
			throw new InternalDomainException(errorMsg, e);
		}
		customContext.subscribeToCategory(categoryId);
		return ret;
	}

	

	/**
	 * @see DomainService#subscribeToSource(UserProfile, String, String)
	 */
	public UserProfile subscribeToSource(final UserProfile userProfile, final String categoryId, final String sourceId) 
	throws CategoryNotVisibleException, InternalDomainException, 
	CategoryTimeOutException, SourceNotVisibleException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("subscribeToSource(" + userProfile.getUserId() + "," + categoryId 
				+ "," + sourceId + ")");
		}
		try {
			//merge to attache again userProfile  
			UserProfile ret = DomainTools.getDaoService().mergeUserProfile(userProfile); 
			CustomCategory customCategory = ret.getCustomCategory(categoryId);
			customCategory.subscribeToSource(sourceId);
			return ret;
// GB : No needed because of improve of exception management	
//			} catch	(CategoryProfileNotFoundException e) {
//			String errorMsg = "CategoryProfileNotFoundException for service 'subscribeToSource(user "
//				+ uid + ", category " + categoryId + ", source " + sourceId + ")";
//			LOG.error(errorMsg);
//			//userProfile.cleanCustomCategoryFromProfile(categoryId);
//			userProfile.removeCustomCategoryFromProfile(categoryId);
//			throw new InternalDomainException(errorMsg, e);
		} catch (CustomCategoryNotFoundException e) {
			String errorMsg = "CustomCategoryNotFound for service 'subscribeToSource(user "
				+ userProfile.getUserId() + ", category " + categoryId + ", source " + sourceId + ").\n" 
				+ "User " + userProfile.getUserId() + " is not subscriber of Category " + categoryId;
			LOG.error(errorMsg);
			// TODO (GB RB) Remonter une SubsriptionNotFoundForUserException à la place ?
			throw new InternalDomainException(errorMsg, e);
		} 
	}
	
	/**
	 * @see DomainService#unsubscribeToCategory(UserProfile, String, String)
	 */
	public UserProfile unsubscribeToCategory(final UserProfile userProfile, final String contextId, final String categoryId) 
	throws InternalDomainException, CategoryNotVisibleException, CategoryObligedException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("unsubscribeToCategory(" + userProfile.getUserId() + "," + contextId + "," + categoryId + ")");
		}
		//merge to attache again userProfile  
		UserProfile ret = DomainTools.getDaoService().mergeUserProfile(userProfile); 
		CustomContext customContext;
		try {
			customContext = ret.getCustomContext(contextId);
		} catch (ContextNotFoundException e) {
			String errorMsg = "Unable to unsubscribeToCategory because context is not found";
			LOG.error(errorMsg);
			throw new InternalDomainException(errorMsg, e);
		}
		customContext.unsubscribeToCategory(categoryId);
		return ret;
	}

	/**
	 * @see DomainService#unsubscribeToSource(UserProfile, String, String)
	 */
	public UserProfile unsubscribeToSource(final UserProfile userProfile, final String categoryId, final String sourceId) 
	throws InternalDomainException, CategoryNotVisibleException, CategoryTimeOutException, SourceObligedException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("subscribeToSource(" + userProfile.getUserId() + "," + categoryId + "," 
				+ sourceId + ")");
		}
		try {
			//merge to attache again userProfile  
			UserProfile ret = DomainTools.getDaoService().mergeUserProfile(userProfile); 
			CustomCategory customCategory = ret.getCustomCategory(categoryId);
			customCategory.unsubscribeToSource(sourceId);
			return ret;
// GB : No needed because of improve of exception management	
//		} catch	(CategoryProfileNotFoundException e) {
//			String errorMsg = "CategoryProfileNotFoundException for service 'unsubscribeToSource(user "
//				+ uid + ", category " + categoryId + ", source " + sourceId + ")";
//			LOG.error(errorMsg);
//			//userProfile.cleanCustomCategoryFromProfile(categoryId);
//			userProfile.removeCustomCategoryFromProfile(categoryId);
//			throw new InternalDomainException(errorMsg, e);
		} catch (CustomCategoryNotFoundException e) {
			String errorMsg = "CustomCategoryNotFound for service 'unsubscribeToSource(user "
				+ userProfile.getUserId() + ", category " + categoryId + ", source " + sourceId + ").\n" 
				+ "User " + userProfile.getUserId() + " is not subscriber of Category " + categoryId;
			LOG.error(errorMsg);
			// TODO (GB RB) Remonter une SubsriptionNotFoundForUserException à la place ?
			throw new InternalDomainException(errorMsg, e);
		} 	
	}
	
	
	

	/*
	 ************************** Accessors ************************************/
	
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
	 * @see org.esupportail.lecture.domain.DomainService#isGuestMode()
	 */
	public boolean isGuestMode() {
		boolean ret;
		String connectedUser = authenticationService.getAuthInfo().getId();
		if (connectedUser == null) {
			return true;
		}
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
	public void setI18nService(final I18nService service) {
		i18nService = service;
	}

}
