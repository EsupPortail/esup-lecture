/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.beans;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.esupportail.lecture.domain.model.AvailabilityMode;
import org.esupportail.lecture.domain.model.CategoryProfile;
import org.esupportail.lecture.domain.model.CoupleProfileAvailability;
import org.esupportail.lecture.domain.model.CustomCategory;
import org.esupportail.lecture.domain.model.CustomContext;
import org.esupportail.lecture.exceptions.domain.ManagedCategoryNotLoadedException;
import org.esupportail.lecture.exceptions.domain.CategoryProfileNotFoundException;
import org.esupportail.lecture.exceptions.domain.ElementDummyBeanException;

/**
 * used to store category informations.
 * @author bourges
 */
public class CategoryBean {
	
	/* 
	 *************************** PROPERTIES ******************************** */	
	
	/**
	 * id of category.
	 */
	private String id;
	/**
	 * name of category.
	 */
	private String name;
	/**
	 * description of the category.
	 */
	private String description;
	/**
	 * store if category is folded or not.
	 */
	private boolean folded = true;	
	/**
	 * type of category.
	 * "subscribed" --> The category is allowed and subscribed by the user
	 * "notSubscribed" --> The category is allowed and not yet subscribed by the user (used in edit mode)
	 * "obliged" --> The category is obliged: user can't subscribe or unsubscribe this source
	 * "owner" --> For personal categories
	 */
	private AvailabilityMode type;
	/**
	 * orderedSourceIDs store SourceID and ordering order in the CategoryProfile definition.
	 */
	private Map<String, Integer> orderedSourceIDs = Collections.synchronizedMap(new HashMap<String, Integer>());	
	
	/*
	 *************************** INIT ************************************** */	
			
	/**
	 * Default Constructor.
	 */
	public CategoryBean() {
		// empty
	}
	
	/**
	 * Constructor initializing object.
	 * @param customCategory
	 * @param customContext
	 * @throws CategoryProfileNotFoundException
	 * @throws ManagedCategoryNotLoadedException
	 */
	public CategoryBean(final CustomCategory customCategory, final CustomContext customContext) 
			throws CategoryProfileNotFoundException, ManagedCategoryNotLoadedException {
		CategoryProfile profile = customCategory.getProfile();
		this.name = profile.getName();
		this.description = profile.getDescription();
		this.id = profile.getId();
		this.folded = customContext.isCategoryFolded(id);
		this.orderedSourceIDs = profile.getOrderedSourceIDs();
	}
	
	/**
	 * constructor initializing object with CoupleProfileAvailability.
	 * @param profAv CoupleProfileAvailability
	 * @throws ManagedCategoryNotLoadedException 
	 */
	public CategoryBean(final CoupleProfileAvailability profAv) throws ManagedCategoryNotLoadedException {
		CategoryProfile profile = (CategoryProfile) profAv.getProfile(); 
		this.name = profile.getName();
		this.id = profile.getId();
		this.type = profAv.getMode();
		this.orderedSourceIDs = profile.getOrderedSourceIDs();
	}
	
	/*
	 *************************** ACCESSORS ********************************* */	
	
	/**
	 * @return description of the category
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description description of the category
	 */
	public void setDescription(final String description) {
		this.description = description;
	}
	/**
	 * @return if category is folded or not
	 */
	public boolean isFolded() {
		return folded;
	}
	/**
	 * @param folded
	 */
	public void setFolded(final boolean folded) {
		this.folded = folded;
	}
	/**
	 * @return id of category
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id
	 */
	public void setId(final String id) {
		this.id = id;
	}
	/**
	 * @return name of category
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * @return type of category
	 */
	public AvailabilityMode getType() {
		return type;
	}
	
	/**
	 * @param type
	 */
	public void setType(final AvailabilityMode type) {
		this.type = type;
	}
	
	/*
	 *************************** METHODS *********************************** */	

	/**
	 * @param sourceID - the ID of source to find
	 * @return the XML order in the source in the CategoryProfile definition
	 */
	public int getXMLOrder(final String sourceID) {
		Integer ret = orderedSourceIDs.get(sourceID);
		if (ret == null) {
			ret = Integer.MAX_VALUE;
		}
		return ret;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String string = "";
		string += "     Id = " + id.toString() + "\n";
		string += "     Name = " + name.toString() + "\n";
		string += "     Desc = "; 
		if (description != null) {
			string += description.toString() + "\n";
		}
		string += "     Type = "; 
		if (type != null) {
			string += type;
		}		
		string += "     Folded = " + folded + "\n";
		
		return string;
	}
	
}
