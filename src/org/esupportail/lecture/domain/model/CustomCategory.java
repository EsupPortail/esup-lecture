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
import org.esupportail.lecture.exceptions.domain.ComputeFeaturesException;
import org.esupportail.lecture.exceptions.domain.SourceNotVisibleException;
import org.esupportail.lecture.exceptions.domain.SourceObligedException;
import org.esupportail.lecture.exceptions.domain.SourceProfileNotFoundException;


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
	private String elementId;	
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
		this.elementId = profileId;
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
	 * Return a list of <SourceProfile,AvailabilityMode> corresponding to visible sources for user, 
	 * in this customCategory.
	 * @param ex access to external service 
	 * @return list of ProfileAvailability
	 * @throws CategoryProfileNotFoundException 
	 * @throws CategoryNotVisibleException 
	 * @throws CategoryNotLoadedException 
	 */
	public abstract List<ProfileAvailability> getVisibleSources(ExternalService ex) 
		throws CategoryProfileNotFoundException, CategoryNotVisibleException, CategoryNotLoadedException;

	
	
	/**
	 * The used name of the categoryProfile
	 * @throws CategoryProfileNotFoundException 
	 * @see org.esupportail.lecture.domain.model.CustomElement#getName()
	 */
	public String getName() throws CategoryProfileNotFoundException  {
		if (log.isDebugEnabled()){
			log.debug("id="+elementId+" - getName()");
		}
		return getProfile().getName();
	}
	
	/**
	 * For a customManagedCategory, it subscribes sourceId, but for a customPersonalcategory, 
	 * there is not any subscription and throws a SubcriptionInPersonalException
	 * @param sourceId source ID
	 * @param ex access to externalService
	 * @throws CategoryProfileNotFoundException 
	 * @throws SourceProfileNotFoundException 
	 * @throws CategoryNotLoadedException 
	 * @throws SourceNotVisibleException 
	 * @throws ComputeFeaturesException 
	 */
	// TODO faire le throw SubcriptionInPersonalImpossibleException pour le personal
	public abstract void subscribeToSource(String sourceId, ExternalService ex) 
		throws CategoryProfileNotFoundException, CategoryNotLoadedException, SourceProfileNotFoundException, 
		SourceNotVisibleException, ComputeFeaturesException;


	/**
	 * For a customManagedCategory, it unsubscribes sourceId, but for a customPersonalcategory, 
	 * there is not any subscription and throws a SubcriptionInPersonalException
	 * @param sourceId source ID
	 * @param ex access to externalService
	 * @throws CategoryProfileNotFoundException 
	 * @throws SourceProfileNotFoundException 
	 * @throws CategoryNotLoadedException 
	 * @throws ComputeFeaturesException 
	 * @throws SourceObligedException 
	 */
	public abstract void unsubscribeToSource(String sourceId, ExternalService ex) 
		throws CategoryProfileNotFoundException, CategoryNotLoadedException, SourceProfileNotFoundException, 
		ComputeFeaturesException, SourceObligedException;

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null) return false;
		if (!(o instanceof CustomCategory)) return false;
		final CustomCategory customCategory = (CustomCategory) o;
		if (!customCategory.getElementId().equals(this.getElementId())) return false;
		return true;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return this.getElementId().hashCode();
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
		return elementId;
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
	 * @param elementId
	 */
	public void setElementId(String elementId) {
		this.elementId = elementId;
	}

}
