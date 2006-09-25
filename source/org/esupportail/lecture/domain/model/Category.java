/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;



import java.io.Serializable;

/**
 * Category element : a category can be a managed or personal one.
 * @author gbouteil
 *
 */
public abstract class Category {

	/*
	 *************************** PROPERTIES *********************************/	
	
	/**
	 * Name of the category : not used
	 */
	private String name = "";

	/**
	 * Description of the category
	 */
	private String description = "";

	/**
	 * Id of the categoryProfil
	 */
	private String profilId;
	
	private Hashtable<String,SourceProfile> sourceProfilesHash;
	
	
	/*
	 *************************** METHODS *********************************/
	
	
	protected SourceProfile getSourceProfileById(String id){
		return sourceProfilesHash.get(id);
	}



	/*
	 *************************** ACCESSORS *********************************/	
	
	
	
	/**
	 * @return Returns the sourceProfilesSet.
	 */
	public Hashtable<String,SourceProfile> getSourceProfilesHash() {
		return sourceProfilesHash;
	}


	/**
	 * @param sourceProfilesSet The sourceProfilesSet to set.
	 */
	public void setSourceProfilesHash(Hashtable<String,SourceProfile> sourceProfilesHash) {
		this.sourceProfilesHash = sourceProfilesHash;
	}
	/**
	 * Returns catgeory name
	 * @return name
	 * @see Category#name
	 */
	public String getName() {
		return name;
	}


	/**
	 * Sets categroy name
	 * @param name
	 * @see Category#name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Returns category description
	 * @return description
	 * @see Category#description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets category description
	 * @param description
	 * @see Category#description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	
	/**
	 * Returns the id category
	 * @return id
	 * @see Category#profilId
	 */
	public String getProfilId() {
		return profilId;
	}


	/**
	 * Sets id category
	 * @param profilId
	 * @see Category#profilId
	 */
	public void setProfilId(String profilId) {
		this.profilId = profilId;
	}



	

}
