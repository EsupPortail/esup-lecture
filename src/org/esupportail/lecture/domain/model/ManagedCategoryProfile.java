/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;


import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.DomainTools;
import org.esupportail.lecture.domain.ExternalService;
import org.esupportail.lecture.exceptions.domain.CategoryNotLoadedException;
import org.esupportail.lecture.exceptions.domain.ElementNotLoadedException;

/**
 * Managed category profile element.
 * @author gbouteil
 * @see org.esupportail.lecture.domain.model.CategoryProfile
 * @see org.esupportail.lecture.domain.model.ManagedElementProfile
 *
 */
public class ManagedCategoryProfile extends CategoryProfile implements ManagedElementProfile {

	/*
	 ************************** PROPERTIES ******************************** */	
	/**
	 * Log instance 
	 */
	protected static final Log log = LogFactory.getLog(ManagedCategoryProfile.class); 

	/**
	 * URL of the remote managed category
	 */
	private String categoryURL;
	
	/**
	 * Access mode on the remote managed category
	 */
	private Accessibility access;
	
	/**
	 * trustCategory parameter : indicates between managed category and category profile, which one to trust
	 * True : category is trusted. 
	 * False : category is not trusted, only parameters profile are good 
	 * parameters (edit, visibility)
	 */
	private boolean trustCategory;
	
	/**
	 * Resolve feature values (edit, visibility,tll) from :
	 * - managedCategoryProfile features
	 * - managedCategory features
	 * - trustCategory parameter 
	 */
	private ComputedManagedCategoryFeatures computedFeatures;
		
	// Later
//	/**
//	 * Remote managed category edit mode : not used for the moment
//	 * Using depends on trustCategory parameter
//	 */	
//	private Editability edit;
	
	/**
	 * Visibility rights for groups on the remote managed category
	 * Using depends on trustCategory parameter
	 */
	private VisibilitySets visibility;

	/**
	 * Ttl of the remote managed category reloading
	 */
	private int ttl;
	

	/**
	 * Contexts where these profiles category are referenced
	 */
	private Set<Context> contextsSet = new HashSet<Context>();


	
	
	/*
	 ************************** INITIALIZATION ******************************** */	
	
	
	/**
	 * Constructor 
	 */
	public ManagedCategoryProfile() {
		if (log.isDebugEnabled()){
			log.debug("ManagedCategoryProfile()");
		}
		computedFeatures = new ComputedManagedCategoryFeatures(this);
	}
	
	/*
	 ************************** METHODS ******************************** */	
	
	

	/**
	 * @param customContext
	 * @param ex
	 * @return true if the category is visble by the userProfile
	 */
	synchronized public boolean updateCustomContext(CustomContext customContext,ExternalService ex) 
		throws ElementNotLoadedException, CategoryNotLoadedException {
		if (log.isDebugEnabled()){
			log.debug("updateCustomContext("+customContext.getElementId()+"externalService)");
		}
		loadCategory(ex);
		return setUpCustomContextVisibility(customContext, ex);
		
	}

	@Override
	synchronized protected void loadCategory(ExternalService ex) throws CategoryNotLoadedException {
		if (log.isDebugEnabled()){
			log.debug("loadCategory(externalService)");
		}
		if(getAccess() == Accessibility.PUBLIC) {
			setElement(DomainTools.getDaoService().getManagedCategory(this)); 
			
		} else if (getAccess() == Accessibility.CAS) {
			String ptCas = ex.getUserProxyTicketCAS();
			setElement(DomainTools.getDaoService().getManagedCategory(this,ptCas));
		}
		//computedFeatures.compute();
	}
	
