/**
 * ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
 * For any information please refer to http://esup-helpdesk.sourceforge.net
 * You may obtain a copy of the licence at http://www.esup-portail.org/license/
 */
package org.esupportail.lecture.web.controllers;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.commons.web.controllers.Resettable;
import org.esupportail.lecture.domain.DomainTools;
import org.esupportail.lecture.domain.FacadeService;
import org.esupportail.lecture.domain.beans.CategoryBean;
import org.esupportail.lecture.domain.beans.CategoryDummyBean;
import org.esupportail.lecture.domain.beans.ContextBean;
import org.esupportail.lecture.domain.beans.ItemBean;
import org.esupportail.lecture.domain.beans.SourceBean;
import org.esupportail.lecture.domain.beans.SourceDummyBean;
import org.esupportail.lecture.domain.model.AvailabilityMode;
import org.esupportail.lecture.domain.model.ItemDisplayMode;
import org.esupportail.lecture.domain.model.UserProfile;
import org.esupportail.lecture.exceptions.domain.DomainServiceException;
import org.esupportail.lecture.exceptions.domain.InternalDomainException;
import org.esupportail.lecture.exceptions.domain.InternalExternalException;
import org.esupportail.lecture.exceptions.domain.ManagedCategoryNotLoadedException;
import org.esupportail.lecture.exceptions.domain.SourceNotLoadedException;
import org.esupportail.lecture.exceptions.web.WebException;
import org.esupportail.lecture.web.beans.CategoryWebBean;
import org.esupportail.lecture.web.beans.ContextWebBean;
import org.esupportail.lecture.web.beans.SourceWebBean;
import org.springframework.util.Assert;

/**
 * @author : Raymond 
 */
public abstract class TwoPanesController extends AbstractContextAwareController implements Resettable {
	/**
	 * Log instance.
	 */
	private static final Log LOG = LogFactory.getLog(TwoPanesController.class);
	/**
	 * min value for tree size.
	 */
	private static final int MIN_TREE_SIZE = 20;
	/**
	 * max value for tree size.
	 */
	private static final int MAX_TREE_SIZE = 90;
	/**
	 * increment value when changing tree size.
	 */
	private static final int TREE_SIZE_STEP = 5;
	/**
	 * userProfile Key
	 */
	private static final String USERPROFILE = "userProfile";
	/**
	 * is tree is visible or not.
	 */
	private boolean treeVisible = true;
	/**
	 * Access to facade services (init by Spring).
	 */
	private FacadeService facadeService;
	/**
	 * uid of the connected user.
	 */
	private String uid;
	/**
	 * contextId store the contextId.
	 */
	private String contextId;
	/**
	 * dummyId is used to have an unique ID for DummyCategory or DummySource.
	 * 0 as default.
	 */
	private int dummyId;
	/**
	 * Session with portlet context compatibility.
	 */
	private VirtualSession virtualSession;
	/**
	 * source to set by t:updateActionListener.
	 */
	private SourceWebBean ualSource;
	/**
	 * category to set by t:updateActionListener.
	 */
	private CategoryWebBean ualCategory;

	
	/**
	 * Controller constructor.
	 */
	public TwoPanesController() {
		super();
	}

	/**
	 * Remove Context cache from the virtual session.
	 */
	protected void flushContextFormVirtualSession() {
		virtualSession.remove(getContextKey());
		virtualSession.remove(USERPROFILE);
	}

	/**
	 * @return the context key used to store the context in virtual session
	 */
	abstract String getContextKey();

