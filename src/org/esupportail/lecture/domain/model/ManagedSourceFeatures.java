package org.esupportail.lecture.domain.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.exceptions.domain.CategoryNotLoadedException;
import org.esupportail.lecture.exceptions.domain.ComputeFeaturesException;
import org.esupportail.lecture.exceptions.domain.ElementNotLoadedException;

/**
 * Class that contains computed features of a source :
 * It merges features  between managedSourceProfile
 * and its managedSource or else
 * @author gbouteil
 *
 */
public class ManagedSourceFeatures extends ManagedElementFeatures {

	/*
	 *********************** PROPERTIES**************************************/ 
	
	/**
	 * Log instance 
	 */
	protected static final Log log = LogFactory.getLog(ManagedSourceFeatures.class);
	
	
	private Accessibility access;
	
	
	
	
	/*
	 ********************* INITIALIZATION **************************************/
	protected ManagedSourceFeatures(ManagedSourceProfile msp) {
		super(msp);
		if (log.isDebugEnabled()){
			log.debug("ManagedSourceFeatures("+msp.getId()+")");
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
	synchronized public void update(VisibilitySets setVisib, Accessibility setAccess) {
		if (log.isDebugEnabled()){
			log.debug("update(setVisib,setAccess)");
		}
		super.update(setVisib);
		access = setAccess;
	}

	/**
	 * @return Returns the access.
	 * @throws ComputeFeaturesException 
	 * @throws CategoryNotLoadedException 
	 * @throws ElementNotLoadedException 
	 */
	synchronized protected Accessibility getAccess() throws ComputeFeaturesException{
		if (log.isDebugEnabled()){
			log.debug("getAccess()");
		}
		if (!super.isComputed()){
			try {
				super.compute();
			} catch (CategoryNotLoadedException e) {
				String errorMsg = "Impossible to compute features on element "+ super.mep.getId() + "because Category is not loaded";
				log.error(errorMsg);
				throw new ComputeFeaturesException(errorMsg,e);
			}
		}
		return access;
	}
	

	/*
	 *********************** ACCESSORS **************************************/ 




}
