package org.esupportail.lecture.domain.model;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


//import java.util.Collection;
//import java.util.Set;
//import java.util.SortedSet;

/**
 * Customizations on a context for a user profile 
 * @author gbouteil
 *
 */
public class CustomContext {

	/*
	 ************************** PROPERTIES *********************************/	
	
	/**
	 * Log instance
	 */
	protected static final Log log = LogFactory.getLog(CustomContext.class);
	
	/**
	 * Used for tests
	 */
	public String test = "CustomCharge";
	
	
	/**
	 * The context Id of this customization refered to
	 */
	String contextId;

	/**
	 * The Id of this CustomContext
	 */
	int Id;

	/**
	 * The map of subscribed CustomManagedCategory
	 */
	private Map<String,CustomManagedCategory> subscriptions;
	
	/**
	 * The userprofile parent (used by hibernate)
	 */
	private UserProfile userProfile;
	
	/**
	 * Constructor
	 */
	public CustomContext() {
		subscriptions = new Hashtable<String,CustomManagedCategory>();
	}
	
	/*
	 *************************** METHODS ************************************/

	
	
	
	/**
	 * Add a custom category to this context if exist. Creates it if no exist
	 * @param profile the managed category profile associated to the customCategory
	 */
	public void addManagedCustomCategory(ManagedCategoryProfile profile) {
		String profileId = profile.getId();
		
		if (!subscriptions.containsKey(profileId)){
			CustomManagedCategory customManagedCategory = new CustomManagedCategory();
			customManagedCategory.setCategoryProfile(profile);
			subscriptions.put(profileId,customManagedCategory);
		}
	}
	
	
	/**
	 * Remove the managedCategoryProfile from the customContext
	 * @param profile managedCategoryProfile to remove
	 */
	public void removeManagedCustomCategory(ManagedCategoryProfile profile) {
		// TODO tester avec la BDD
		subscriptions.remove(profile.getId());
		
	}
	/* 
	 ************************** ACCESSORS **********************************/

	/**
	 * @return context
	 * @see CustomContext#contextId
	 */
	public String getContextId() {
		return contextId;
	}

	/**
	 * @param contextId : the contextId to set
	 * @see CustomContext#contextId
	 */
	public void setContextId(String contextId) {
		this.contextId = contextId;
	}

	/**
	 * Returns the customCategories of this customContext
	 * @return an Enumeration of the customCategories
	 */
	public Enumeration<CustomManagedCategory> getCustomCategories(){
		// TODO à redéfinir avec les custom personnal category : en fonction de l'ordre d'affichage peut etre.
		Hashtable<String,CustomManagedCategory> hash = (Hashtable<String, CustomManagedCategory>)this.subscriptions;
		return hash.elements();
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public UserProfile getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(UserProfile userprofile) {
		this.userProfile = userprofile;
	}





//	public Collection getSubscriptions() {
//		return subscriptions;
//	}
//
//	public void setSubscriptions(Collection subscriptions) {
//		this.subscriptions = subscriptions;
//	}



//
//	
//	public Collection getCreations() {
//		return creations;
//	}
//
//	public void setCreations(Collection creations) {
//		this.creations = creations;
//	}

	

//
//	public Set getFoldedCategories() {
//		return foldedCategories;
//	}
//
//	
//	public void setFoldedCategories(Set foldedCategories) {
//		this.foldedCategories = foldedCategories;
//	}


//	
//	public SortedSet getOrderCategories() {
//		return orderCategories;
//	}
//
//	
//	public void setOrderCategories(SortedSet orderCategories) {
//		this.orderCategories = orderCategories;
//	}



//
//	public Collection getImportations() {
//		return importations;
//	}
//
//	
//	public void setImportations(Collection importations) {
//		this.importations = importations;
//	}



//
//	public int getTreeWinWidth() {
//		return treeWinWidth;
//	}
//
//	public void setTreeWinWidth(int treeWinWidth) {
//		this.treeWinWidth = treeWinWidth;
//	}






}
