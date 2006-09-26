package org.esupportail.lecture.domain.model;

import java.util.Iterator;
import java.util.List;

import org.esupportail.lecture.domain.service.PortletService;

/**
 * Customizations on a managedCategory for a customContext
 * @author gbouteil
 *
 */
public abstract class CustomCategory implements CustomElement {

	/*
	 ************************** PROPERTIES *********************************/	

	/**
	 * The userprofile parent (used by hibernate)
	 */
	private UserProfile userProfile;

	/**
	 * The Id of this CustomCategory
	 */
	int Id;
	
	/**
	 * Flag : store if CustomCategory is folded or not
	 */
	boolean folded;
	
	/* 
	 ************************** ACCESSORS **********************************/
	
	public UserProfile getUserProfile() {
		return userProfile;
	}
	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public boolean isFolded() {
		return folded;
	}
	public void setFolded(boolean folded) {
		this.folded = folded;
	}
	
	
	
	
	/**
	 * @return the categoryProfile associated with this customCategory
	 */
	public abstract CategoryProfile getCategoryProfile() ;
	
	public abstract List<CustomSource> getSortedCustomSources();
	
	/** Update data contains in this customCategory :
	 *  - evaluation visibilty on managedSources to update list of customManagedSources
	 * @param portletService
	 */
	public abstract void updateData(PortletService portletService); 
	
	
}
