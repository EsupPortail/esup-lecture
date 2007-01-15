/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;

import java.util.Hashtable;
import java.io.Serializable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.exceptions.domain.SourceProfileNotFoundException;

/**
 * Category element : a category can be a managed or personal one.
 * A Category is referenced by a categoryProfile and contains SourceProfiles
 * @author gbouteil
 * @see Element
 *
 */
public abstract class Category implements Element,Serializable {

	/*
	 *************************** PROPERTIES *********************************/	

	/**
	 * Log instance
	 */
	protected static final Log log = LogFactory.getLog(Category.class);
	/**
	 * Name of the category 
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
	
	/**
	 * SourcesProfiles contained by this Category
	 */
	private Hashtable<String,SourceProfile> sourceProfilesHash = new Hashtable<String,SourceProfile>();
	

	/*
	 *************************** INIT *********************************/
	
	
	/*
	 *************************** METHODS *********************************/
	
	
	/**
	 * Returns the sourceProfile identified by id, defined in this Category
	 * @param id id oh the sourceProfile
	 * @return the sourceProfile
	 * @throws SourceProfileNotFoundException 
	 */
	protected SourceProfile getSourceProfileById(String id) throws SourceProfileNotFoundException{
		if (log.isDebugEnabled()){
			log.debug("getSourceProfileById("+id+")");
		}
		SourceProfile profile = sourceProfilesHash.get(id);
		if (profile==null){
			String errorMsg = "SourceProfile "+id+"is not found in Category "+this.profileId;
			log.error(errorMsg);
			throw new SourceProfileNotFoundException(errorMsg);
		}
		return profile;
	}




	/*
	 *************************** ACCESSORS *********************************/	
	
	
	
	/**
	 * @return Returns the sourceProfilesHash of this category
	 */
	protected Hashtable<String,SourceProfile> getSourceProfilesHash() {
	
		return sourceProfilesHash;
	}


	/**
	 * @param sourceProfilesHash to set.
	 */
	synchronized public void setSourceProfilesHash(Hashtable<String,SourceProfile> sourceProfilesHash) {
		// TODO (GB later) revoir la visibilite public qd on créera les sourcesProfiles avec des daoBeans
		this.sourceProfilesHash = sourceProfilesHash;
	}
	
	/**
	 * Returns the name of the category
	 * @return name
	 * @see Category#name
	 */
	protected String getName() {
		return name;
	}


	/**
	 * Sets the category name
	 * @param name
	 * @see Category#name
	 */
	synchronized public void setName(String name) {
		// TODO (GB later) revoir la visibilite public qd on créera les sourcesProfiles avec des daoBeans
		this.name = name;
	}
	
	/**
	 * Returns the category description
	 * @return description
	 * @see Category#description
	 */
	protected String getDescription() {
		return description;
	}

	/**
	 * Sets the category description
	 * @param description
	 * @see Category#description
	 */
	synchronized public void setDescription(String description) {
		// TODO (GB later) revoir la visibilite public qd on créera les sourcesProfiles avec des daoBeans
		this.description = description;
	}

	
	/**
	 * Returns the id of the categoryProfile accociated to this Category
	 * @return id
	 * @see Category#profileId
	 */
	protected String getProfileId() {
		return profileId;
	}


	/**
	 * Sets the id of the categoryProfile accociated to this Category
	 * @param profileId
	 * @see Category#profileId
	 */
	synchronized public void setProfileId(String profileId) {
		// TODO (GB later) revoir la visibilite public qd on créera les sourcesProfiles avec des daoBeans
		this.profileId = profileId;
	}

}
