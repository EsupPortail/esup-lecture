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
import org.esupportail.lecture.domain.service.PortletService;
import org.esupportail.lecture.utils.exception.ErrorException;


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
	 * Used for tests
	 */
	public String test = "CustomCharge";
	
	
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
	
	/**
	 * Selected element to be display on right part window
	 */
	// TODO mettre dans la BDD ?
	private CustomElement selectedElement;
	
	
	/*
	 ************************** INIT *********************************/	

	
	/**
	 * Constructor
	 */
	public CustomContext() {
		subscriptions = new Hashtable<String,CustomManagedCategory>();
		selectedElement = this;
	}
	
	/*
	 *************************** METHODS ************************************/

	public List<ManagedCategoryProfile> getVisibleManagedCategoryProfile(PortletService portletService) {
		
		return getContext().loadAndEvaluateVisibilityOnManagedCategoriesToUpdate(this,portletService);
		
	}

	
	
	
	/** Update data contains in this customContext :
	 *  - evaluation visibilty on managedCategories to update list of customManagedCategories
	 * @param portletService
	 */
	public void updateData(PortletService portletService) {
		
		getContext().loadAndEvaluateVisibilityOnManagedCategoriesToUpdate(this,portletService);
		// later :  Personnal Categories
		
		Iterator iterator = subscriptions.values().iterator();
		while(iterator.hasNext()){
			CustomManagedCategory customManagedCategory = (CustomManagedCategory)iterator.next();
			customManagedCategory.updateData(portletService);
		}	
	}
	
	
	/**
	 * Returns the customCategories of this customContext
	 * @return an Enumeration of the customCategories
	 */
	public List<CustomCategory> getSortedCustomCategories(){
		// TODO à redéfinir avec les custom personnal category : en fonction de l'ordre d'affichage peut etre.
		
		List<CustomCategory> listCategories = new Vector<CustomCategory>();
		Iterator iterator = subscriptions.values().iterator();
		
		while(iterator.hasNext()){
			CustomCategory customCategory = (CustomCategory)iterator.next();
			log.debug("CUSTOMCONTEXT CAST PB :"+ customCategory.getClass()+ "Nom : "+customCategory.getId());
			listCategories.add(customCategory);
		}
		
		return listCategories;
	}
	
	
	/**
	 * Add a custom category to this custom context if no exists after creating it.
	 * @param profile the managed category profile associated to the customCategory
	 */
	public void addManagedCustomCategory(ManagedCategoryProfile profile) {
		String profileId = profile.getId();
		
		if (!subscriptions.containsKey(profileId)){
			CustomManagedCategory customManagedCategory = new CustomManagedCategory();
			//customManagedCategory.setCategoryProfile(profile);
			customManagedCategory.setCategoryProfileID(profileId);
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

	/**
	 * @return Returns the selectedElement.
	 */
	public CustomElement getSelectedElement() {
		return selectedElement;
	}

	/**
	 * @param selectedElement The selectedElement to set.
	 */
	public void setSelectedElement(CustomElement selectedElement) {
		this.selectedElement = selectedElement;
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
