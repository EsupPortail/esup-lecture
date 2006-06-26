package org.esupportail.lecture.domain.model;


import java.util.*;

import org.esupportail.lecture.utils.exception.MissingPtCasException;

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
	 * trustCategory parameter indicates between managed category and category profile, which one to trust
	 * True : category is trusted. False :catgory is not trusted, only profile is good for following 
	 * parameters
	 * (edit, visibility, ttl)
	 */
	private boolean trustCategory;
	
	/**
	 * The remote managed category edit mode : not used for the moment
	 */	
	private Editability edit;
	
	/**
	 * Access mode on this remote managed category
	 */
	private Accessibility access;
	
	/**
	 * Visibility rights for groups on the remote managed category
	 */
	private VisibilitySets visibility;

	/**
	 * ttl of the remote managed category
	 */
	private int ttl;
	
	/**
	 * The remote managed category
	 */
	private ManagedCategory category = null; 
	
	/**
	 * Contexts where these profiles category are defined
	 */
	private List<Context> contextsList = new ArrayList();


/* ************************** ACCESSORS ******************************** */	

	public String getPtCas() {
		return ptCas;
	}
	public void setPtCas(String ptCas) {
		this.ptCas = ptCas;
	}
	
	public String getUrlCategory() {
		return urlCategory;
	}
	public void setUrlCategory(String urlCategory) {
		this.urlCategory = urlCategory;
	}

	public boolean getTrustCategory() {
		return trustCategory;
	}
	public void setTrustCategory(boolean trustCategory) {
		this.trustCategory = trustCategory;
	}

	public Editability getEdit() {
		return edit;
	}
	public void setEdit(Editability edit) {
		this.edit = edit;
	}

	public Accessibility getAccess() {
		return access;
	}
	public void setAccess(Accessibility access) {
		this.access = access;
	}
	

	public VisibilitySets getVisibility() {
		return visibility;
	}
	public void setVisibility(VisibilitySets visibility) {
		this.visibility = visibility;
	}

	public int getTtl() {
		return ttl;
	}
	public void setTtl(int ttl) {
		this.ttl = ttl;
	}

	public ManagedCategory getCategory() {
		return category;
	}
	public void setCategory(ManagedCategory category) {
		this.category = category;
	}

	public List<Context> getContextsList() {
		return contextsList;
	}
	public void setContextsList(List<Context> contextsList) {
		this.contextsList = contextsList;
	}	
	
/* ************************** METHODS ******************************** */	

	public String toString(){
		
		String string ="";
		
		string += super.toString();
		
		/* Proxy ticket CAS */
		string += "	PtCas : " + ptCas.toString() +"\n";
		
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
		string += "	category : " + category +"\n";

		/* Contexts where these profiles category are defined */
		string += "	contextsList : \n";
		Iterator iterator = contextsList.iterator();
		for (Context c = null; iterator.hasNext();) {
			c = (Context)iterator.next();
			string += "          ("+ c.getId() + "," + c.getName()+")\n";
		}
		
		return string;
		
	}
	
	/**
	 * Add a context to  contextsList if is not present in.
	 * (Method used by context where the managed Category is referenced to be known by a managedCategoryProfile)
	 * @param c : context added
	 * Method used by context where the managed Category is referenced to be known by a managedCategoryProfile
	 */
	public void addContext(Context c){
		
		if (contextsList.indexOf(c) < 0){
			contextsList.add(c);
		}
	}
		/**
		 */
	public void refresh(){
		
		}




			
				
				
					
					
					public void loadCategory(String urlCategory)	throws MissingPtCasException {
					
									
												
												}

					/**
					 * @uml.property  name="realVisibility"
					 */
					private VisibilitySets realVisibility;

					/**
					 * Getter of the property <tt>realVisibility</tt>
					 * @return  Returns the realVisibility.
					 * @uml.property  name="realVisibility"
					 */
					public VisibilitySets getRealVisibility() {
						return realVisibility;
					}

					/**
					 * Setter of the property <tt>realVisibility</tt>
					 * @param realVisibility  The realVisibility to set.
					 * @uml.property  name="realVisibility"
					 */
					public void setRealVisibility(VisibilitySets realVisibility) {
						this.realVisibility = realVisibility;
					}

					/**
					 * @uml.property  name="realTtl"
					 */
					private int realTtl;

					/**
					 * Getter of the property <tt>realTtl</tt>
					 * @return  Returns the realTtl.
					 * @uml.property  name="realTtl"
					 */
					public int getRealTtl() {
						return realTtl;
					}

					/**
					 * Setter of the property <tt>realTtl</tt>
					 * @param realTtl  The realTtl to set.
					 * @uml.property  name="realTtl"
					 */
					public void setRealTtl(int realTtl) {
						this.realTtl = realTtl;
					}

					/**
					 * @uml.property  name="refreshTimer"
					 */
					private int refreshTimer;

					/**
					 * Getter of the property <tt>refreshTimer</tt>
					 * @return  Returns the refreshTimer.
					 * @uml.property  name="refreshTimer"
					 */
					public int getRefreshTimer() {
						return refreshTimer;
					}

					/**
					 * Setter of the property <tt>refreshTimer</tt>
					 * @param refreshTimer  The refreshTimer to set.
					 * @uml.property  name="refreshTimer"
					 */
					public void setRefreshTimer(int refreshTimer) {
						this.refreshTimer = refreshTimer;
					}

						
						/**
						 */
						public boolean isTimeToReload(){
							return true;
						}

							
	

								
								/**
								 */
								public void forceRefreshTimer(){
								
								}

}
