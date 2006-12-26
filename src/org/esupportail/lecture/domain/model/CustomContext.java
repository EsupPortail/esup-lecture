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
import org.esupportail.lecture.exceptions.domain.ContextNotFoundException;
import org.esupportail.lecture.exceptions.domain.ElementNotLoadedException;
import org.esupportail.lecture.exceptions.domain.TreeSizeErrorException;

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
	private Map<String,CustomManagedCategory> subscriptions;
	
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

	/**
	 * Database Primary Key
	 */
	private long customContextPK; 

	
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
	
	/**
	 * Constructor
	 */
	public CustomContext() {
		subscriptions = new Hashtable<String,CustomManagedCategory>();
		unfoldedCategories = new HashSet<String>();
	}
	
	/*
	 *************************** METHODS ************************************/

	/**
	 * @param ex access to externalService
	 * @return list of customCategories defined in this customContext
	 * @throws ContextNotFoundException 
	 * @throws ElementNotLoadedException 
	 */
	public List<CustomCategory> getSortedCustomCategories(ExternalService ex) 
		throws ContextNotFoundException, ElementNotLoadedException{
		if (log.isDebugEnabled()){
			log.debug("getSortedCustomCategories(externalService)");
		}
		// TODO (GB later) rewrite with custom personnal category (+ sorted display)
	
		/* update this customContext with context */
		getContext().updateCustom(this,ex);
		
		DomainTools.getDaoService().updateCustomContext(this);
		DomainTools.getDaoService().updateUserProfile(userProfile);
		
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
	// TODO (GB later) addImportation(), addCreation())
	
	/**
	 * Remove the managedCategoryProfile from the customContext
	 * Used to remove a subscription or an importation indifferently
	 * @param profile managedCategoryProfile to remove
	 */
	public void removeCustomManagedCategory(ManagedCategoryProfile profile) {
		if (log.isDebugEnabled()){
			log.debug("removeCustomManagedCategory("+profile.getId()+")");
		}
		String profileId = profile.getId();
		CustomManagedCategory cmc = subscriptions.get(profileId);
		if (cmc != null) {
			subscriptions.remove(profileId);
			userProfile.removeCustomCategory(profile.getId());
			// TODO (gb later) : il faudra supprimer toutes les références à cette cmc
			// (importations dans d'autre customContext)
		} 
		
	}
	// TODO (GB later)  removeCustomPersonalCategory()
	
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
	
	
	/**
	 * modify tree size
	 * @param size
	 * @throws TreeSizeErrorException
	 */
	public void modifyTreeSize(int size)throws TreeSizeErrorException {
		/* old name was setTreesize but it has been changed to prevent 
		 * loop by calling dao
		 */
		// TODO (GB later) externaliser les bornes
		if ((size >=0) && (size <=100)){
			treeSize = size;
			DomainTools.getDaoService().updateCustomContext(this);
		}else {
			throw new TreeSizeErrorException("TreeSize must be into 0 and 100");
		}
		
	}
	

	/**
	 * mark as folded a category
	 * @param catId
	 */
	public void foldCategory(String catId) {
		if (!unfoldedCategories.remove(catId)){
			log.warn("foldCategory("+catId+") is called in customContext "+contextId+" but this category is yet folded");
		} else {
			DomainTools.getDaoService().updateCustomContext(this);
		}
	}
	
	/**
	 * mark as unfolded a category
	 * @param catId
	 */
	public void unfoldCategory(String catId) {
		if(!unfoldedCategories.add(catId)){
			log.warn("unfoldCategory("+catId+") is called in customContext "+contextId+" but this category is yet unfolded");
		} else {
			DomainTools.getDaoService().updateCustomContext(this);
		}
	
	}

	/**
	 * @param catId
	 * @return if category is folded or not
	 */
	public boolean isCategoryFolded(String catId) {
		boolean ret = false;
		if(unfoldedCategories.contains(catId)){
			ret = false;
		}else {
			ret = true;
		}
		return ret;
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

	/**
	 * @param size
	 * @throws TreeSizeErrorException
	 */
	public void setTreeSize(int size)throws TreeSizeErrorException {
		treeSize = size;
	}


	/**
	 * @return tree size
	 */
	public int getTreeSize() {
		return treeSize;
	}


	/**
	 * @return database primary Key
	 */
	public long getCustomContextPK() {
		return customContextPK;
	}

	/**
	 * @param customContextPK - database Primary Key
	 */
	public void setCustomContextPK(long customContextPK) {
		this.customContextPK = customContextPK;
	}

	/**
	 * @return context ID
	 */
	public String getContextId() {
		return contextId;
	}

	/**
	 * @param contextId
	 */
	public void setContextId(String contextId) {
		this.contextId = contextId;
	}

	/**
	 * @param userProfile
	 */
	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}

	/**
	 * @return hash of subscribed categories
	 */
	public Map<String, CustomManagedCategory> getSubscriptions() {
		return subscriptions;
	}

	/**
	 * @param subscriptions
	 */
	public void setSubscriptions(
			Map<String, CustomManagedCategory> subscriptions) {
		this.subscriptions = subscriptions;
	}

	/**
	 * @return a set of folded categories ID 
	 */
	public Set<String> getUnfoldedCategories() {
		return unfoldedCategories;
	}

	/**
	 * @param foldedCategories - set of olded categories ID 
	 */
	public void setUnfoldedCategories(Set<String> foldedCategories) {
		this.unfoldedCategories = foldedCategories;
	}
}
