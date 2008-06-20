/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
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
import org.esupportail.lecture.exceptions.domain.CategoryNotLoadedException;
import org.esupportail.lecture.exceptions.domain.CategoryNotVisibleException;
import org.esupportail.lecture.exceptions.domain.CategoryObligedException;
import org.esupportail.lecture.exceptions.domain.CategoryOutOfReachException;
import org.esupportail.lecture.exceptions.domain.CategoryProfileNotFoundException;
import org.esupportail.lecture.exceptions.domain.CategoryTimeOutException;
import org.esupportail.lecture.exceptions.domain.ContextNotFoundException;
import org.esupportail.lecture.exceptions.domain.InfoDomainException;
import org.esupportail.lecture.exceptions.domain.InternalDomainException;
import org.esupportail.lecture.exceptions.domain.ManagedCategoryProfileNotFoundException;
import org.esupportail.lecture.exceptions.domain.SourceNotVisibleException;
import org.esupportail.lecture.exceptions.domain.SourceObligedException;
import org.esupportail.lecture.exceptions.domain.SourceProfileNotFoundException;
import org.esupportail.lecture.exceptions.domain.TreeSizeErrorException;

/**
 * Customizations on a context for a user profile.
 * @author gbouteil
 * @see CustomElement
 *
 */
public class CustomContext implements CustomElement {

	/*
	 ************************** PROPERTIES *********************************/	
	
	/**
	 * Max tree size.
	 */
	private static final int MAXTREESIZE = 100;

	/**
	 * Default tree size.
	 */
	private static final int DEFAULTTREESIZE = 20;

	/**
	 * ID string for log.
	 */
	private static final String ID = "id=";

	/**
	 * Log instance.
	 */
	private static final Log LOG = LogFactory.getLog(CustomContext.class);
	
	/**
	 * The context Id of this CustomContext refers to.
	 */
	private String elementId;

	/**
	 * The context of this customization referred to, corresponding to the contextId.
	 */
	private Context context;
	
	/**
	 * Subscriptions of CustomManagedCategory.
	 */
	private Map<String, CustomManagedCategory> subscriptions = new Hashtable<String, CustomManagedCategory>();
	
	/**
	 * The userprofile owner.
	 */
	private UserProfile userProfile;
	
	/**
	 * Tree size of the customContext.
	 */
	private int treeSize;
	
	/**
	 * Set of id corresponding to unfolded categories.
	 */
	private Set<String> unfoldedCategories = new HashSet<String>();

	/**
	 * Database Primary Key.
	 */
	private long customContextPK; 

	
	/*
	 ************************** INIT *********************************/	
	
	/**
	 * Constructor.
	 * @param contextId id of the context refered by this CustomContext
	 * @param user owner of this CustomContext
	 */
	protected CustomContext(final String contextId, final UserProfile user) {
		if (LOG.isDebugEnabled()) {
			LOG.debug(ID + contextId + " - CustomContext(" + contextId + "," + user.getUserId() + ")");
		}
		this.elementId = contextId;
		this.userProfile = user;
		treeSize = DEFAULTTREESIZE;
	}
	
