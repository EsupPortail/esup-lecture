/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.ExternalService;
import org.esupportail.lecture.exceptions.domain.CategoryNotLoadedException;
import org.esupportail.lecture.exceptions.domain.CategoryNotVisibleException;
import org.esupportail.lecture.exceptions.domain.CategoryProfileNotFoundException;


/**
 * Customizations on a CategoryProfile for a user profile 
 * @author gbouteil
 * @see CustomElement
 */
public abstract class CustomCategory implements CustomElement {

	/*
	 ************************** PROPERTIES *********************************/	
	/**
	 * Log instance
	 */
	protected static final Log log = LogFactory.getLog(CustomCategory.class);
	/**
	 * The userprofile owner 
	 */
	protected UserProfile userProfile;
	/**
	 * The Id of the categoryProfile referring by this CustomCategory
	 */
	private String profileId;	
	/**
	 * database pk
	 */
	private long customCategoryPK;
	
	/* 
	 ************************** INIT **********************************/
	
	/**
	 * Constructor
	 * @param profileId of the category profile refered by this CustomManagedSource
	 * @param user owner of this  CustomCategory
	 */
	protected CustomCategory(String profileId, UserProfile user) {
		if (log.isDebugEnabled()){
			log.debug("CustomCategory("+profileId+","+user.getUserId()+")");
		}
		this.profileId = profileId;
		this.userProfile = user;
	}

	/**
	 * default constructor
	 */
	protected CustomCategory() {
		if (log.isDebugEnabled()){
			log.debug("CustomCategory()");
		}
	}

	/* 
	 ************************** METHODS **********************************/
	

	/**
	 * Return the list of sorted customSources displayed by this customCategory
	 * @param ex access to external service 
	 * @return the list of customSource
	 * @throws CategoryProfileNotFoundException
	 * @throws CategoryNotVisibleException
	 * @throws CategoryNotLoadedException
	 */
	public abstract List<CustomSource> getSortedCustomSources(ExternalService ex) throws CategoryProfileNotFoundException, CategoryNotVisibleException, CategoryNotLoadedException;

	/**
	 * remove a CustomManegedSource displayed in this CustomCategory
	 * and also removes it from the userProfile
	 * Used to remove a subscription or an importation indifferently
	 * @param managedSourceProfile the managedSourceProfile associated to the CustomManagedSource to remove
	 */
	protected abstract void removeCustomManagedSource (ManagedSourceProfile managedSourceProfile) ;
	// TODO (GB later) removeCustomPersonalSource())
	
	/**
	 * The categoryProfile associated to this CustomCategory
	 * @return the categoryProfile 
	 * @throws CategoryProfileNotFoundException 
	 */
	public abstract CategoryProfile getProfile() throws CategoryProfileNotFoundException ;
	
	/**
	 * The used name of the categoryProfile
	 * @throws CategoryProfileNotFoundException 
	 * @see org.esupportail.lecture.domain.model.CustomElement#getName()
	 */
	public String getName() throws CategoryProfileNotFoundException  {
		if (log.isDebugEnabled()){
			log.debug("id="+profileId+" - getName()");
		}
		return getProfile().getName();
	}
	
	/* 
	 ************************** ACCESSORS **********************************/

	/**
	 * The user Profile, owner of this CustomCategory
	 * @see org.esupportail.lecture.domain.model.CustomElement#getUserProfile()
	 */
	public UserProfile getUserProfile() {
		return userProfile;
	}
	

	/**
	 * Sets userProfile
	 * @param userProfile
	 */
	private void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}
	
	/**
	 * @return id of the profile category referred by this customCategory
	 * @see org.esupportail.lecture.domain.model.CustomElement#getElementId()
	 */
	public String getElementId() {
		return profileId;
	}

	/**
	 * Sets categoryProfile Id
	 * @param profileId
	 */
	// TODO (GB --> RB) : renommer cette methode, en setElementId()
	private void setProfileId(String profileId) {
		this.profileId = profileId;
	}
	
	/**
	 * @return database pk
	 */
	public long getCustomCategoryPK() {
		return customCategoryPK;
	}

	/**
	 * @param customCategoryPK - databasePK
	 */
	public void setCustomCategoryPK(long customCategoryPK) {
		this.customCategoryPK = customCategoryPK;
	}
	

	/**
	 * @return profile id
	 */
	// TODO (GB --> RB) : retirer cette methode, meme usage que getElementId()
	private String getProfileId() {
		return profileId;
	}


}
