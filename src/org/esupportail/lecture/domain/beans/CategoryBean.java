/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.beans;

import org.esupportail.lecture.domain.model.CategoryProfile;
import org.esupportail.lecture.domain.model.CustomCategory;
import org.esupportail.lecture.domain.model.CustomContext;
import org.esupportail.lecture.exceptions.domain.CategoryNotLoadedException;
import org.esupportail.lecture.exceptions.domain.CategoryProfileNotFoundException;
import org.esupportail.lecture.exceptions.domain.DomainServiceException;

/**
 * used to store category informations
 * @author bourges
 */
public class CategoryBean {
	
	/* 
	 *************************** PROPERTIES ******************************** */	
	
	/**
	 * id of categery
	 */
	private String id;
	/**
	 * name of category
	 */
	private String name;
	/**
	 * description of the category
	 */
	private String description;
	/**
	 * store if category is folded or not
	 */
	private boolean folded = true;
	
	/*
	 *************************** INIT ************************************** */	
			
	/**
	 * Default Constructor.
	 */
	public CategoryBean(){}
	
	/**
	 * Constructor initializing object
	 * @param customCategory
	 * @param customContext
	 * @throws CategoryProfileNotFoundException
	 * @throws CategoryNotLoadedException
	 */
	public CategoryBean(CustomCategory customCategory,CustomContext customContext) throws CategoryProfileNotFoundException, CategoryNotLoadedException{
		CategoryProfile profile = customCategory.getProfile();
		
		this.name = profile.getName();
		this.description = profile.getDescription();
		this.id = profile.getId();
		this.folded = customContext.isCategoryFolded(id);
	}
	
	/*
	 *************************** ACCESSORS ********************************* */	
	
	/**
	 * @return description of the category
	 * @throws DomainServiceException 
	 */
	public String getDescription() throws DomainServiceException {
		return description;
	}
	/**
	 * @param description description of the category
	 * @throws DomainServiceException 
	 */
	public void setDescription(String description) throws DomainServiceException {
		this.description = description;
	}
	/**
	 * @return if category is folded or not
	 * @throws DomainServiceException 
	 */
	public boolean isFolded() throws DomainServiceException {
		return folded;
	}
	/**
	 * @param folded
	 * @throws DomainServiceException 
	 */
	public void setFolded(boolean folded) throws DomainServiceException {
		this.folded = folded;
	}
	/**
	 * @return id of category
	 * @throws DomainServiceException 
	 */
	public String getId() throws DomainServiceException {
		return id;
	}
	/**
	 * @param id
	 * @throws DomainServiceException 
	 */
	public void setId(String id) throws DomainServiceException {
		this.id = id;
	}
	/**
	 * @return name of category
	 * @throws DomainServiceException 
	 */
	public String getName() throws DomainServiceException {
		return name;
	}
	/**
	 * @param name
	 * @throws DomainServiceException 
	 */
	public void setName(String name) throws DomainServiceException {
		this.name = name;
	}

	/*
	 *************************** METHODS *********************************** */	

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString(){
		String string = "";
		string += "     Id = " + id.toString() + "\n";
		string += "     Name = " + name.toString() + "\n";
		string += "     Desc = "; 
		if (description != null){
			string += description.toString() + "\n";
		}
		string += "     Folded = "+ folded + "\n";
		
		return string;
	}
	
}
