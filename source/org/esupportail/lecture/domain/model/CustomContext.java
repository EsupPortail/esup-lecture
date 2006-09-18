package org.esupportail.lecture.domain.model;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

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

	private Hashtable<String,CustomManagedCategory> subscriptions;
	
//	private Collection creations;
//	private Set foldedCategories;
//	private SortedSet orderCategories;
//	private Collection importations;
//	private int treeWinWidth;
	
	/*
	 ************************** Initialization ************************************/
	
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
	
	
	public void removeManagedCustomCategory(ManagedCategoryProfile profile) {
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

	public Enumeration<CustomManagedCategory> getCustomCategories(){
		// TODO � red�finir avec les custom personnal category : en fonction de l'ordre d'affichage peut etre.
		return subscriptions.elements();
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