	/**
	 * Evaluate visibility of current user for this managed category.
	 * Update customContext (belongs to user) if needed :
	 * add or remove customCategories associated with
	 * @param ex
	 * @param customContext
	 * @return true if the mcp is visible by the user of the customContext, else return false
	 * @throws ElementNotLoadedException 
	 */
	synchronized private boolean setUpCustomContextVisibility(CustomContext customContext, ExternalService ex) 
		throws ElementNotLoadedException {
		if (log.isDebugEnabled()){
			log.debug("setUpCustomContextVisibility("+customContext.getElementId()+",externalService)");
		}
		/*
		 * Algo pour gerer les customCategories :
		 * ------------------------------------
		 * user app. obliged => enregistrer la cat dans le user profile + sortir
		 * user app. autoSub => enregistrer la cat dans le user profile si c'est la première fois + sortir
		 * user app.allowed => rien à faire + sortir
		 * user n'app. rien => effacer la cat.
		 */

		VisibilityMode mode = getVisibility().whichVisibility(ex);
		
		if (mode == VisibilityMode.OBLIGED){
			if (log.isTraceEnabled()){
				log.trace("IsInObliged : "+mode);
			}
			customContext.addSubscription(this);
			return true;
		}
		
		if (mode == VisibilityMode.AUTOSUBSCRIBED){
			if (log.isTraceEnabled()){
				log.trace("IsInAutoSubscribed : "+mode);
			}
			// TODO (GB later) l'ajouter dans le custom context si c'est la premiere fois
			//customContext.addSubscription(this);
			return true;
		}
		
		if (mode == VisibilityMode.ALLOWED) {
			if (log.isTraceEnabled()){
				log.trace("IsInAllowed : "+mode);
			}
			// Nothing to do
			return true;
		} 
		// TODO (GB later) retirer les customCat du user profile qui correspondent à des profiles 
		// de catégories  disparus
		
		// ELSE not Visible
		customContext.removeCustomManagedCategory(this);
		return false;
		
//		boolean isInObliged = false;
//		boolean isInAutoSubscribed = false;
//		boolean isInAllowed = false;
//		
//		boolean isVisible = false;
//		
//		
//	/* ---OBLIGED SET--- */
//	//	log.trace("Appel de evaluate sur DefenitionSets(obliged) de la cat : "+ getId());
//		isInObliged =  getVisibilityObliged().evaluateVisibility(ex);
//	//	log.debug("IsInObliged : "+isInObliged);
//		if (isInObliged) {
//			customContext.addSubscription(this);
//			isVisible = true;
//		
//		} else {
//	/* ---AUTOSUBSCRIBED SET--- */
//			// en attendant : isInAutoSubscribed = false 			
//			if(isInAutoSubscribed) {
//				
//				// customContext.addCustomCategory(mcp);
//				// isVisible = true;
//			
//			} else {
//	/* ---ALLOWED SET--- */
//				log.debug("Appel de evaluate sur DefenitionSets(allowed) de la cat : "+ getId());
//				isInAllowed =  getVisibilityAllowed().evaluateVisibility(ex);
//				isVisible = true;
//				
//				if (!isInAllowed) { // If isInAllowed : nothing to do
//	/* ---CATEGORY NOT VISIBLE FOR USER--- */
//					customContext.removeCustomManagedCategory(this);
//					isVisible = false;
//				}			
//			}	
//		}
//		return isVisible;
	}
	
		
	/**
	 * Evaluate user visibility on managed source profiles of this managed category 
	 * And update the customManagedCategory associated with, according to visibilities
	 * But there is not any loading of source at this time
	 * @param customManagedCategory customManagedCAtegory to update
	 * @param portletService Access to portlet service
	 * @throws ElementNotLoadedException 
	 * @throws ElementNotLoadedException 
	 */
	synchronized public void updateCustom(CustomManagedCategory customManagedCategory,ExternalService ex) 
		throws ElementNotLoadedException {
		if (log.isDebugEnabled()){
			log.debug("updateCustom("+customManagedCategory.getElementId()+",externalService)");
		}
		ManagedCategory category = (ManagedCategory) getElement();
		category.updateCustom(customManagedCategory, ex);
	}
	
	
	/**
	 * @throws CategoryNotLoadedException 
	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#computeActiveFeatures()
	 */
	synchronized public void computeFeatures() throws CategoryNotLoadedException {
		if (log.isDebugEnabled()){
			log.debug("computeFeatures()");
		}
		
		ManagedCategory managedCategory = (ManagedCategory)getElement();
		//Editability setEdit;
		VisibilitySets setVisib = visibility;
		
		if (trustCategory) {		
			//setEdit = managedCategory.getEdit();
			setVisib = managedCategory.getVisibility();
			
//			if (setEdit == null) {
//				setEdit = this.edit;
//			}
			if (setVisib == null) {
				setVisib = this.visibility;
			}

			
		}/* else {
				Already done during channel config loading 
		} */
		computedFeatures.update(setVisib);
	}

	/**
	 * @return Visibility
	 * @throws ElementNotLoadedException 
	 * @see ManagedCategoryProfile#visibility
	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#getVisibility()
	 */
	public VisibilitySets getVisibility() throws ElementNotLoadedException {
		if (log.isDebugEnabled()){
			log.debug("getVisibility()");
		}
		return computedFeatures.getVisibility();
	}
	
	/**
	 * @see ManagedCategoryProfile#visibility
	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#setVisibility(org.esupportail.lecture.domain.model.VisibilitySets)
	 */
	synchronized public void setVisibility(VisibilitySets visibility) {
		if (log.isDebugEnabled()){
			log.debug("setVisibility(visibility)");
		}
		this.visibility = visibility;
		computedFeatures.setIsComputed(false);
	}
	
