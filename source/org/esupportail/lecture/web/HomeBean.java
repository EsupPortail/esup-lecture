/**
 * ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
 * For any information please refer to http://esup-helpdesk.sourceforge.net
 * You may obtain a copy of the licence at http://www.esup-portail.org/license/
 */
package org.esupportail.lecture.web;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.component.html.HtmlInputHidden;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.model.Category;
import org.esupportail.lecture.domain.model.tmp.CategoryRB;
import org.esupportail.lecture.domain.model.tmp.Item;
import org.esupportail.lecture.domain.model.tmp.SourceRB;
import org.esupportail.lecture.domain.service.FacadeService;

//TODO Javadoc
/**
 * @author : Raymond 
 * Classe de tests
 */
public class HomeBean {
	/**
	 * Log instance 
	 */
	protected static final Log log = LogFactory.getLog(HomeBean.class);
	private FacadeService facadeService;
	private int treeSize=20;
	private boolean treeVisible=true;
	private String itemDisplayMode;
	private String currentCategoryName;
	private int categoryID;
	private int sourceID;
	private int itemID;
	
/*
 * **************** Getter and Setter ****************
 */
	
	/**
	 * For Spring injection of Service Class
	 * @param facadeService facade og Spring Service Class
	 */
	public void setFacadeService(FacadeService facadeService) {
		this.facadeService = facadeService;
	}
	
	public boolean isTreeVisible() {
		return treeVisible;
	}

	public void setTreeVisible(boolean treeVisible) {
		this.treeVisible = treeVisible;
	}

	public int getTreeSize() {
		if (log.isDebugEnabled()) {
			log.debug("In getTreeSize :" + treeSize);
		}
		return treeSize;
	}

	public void setTreeSize(int treeSize) {
		this.treeSize = treeSize;
	}

	public List<Item> getItems() {
		SourceRB src = getSelectedSourceFromCategory(getCurrentCategory());
		if (src != null) {
			List<Item> items = src.getItems();
			return sortedItems(items);			
		}
		return null;
	}
	
	public String getItemDisplayMode() {
		return itemDisplayMode;
	}

	public void setItemDisplayMode(String itemDisplayMode) {
		this.itemDisplayMode = itemDisplayMode;
	}

	public String getCurrentCategoryName() {
		String ret = null;
		CategoryRB cat = getCurrentCategory();
		if (cat != null) {
			SourceRB src = getSelectedSourceFromCategory(cat);
			if (src != null) {
				return src.getName();				
			}
		}
		return ret;
	}

	public void setCurrentCategoryName(String currentCategoryName) {
		this.currentCategoryName = currentCategoryName;
	}

	/*
 * **************** Action and listener method ****************
 */		
	public void adjustTreeSize(ActionEvent e) {
		if (log.isDebugEnabled()) {
			log.debug("In adjustTreeSize");
		}
		FacesContext ctx = FacesContext.getCurrentInstance();
		String id = e.getComponent().getClientId(ctx);
		if (id.equals("home:leftSubview:treeSmallerButton")) {
			int treeSize = getTreeSize();
			if (treeSize > 10) {
				setTreeSize(treeSize-5);
			}
		}
		if (id.equals("home:leftSubview:treeLargerButton")) {
			int treeSize = getTreeSize();
			if (treeSize < 90) {
				setTreeSize(treeSize+5);
			}
		}
	}
	
	public String toggleTreeVisibility() {
		if (log.isDebugEnabled()) {
			log.debug("In toggleTreeVisibility");
		}
		if (isTreeVisible()) {
			setTreeVisible(false);
		} else {
			setTreeVisible(true);
		}
		return "OK";
	}

	public String toggleItemReadState() {
		if (log.isDebugEnabled()) {
			log.debug("In toggleItemReadState");
		}
		int id = itemID;
		if (log.isDebugEnabled()) {
			log.debug("itemID = "+id);
		}
		SourceRB src = getSelectedSourceFromCategory(getCurrentCategory());
		if (src != null) {
			Iterator<Item> iter = src.getItems().iterator();
			while (iter.hasNext()) {
				Item item = (Item) iter.next();
				if (item.getId() == id) {
					item.setRead(!item.isRead());
				}				
			}
		}
		return "OK";
	}

//	public void selectElement(ActionEvent e) {
//		if (log.isDebugEnabled()) {
//			log.debug("In selectElement");
//		}
//		FacesContext context = FacesContext.getCurrentInstance(); 
//		Map map = context.getExternalContext().getRequestParameterMap();
//		String catID = (String)map.get("categoryID");
//		if (log.isDebugEnabled()) {
//			log.debug("categoryID = "+catID);
//		}
//		String srcId = (String)map.get("sourceID");
//		if (log.isDebugEnabled()) {
//			log.debug("sourceID = "+srcId);
//		}
//		CategoryRB cat = getCategorieByID(Integer.parseInt(catID));
//		CategoryRB current = getCurrentCategory();
//		if (srcId.equals("")) {
//			//toggle expanded status
//			cat.setExpanded(!cat.isExpanded());
//		} 
//		else {
//			//unselect current selected source
//			SourceRB src2 = getSelectedSourceFromCategory(cat);
//			if (src2 != null) {
//				src2.setSelected(false);
//			}
//			SourceRB src = getSourceByID(cat, Integer.parseInt(srcId));
//			//select new source
//			src.setSelected(true);
//		}
//		if (current != null) current.setSelected(false);
//		cat.setSelected(true);
//	}

