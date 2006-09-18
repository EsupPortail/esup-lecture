package org.esupportail.lecture.domain.model;

/**
 * Class that contains active features :
 * It merges features (edit, visibility,tll) between managedCategoryProfile
 * and its managedCategory, according to the "trustCategory"
 * parameter in managedCategoryProfile
 * @author gbouteil
 *
 */
public class ActiveFeatures {
	
	/*
	 *********************** PROPERTIES**************************************/ 
	
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
	 * Indicates if activeFeatures can be used or not :
	 *  - false : features are not computed, they can't be used
	 *  - true : features are computed, they can be used
 	 */
	private boolean isComputed = false;
	
	/**
	 * Indicates if property "visibility" has to be evaluated) or it is ever made
	 *  - false : visibilitySets have to be evaluated
	 *  - true : visibilitySets is evaluated, it can be used 
	 */

	/*
	 ********************* INITIALIZATION **************************************/
	

	/*
	 *********************** METHODS **************************************/
	
	
		/**
	 * Initialize attributes
	 * @param edit
	 * @param visibility
	 * @param ttl
	 */
	public void update(Editability edit,VisibilitySets visibility,int ttl) {
		this.edit = edit;
		this.visibility = visibility;
		this.ttl = ttl;
	}
	
	/*
	 *********************** ACCESSORS **************************************/ 


	/**
	 * @return Returns the edit.
	 */
	public Editability getEdit() {
		return edit;
	}


	/**
	 * @param edit The edit to set.
	 */
	public void setEdit(Editability edit) {
		this.edit = edit;
	}


	/**
	 * @return Returns the ttl.
	 */
	public int getTtl() {
		return ttl;
	}


	/**
	 * @param ttl The ttl to set.
	 */
	public void setTtl(int ttl) {
		this.ttl = ttl;
	}


	/**
	 * @return Returns the visibility.
	 */
	public VisibilitySets getVisibility() {
		return visibility;
	}


	/**
	 * @param visibility The visibility to set.
	 */
	public void setVisibility(VisibilitySets visibility) {
		this.visibility = visibility;
	}


	/**
	 * @return Returns the isComputed.
	 */
	public boolean isComputed() {
		return isComputed;
	}


	/**
	 * @param isComputed The isComputed to set.
	 */
	public void setComputed(boolean isComputed) {
		this.isComputed = isComputed;
	}

	
	
	
}
