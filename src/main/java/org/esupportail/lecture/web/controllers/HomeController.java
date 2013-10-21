package org.esupportail.lecture.web.controllers;

import java.util.Locale;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.exceptions.web.WebException;
import org.esupportail.lecture.web.beans.ContextWebBean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

@Controller
@RequestMapping("VIEW")
public class HomeController extends TwoPanesController {

    final String TREE_VISIBLE = "treeVisible";
    final String CHANGE_ITEM_DISPLAY_MODE = "changeItemDisplayMode";
    final String AVAILABLE_ITEM_DISPLAY_MODE = "availableItemDisplayModes";
    /**
     * Log instance.
     */
    private static final Log LOG = LogFactory.getLog(HomeController.class);

    /**
     * render home page
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RenderMapping
    public String goHome(RenderRequest request, RenderResponse response, ModelMap model) {
        model = bindInitialModel(model, response, request);
        return "home";
    }

    @ResourceMapping(value = "getJSON")
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
     *
     * @param catID Category ID
     * @param srcID Source ID
     * @param itemID Item ID
     * @param isRead is source read ?
     */
    @ResourceMapping(value = "toggleItemReadState")
    public void toggleItemReadState(
            @RequestParam(required = true, value = "p1") String catID,
            @RequestParam(required = true, value = "p2") String srcID,
            @RequestParam(required = true, value = "p3") String itemID,
            @RequestParam(required = true, value = "p4") boolean isRead) {
        if (isGuestMode()) {
            throw new SecurityException("Try to access restricted function is guest mode");
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("toggleItemReadState(" + catID + ", " + srcID + ", " + itemID + ")");
        }
        try {
            facadeService.markItemReadMode(getUID(), srcID, itemID, isRead);
        } catch (Exception e) {
            throw new WebException("Error in toggleItemReadState", e);
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
        return facadeService.getContext(getUID(), ctxId);
    }
}