	/*
	 * **************** Action and listener method ****************
	 */	
	/**
	 * JSF action : change treesize.
	 * @param actionEvent JSF ActionEvent used to know which button is used 
	 */
	public void adjustTreeSize(final ActionEvent actionEvent) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("In adjustTreeSize");
		}
		int treeSize = getTreeSize();
		FacesContext ctx = FacesContext.getCurrentInstance();
		String id = actionEvent.getComponent().getClientId(ctx);
		if (id.endsWith("treeSmallerButton")) {
			if (treeSize > MIN_TREE_SIZE) {
				treeSize -= TREE_SIZE_STEP;
			}
		}
		if (id.endsWith("treeLargerButton")) {
			if (treeSize < MAX_TREE_SIZE) {
				treeSize += TREE_SIZE_STEP;
			}
		}
		try {
			//for current session:
			getContext().setTreeSize(treeSize);
			//store in database:
			UserProfile userProfile = getUserProfile();
			userProfile = getFacadeService().setTreeSize(userProfile, getContextId(), treeSize);
			setUserProfile(userProfile);
		} catch (DomainServiceException e) {
			throw new WebException("Error in adjustTreeSize", e);
		} catch (InternalExternalException e) {
			throw new WebException("Error in adjustTreeSize", e);
		}
	}
	
	/**
	 * JSF action : toogle from tree visible to not visible and not visible to visible.
	 * @return JSF from-outcome
	 */
	public String toggleTreeVisibility() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("In toggleTreeVisibility");
		}
		if (isTreeVisible()) {
			setTreeVisible(false);
		} else {
			setTreeVisible(true);
		}
		return "navigationHome";
	}
	
	/**
	 * @return if tree is visible or not
	 */
	public boolean isTreeVisible() {
		return treeVisible;
	}
	
	/**
	 * @return the size of left tree
	 */
	public int getTreeSize() {
		int ret = getContext().getTreeSize();
		if (ret == 0) {
			ret = MIN_TREE_SIZE;
		}
		return ret;
	}

	/**
	 * set tree visibility to yes or no.
	 * @param treeVisible boolean value for tree visibility
	 */
	public void setTreeVisible(final boolean treeVisible) {
		this.treeVisible = treeVisible;
	}

	/*
	 * **************** Getter and Setter ****************
	 */
	
	/**
	 * For Spring injection of Service Class.
	 * @param facadeService facade of Spring Service Class
	 */
	public void setFacadeService(final FacadeService facadeService) {
		this.facadeService = facadeService;
	}

	/**
	 * @return facadeService
	 */
	public FacadeService getFacadeService() {
		return facadeService;
	}

	/**
	 * To display information about the custom Context of the connected user.
	 * @return Returns the context.
	 */
	public ContextWebBean getContext() {
		String contextName = getContextName();
		ContextWebBean context = (ContextWebBean) virtualSession.get(contextName);
		if (LOG.isTraceEnabled()) {
			// browse sessions
			Enumeration<String> keys = virtualSession.getSessions().keys();
			while (keys.hasMoreElements()) {
				String key = keys.nextElement();
				LOG.trace("session: " + key);
				Hashtable<String, Object> sessions = virtualSession.getSessions().get(key);
				// browse objects in sessions
				Enumeration<String> keys2 = sessions.keys();
				while (keys2.hasMoreElements()) {
					String key2 = keys2.nextElement();
					LOG.trace("  obj: " + key2);
					Object obj = sessions.get(key2);
					if (obj instanceof ContextWebBean) {
						ContextWebBean ctx = (ContextWebBean) obj;
						LOG.trace("    ContextWebBean: " + ctx.getId());
					}
				}
			}
		}
		if (context == null) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("getContext() :  Context (" + contextName 
					+ ") not yet loaded or need to be refreshing : loading...");			
			}
			try {
				//We evaluate the context and we put it in the virtual session
				context = new ContextWebBean();
				String ctxId = getContextId();
				ContextBean contextBean = getFacadeService().getContext(getUserProfile(), ctxId);
				if (contextBean == null) {
					throw new WebException("No context with ID \"" + ctxId
						+ "\" found in lecture-config.xml file. " 
						+ "See this file or portlet preference with name \""
						+ DomainTools.getContext() + "\".");
				}
				context.setName(contextBean.getName());
				context.setId(contextBean.getId());
				context.setDescription(contextBean.getDescription());
				context.setTreeSize(contextBean.getTreeSize());
				context.setTreeVisible(contextBean.isTreeVisible());
				//find categories in this context
				List<CategoryBean> categories = getCategories(ctxId);
				List<CategoryWebBean> categoriesWeb = new ArrayList<CategoryWebBean>();
				if (categories != null) {
					for (CategoryBean categoryBean : categories) {
						CategoryWebBean categoryWebBean = populateCategoryWebBean(categoryBean);
						//find sources in this category (if this category is subscribed)
						if (categoryBean.getType() != AvailabilityMode.NOTSUBSCRIBED) {
							List<SourceBean> sources = getSources(categoryBean);
							List<SourceWebBean> sourcesWeb = new ArrayList<SourceWebBean>();
							if (sources != null) {
								for (SourceBean sourceBean : sources) {
									SourceWebBean sourceWebBean = populateSourceWebBean(sourceBean);
									//we add the source order in the Category XML definition file
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
				virtualSession.put(getContextName(), context);
			} catch (Exception e) {
				throw new WebException("Error in getContext", e);
			} 
		}
		return context;
	}

	/**
	 * To display information about the custom Context of the connected user.
	 * @return Returns the userProfile.
	 */
	protected UserProfile getUserProfile() {
		//get userProfile from session
		UserProfile userProfile = (UserProfile) virtualSession.get(USERPROFILE);
		//if not present cache it
		if (userProfile == null) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("getUserProfile() :  userProfile not yet loaded: loading...");			
			}
			try {
				userProfile = getFacadeService().getUserProfile(getUID());
				//We evaluate the context and we put it in the virtual session
				setUserProfile(userProfile);
			} catch (Exception e) {
				throw new WebException("Error in getUserProfile", e);
			} 
		}
		return userProfile;
	}

	/**
	 * @param userProfile
	 */
	protected void setUserProfile(UserProfile userProfile) {
		virtualSession.put(USERPROFILE, userProfile);
	}

	/**
	 * return the context name of the implementation of twoPanesController.
	 * @return the context name used by virtualSession as key for storing context informations in session
	 */
	protected abstract String getContextName();

	/**
	 * populate a SourceWebBean from a SourceBean.
	 * @param sourceBean
	 * @return populated SourceWebBean
	 * @throws DomainServiceException
	 */
	protected SourceWebBean populateSourceWebBean(final SourceBean sourceBean) throws DomainServiceException {
		SourceWebBean sourceWebBean;
		if (sourceBean instanceof SourceDummyBean) {
			sourceWebBean = new SourceWebBean(null);
			String cause = ((SourceDummyBean) sourceBean).getCause().getMessage();
			String id = "DummySrc:" + dummyId++;
			sourceWebBean.setId(id);
			sourceWebBean.setName(cause);
			sourceWebBean.setType(AvailabilityMode.OBLIGED);
			sourceWebBean.setItemDisplayMode(ItemDisplayMode.ALL);
			sourceWebBean.setItemsNumber();
			sourceWebBean.setUnreadItemsNumber(0);
		} else {
			//get Item for the source
			List<ItemBean> itemsBeans = getItems(sourceBean);
			sourceWebBean = new SourceWebBean(itemsBeans);
			sourceWebBean.setId(sourceBean.getId());
			sourceWebBean.setName(sourceBean.getName());
			sourceWebBean.setType(sourceBean.getType());		
			sourceWebBean.setItemDisplayMode(sourceBean.getItemDisplayMode());
			sourceWebBean.setItemsNumber();
			sourceWebBean.setUnreadItemsNumber();
		}
		return sourceWebBean;
	}

	/**
	 * populate a CategoryWebBean from a CategoryBean.
	 * @param categoryBean
	 * @return populated CategoryWebBean
	 */
	protected CategoryWebBean populateCategoryWebBean(final CategoryBean categoryBean) {
		CategoryWebBean categoryWebBean =  new CategoryWebBean();
		if (categoryBean instanceof CategoryDummyBean) {
			String cause = ((CategoryDummyBean) categoryBean).getCause().getMessage();
			String id = "DummyCat:" + dummyId++;
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
	 * @param categoryBean
	 * @return list of available sources
	 * @throws DomainServiceException
	 */
	protected List<SourceBean> getSources(final CategoryBean categoryBean) throws DomainServiceException {
		//this method need to be overwrite in edit controller (VisibledSource and not just DisplayedSources)
		List<SourceBean> tempListSourceBean = null;
		List<SourceBean> ret = new ArrayList<SourceBean>();
		String catId;
		catId = categoryBean.getId();
		tempListSourceBean = getFacadeService().getDisplayedSources(getUserProfile(), catId);
		for (Iterator<SourceBean> iter = tempListSourceBean.iterator(); iter.hasNext();) {
			SourceBean element = iter.next();
			if (!(element instanceof SourceDummyBean)) {
				ret.add(element);				
			}
		}
		return ret;
	}

	/**
	 * @param ctxtId
	 * @return list of available categories
	 * @throws InternalDomainException 
	 */
	protected List<CategoryBean> getCategories(final String ctxtId) throws InternalDomainException {
		//Note: this method need to be overwrite in edit controller
		List<CategoryBean> ret = new ArrayList<CategoryBean>();
		List<CategoryBean> categories = getFacadeService().getDisplayedCategories(getUserProfile(), ctxtId);
		//Temporary: remove dummy form the list
		for (Iterator<CategoryBean> iter = categories.iterator(); iter.hasNext();) {
			CategoryBean element = iter.next();
			if (!(element instanceof CategoryDummyBean)) {
				ret.add(element);				
			}
		}
		return ret;
	}

	/**
	 * @return the connected user UID
	 */
	public String getUID() {
		if (uid == null) {
			//init the user
			try {
				uid = getSessionController().getCurrentUser().getId(); 
			} catch (Exception e) {
				throw new WebException("Error in getUID", e);
			}
		}
		return uid;
	}

	/**
	 * @return Selection Title
	 */
	public String getSelectionTitle() {
		String ret = null;
		ContextWebBean ctx = getContext();
		CategoryWebBean categoryWebBean = ctx.getSelectedCategory();
		if (categoryWebBean != null) {
			ret = categoryWebBean.getName();
			SourceWebBean sourceWebBean = categoryWebBean.getSelectedSource();
			if (sourceWebBean != null) {
				ret += " > " + sourceWebBean.getName();
			}
		}
		return ret;
	}
	
	/**
	 * @param id of catogory to find in the context
	 * @return the finded category
	 */
	protected CategoryWebBean getCategorieByID(final String id) {
		CategoryWebBean ret = null;
		ContextWebBean ctx = getContext();
		Iterator<CategoryWebBean> iter = ctx.getCategories().iterator();
		while (iter.hasNext()) {
			CategoryWebBean cat = iter.next();
			if (cat.getId() == id) {
				ret = cat;
			}
		}
		return ret;
	}

	/**
	 * @param cat where to find source
	 * @param id of source to find
	 * @return the fund source
	 */
	protected SourceWebBean getSourceByID(final CategoryWebBean cat, final String id) {
		SourceWebBean ret = null;
		Iterator<SourceWebBean> iter = cat.getSources().iterator();
		while (iter.hasNext()) {
			SourceWebBean src = iter.next();
			if (src.getId() == id) {
				ret = src;
			}
		}
		return ret;
	}

	/**
	 * @return Context ID from facade service
	 * @throws InternalExternalException
	 */
	protected String getContextId() throws InternalExternalException {
		if (contextId == null) {
			contextId = getFacadeService().getCurrentContextId();
		}
		return contextId;
	}

	/**
	 * @see org.esupportail.lecture.web.controllers.AbstractContextAwareController#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		Assert.notNull(facadeService, 
				"property facadeService of class " + this.getClass().getName() + " can not be null");
		try {
			virtualSession = new VirtualSession(facadeService.getCurrentContextId());
			setTreeVisible(facadeService.getContext(getUserProfile(), getContextId()).isTreeVisible());
		} catch (InternalExternalException e) {
			throw new WebException("Error in afterPropertiesSet", e);
		} catch (InternalDomainException e) {
			throw new WebException("Error in afterPropertiesSet", e);
		}
	}

	/**
	 * @see org.esupportail.lecture.web.controllers.AbstractDomainAwareBean#reset()
	 */
	@Override
	public void reset() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("reset the controller");
		}
		//reset context and userPrfile from session
		if (virtualSession != null) {
			String contextName = getContextName();
			virtualSession.remove(contextName);
			virtualSession.remove(USERPROFILE);
		}
	}

	/**
	 * @return true if current is the guest user.
	 * also we are in a guest mode
	 * @see org.esupportail.lecture.domain.FacadeService#isGuestMode()
	 */
	public boolean isGuestMode() {
		return facadeService.isGuestMode();
	}

	/**
	 * @return the current selected category
	 */
	public CategoryWebBean getSelectedCategory() {
		CategoryWebBean ret = null;
		ContextWebBean ctx = getContext();
		ret = ctx.getSelectedCategory();
		return ret;
	}

	/**
	 * @param source the source to set by t:updateActionListener
	 */
	public void setUalSource(final SourceWebBean source) {
		this.ualSource = source;
	}

	/**
	 * @return the ualSource
	 */
	public SourceWebBean getUalSource() {
		return ualSource;
	}

	/**
	 * @param category the currentCategory to set by t:updateActionListener
	 */
	public void setUalCategory(final CategoryWebBean category) {
		this.ualCategory = category;
	}

	/**
	 * @return the ualCategory
	 */
	public CategoryWebBean getUalCategory() {
		return ualCategory;
	}

	/**
	 * @return the virtualSession
	 */
	public VirtualSession getVirtualSession() {
		return virtualSession;
	}

	/**
	 * @param virtualSession the virtualSession to set
	 */
	public void setVirtualSession(final VirtualSession virtualSession) {
		this.virtualSession = virtualSession;
	}

	/**
	 * JSF action : select a category or a source from the tree.
	 * Use ualCategory and ualSource valued by t:updateActionListener.
	 * @return JSF from-outcome
	 */
	public String selectElement() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("in selectElement");
		}
		CategoryWebBean selectedCategory = getUalCategory();
		SourceWebBean selectedSource = getUalSource();
		if (selectedCategory != null) {
			//set source focused by user as selected source in the category
			selectedCategory.setSelectedSource(selectedSource);
		}
		// set category focused by user as selected category in the context
		ContextWebBean ctx = getContext();
		ctx.setSelectedCategory(selectedCategory);
		return "OK";
	}

	/**
	 * @param sourceBean
	 * @return a list of ItemBean
	 * @throws SourceNotLoadedException
	 * @throws ManagedCategoryNotLoadedException
	 * @throws InternalDomainException
	 */
	protected List<ItemBean> getItems(final SourceBean sourceBean) throws SourceNotLoadedException,
			ManagedCategoryNotLoadedException, InternalDomainException {
				//must be overwritten in edit mode 
				//(return null and not items because user isn't 
				//probably not already subscribed to source)
				return getFacadeService().getItems(getUserProfile(), sourceBean.getId());
			}

	/**
	 * change Tree Size with mouse
	 * @param treeSize
	 */
	public void changeTreeSize(final int treeSize) {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("changeTreeSize : UID : " + getUID());
			}
			//for current session:
			getContext().setTreeSize(treeSize);
			//store in database:
			UserProfile userProfile = getUserProfile();
			userProfile = getFacadeService().setTreeSize(userProfile, getContextId(), treeSize);
			setUserProfile(userProfile);			
		} catch (DomainServiceException e) {
			throw new WebException("Error in changeTreeSize", e);
		} catch (InternalExternalException e) {
			throw new WebException("Error in changeTreeSize", e);
		} finally {
			// Nothing here ?
		}
	}


}
