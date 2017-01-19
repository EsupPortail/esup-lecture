package org.esupportail.lecture.web.controllers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.commons.services.portal.PortalUtils;
import org.esupportail.lecture.exceptions.web.WebException;
import org.esupportail.lecture.utils.SeviceUtilLecture;
import org.esupportail.lecture.web.beans.CategoryWebBean;
import org.esupportail.lecture.web.beans.ContextWebBean;
import org.esupportail.lecture.web.beans.ItemWebBean;
import org.esupportail.lecture.web.beans.SourceWebBean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import javax.portlet.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.Predicate;

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
		if (request.getWindowState().equals(WindowState.MINIMIZED)) {
			return "mini";
		}
		ContextWebBean contexte = getContext();
		List<CategoryWebBean> listCat = contexte.getCategories();
		List<ItemWebBean> listeItemAcceuil = new ArrayList<ItemWebBean>();
		int nbrArticleNonLu=0;
		if(contexte.isViewDef()){
			//la liste des articles à afficher+nombre d'articles non lus
			listeItemAcceuil = SeviceUtilLecture.getListItemAccueil(contexte, listCat);
		}
		model = bindInitialModel(model, response, request);
		model.addAttribute("listCat", listCat);
		model.addAttribute("contexte", contexte);
		model.addAttribute("nombreArticleNonLu",nbrArticleNonLu);
		model.addAttribute("listeItemAcceuil", listeItemAcceuil);
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
	 * @param catID
	 *            Category ID
	 * @param srcID
	 *            Source ID
	 * @param itemID
	 *            Item ID
	 * @param isRead
	 *            is source read ?
	 */
	@ResourceMapping(value = "toggleItemReadState")
	public @ResponseBody void toggleItemReadState(@RequestParam(required = true, value = "p1") String catID,
			@RequestParam(required = true, value = "p2") String srcID,
			@RequestParam(required = true, value = "p3") String itemID,
			@RequestParam(required = true, value = "p4") boolean isRead,
			@RequestParam(required = true, value = "p5") boolean isPublisherMode){
		if (isGuestMode()) {
			throw new SecurityException("Try to access restricted function is guest mode");
		}
		if (LOG.isDebugEnabled()) {
			LOG.debug("toggleItemReadState(" + catID + ", " + srcID + ", " + itemID + ")");
		}
		try {
			facadeService.markItemReadMode(getUID(), srcID, itemID, isRead,isPublisherMode);
		} catch (Exception e) {
			throw new WebException("Error in toggleItemReadState", e);
		}
	}

	/**
	 * action : toggle all item from read to unread and unread to read.
	 * 
	 * @param isRead
	 */
	@ResourceMapping(value = "toggleAllItemReadState")
	public @ResponseBody ModelAndView toggleAllItemReadState(
			@RequestParam(required = true, value = "p1") boolean isRead,
			@RequestParam(required = true, value = "p2") String idCat,
			@RequestParam(required = true, value = "p3") String idSrc,
			@RequestParam(required = true, value = "p4") String filtreNonLu, Model model) {
		List<CategoryWebBean> listCatFiltre = new ArrayList<CategoryWebBean>();
		if (isGuestMode()) {
			throw new SecurityException("Try to access restricted function is guest mode");
		}
		try {
			ContextWebBean contexte = getContext();
			List<CategoryWebBean> listCat = contexte.getCategories();
			for (CategoryWebBean cat : listCat) {
				for (SourceWebBean src : cat.getSources()) {
					for (ItemWebBean item : src.getItems()) {
						if (LOG.isDebugEnabled()) {
							LOG.debug("toggleAllItemReadState(" + cat.getId() + ", " + src.getId() + ", " + item.getId()
									+ ")");
						}
						facadeService.markItemReadMode(getUID(), src.getId(), item.getId(), isRead,false);
					}
				}
			}
			listCatFiltre = SeviceUtilLecture.trierListCategorie(listCat, idCat, idSrc, filtreNonLu);
			model.addAttribute("listCat", listCatFiltre);
			model.addAttribute("isRead", isRead);
		} catch (Exception e) {
			throw new WebException("Error in toggleAllItemReadState", e);
		}
		return new ModelAndView("articleZone");
	}

	/**
	 * action : Filter items by idCat, idSrc,
	 * 
	 * @param idCat
	 * @param idSrc
	 * @param filtreNonLu
	 * @param model
	 * @return
	 */
	@ResourceMapping(value = "FilteredItem")
	public @ResponseBody ModelAndView FilteredItem(@RequestParam(required = true, value = "p1") String idCat,
			@RequestParam(required = true, value = "p2") String idSrc,
			@RequestParam(required = true, value = "p3") String filtreNonLu
			,Model model) {
		List<CategoryWebBean> listCatFiltre = new ArrayList<CategoryWebBean>();
		if (isGuestMode()) {
			throw new SecurityException("Try to access restricted function is guest mode");
		}
		try {
			ContextWebBean contexte = getContext();
			List<CategoryWebBean> listCat = contexte.getCategories();
			listCatFiltre = SeviceUtilLecture.trierListCategorie(listCat, idCat, idSrc, filtreNonLu);
			model.addAttribute("contexte", contexte);
			model.addAttribute("listCat", listCatFiltre);
		} catch (Exception e) {
			throw new WebException("Error in FilteredItem", e);
		}
		return new ModelAndView("articleZoneFiltre");
	}

	/**
	 * Model : Context of the connected user.
	 *
	 * @return Returns the context.
	 */
	private ContextWebBean getContext() {
		String ctxId;
		ctxId = facadeService.getCurrentContextId();
		boolean viewDef = facadeService.getCurrentViewDef();
		int nbreArticle = facadeService.getNombreArcticle();
		String lienVue =facadeService.getLienVue();
		return facadeService.getContext(getUID(), ctxId, viewDef, nbreArticle,lienVue);
	}
}
