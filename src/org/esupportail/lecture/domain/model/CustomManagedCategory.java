package org.esupportail.lecture.domain.model;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.esupportail.lecture.domain.DomainTools;
import org.esupportail.lecture.domain.ExternalService;

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

	public CustomManagedCategory(){
		subscriptions = new Hashtable<String,CustomManagedSource>();
	}
	
	public CustomManagedCategory(String catId){
		subscriptions = new Hashtable<String,CustomManagedSource>();
		setCategoryProfileID(catId);
	}
	
	/*
	 ************************** METHODS *********************************/	
	
	/**
	 * Update data contained in this customManagedCategory by visibilty evaluation
	 * on managedCategories, in order to update list of customManagedCategories
	 * @param externalService
	 */
	public void update(ExternalService externalService) {	
		ManagedCategoryProfile managedCategory = (ManagedCategoryProfile)getCategoryProfile();
		managedCategory.updateCustomCategory(this,externalService);
		// later : Personnal Sources;
		
	}

	public List<CustomSource> getSortedCustomSources(ExternalService externalService){
	// TODO � red�finir avec les custom personnal category : en fonction de l'ordre d'affichage peut etre.
		
		ManagedCategoryProfile profile = getCategoryProfile();
		
		update(externalService);
		
		List<CustomSource> listSources = new Vector<CustomSource>();
		for(CustomSource customSource : subscriptions.values()){
			listSources.add(customSource);
		}
	
		
		return listSources;
	}
	
	/* see later */
	
	
	/**
	 * Add a custom source to this custom category if no exists after creating it.
	 * @param profile the managed source profile associated to the customManagedSource
	 */
	public void addManagedCustomSource(ManagedSourceProfile managedSourceProfile) {
		String profileId = managedSourceProfile.getId();
		
		if (!subscriptions.containsKey(profileId)){
			CustomManagedSource customManagedSource = new CustomManagedSource(managedSourceProfile);
			customManagedSource.setManagedCategoryProfileId(this.getCategoryProfileID());
			subscriptions.put(profileId,customManagedSource);
		}
	}
	
	

	public void removeManagedCustomSource(ManagedSourceProfile profile) {
		//		 TODO tester avec la BDD
		subscriptions.remove(profile.getId());
		
	}
	
	public ManagedCategoryProfile getCategoryProfile() {
		Channel channel = DomainTools.getChannel();
		return channel.getManagedCategoryProfile(this.categoryProfileID);
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

	

	
	

	

	public String getName() {
		return getCategoryProfile().getName();
	}

	public String getContent() {
		return getCategoryProfile().getDescription();
	}



	
}
