/**
 * ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
 * For any information please refer to http://esup-helpdesk.sourceforge.net
 * You may obtain a copy of the licence at http://www.esup-portail.org/license/
 */
package org.esupportail.lecture.web;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.beans.ContextUserBean;
import org.esupportail.lecture.domain.model.Category;
import org.esupportail.lecture.domain.model.tmp.CategoryRB;
import org.esupportail.lecture.domain.model.tmp.Item;
import org.esupportail.lecture.domain.model.tmp.SourceRB;
import org.esupportail.lecture.domain.service.FacadeService;
import org.esupportail.lecture.domain.service.PortletService;
import org.esupportail.lecture.utils.LectureTools;
import org.esupportail.lecture.utils.exception.ErrorException;


/**
 * @author : Raymond 
 * Classe de tests
 */
/**
 * @author bourges
 *
 */
public class HomeBean {
	/**
	 * Log instance 
	 */
	protected static final Log log = LogFactory.getLog(HomeBean.class);
	/**
	 * Access to facade services (init by Spring)
	 */
	private FacadeService facadeService;
	/**
	 * Access to portlet services (init by Spring)
	 */
	private PortletService portletService;
	/**
	 * default tree size 
	 */
	private int treeSize=20;
	/**
	 * is tree is visible or not
	 */
	private boolean treeVisible=true;
	/**
	 * Display mode for item (all | unread | unreadfirst)
	 */
	private String itemDisplayMode;
	/**
	 *  categoryID used by t:updateActionListener
	 */
	private int categoryID;
	/**
	 *  sourceID used by t:updateActionListener
	 */
	private int sourceID;
	/**
	 *  itemID used by t:updateActionListener
	 */
	private int itemID;
	/**
	 * Access to multiple instance of channel in a one session (contexts)
	 */
	private VirtualSession virtualSession;
	
//	TODO : RB à virer ?
//	public void setCurrentCategoryName(String currentCategoryName) {
//	this.currentCategoryName = currentCategoryName;
//	}
	
	/*
	 * **************** Action and listener method ****************
	 */		
	/**
	 * JSF action : change treesize
	 * @param e JSF ActionEvent used to know which button is used 
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
	
	/**
	 * JSF action : toogle from tree visible to not visible and not visible to visible
	 * @return JSF from-outcome
	 */
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
	
	/**
	 * JSF action : toogle item from read to unread and unread to read
	 * @return JSF from-outcome
	 */
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
	
	/**
	 * JSF action : select a category or a source from the tree, use categoryID and sourceID valued by t:updateActionListener
	 * @return JSF from-outcome
	 */
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
	
	/**
	 * JSF action : Change display mode, nothing todo because itemDisplayMode is valued by JSF
	 * @return JSF from-outcome
	 */
	public String changeItemDisplayMode() {
		return "OK";
	}	
	
	/*
	 * **************** internal  method ****************
	 */
	
	//TODO : RB temporary ?
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
	
	//TODO : RB temporary ?
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
	
	//TODO : RB temporary ?
	public List<Category> getCategories() {
		if (log.isDebugEnabled()) {
			log.debug("In getCategories");
		}
		// Call of domain.service class
		return facadeService.getDomainService().getCategories();
	}
	
	//TODO : RB temporary ?
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
	
	//TODO : RB temporary ?
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
	
	/**
	 * sort items list in function of itemDisplayMode
	 * @param items List to sort
	 * @return Sorted items list
	 */
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
	
	public void setCategoryID(int categoryID) {
		this.categoryID = categoryID;
	}
	
	public void setSourceID(int sourceID) {
		this.sourceID = sourceID;
	}
	
	public void setItemID(int itemID) {
		this.itemID = itemID;
	}

	/**
	 * To display information about the custom Context of the connected user
	 * @return Returns the context.
	 * @throws ErrorException
	 */
	public ContextUserBean getContext() throws ErrorException {
	
		ContextUserBean context = (ContextUserBean) virtualSession.get("ContextUserBean");
		String contextId = virtualSession.getCurrentContextId();
		
		if (context == null){
			log.debug ("getContext() : ContextUserBean is null");
			context = facadeService.getDomainService().getContextUserBean(getCurrentUserId(),contextId );
			log.debug("getContext() : CurrentContextId : "+ contextId);
			virtualSession.put("ContextUserBean",context);
		}else{
			log.debug ("getContext() :  Context already loaded : "+context.getId());
		}
		return context;
	}

	/**
	 * @return Returns the virtualSession.
	 */
	public VirtualSession getVirtualSession() {
		return virtualSession;
	}

	/**
	 * @param virtualSession The virtualSession to set.
	 */
	public void setVirtualSession(VirtualSession virtualSession) {
		this.virtualSession = virtualSession;
	}

	/**
	 * @return id of the current user of the session
	 */
	private String getCurrentUserId() {
		String userId = portletService.getUserAttribute(LectureTools.USER_ID);
		return userId;
	}

	/**
	 * @return Returns the portletService.
	 */
	public PortletService getPortletService() {
		return portletService;
	}

	/**
	 * @param portletService The portletService to set.
	 */
	public void setPortletService(PortletService portletService) {
		this.portletService = portletService;
	}
	
}
