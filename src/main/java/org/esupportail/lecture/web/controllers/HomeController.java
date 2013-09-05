package org.esupportail.lecture.web.controllers;

import java.util.Locale;

import javax.annotation.Resource;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.ResourceURL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.commons.services.authentication.AuthenticationService;
import org.esupportail.commons.services.i18n.I18nService;
import org.esupportail.lecture.domain.DomainTools;
import org.esupportail.lecture.domain.FacadeService;
import org.esupportail.lecture.domain.model.UserProfile;
import org.esupportail.lecture.exceptions.web.WebException;
import org.esupportail.lecture.web.beans.ContextWebBean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

@Controller
@RequestMapping("VIEW")
public class HomeController {

	final String TREE_VISIBLE = "treeVisible"; 
	final String GUEST_MODE = "guestMode"; 
	final String CONTEXT = "context"; 
	final String CHANGE_ITEM_DISPLAY_MODE = "changeItemDisplayMode";
	final String AVAILABLE_ITEM_DISPLAY_MODE = "availableItemDisplayModes";
	final String STATICRESOURCESPATH = "resourcesPath";
	final String DYNAMICRESOURCESPATTERN = "dynamicResourcesPattern";
	private static final String MESSAGES = "messages";

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

	@Resource(name="i18nService")
	private I18nService i18nService;

	@ResourceMapping(value="getJSON")
	public View getJSON(ResourceRequest request, ResourceResponse response) {
		MappingJacksonJsonView view = new MappingJacksonJsonView();
		view.addStaticAttribute(CONTEXT, getContext());
		view.addStaticAttribute(GUEST_MODE, isGuestMode());
		Locale locale = request.getLocale();
		view.addStaticAttribute(MESSAGES, i18nService.getStrings(locale));
		return view;
	}
	
	
	/**
	 * action : toggle item from read to unread and unread to read.
	 * @param catID Category ID
	 * @param srcID Source ID
	 * @param itemID Item ID
	 */
	@ResourceMapping(value="toggleItemReadState")
	public void toggleItemReadState(
			@RequestParam(required=true, value="p1") String catID,
			@RequestParam(required=true, value="p2") String srcID,
			@RequestParam(required=true, value="p3") String itemID,
			@RequestParam(required=true, value="p4") boolean isRead) {
		if (isGuestMode()) {
			throw new SecurityException("Try to access restricted function is guest mode");
		}
		if (LOG.isDebugEnabled()) {
			LOG.debug("toggleItemReadState(" + catID + ", " + srcID + ", " + itemID + ")");
		}
		try {
			 facadeService.markItemReadMode(getUID(), 
					srcID, itemID, isRead);
		} catch (Exception e) {
			throw new WebException("Error in toggleItemReadState", e);
		}
	}
	
	/**
	 * render home page
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RenderMapping()
	public String goHome(RenderRequest request, RenderResponse response, ModelMap model) {
		model.addAttribute(STATICRESOURCESPATH, response.encodeURL(request.getContextPath()));
		ResourceURL resourceURL = response.createResourceURL();
		resourceURL.setResourceID("@@id@@");
		resourceURL.setParameter("p1", "__p1__");
		resourceURL.setParameter("p2", "__p2__");
		resourceURL.setParameter("p3", "__p3__");
		resourceURL.setParameter("p4", "__p4__");
		model.addAttribute(DYNAMICRESOURCESPATTERN, resourceURL);
		return "home";
	}

	/**
	 * Model : Context of the connected user.
	 * @return Returns the context.
	 */
	private ContextWebBean getContext() {
		String ctxId;
		ctxId = facadeService.getCurrentContextId();
		return facadeService.getContext(getUID(), ctxId);
	}
	
	/**
	 * Model : Guest mode
	 * @return true if current is the guest user.
	 */
	private boolean isGuestMode() {
		Boolean guestMode = (Boolean) getFromSession(GUEST_MODE);
		if (guestMode == null) {
			guestMode = facadeService.isGuestMode();
			setInSession(GUEST_MODE, guestMode);
		}
		return guestMode;
	}

	/*
	 * **************** Getter and Setter ****************
	 */

	/**
	 * @return the connected user UID
	 */
	private String getUID() {
		String ret = DomainTools.getCurrentUserId(authenticationService);
		return ret;
	}

	/**
	 * Store a object in session
	 * @param name of stored object
	 * @param value of stored object
	 */
	private void setInSession(final String name, Object value) {
		RequestContextHolder.getRequestAttributes().setAttribute(name, value, PortletSession.PORTLET_SCOPE);
	}

	/**
	 * @param name of stored object
	 * @return the object stored in session
	 */
	private Object getFromSession(final String name) {
		return RequestContextHolder.getRequestAttributes().getAttribute(name, PortletSession.PORTLET_SCOPE);
	}

}
