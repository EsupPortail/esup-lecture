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
import org.esupportail.lecture.exceptions.dao.TimeoutException;
import org.esupportail.lecture.exceptions.domain.CategoryNotLoadedException;
import org.esupportail.lecture.exceptions.domain.CategoryTimeOutException;
import org.esupportail.lecture.exceptions.domain.ComputeFeaturesException;
import org.esupportail.lecture.exceptions.domain.SourceProfileNotFoundException;
import org.esupportail.lecture.exceptions.domain.UserNotSubscribedToCategoryException;
import org.esupportail.lecture.exceptions.web.WebException;

/**
 * Managed category profile element. It references a managedCategory
 * It is defined in a context.
 * @author gbouteil
 * @see CategoryProfile
 * @see ManagedElementProfile
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
	 * parameters interested : edit, visibility
	 */
	private boolean trustCategory;
	
	/**
	 * Resolve feature values (edit, visibility) from :
	 * - managedCategoryProfile features
	 * - managedCategory features
	 * - trustCategory parameter 
	 */
	private ManagedCategoryFeatures features;
		
	// Later
//	/**
//	 * Remote managed category edit mode : not used for the moment
//	 * Using depends on trustCategory parameter
//	 */	
//	private Editability edit;
	
	/**
	 * Visibility rights for groups on the remote managed category.
	 * Value is one defined in channel config. This value not must be
	 * exploited directly. It must be used by attribute "features" 
	 * (take care of trustCategory parameter)
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
	protected ManagedCategoryProfile() {
		if (log.isDebugEnabled()){
			log.debug("ManagedCategoryProfile()");
		}
		features = new ManagedCategoryFeatures(this);
	}
	
	/*
	 ************************** METHODS ******************************** */	
	
	

	/**
	 * Update CustomContext with this ManagedCategoryProfile. 
	 * It evaluates visibility for user profile and subscribe it 
	 * or not to customContext.
	 * @param customContext the customContext to update
	 * @param ex access to externalService to evaluate visibility
	 * @return true if the category is visible by the userProfile
	 * @throws ComputeFeaturesException 
	 * @throws CategoryTimeOutException 
	 */
	synchronized protected VisibilityMode updateCustomContext(CustomContext customContext,ExternalService ex) 
		throws ComputeFeaturesException, CategoryTimeOutException{
		if (log.isDebugEnabled()){
			log.debug("id="+this.getId()+" - updateCustomContext("+customContext.getElementId()+"externalService)");
		}
		loadCategory(ex);
		return setUpCustomContextVisibility(customContext, ex);
		
	}

	/**
	 *  Load the category referenced by this ManagedCategoryProfile
	 *  @param ex access to externalservice to get proxy ticket CAS
	 * @throws CategoryTimeOutException 
	 * @throws TimeoutException 
	 */
	@Override
	synchronized protected void loadCategory(ExternalService ex) throws CategoryTimeOutException {
		if (log.isDebugEnabled()){
			log.debug("id="+this.getId()+" - loadCategory(externalService)");
		}
		if(getAccess() == Accessibility.PUBLIC) {
			try {
				setElement(DomainTools.getDaoService().getManagedCategory(this));
			} catch (TimeoutException e) {
				String errorMsg = "the managedCategory"+ this.getId()+"is impossible to load because of a TimeoutException";
				log.error(errorMsg);
				throw new CategoryTimeOutException(errorMsg,e);
			}
						
		} else if (getAccess() == Accessibility.CAS) {
			String ptCas = ex.getUserProxyTicketCAS();
			setElement(DomainTools.getDaoService().getManagedCategory(this,ptCas));
		}
	}
	
	/**
	 * Evaluate visibility of current user for this managed category.
	 * Update customContext (belongs to user) if needed :
	 * add or remove subscription associated with this managedCategoryProfile
	 * @param ex access to externalService to evaluate visibility
	 * @param customContext customContext to set up
	 * @return true if the mcp is visible by the user of the customContext (in Obliged or in autoSubscribed, or in Allowed), else return false 
	 * @throws ComputeFeaturesException 
	 */
	synchronized private VisibilityMode setUpCustomContextVisibility(CustomContext customContext, ExternalService ex) 
		throws ComputeFeaturesException {
		if (log.isDebugEnabled()){
			log.debug("id="+this.getId()+" - setUpCustomContextVisibility("+customContext.getElementId()+",externalService)");
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
			return mode;
		}
		
		if (mode == VisibilityMode.AUTOSUBSCRIBED){
			if (log.isTraceEnabled()){
				log.trace("IsInAutoSubscribed : "+mode);
			}
			// TODO (GB later) l'ajouter dans le custom context si c'est la premiere fois
			//customContext.addSubscription(this);
			return mode;
		}
		
		if (mode == VisibilityMode.ALLOWED) {
			if (log.isTraceEnabled()){
				log.trace("IsInAllowed : "+mode);
			}
			// Nothing to do
			return mode;
		} 
		// TODO (GB later) retirer les customCat du user profile qui correspondent à des profiles 
		// de catégories disparus
		
		// ELSE not Visible
		customContext.removeCustomManagedCategoryFromProfile(this.getId());

		mode = VisibilityMode.NOVISIBLE;
		return mode;
		
//		boolean isInObliged = false;
//		boolean isInAutoSubscribed = false;
//		boolean isInAllowed = false;
		
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
	 * Update the CustomManagedCategory linked to this ManagedCategoryProfile.
	 * It sets up subscriptions of customManagedCategory on managedSourcesProfiles
	 * defined in ManagedCategory of this Profile, according to managedSourceProfiles visibility
	 * (there is not any loading of source at this time)
	 * @param customManagedCategory customManagedCategory to update
	 * @param ex access to external service for visibility evaluation
	 * @throws CategoryNotLoadedException
	 */
	synchronized protected void updateCustom(CustomManagedCategory customManagedCategory,ExternalService ex) 
		throws CategoryNotLoadedException {
		if (log.isDebugEnabled()){
			log.debug("id="+this.getId()+" - updateCustom("+customManagedCategory.getElementId()+",externalService)");
		}
		ManagedCategory category = (ManagedCategory) getElement();
		category.updateCustom(customManagedCategory, ex);
	}
	
	/**
	 * Return a list of <SourceProfile,VisibilityMode> corresponding to visible sources for user, 
	 * in this CustomManagedCategory and update it (like methode updateCustom): 
	 * It sets up subscriptions of customManagedCategory on managedSourcesProfiles
	 * defined in ManagedCategory of this Profile, according to managedSourceProfiles visibility
	 * @param customManagedCategory custom to update
	 * @param ex access to externalService
	 * @return list of <ProfileVisibility>
	 * @throws CategoryNotLoadedException 
	 * @see ManagedCategoryProfile#updateCustom(CustomManagedCategory, ExternalService)
	 */
	synchronized protected List<ProfileVisibility> getVisibleSourcesAndUpdateCustom(CustomManagedCategory customManagedCategory, ExternalService ex) 
		throws CategoryNotLoadedException {
		if (log.isDebugEnabled()){
			log.debug("id="+this.getId()+" - getVisibleSourcesAndUpdateCustom("+customManagedCategory.getElementId()+",externalService)");
		}
		ManagedCategory category = (ManagedCategory) getElement();
		return category.getVisibleSourcesAndUpdateCustom(customManagedCategory, ex);
	}
	
	
	
	/** 
	 * Computes rights on parameters shared between a ManagedCategoryProfile and its
	 * ManagedCategory (edit, visibility)
	 * @throws CategoryNotLoadedException 
	 */
	synchronized public void computeFeatures() throws CategoryNotLoadedException  {
		if (log.isDebugEnabled()){
			log.debug("id="+this.getId()+" - computeFeatures()");
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
		features.update(setVisib);
	}

	/**
	 * Compute visibility value from features and returns it
	 * @return Visibility
	 * @throws ComputeFeaturesException 
	 * @see ManagedCategoryProfile#visibility
	 */
	public VisibilitySets getVisibility() throws ComputeFeaturesException{
		if (log.isDebugEnabled()){
			log.debug("id="+this.getId()+" - getVisibility()");
		}
		return features.getVisibility();
	}
	
	/**
	 * @param visibility
	 */
	synchronized public void setVisibility(VisibilitySets visibility) {
		if (log.isDebugEnabled()){
			log.debug("id="+this.getId()+" - setVisibility(visibility)");
		}
		this.visibility = visibility;
		features.setIsComputed(false);
	}
	
//	/**
//	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#setVisibilityAllowed(org.esupportail.lecture.domain.model.DefinitionSets)
//	 */
//	synchronized public void setVisibilityAllowed(DefinitionSets d) {
//		if (log.isDebugEnabled()){
//			log.debug("setVisibilityAllowed(definitionSets)");
//		}
//		this.visibility.setAllowed(d);
//		features.setIsComputed(false);
//	}
//	
//	/**
//	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#setVisibilityAutoSubcribed(org.esupportail.lecture.domain.model.DefinitionSets)
//	 */
//	synchronized public void setVisibilityAutoSubcribed(DefinitionSets d) {
//		if (log.isDebugEnabled()){
//			log.debug("setVisibilityAutoSubcribed(definitionSets)");
//		}
//		this.visibility.setAutoSubscribed(d);
//		features.setIsComputed(false);
//	}
//	
//	/**
//	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#setVisibilityObliged(org.esupportail.lecture.domain.model.DefinitionSets)
//	 */
//	synchronized public void setVisibilityObliged(DefinitionSets d) {
//		if (log.isDebugEnabled()){
//			log.debug("setVisibilityObliged(definitionSets)");
//		}
//		this.visibility.setObliged(d);
//		features.setIsComputed(false);
//	}
//	

	/**
	 * Add a context to the set of context of this managed category profile
	 * This means that this managedCategoryProfile is declared in context c
	 * @param c context to add
	 */
	synchronized protected void addContext(Context c){
		if (log.isDebugEnabled()){
			log.debug("id="+this.getId()+" - addContext("+c.getId()+")");
		}
		contextsSet.add(c);
	}
	
	/**
	 * Returns the managedSourceProfile identified by id, accessible by this ManagedCategoryProfile
	 * (Defined in ManagedCategory referred by this ManagedCategoryProfile)
	 * @param id id of the sourceProfile to get
	 * @return the sourceProfile
	 * @throws CategoryNotLoadedException 
	 * @throws SourceProfileNotFoundException 
	 */
	@Override
	protected ManagedSourceProfile getSourceProfileById(String id) throws CategoryNotLoadedException, SourceProfileNotFoundException {
		if (log.isDebugEnabled()){
			log.debug("id="+this.getId()+" - getSourceProfileById("+id+")");
		}
//		 TODO (GB) ? on pourrait faire un loadCategory ou autre chose ou ailleurs ?
		return (ManagedSourceProfile) getElement().getSourceProfileById(id);

	}

	
	
	
	/*
	 *************************** ACCESSORS ******************************** */	


	/**
	 * Returns the URL of the remote managed category
	 * @return urlCategory
	 */
	public String getUrlCategory() {
		return categoryURL;
	}
	
	/** 
	 * Sets the URL of the remote managed category
	 * @param categoryURL the URL to set
	 */
	synchronized public void setUrlCategory(String categoryURL) {
		//RB categoryURL = DomainTools.replaceWithUserAttributes(categoryURL);
		this.categoryURL = categoryURL;
	}

	/**
	 * Returns the state (true or false) of the trust category parameter
	 * @return trustCategory
	 */
	protected boolean getTrustCategory() {
		return trustCategory;
	}
	
	/**
	 * Sets the trust category parameter
	 * @param trustCategory 
	 */
	synchronized protected void setTrustCategory(boolean trustCategory) {
		this.trustCategory = trustCategory;
	}


	/**
	 * @return access
	 */
	public Accessibility getAccess() {
		return access;
	}
	
	/**
	 * @param access 
	 */
	synchronized public void setAccess(Accessibility access) {
		this.access = access;
	}
	
	// utile plus tard
//	protected Editability getEdit() {
//		return features.getEdit();
//	}
//	protected void setEdit(Editability edit) {
//		this.edit = edit;
//	features.setComputed(false);
//	}
	
	

//	/**
//	 * @return allowed visibility group 
//	 * @throws ComputeFeaturesException 
//	 */
//	public DefinitionSets getVisibilityAllowed() throws ComputeFeaturesException  {
//		return getVisibility().getAllowed();
//	}
//
//
//	/** 
//	 * @return autoSubscribed group visibility
//	 * @throws ComputeFeaturesException 
//	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#getVisibilityAutoSubscribed()
//	 */
//	public DefinitionSets getVisibilityAutoSubscribed() throws ComputeFeaturesException  {
//		return getVisibility().getAutoSubscribed();
//	}
//
//	
//	/**
//	 * @return obliged group visibility
//	 * @throws ElementNotLoadedException 
//	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#getVisibilityObliged()
//	 */
//	public DefinitionSets getVisibilityObliged() throws ComputeFeaturesException {
//		return getVisibility().getObliged();
//		
//	}

	/**
	 * @return ttl
	 * @see ManagedCategoryProfile#ttl
	 */
	public int getTtl(){
		return ttl;
	}
	
	/**
	 * @param ttl 
	 * @see ManagedCategoryProfile#ttl
	 */
	synchronized public void setTtl(int ttl) {
		this.ttl = ttl;
	}

	/**
	 * @return contextSets
	 */
	protected Set<Context> getContextsSet() {
		return contextsSet;
	}





}
