package org.esupportail.lecture.domain.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.exceptions.domain.ElementNotLoadedException;

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
	 * Log instance 
	 */
	protected static final Log log = LogFactory.getLog(ComputedManagedElementFeatures.class);
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
	private ManagedElementProfile mep;
	
	
	/*
	 ********************* INITIALIZATION **************************************/
	
	public ComputedManagedElementFeatures(){
		
	}
	
	
	/** 
	 * Constructor
	 * @param mcp Managed category profile concerned by these features
	 */
	protected ComputedManagedElementFeatures(ManagedElementProfile mep){
		if (log.isDebugEnabled()){
			log.debug("ComputedManagedElementFeatures("+mep.getId()+")");
		}
		this.mep = mep;
	}
	

	
	/*
	 *********************** METHODS **************************************/
	/**
	 * Compute features
	 * @throws ElementNotLoadedException 
	 */
	protected void compute() throws ElementNotLoadedException {
		if (log.isDebugEnabled()){
			log.debug("compute()");
		}
		mep.computeFeatures();
		isComputed = true;
	}
	
	
	/**
	 * Update features simply
	 * It is called by the associated managed element profile when it concretly computes features
	 * @param visibility
	 */
	protected void update( VisibilitySets visibility) {
		if (log.isDebugEnabled()){
			log.debug("update(visibility)");
		}
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
