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
	 * @throws ElementDummyBeanException 
	 */
	public String getDescription() throws ElementDummyBeanException {
		return description;
	}
	/**
	 * @param description description of the category
	 * @throws ElementDummyBeanException 
	 */
	public void setDescription(final String description) throws ElementDummyBeanException {
		this.description = description;
	}
	/**
	 * @return if category is folded or not
	 * @throws ElementDummyBeanException 
	 */
	public boolean isFolded() throws ElementDummyBeanException {
		return folded;
	}
	/**
	 * @param folded
	 * @throws ElementDummyBeanException 
	 */
	public void setFolded(final boolean folded) throws ElementDummyBeanException {
		this.folded = folded;
	}
	/**
	 * @return id of category
	 * @throws ElementDummyBeanException 
	 */
	public String getId() throws ElementDummyBeanException {
		return id;
	}
	/**
	 * @param id
	 * @throws ElementDummyBeanException 
	 */
	public void setId(final String id) throws ElementDummyBeanException {
		this.id = id;
	}
	/**
	 * @return name of category
	 * @throws ElementDummyBeanException 
	 */
	public String getName() throws ElementDummyBeanException {
		return name;
	}
	/**
	 * @param name
	 * @throws ElementDummyBeanException 
	 */
	public void setName(final String name) throws ElementDummyBeanException {
		this.name = name;
	}

	/**
	 * @return type of category
	 * @throws ElementDummyBeanException 
	 */
	public AvailabilityMode getType()  throws ElementDummyBeanException {
		return type;
	}
	
	/**
	 * @param type
	 * @throws ElementDummyBeanException 
	 */
	public void setType(final AvailabilityMode type) throws ElementDummyBeanException {
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
