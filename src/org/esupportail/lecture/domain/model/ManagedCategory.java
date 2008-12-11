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
import org.esupportail.lecture.exceptions.domain.ManagedCategoryNotLoadedException;


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
	 * Inner features declared in XML file.
	 */
	protected InnerFeatures inner;
	/**
	 * Name of the category. 
	 */
	private String name = "";
	/**
	 * Description of the category.
	 */
	private String description = "";

	/*
	 *********************** INIT **************************************/ 

	/**
	 * Constructor.
	 * @param cp categoryProfile associated to this managedCategory
	 */
	public ManagedCategory(final ManagedCategoryProfile cp) {
		super(cp);
	   	if (LOG.isDebugEnabled()) {
    		LOG.debug("ManagedCategory(" + cp.getId() + ")");
    	}
	   	inner = new InnerFeatures();
	}
	
	/*
	 *********************** METHOD **************************************/ 

	/**
	 * @return managedCategoryProfile associated to this category
	 */
	@Override
	public ManagedCategoryProfile getProfile() {
		return (ManagedCategoryProfile) super.getProfile();
	}
	
	/**
	 * @param sourceId
	 * @return true if this managedCategory contains the source identified by sourceId
	 */
	public boolean containsSource(final String sourceId) {
	   	if (LOG.isDebugEnabled()) {
    		LOG.debug("profileId=" + getProfileId() + " - containsSource(" + sourceId + ")");
    	}
	   	Hashtable<String, SourceProfile> hashSourceProfile = getSourceProfilesHash();
	   	boolean result = hashSourceProfile.containsKey(sourceId);
	   	return result;
	}
	
	/**
	 * Return accessibility of the category.
	 * @return accessibility
	 */
	protected Accessibility getAccess() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("id=" + getProfileId() + " - getAccessy()");
		}
		return getProfile().getAccess();
	}

	/**
	 * @return timeOut
	 */
	protected int getTimeOut() {
		return getProfile().getTimeOut();
	}

	/**
	 * @return ttl
	 */
	public int getTtl() {
		return getProfile().getTtl();
	}
	
	/**
	 * Return visibility of the category, taking care of inheritance regulars.
	 * @return visibility 
	 * @throws ManagedCategoryNotLoadedException 
	 */
	protected VisibilitySets getVisibility() throws ManagedCategoryNotLoadedException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("id=" + getProfileId() + " - getVisibility()");
		}
		VisibilitySets v;
		try {
			v = getProfile().getVisibility();
			// Visibility is computed in ManagedCategoryProfile
		} catch (ManagedCategoryNotLoadedException e) {
			LOG.error("Impossible situation : ManagedCategoryNotLoadedException "
					+ "in a ManagedCategory - please contact developer");
			v = null;
		}
		return v;
	}
	
	/**
	 * @param visibility 
	 */
	public void setVisibility(final VisibilitySets visibility) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("id=" + getProfileId() + " - setVisibility(visibility)");
		}
		inner.setVisibility(visibility);
		getProfile().setFeaturesComputed(false);
	}
	
	/**	
	 * Return editability of the category, taking care of inheritance regulars.
	 * @return edit
	 * @throws ManagedCategoryNotLoadedException 
	 */
	protected Editability getEdit() throws ManagedCategoryNotLoadedException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("id = " + getProfileId() + " - getEdit()");
		}
		Editability e;
		try {
			e = getProfile().getEdit();
			// Editability is computed in ManagedCategoryProfile
			
		} catch (ManagedCategoryNotLoadedException ex) {
			LOG.error("Impossible situation : ManagedCategoryNotLoadedException"
					+ " in a ManagedCategory, please contact developer");
			e = null;
		}
		return e;
	}
	
	/**
	 * @param edit	
	 */
	public void setEdit(final Editability edit) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("id=" + getProfileId() + " - setEditability()");
		}
		inner.setEdit(edit);
		getProfile().setFeaturesComputed(false);
	}
	
	/* 
	 *************************** INNER CLASS ******************************** */	
	
	/**
	 * Inner Features (editability, visibility) declared in xml file. 
	 * These values are used according to inheritance regulars
	 * @author gbouteil
	 */
	protected class InnerFeatures {
		 
		/** 
		 * Managed category edit mode.
		*/
		private Editability edit;
		/**
		 * Visibility rights for groups on the remote source.
		 */
		private VisibilitySets visibility;
		
		/**
		 * Constructor. 
		 */
		protected InnerFeatures() {
			// Nothing to do
		}

		/**
		 * @return edit
		 */
		protected Editability getEdit() {
			return edit;
		}
		/**
		 * @param edit
		 */
		protected void setEdit(final Editability edit) {
			this.edit = edit;
		}
		/**
		 * @return edit
		 */
		protected VisibilitySets getVisibility() {
			return visibility;
		}
		/**
		 * @param visibility
		 */
		protected void setVisibility(final VisibilitySets visibility) {
			this.visibility = visibility;
		}
	
	}
	

	/* UPDATING */
	
	/**
	 * Update the CustomManagedCategory linked to this ManagedCategory.
	 * It sets up subscriptions of customManagedCategory on managedSourcesProfiles
	 * defined in ths ManagedCategory, according to managedSourceProfiles visibility
	 * (there is not any loading of source at this time)
	 * @param customManagedCategory customManagedCategory to update
	 */
	protected synchronized void updateCustom(final CustomManagedCategory customManagedCategory) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("id = " + getProfileId() + " - updateCustom("
					+ customManagedCategory.getElementId() + ")");
		}
		Iterator<SourceProfile> iterator = getSourceProfilesHash().values().iterator();
		
		// update for managedSources defined in this managedCategory
		while (iterator.hasNext()) {
			ManagedSourceProfile msp = (ManagedSourceProfile) iterator.next();
			try {
				msp.updateCustomCategory(customManagedCategory);
			} catch (ManagedCategoryNotLoadedException e) {
				String errorMsg = "Impossible to update customCategory " + getProfileId()
				+ " because of managedCategory not loaded.";
				LOG.error(errorMsg);
			}
		
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
	 * @return list of CoupleProfileVisibility
	 */
	protected List<CoupleProfileVisibility> getVisibleSourcesAndUpdateCustom(
			final CustomManagedCategory customManagedCategory) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("id=" + getProfileId() + " - getVisibleSourcesAndUpdateCustom("
					+ getProfileId() + ")");
		}
		List<CoupleProfileVisibility> couplesVisib = new Vector<CoupleProfileVisibility>();
		Iterator<SourceProfile> iterator = getSourceProfilesHash().values().iterator();
		
		// update and get managedSources defined in this managedCategory 
		while (iterator.hasNext()) {
			ManagedSourceProfile msp = (ManagedSourceProfile) iterator.next();
			CoupleProfileVisibility couple;
			VisibilityMode mode;
			try {
				mode = msp.updateCustomCategory(customManagedCategory);
			
				if (mode != VisibilityMode.NOVISIBLE) {
					couple = new CoupleProfileVisibility(msp, mode);
					couplesVisib.add(couple);
				}
			} catch (ManagedCategoryNotLoadedException e) {
				String errorMsg = "Impossible to update customCategory " + getProfileId()
				+ " because of managedCategory not loaded.";
				LOG.error(errorMsg);
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


	
	/*
	 *********************** ACCESSORS**************************************/ 

	/**
	 * Returns the name of the category.
	 * @return name
	 * */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * Sets the category name.
	 * @param name
	 */
	@Override
	public void setName(final String name) {
		this.name = name;
	}
	
	/**
	 * Returns the description of the category.
	 * @return description
	 * */
	@Override
	protected String getDescription() {
		return description;
	}


	/**
	 * Sets the description name.
	 * @param description
	 */
	@Override
	public void setDescription(final String description) {
		this.description = description;
	}

}
