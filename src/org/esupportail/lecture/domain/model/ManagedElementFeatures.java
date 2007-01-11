package org.esupportail.lecture.domain.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.exceptions.domain.CategoryNotLoadedException;
import org.esupportail.lecture.exceptions.domain.ComputeFeaturesException;
import org.esupportail.lecture.exceptions.domain.ElementNotLoadedException;

/**
 * Class that contains computed features of a element :
 * It merges features (visibility,tll) between managedElementProfile
 * and its managedElement
 * @author gbouteil
 *
 */
public abstract class ManagedElementFeatures {

	/*
	 *********************** PROPERTIES**************************************/ 
	/**
	 * Log instance 
	 */
	protected static final Log log = LogFactory.getLog(ManagedElementFeatures.class);
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
	protected ManagedElementProfile mep;
	
	
	/*
	 ********************* INITIALIZATION **************************************/
	
	public ManagedElementFeatures(){
		
	}
	
	
	/** 
	 * Constructor
	 * @param mcp Managed category profile concerned by these features
	 */
	protected ManagedElementFeatures(ManagedElementProfile mep){
		if (log.isDebugEnabled()){
			log.debug("ManagedElementFeatures("+mep.getId()+")");
		}
		this.mep = mep;
	}
	

	
	/*
	 *********************** METHODS **************************************/
	/**
	 * Compute features
	 * @throws CategoryNotLoadedException 
	 * @throws ComputeFeaturesException 
	 * @throws CategoryNotLoadedException 
	 * @throws ComputeFeaturesException 
	 * @throws CategoryNotLoadedException 
	 * @throws ComputeFeaturesException 
	 * @throws ElementNotLoadedException 
	 * @throws ElementNotLoadedException 
	 */
	synchronized protected void compute() throws CategoryNotLoadedException, ComputeFeaturesException   {
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
	synchronized protected void update( VisibilitySets visibility) {
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
	synchronized protected void setIsComputed(boolean b) {
		isComputed = b;
	}

		
	/**
	 * @return Returns the visibility.
	 * @throws ComputeFeaturesException 
	 * @throws CategoryNotLoadedException 
	 * @throws ElementNotLoadedException 
	 */
	synchronized protected VisibilitySets getVisibility() throws ComputeFeaturesException {
		if (!isComputed){
			try {
				compute();
			} catch (CategoryNotLoadedException e) {
				String errorMsg = "Impossible to compute features on element "+ mep.getId() + "because Category is not loaded";
				log.error(errorMsg);
				throw new ComputeFeaturesException(errorMsg,e);
			}
		}
		return visibility;
	}
	
}
