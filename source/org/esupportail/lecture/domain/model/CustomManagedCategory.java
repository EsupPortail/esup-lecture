package org.esupportail.lecture.domain.model;

/**
 * Customizations on a managedCategory for a customContext
 * @author gbouteil
 *
 */
public class CustomManagedCategory {

	/*
	 ************************** PROPERTIES *********************************/	

	private CategoryProfile categoryProfile;
	/**
	 * Used for tests
	 */
	public String test = "CustomCAtegoryCharge";
	
	/* 
	 ************************** ACCESSORS **********************************/
	
	/**
	 * @return Returns the test.
	 */
	public String getTest() {
		return test;
	}
	/**
	 * @param test The test to set.
	 */
	public void setTest(String test) {
		this.test = test;
	}


	/**
	 * Sets the managedCategory profile associated with this CustomManagedCategoryProfile
	 * @param profile
	 */
	public void setCategoryProfile(ManagedCategoryProfile profile) {
		categoryProfile = profile;
		
	}
	/**
	 * @return Returns the categoryProfile.
	 */
	public CategoryProfile getCategoryProfile() {
		return categoryProfile;
	}
}
