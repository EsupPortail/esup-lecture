/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.esupportail.lecture.web.controllers;

import javax.annotation.Resource;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceURL;
import org.esupportail.commons.services.authentication.AuthenticationService;
import org.esupportail.lecture.domain.DomainTools;
import org.esupportail.lecture.domain.FacadeService;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.RequestContextHolder;

/**
 *
 * @author bourges
 */
class TwoPanesController {
    final String STATICRESOURCESPATH = "resourcesPath";
    final String DYNAMICRESOURCESPATTERN = "dynamicResourcesPattern";
    final String GUEST_MODE = "guestMode";

    /**
     * Access to facade services (init by Spring).
     */
    @Resource(name = "facadeService")
    protected FacadeService facadeService;
    /**
     * The authentication service.
     */
    @Resource(name = "authenticationService")
    protected AuthenticationService authenticationService;

    protected ModelMap bindInitialModel(final ModelMap model, final RenderResponse response, final RenderRequest request) {
        model.addAttribute(STATICRESOURCESPATH, response.encodeURL(request.getContextPath()));
        ResourceURL resourceURL = response.createResourceURL();
        resourceURL.setResourceID("@@id@@");
        resourceURL.setParameter("p1", "__p1__");
        resourceURL.setParameter("p2", "__p2__");
        resourceURL.setParameter("p3", "__p3__");
        resourceURL.setParameter("p4", "__p4__");
        model.addAttribute(DYNAMICRESOURCESPATTERN, resourceURL);
        return model;
    }

    /**
     * Model : Guest mode
     *
     * @return true if current is the guest user.
     */
    protected boolean isGuestMode() {
        Boolean guestMode = (Boolean) getFromSession(GUEST_MODE);
        if (guestMode == null) {
            guestMode = facadeService.isGuestMode();
            setInSession(GUEST_MODE, guestMode);
        }
        return guestMode;
    }

    /**
     * @param name of stored object
     * @return the object stored in session
     */
    protected Object getFromSession(final String name) {
        return RequestContextHolder.getRequestAttributes().getAttribute(name, PortletSession.PORTLET_SCOPE);
    }

    /**
     * Store a object in session
     *
     * @param name of stored object
     * @param value of stored object
     */
    protected void setInSession(final String name, Object value) {
        RequestContextHolder.getRequestAttributes().setAttribute(name, value, PortletSession.PORTLET_SCOPE);
    }

    /*
     * **************** Getter and Setter ****************
     */
    /**
     * @return the connected user UID
     */
    protected String getUID() {
        String ret = DomainTools.getCurrentUserId(authenticationService);
        return ret;
    }
    
}
