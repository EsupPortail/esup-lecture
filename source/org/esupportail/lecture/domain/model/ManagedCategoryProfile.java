/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;


import java.util.*;

import org.esupportail.lecture.dao.DaoService;
import org.esupportail.lecture.domain.service.PortletService;

/**
 * Managed category profile element.
 * @author gbouteil
 * @see org.esupportail.lecture.domain.model.CategoryProfile
 * @see org.esupportail.lecture.domain.model.ManagedComposantProfile
 *
 */
public class ManagedCategoryProfile extends CategoryProfile implements ManagedComposantProfile {

/* ************************** PROPERTIES ******************************** */	

	
	/**
	 * Proxy ticket CAS to access remote managed category (not necessary) 
	 */
	private String ptCas = "";
	
	/**
	 * URL of the remote managed category
	 */
	private String urlCategory = "";
	
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
	private ActiveFeatures activeFeatures;
		
	/**
	 * Remote managed category edit mode : not used for the moment
	 * Using depends on trustCategory parameter
	 */	
	private Editability edit;
	
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

	/**
	 * Its category
	 * When its managed category is not null,
	 * The managed categroy profile is said "full"
	 */
	private ManagedCategory managedCategory;
	
	
	/*
	 ************************** INITIALIZATION ******************************** */	
	
	

	/**
	 * Initialize activeFeature attribute
	 */
	public void initMiscellaneous() {
		activeFeatures = new ActiveFeatures();
		activeFeatures.update(edit,visibility,ttl);
		if (!trustCategory) {
			/* As the category is not trusted, 
			 *  -> features are already computed */
			activeFeatures.setComputed(true);
		}
	}
	
	/*
	 ************************** METHODS ******************************** */	

	
	/** 
	 * Returns the managed category
	 * @return category
	 * @see ManagedCategoryProfile#category
	 */
	public void loadCategory() {
		// TODO voir l'heritage
		managedCategory 
		= super.getDaoService().getCategory(
				urlCategory,
				activeFeatures.getTtl(),
				this.getId());
		computeActiveFeatures(managedCategory);
	}
	
	/**
	 * Computes rights on parameters shared between a ManagedCategoryProfile and its
	 * ManagedCategory (edit, visibility
	 * @param managedCategory
	 */
	private void computeActiveFeatures(ManagedCategory managedCategory) {
		
		Editability setEdit;
		VisibilitySets setVisib;
		int setTtl;
		
		if (trustCategory) {		
			setEdit = managedCategory.getEdit();
			setVisib = managedCategory.getVisibility();
			setTtl = managedCategory.getTtl();
			
			if (setEdit == null) {
				setEdit = this.edit;
			}
			if (setVisib == null) {
				setVisib = this.visibility;
			}
			if (setTtl == -1) {
				setTtl = this.ttl;
			}	
			activeFeatures.update(edit,visibility,ttl);
			activeFeatures.setComputed(true);
		}/* else {
				Already done during channel config loading 
		} */
	}
	
	/**
	 * Evaluate visibility of current user for this managed category.
	 * Update customContext (belongs to user) if needed :
	 * add or remove customCategories associated with
	 * @param portletService
	 * @param customContext
	 */
	public void evaluateVisibilityAndUpdateUser(PortletService portletService, CustomContext customContext) {
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
		
		VisibilitySets sets = activeFeatures.getVisibility();
		
	/* ---OBLIGED SET--- */
		isInObliged = sets.getObliged().evaluateVisibility(portletService);
		
		if (isInObliged) {
			customContext.addManagedCustomCategory(this);
		
		} else {
	/* ---AUTOSUBSCRIBED SET--- */	
			// TODO isInAutoSubscribed = sets.getAutoSubscribed().evaluateVisibility(portletService);
			// en attendant : isInAutoSubscribed = false 
			
			if(isInAutoSubscribed) {
				// TODO l'ajouter dans le custom context si c'est la preniere fois
				// customContext.addCustomCategory(mcp);
			
			} else {
	/* ---ALLOWED SET--- */	
				isInAllowed = sets.getAllowed().evaluateVisibility(portletService);
				
				if (!isInAllowed) { // If isInAllowed : nothing to do
	/* ---CATEGORY NOT VISIBLE FOR USER--- */
					customContext.removeManagedCustomCategory(this);
				}
				
			}	
		}
		// TODO retirer les customCat du user profile qui correspondent à des profiles 
		// de catégories  disparus	
	}
	
	
	
	
	

