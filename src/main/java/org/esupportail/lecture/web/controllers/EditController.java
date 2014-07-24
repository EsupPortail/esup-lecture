package org.esupportail.lecture.web.controllers;

import org.esupportail.lecture.exceptions.domain.DomainServiceException;
import org.esupportail.lecture.web.beans.ContextWebBean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import javax.portlet.*;
import java.util.Locale;

/**
 *
 * @author bourges
 */
@Controller
@RequestMapping("EDIT")
public class EditController extends TwoPanesController {

    /**
     * render home page
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RenderMapping
    public String goEdit(RenderRequest request, RenderResponse response, ModelMap model) {
        if (request.getWindowState().equals(WindowState.MINIMIZED)) {
            return "mini";
        }
        model = bindInitialModel(model, response, request);
        return "edit";
    }

    /**
     *
     * @param request
     * @param response
     * @return
     */
    @ActionMapping(params = "action=back")
    public void goView(ActionRequest request, ActionResponse response) throws PortletModeException {
        response.setPortletMode(PortletMode.VIEW);
    }

    @ResourceMapping(value = "getEditJSON")
    public View getJSON(ResourceRequest request, ResourceResponse response) {
        MappingJacksonJsonView view = new MappingJacksonJsonView();
        view.addStaticAttribute(CONTEXT, getContext());
        view.addStaticAttribute(GUEST_MODE, isGuestMode());
        Locale locale = request.getLocale();
        view.addStaticAttribute(MESSAGES, i18nService.getStrings(locale));
        return view;
    }

    /**
     * Change subscription status of a source.
     *
     * @param catID Category ID
     * @param srcID Source ID
     * @param isSubscribed is source subscribed ?
     */
    @ResourceMapping(value = "toogleSourceSubcribtion")
    public void toogleSourceSubcribtion(
            @RequestParam(required = true, value = "p1") String catID,
            @RequestParam(required = true, value = "p2") String srcID,
            @RequestParam(required = true, value = "p3") boolean isSubscribed) {
        if (isGuestMode()) {
            throw new SecurityException("Try to access restricted function is guest mode");
        }
        try {
            if (isSubscribed) {
                facadeService.unsubscribeToSource(getUID(), catID, srcID);
            } else {
                facadeService.subscribeToSource(getUID(), catID, srcID);
            }
        } catch (DomainServiceException e) {
            throw new RuntimeException("Error in toogleSourceSubcribtion", e);
        }
    }

    /**
     * Change subscription status of a category.
     *
     * @param ctxID Source ID
     * @param catID Category ID
     * @param isSubscribed is source subscribed ?
     */
    @ResourceMapping(value = "toogleCategorySubcribtion")
    public void toogleCategorySubcribtion(
            @RequestParam(required = true, value = "p1") String ctxID,
            @RequestParam(required = true, value = "p2") String catID,
            @RequestParam(required = true, value = "p3") boolean isSubscribed) {
        if (isGuestMode()) {
            throw new SecurityException("Try to access restricted function is guest mode");
        }
        try {
            if (isSubscribed) {
                facadeService.unsubscribeToCategory(getUID(), ctxID, catID);
            }
            else {
                facadeService.subscribeToCategory(getUID(), ctxID, catID);
            }
        } catch (DomainServiceException e) {
            throw new RuntimeException("Error in toogleCategorySubcribtion", e);
        }
    }

        /**
     * Model : Context of the connected user.
     *
     * @return Returns the context.
     */
    private ContextWebBean getContext() {
        String ctxId;
        ctxId = facadeService.getCurrentContextId();
        return facadeService.getEditContext(getUID(), ctxId);
    }

}
