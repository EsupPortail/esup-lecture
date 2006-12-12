package org.esupportail.lecture.domain.model;

import java.util.List;

import org.esupportail.lecture.domain.ExternalService;
import org.esupportail.lecture.exceptions.CategoryNotLoadedException;
import org.esupportail.lecture.exceptions.CategoryProfileNotFoundException;
import org.esupportail.lecture.exceptions.ElementNotLoadedException;


/**
 * Customizations on a Category for a customContext
 * @author gbouteil
 *
 */
public abstract class CustomCategory implements CustomElement {

	/*
	 ************************** PROPERTIES *********************************/	

	/**
	 * The userprofile parent 
	 */
	private UserProfile userProfile;

	/**
	 * The Id of this CustomCategory
	 */
	private String profileId;
	
//  not here : in parent customContext	
//	/**
//	 * Flag : store if CustomCategory is folded or not
//	 */
//	private boolean folded;
	

	/* 
	 ************************** INIT **********************************/
	
	/**
	 * Constructor
	 * @param catId id of the category refered by this
	 * @param user owner of this 
	 */
	public CustomCategory(String profileId, UserProfile user) {
		this.profileId = profileId;
		this.userProfile = user;
	}

	/* 
	 ************************** METHODS **********************************/


	
	/**
	 * @param externalService
	 * @return a list of customSource associated to this CustomCategory
	 * @throws CategoryProfileNotFoundException
	 * @throws CategoryNotLoadedException
	 * @throws ElementNotLoadedException 
	 */
	public abstract List<CustomSource> getSortedCustomSources(ExternalService externalService) throws CategoryProfileNotFoundException, ElementNotLoadedException;

	
//	Not here : it is only specific to CustomManagedCategories
//	/**
//	 * Add a ManagedCustomSource 
//	 * @param managedSourceProfile
//	 */
//	public abstract void addSubscription (ManagedSourceProfile managedSourceProfile) ;
// TODO (GB later : addImportation(), addCreation())

	/**
	 * remove a ManagedCustomSource, indifferently an importation or a subscription
	 * @param managedSourceProfile
	 */
	public abstract void removeCustomManagedSource (ManagedSourceProfile managedSourceProfile) ;
	// TODO (GB later : removeCustomPersonalSource())
	
	/**
	 * @return the categoryProfile associated with this customCategory
	 * @throws CategoryProfileNotFoundException 
	 */
	public abstract CategoryProfile getProfile() throws CategoryProfileNotFoundException ;
	
	/**
	 * @see org.esupportail.lecture.domain.model.CustomElement#getName()
	 */
	public String getName() throws CategoryProfileNotFoundException, CategoryNotLoadedException {
		return getProfile().getName();
	}
	
	
	/* 
	 ************************** ACCESSORS **********************************/
	

	/**
	 * @see org.esupportail.lecture.domain.model.CustomElement#getUserProfile()
	 */
	public UserProfile getUserProfile() {
		return userProfile;
	}

//	/**
//	 * @return true is the category is folded
//	 */
//	public boolean isFolded() {
//		return folded;
//	}
//	/** set if the categoryis folded or not
//	 * @param folded
//	 */
//	public void setFolded(boolean folded) {
//		this.folded = folded;
//	}
	
	/**
	 * @see org.esupportail.lecture.domain.model.CustomElement#getElementId()
	 */
	public String getElementId() {
		return profileId;
	}
	

	
	
}
