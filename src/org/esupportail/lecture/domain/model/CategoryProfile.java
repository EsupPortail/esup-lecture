/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.ExternalService;
import org.esupportail.lecture.exceptions.domain.CategoryNotLoadedException;
import org.esupportail.lecture.exceptions.domain.CategoryTimeOutException;
import org.esupportail.lecture.exceptions.domain.InfoDomainException;
import org.esupportail.lecture.exceptions.domain.SourceProfileNotFoundException;

/**
 * Category profile element : a category profile can be a managed or personal one.
 * A Category profile references a category and is displayed in a context
 * @author gbouteil
 * @see ElementProfile
 */
public abstract class CategoryProfile implements ElementProfile {
	
	/* 
	 *************************** PROPERTIES *********************************/	

	/**
	 * Log instance. 
	 */
	protected static final Log LOG = LogFactory.getLog(CategoryProfile.class); 
	/**
	 *  Category profile name.
	 *  It is the category name that is used.
	 *  This name is used only when category is not loaded
	 */
	private String name = "";
	/**
	 *  Category profile id.
	 */
	private String id;
	/**
	 * Category described by this CategoryProfile.
	 */
	private Category category;
	
	/*
	 ************************** METHODS *********************************/	
	
	/**
	 * Load the category referenced by this CategoryProfile.
	 * @param ex
	 * @throws CategoryTimeOutException 
	 */
	protected abstract void loadCategory(ExternalService ex)  throws InfoDomainException;
	
	/**
	 * Return the name of the referenced category. When the category is not loaded, it returns
	 * the name of this CategoryProfile.
	 * @return name
	 */
	public String getName(){
		if (LOG.isDebugEnabled()){
			LOG.debug("id="+id+" - getName()");
		}
		String currentName ;
		
		try {
			currentName = getElement().getName();
		}catch (CategoryNotLoadedException e) {
			LOG.error("Category "+id+" is not loaded");
			currentName = this.name;
		}
		return currentName;
	}
	
	/**
	 * @return description of the category referenced by this CategoryProfile
	 * @throws CategoryNotLoadedException
	 */
	public String getDescription() throws CategoryNotLoadedException {
		if (LOG.isDebugEnabled()){
			LOG.debug("id="+id+" - getDescription()");
		}
		return getElement().getDescription();
		
	}

	/**
	 * @return Returns the category referenced by this CategoryProfile
	 * @throws CategoryNotLoadedException 
	 */
	public Category getElement() throws CategoryNotLoadedException {
		if (LOG.isDebugEnabled()){
			LOG.debug("id="+id+" - getElement()");
		}
		if (category==null){
			String errorMsg = "Category "+id+" is not loaded in profile";
			LOG.error(errorMsg);
			throw new CategoryNotLoadedException(errorMsg);
		}
		return category;
	}
	
	/**
	 * Returns the sourceProfile identified by id, accessible by CategoryProfile
	 * (Defined in Category referred by this categoryProfile
	 * @param sourceProfileId id of the sourceProfile
	 * @return the sourceProfile
	 * @throws CategoryNotLoadedException 
	 * @throws SourceProfileNotFoundException 
	 */
	protected abstract SourceProfile getSourceProfileById(String sourceProfileId) throws CategoryNotLoadedException, SourceProfileNotFoundException ;

	
	
	
	
	/** 
	 * @see java.lang.Object#toString()
	 */
	@Override
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
	 * @param name
	 * @see CategoryProfile#name
	 * @see ElementProfile#setName(String)
	 */
	public void setName(final String name) {
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
	public void setId(final String id) {
		this.id = id;
	}


	/**
	 * @param category The category to set.
	 */
	public void setElement(final Category category) {
		this.category = category;
	}

}
