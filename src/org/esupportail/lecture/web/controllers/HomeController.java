/**
 * ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
 * For any information please refer to http://esup-helpdesk.sourceforge.net
 * You may obtain a copy of the licence at http://www.esup-portail.org/license/
 */
package org.esupportail.lecture.web.controllers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
//import org.esupportail.lecture.beans.ContextUserBean;
import org.esupportail.lecture.domain.beans.CategoryBean;
import org.esupportail.lecture.domain.beans.ContextBean;
import org.esupportail.lecture.domain.beans.SourceBean;
import org.esupportail.lecture.domain.beans.UserBean;
import org.esupportail.lecture.web.beans.CategoryWebBean;
import org.esupportail.lecture.web.beans.ContextWebBean;
import org.esupportail.lecture.web.beans.ItemWebBean;
import org.esupportail.lecture.web.beans.SourceWebBean;
import org.esupportail.lecture.domain.FacadeService;
import org.springframework.util.Assert;


/**
 * @author : Raymond 
 */
public class HomeController extends AbstractContextAwareController {
	/**
	 * Log instance 
	 */
	protected static final Log log = LogFactory.getLog(HomeController.class);
	/**
	 * Access to facade services (init by Spring)
	 */
	private FacadeService facadeService;
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
	private String categoryID;
	/**
	 *  sourceID used by t:updateActionListener
	 */
	private String sourceID;
	/**
	 *  itemID used by t:updateActionListener
	 */
	private String itemID;
	/**
	 * Access to multiple instance of channel in a one session (contexts)
	 */
	private VirtualSession virtualSession;
	//TODO no static values !!!!
	private String ContextId = "c1";
	
	
	public HomeController() {
		super();
		//instatiate a virtual session
		virtualSession = new VirtualSession();
		
	}

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
		String id = itemID;
		if (log.isDebugEnabled()) {
			log.debug("itemID = "+id);
		}
//		SourceBean src = getSelectedSourceFromCategory(getCurrentCategory());
//		if (src != null) {
//			Iterator<ItemBean> iter = src.getItems().iterator();
//			while (iter.hasNext()) {
//				ItemBean item = iter.next();
//				if (item.getId() == id) {
//					item.setRead(!item.isRead());
//				}				
//			}
//		}
		return "OK";
	}
	
	/**
	 * JSF action : select a category or a source from the tree, use categoryID and sourceID valued by t:updateActionListener
	 * @return JSF from-outcome
	 */
	public String selectElement() {
		String catID = this.categoryID;
		String srcId = this.sourceID;
		CategoryWebBean cat = getCategorieByID(catID);
		CategoryWebBean current = getSelectedCategory();
		if (srcId.endsWith("0")) {
			//toggle expanded status
			cat.setFolded(!cat.isFolded());
		} 
		else {
			//unselect current selected source
			//TODO
//			SourceRB src2 = getSelectedSourceFromCategory(cat);
//			if (src2 != null) {
//				src2.setSelected(false);
//			}
//			SourceRB src = getSourceByID(cat, srcId);
//			//select new source
//			src.setSelected(true);
		}
		// set category focused by user as selected categori in the context
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
		Assert.notNull(facadeService, 
				"property facadeService of class " + this.getClass().getName() + " can not be null");
	}
	
	/*
	 * **************** internal  method ****************
	 */
	
	/**
	 * @param id of catogory to find in the context
	 * @return the finded category
	 */
	private CategoryWebBean getCategorieByID(String id) {
		CategoryWebBean ret = null;
		ContextWebBean ctx = getContext();
		Iterator<CategoryWebBean> iter = ctx.getCategories().iterator();
		while (iter.hasNext()) {
			CategoryWebBean cat = iter.next();
			if (cat.getId() == id) {
				ret = cat;
			}
		}
		return ret;
	}
	
	//TODO : RB temporary ?
//	private SourceRB getSourceByID(CategoryRB cat, int sourceID) {
//		List<SourceRB> sources = cat.getSources();
//		if (sources != null) {
//			Iterator<SourceRB> iter = sources.iterator();
//			while (iter.hasNext()) {
//				SourceRB src = (SourceRB) iter.next();
//				if (src.getId() == sourceID) {
//					return src; 
//				}
//			}
//		}
//		return null;
//	}
	
//	public List<CategoryWebBean> getCategories() {
//		if (log.isDebugEnabled()) {
//			log.debug("In getCategories");
//		}
//		List<CategoryBean> categories = facadeService.getCategories(ContextId, userId);
//		// TODO conver CategoryBean to CategoryWebBean
//		List<CategoryWebBean> ret = new ArrayList<CategoryWebBean>();
//		if (categories != null) {
//			Iterator<CategoryBean> iter = categories.iterator();
//			while (iter.hasNext()) {
//				CategoryBean element = iter.next();
//				CategoryWebBean tmp =  new CategoryWebBean();
//				tmp.setId(element.getId());
//				tmp.setName(element.getName());
//				ret.add(tmp);
//			}
//		}
//		return ret;
//	}
	
	//TODO : RB temporary ?
