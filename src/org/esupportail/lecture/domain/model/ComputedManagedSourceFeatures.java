package org.esupportail.lecture.domain.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.exceptions.domain.ElementNotLoadedException;

/**
 * Class that contains computed features of a source :
 * It merges features  between managedSourceProfile
 * and its managedSource or else
 * @author gbouteil
 *
 */
public class ComputedManagedSourceFeatures extends ComputedManagedElementFeatures {

	/*
	 *********************** PROPERTIES**************************************/ 
	
	/**
	 * Log instance 
	 */
	protected static final Log log = LogFactory.getLog(ComputedManagedSourceFeatures.class);
	
	
	private Accessibility access;
	
	
	
	
	/*
	 ********************* INITIALIZATION **************************************/
	protected ComputedManagedSourceFeatures(ManagedSourceProfile msp) {
		super(msp);
		if (log.isDebugEnabled()){
			log.debug("ComputedManagedSourceFeatures("+msp.getId()+")");
		}
	}
	

	/*
	 *********************** METHODS **************************************/
	
	/**
	 * Update features "simply"
	 * It is called by the associated managed source profile when it has concretly computed features
	 * @param setVisib visibility
	 * @param setAccess access
	 */
	public void update(VisibilitySets setVisib, Accessibility setAccess) {
		if (log.isDebugEnabled()){
			log.debug("update(setVisib,setAccess)");
		}
		super.update(setVisib);

		access = setAccess;
	}

	/**
	 * @return Returns the access.
	 * @throws ElementNotLoadedException 
	 */
	protected Accessibility getAccess() throws ElementNotLoadedException {
		if (log.isDebugEnabled()){
			log.debug("getAccess()");
		}
		if (!super.isComputed()){
			super.compute();
		}
		return access;
	}
	

	/*
	 *********************** ACCESSORS **************************************/ 




}
