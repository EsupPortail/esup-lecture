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
import org.esupportail.lecture.exceptions.ErrorException;
import org.esupportail.lecture.exceptions.web.WebException;
import org.esupportail.lecture.web.beans.CategoryWebBean;
import org.esupportail.lecture.web.beans.ContextWebBean;
import org.esupportail.lecture.web.beans.ItemWebBean;
import org.esupportail.lecture.web.beans.SourceWebBean;
import org.springframework.util.Assert;

/**
 * @author : Raymond 
 */
public class HomeController extends twoPanesController {
	/**
	 * Log instance 
	 */
	protected static final Log log = LogFactory.getLog(HomeController.class);
	/**
	 * Display mode for item (all | unread | unreadfirst)
	 */
	private String itemDisplayMode;
	/**
	 *  item used by t:updateActionListener
	 */
	private ItemWebBean item;
	/**
	 * Key used to store the context in virtual session
	 */
	static final String CONTEXT = "context";
	/**
	 * Controller constructor
	 */
	public HomeController() {
		super();
		//instatiate a virtual session
		virtualSession = new VirtualSession();
	}

	/**
	 * JSF action : toogle item from read to unread and unread to read
	 * @return JSF from-outcome
	 */
	public String toggleItemReadState() {
		if (log.isDebugEnabled()) {
			log.debug("In toggleItemReadState");
		}
		if (log.isDebugEnabled()) {
			log.debug("itemID = "+item.getId());
		}
		CategoryWebBean selectedCategory = getContext().getSelectedCategory();
		try {
			if (item.isRead()) {
				getFacadeService().marckItemAsUnread(getUID(), selectedCategory.getSelectedSource().getId(), item.getId());
			}
			else {
				getFacadeService().marckItemAsRead(getUID(), selectedCategory.getSelectedSource().getId(), item.getId());			
			}
		} catch (Exception e) {
			throw new WebException("Error in toggleItemReadState",e);
		}
		item.setRead(!item.isRead());
		return "OK";
	}

	/**
	 * JSF action : select a category or a source from the tree, use categoryID and sourceID valued by t:updateActionListener
	 * @return JSF from-outcome
	 */
	public String selectElement() {
		if (log.isDebugEnabled()) log.debug("in selectElement");
		String catID = this.categoryId;
		String srcId = this.sourceId;
		CategoryWebBean cat = getCategorieByID(catID);
		if (srcId.equals("0")) {
			//toggle expanded status
			cat.setFolded(!cat.isFolded());
			isSourceSelected = false;
		} 
		else {
			//set source focused by user as selected source in the category
			SourceWebBean src = getSourceByID(cat, srcId);
			cat.setSelectedSource(src);
			isSourceSelected = true;
		}
		// set category focused by user as selected category in the context
		ContextWebBean ctx = getContext();
		ctx.setSelectedCategory(cat);
		return "OK";
	}

	/**
	 * JSF action : Change display mode, nothing todo because itemDisplayMode is valued by JSF
	 * @return JSF from-outcome
	 */
	public String changeItemDisplayMode() {
		return "OK";
	}	

	/**
	 * @see org.esupportail.lecture.web.controllers.AbstractContextAwareController#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		Assert.notNull(getFacadeService(), 
				"property facadeService of class " + this.getClass().getName() + " can not be null");
	}

	/*
	 * **************** internal  method ****************
	 */

	/**
	 * sort items list in function of itemDisplayMode
	 * @param items List to sort
	 * @return Sorted items list
	 */
	private List<ItemWebBean> sortedItems(List<ItemWebBean> items) {
		if (itemDisplayMode ==  null) return items;
		if (itemDisplayMode.equals("all")) {
			// nothing to do
		} else if (itemDisplayMode.equals("notRead")) {
			if (items != null){
				List<ItemWebBean> ret = new ArrayList<ItemWebBean>();
				Iterator<ItemWebBean> iter = items.iterator();
				while (iter.hasNext()) {
					ItemWebBean itemWebBean = iter.next();
					if (!itemWebBean.isRead()) {
						ret.add(itemWebBean);
					}
				}
				return ret;
			}
		} else if (itemDisplayMode.equals("unreadFirst")) {
			if (items != null){
				List<ItemWebBean> ret = new ArrayList<ItemWebBean>();
				// find unread
				Iterator<ItemWebBean> iter = items.iterator();
				while (iter.hasNext()) {
					ItemWebBean itemWebBean = iter.next();
					if (!itemWebBean.isRead()) {
						ret.add(itemWebBean);
					}
				}
				// and read
				iter = items.iterator();
				while (iter.hasNext()) {
					ItemWebBean itemWebBean = iter.next();
					if (itemWebBean.isRead()) {
						ret.add(itemWebBean);
					}
				}
				return ret;
			}

		} else {
			log.warn("Unknown itemDisplayMode value \""+itemDisplayMode+"\" in sortedItems function");
			// nothing to do
		}
		return(items);
	}

	/*
	 * **************** Getter and Setter ****************
	 */

	/**
	 * @return the list of items to dysplay for current selection (category/source) and displayMode
	 */
	public List<ItemWebBean> getItems() {
		if (log.isDebugEnabled()) log.debug("in getItems");
		List<ItemWebBean> ret = null;
		CategoryWebBean selectedCategory = getContext().getSelectedCategory();
		if (selectedCategory != null) {
			SourceWebBean selectedSource = selectedCategory.getSelectedSource();
			if (selectedSource != null) {
				//Test if list in already in selected source				
				if (selectedSource.getItems() != null) {
					ret = selectedSource.getItems();
				}
				else{
					if (log.isDebugEnabled()) log.debug("Put items in selected source");
					List<ItemBean> items;
					try {
						items = getFacadeService().getItems(getUID(), selectedSource.getId());
					} catch (Exception e) {
						throw new WebException("Error in getItems",e);
					}
					ret = new ArrayList<ItemWebBean>();
					if (items != null) {
						Iterator<ItemBean> iter = items.iterator();
						while (iter.hasNext()) {
							ItemBean itemBean = iter.next();
							ItemWebBean itemWebBean = new ItemWebBean();
							itemWebBean.setId(itemBean.getId());
							itemWebBean.setHtmlContent(itemBean.getHtmlContent());
							ret.add(itemWebBean);
						}
						//Put items in selected source
						selectedSource.setItems(ret);
					}

				}
			}			
		}
		return sortedItems(ret);
	}

	/**
	 * @return desciption of current selected element (category or source)
	 */
	public String getSelectedElementDescription(){
		String ret = null;
		ContextWebBean ctx = getContext();
		ret = ctx.getDescription();
		CategoryWebBean categoryWebBean = ctx.getSelectedCategory();
		if (categoryWebBean != null) {
			//TODO (RB) description not name
			ret = categoryWebBean.getDescription();
		}
		return ret;
	}

	/**
	 * @return Display mode form items
	 */
	public String getItemDisplayMode() {
		return itemDisplayMode;
	}

	/**
	 * set the item display mode
	 * @param itemDisplayMode
	 */
	public void setItemDisplayMode(String itemDisplayMode) {
		this.itemDisplayMode = itemDisplayMode;
	}

	/**
	 * set item from t:updateActionListener in JSF view
	 * @param item
	 */
	public void setItem(ItemWebBean item) {
		this.item = item;
	}

	/**
	 * @see org.esupportail.commons.web.controllers.Resettable#reset()
	 */
	public void reset() {
		// TODO Auto-generated method stub
	}

}
