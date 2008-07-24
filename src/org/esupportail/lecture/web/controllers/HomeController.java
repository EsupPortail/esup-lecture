/**
 * ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
 * For any information please refer to http://esup-helpdesk.sourceforge.net
 * You may obtain a copy of the licence at http://www.esup-portail.org/license/
 */
package org.esupportail.lecture.web.controllers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.beans.ItemBean;
import org.esupportail.lecture.domain.model.ItemDisplayMode;
import org.esupportail.lecture.exceptions.web.WebException;
import org.esupportail.lecture.web.beans.CategoryWebBean;
import org.esupportail.lecture.web.beans.ContextWebBean;
import org.esupportail.lecture.web.beans.ItemWebBean;
import org.esupportail.lecture.web.beans.SourceWebBean;

/**
 * @author : Raymond 
 */
public class HomeController extends TwoPanesController {
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
	private ItemDisplayMode itemDisplayMode = ItemDisplayMode.ALL;
	/**
	 *  item used by t:updateActionListener.
	 */
	private ItemWebBean item;

	/**
	 * Constructor.
	 */
	public HomeController() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * JSF action : toogle item from read to unread and unread to read.
	 * @return JSF from-outcome
	 */
	public String toggleItemReadState() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("In toggleItemReadState");
		}
		if (LOG.isDebugEnabled()) {
			LOG.debug("itemID = " + item.getId());
		}
		CategoryWebBean selectedCategory = getContext().getSelectedCategory();
		try {
			getFacadeService().marckItemReadMode(getUID(), 
					selectedCategory.getSelectedSource().getId(), item.getId(), !item.isRead());
			
		} catch (Exception e) {
			throw new WebException("Error in toggleItemReadState", e);
		}
		item.setRead(!item.isRead());
		return "OK";
	}

	/**
	 * JSF action : select a category or a source from the tree.
	 * Use categoryID and sourceID valued by t:updateActionListener.
	 * @return JSF from-outcome
	 */
	public String selectElement() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("in selectElement");
		}
		String catID = this.categoryId;
		CategoryWebBean cat = getCategorieByID(catID);
		if (getSource() == null) {
			isSourceSelected = false;
			cat.setSelectedSource(null);
		} else {
			//set source focused by user as selected source in the category
			SourceWebBean src = getSourceByID(cat, getSource().getId());
			cat.setSelectedSource(src);
			isSourceSelected = true;
			//set controller itemDisplayMode to the select source itemDisplayMode
			itemDisplayMode = src.getItemDisplayMode();
		}
		// set category focused by user as selected category in the context
		ContextWebBean ctx = getContext();
		ctx.setSelectedCategory(cat);
		return "OK";
	}

	/**
	 * JSF action : toggle Folded State of the current category.
	 * @return JSF from-outcome
	 */
	public String toggleFoldedState() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("in toggleFoldedState");
		}
		String catID = this.categoryId;
		CategoryWebBean cat = getCategorieByID(catID);
		//toggle expanded status
		cat.setFolded(!cat.isFolded());
		return "OK";
	}

	/**
	 * JSF action : Change display mode, nothing todo because itemDisplayMode is valued by JSF.
	 * @return JSF from-outcome
	 */
	public String changeItemDisplayMode() {
		CategoryWebBean selectedCategory = getContext().getSelectedCategory();
		if (selectedCategory != null) {
			SourceWebBean selectedSource = selectedCategory.getSelectedSource();
			if (selectedSource != null) {
				try {
					getFacadeService().marckItemDisplayMode(getUID(),
						selectedSource.getId(), itemDisplayMode);
					selectedSource.setItemDisplayMode(itemDisplayMode);
				} catch (Exception e) {
					throw new WebException("Error in changeItemDisplayMode", e);
				}
			}
		}
		return "OK";
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
		CategoryWebBean categoryWebBean = ctx.getSelectedCategory();
		if (categoryWebBean != null) {
			ret = categoryWebBean.getDescription();
		}
		return ret;
	}

	/**
	 * @return Display mode form items
	 */
	public ItemDisplayMode getItemDisplayMode() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("getItemDisplayMode()=" + itemDisplayMode);
		}
		return itemDisplayMode;
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
	public void setItem(final ItemWebBean item) {
		this.item = item;
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
	 * @return ALL value from ItemDisplayMode enumeration for JSF view
	 */
	public ItemDisplayMode getAll() {
		return ItemDisplayMode.ALL;
	}

	/**
	 * @return UNREAD value from ItemDisplayMode enumeration for JSF view
	 */
	public ItemDisplayMode getUnread() {
		return ItemDisplayMode.UNREAD;
	}

	/**
	 * @return UNREADFIRST value from ItemDisplayMode enumeration for JSF view
	 */
	public ItemDisplayMode getUnreadfirt() {
		return ItemDisplayMode.UNREADFIRST;
	}

	/**
	 * @see org.esupportail.lecture.web.controllers.TwoPanesController#getContextKey()
	 */
	@Override
	String getContextKey() {
		return CONTEXT;
	}

}
