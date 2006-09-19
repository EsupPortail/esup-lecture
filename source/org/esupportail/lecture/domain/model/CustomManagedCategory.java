package org.esupportail.lecture.domain.model;

import org.esupportail.lecture.domain.DomainTools;

/**
 * Customizations on a managedCategory for a customContext
 * @author gbouteil
 *
 */
public class CustomManagedCategory extends CustomCategory {

	/*
	 ************************** PROPERTIES *********************************/	

	/**
	 * Used for tests
	 */
	public String test = "CustomCAtegoryCharge";
	
	
	/**
	 * The ID of related profilCategory
	 */
	private String categoryProfileID;
	
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


	public String getCategoryProfileID() {
		return categoryProfileID;
	}
	public void setCategoryProfileID(String profilID) {
		this.categoryProfileID = profilID;
	}
	public CategoryProfile getCategoryProfile() {
		return DomainTools.getChannel().getManagedCategoryProfile(this.categoryProfileID);
	}
	
}
