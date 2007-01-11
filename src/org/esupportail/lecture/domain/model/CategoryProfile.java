/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.dao.DaoService;
import org.esupportail.lecture.domain.ExternalService;
import org.esupportail.lecture.exceptions.domain.CategoryNotLoadedException;
import org.esupportail.lecture.exceptions.domain.ManagedCategoryProfileNotFoundException;

/**
 * Category profile element : a category profile can be a managed or personal one.
 * @author gbouteil
 *
 */
public abstract class CategoryProfile implements ElementProfile {
	
	/* 
	 *************************** PROPERTIES *********************************/	
	/**
	 * Log instance 
	 */
	protected static final Log log = LogFactory.getLog(CategoryProfile.class); 
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
	 ************************** METHODS *********************************/	
	
	/**
	 * @return name
	 * @throws CategoryNotLoadedException 
	 * @see CategoryProfile#name
	 * @see ElementProfile#getName()
	 */
	public String getName(){
		if (log.isDebugEnabled()){
			log.debug("getName()");
		}
		String name ;
		
		try {
			name = getElement().getName();
		}catch (CategoryNotLoadedException e) {
			log.error("Category "+id+" is not loaded");
			name = this.name;
		}
		return name;
	}
	
	
	/**
	 * @return description of the category
	 * @throws CategoryNotLoadedException
	 */
	public String getDescription() throws CategoryNotLoadedException {
		if (log.isDebugEnabled()){
			log.debug("getDescription()");
		}
		Category category = (Category) getElement();
		return category.getDescription();
		
	}

	/**
	 * @return Returns the category.
	 * @throws CategoryNotLoadedException 
	 */
	public Category getElement() throws CategoryNotLoadedException {
		if (log.isDebugEnabled()){
			log.debug("getElement()");
		}
		if (category==null){
			// TODO (GB) on pourrait faire un loadCategory ou autre chose ou ailleurs ?
			String errorMsg = "Category "+id+" is not loaded in profile";
			log.error(errorMsg);
			throw new CategoryNotLoadedException(errorMsg);
		}
		return category;
	}

	protected abstract void loadCategory(ExternalService ex);
	
	/*
	 ************************** ACCESSORS *********************************/	

	

	
	
	/**
	 * @param name
	 * @see CategoryProfile#name
	 * @see ElementProfile#setName(String)
	 */
	synchronized public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return id
	 * @see CategoryProfile#id
	 * @see ElementProfile#getId()
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * @param id
	 * @see CategoryProfile#id
	 * @see ElementProfile#setId(String)
	 */
	synchronized public void setId(String id) {
		this.id = id;
	}


	/**
	 * @param category The category to set.
	 */
	synchronized public void setElement(Category category) {
		this.category = category;
	}

	

	


}
