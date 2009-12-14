/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.exceptions.domain.ManagedCategoryNotLoadedException;
import org.esupportail.lecture.exceptions.domain.SourceProfileNotFoundException;

/**
 * Category profile element : a category profile can be a managed or personal one.
 * A Category profile references a category and is displayed in a context
 * @author gbouteil
 * @see ElementProfile
 */
/**
 * @author gbouteil
 *
 */
public abstract class CategoryProfile implements ElementProfile {
	
	/* 
	 *************************** PROPERTIES *********************************/	

	/**
	 * Log instance. 
	 */
	protected static final Log LOG = LogFactory.getLog(CategoryProfile.class); 

	/**
	 * Category described by this CategoryProfile.
	 */
	private Category element;
	/**
	 *  Category profile id.
	 */
	private String id;
	/**
	 *  Category profile name.
	 */
	private String name = "";
	/**
	 *  Category profile description.
	 */
	private String description = "";	

	
	
	/*
	 ************************** INITIALIZATION ******************************** */	
	
	/**
	 * Constructor. 
	 */
	public CategoryProfile() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("CategoryProfile()");
		}
	}
	
	/*
	 ************************** METHODS *********************************/	
	
	/**
	 * @return Returns the category referenced by this CategoryProfile
	 * @throws ManagedCategoryNotLoadedException 
	 */
	protected Category getElement() throws ManagedCategoryNotLoadedException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("id=" + id + " - getElement()");
		}
		return element;
	}
	
	/**
	 * @return the Map of Ordered Source IDs
	 * @throws ManagedCategoryNotLoadedException 
	 */
	public Map<String, Integer> getOrderedSourceIDs() throws ManagedCategoryNotLoadedException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("id=" + id + " - getName()");
		}
		return getElement().getOrderedSourceIDs();
	}
	
	
	/**
	 * Returns the sourceProfile identified by id, accessible by CategoryProfile.
	 * (Defined in Category referred by this categoryProfile)
	 * @param profileId id of the sourceProfile
	 * @return the sourceProfile
	 * @throws SourceProfileNotFoundException 
	 * @throws ManagedCategoryNotLoadedException 
	 */
	protected SourceProfile getSourceProfileById(final String profileId) 
	throws SourceProfileNotFoundException, ManagedCategoryNotLoadedException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("id=" + this.getId() + " - getSourceProfileById(" + id + ")");
		}
		SourceProfile msp = getElement().getSourceProfileById(profileId);
		return msp;

	}
	
	/** 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()  {
		
		String string = "";
		
		/* The category profile name */
		string += "	name : " + getName() + "\n";
			
		/* The category profile id */
		string += "	id : " + getId() + "\n";
		
		return string;
	}
	
	/*
	 ************************** ACCESSORS *********************************/	
	
	/**
	 * Return the name of the category profile. 
	 * @return name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name to the category profile.
	 * @param name
	 */
	public void setName(final String name) {
		this.name = name;
	}
	
	/**
	 * @return description of this CategoryProfile
	 */
	public String getDescription() {
		return description;
		
	}
	
	/**
	 * Sets the description to this category profile.
	 * @param desc
	 */
	protected void setDescription(final String desc) {
		this.description = desc;
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
	 */
	public void setId(final String id) {
		this.id = id;
	}


	/**
	 * @param category The category to set.
	 */
	protected void setElement(final Category category) {
		this.element = category;
	}

	/**
	 * @return false
	 */
	public boolean isUserCanMarkRead() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Make the (long)id of this categoryProfile (context:type:interneId).
	 * @param parentId 
	 * @param type = p | m  (personal or managed)
	 * @param simpleId = interneId for a personal | fileId for a managed	  
	 * @return ID made from the three parameters
	 */
	protected String makeId(final String parentId, final String type, final String simpleId) {
	   	if (LOG.isDebugEnabled()) {
    		LOG.debug("id=" + this.id + " - makeId(" + type + "," + parentId + "," + simpleId + ")");
    	}
		// id = type + ":" + parentId + ":" + simpleId;
		id = parentId + ":" + type + ":" + simpleId;
		return id;
	}
		
}
