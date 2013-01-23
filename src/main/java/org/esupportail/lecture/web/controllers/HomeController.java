package org.esupportail.lecture.web.controllers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.portlet.PortletSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.commons.services.authentication.AuthenticationService;
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
import org.esupportail.lecture.exceptions.web.WebException;
import org.esupportail.lecture.web.beans.CategoryWebBean;
import org.esupportail.lecture.web.beans.ContextWebBean;
import org.esupportail.lecture.web.beans.ItemWebBean;
import org.esupportail.lecture.web.beans.SourceWebBean;
import org.esupportail.lecture.web.formBeans.ChangeItemDisplayMode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

@Controller
@RequestMapping("VIEW")
public class HomeController {

	/**
	 * Log instance.
	 */
	private static final Log LOG = LogFactory.getLog(HomeController.class);

	/**
	 * Access to facade services (init by Spring).
	 */
	@Resource(name="facadeService")
	private FacadeService facadeService;

	/**
	 * The authentication service.
	 */
	@Resource(name="authenticationService")
	private AuthenticationService authenticationService;

	@RenderMapping()
	public String goHome() {
		return "home";
	}

	/**
	 * Action : toggle Folded State of the selected category.
	 */
	@ActionMapping(params="action=toggleFoldedState")
	public void toggleFoldedState(
			@RequestParam(required=true, value="catID") String catID) {
		if (isGuestMode()) {
			throw new SecurityException("Try to access restricted function is guest mode");
		}
		if (LOG.isDebugEnabled()) LOG.debug("toggleFoldedState("+ catID +")");
		CategoryWebBean selectedCategory = getContext().getCategory(catID);
		//		//toggle expanded status
		selectedCategory.setFolded(!selectedCategory.isFolded());
	}

	/**
	 * Action : Select a context from the tree.
	 */
	@ActionMapping(params="action=selectContext")
	public void selectContext(){
		selectElement(null, null);
	}

	/**
	 * Action : Select a context from the tree.
	 */
	@ActionMapping(params="action=selectCategory")
	public void selectCategory(
			@RequestParam(required=true, value="catID") String catID){
		selectElement(catID, null);
	}

