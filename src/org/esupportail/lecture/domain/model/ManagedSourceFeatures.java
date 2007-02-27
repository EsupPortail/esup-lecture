/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.exceptions.domain.CategoryNotLoadedException;
import org.esupportail.lecture.exceptions.domain.ComputeFeaturesException;

/**
 * Class that contains features of a source needed to be computed
 * because of inheritance rules between Source and managedSourceProfile,
 * depending on trustCategory parameter :
 * Interested feature are : visibility, access, timeOut
 * @author gbouteil
 * @see ManagedElementFeatures
 *
 */
public class ManagedSourceFeatures extends ManagedElementFeatures {

	/*
	 *********************** PROPERTIES**************************************/ 
	
	/**
	 * Log instance 
	 */
	protected static final Log log = LogFactory.getLog(ManagedSourceFeatures.class);
	/**
	 * Access mode on the Source
	 */	
	private Accessibility access;
	/**
	 * timeOut to get the Source
	 */	
	private int timeOut;
	
	
	/*
	 ********************* INITIALIZATION **************************************/

	/** 
	 * Constructor
	 * @param msp Managed source profile needing these features
	 */	
	protected ManagedSourceFeatures(ManagedSourceProfile msp) {
		super(msp);
		if (log.isDebugEnabled()){
			log.debug("ManagedSourceFeatures("+msp.getId()+")");
		}
	}
	

	/*
	 *********************** METHODS **************************************/
	
	/**
	 * Used to update features directly, without any computing
	 * It only sets value in parameters
	 * @param visib the visibility feature to update
	 * @param acces the access mode to update
	 * @param to timeOut to update
	 */
	synchronized protected void update(VisibilitySets visib, Accessibility acces, int to) {
		if (log.isDebugEnabled()){
			log.debug("update(setVisib,setAccess)");
		}
		super.update(visib);
		access = acces;
		timeOut = to;
	}

	/**
	 * @return Returns the accessibility mode (feature is automatically computed if needed).
	 * @throws ComputeFeaturesException 
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
	
	/**
	 * @return Returns the timeOut (feature is automatically computed if needed).
	 * @throws ComputeFeaturesException 
	 */
	synchronized protected int getTimeOut() throws ComputeFeaturesException{
		if (log.isDebugEnabled()){
			log.debug("getTimeOut()");
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
		return timeOut;
	}
	

	/*
	 *********************** ACCESSORS **************************************/ 




}
