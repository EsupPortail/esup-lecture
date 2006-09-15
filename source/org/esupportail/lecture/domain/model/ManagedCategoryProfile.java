/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;


import java.util.*;
import org.esupportail.lecture.dao.DaoService;

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
	 * Access to data
	 */
	DaoService daoService;
	
	/**
	 * Proxy ticket CAS to access remote managed category (not necessary) 
	 */
	private String ptCas = "";
	
	/**
	 * URL of the remote managed category
	 */
	private String urlCategory = "";
	
	/**
	 * trustCategory parameter : indicates between managed category and category profile, which one to trust
	 * True : category is trusted. 
	 * False : category is not trusted, only parameters profile are good 
	 * parameters (edit, visibility, ttl)
	 */
	private boolean trustCategory;
	
	/**
	 * Remote managed category edit mode : not used for the moment
	 * Using depends on trustCategory parameter
	 */	
	private Editability edit;
	
	/**
	 * Access mode on the remote managed category
	 */
	private Accessibility access;
	
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
//	/**
//	 * Timer to refresh category
//	 */

	// TODO : initialiser come il faut */
	//private int refreshTimer;


/* ************************** METHODS ******************************** */	

	
	/** 
	 * Returns the managed category
	 * @return category
	 * @see ManagedCategoryProfile#category
	 */
	protected ManagedCategory getCategory() {
		//return daoService.getCategory(urlCategory,ttl,this.getId());
		return null;
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
	 * @see org.esupportail.lecture.domain.model.ManagedComposantProfile#setVisibilityAllowed(org.esupportail.lecture.domain.model.DefAndContentSets)
	 */
	public void setVisibilityAllowed(DefAndContentSets d) {
		this.visibility.setAllowed(d);
	}
	
	/**
	 * @return allowed visibility group 
	 * @see org.esupportail.lecture.domain.model.ManagedComposantProfile#getVisibilityAllowed()
	 */
	public DefAndContentSets getVisibilityAllowed() {
		return this.visibility.getAllowed();
	}
	
	/**
	 * @see org.esupportail.lecture.domain.model.ManagedComposantProfile#setVisibilityAutoSubcribed(org.esupportail.lecture.domain.model.DefAndContentSets)
	 */
	public void setVisibilityAutoSubcribed(DefAndContentSets d) {
		this.visibility.setAutoSubscribed(d);
	}
	
	/** 
	 * @return autoSubscribed group visibility
	 * @see org.esupportail.lecture.domain.model.ManagedComposantProfile#getVisibilityAutoSubscribed()
	 */
	public DefAndContentSets getVisibilityAutoSubscribed() {
		return this.visibility.getAutoSubscribed();
	}
	
	/**
	 * @see org.esupportail.lecture.domain.model.ManagedComposantProfile#setVisibilityObliged(org.esupportail.lecture.domain.model.DefAndContentSets)
	 */
	public void setVisibilityObliged(DefAndContentSets d) {
		this.visibility.setObliged(d);
	}
	
	/**
	 * @return obliged group visibility
	 * @see org.esupportail.lecture.domain.model.ManagedComposantProfile#getVisibilityObliged()
	 */
	public DefAndContentSets getVisibilityObliged() {
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
