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

/**
 * Managed category profile element.
 * @author gbouteil
 * @see org.esupportail.lecture.domain.model.CategoryProfile
 * @see org.esupportail.lecture.domain.model.ManagedComposantProfile
 *
 */
public class ManagedCategoryProfile extends CategoryProfile implements ManagedComposantProfile {

	/*
	 ************************** PROPERTIES ******************************** */	
	/**
	 * Log instance 
	 */
	protected static final Log log = LogFactory.getLog(ManagedCategoryProfile.class); 

	/**
	 * URL of the remote managed category
	 */
	private String urlCategory;
	
	/**
	 * Access mode on the remote managed category
	 */
	private Accessibility access;
	
	/**
	 * trustCategory parameter : indicates between managed category and category profile, which one to trust
	 * True : category is trusted. 
	 * False : category is not trusted, only parameters profile are good 
	 * parameters (edit, visibility, ttl)
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
	 * Using depends on trustCategory parameter
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
		computedFeatures = new ComputedManagedCategoryFeatures(this);
	}
	
	/*
	 ************************** METHODS ******************************** */	
	
	

//	/**
//	 * @param customContext
//	 * @param externalService
//	 * @return
//	 */
	public boolean updateCustomContext(CustomContext customContext,ExternalService externalService){
		
		loadCategory(externalService);
		return setUpCustomContextVisibility(externalService, customContext);
		
	}

	private void loadCategory(ExternalService externalService) {
		if(getAccess() == Accessibility.PUBLIC) {
			setCategory(DomainTools.getDaoService().getManagedCategory(this)); 
			
		} else if (getAccess() == Accessibility.CAS) {
			String ptCas = externalService.getUserProxyTicketCAS();
			setCategory(DomainTools.getDaoService().getManagedCategory(this,ptCas));
		}
		computedFeatures.compute();
	}
	
	/**
	 * Evaluate visibility of current user for this managed category.
	 * Update customContext (belongs to user) if needed :
	 * add or remove customCategories associated with
	 * @param externalService
	 * @param customContext
	 * @return true if the mcp is visible by the user of the customContext, else return false
	 */
	private boolean setUpCustomContextVisibility(ExternalService externalService, CustomContext customContext) {
		/*
		 * Algo pour gerer les customCategories :
		 * ------------------------------------
		 * user app. obliged => enregistrer la cat dans le user profile + sortir
		 * user app. autoSub => enregistrer la cat dans le user profile si c'est la première fois + sortir
		 * user app.allowed => rien à faire + sortir
		 * user n'app. rien => effacer la cat.
		 */

		boolean isInObliged = false;
		boolean isInAutoSubscribed = false;
		boolean isInAllowed = false;
		
		
	/* ---OBLIGED SET--- */
		log.debug("Appel de evaluate sur DefenitionSets(obliged) de la cat : "+ getName());
		isInObliged =  getVisibilityObliged().evaluateVisibility(externalService);
		log.debug("IsInObliged : "+isInObliged);
		if (isInObliged) {
			customContext.addManagedCustomCategory(this);
		
		} else {
	/* ---AUTOSUBSCRIBED SET--- */	
			// TODO (later) isInAutoSubscribed =  getVisibilityAutoSubscribed().evaluateVisibility(portletService);
			// en attendant : isInAutoSubscribed = false 			
			if(isInAutoSubscribed) {
				// TODO (later) l'ajouter dans le custom context si c'est la preniere fois
				// customContext.addCustomCategory(mcp);
			
			} else {
	/* ---ALLOWED SET--- */
				log.debug("Appel de evaluate sur DefenitionSets(allowed) de la cat : "+ getName());
				isInAllowed =  getVisibilityAllowed().evaluateVisibility(externalService);
				
				if (!isInAllowed) { // If isInAllowed : nothing to do
	/* ---CATEGORY NOT VISIBLE FOR USER--- */
					customContext.removeManagedCustomCategory(this);
					return false;
				}			
			}	
		}
		// TODO (later) retirer les customCat du user profile qui correspondent à des profiles 
		// de catégories  disparus
		return true;
	}
	
		
	/**
	 * Evaluate user visibility on managed source profiles of this managed category 
	 * And update the customManagedCategory associated with, according to visibilities
	 * But there is not any loading of source at this time
	 * @param customManagedCategory customManagedCAtegory to update
	 * @param portletService Access to portlet service
	 */
	public void updateCustom(CustomManagedCategory customManagedCategory,ExternalService externalService) {
		ManagedCategory category = (ManagedCategory) getCategory();
		category.updateCustom(customManagedCategory, externalService);
	}
	
	
	/**
	 * @see org.esupportail.lecture.domain.model.ManagedComposantProfile#computeActiveFeatures()
	 */
	public void computeFeatures() {
		
		ManagedCategory managedCategory = (ManagedCategory)super.getCategory();
		//Editability setEdit;
		VisibilitySets setVisib = visibility;
		int setTtl = ttl;
		
		if (trustCategory) {		
			//setEdit = managedCategory.getEdit();
			setVisib = managedCategory.getVisibility();
			setTtl = managedCategory.getTtl();
			
//			if (setEdit == null) {
//				setEdit = this.edit;
//			}
			if (setVisib == null) {
				setVisib = this.visibility;
			}
			if (setTtl == -1) {
				setTtl = this.ttl;
			}	
			
		}/* else {
				Already done during channel config loading 
		} */
		computedFeatures.update(setVisib,setTtl);
	}
	

