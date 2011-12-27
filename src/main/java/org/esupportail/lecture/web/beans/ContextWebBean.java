package org.esupportail.lecture.web.beans;

import java.util.Collections;
import java.util.List;

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
	private boolean treeVisible;

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
//		if (selectedCategory == null && categories.size() > 0) {
//			selectedCategory = categories.get(0);
//		}
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
	 * @return treeVisible
	 */
	public boolean isTreeVisible() {
		return treeVisible;
	}

	/**
	 * @param treeVisible the treeVisible to set
	 */
	public void setTreeVisible(boolean treeVisible) {
		this.treeVisible = treeVisible;
	}
	
}
