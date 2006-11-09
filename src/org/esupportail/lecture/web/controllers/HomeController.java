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
import org.esupportail.lecture.domain.beans.CategoryBean;
import org.esupportail.lecture.domain.beans.ContextBean;
import org.esupportail.lecture.domain.beans.ItemBean;
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
	private String categoryId;
	/**
	 *  sourceID used by t:updateActionListener
	 */
	private String sourceId;
	/**
	 *  item used by t:updateActionListener
	 */
	private ItemWebBean item;
	/**
	 * Access to multiple instance of channel in a one session (contexts)
	 */
	private VirtualSession virtualSession;
	/**
	 * ContextId not yet find in external context
	 */
	private String ContextId;
	/**
	 * Key used to store the context in virtual session
	 */
	static final String CONTEXT = "context";
	/**
	 * UserBean of the connected user
	 */
	private UserBean user;	
	
	/**
	 * Controller constructor
	 */
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
			if (treeSize > 10) {
				treeSize-=5;
			}
		}
		if (id.equals("home:leftSubview:treeLargerButton")) {
			if (treeSize < 90) {
				treeSize+=5;
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
		if (log.isDebugEnabled()) {
			log.debug("itemID = "+item.getId());
		}
		CategoryWebBean selectedCategory = getContext().getSelectedCategory();
		if (item.isRead()) {
			facadeService.marckItemasUnread(user.getUid(), item.getId(), selectedCategory.getId());
		}
		else {
			facadeService.marckItemasRead(user.getUid(), item.getId(), selectedCategory.getId());			
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
		} 
		else {
			//set source focused by user as selected source in the category
			SourceWebBean src = getSourceByID(cat, srcId);
			cat.setSelectedSource(src);
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
		Assert.notNull(facadeService, 
				"property facadeService of class " + this.getClass().getName() + " can not be null");
		//init the user
		user = facadeService.getConnectedUser();
		//init the contextId
		ContextId = facadeService.getCurrentContextId();
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
	
	/**
	 * @param cat where to find source
	 * @param id of source to find
	 * @return the finded source
	 */
	private SourceWebBean getSourceByID(CategoryWebBean cat, String id) {
		SourceWebBean ret = null;
		Iterator<SourceWebBean> iter = cat.getSources().iterator();
		while (iter.hasNext()) {
			SourceWebBean src = iter.next();
			if (src.getId() == id) {
				ret = src;
			}
		}
		return ret;
	}
	
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
	 * For Spring injection of Service Class
	 * @param facadeService facade og Spring Service Class
	 */
	public void setFacadeService(FacadeService facadeService) {
		this.facadeService = facadeService;
	}
	
	/**
	 * @return if tree is visible or not
	 */
	public boolean isTreeVisible() {
		return treeVisible;
	}
	
	/**
	 * set tree visibility to yes or no
	 * @param treeVisible boolean value for tree visibility
	 */
	public void setTreeVisible(boolean treeVisible) {
		this.treeVisible = treeVisible;
	}
	
	/**
	 * @return the size of left tree
	 */
	public int getTreeSize() {
		return treeSize;
	}
	
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
					List<ItemBean> items = facadeService.getItems(selectedSource.getId(), user.getUid());
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
	 * set categoryId from t:updateActionListener in JSF view
	 * @param categoryId
	 */
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	
	/**
	 * set sourceId from t:updateActionListener in JSF view
	 * @param sourceId
	 */
	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}
	
	/**
	 * set item from t:updateActionListener in JSF view
	 * @param item
	 */
	public void setItem(ItemWebBean item) {
		this.item = item;
	}

	/**
	 * To display information about the custom Context of the connected user
	 * @return Returns the context.
	 */
	public ContextWebBean getContext() {
		ContextWebBean context = (ContextWebBean) virtualSession.get(CONTEXT);
		if (context == null){
			if (log.isDebugEnabled()) 
				log.debug ("getContext() :  Context not yet loaded : loading...");
			//We evalute the context and we put it in the virtual session
			context = new ContextWebBean();
			String contextId = facadeService.getCurrentContextId(); 
			ContextBean contextBean = facadeService.getContext(contextId);
			context.setName(contextBean.getName());
			context.setId(contextBean.getId());
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
							sourceWebBean.setId(sourceBean.getId());
							sourceWebBean.setName(sourceBean.getName());
							sourcesWeb.add(sourceWebBean);
						}
					}
					categoryWebBean.setSources(sourcesWeb);
					categoriesWeb.add(categoryWebBean);
				}
			}
			context.setCategories(categoriesWeb);
			virtualSession.put(CONTEXT,context);
		}
		return context;
	}

	/**
	 * @see org.esupportail.commons.web.controllers.Resettable#reset()
	 */
	public void reset() {
		// TODO Auto-generated method stub
		
	}

}