	/**
	 * Return a string containing content of the managed category profile :
	 * URL of the remote managed category, trustCategory parameter, Access mode on remote managed category,
	 * Visibility rights for groups, ttl of the remote managed category,The remote managed category,
	 * Contexts where these profiles category are defined
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		
		String string ="";
		
		string += super.toString();
		
		/* Proxy ticket CAS */
//		string += "	PtCas : " + ptCas.toString() +"\n";
		
		/* URL of the remote managed category */
		string += "	urlCategory : " + urlCategory.toString() +"\n";		
		
		/* trustCategory parameter */
		if (trustCategory){
			string += "	trust Category : true \n";		
		} else {
			string += "	trust Category : false \n";		
		}
		
		/* The remote managed category edit mode : not used for the moment */	
		//string += "	edit : " + edit.toString() +"\n";	
		
		/* Access mode on this remote managed category */
		string += "	access : " + access.toString() +"\n";	

		/* Visibility rights for groups */
		string += "	visibility : " +"\n"+ visibility.toString();
		
		/* ttl of the remote managed category */
		string += "	ttl : " + ttl +"\n";
		
		/* The remote managed category */
		//string += "	category : " + category +"\n";

		/* Contexts where these profiles category are defined */
		string += "	contextsSet : \n";
		Iterator iterator = contextsSet.iterator();
		for (Context c = null; iterator.hasNext();) {
			c = (Context)iterator.next();
			string += "          ("+ c.getId() + "," + c.getName()+")\n";
		}
		
		return string;
		
	}
	
