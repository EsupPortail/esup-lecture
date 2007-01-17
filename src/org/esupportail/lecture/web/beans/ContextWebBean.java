package org.esupportail.lecture.web.beans;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author bourges
 * Top Object of the view
 */
public class ContextWebBean {
	/**
	 * id of context
	 */
	private String id;
	/**
	 * name of context
	 */
	private String name;
	/**
	 * selected category of context
	 */
	private CategoryWebBean selectedCategory;
	/**
	 * List of categories of context
	 */
	private List<CategoryWebBean> categories;
	/**
	 * description of category
	 */
	private String description;
	
	/**
	 * get the id of the context
	 * @return id of context
	 */
	public String getId() {
		return id;
	}
	/**
	 * set the id of the context
	 * @param id id of context
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * get the name of the context
	 * @return name of context
	 */
	public String getName() {
		return name;
	}
	/** 
	 * set the name of the context
	 * @param name name of the context
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * get the selected category of the context
	 * @return selected category
	 */
	public CategoryWebBean getSelectedCategory() {
		return selectedCategory;
	}
	/**
	 * set the selected category of the context
	 * @param selectedCategory selected category
	 */
	public void setSelectedCategory(CategoryWebBean selectedCategory) {
		this.selectedCategory = selectedCategory;
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
	public void setCategories(List<CategoryWebBean> categories) {
		Collections.sort(categories);
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
	public void setDescription(String description) {
		this.description = description;
	}

}
