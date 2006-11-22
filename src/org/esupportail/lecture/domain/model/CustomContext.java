package org.esupportail.lecture.domain.model;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.DomainTools;
import org.esupportail.lecture.domain.ExternalService;
import org.esupportail.lecture.exceptions.ErrorException;


//import java.util.Collection;
//import java.util.Set;
//import java.util.SortedSet;

/**
 * Customizations on a context for a user profile 
 * @author gbouteil
 *
 */
public class CustomContext implements CustomElement {

	/*
	 ************************** PROPERTIES *********************************/	
	
	/**
	 * Log instance
	 */
	protected static final Log log = LogFactory.getLog(CustomContext.class);
	
	/**
	 * The context Id of this customization refered to
	 */
	private String contextId;

	/**
	 * The context of this customization referred to, corresponding to the contextId
	 */
	private Context context;
	
	/**
	 * The Id of this CustomContext : for Database
	 */
	private int Id;

	/**
	 * The map of subscribed CustomManagedCategory
	 */
	private Map<String,CustomManagedCategory> subscriptions;
	// TODO mettre autre chose qu'une map ?
	
	/**
	 * The userprofile parent (used by hibernate)
	 */
	private UserProfile userProfile;
	

	
	/*
	 ************************** INIT *********************************/	

	
	/**
	 * Constructor
	 */
	public CustomContext() {
		subscriptions = new Hashtable<String,CustomManagedCategory>();
	}
	
	/**
	 * Constructor
	 */
	public CustomContext(String contextId, UserProfile user) {
		subscriptions = new Hashtable<String,CustomManagedCategory>();
		this.contextId = contextId;
		this.userProfile = user;
	}
	
	/*
	 *************************** METHODS ************************************/

	/**
	 * @param externalService access to externalService
	 * @return list of customCategories defined in this customContext
	 */
	public List<CustomCategory> getSortedCustomCategories(ExternalService externalService){
		// TODO à redéfinir avec les custom personnal category : en fonction de l'ordre d'affichage peut etre.
	
		/* update categories in this customContext */
		getContext().updateCustomContext(this,externalService);
		
		List<CustomCategory> listCustomCategories = new Vector<CustomCategory>();
		for(CustomManagedCategory customCat : subscriptions.values()){
			// plus tard, il faudra ajouter les autres custom (imported et personal)
			listCustomCategories.add(customCat);
		}
		return listCustomCategories;
	}
	
	
	/* see later */
	
	
	/** 
	 * Update data contained in this customContext by visibilty evaluation
	 * on managedCategories, in order to update list of customManagedCategories
	 * @param externalService
	 */
	private void update(ExternalService externalService) {
		
		getContext().updateCustomContext(this,externalService);
		// later :  Personnal Categories
		
		Iterator<CustomManagedCategory> iterator = subscriptions.values().iterator();
		while(iterator.hasNext()){
			CustomManagedCategory customManagedCategory = iterator.next();
			customManagedCategory.update(externalService);
		}	
	}
	
	
	
	
	
	
	
	
	
	
	
	
	public List<ManagedCategoryProfile> getVisibleManagedCategoryProfile(ExternalService externalService) {
		
		return getContext().updateCustomContext(this,externalService);
		
	}

	
	
	

	
	
	
	/**
	 * Add a custom category to this custom context if no exists after creating it.
	 * @param profile the managed category profile associated to the customCategory
	 */
	public void addManagedCustomCategory(ManagedCategoryProfile profile) {
		String profileId = profile.getId();
		
		if (!subscriptions.containsKey(profileId)){
			CustomManagedCategory customManagedCategory = new CustomManagedCategory(profileId);
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
	
	public String getContent() {
		return getContext().getDescription();
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



	public Context getContext() {
		if (context == null) {
			context = DomainTools.getChannel().getContext(contextId);
			if (context == null) {
				throw new ErrorException("Context "+contextId+" is not defined in this channel");
			}
		}
		return context;
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



	public String getName() {
		return getContext().getName();
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
