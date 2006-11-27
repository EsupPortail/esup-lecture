package org.esupportail.lecture.domain.model;

import java.util.Iterator;
import java.util.List;

import org.esupportail.lecture.domain.ExternalService;
import org.esupportail.lecture.exceptions.CategoryNotLoadedException;
import org.esupportail.lecture.exceptions.ComposantNotLoadedException;
import org.esupportail.lecture.exceptions.ManagedCategoryProfileNotFoundException;



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
	String categoryId;
	
	/**
	 * Flag : store if CustomCategory is folded or not
	 */
	boolean folded;
	
	/* 
	 ************************** ACCESSORS **********************************/
	
	public CustomCategory(String catId, UserProfile user) {
		setId(catId);
		setUserProfile(user);
	}
	public UserProfile getUserProfile() {
		return userProfile;
	}
	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}
	public String getId() {
		return categoryId;
	}
	public void setId(String id) {
		categoryId = id;
	}
	public boolean isFolded() {
		return folded;
	}
	public void setFolded(boolean folded) {
		this.folded = folded;
	}
	
	
	
	
	/**
	 * @return the categoryProfile associated with this customCategory
	 * @throws ManagedCategoryProfileNotFoundException 
	 */
	public abstract CategoryProfile getProfile() throws ManagedCategoryProfileNotFoundException ;
	
	public abstract List<CustomSource> getSortedCustomSources(ExternalService externalService) throws ManagedCategoryProfileNotFoundException, ComposantNotLoadedException;
	
	
	
}