/* ************************** ACCESSORS ******************************** */	

	/**
	 * Return the proxy ticket CAS used to access to the remote managed category
	 * @return ptCas
	 * @see ManagedCategoryProfile#ptCas
	 */
	protected String getPtCas() {
		return ptCas;
	}
	
	/**
	 * Sets the proxy ticket CAS used to access to the remote managed category
	 * @param ptCas the proxy ticket CAS to set
	 * @see ManagedCategoryProfile#ptCas
	 */
	protected void setPtCas(String ptCas) {
		this.ptCas = ptCas;
	}
	
	/**
	 * Returns the URL of the remote managed category
	 * @return urlCategory
	 * @see ManagedCategoryProfile#urlCategory
	 */
	protected String getUrlCategory() {
		return urlCategory;
	}
	
	/** 
	 * Sets the URL of the remote managed category
	 * @param urlCategory the URL to set
	 * @see ManagedCategoryProfile#urlCategory
	 */
	protected void setUrlCategory(String urlCategory) {
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

	// utile plus tard
//	protected Editability getEdit() {
//		return edit;
//	}
//	protected void setEdit(Editability edit) {
//		this.edit = edit;
//	}

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
	
	/**
	 * @return Visibility
	 * @see ManagedCategoryProfile#visibility
	 * @see org.esupportail.lecture.domain.model.ManagedComposantProfile#getVisibility()
	 */
	public VisibilitySets getVisibility() {
		return visibility;
	}
	
	/**
	 * @see ManagedCategoryProfile#visibility
	 * @see org.esupportail.lecture.domain.model.ManagedComposantProfile#setVisibility(org.esupportail.lecture.domain.model.VisibilitySets)
	 */
	public void setVisibility(VisibilitySets visibility) {
		this.visibility = visibility;
	}

	/**
	 * @see org.esupportail.lecture.domain.model.ManagedComposantProfile#setVisibilityAllowed(org.esupportail.lecture.domain.model.DefinitionSets)
	 */
	public void setVisibilityAllowed(DefinitionSets d) {
		this.visibility.setAllowed(d);
	}
	
	/**
	 * @return allowed visibility group 
	 * @see org.esupportail.lecture.domain.model.ManagedComposantProfile#getVisibilityAllowed()
	 */
	public DefinitionSets getVisibilityAllowed() {
		return this.visibility.getAllowed();
	}
	
	/**
	 * @see org.esupportail.lecture.domain.model.ManagedComposantProfile#setVisibilityAutoSubcribed(org.esupportail.lecture.domain.model.DefinitionSets)
	 */
	public void setVisibilityAutoSubcribed(DefinitionSets d) {
		this.visibility.setAutoSubscribed(d);
	}
	
	/** 
	 * @return autoSubscribed group visibility
	 * @see org.esupportail.lecture.domain.model.ManagedComposantProfile#getVisibilityAutoSubscribed()
	 */
	public DefinitionSets getVisibilityAutoSubscribed() {
		return this.visibility.getAutoSubscribed();
	}
	
	/**
	 * @see org.esupportail.lecture.domain.model.ManagedComposantProfile#setVisibilityObliged(org.esupportail.lecture.domain.model.DefinitionSets)
	 */
	public void setVisibilityObliged(DefinitionSets d) {
		this.visibility.setObliged(d);
	}
	
	/**
	 * @return obliged group visibility
	 * @see org.esupportail.lecture.domain.model.ManagedComposantProfile#getVisibilityObliged()
	 */
	public DefinitionSets getVisibilityObliged() {
		return this.visibility.getObliged();
	}
	
	/**
	 * Returns ttl
	 * @see ManagedCategoryProfile#ttl
	 * @see org.esupportail.lecture.domain.model.ManagedComposantProfile#getTtl()
	 */
	public int getTtl() {
		return ttl;
	}
	
	/**
	 * @see ManagedCategoryProfile#ttl
	 * @see org.esupportail.lecture.domain.model.ManagedComposantProfile#setTtl(int)
	 */
	public void setTtl(int ttl) {
		this.ttl = ttl;
	}


	


// Aretirer si inutile	
//	protected Set<Context> getContextsSet() {
//		return contextsSet;
//	}
	
//	A retirer si inutile	
//	protected void setContextsSet(Set<Context> contextsSet) {
//		this.contextsSet = contextsSet;
//	}	
	
	/**
	 * Add a context to the set of context in this managed category profile
	 * @param c context to add
	 * @see ManagedCategoryProfile#contextsSet
	 */
	protected void addContext(Context c){
		contextsSet.add(c);
	}

	/**
	 * @return Returns the managedCategory.
	 */
	public ManagedCategory getManagedCategory() {
		return managedCategory;
	}

	/**
	 * @return Returns the activeFeatures.
	 */
	public ActiveFeatures getActiveFeatures() {
		return activeFeatures;
	}




	
// utiles plus tard	
//	/**
//	 * Getter of the property <tt>refreshTimer</tt>
//	 * @return  Returns the refreshTimer.
//	 */
//	protected int getRefreshTimer() {
//		return refreshTimer;
//	}
//
//	/**
//	 * Setter of the property <tt>refreshTimer</tt>
//	 * @param refreshTimer  The refreshTimer to set.
//	 */
//	protected void setRefreshTimer(int refreshTimer) {
//		this.refreshTimer = refreshTimer;
//	}	
/* *******************************************************************/	

// Utiles plus tard	
	
//		/**
//		 */
//	public void refresh(){
//		
//		}

//	public void loadCategory(String urlCategory)	throws MissingPtCasException {
//												
//	}
//
//					/**
//					 * @uml.property  name="realVisibility"
//					 */
//					private VisibilitySets realVisibility;
//
//					/**
//					 * Getter of the property <tt>realVisibility</tt>
//					 * @return  Returns the realVisibility.
//					 * @uml.property  name="realVisibility"
//					 */
//					public VisibilitySets getRealVisibility() {
//						return realVisibility;
//					}
//
//					/**
//					 * Setter of the property <tt>realVisibility</tt>
//					 * @param realVisibility  The realVisibility to set.
//					 * @uml.property  name="realVisibility"
//					 */
//					public void setRealVisibility(VisibilitySets realVisibility) {
//						this.realVisibility = realVisibility;
//					}
//
//					/**
//					 * @uml.property  name="realTtl"
//					 */
//					private int realTtl;
//
//					/**
//					 * Getter of the property <tt>realTtl</tt>
//					 * @return  Returns the realTtl.
//					 * @uml.property  name="realTtl"
//					 */
//					public int getRealTtl() {
//						return realTtl;
//					}
//
//					/**
//					 * Setter of the property <tt>realTtl</tt>
//					 * @param realTtl  The realTtl to set.
//					 * @uml.property  name="realTtl"
//					 */
//					public void setRealTtl(int realTtl) {
//						this.realTtl = realTtl;
//					}



						
//						/**
//						 */
//						public boolean isTimeToReload(){
//							return true;
//						}
//
//							
	

//								
//								/**
//								 */
//								public void forceRefreshTimer(){
//								
//								}

}
