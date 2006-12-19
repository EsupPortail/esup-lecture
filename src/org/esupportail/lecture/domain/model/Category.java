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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.dao.DaoServiceStub;

/**
 * Category element : a category can be a managed or personal one.
 * @author gbouteil
 *
 */
public abstract class Category implements Element,Serializable{

	/*
	 *************************** PROPERTIES *********************************/	
	/**
	 * Log instance
	 */
	protected static final Log log = LogFactory.getLog(Category.class);
	
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
	private String profileId;
	
	private Hashtable<String,SourceProfile> sourceProfilesHash;
	
	/*
	 *************************** INIT *********************************/
	
	/**
	 * Constructor
	 */
	public Category() {
		sourceProfilesHash = new Hashtable<String,SourceProfile>();
	}
	
	/*
	 *************************** METHODS *********************************/
	
	
	protected SourceProfile getSourceProfileById(String id){
		if (log.isDebugEnabled()){
			log.debug("getSourceProfileById("+id+")");
		}
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
	synchronized public void setSourceProfilesHash(Hashtable<String,SourceProfile> sourceProfilesHash) {
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
	synchronized public void setName(String name) {
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
	synchronized public void setDescription(String description) {
		this.description = description;
	}

	
	/**
	 * Returns the id category
	 * @return id
	 * @see Category#profilId
	 */
	public String getProfileId() {
		return profileId;
	}


	/**
	 * Sets id category
	 * @param profilId
	 * @see Category#profilId
	 */
	synchronized public void setProfileId(String profileId) {
		this.profileId = profileId;
	}

}
