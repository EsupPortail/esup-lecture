package org.esupportail.lecture.domain.model;

import java.util.Map;

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
	 * The map of subscribed CustomManagedSource
	 */
	private Map<String,CustomManagedSource> subscriptions;
	// TODO mettre autre chose qu'une map ?
	
	/*
	 ************************** INIT *********************************/	

	/*
	 ************************** METHODS *********************************/	
	
	/**
	 * Add a custom source to this custom category if no exists after creating it.
	 * @param profile the managed source profile associated to the customManagedSource
	 */
	public void addManagedCustomSource(ManagedSourceProfile managedSourceProfile) {
		String profileId = managedSourceProfile.getId();
		
		if (!subscriptions.containsKey(profileId)){
			CustomManagedSource customManagedSource = new CustomManagedSource();
			customManagedSource.setSourceProfileID(profileId);
			subscriptions.put(profileId,customManagedSource);
		}
	}
	
	

	public void removeManagedCustomSource(ManagedSourceProfile profile) {
		//		 TODO tester avec la BDD
		subscriptions.remove(profile.getId());
		
	}
	
	public CategoryProfile getCategoryProfile() {
		return DomainTools.getChannel().getManagedCategoryProfile(this.categoryProfileID);
	}
	
	/*
	 ************************** ACCESSORS *********************************/	

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

	
	@Override
	public Category getCategory() {
		
		return getCategoryProfile().getCategory();
	}



	
}
