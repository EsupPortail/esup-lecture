package org.esupportail.lecture.domain.model;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.esupportail.lecture.domain.DomainTools;
import org.esupportail.lecture.domain.service.PortletService;

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
	
	/*
	 ************************** METHODS *********************************/	
	
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

	

	public void updateData(PortletService portletService) {	
//		 TODO faire evaluate sur le profile
		ManagedCategory managedCategory = (ManagedCategory)getCategoryProfile().getCategory();
		managedCategory.loadAndEvaluateVisibilityOnSourcesToUpdate(this,portletService);
		// later : Personnal Sources;
		
	}
	

	
	public List<CustomSource> getSortedCustomSources(){
	// TODO à redéfinir avec les custom personnal category : en fonction de l'ordre d'affichage peut etre.
		
		List<CustomSource> listSources = new Vector<CustomSource>();
		Iterator iterator = subscriptions.values().iterator();
		
		while(iterator.hasNext()){
			CustomSource customSource = (CustomSource)iterator.next();
			listSources.add(customSource);
		}
		
		return listSources;
	}



	
}