	/*
	 *************************** ACCESSORS ******************************** */	


	/**
	 * Returns the URL of the remote managed category
	 * @return urlCategory
	 * @see ManagedCategoryProfile#urlCategory
	 */
	public String getUrlCategory() {
		return urlCategory;
	}
	
	/** 
	 * Sets the URL of the remote managed category
	 * @param urlCategory the URL to set
	 * @see ManagedCategoryProfile#urlCategory
	 */
	public void setUrlCategory(String urlCategory) {
		this.urlCategory = urlCategory;
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
	protected void setTrustCategory(boolean trustCategory) {
		this.trustCategory = trustCategory;
	}


	/**
	 * @return access
	 * @see ManagedCategoryProfile#access
	 * @see org.esupportail.lecture.domain.model.ManagedComposantProfile#getAccess()
	 */
	public Accessibility getAccess() {
		return access;
	}
	
	/**
	 * @see ManagedCategoryProfile#access
	 * @see org.esupportail.lecture.domain.model.ManagedComposantProfile#setAccess(org.esupportail.lecture.domain.model.Accessibility)
	 */
	public void setAccess(Accessibility access) {
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
	 * @return Visibility
	 * @see ManagedCategoryProfile#visibility
	 * @see org.esupportail.lecture.domain.model.ManagedComposantProfile#getVisibility()
	 */
	public VisibilitySets getVisibility() {
		return computedFeatures.getVisibility();
	}
	
	/**
	 * @see ManagedCategoryProfile#visibility
	 * @see org.esupportail.lecture.domain.model.ManagedComposantProfile#setVisibility(org.esupportail.lecture.domain.model.VisibilitySets)
	 */
	public void setVisibility(VisibilitySets visibility) {
		this.visibility = visibility;
		computedFeatures.setIsComputed(false);
	}

	/**
	 * @return allowed visibility group 
	 * @see org.esupportail.lecture.domain.model.ManagedComposantProfile#getVisibilityAllowed()
	 */
	public DefinitionSets getVisibilityAllowed() {
		return getVisibility().getAllowed();
	}
	
	/**
	 * @see org.esupportail.lecture.domain.model.ManagedComposantProfile#setVisibilityAllowed(org.esupportail.lecture.domain.model.DefinitionSets)
	 */
	public void setVisibilityAllowed(DefinitionSets d) {
		this.visibility.setAllowed(d);
		computedFeatures.setIsComputed(false);
	}

	/** 
	 * @return autoSubscribed group visibility
	 * @see org.esupportail.lecture.domain.model.ManagedComposantProfile#getVisibilityAutoSubscribed()
	 */
	public DefinitionSets getVisibilityAutoSubscribed() {
		return getVisibility().getAutoSubscribed();
	}
	
	/**
	 * @see org.esupportail.lecture.domain.model.ManagedComposantProfile#setVisibilityAutoSubcribed(org.esupportail.lecture.domain.model.DefinitionSets)
	 */
	public void setVisibilityAutoSubcribed(DefinitionSets d) {
		this.visibility.setAutoSubscribed(d);
		computedFeatures.setIsComputed(false);
	}
	
	/**
	 * @return obliged group visibility
	 * @see org.esupportail.lecture.domain.model.ManagedComposantProfile#getVisibilityObliged()
	 */
	public DefinitionSets getVisibilityObliged() {
		return getVisibility().getObliged();
		
	}
	
	/**
	 * @see org.esupportail.lecture.domain.model.ManagedComposantProfile#setVisibilityObliged(org.esupportail.lecture.domain.model.DefinitionSets)
	 */
	public void setVisibilityObliged(DefinitionSets d) {
		this.visibility.setObliged(d);
		computedFeatures.setIsComputed(false);
	}
	
	/**
	 * Returns ttl
	 * @see ManagedCategoryProfile#ttl
	 * @see org.esupportail.lecture.domain.model.ManagedComposantProfile#getTtl()
	 */
	public int getTtl() {
		return computedFeatures.getTtl();
	}
	
	/**
	 * @see ManagedCategoryProfile#ttl
	 * @see org.esupportail.lecture.domain.model.ManagedComposantProfile#setTtl(int)
	 */
	public void setTtl(int ttl) {
		this.ttl = ttl;
		computedFeatures.setIsComputed(false);
	}


	/**
	 * Add a context to the set of context in this managed category profile
	 * @param c context to add
	 * @see ManagedCategoryProfile#contextsSet
	 */
	protected void addContext(Context c){
		contextsSet.add(c);
	}

	


	
	



}
