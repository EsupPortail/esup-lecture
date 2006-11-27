package org.esupportail.lecture.domain.model;

import org.esupportail.lecture.exceptions.ComposantNotLoadedException;

/**
 * Class that contains computed features of a composant :
 * It merges features (visibility,tll) between managedComposantProfile
 * and its managedComposant
 * @author gbouteil
 *
 */
public abstract class ComputedManagedComposantFeatures {

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
	 * Managed composant profile concerned by these features
	 */
	private ManagedComposantProfile mcp;
	
	
	/*
	 ********************* INITIALIZATION **************************************/
	
	/** 
	 * Constructor
	 * @param mcp Managed category profile concerned by these features
	 */
	protected ComputedManagedComposantFeatures(ManagedComposantProfile mcp){
		this.mcp = mcp;
	}
	

	
	/*
	 *********************** METHODS **************************************/
	/**
	 * Compute features
	 * @throws ComposantNotLoadedException 
	 */
	protected void compute() throws ComposantNotLoadedException {
		mcp.computeFeatures();
		isComputed = true;
	}
	
	
	/**
	 * Update features simply
	 * It is called by the associated managed composant profile when it concretly computes features
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
	 * @throws ComposantNotLoadedException 
	 */
	protected VisibilitySets getVisibility() throws ComposantNotLoadedException {
		if (!isComputed){
			compute();
		}
		return visibility;
	}
	
}
