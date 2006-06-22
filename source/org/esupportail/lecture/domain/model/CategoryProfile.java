package org.esupportail.lecture.domain.model;

/**
 * Abstract class Category profile
 * @author gbouteil
 *
 */

public abstract class CategoryProfile {
/* ************************** PROPERTIES ******************************** */	
	/**
	 *  Category profile name
	 */
	private String name = "";
	/**
	 *  Category profile id
	 */
	private int id;

/* ************************** ACCESSORS ******************************** */	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

/* ************************** METHODS ******************************** */	
}
