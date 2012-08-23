/**
 * ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
 * For any information please refer to http://esup-helpdesk.sourceforge.net
 * You may obtain a copy of the licence at http://www.esup-portail.org/license/
 */
package org.esupportail.lecture.web.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.portlet.faces.BridgeUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.model.ItemDisplayMode;
import org.esupportail.lecture.domain.model.TreeDisplayMode;
import org.esupportail.lecture.domain.model.UserProfile;
import org.esupportail.lecture.exceptions.web.WebException;
import org.esupportail.lecture.web.beans.CategoryWebBean;
import org.esupportail.lecture.web.beans.ContextWebBean;
import org.esupportail.lecture.web.beans.ItemWebBean;
import org.esupportail.lecture.web.beans.SourceWebBean;

/**
 * @author : Raymond 
 */
public class HomeController extends TwoPanesController {
	private static final long serialVersionUID = 1L;
	/**
	 * Key used to store the context in virtual session.
	 */
	static final String CONTEXT = "context";
	/**
	 * Log instance.
	 */
	private static final Log LOG = LogFactory.getLog(HomeController.class);
	/**
	 * Display mode for item (all | unread | unreadfirst).
	 */
	private ItemDisplayMode itemDisplayMode = ItemDisplayMode.UNDEFINED;
	/**
	 *  item used by t:updateActionListener.
	 */
	private ItemWebBean ualItem;

	/**
	 * Constructor.
	 */
	public HomeController() {
		super();
	}

	/**
	 * JSF action : toogle item from read to unread and unread to read.
	 * @return JSF from-outcome
	 */
	public String toggleItemReadState() {
		if (isGuestMode()) {
			throw new SecurityException("Try to access restricted function is guest mode");
		}
		if (LOG.isDebugEnabled()) {
			LOG.debug("In toggleItemReadState");
		}
		if (LOG.isDebugEnabled()) {
			LOG.debug("itemID = " + ualItem.getId());
		}
		SourceWebBean selectedSource = getUalSource();
		try {
			UserProfile userProfile = getUserProfile();
			userProfile = getFacadeService().markItemReadMode(userProfile, 
					selectedSource.getId(), ualItem.getId(), !ualItem.isRead());
			setUserProfile(userProfile);
		} catch (Exception e) {
			throw new WebException("Error in toggleItemReadState", e);
		}
		if (ualItem.isRead()) {
			selectedSource.setUnreadItemsNumber(selectedSource.getUnreadItemsNumber() + 1);
		} else {
			if (selectedSource.getUnreadItemsNumber() > 0) {
				selectedSource.setUnreadItemsNumber(selectedSource.getUnreadItemsNumber() - 1);
			}
		}
		ualItem.setRead(!ualItem.isRead());

		return "OK";
	}

	/**
	 * JSF action : toggle Folded State of the selected category.
	 * @return JSF from-outcome
	 */
	public String toggleFoldedState() {
		if (isGuestMode()) {
			throw new SecurityException("Try to access restricted function is guest mode");
		}
		if (LOG.isDebugEnabled()) {
			LOG.debug("in toggleFoldedState");
		}
		CategoryWebBean selectedCategory = getUalCategory();
		//toggle expanded status
		selectedCategory.setFolded(!selectedCategory.isFolded());
		return "OK";
	}

	/**
	 * JSF action : Change display mode, nothing todo because itemDisplayMode is valued by JSF.
	 * @return JSF from-outcome
	 */
	public String changeItemDisplayMode() {
		if (isGuestMode()) {
			throw new SecurityException("Try to access restricted function is guest mode");
		}
		List<CategoryWebBean> categoryWebBeans = getSelectedOrAllCategories();
		if (itemDisplayMode != ItemDisplayMode.UNDEFINED) {
			try {
				for (CategoryWebBean categoryWebBean : categoryWebBeans) {
					List<SourceWebBean> sources = categoryWebBean.getSelectedOrAllSources();
					if (sources != null) {
						UserProfile userProfile = getUserProfile();
						for (SourceWebBean sourceWeb : sources) {
							userProfile = getFacadeService().markItemDisplayMode(userProfile,
									sourceWeb.getId(), itemDisplayMode);
							sourceWeb.setItemDisplayMode(itemDisplayMode);
						}
						setUserProfile(userProfile);
					}
				}
			} catch (Exception e) {
				throw new WebException("Error in changeItemDisplayMode", e);
			}			
		}
		return "OK";
	}	

	/**
	 * JSF action : mark all displayed items as read.
	 * @return JSF from-outcome
	 */
	public String markAllItemsAsRead() {
		return toogleAllItemsReadState(true);
	}	

	/**
	 * JSF action : mark all displayed items as not read.
	 * @return JSF from-outcome
	 */
	public String markAllItemsAsNotRead() {
		return toogleAllItemsReadState(false);
	}	

	/**
	 * JSF action : toogle all displayed items read state.
	 * @param read 
	 * @return JSF from-outcome
	 */
	public String toogleAllItemsReadState(boolean read) {
		if (isGuestMode()) {
			throw new SecurityException("Try to access restricted function is guest mode");
		}
		List<CategoryWebBean> categoryWebBeans = getSelectedOrAllCategories();
		try {
			for (CategoryWebBean categoryWebBean : categoryWebBeans) {
				List<SourceWebBean> sources = categoryWebBean.getSelectedOrAllSources();
				if (sources != null) {
					for (SourceWebBean sourceWeb : sources) {
						List<ItemWebBean> items = sourceWeb.getItems();
						if (items != null) {
							UserProfile userProfile = getUserProfile();
							for (ItemWebBean itemWeb : items) {
								userProfile = getFacadeService().markItemReadMode(userProfile, 
										sourceWeb.getId(), itemWeb.getId(), read);
								itemWeb.setRead(read);
							}
							setUserProfile(userProfile);
						}
						if (read) {
							sourceWeb.setUnreadItemsNumber(0);
						} else {
							sourceWeb.setUnreadItemsNumber(sourceWeb.getItemsNumber());
						}
					}
				}
			}
		} catch (Exception e) {
			throw new WebException("Error in toogleAllItemsReadState", e);
		}
		return "navigationHome";
	}	

