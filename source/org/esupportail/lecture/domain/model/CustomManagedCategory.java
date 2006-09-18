package org.esupportail.lecture.domain.model;

public class CustomManagedCategory {

	private CategoryProfile categoryProfile;
	/**
	 * Used for tests
	 */
	public String test = "CustomCAtegoryCharge";
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

// TODO
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
