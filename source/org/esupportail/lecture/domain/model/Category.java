package org.esupportail.lecture.domain.model;

public abstract class Category {

/* ************************** PROPERTIES ******************************** */	
	
	/**
	 * Name of the category
	 */
	private String name = "";

	/**
	 * Description of the category
	 */
	private String description = "";
	/**
	 * Id of the category
	 */
	private int id;
	
/* ************************** METHODS ******************************** */
	
	
/* ************************** ACCESSORS ******************************** */	
	/**
	 * Returns catgeory name
	 * @return name
	 * @see Category#name
	 */
	protected String getName() {
		return name;
	}


	/**
	 * Sets categroy name
	 * @param name
	 * @see Category#name
	 */
	protected void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Returns category description
	 * @return description
	 * @see Category#description
	 */
	protected String getDescription() {
		return description;
	}

	/**
	 * Sets category description
	 * @param description
	 * @see Category#description
	 */
	protected void setDescription(String description) {
		this.description = description;
	}

	
	/**
	 * Returns the id category
	 * @return id
	 * @see Category#id
	 */
	protected int getId() {
		return id;
	}


	/**
	 * Sets id category
	 * @param id
	 * @see Category#id
	 */
	protected void setId(int id) {
		this.id = id;
	}

}