	/*
	 * **************** Getter and Setter ****************
	 */


	/**
	 * @return desciption of current selected element (category or source)
	 */
	public String getSelectedElementDescription() {
		String ret = null;
		ContextWebBean ctx = getContext();
		ret = ctx.getDescription();
		CategoryWebBean selectedCategory = ctx.getSelectedCategory();
		if (selectedCategory != null) {
			ret = selectedCategory.getDescription();
		}
		return ret;
	}

	/**
	 * @return Display mode form items
	 */
	public ItemDisplayMode getItemDisplayMode() {
		ItemDisplayMode ret = ItemDisplayMode.UNDEFINED;
		if (LOG.isDebugEnabled()) {
			LOG.debug("getItemDisplayMode()=" + itemDisplayMode);
		}
		ContextWebBean currentContext = getContext();
		CategoryWebBean selectedCategory = currentContext.getSelectedCategory();
		if (selectedCategory != null) {
			SourceWebBean source = selectedCategory.getSelectedSource();
			if (source != null) {
				if (LOG.isDebugEnabled()) {
					LOG.debug("getItemDisplayMode() source selected = " + source.getName());
				}
				ret = source.getItemDisplayMode();
			}
		}
		return ret;
	}
	
	/**
	 * @return all available display mode for selected item
	 */
	public List<SelectItem> getAvailableItemDisplayModes() {
		List<SelectItem> ret = new ArrayList<SelectItem>();
		ContextWebBean currentContext = getContext();
		CategoryWebBean selectedCategory = currentContext.getSelectedCategory();
		if (selectedCategory == null || selectedCategory.getSelectedSource() == null) {
			ret.add(new SelectItem(ItemDisplayMode.UNDEFINED, getString("undefined")));			
		}
		ret.add(new SelectItem(ItemDisplayMode.ALL, getString("all")));
		ret.add(new SelectItem(ItemDisplayMode.UNREAD, getString("notRead")));
		ret.add(new SelectItem(ItemDisplayMode.UNREADFIRST, getString("unreadFirst")));
		return ret;
	}

	/**
	 * set the item display mode.
	 * @param itemDisplayMode
	 */
	public void setItemDisplayMode(final ItemDisplayMode itemDisplayMode) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("setItemDisplayMode(" + itemDisplayMode + ")");
		}
		this.itemDisplayMode = itemDisplayMode;
	}

	/**
	 * set item from t:updateActionListener in JSF view.
	 * @param item
	 */
	public void setUalItem(final ItemWebBean item) {
		this.ualItem = item;
	}

	/**
	 * @see org.esupportail.lecture.web.controllers.TwoPanesController#getContextName()
	 */
	@SuppressWarnings("static-access")
	@Override
	protected String getContextName() {
		return this.CONTEXT;
	}

	/**
	 * @see org.esupportail.lecture.web.controllers.TwoPanesController#getContextKey()
	 */
	@Override
	String getContextKey() {
		return CONTEXT;
	}
	
	public boolean isForcedNoTreeVisible() {
		ContextWebBean currentContext = getContext();
		return currentContext.getTreeVisible().equals(TreeDisplayMode.NEVERVISIBLE);
	}
	
	public String getMainDivStyleClass() {
		String ret = "portlet-section";
		if (isTreeVisible() && !isGuestMode()) {
			ret += " fl-container-flex";
		}
		return ret;
	}

	public String getLeftDivStyleClass() {
		String ret = "";
		if (isTreeVisible() && !isGuestMode()) {
			ret += "fl-col fl-force-left leftArea";
		}
		return ret;
	}

	public String getRightDivStyleClass() {
		String ret = "";
		if (isTreeVisible() && !isGuestMode()) {
			ret += "fl-col fl-force-right rightArea";
		}
		return ret;
	}

	public String getLeftDivStyle() {
		String ret = "";
		if (isTreeVisible() && !isGuestMode()) {
			ret += "width:" + getTreeSize() + "%";
		}
		return ret;
	}

	public String getRightDivStyle() {
		String ret = "";
		if (isTreeVisible() && !isGuestMode()) {
			int size = 98 - getTreeSize();
			ret += "width:" + size + "%";
		}
		return ret;
	}

	public String getToggleTreeVisibilityImage() {
		String ret = "";
		if (isTreeVisible()) {
			ret = "/media/XMLWithoutMenu.gif";
		} else {
			ret = "/media/menuAndXML.gif";
		}
		return ret;
	}

	public String getToggleTreeVisibilityTitle() {
		String ret = "";
		if (isTreeVisible()) {
			ret = getString("hideTree");
		} else {
			ret = getString("showTree");
		}
		return ret;
	}

	public String getNamespace() {
		String ret="servlet_";
		if (BridgeUtil.isPortletRequest()) {
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			ret = externalContext.encodeNamespace("");
		}
		return ret;
	}
	
}
