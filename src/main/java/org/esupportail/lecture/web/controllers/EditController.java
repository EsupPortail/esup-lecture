package org.esupportail.lecture.web.controllers;

import java.util.List;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import org.esupportail.lecture.domain.beans.CategoryBean;
import org.esupportail.lecture.domain.beans.SourceBean;
import org.esupportail.lecture.exceptions.domain.DomainServiceException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

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
    public String goHome(RenderRequest request, RenderResponse response, ModelMap model) {
        model = bindInitialModel(model, response, request);
        return "edit";
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
     * @param catID Category ID
     * @param srcID Source ID
     * @param isSubscribed is source subscribed ?
     */
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
    
}
