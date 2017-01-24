/**
 * ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium For any
 * information please refer to http://esup-helpdesk.sourceforge.net You may
 * obtain a copy of the licence at http://www.esup-portail.org/license/
 */
package org.esupportail.lecture.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

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
import org.esupportail.lecture.domain.model.AvailabilityMode;
import org.esupportail.lecture.domain.model.CoupleProfileAvailability;
import org.esupportail.lecture.domain.model.CustomCategory;
import org.esupportail.lecture.domain.model.CustomContext;
import org.esupportail.lecture.domain.model.CustomSource;
import org.esupportail.lecture.domain.model.Item;
import org.esupportail.lecture.domain.model.ItemDisplayMode;
import org.esupportail.lecture.domain.model.UserProfile;
import org.esupportail.lecture.domain.model.VersionManager;
import org.esupportail.lecture.exceptions.domain.CategoryNotVisibleException;
import org.esupportail.lecture.exceptions.domain.CategoryObligedException;
import org.esupportail.lecture.exceptions.domain.CategoryProfileNotFoundException;
import org.esupportail.lecture.exceptions.domain.CategoryTimeOutException;
import org.esupportail.lecture.exceptions.domain.ComputeItemsException;
import org.esupportail.lecture.exceptions.domain.CustomCategoryNotFoundException;
import org.esupportail.lecture.exceptions.domain.CustomSourceNotFoundException;
import org.esupportail.lecture.exceptions.domain.DomainServiceException;
import org.esupportail.lecture.exceptions.domain.InfoDomainException;
import org.esupportail.lecture.exceptions.domain.InternalDomainException;
import org.esupportail.lecture.exceptions.domain.ManagedCategoryNotLoadedException;
import org.esupportail.lecture.exceptions.domain.SourceNotLoadedException;
import org.esupportail.lecture.exceptions.domain.SourceNotVisibleException;
import org.esupportail.lecture.exceptions.domain.SourceObligedException;
import org.esupportail.lecture.exceptions.domain.TreeSizeErrorException;
import org.esupportail.lecture.exceptions.web.WebException;
import org.esupportail.lecture.web.beans.CategoryWebBean;
import org.esupportail.lecture.web.beans.ContextWebBean;
import org.esupportail.lecture.web.beans.SourceWebBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.util.Assert;

/**
 * Service implementation provided by domain layer All of services are available
 * for a user only if he has a customContext defined in his userProfile. To have
 * a customContext defined in a userProfile, the service getContext must have
 * been called one time (over several user session) This class throws
 * ContextNotFoundException because getting context is not an automatic
 * research, it is leading by higher layer.
 *
 * @author gbouteil
 */
public class DomainServiceImpl implements DomainService, InitializingBean {
	/*
	 ************************** PROPERTIES ********************************
	 */

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
	 ************************** INIT
	 **********************************/
	/**
	 * default constructor.
	 */
	public DomainServiceImpl() {
		super();
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		Assert.notNull(authenticationService,
				"property authenticationService of class " + this.getClass().getName() + " can not be null");
		Assert.notNull(i18nService, "property i18nService of class " + this.getClass().getName() + " can not be null");
	}

