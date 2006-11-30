package org.esupportail.lecture.domain.model;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.DomainTools;
import org.esupportail.lecture.domain.ExternalService;
import org.esupportail.lecture.exceptions.ElementNotLoadedException;
import org.esupportail.lecture.exceptions.ContextNotFoundException;
import org.esupportail.lecture.exceptions.ElementProfileNotFoundException;
import org.esupportail.lecture.exceptions.TreeSizeErrorException;


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
	 * The map of subscribed CustomManagedCategory
	 */
	private Hashtable<String,CustomManagedCategory> subscriptions;
	
	/**
	 * The userprofile parent (used by hibernate)
	 */
	private UserProfile userProfile;
	
	/**
	 * The Id of this CustomContext : for Database
	 */
	private int Id;
	
	/**
	 * Tree size of the customContext
	 */
	private int treeSize;
	
	/**
	 * Set of id corresponding to unfolded categories
	 */
	private Set<String> unfoldedCategories; 

	
	/*
	 ************************** INIT *********************************/	
	
	/**
	 * Constructor
	 * @param contextId id of the context refered by this
	 * @param user owner of this
	 */
	public CustomContext(String contextId, UserProfile user) {
		subscriptions = new Hashtable<String,CustomManagedCategory>();
		unfoldedCategories = new HashSet<String>();
		this.contextId = contextId;
		this.userProfile = user;
		treeSize = 20;
	}
	
	/*
	 *************************** METHODS ************************************/

	/**
	 * @param externalService access to externalService
	 * @return list of customCategories defined in this customContext
	 * @throws ContextNotFoundException 
	 * @throws ElementNotLoadedException 
	 */
	public List<CustomCategory> getSortedCustomCategories(ExternalService externalService) throws ContextNotFoundException, ElementNotLoadedException{
		// TODO (GB later) rewrite with custom personnal category (+ sorted display)
	
		/* update this customContext with context */
		getContext().updateCustom(this,externalService);
		
		List<CustomCategory> listCustomCategories = new Vector<CustomCategory>();
		for(CustomManagedCategory customCat : subscriptions.values()){
			// later : add other custom elements (imported et personal)
			listCustomCategories.add(customCat);
		}
		return listCustomCategories;
	}
	

	/**
	 * Add a custom category to this custom context if no exists after creating it.
	 * @param profile the managed category profile associated to the customCategory
	 */
	public void addCustomManagedCategory(ManagedCategoryProfile profile) {
		String profileId = profile.getId();
		
		if (!subscriptions.containsKey(profileId)){
			CustomManagedCategory customManagedCategory = new CustomManagedCategory(profileId,userProfile);
			subscriptions.put(profileId,customManagedCategory);
			userProfile.addCustomManagedCategory(customManagedCategory);
		}
	}
	
	/**
	 * Remove the managedCategoryProfile from the customContext
	 * @param profile managedCategoryProfile to remove
	 */
	public void removeCustomManagedCategory(ManagedCategoryProfile profile) {
		// TODO (GB) tester avec la BDD
		subscriptions.remove(profile.getId());
		userProfile.removeCustomManagedCategory(profile.getId());
		
	}
	
	/**
	 * @return context refered by this
	 * @throws ContextNotFoundException 
	 */
	public Context getContext() throws ContextNotFoundException {
		if (context == null) {
			context = DomainTools.getChannel().getContext(contextId);
		}
		return context;
	}
	
	/**
	 * @throws ContextNotFoundException 
	 * @see org.esupportail.lecture.domain.model.CustomElement#getName()
	 */
	public String getName() throws ContextNotFoundException {
		return getContext().getName();
	}
	
	/* 
	 ************************** ACCESSORS **********************************/

	/**
	 * @return contextId
	 * @see CustomContext#contextId
	 */
	public String getElementId() {
		return contextId;
	}


	/**
	 * @return id
	 */
	public int getId() {
		return Id;
	}

	/**
	 * @param id
	 */
	public void setId(int id) {
		Id = id;
	}

	/**
	 * @return userProfile
	 */
	public UserProfile getUserProfile() {
		return userProfile;
	}

	public void setTreeSize(int size)throws TreeSizeErrorException {
		// TODO (GB) externaliser les bornes
		if ((size >=0) && (size <=100)){
			treeSize = size;
		}else {
			throw new TreeSizeErrorException("TreeSize must be into 0 and 100");
		}
		
	}

	public int getTreeSize() {
		return treeSize;
	}

	public void foldCategory(String catId) {
		if (!unfoldedCategories.remove(catId)){
			log.warn("foldCategory("+catId+") is called in customContext "+contextId+" but this category is yet folded");
		}
	}
	
	public void unfoldCategory(String catId) {
		if(!unfoldedCategories.add(catId)){
			log.warn("unfoldCategory("+catId+") is called in customContext "+contextId+" but this category is yet unfolded");
		}
	
	}

	public boolean isCategoryFolded(String catId) {
		if(unfoldedCategories.contains(catId)){
			return false;
		}else {
			return true;
		}
	}

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
