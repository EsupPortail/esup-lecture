package org.esupportail.lecture.domain.model;

/**
 * Category profile element : a category profile can be a managed or personal one.
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
	private String id;

	


/* ************************** METHODS ******************************** */	

	/** 
	 * Returns a string containing category profile content : name, description and Id.
	 * @see java.lang.Object#toString()
	 */
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		
		String string = "";
		
		/* The category profile name */
		string += "	name : "+ name +"\n";
		
	
		/* The category profile id */
		string += "	id : "+ id +"\n";
		
		return string;
	}

/* ************************** ACCESSORS ******************************** */	
	/**
	 * @return name
	 * @see CategoryProfile#name
	 * @see ComposantProfile#getName()
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @param name
	 * @see CategoryProfile#name
	 * @see ComposantProfile#setName(String)
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return id
	 * @see CategoryProfile#id
	 * @see ComposantProfile#getId()
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * @param id
	 * @see CategoryProfile#id
	 * @see ComposantProfile#setId(String)
	 */
	public void setId(String id) {
		this.id = id;
	}


}
