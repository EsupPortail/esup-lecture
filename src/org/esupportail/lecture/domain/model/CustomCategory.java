package org.esupportail.lecture.domain.model;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
	 * Log instance
	 */
	protected static final Log log = LogFactory.getLog(CustomCategory.class);


	/**
	 * The userprofile parent 
	 */
	private UserProfile userProfile;

	/**
	 * The Id of this CustomCategory
	 */
	private String profileId;
	/**
	 * database pk
	 */
	private long customCategoryPK;
	
//  not here : in parent customContext	
//	/**
//	 * Flag : store if CustomCategory is folded or not
//	 */
//	private boolean folded;
	

	/* 
	 ************************** INIT **********************************/
	
	/**
	 * Constructor
	 * @param profileId of the category profile
	 * @param user owner of this 
	 */
	public CustomCategory(String profileId, UserProfile user) {
		if (log.isDebugEnabled()){
			log.debug("CustomCategory("+profileId+","+user.getUserId()+")");
		}
		this.profileId = profileId;
		this.userProfile = user;
	}

	/**
	 * default constructor
	 */
	public CustomCategory() {
		if (log.isDebugEnabled()){
			log.debug("CustomCategory()");
		}
	}

	//TODO (RB) remove ?
//	public CustomCategory() {
//		if (log.isDebugEnabled()){
//			log.debug("CustomCategory()");
//		}
//	}

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
// TODO (GB later) addImportation(), addCreation())

	/**
	 * remove a ManagedCustomSource, indifferently an importation or a subscription
	 * @param managedSourceProfile
	 */
	public abstract void removeCustomManagedSource (ManagedSourceProfile managedSourceProfile) ;
	// TODO (GB later) removeCustomPersonalSource())
	
	/**
	 * @return the categoryProfile associated with this customCategory
	 * @throws CategoryProfileNotFoundException 
	 */
	public abstract CategoryProfile getProfile() throws CategoryProfileNotFoundException ;
	
	/**
	 * @see org.esupportail.lecture.domain.model.CustomElement#getName()
	 */
	public String getName() throws CategoryProfileNotFoundException, CategoryNotLoadedException {
		if (log.isDebugEnabled()){
			log.debug("getName()");
		}
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
	
	/**
	 * @param userProfile
	 */
	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}
	
	/**
	 * @see org.esupportail.lecture.domain.model.CustomElement#getElementId()
	 */
	public String getElementId() {
		return profileId;
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
	public String getProfileId() {
		return profileId;
	}

	/**
	 * @param profileId
	 */
	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}

}