	/**
	 * Constructor.
	 */
	protected CustomContext() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("CustomContext()");
		}
	}
	
	/*
	 *************************** METHODS ************************************/

	/**
	 * Return the list of sorted customCategories displayed by this customContext.
	 * @param ex access to externalService
	 * @return the list of customCategories 
	 * @throws ContextNotFoundException 
	 */
	public List<CustomCategory> getSortedCustomCategories(final ExternalService ex) 
			throws ContextNotFoundException {
		if (LOG.isDebugEnabled()) {
			LOG.debug(ID + elementId + " - getSortedCustomCategories(externalService)");
		}
		// TODO (GB later) rewrite with custom personnal category (+ sorted display)
	
		/* update this customContext with context */
		getContext().updateCustom(this, ex);
		
		List<CustomCategory> listCustomCategories = new Vector<CustomCategory>();
		for (CustomManagedCategory customCat : subscriptions.values()) {
			// later : add other custom elements (imported et personal)
			listCustomCategories.add(customCat);
		}
		return listCustomCategories;
	}
	
	/**
	 * Return a list of "CategoryProfile, AvailabilityMode" corresponding to visible categories for user, 
	 * in this customContext and update it.
	 * @param ex access to external service 
	 * @return list of ProfileAvailability
	 * @throws ContextNotFoundException 
	 */
	public List<ProfileAvailability> getVisibleCategories(ExternalService ex) throws ContextNotFoundException  {
		if (LOG.isDebugEnabled()) {
			LOG.debug(ID + getElementId() + " - getVisibleCategories(ex)");
		}
		// TODO (GB later) à redéfinir avec les custom personnal 
		// category : en fonction de l'ordre d'affichage peut etre.
		Context cxt = getContext();
		List<ProfileVisibility> couplesVisib;
		couplesVisib = cxt.getVisibleCategoriesAndUpdateCustom(this, ex);
			
		List<ProfileAvailability> couplesAvail = new Vector<ProfileAvailability>();
		for (ProfileVisibility coupleV : couplesVisib) {
			// Every couple is not NOTVISIBLE (= visible)
			ProfileAvailability coupleA;
			CategoryProfile categoryProfile = (CategoryProfile) coupleV.getProfile();
			
			if (coupleV.getMode() == VisibilityMode.OBLIGED ) {
				coupleA = new ProfileAvailability(categoryProfile, AvailabilityMode.OBLIGED);
			} else { 
				// It must be ALLOWED OR AUTOSUBSRIBED
				if (subscriptions.containsKey(categoryProfile.getId())) {
					coupleA = new ProfileAvailability(categoryProfile, AvailabilityMode.SUBSCRIBED);
				} else {
					coupleA = new ProfileAvailability(categoryProfile, 
						AvailabilityMode.NOTSUBSCRIBED);
				}
			}
			couplesAvail.add(coupleA);
			LOG.trace("Add category and availability");
		}
		return couplesAvail;
	}
	
	/**
	 * after checking visibility rights, subcribe user to the category categoryId in this CustomContext
	 * @param categoryId category ID
	 * @throws ContextNotFoundException 
	 * @throws ManagedCategoryProfileNotFoundException 
	 * @throws CategoryTimeOutException 
	 * @throws InternalDomainException 
	 * @throws CategoryNotVisibleException 
	 * @throws CategoryOutOfReachException 
	 */
	public void subscribeToCategory(final String categoryId, final ExternalService ex) 
		throws ContextNotFoundException, ManagedCategoryProfileNotFoundException, 
		CategoryTimeOutException, CategoryNotVisibleException, InternalDomainException, 
		CategoryOutOfReachException {	
		if (LOG.isDebugEnabled()) {
			LOG.debug("subscribeToCategory(" + categoryId + ", externalService)");
		}
		context = getContext();
		ManagedCategoryProfile catProfile = null;
		VisibilityMode mode = null;
		try {
			catProfile = context.getCatProfileById(categoryId);
			mode = catProfile.updateCustomContext(this, ex);
		} catch (CategoryNotLoadedException e1) {
			// Dans ce cas : la mise à jour du customContext n'a pas été effectuée
			try {
				userProfile.updateCustomContextsForOneManagedCategory(getElementId(), ex);
				catProfile = context.getCatProfileById(categoryId);
				mode = catProfile.updateCustomContext(this, ex);
			} catch (CategoryNotLoadedException e2) {
				// Dans ce cas : la managedCategory n'est pointé par aucun 
				// context correspondant à des customContext du userProfile => supression ?
				userProfile.removeCustomManagedCategoryIfOrphan(getElementId());
				throw new CategoryOutOfReachException("ManagedCategory " + getElementId()
					+ "is not refered by any customContext in userProfile "
					+ userProfile.getUserId());
			} catch (InfoDomainException e) {
				throw new RuntimeException("Exception in subscribeToCategory", e);
			}
		} catch (InfoDomainException e) {
			throw new RuntimeException("Exception in subscribeToCategory(" + getElementId()
					+ ")", e);
		}

		
		if (mode == VisibilityMode.ALLOWED || mode == VisibilityMode.AUTOSUBSCRIBED) {
			if (subscriptions.containsKey(categoryId)) {
				LOG.warn("Nothing is done for subscribeToCategory requested on category " + categoryId
					+ " in context " + this.getElementId() + "\nfor user " 
					+ getUserProfile().getUserId() 
					+ " because this category is already in subscriptions");
			} else {
				addSubscription(catProfile);
				LOG.debug("addSubscription to category " + categoryId);
			}
			
		} else if (mode == VisibilityMode.OBLIGED) {
			LOG.warn("Nothing is done for subscribeToCategory requested on category " + categoryId
					+ " in context " + this.getElementId() + "\nfor user " 
					+ getUserProfile().getUserId()
					+ " because this category is OBLIGED in this case");
			
		} else if (mode == VisibilityMode.NOVISIBLE) {
			String errorMsg = "subscribeToCategory(" + categoryId
			 	+ ") is impossible because this category is NOT VISIBLE for user "
				+ getUserProfile().getUserId() + "in context " + getElementId();
			LOG.error(errorMsg);
			throw new CategoryNotVisibleException(errorMsg);
		}
		
	}
	
	/**
	 * after checking visibility rights, unsubcribe user to the category categoryId in this CustomContext
	 * @param categoryId category ID
	 * @param externalService
	 * @throws ContextNotFoundException 
	 * @throws ManagedCategoryProfileNotFoundException 
	 * @throws CategoryTimeOutException 
	 * @throws InternalDomainException 
	 * @throws CategoryNotVisibleException 
	 * @throws CategoryOutOfReachException 
	 * @throws CategoryObligedException 
	 */
	public void unsubscribeToCategory(String categoryId, ExternalService externalService) 
		throws ContextNotFoundException, ManagedCategoryProfileNotFoundException, CategoryTimeOutException, 
		CategoryNotVisibleException, InternalDomainException, CategoryOutOfReachException, CategoryObligedException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("subscribeToCategory(" + categoryId + ", externalService)");
		}
		context = getContext();
		ManagedCategoryProfile catProfile = null;
		VisibilityMode mode = null;
		try {
			catProfile = context.getCatProfileById(categoryId);
			mode = catProfile.updateCustomContext(this, externalService);
		} catch (CategoryNotLoadedException e1) {
			// Dans ce cas : la mise à jour du customContext n'a pas été effectuée
			try {
				userProfile.updateCustomContextsForOneManagedCategory(getElementId(), externalService);
				catProfile = context.getCatProfileById(categoryId);
				mode = catProfile.updateCustomContext(this, externalService);
			} catch (CategoryNotLoadedException e2) {
				// Dans ce cas : la managedCategory n'est pointé par aucun 
				// context correspondant à des customContext du userProfile => supression ?
				userProfile.removeCustomManagedCategoryIfOrphan(getElementId());
				throw new CategoryOutOfReachException("ManagedCategory " + getElementId()
					+ "is not refered by any customContext in userProfile "
					+ userProfile.getUserId());
			} catch (InfoDomainException e) {
				throw new RuntimeException("Exception in unsubscribeToCategory", e);
			}
		} catch (InfoDomainException e) {
			throw new RuntimeException("Exception in unsubscribeToCategory", e);
		}
		
		if (mode == VisibilityMode.ALLOWED || mode == VisibilityMode.AUTOSUBSCRIBED) {
			if (!subscriptions.containsKey(categoryId)) {
				LOG.warn("Nothing is done for unsubscribeToCategory requested on category " + categoryId
					+ " in context " + this.getElementId() + "\nfor user " 
					+ getUserProfile().getUserId() 
					+ " because this category is not in subscriptions");
			} else {
				removeCustomManagedCategoryFromProfile(categoryId);
				LOG.trace("removeCustomManagedSource to source " + categoryId);
			}
			
		} else if (mode == VisibilityMode.OBLIGED) {
			String errorMsg = "UnsubscribeToCategory(" + categoryId
				+ ") is impossible because this category is OBLIGED for user "
				+ getUserProfile().getUserId() + "in context " + getElementId();
			LOG.error(errorMsg);
			throw new CategoryObligedException(errorMsg);			
			
		} else if (mode == VisibilityMode.NOVISIBLE) {
			LOG.warn("Nothing is done for unsubscribeToCategory requested on category "
				+ categoryId + " in category " + this.getElementId() + "\nfor user " 
				+ getUserProfile().getUserId()
				+ " because this category is NOVISIBLE in this case");
		}
	}
	
	
	
	/* ADD ELEMENTS */

	/**
	 * Add a subscription category to this custom context (if no exists, else do nothing). 
	 * and creates the corresponding customManagedCategory with the given profile
	 * This new customManagedCategory is also added to the userProfile (owner of all customElements)
	 * @param profile the managed category profile to subscribe
	 */
	protected void addSubscription(final ManagedCategoryProfile profile) {
		if (LOG.isDebugEnabled()) {
			LOG.debug(ID + elementId + " - addSubscription(" + profile.getId() + ")");
		}
		String profileId = profile.getId();
		
		if (!subscriptions.containsKey(profileId)) {
			CustomManagedCategory customManagedCategory = new CustomManagedCategory(profileId, userProfile);
			subscriptions.put(profileId, customManagedCategory);
//			DomainTools.getDaoService().updateCustomContext(this);
			userProfile.addCustomCategory(customManagedCategory);
		}
	}
	// TODO (GB later) addImportation(), addCreation())
	
	/* REMOVE ELEMENTS */
	
	/**
	 * remove a CustomManagedCategory displayed in this CustomContext.
	 * and also removes every occcurence in userProfile
	 * @param profileId the managedCategoryProfile ID associated to the CustomManagedCategory to remove
	 */
	protected void removeCustomManagedCategoryFromProfile(final String profileId) {
		if (LOG.isDebugEnabled()) {
			LOG.debug(ID + elementId + " - removeCustomManagedSourceFromProfile(" + profileId + ")");
		}
		getUserProfile().removeCustomManagedCategoryFromProfile(profileId);
		
	}
	// TODO (GB later)  removeCustomPersonalCategory()
	
	/**
	 * Remove the customCategory categoryId in ths customContext only.
	 * @param categoryId ID for customCategory
	 */
	public void removeCustomCategory(final String categoryId) {
		if (LOG.isDebugEnabled()) {
			LOG.debug(ID + elementId + " - removeCustomCategory(" + categoryId + ")");
		}
		removeCustomManagedCategory(categoryId);
	}
	
	/**
	 * Remove the customManagedCategory categoryId in this customContext only.
	 * @param categoryId ID for customManagedCategory
	 */
	public void removeCustomManagedCategory(final String categoryId) {
		if (LOG.isDebugEnabled()) {
			LOG.debug(ID + elementId + " - removeCustomManagedCategory(" + categoryId + ")");
		}
		CustomCategory cs = subscriptions.get(categoryId);
		if (cs != null) {
			subscriptions.remove(categoryId);
//			DomainTools.getDaoService().updateCustomContext(this);
		}
	}
	
	/**
	 * Remove every subscriptions (customManagedCategories) of this customContext.
	 */
	public void removeSubscriptions() {
		if (LOG.isDebugEnabled()) {
			LOG.debug(ID + elementId + " - removeSubscriptions()");
		}
		
		for (String sourceId : subscriptions.keySet()) {
			removeCustomManagedCategoryFromProfile(sourceId);
		}
		
	}
	
	/* MISCELLANEOUS */
	
	/**
	 * Returns the Context associated to this customContext.
	 * @return context 
	 * @throws ContextNotFoundException 
	 */
	public Context getContext() throws ContextNotFoundException {
		if (LOG.isDebugEnabled()) {
			LOG.debug(ID + elementId + " - getContext()");
		}
		if (context == null) {
			context = DomainTools.getChannel().getContext(elementId);
		}
		return context;
	}
	
	/**
	 * Returns the name of this context.
	 * @throws ContextNotFoundException 
	 * @see org.esupportail.lecture.domain.model.CustomElement#getName()
	 */
	public String getName() throws ContextNotFoundException {
		if (LOG.isDebugEnabled()) {
			LOG.debug(ID + elementId + " - getName()");
		}
		return getContext().getName();
	}
	
	/**
	 * Modify the tree size of this customContext by checking.
	 * min and max treeSize
	 * @param size
	 * @throws TreeSizeErrorException
	 */
	public void modifyTreeSize(final int size)throws TreeSizeErrorException {
		if (LOG.isDebugEnabled()) {
			LOG.debug(ID + elementId + " - modifyTreeSize(size " + size + ")");
		}
		/* old name was setTreesize but it has been changed to prevent 
		 * loop by calling dao
		 */
		// TODO (GB later) externaliser les bornes
		if ((size >= 0) && (size <= MAXTREESIZE)) {
			treeSize = size;
//			DomainTools.getDaoService().updateCustomContext(this);
		} else {
			String errorMsg = "TreeSize must be into 0 and 100";
			LOG.error(errorMsg);
			throw new TreeSizeErrorException(errorMsg);
		}
	}

	/**
	 * mark a customCategory contained in this CustomContext as folded.
	 * @param catId id of the profile category associated to the customCategory
	 */
	public void foldCategory(final String catId) {
		if (LOG.isDebugEnabled()) {
			LOG.debug(ID + elementId + " - foldCategory(catId" + catId + ")");
		}
		if (!unfoldedCategories.remove(catId)) {
			LOG.warn("foldCategory(" + catId + ") is called in customContext " + elementId 
					+ " but this category is yet folded");
//		} else {
//			DomainTools.getDaoService().updateCustomContext(this);
		}
	}
	
	/**
	 * mark a customCategory contained in this CustomContext as unfolded.
	 * @param catId id of the profile category associated to the customCategory
	 */
	public void unfoldCategory(final String catId) {
		if (LOG.isDebugEnabled()) {
			LOG.debug(ID + elementId + " - unfoldCategory(catId" + catId + ")");
		}
		if (!unfoldedCategories.add(catId)) {
			LOG.warn("unfoldCategory(" + catId + ") is called in customContext " + elementId
					+ " but this category is yet unfolded");
//		} else {
//			DomainTools.getDaoService().updateCustomContext(this);
		}
	
	}

	/**
	 * @param categoryId ID for customManagedCategory
	 * @return true if this customContext has a reference on customManagedCategory categoryId
	 */
	public boolean containsCustomManagedCategory(final String categoryId) {
		if (LOG.isDebugEnabled()) {
			LOG.debug(ID + elementId + " - containsCustomManagedCategory(" + categoryId+")");
		}
		return subscriptions.containsKey(categoryId);
		
	}
	
	/**
	 * @param categoryId ID for customCategory
	 * @return true if this customContext has a reference on customCategory categoryId
	 */
	public boolean containsCustomCategory(final String categoryId) {
		if (LOG.isDebugEnabled()) {
			LOG.debug(ID + elementId + " - containsCustomCategory(" + categoryId + ")");
		}
		return containsCustomManagedCategory(categoryId);
		
	}
	
	/**
	 * Return true if the customCategory is folded in this customContext.
	 * @param catId
	 * @return if category is folded or not
	 */
	public boolean isCategoryFolded(final String catId) {
		if (LOG.isDebugEnabled()) {
			LOG.debug(ID + elementId + " - isCategoryFolded(catId" + catId + ")");
		}
		boolean ret = false;
		if (unfoldedCategories.contains(catId)) {
			ret = false;
		} else {
			ret = true;
		}
		return ret;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null) {
			return false;
		}
		if (!(o instanceof CustomContext)) {
			return false;
		}
		final CustomContext customContext = (CustomContext) o;
		if (!customContext.getElementId().equals(this.getElementId())) {
			return false;
		}
		return true;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return this.getElementId().hashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer string = new StringBuffer(getClass().getSimpleName() + "#" + hashCode() 
				+ "[elementId=[" + elementId
				+ "], customContextPK=[" + customContextPK 
				+ "], treeSize=[" + treeSize 
				+ "], userProfile.id=[" + userProfile.getUserId() + "],");
		// subscriptions
		string.append("\n subscriptions=[");
		for (String key : subscriptions.keySet()) {
			string.append(key).append(", ");
		}
		// unfolded categories
		string.append("\n unfolded categories=[");
		for (String cat : unfoldedCategories) {
			string.append(cat).append(", ");
		}
		string.append("]\n]");
		return string.toString();
	}
	
	/* 
	 ************************** ACCESSORS **********************************/

	/**
	 * @return contextId
	 */
	public String getElementId() {
		return elementId;
	}

	/**
	 * @return userProfile
	 */
	public UserProfile getUserProfile() {
		return userProfile;
	}

	/**
	 * @param size
	 */
	protected void setTreeSize(final int size) {
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
	public void setCustomContextPK(final long customContextPK) {
		this.customContextPK = customContextPK;
	}

	/**
	 * @param userProfile
	 */
	protected void setUserProfile(final UserProfile userProfile) {
		this.userProfile = userProfile;
	}

	/**
	 * @return hash of subscribed categories
	 */
	protected Map<String, CustomManagedCategory> getSubscriptions() {
		return subscriptions;
	}

	/**
	 * @return a set of folded categories ID. 
	 */
	@SuppressWarnings("unused")
	private Set<String> getUnfoldedCategories() {
		return unfoldedCategories;
		//Needed by Hibernate
	}

	/**
	 * @param foldedCategories - set of olded categories ID 
	 */
	@SuppressWarnings("unused")
	private void setUnfoldedCategories(final Set<String> foldedCategories) {
		this.unfoldedCategories = foldedCategories;
		//Needed by Hibernate
	}

	/**
	 * @param elementId
	 */
	public void setElementId(final String elementId) {
		this.elementId = elementId;
	}

	/**
	 * @param subscriptions the subscriptions to set
	 */
	public void setSubscriptions(final Map<String, CustomManagedCategory> subscriptions) {
		this.subscriptions = subscriptions;
		//Needed by Hibernate
	}

	

}
