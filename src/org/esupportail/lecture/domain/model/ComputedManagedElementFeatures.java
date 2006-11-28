package org.esupportail.lecture.domain.model;

import org.esupportail.lecture.exceptions.ElementNotLoadedException;

/**
 * Class that contains computed features of a element :
 * It merges features (visibility,tll) between managedElementProfile
 * and its managedElement
 * @author gbouteil
 *
 */
public abstract class ComputedManagedElementFeatures {

	/*
	 *********************** PROPERTIES**************************************/ 

	/**
	 * Visibility rights for groups on the remote managed category
	 * Using depends on trustCategory parameter
	 */
	private VisibilitySets visibility;

	/**
	 * Indicates if activeFeatures can be used or not :
	 *  - false : features are not computed, they can't be used
	 *  - true : features are computed, they can be used
 	 */
	private boolean isComputed = false;
	
	/**
	 * Managed element profile concerned by these features
	 */
	private ManagedElementProfile mcp;
	
	
	/*
	 ********************* INITIALIZATION **************************************/
	
	/** 
	 * Constructor
	 * @param mcp Managed category profile concerned by these features
	 */
	protected ComputedManagedElementFeatures(ManagedElementProfile mcp){
		this.mcp = mcp;
	}
	

	
	/*
	 *********************** METHODS **************************************/
	/**
	 * Compute features
	 * @throws ElementNotLoadedException 
	 */
	protected void compute() throws ElementNotLoadedException {
		mcp.computeFeatures();
		isComputed = true;
	}
	
	
	/**
	 * Update features simply
	 * It is called by the associated managed element profile when it concretly computes features
	 * @param visibility
	 */
	protected void update( VisibilitySets visibility) {
		this.visibility = visibility;
	}
	
	/*
	 *********************** ACCESSORS **************************************/ 
	/**
	 * @return Returns the isComputed.
	 */
	protected boolean isComputed() {
		return isComputed;
	}
	
	
	/**
	 * Sets IsComuted
	 * @param b the boolean to set
	 */
	protected void setIsComputed(boolean b) {
		isComputed = b;
	}

		
	/**
	 * @return Returns the visibility.
	 * @throws ElementNotLoadedException 
	 */
	protected VisibilitySets getVisibility() throws ElementNotLoadedException {
		if (!isComputed){
			compute();
		}
		return visibility;
	}
	
}
