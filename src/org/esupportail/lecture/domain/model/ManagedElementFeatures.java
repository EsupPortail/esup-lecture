/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.exceptions.domain.CategoryNotLoadedException;

/**
 * Class that contains features of a managed element needed to be computed
 * because of inheritance rules between managedElement and managedElementProfile :
 * Interested feature is : visibility.
 * @author gbouteil
 * @see ManagedElementFeatures
 *
 */
public abstract class ManagedElementFeatures {

	/*
	 *********************** PROPERTIES**************************************/ 
	/**
	 * Log instance. 
	 */
	protected static final Log LOG = LogFactory.getLog(ManagedElementFeatures.class);
	/**
	 * Managed element profile needing these features.
	 */
	protected ManagedElementProfile mep;
	
	/**
	 * Visibility rights for groups on the managed element
	 * Its values depends on trustCategory parameter. 
	 */
	private VisibilitySets visibility;
	/**
	 * Indicates if activeFeatures can be used or not :
	 *  - false : features are not computed, they can't be used
	 *  - true : features are computed, they can be used.
 	 */
	private boolean isComputed = false;
	

	
	
	/*
	 ********************* INITIALIZATION **************************************/
	
	/** 
	 * Constructor.
	 * @param mep Managed element profile needing these features
	 */
	protected ManagedElementFeatures(final ManagedElementProfile mep) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("ManagedElementFeatures(" + mep.getId() + ")");
		}
		this.mep = mep;
	}
		
	/*
	 *********************** METHODS **************************************/
	
	/**
	 * Compute features.
	 * @throws CategoryNotLoadedException 
	 */
	protected void compute() throws CategoryNotLoadedException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("compute()");
		}
		mep.computeFeatures();
		isComputed = true;
	}
	
	/**
	 * Used to update features directly, without any computing.
	 * It only sets value in parameter
	 * @param visib the visibility feature to update
	 */
	protected void update(final VisibilitySets visib) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("update(visibility)");
		}
		this.visibility = visib;
	}
	
	/**
	 * @return Returns the visibility (feature is automatically computed if needed).
	 * @throws CategoryNotLoadedException 
	 */
	protected VisibilitySets getVisibility() throws CategoryNotLoadedException {
		if (!isComputed) {
			try {
				compute();
			} catch (CategoryNotLoadedException e) {
				String errorMsg = "Impossible to compute features on element " 
					+ mep.getId() + "because Category is not loaded";
				LOG.error(errorMsg);
				throw e;
			}
		}
		return visibility;
	}
	
	/*
	 *********************** ACCESSORS **************************************/ 
	/**
	 * @return true if features are computed
	 */
	protected boolean isComputed() {
		return isComputed;
	}
	
	/**
	 * Sets IsComputed.
	 * @param b the boolean to set
	 */
	protected void setIsComputed(final boolean b) {
		isComputed = b;
	}
		
	
	
}
