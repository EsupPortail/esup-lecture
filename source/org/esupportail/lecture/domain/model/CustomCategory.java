package org.esupportail.lecture.domain.model;

/**
 * Customizations on a managedCategory for a customContext
 * @author gbouteil
 *
 */
public class CustomCategory {

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
}
