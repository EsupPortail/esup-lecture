package org.esupportail.lecture.domain.model;

import java.util.*;


/**
 * 
 * @author gbouteil
 * 
 *
 */

public class Context {
/* ************************** PROPERTIES ******************************** */	
	/**
	 *  The context name 
	 */
	private String name = "";
	
	/**
	 * The context description
	 */
	private String description = "";
	
	/**
	 * The context id
	 */
	private int id;

	/**
	 * The context edit mode : not used for the moment
	 */
	private Editability edit;
	
	/**
	 * Managed category profiles available in this context.
	 * They are in a hashtable, reachable by their id
	 */
	private Map managedCategoryProfilesMap = new HashMap();

/* ************************** ACCESSORS ******************************** */	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public Editability getEdit() {
		return edit;
	}

	public void setEdit(Editability edit) {
		this.edit = edit;
	}

	public Map getManagedCategoryProfiles() {
		return managedCategoryProfilesMap;
	}
	public void setManagedCategoryProfiles(Map managedCategoryProfilesMap) {
		this.managedCategoryProfilesMap = managedCategoryProfilesMap;
	}
/* ************************** METHODS ******************************** */	

}