	public String selectElement2() {
		int catID = this.categoryID;
		int srcId = this.sourceID;
		CategoryRB cat = getCategorieByID(catID);
		CategoryRB current = getCurrentCategory();
		if (srcId == 0) {
			//toggle expanded status
			cat.setExpanded(!cat.isExpanded());
		} 
		else {
			//unselect current selected source
			SourceRB src2 = getSelectedSourceFromCategory(cat);
			if (src2 != null) {
				src2.setSelected(false);
			}
			SourceRB src = getSourceByID(cat, srcId);
			//select new source
			src.setSelected(true);
		}
		if (current != null) current.setSelected(false);
		cat.setSelected(true);
		return "OK";
	}

	public String changeItemDisplayMode() {
		return "OK";
	}	

/*
 * **************** internal  method ****************
 */
	
	private CategoryRB getCategorieByID(int id) {
		CategoryRB ret = null;
		Iterator<Category> iter = getCategories().iterator();
		while (iter.hasNext()) {
			CategoryRB cat = (CategoryRB) iter.next();
			if (cat.getId() == id) {
				ret = cat;
			}
		}
		return ret;
	}
	
	private SourceRB getSourceByID(CategoryRB cat, int sourceID) {
		List<SourceRB> sources = cat.getSources();
		if (sources != null) {
			Iterator<SourceRB> iter = sources.iterator();
			while (iter.hasNext()) {
				SourceRB src = (SourceRB) iter.next();
				if (src.getId() == sourceID) {
					return src; 
				}
			}
		}
		return null;
	}
	
	public List<Category> getCategories() {
		if (log.isDebugEnabled()) {
			log.debug("In getCategories");
		}
		// Call of domain.service class
		return facadeService.getDomainService().getCategories();
	}
	
	private SourceRB getSelectedSourceFromCategory(CategoryRB cat) {
		SourceRB ret = null;
		if (cat != null) {
			List<SourceRB> sources = cat.getSources();
			if (sources != null) {
				Iterator<SourceRB> iter = sources.iterator();
				while (iter.hasNext()) {
					SourceRB src = (SourceRB) iter.next();
					if (src.isSelected()) {
						ret = src;
					}
				}				
			}
		}
		return ret;
	}

	private List<Item> sortedItems(List<Item> items) {
		String itemDisplayMode = getItemDisplayMode();
		if (itemDisplayMode ==  null) return items;
		if (itemDisplayMode.equals("all")) {
			// nothing to do
		} else if (itemDisplayMode.equals("notRead")) {
			if (items != null){
				List<Item> ret = new ArrayList<Item>();
				Iterator<Item> iter = items.iterator();
				while (iter.hasNext()) {
					Item item = (Item)iter.next();
					if (!item.isRead()) {
						ret.add(item);
					}
				}
				return ret;
			}
		} else if (itemDisplayMode.equals("unreadFirst")) {
			if (items != null){
				List<Item> ret = new ArrayList<Item>();
				// find unread
				Iterator<Item> iter = items.iterator();
				while (iter.hasNext()) {
					Item item = (Item)iter.next();
					if (!item.isRead()) {
						ret.add(item);
					}
				}
				// and read
				iter = items.iterator();
				while (iter.hasNext()) {
					Item item = (Item)iter.next();
					if (item.isRead()) {
						ret.add(item);
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

	private CategoryRB getCurrentCategory() {
		CategoryRB ret = null;
		List<Category> cats = getCategories();
		if (cats != null) {
			Iterator<Category> iter = cats.iterator();
			while (iter.hasNext()) {
				CategoryRB cat = (CategoryRB) iter.next();
				if (cat.isSelected()) {
					ret = cat;
				}
			}			
		}
		return ret;
	}

	public void setCategoryID(int categoryID) {
		this.categoryID = categoryID;
	}

	public void setSourceID(int sourceID) {
		this.sourceID = sourceID;
	}

	public void setItemID(int itemID) {
		this.itemID = itemID;
	}

}
