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
	/**
	 * The category description
	 */
	private String description = "";
	

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

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
/* ************************** METHODS ******************************** */	

	public String toString(){
		
		String string = "";
		
		/* The category profile name */
		string += "	name : "+ name +"\n";
		
		/* The category profile description */
		string += "	description : "+ description +"\n";
	
		/* The category profile id */
		string += "	id : "+ id +"\n";
		
		return string;
	}


}