	/**
	 * return the user profile identified by "userId".
	 * 
	 * @param userId
	 *            : identifient of the user profile
	 * @return the user profile
	 */
	private synchronized UserProfile getUserProfile(final String userId) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("getFreshUserProfile(" + userId + ")");
		}
		UserProfile userProfile = DomainTools.getDaoService().getUserProfile(userId);
		if (userProfile == null) {
			userProfile = DomainTools.getDaoService().mergeUserProfile(new UserProfile(userId));
		}
		return userProfile;
	}

	/**
	 * Returns a list of categoryBean - corresponding to categories to display
	 * on interface. into context contextId for user userId Displayed categories
	 * are one that user : - is subscribed to (obliged or allowed or
	 * autoSubscribe) - has created (personal categories)
	 *
	 * @param userProfile
	 *            current userProfile
	 * @param contextId
	 *            id of the current context
	 * @return a list of CategoryBean
	 * @throws InternalDomainException
	 */
	private List<CategoryBean> getDisplayedCategories(final UserProfile userProfile, final String contextId)
			throws InternalDomainException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("getDisplayedCategories(" + userProfile.getUserId() + "," + contextId + ")");
		}

		/* Get customContext */
		CustomContext customContext;
		customContext = userProfile.getCustomContext(contextId);
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
				LOG.warn("Warning on service 'getDisplayedeCategories(user " + userProfile.getUserId() + ", context "
						+ contextId + ") : clean custom source ");
				// userProfile.cleanCustomCategoryFromProfile(customCategory.getElementId());
				userProfile.removeCustomCategoryFromProfile(customCategory.getElementId());
			} catch (InfoDomainException e) {
				LOG.error("Error on service 'getDisplayedCategories(user " + userProfile.getUserId() + ", context "
						+ contextId + ") : creation of a CategoryDummyBean");
				category = new CategoryDummyBean(e);
				listCategoryBean.add(category);
			}

		}
		return listCategoryBean;
	}

	/**
	 * Returns a list of sourceBean - corresponding to available categories to
	 * display on interface. into category categoryId for user userId Available
	 * sources are one that user : - is subscribed to (obliged or allowed or
	 * autoSubscribe) - has created (personal sources)
	 */
	private List<SourceBean> getDisplayedSources(final UserProfile userProfile, final String categoryId)
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
				} catch (InfoDomainException e) {
					LOG.error("Error on service 'getDisplayedSources(user " + userProfile.getUserId() + ", category "
							+ categoryId + ") : " + "creation of a SourceDummyBean");
					source = new SourceDummyBean(e);
					listSourceBean.add(source);
				} catch (DomainServiceException e) {
					LOG.error("Error on service 'getDisplayedSources(user " + userProfile.getUserId() + ", category "
							+ categoryId + ") : " + "creation of a SourceDummyBean");
					source = new SourceDummyBean(e);
					listSourceBean.add(source);
				}
			}
		} catch (CustomCategoryNotFoundException e) {
			String errorMsg = "CustomCategoryNotFound for service 'getDisplayedSources(user " + userProfile.getUserId()
					+ ", category " + categoryId + ")\n" + "User " + userProfile.getUserId()
					+ " is not subscriber of Category " + categoryId;
			LOG.error(errorMsg);
			throw new InternalDomainException(errorMsg, e);
		}

		return listSourceBean;
	}

	/**
	 * @param sourceId
	 * @param userProfile
	 * @return List(ItemBean)
	 * @throws SourceNotLoadedException
	 * @throws InternalDomainException
	 * @throws ManagedCategoryNotLoadedException
	 */
	private List<ItemBean> getItems(final UserProfile userProfile, final String sourceId)
			throws InternalDomainException, SourceNotLoadedException, ManagedCategoryNotLoadedException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("getItems(" + userProfile.getUserId() + "," + sourceId + ")");
		}

		List<ItemBean> listItemBean = new ArrayList<ItemBean>();
		try {
			/* Get current user profile and customCoategory */
			CustomSource customSource = userProfile.getCustomSource(sourceId);
			List<Item> listItems;
			listItems = customSource.getItems();

			for (Item item : listItems) {
				ItemBean itemBean = new ItemBean(item, customSource);
				listItemBean.add(itemBean);
			}
		} catch (CustomSourceNotFoundException e) {
			String errorMsg = "CustomSourceNotFoundException for service 'getItems(user " + userProfile.getUserId()
					+ ", source " + sourceId + ")";
			LOG.error(errorMsg);
			throw new InternalDomainException(errorMsg, e);
		} catch (ComputeItemsException e) {
			String errorMsg = "ComputeItemsException for service 'getItems(user " + userProfile.getUserId()
					+ ", source " + sourceId + ")";
			LOG.error(errorMsg);
			throw new InternalDomainException(errorMsg, e);
		}
		return listItemBean;
	}

	/**
	 * Mark item as read for user uid.
	 *
	 * @see org.esupportail.lecture.domain.DomainService#marckItemReadMode(UserProfile,
	 *      String, String, boolean)
	 */
	@Override
	public void markItemReadMode(final String userId, final String sourceId, final String itemId, final boolean isRead,
			final boolean isPubliserMode) throws InternalDomainException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("marckItemReadMode(" + userId + "," + sourceId + "," + itemId + "," + isRead + ")");
		}
		try {
			/* Get customCoategory */
			UserProfile userProfile = getUserProfile(userId);
			if (isPubliserMode) {
				// Modif pour marquer lu l'element sur toutes ses sources
				for (CustomSource csrs : userProfile.getCustomSources().values()) {
					try {
						for (Item itm : csrs.getItems()) {
							if (itm.getId().equals(itemId)) {
								csrs.setItemReadMode(itemId, isRead);
							}
						}
					} catch (SourceNotLoadedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ManagedCategoryNotLoadedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ComputeItemsException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} else {
				CustomSource customSource;
				customSource = userProfile.getCustomSource(sourceId);
				customSource.setItemReadMode(itemId, isRead);
			}

		} catch (CustomSourceNotFoundException e) {
			String errorMsg = "CustomSourceNotFoundException for service 'marckItemReadMode(user " + userId
					+ ", source " + sourceId + ", item " + itemId + ", isRead " + isRead + ")";
			LOG.error(errorMsg);
			throw new InternalDomainException(errorMsg, e);
		}
	}

	/**
	 * @see DomainService#markItemDisplayMode(UserProfile, String,
	 *      ItemDisplayMode)
	 */
	@Override
	public UserProfile markItemDisplayMode(final UserProfile userProfile, final String sourceId,
			final ItemDisplayMode mode) throws InternalDomainException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("markItemDisplayMode(" + userProfile.getUserId() + "," + sourceId + "," + mode + ")");
		}
		try {
			UserProfile ret = getAttachedUserProfile(userProfile);
			/* Get customCategory */
			CustomSource customSource;
			customSource = ret.getCustomSource(sourceId);
			customSource.modifyItemDisplayMode(mode);
			return ret;
		} catch (CustomSourceNotFoundException e) {
			String errorMsg = "CustomSourceNotFoundException for service 'markItemDisplayMode(user "
					+ userProfile.getUserId() + ", source " + sourceId + ", mode " + mode + ")";
			LOG.error(errorMsg);
			throw new InternalDomainException(errorMsg, e);
		}

	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#setTreeSize(UserProfile,
	 *      String, int)
	 */
	@Override
	public UserProfile setTreeSize(final UserProfile userProfile, final String contextId, final int size)
			throws InternalDomainException, TreeSizeErrorException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("setTreeSize(" + userProfile.getUserId() + "," + contextId + "," + size + ")");
		}
		UserProfile ret = getAttachedUserProfile(userProfile);
		/* Get customContext */
		CustomContext customContext;
		customContext = ret.getCustomContext(contextId);
		customContext.modifyTreeSize(size);
		return ret;
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#foldCategory(UserProfile,
	 *      String, String)
	 */
	@Override
	public UserProfile foldCategory(final UserProfile userProfile, final String cxtId, final String catId)
			throws InternalDomainException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("foldCategory(" + userProfile.getUserId() + "," + cxtId + "," + catId + ")");
		}
		UserProfile ret = getAttachedUserProfile(userProfile);
		/* Get customContext */
		CustomContext customContext;
		customContext = ret.getCustomContext(cxtId);
		customContext.foldCategory(catId);
		return ret;
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#unfoldCategory(UserProfile,
	 *      String, String)
	 */
	@Override
	public UserProfile unfoldCategory(final UserProfile userProfile, final String cxtId, final String catId)
			throws InternalDomainException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("unfoldCategory(" + userProfile.getUserId() + "," + cxtId + "," + catId + ")");
		}
		UserProfile ret = getAttachedUserProfile(userProfile);
		/* Get customContext */
		CustomContext customContext;
		customContext = ret.getCustomContext(cxtId);
		customContext.unfoldCategory(catId);
		return ret;
	}

	/**
	 * Return visible categories. Obliged, subscribed, obliged for managed
	 * category or personal category. This for a contextId for user userProfile
	 * (for EDIT mode)
	 * 
	 * @param contextId
	 * @param userProfile
	 * @return List of CategoryBean
	 * @throws InternalDomainException
	 * @throws ManagedCategoryNotLoadedException
	 */
	private List<CategoryBean> getVisibleCategories(final String contextId, final UserProfile userProfile)
			throws InternalDomainException, ManagedCategoryNotLoadedException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("getVisibleCategories(" + userProfile.getUserId() + "," + contextId + ")");
		}
		List<CategoryBean> listCategoryBean = new ArrayList<CategoryBean>();
		CustomContext customContext;
		customContext = userProfile.getCustomContext(contextId);
		List<CoupleProfileAvailability> couples = customContext.getVisibleCategories();
		for (CoupleProfileAvailability couple : couples) {
			CategoryBean category;
			category = new CategoryBean(couple);
			listCategoryBean.add(category);
		}
		return listCategoryBean;

	}

	/**
	 * @param categoryId
	 * @param userProfile
	 * @return List(SourceBean)
	 * @throws CategoryNotVisibleException
	 * @throws InternalDomainException
	 * @throws CategoryTimeOutException
	 * @see FacadeService#getVisibleSources(UserProfile, String)
	 */
	private List<SourceBean> getVisibleSources(final CategoryBean categoryBean, final UserProfile userProfile)
			throws CategoryNotVisibleException, InternalDomainException, CategoryTimeOutException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("getVisibleSources(" + categoryBean.getId() + "," + userProfile.getUserId() + ")");
		}
		List<SourceBean> listSourceBean = new ArrayList<SourceBean>();
		String catId = categoryBean.getId();
		try {

			CustomCategory customCategory = userProfile.getCustomCategory(catId);
			List<CoupleProfileAvailability> couples = customCategory.getVisibleSources();
			for (CoupleProfileAvailability couple : couples) {
				SourceBean source;
				source = new SourceBean(couple);
				listSourceBean.add(source);
			}
		} catch (CustomCategoryNotFoundException e) {
			String errorMsg = "CustomCategoryNotFound for service 'getVisibleSources(user " + userProfile.getUserId()
					+ ", category " + catId + ")" + "User " + userProfile.getUserId()
					+ " is not subscriber of Category " + catId;
			LOG.error(errorMsg);
			throw new InternalDomainException(errorMsg, e);
		}
		return listSourceBean;

	}

	/**
	 * @see DomainService#subscribeToCategory(UserProfile, String, String)
	 */
	@Override
	public UserProfile subscribeToCategory(final String userId, final String contextId, final String categoryId)
			throws InternalDomainException, CategoryNotVisibleException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("subscribeToCategory(" + userId + "," + contextId + "," + categoryId + ")");
		}
		UserProfile ret = getUserProfile(userId);
		CustomContext customContext;
		customContext = ret.getCustomContext(contextId);
		customContext.subscribeToCategory(categoryId);
		return ret;
	}

	/**
	 * @see DomainService#subscribeToSource(UserProfile, String, String)
	 */
	@Override
	public UserProfile subscribeToSource(final String userId, final String categoryId, final String sourceId)
			throws CategoryNotVisibleException, InternalDomainException, CategoryTimeOutException,
			SourceNotVisibleException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("subscribeToSource(" + userId + "," + categoryId + "," + sourceId + ")");
		}
		try {
			UserProfile ret = getAttachedUserProfile(getUserProfile(userId));
			CustomCategory customCategory = ret.getCustomCategory(categoryId);
			customCategory.subscribeToSource(sourceId);
			return ret;
		} catch (CustomCategoryNotFoundException e) {
			String errorMsg = "CustomCategoryNotFound for service 'subscribeToSource(user " + userId + ", category "
					+ categoryId + ", source " + sourceId + ").\n" + "User " + userId
					+ " is not subscriber of Category " + categoryId;
			LOG.error(errorMsg);
			throw new InternalDomainException(errorMsg, e);
		}
	}

	/**
	 * @see DomainService#unsubscribeToCategory(UserProfile, String, String)
	 */
	@Override
	public UserProfile unsubscribeToCategory(final String userId, final String contextId, final String categoryId)
			throws InternalDomainException, CategoryNotVisibleException, CategoryObligedException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("unsubscribeToCategory(" + userId + "," + contextId + "," + categoryId + ")");
		}
		UserProfile ret = getUserProfile(userId);
		CustomContext customContext;
		customContext = ret.getCustomContext(contextId);
		customContext.unsubscribeToCategory(categoryId);
		return ret;
	}

	/**
	 * @see DomainService#unsubscribeToSource(UserProfile, String, String)
	 */
	@Override
	public UserProfile unsubscribeToSource(final String userId, final String categoryId, final String sourceId)
			throws InternalDomainException, CategoryNotVisibleException, CategoryTimeOutException,
			SourceObligedException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("subscribeToSource(" + userId + "," + categoryId + "," + sourceId + ")");
		}
		try {
			UserProfile ret = getAttachedUserProfile(getUserProfile(userId));
			CustomCategory customCategory = ret.getCustomCategory(categoryId);
			customCategory.unsubscribeToSource(sourceId);
			return ret;
		} catch (CustomCategoryNotFoundException e) {
			String errorMsg = "CustomCategoryNotFound for service 'unsubscribeToSource(user " + userId + ", category "
					+ categoryId + ", source " + sourceId + ").\n" + "User " + userId
					+ " is not subscriber of Category " + categoryId;
			LOG.error(errorMsg);
			throw new InternalDomainException(errorMsg, e);
		}
	}

	/*
	 ************************** Accessors
	 ************************************/
	/**
	 * @see org.esupportail.lecture.domain.DomainService#isGuestMode()
	 */
	@Override
	public boolean isGuestMode() {
		boolean ret;
		String connectedUser = DomainTools.getCurrentUserId(authenticationService);
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

	/**
	 * @see org.esupportail.lecture.domain.DomainService#updateSQL(java.lang.String)
	 */
	@Override
	public void updateSQL(final String query) {
		DomainTools.getDaoService().updateSQL(query);
	}

	/**
	 * @param userProfile
	 *            coming from controller
	 * @return a userProfile attached to HB session
	 */
	private UserProfile getAttachedUserProfile(UserProfile userProfile) {
		// try to find a UserProfile in database by userId
		UserProfile ret = DomainTools.getDaoService().getUserProfile(userProfile.getUserId());
		if (ret == null) {
			// A new userProfile is inserted in database when merging
			ret = DomainTools.getDaoService().mergeUserProfile(userProfile);
		}
		return ret;
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
	 * @see org.esupportail.lecture.domain.DomainService#getDatabaseVersion()
	 */
	@Override
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
	@Override
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
	 * @param authenticationService
	 *            the authenticationService to set
	 */
	public void setAuthenticationService(final AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	/**
	 * @param service
	 *            the i18nService to set
	 */
	public void setI18nService(final I18nService service) {
		i18nService = service;
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#getContext(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public ContextWebBean getContext(String userId, String ctxId, boolean viewDef, int nombreArticle, String lienVue) {
		ContextWebBean context = new ContextWebBean();
		try {
			UserProfile userProfile = getUserProfile(userId);
			ContextBean contextBean = new ContextBean(userProfile.getCustomContext(ctxId));
			context.setName(contextBean.getName());
			context.setId(contextBean.getId());
			context.setDescription(contextBean.getDescription());
			context.setTreeSize(contextBean.getTreeSize());
			context.setViewDef(viewDef);
			context.setNombreArticle(nombreArticle);
			context.setLienVue(lienVue);
			context.setTreeVisible(contextBean.getTreeVisible());
			context.setModePublisher(contextBean.isModePublisher());
			context.setUserCanMarckRead(contextBean.isUserCanMarckRead());
			// find categories in this context
			List<CategoryBean> categories = getCategories(ctxId, userProfile);
			List<CategoryWebBean> categoriesWeb = new ArrayList<CategoryWebBean>();
			if (categories != null) {
				for (CategoryBean categoryBean : categories) {
					CategoryWebBean categoryWebBean = populateCategoryWebBean(categoryBean);
					// find sources in this category (if this category is
					// subscribed)
					if (categoryBean.getType() != AvailabilityMode.NOTSUBSCRIBED) {
						List<SourceBean> sources = getSources(categoryBean, userProfile);
						List<SourceWebBean> sourcesWeb = new ArrayList<SourceWebBean>();
						if (sources != null) {
							for (SourceBean sourceBean : sources) {
								SourceWebBean sourceWebBean = populateSourceWebBean(sourceBean, userProfile);
								// we add the source order in the Category XML
								// definition file
								int xmlOrder = categoryBean.getXMLOrder(sourceBean.getId());
								sourceWebBean.setXmlOrder(xmlOrder);
								sourcesWeb.add(sourceWebBean);
							}
						}
						categoryWebBean.setSources(sourcesWeb);
					}
					int xmlOrder = contextBean.getXMLOrder(categoryBean.getId());
					categoryWebBean.setXmlOrder(xmlOrder);
					categoriesWeb.add(categoryWebBean);
				}
			}
			context.setCategories(categoriesWeb);
		} catch (Exception e) {
			throw new WebException("Error in getContext", e);
		}
		return context;
	}

	@Override
	public ContextWebBean getEditContext(String userId, String ctxId) {
		ContextWebBean context = new ContextWebBean();
		try {
			UserProfile userProfile = getUserProfile(userId);
			ContextBean contextBean = new ContextBean(userProfile.getCustomContext(ctxId));
			context.setName(contextBean.getName());
			context.setId(contextBean.getId());
			context.setDescription(contextBean.getDescription());
			context.setTreeSize(contextBean.getTreeSize());
			context.setTreeVisible(contextBean.getTreeVisible());
			// find categories in this context
			List<CategoryBean> categories = getVisibleCategories(ctxId, userProfile);
			// TODO gérer le getCategories et getSources pour le mode edit
			List<CategoryWebBean> categoriesWeb = new ArrayList<CategoryWebBean>();
			if (categories != null) {
				for (CategoryBean categoryBean : categories) {
					CategoryWebBean categoryWebBean = populateCategoryWebBean(categoryBean);
					// find sources in this category (if this category is
					// subscribed)
					if (categoryBean.getType() != AvailabilityMode.NOTSUBSCRIBED) {
						List<SourceBean> sources = getVisibleSources(categoryBean, userProfile);
						List<SourceWebBean> sourcesWeb = new ArrayList<SourceWebBean>();
						if (sources != null) {
							for (SourceBean sourceBean : sources) {
								SourceWebBean sourceWebBean = populateSourceWebBean(sourceBean, userProfile, false);
								// we add the source order in the Category XML
								// definition file
								int xmlOrder = categoryBean.getXMLOrder(sourceBean.getId());
								sourceWebBean.setXmlOrder(xmlOrder);
								sourcesWeb.add(sourceWebBean);
							}
						}
						categoryWebBean.setSources(sourcesWeb);
					}
					int xmlOrder = contextBean.getXMLOrder(categoryBean.getId());
					categoryWebBean.setXmlOrder(xmlOrder);
					categoriesWeb.add(categoryWebBean);
				}
			}
			context.setCategories(categoriesWeb);
		} catch (Exception e) {
			throw new WebException("Error in getContext", e);
		}
		return context;
	}

	/**
	 * @param ctxtId
	 * @param userProfile
	 * @return list of available categories
	 * @throws InternalDomainException
	 */
	private List<CategoryBean> getCategories(final String ctxtId, UserProfile userProfile)
			throws InternalDomainException {
		// Note: this method need to be overwrite in edit controller
		List<CategoryBean> ret = new ArrayList<CategoryBean>();
		List<CategoryBean> categories = getDisplayedCategories(userProfile, ctxtId);
		// Temporary: remove dummy form the list
		for (Iterator<CategoryBean> iter = categories.iterator(); iter.hasNext();) {
			CategoryBean element = iter.next();
			if (!(element instanceof CategoryDummyBean)) {
				ret.add(element);
			}
		}
		return ret;
	}

	/**
	 * @param categoryBean
	 * @param userProfile
	 * @return list of available sources
	 * @throws DomainServiceException
	 */
	private List<SourceBean> getSources(final CategoryBean categoryBean, UserProfile userProfile)
			throws DomainServiceException {
		// this method need to be overwrite in edit controller (VisibledSource
		// and not just DisplayedSources)
		List<SourceBean> ret = new ArrayList<SourceBean>();
		String catId;
		catId = categoryBean.getId();
		List<SourceBean> tempListSourceBean = getDisplayedSources(userProfile, catId);
		for (Iterator<SourceBean> iter = tempListSourceBean.iterator(); iter.hasNext();) {
			SourceBean element = iter.next();
			if (!(element instanceof SourceDummyBean)) {
				ret.add(element);
			}
		}
		return ret;
	}

	/**
	 * populate a CategoryWebBean from a CategoryBean.
	 *
	 * @param categoryBean
	 * @return populated CategoryWebBean
	 */
	private CategoryWebBean populateCategoryWebBean(final CategoryBean categoryBean) {
		CategoryWebBean categoryWebBean = new CategoryWebBean();
		if (categoryBean instanceof CategoryDummyBean) {
			String cause = ((CategoryDummyBean) categoryBean).getCause().getMessage();
			String id = "DummyCat:" + UUID.randomUUID();
			categoryWebBean.setId(id);
			categoryWebBean.setName("Error!");
			categoryWebBean.setDescription(cause);
		} else {
			categoryWebBean.setId(categoryBean.getId());
			categoryWebBean.setName(categoryBean.getName());
			categoryWebBean.setAvailabilityMode(categoryBean.getType());
			categoryWebBean.setDescription(categoryBean.getDescription());
			categoryWebBean.setUserCanMarkRead(categoryBean.isUserCanMarkRead());
		}
		return categoryWebBean;
	}

	/**
	 * populate a SourceWebBean from a SourceBean.
	 *
	 * @param sourceBean
	 * @param userProfile
	 * @return populated SourceWebBean
	 * @throws DomainServiceException
	 */
	private SourceWebBean populateSourceWebBean(final SourceBean sourceBean, UserProfile userProfile)
			throws DomainServiceException {
		return populateSourceWebBean(sourceBean, userProfile, true);
	}

	/**
	 * populate a SourceWebBean from a SourceBean with or without items
	 * 
	 * @param sourceBean
	 * @param userProfile
	 * @param withItems
	 * @return populated SourceWebBean with or without items
	 * @throws DomainServiceException
	 */
	private SourceWebBean populateSourceWebBean(final SourceBean sourceBean, UserProfile userProfile, boolean withItems)
			throws DomainServiceException {
		SourceWebBean sourceWebBean;
		if (sourceBean instanceof SourceDummyBean) {
			sourceWebBean = new SourceWebBean(null);
			String cause = ((SourceDummyBean) sourceBean).getCause().getMessage();
			String id = "DummySrc:" + UUID.randomUUID();
			sourceWebBean.setId(id);
			sourceWebBean.setName(cause);
			sourceWebBean.setType(AvailabilityMode.OBLIGED);
			sourceWebBean.setItemDisplayMode(ItemDisplayMode.ALL);
			sourceWebBean.setItemsNumber();
			sourceWebBean.setUnreadItemsNumber(0);
		} else {
			// get Item for the source
			List<ItemBean> itemsBeans = new ArrayList<ItemBean>();
			if (withItems) {
				itemsBeans = getItems(userProfile, sourceBean.getId());
			}
			sourceWebBean = new SourceWebBean(itemsBeans);
			sourceWebBean.setId(sourceBean.getId());
			sourceWebBean.setName(sourceBean.getName());
			sourceWebBean.setType(sourceBean.getType());
			sourceWebBean.setItemDisplayMode(sourceBean.getItemDisplayMode());
			sourceWebBean.setItemsNumber();
			sourceWebBean.setUnreadItemsNumber();

			// nouveau parametrage
			sourceWebBean.setHighlight(sourceBean.getHighlight());
			sourceWebBean.setColor(sourceBean.getColor());
		}
		return sourceWebBean;
	}
}
