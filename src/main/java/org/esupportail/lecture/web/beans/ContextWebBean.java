package org.esupportail.lecture.web.beans;

import java.util.Collections;
import java.util.List;

import org.esupportail.lecture.domain.model.ItemDisplayMode;
import org.esupportail.lecture.domain.model.TreeDisplayMode;

/**
 * @author bourges
 * Top Object of the view
 */
public class ContextWebBean {
	/**
	 * id of context.
	 */
	private String id;
	/**
	 * name of context.
	 */
	private String name;
	/**
	 * selected category of context.
	 */
	private CategoryWebBean selectedCategory;
	/**
	 * List of categories of context.
	 */
	private List<CategoryWebBean> categories;
	/**
	 * description of category.
	 */
	private String description;
	/**
	 * size of tree window.
	 */
	private int treeSize;
	
	/**
	* visibility of the tree panel
	*/
	private TreeDisplayMode treeVisible;
	
	/**
	 * if the current page is the first one
	 */
	private boolean viewDef;
	/**
	 * the number of items to display
	 */
	private int nombreArticle;
	
	/**
	 * link to see all items
	 */
	private String lienVue;
	
	private int nbrUnreadItem;

	private int nbrUnreadItemHighlight;
	
	private boolean modePublisher;

	private boolean userCanMarkRead;
	private ItemDisplayMode itemDisplayMode = ItemDisplayMode.ALL;
	private Boolean isContextUnreadMode;
	/**
	 * get the id of the context.
	 * @return id of context
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * set the id of the context.
	 * @param id id of context
	 */
	public void setId(final String id) {
		this.id = id;
	}
	
	/**
	 * get the name of the context.
	 * @return name of context
	 */
	public String getName() {
		return name;
	}
	
	/** 
	 * set the name of the context.
	 * @param name name of the context
	 */
	public void setName(final String name) {
		this.name = name;
	}
	
	/**
	 * get the selected category of the context.
	 * @return selected category
	 */
	public CategoryWebBean getSelectedCategory() {
		return selectedCategory;
	}
	
	/**
	 * set the selected category of the context.
	 * @param selectedCategory selected category
	 */
	public void setSelectedCategory(final CategoryWebBean selectedCategory) {
		this.selectedCategory = selectedCategory;
	}
	
	/**
	 * set the selected category of the context.
	 * @param catId - Id of category to assign as selected category
	 */
	public void setSelectedCategoryById(final String catId) {
		for (CategoryWebBean cat : categories) {
			if (cat.getId().equals(catId)) {
				this.selectedCategory = cat;
			}
		}
	}
	/**
	 * @return list of categories
	 */
	public List<CategoryWebBean> getCategories() {
		return categories;
	}
	
	/**
	 * find a category from context
	 * @param catID
	 * @return
	 */
	public CategoryWebBean getCategory(String catID) {
		if (catID != null) {
			List<CategoryWebBean> categoryWebBeans = getCategories();
			for (CategoryWebBean categoryWebBean : categoryWebBeans) {
				if (categoryWebBean.getId().equals(catID)) {
					return categoryWebBean;
				}
			}			
		}
		return null;
	}
	
	/**
	 * @param categories
	 */
	public void setCategories(final List<CategoryWebBean> categories) {
		if (categories != null) {
			Collections.sort(categories);			
		}
		this.categories = categories;
	}
	/**
	 * @return descrition
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description
	 */
	public void setDescription(final String description) {
		this.description = description;
	}
	/**
	 * @return the treeSize
	 */
	public int getTreeSize() {
		return treeSize;
	}
	/**
	 * @param treeSize the treeSize to set
	 */
	public void setTreeSize(final int treeSize) {
		this.treeSize = treeSize;
	}

	/**
	 * @return true if this context have a not null selectedCatogory.
	 */
	public boolean isWithSelectedCategory() {
		boolean ret = false;
		if (selectedCategory != null) {
			ret = true;
		} 
		return ret;
	}

	/**
	 * @return is tree is visible or not
	 */
	public boolean isTreeVisible() {
		return getTreeVisibleState().equals(TreeDisplayMode.VISIBLE);
	}
	
	/**
	 * @return is tree is mark as never visible or not
	 */
	public boolean isTreeNeverVisible() {
		return getTreeVisibleState().equals(TreeDisplayMode.NEVERVISIBLE);
	}
	
	/**
	 * @return treeVisible
	 */
	public TreeDisplayMode getTreeVisibleState() {
		return treeVisible;
	}

	/**
	 * @param treeVisible the treeVisible to set
	 */
	public void setTreeVisible(TreeDisplayMode treeVisible) {
		this.treeVisible = treeVisible;
	}

	public boolean isViewDef() {
		return viewDef;
	}

	public void setViewDef(boolean viewDef) {
		this.viewDef = viewDef;
	}

	public int getNombreArticle() {
		return nombreArticle;
	}

	public void setNombreArticle(int nombreArticle) {
		this.nombreArticle = nombreArticle;
	}

	public String getLienVue() {
		return lienVue;
	}

	public void setLienVue(String lienVue) {
		this.lienVue = lienVue;
	}

	public int getNbrUnreadItem() {
		return nbrUnreadItem;
	}

	public void setNbrUnreadItem(int nbrUnreadItem) {
		this.nbrUnreadItem = nbrUnreadItem;
	}

	public int getNbrUnreadItemHighlight() {
		return nbrUnreadItemHighlight;
	}

	public void setNbrUnreadItemHighlight(int nbrUnreadItemHighlight) {
		this.nbrUnreadItemHighlight = nbrUnreadItemHighlight;
	}

	public boolean isModePublisher() {
		return modePublisher;
	}

	public void setModePublisher(boolean modePublisher) {
		this.modePublisher = modePublisher;
	}

	public boolean isUserCanMarkRead() {
		return userCanMarkRead;
	}

	public void setUserCanMarkRead(boolean userCanMarkRead) {
		this.userCanMarkRead = userCanMarkRead;
	}

	public ItemDisplayMode getItemDisplayMode() {
		return itemDisplayMode;
	}

	public void setItemDisplayMode(ItemDisplayMode itemDisplayMode) {
		this.itemDisplayMode = itemDisplayMode;
	}

	public Boolean getIsContextUnreadMode() {
		return isContextUnreadMode;
	}

	public void setIsContextUnreadMode(Boolean isContextUnreadMode) {
		this.isContextUnreadMode = isContextUnreadMode;
	}
}
