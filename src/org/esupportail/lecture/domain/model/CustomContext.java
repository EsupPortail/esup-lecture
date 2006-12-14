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
		if (log.isDebugEnabled()){
			log.debug("CustomContext("+contextId+","+user.getUserId()+")");
		}
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
	public List<CustomCategory> getSortedCustomCategories(ExternalService externalService) 
		throws ContextNotFoundException, ElementNotLoadedException{
		if (log.isDebugEnabled()){
			log.debug("getSortedCustomCategories(externalService)");
		}
		// TODO (GB later) rewrite with custom personnal category (+ sorted display)
	
		/* update this customContext with context */
		getContext().updateCustom(this,externalService);
		
		DomainTools.getDaoService().updateUserProfile(userProfile);
		DomainTools.getDaoService().updateCustomContext(this);
		
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
	public void addSubscription(ManagedCategoryProfile profile) {
		if (log.isDebugEnabled()){
			log.debug("addSubscription("+profile.getId()+")");
		}
		String profileId = profile.getId();
		
		if (!subscriptions.containsKey(profileId)){
			CustomManagedCategory customManagedCategory = new CustomManagedCategory(profileId,userProfile);
			subscriptions.put(profileId,customManagedCategory);
			userProfile.addCustomCategory(customManagedCategory);
		}
	}
	// TODO (GB later : addImportation(), addCreation())
	
	/**
	 * Remove the managedCategoryProfile from the customContext
	 * Used to remove a subscription or an importation indifferently
	 * @param profile managedCategoryProfile to remove
	 */
	public void removeCustomManagedCategory(ManagedCategoryProfile profile) {
		if (log.isDebugEnabled()){
			log.debug("removeCustomManagedCategory("+profile.getId()+")");
		}
		// TODO (GB) tester avec la BDD
		String profileId = profile.getId();
		CustomManagedCategory cmc = subscriptions.get(profileId);
		if (cmc != null) {
			subscriptions.remove(profileId);
			userProfile.removeCustomCategory(profile.getId());
			DomainTools.getDaoService().deleteCustomCategory(cmc);
		} 
		
	}
	// TODO (GB later : removeCustomPersonalCategory())
	
	/**
	 * @return context refered by this
	 * @throws ContextNotFoundException 
	 */
	public Context getContext() throws ContextNotFoundException {
		if (log.isDebugEnabled()){
			log.debug("getContext()");
		}
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
		if (log.isDebugEnabled()){
			log.debug("getName()");
		}
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
	 * @return userProfile
	 */
	public UserProfile getUserProfile() {
		return userProfile;
	}

	public void setTreeSize(int size)throws TreeSizeErrorException {
		// TODO (GB) externaliser les bornes
		if ((size >=0) && (size <=100)){
			treeSize = size;
			DomainTools.getDaoService().updateCustomContext(this);
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
		} else {
			DomainTools.getDaoService().updateCustomContext(this);
		}
	}
	
	public void unfoldCategory(String catId) {
		if(!unfoldedCategories.add(catId)){
			log.warn("unfoldCategory("+catId+") is called in customContext "+contextId+" but this category is yet unfolded");
		} else {
			DomainTools.getDaoService().updateCustomContext(this);
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