	/**
	 * Action : Select a category and a source from the tree.
	 */
	@ActionMapping(params="action=selectSource")
	public void selectElement(
			@RequestParam(required=true, value="catID") String catID,
			@RequestParam(required=true, value="srcID") String srcID) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("selectElement(" + catID + ", " + srcID + ")");
		}
		// set category focused by user as selected category in the context
		ContextWebBean ctx = getContext();
		CategoryWebBean selectedCategory = ctx.getCategory(catID);
		ctx.setSelectedCategory(selectedCategory);
		//if no selected category then we have to invalidate selected source in all categories
		if (selectedCategory == null) {
			for (CategoryWebBean category : ctx.getCategories()) {
				category.setSelectedSource(null);
			}
		}
		// set source focused by user as selected source in the selected category
		if (selectedCategory != null) {
			SourceWebBean selectedSource = selectedCategory.getSource(srcID);
			selectedCategory.setSelectedSource(selectedSource);
		}
	}

	/**
	 * action : toogle item from read to unread and unread to read.
	 * @param catID Category ID
	 * @param srcID Source ID
	 * @param itemID Item ID
	 */
	@ActionMapping(params="action=toggleItemReadState")
	public void toggleItemReadState(
			@RequestParam(required=true, value="catID") String catID,
			@RequestParam(required=true, value="srcID") String srcID,
			@RequestParam(required=true, value="itemID") String itemID) {
		if (isGuestMode()) {
			throw new SecurityException("Try to access restricted function is guest mode");
		}
		if (LOG.isDebugEnabled()) {
			LOG.debug("toggleItemReadState(" + catID + ", " + srcID + ", " + itemID + ")");
		}
		SourceWebBean selectedSource = getContext().getCategory(catID).getSource(srcID);
		ItemWebBean selectedItem = selectedSource.getItem(itemID);
		//update model
		try {
			UserProfile userProfile = getUserProfile();
			userProfile = facadeService.markItemReadMode(userProfile, 
					srcID, itemID, !selectedItem.isRead());
		} catch (Exception e) {
			throw new WebException("Error in toggleItemReadState", e);
		}
		//update web beans
		if (selectedItem.isRead()) {
			selectedSource.setUnreadItemsNumber(selectedSource.getUnreadItemsNumber() + 1);
		} else {
			if (selectedSource.getUnreadItemsNumber() > 0) {
				selectedSource.setUnreadItemsNumber(selectedSource.getUnreadItemsNumber() - 1);
			}
		}
		selectedItem.setRead(!selectedItem.isRead());
	}

	/**
	 * action : Change display mode
	 */
	@ActionMapping(params="action=changeItemDisplayMode")
	public String changeItemDisplayMode(@ModelAttribute("changeItemDisplayMode") ChangeItemDisplayMode changeItemDisplayMode) {
		if (isGuestMode()) {
			throw new SecurityException("Try to access restricted function is guest mode");
		}
		if (LOG.isDebugEnabled()) {
			LOG.debug("changeItemDisplayMode()");
		}
	//		List<CategoryWebBean> categoryWebBeans = getSelectedOrAllCategories();
	//		if (itemDisplayMode != ItemDisplayMode.UNDEFINED) {
	//			try {
	//				for (CategoryWebBean categoryWebBean : categoryWebBeans) {
	//					List<SourceWebBean> sources = categoryWebBean.getSelectedOrAllSources();
	//					if (sources != null) {
	//						UserProfile userProfile = getUserProfile();
	//						for (SourceWebBean sourceWeb : sources) {
	//							userProfile = getFacadeService().markItemDisplayMode(userProfile,
	//									sourceWeb.getId(), itemDisplayMode);
	//							sourceWeb.setItemDisplayMode(itemDisplayMode);
	//						}
	//						setUserProfile(userProfile);
	//					}
	//				}
	//			} catch (Exception e) {
	//				throw new WebException("Error in changeItemDisplayMode", e);
	//			}			
	//		}
			return "OK";
		}

	/**
	 * @return the ChangeItemDisplayMode to display in form
	 */
	@ModelAttribute("changeItemDisplayMode")
	public ChangeItemDisplayMode getChangeItemDisplayMode() {
		return new ChangeItemDisplayMode();
	}	

	@ModelAttribute("availableItemDisplayModes")
	public List<ItemDisplayMode> getAvailableItemDisplayModes() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("getAvailableItemDisplayModes()");
		}
		List<ItemDisplayMode> ret = new ArrayList<ItemDisplayMode>();
		ContextWebBean currentContext = getContext();
		CategoryWebBean selectedCategory = currentContext.getSelectedCategory();
		if (selectedCategory == null || selectedCategory.getSelectedSource() == null) {
			ret.add(ItemDisplayMode.UNDEFINED);			
		}
		ret.add(ItemDisplayMode.ALL);
		ret.add(ItemDisplayMode.UNREAD);
		ret.add(ItemDisplayMode.UNREADFIRST);
		return ret;
	}
	
	
	/**
	 * Model : Context of the connected user.
	 * @return Returns the context.
	 */
	@ModelAttribute("context")
	public ContextWebBean getContext() {
		final String CONTEXT = "context"; 
		ContextWebBean context = (ContextWebBean) RequestContextHolder.getRequestAttributes().getAttribute(CONTEXT, PortletSession.PORTLET_SCOPE);
		if (context == null) {
			context = new ContextWebBean();
			String ctxId;
			try {
				UserProfile userProfile = getUserProfile();
				ctxId = facadeService.getCurrentContextId();
				ContextBean contextBean = facadeService.getContext(userProfile, ctxId);
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
				context.setTreeVisible(contextBean.getTreeVisible());
				//find categories in this context
				List<CategoryBean> categories = getCategories(ctxId, userProfile);
				List<CategoryWebBean> categoriesWeb = new ArrayList<CategoryWebBean>();
				if (categories != null) {
					for (CategoryBean categoryBean : categories) {
						CategoryWebBean categoryWebBean = populateCategoryWebBean(categoryBean);
						//find sources in this category (if this category is subscribed)
						if (categoryBean.getType() != AvailabilityMode.NOTSUBSCRIBED) {
							List<SourceBean> sources = getSources(categoryBean, userProfile);
							List<SourceWebBean> sourcesWeb = new ArrayList<SourceWebBean>();
							if (sources != null) {
								for (SourceBean sourceBean : sources) {
									SourceWebBean sourceWebBean = populateSourceWebBean(sourceBean, userProfile);
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
			} catch (Exception e) {
				throw new WebException("Error in getContext", e);
			}
			RequestContextHolder.getRequestAttributes().setAttribute(CONTEXT, context, PortletSession.PORTLET_SCOPE);
		}
		return context;
	}

	/**
	 * Model : Guest mode
	 * @return true if current is the guest user.
	 */
	@ModelAttribute("guestMode")
	public boolean isGuestMode() {
		final String GUEST_MODE = "guestMode"; 
		Boolean guestMode = (Boolean) RequestContextHolder.getRequestAttributes().getAttribute(GUEST_MODE, PortletSession.PORTLET_SCOPE);
		if (guestMode == null) {
			guestMode = facadeService.isGuestMode();
			RequestContextHolder.getRequestAttributes().setAttribute(GUEST_MODE, guestMode, PortletSession.PORTLET_SCOPE);
		}
		return guestMode;
	}

	/**
	 * @return the connected user UID
	 */
	private String getUID() {
		String ret = DomainTools.getCurrentUserId(authenticationService);
		return ret;
	}

	/**
	 * populate a SourceWebBean from a SourceBean.
	 * @param sourceBean
	 * @param userProfile 
	 * @return populated SourceWebBean
	 * @throws DomainServiceException
	 */
	private SourceWebBean populateSourceWebBean(final SourceBean sourceBean, UserProfile userProfile) throws DomainServiceException {
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
			//get Item for the source
			List<ItemBean> itemsBeans = facadeService.getItems(userProfile, sourceBean.getId());
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
	 * @param categoryBean
	 * @param userProfile 
	 * @return list of available sources
	 * @throws DomainServiceException
	 */
	private List<SourceBean> getSources(final CategoryBean categoryBean, UserProfile userProfile) throws DomainServiceException {
		//this method need to be overwrite in edit controller (VisibledSource and not just DisplayedSources)
		List<SourceBean> tempListSourceBean = null;
		List<SourceBean> ret = new ArrayList<SourceBean>();
		String catId;
		catId = categoryBean.getId();
		tempListSourceBean = facadeService.getDisplayedSources(userProfile, catId);
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
	 * @param categoryBean
	 * @return populated CategoryWebBean
	 */
	private CategoryWebBean populateCategoryWebBean(final CategoryBean categoryBean) {
		CategoryWebBean categoryWebBean =  new CategoryWebBean();
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
	 * @param ctxtId
	 * @param userProfile 
	 * @return list of available categories
	 * @throws InternalDomainException 
	 */
	private List<CategoryBean> getCategories(final String ctxtId, UserProfile userProfile) throws InternalDomainException {
		//Note: this method need to be overwrite in edit controller
		List<CategoryBean> ret = new ArrayList<CategoryBean>();
		List<CategoryBean> categories = facadeService.getDisplayedCategories(userProfile, ctxtId);
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
	 * To display information about the custom Context of the connected user.
	 * @return Returns the userProfile.
	 */
	private UserProfile getUserProfile() {
		final String USER_PROFILE = "userProfile"; 
		UserProfile userProfile = (UserProfile) RequestContextHolder.getRequestAttributes().getAttribute(USER_PROFILE, PortletSession.PORTLET_SCOPE);
		if (userProfile == null) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("getUserProfile() :  userProfile not yet loaded: loading...");			
			}
			try {
				userProfile = facadeService.getUserProfile(getUID());
				RequestContextHolder.getRequestAttributes().setAttribute(USER_PROFILE, userProfile, PortletSession.PORTLET_SCOPE);
			} catch (Exception e) {
				throw new WebException("Error in getUserProfile", e);
			} 
		}
		return userProfile;
	}

}
