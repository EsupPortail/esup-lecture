/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.esupportail.lecture.dao.DaoService;
import org.esupportail.lecture.exceptions.CategoryNotLoadedException;
import org.esupportail.lecture.exceptions.ManagedCategoryProfileNotFoundException;

/**
 * Category profile element : a category profile can be a managed or personal one.
 * @author gbouteil
 *
 */
public abstract class CategoryProfile {
	
	/* 
	 *************************** PROPERTIES *********************************/	
	/**
	 *  Category profile name
	 */
	private String name = "";
	/**
	 *  Category profile id
	 */
	private String id;

	/**
	 * Its category
	 * When its category is not null,
	 * The categroy profile is said "full"
	 */
	private Category category;
	
	
	/** 
	 * Returns a string containing category profile content : name, description and Id.
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

	/*
	 ************************** ACCESSORS *********************************/	
	/**
	 * @return name
	 * @throws CategoryNotLoadedException 
	 * @see CategoryProfile#name
	 * @see ComposantProfile#getName()
	 */
	public String getName() throws CategoryNotLoadedException {
		
		if (category == null ){
			return name;
		}else {
			return getCategory().getName();
		}
			
	}
	
	
	public String getDescription() throws CategoryNotLoadedException {
		if (category == null){
			return null;
		}else {
			return getCategory().getDescription();
		}
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

	/**
	 * @return Returns the category.
	 * @throws CategoryNotLoadedException 
	 */
	public Category getCategory() throws CategoryNotLoadedException {
		if (category==null){
			// TODO (GB) on pourrait faire un loadCategory ?
			throw new CategoryNotLoadedException("Category "+id+" is not loaded in profile");
		}
		return category;
	}

	/**
	 * @param category The category to set.
	 */
	public void setCategory(Category category) {
		this.category = category;
	}

	

	


}