	/**
	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#setVisibilityAllowed(org.esupportail.lecture.domain.model.DefinitionSets)
	 */
	synchronized public void setVisibilityAllowed(DefinitionSets d) {
		if (log.isDebugEnabled()){
			log.debug("setVisibilityAllowed(definitionSets)");
		}
		this.visibility.setAllowed(d);
		computedFeatures.setIsComputed(false);
	}
	
	/**
	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#setVisibilityAutoSubcribed(org.esupportail.lecture.domain.model.DefinitionSets)
	 */
	synchronized public void setVisibilityAutoSubcribed(DefinitionSets d) {
		if (log.isDebugEnabled()){
			log.debug("setVisibilityAutoSubcribed(definitionSets)");
		}
		this.visibility.setAutoSubscribed(d);
		computedFeatures.setIsComputed(false);
	}
	
	/**
	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#setVisibilityObliged(org.esupportail.lecture.domain.model.DefinitionSets)
	 */
	synchronized public void setVisibilityObliged(DefinitionSets d) {
		if (log.isDebugEnabled()){
			log.debug("setVisibilityObliged(definitionSets)");
		}
		this.visibility.setObliged(d);
		computedFeatures.setIsComputed(false);
	}
	

	/**
	 * Add a context to the set of context in this managed category profile
	 * @param c context to add
	 * @see ManagedCategoryProfile#contextsSet
	 */
	synchronized protected void addContext(Context c){
		if (log.isDebugEnabled()){
			log.debug("addContext("+c.getId()+")");
		}
		contextsSet.add(c);
	}
	
	/*
	 *************************** ACCESSORS ******************************** */	


	/**
	 * Returns the URL of the remote managed category
	 * @return urlCategory
	 * @see ManagedCategoryProfile#urlCategory
	 */
	public String getUrlCategory() {
		return categoryURL;
	}
	
	/** 
	 * Sets the URL of the remote managed category
	 * @param urlCategory the URL to set
	 * @see ManagedCategoryProfile#urlCategory
	 */
	synchronized public void setUrlCategory(String categoryURL) {
		this.categoryURL = categoryURL;
	}

	/**
	 * Returns the state (true or false) of the trust category parameter
	 * @return trustCategory
	 * @see ManagedCategoryProfile#trustCategory
	 */
	protected boolean getTrustCategory() {
		return trustCategory;
	}
	
	/**
	 * Sets the trust category parameter
	 * @param trustCategory 
	 * @see ManagedCategoryProfile#trustCategory
	 */
	synchronized protected void setTrustCategory(boolean trustCategory) {
		this.trustCategory = trustCategory;
	}


	/**
	 * @return access
	 * @see ManagedCategoryProfile#access
	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#getAccess()
	 */
	public Accessibility getAccess() {
		return access;
	}
	
	/**
	 * @see ManagedCategoryProfile#access
	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#setAccess(org.esupportail.lecture.domain.model.Accessibility)
	 */
	synchronized public void setAccess(Accessibility access) {
		this.access = access;
	}
	
	// utile plus tard
//	protected Editability getEdit() {
//		return computedFeatures.getEdit();
//	}
//	protected void setEdit(Editability edit) {
//		this.edit = edit;
//	computedFeatures.setComputed(false);
//	}
	
	

	/**
	 * @return allowed visibility group 
	 * @throws ElementNotLoadedException 
	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#getVisibilityAllowed()
	 */
	public DefinitionSets getVisibilityAllowed() throws ElementNotLoadedException {
		return getVisibility().getAllowed();
	}


	/** 
	 * @return autoSubscribed group visibility
	 * @throws ElementNotLoadedException 
	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#getVisibilityAutoSubscribed()
	 */
	public DefinitionSets getVisibilityAutoSubscribed() throws ElementNotLoadedException {
		return getVisibility().getAutoSubscribed();
	}

	
	/**
	 * @return obliged group visibility
	 * @throws ElementNotLoadedException 
	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#getVisibilityObliged()
	 */
	public DefinitionSets getVisibilityObliged() throws ElementNotLoadedException {
		return getVisibility().getObliged();
		
	}

	/**
	 * Returns ttl
	 * @throws ElementNotLoadedException 
	 * @see ManagedCategoryProfile#ttl
	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#getTtl()
	 */
	public int getTtl(){
		return ttl;
	}
	
	/**
	 * @see ManagedCategoryProfile#ttl
	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#setTtl(int)
	 */
	synchronized public void setTtl(int ttl) {
		this.ttl = ttl;
	}

	protected Set<Context> getContextsSet() {
		return contextsSet;
	}



}
