package org.esupportail.lecture.beans;

/**
 * Bean to display a category according to a user profile
 * @author gbouteil
 *
 */
public class CategoryUserBean {
	
	/*
	 ************************ PROPERTIES ******************************** */	

	/**
	 * Name of the category
	 */
	private String name;
	
	/**
	 *  Description of the category
	 */
	private String description;

	
	/*
	 ************************ ACCESSORS ******************************** */	

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
}