//	private SourceBean getSelectedSourceFromCategory(CategoryBean cat) {
//		SourceBean ret = null;
//		if (cat != null) {
//			List<SourceBean> sources = cat.getSources();
//			if (sources != null) {
//				Iterator<SourceBean> iter = sources.iterator();
//				while (iter.hasNext()) {
//					SourceBean src = iter.next();
//					if (src.isSelected()) {
//						ret = src;
//					}
//				}				
//			}
//		}
//		return ret;
//	}
	
	/**
	 * @return the selected category from the context
	 */
	private CategoryWebBean getSelectedCategory() {
		CategoryWebBean ret = null;
		ContextWebBean ctx = getContext();
		ret = ctx.getSelectedCategory();
		return ret;
	}
	
	/**
	 * sort items list in function of itemDisplayMode
	 * @param items List to sort
	 * @return Sorted items list
	 */
	private List<ItemWebBean> sortedItems(List<ItemWebBean> items) {
		String itemDisplayMode = getItemDisplayMode();
		if (itemDisplayMode ==  null) return items;
		if (itemDisplayMode.equals("all")) {
			// nothing to do
		} else if (itemDisplayMode.equals("notRead")) {
			if (items != null){
				List<ItemWebBean> ret = new ArrayList<ItemWebBean>();
				Iterator<ItemWebBean> iter = items.iterator();
				while (iter.hasNext()) {
					ItemWebBean item = iter.next();
					if (!item.isRead()) {
						ret.add(item);
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
					ItemWebBean item = iter.next();
					if (!item.isRead()) {
						ret.add(item);
					}
				}
				// and read
				iter = items.iterator();
				while (iter.hasNext()) {
					ItemWebBean item = iter.next();
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
	
	/**
	 * @return the name of the current context
	 */
//	public String getContextName() {
//		String ret = null;
//		String contextId = facadeService.getCurrentContextId(); 
//		ContextBean contextBean = facadeService.getContext(contextId);
//		ret = contextBean.getName();
//		return ret;
//	}
	
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
	
	public List<ItemWebBean> getItems() {
		//SourceWebBean src = getSelectedSourceFromCategory(getCurrentCategory());
		//TODO use category.selectedsource
		SourceWebBean src = null;
		if (src != null) {
			List<ItemWebBean> items = src.getItems();
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
	
//	public String getCurrentCategoryName() {
//		String ret = null;
//		CategoryWebBean cat = getCurrentCategory();
//		if (cat != null) {
//			//SourceWebBean src = getSelectedSourceFromCategory(cat);
//			//TODO use category.selectedsource
//			SourceWebBean src = null;
//			if (src != null) {
//				return src.getName();
//			}
//		}
//		return ret;
//	}
	
	public void setCategoryID(String categoryID) {
		this.categoryID = categoryID;
	}
	
	public void setSourceID(String sourceID) {
		this.sourceID = sourceID;
	}
	
	public void setItemID(String itemID) {
		this.itemID = itemID;
	}

	/**
	 * To display information about the custom Context of the connected user
	 * @return Returns the context.
	 */
	public ContextWebBean getContext() {
		ContextWebBean context = (ContextWebBean) virtualSession.get("Context");
		if (context == null){
			if (log.isDebugEnabled()) 
				log.debug ("getContext() :  Context not yet loaded : loading...");
			//We evalute the context and we put it in the virtual session
			context = new ContextWebBean();
			String contextId = facadeService.getCurrentContextId(); 
			ContextBean contextBean = facadeService.getContext(contextId);
			context.setName(contextBean.getName());
			//find categories in this context
			UserBean user = facadeService.getConnectedUser();
			List<CategoryBean> categories = facadeService.getCategories(ContextId, user.getUid());
			List<CategoryWebBean> categoriesWeb = new ArrayList<CategoryWebBean>();
			if (categories != null) {
				Iterator<CategoryBean> iter = categories.iterator();
				while (iter.hasNext()) {
					CategoryBean categoryBean = iter.next();
					CategoryWebBean categoryWebBean =  new CategoryWebBean();
					categoryWebBean.setId(categoryBean.getId());
					categoryWebBean.setName(categoryBean.getName());
					//find sources in this category
					List<SourceBean> sources = facadeService.getSources(categoryBean.getId(), user.getUid());
					List<SourceWebBean> sourcesWeb = new ArrayList<SourceWebBean>();
					if (sources != null) {
						Iterator<SourceBean> iter2 = sources.iterator();
						while (iter2.hasNext()) {
							SourceBean sourceBean = iter2.next();
							SourceWebBean sourceWebBean = new SourceWebBean();
							sourceWebBean.setName(sourceBean.getName());
							sourcesWeb.add(sourceWebBean);
						}
					}
					categoryWebBean.setSources(sourcesWeb);
					categoriesWeb.add(categoryWebBean);
				}
			}
			context.setCategories(categoriesWeb);
			virtualSession.put("Context",context);
		}else{
			if (log.isDebugEnabled()) 
				log.debug ("getContext() :  Context already loaded : " + context.getId());
		}
		return context;
	}

	public void reset() {
		// TODO Auto-generated method stub
		
	}

}
