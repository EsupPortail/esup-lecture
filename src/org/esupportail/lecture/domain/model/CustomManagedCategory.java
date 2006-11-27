package org.esupportail.lecture.domain.model;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.DomainServiceImpl;
import org.esupportail.lecture.domain.DomainTools;
import org.esupportail.lecture.domain.ExternalService;
import org.esupportail.lecture.exceptions.CategoryNotLoadedException;
import org.esupportail.lecture.exceptions.ComposantNotLoadedException;
import org.esupportail.lecture.exceptions.ManagedCategoryProfileNotFoundException;

/**
 * Customizations on a managedCategory for a customContext
 * @author gbouteil
 *
 */
public class CustomManagedCategory extends CustomCategory {

	/*
	 ************************** PROPERTIES *********************************/	
	protected static final Log log = LogFactory.getLog(CustomManagedCategory.class);

	
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
	
	public CustomManagedCategory(String catId,UserProfile user){
		subscriptions = new Hashtable<String,CustomManagedSource>();
		setCategoryProfileID(catId);
		setUserProfile(user);
		
	}
	
	/*
	 ************************** METHODS *********************************/	
	


	public List<CustomSource> getSortedCustomSources(ExternalService externalService) throws ManagedCategoryProfileNotFoundException, ComposantNotLoadedException{
	// TODO (later) à redéfinir avec les custom personnal category : en fonction de l'ordre d'affichage peut etre.
		
		ManagedCategoryProfile profile = getProfile();
		profile.updateCustom(this,externalService);
		
		List<CustomSource> listSources = new Vector<CustomSource>();
		for(CustomSource customSource : subscriptions.values()){
			listSources.add(customSource);
			log.debug("Add source");
		}
	
		
		return listSources;
	}
	

	
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
			getUserProfile().addCustomSource(customManagedSource);
		}
	}
	
	

	public void removeManagedCustomSource(ManagedSourceProfile profile) {
		//		 TODO tester avec la BDD
		subscriptions.remove(profile.getId());
		
	}
	
	public ManagedCategoryProfile getProfile() throws ManagedCategoryProfileNotFoundException {
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
	public String getName() throws ManagedCategoryProfileNotFoundException, CategoryNotLoadedException {
		return getProfile().getName();
	}

	public String getContent() throws ManagedCategoryProfileNotFoundException, CategoryNotLoadedException {
		return getProfile().getDescription();
	}



	
}
