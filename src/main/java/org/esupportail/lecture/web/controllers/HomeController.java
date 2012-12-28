package org.esupportail.lecture.web.controllers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

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
import org.esupportail.lecture.exceptions.domain.InternalExternalException;
import org.esupportail.lecture.exceptions.web.WebException;
import org.esupportail.lecture.web.beans.CategoryWebBean;
import org.esupportail.lecture.web.beans.ContextWebBean;
import org.esupportail.lecture.web.beans.SourceWebBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

@Controller
@RequestMapping("VIEW")
@SessionAttributes("context")
public class HomeController {

	/**
	 * contextId store the contextId.
	 */
	private String contextId;
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
	/**
	 * dummyId is used to have an unique ID for DummyCategory or DummySource.
	 * 0 as default.
	 */
	private int dummyId;

	@RenderMapping
	public String goHome() {
		return "home";
	}

	/**
	 * To display information about the custom Context of the connected user.
	 * @return Returns the context.
	 */
	@ModelAttribute("context")
	public ContextWebBean getContext() {
		ContextWebBean context = new ContextWebBean();
		String ctxId;
		try {
			UserProfile userProfile = facadeService.getUserProfile(getUID());
			ctxId = getContextId();
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
		return context;
	}

	/**
	 * @return Context ID from facade service
	 * @throws InternalExternalException
	 */
	private String getContextId() throws InternalExternalException {
		if (contextId == null) {
			contextId = facadeService.getCurrentContextId();
		}
		return contextId;
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
			String id = "DummySrc:" + dummyId++;
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

}
