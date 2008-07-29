/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.ExternalService;
import org.esupportail.lecture.exceptions.domain.CategoryNotLoadedException;
import org.esupportail.lecture.exceptions.domain.CategoryProfileNotFoundException;


/**
 * Managed category element : loaded from a remote definition, transfered by an XML file.
 * A category contains a set of sourceProfiles
 * @author gbouteil
 * @see Category
 *
 */
@SuppressWarnings("serial")
public class ManagedCategory extends Category {

	/*
	 *********************** PROPERTIES**************************************/ 


	/**
	 * Log instance.
	 */
	protected static final Log LOG = LogFactory.getLog(ManagedCategory.class);
	/**
	 * Visibility sets of this category (if defined).
	 * Using depends on trustCategory parameter in 
	 * ManagedCategoryProfile corresponding 
	 */
	private VisibilitySets visibility;
//	/**
//	 * Managed category edit mode : not used for the moment (if defined)
//	 * Using depends on trustCategory parameter in 
//	 * ManagedCategoryProfile corresponding
//	 */
//	private Editability edit;
	
	/*
	 *********************** INIT **************************************/ 


	
	/*
	 *********************** METHOD **************************************/ 
		
	/**
	 * Update the CustomManagedCategory linked to this ManagedCategory.
	 * It sets up subscriptions of customManagedCategory on managedSourcesProfiles
	 * defined in ths ManagedCategory, according to managedSourceProfiles visibility
	 * (there is not any loading of source at this time)
	 * @param customManagedCategory customManagedCategory to update
	 * @param  ex access to external service for visibility evaluation
	 * @throws CategoryNotLoadedException 
	 * @throws CategoryProfileNotFoundException 
	 */
	protected synchronized void updateCustom(final CustomManagedCategory customManagedCategory,
			final ExternalService ex) throws CategoryNotLoadedException, CategoryProfileNotFoundException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("id = " + this.getProfileId() + " - updateCustom("
					+ customManagedCategory.getElementId() + ",externalService)");
		}
		Iterator<SourceProfile> iterator = getSourceProfilesHash().values().iterator();
		
		// update for managedSources defined in this managedCategory
		while (iterator.hasNext()) {
			ManagedSourceProfile msp = (ManagedSourceProfile) iterator.next();
			msp.updateCustomCategory(customManagedCategory, ex);
		
		}
		
		// update managedSources not anymore in this managedCategory
		updateCustomForVanishedSubscriptions(customManagedCategory);
	}
	
	/**
	 * Return a list of (SourceProfile,VisibilityMode).
	 * Corresponding to visible sources for user
	 * in this ManagedCategory and update it (like methode updateCustom): 
	 * It sets up subscriptions of customManagedCategory on managedSourcesProfiles
	 * defined in ths ManagedCategory, according to managedSourceProfiles visibility
	 * (there is not any loading of source at this time)
	 * @param customManagedCategory custom to update
	 * @param ex access to external service for visibility evaluation
	 * @return list of ProfileVisibility
	 * @throws CategoryNotLoadedException 
	 * @throws CategoryProfileNotFoundException 
	 */
	protected List<ProfileVisibility> getVisibleSourcesAndUpdateCustom(
			final CustomManagedCategory customManagedCategory,
			final ExternalService ex) throws CategoryNotLoadedException, CategoryProfileNotFoundException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("id=" + this.getProfileId() + " - getVisibleSourcesAndUpdateCustom("
					+ getProfileId() + ",externalService)");
		}
		List<ProfileVisibility> couplesVisib = new Vector<ProfileVisibility>();
		Iterator<SourceProfile> iterator = getSourceProfilesHash().values().iterator();
		
		// update and get managedSources defined in this managedCategory 
		while (iterator.hasNext()) {
			ManagedSourceProfile msp = (ManagedSourceProfile) iterator.next();
			ProfileVisibility couple;
			VisibilityMode mode = msp.updateCustomCategory(customManagedCategory, ex);
			if (mode != VisibilityMode.NOVISIBLE) {
				couple = new ProfileVisibility(msp, mode);
				couplesVisib.add(couple);
			}
		}
		
		// update for managedSources not anymore in this managedCategory
		updateCustomForVanishedSubscriptions(customManagedCategory);
		
		return couplesVisib;
	}

	/**
	 * Update CustomManagedCategory for managedSources not anymore in subscriptions.
	 * @param customManagedCategory element to update
	 */
	private void updateCustomForVanishedSubscriptions(final CustomManagedCategory customManagedCategory) {
		List<String> sids = new ArrayList<String>();
		for (String sourceId : customManagedCategory.getSubscriptions().keySet()) {
			sids.add(sourceId);
		}
		for (String sourceId : sids) {
			if (!containsSource(sourceId)) {
				customManagedCategory.removeCustomManagedSource(sourceId);
				UserProfile user = customManagedCategory.getUserProfile();
				user.removeCustomManagedSourceIfOrphan(sourceId);
			}
		}
	}

	
	/**
	 * @param sourceId
	 * @return true if this managedCategory contains the source identified by sourceId
	 */
	public boolean containsSource(final String sourceId) {
	   	if (LOG.isDebugEnabled()) {
    		LOG.debug("profileId=" + super.getProfileId() + " - containsSource(" + sourceId + ")");
    	}
	   	Hashtable<String, SourceProfile> hashSourceProfile = getSourceProfilesHash();
	   	
	   	boolean result = hashSourceProfile.containsKey(sourceId);
	   	return result;
	}
	
	
	/*
	 *********************** ACCESSORS**************************************/ 

	
	
	/**
	 * Returns visibility sets of this managed category (if defined).
	 * @return visibility
	 */
	protected VisibilitySets getVisibility() {
		return visibility;
	}


	/**
	 * Sets visibility sets of this managed category.
	 * @param visibility
	 */
	public void setVisibility(final VisibilitySets visibility) {
		this.visibility = visibility;
	}

}
