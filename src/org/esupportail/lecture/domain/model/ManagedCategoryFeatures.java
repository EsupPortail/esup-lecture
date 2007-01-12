/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class that contains features of category needed to be computed
 * because of inheritance rules between managedCategory and managedCategoryProfile :
 * Interested features are : edit, visibility
 * @author gbouteil 
 */
public class ManagedCategoryFeatures extends ManagedElementFeatures {
	
	/*
	 *********************** PROPERTIES**************************************/ 
	
	/**
	 * Log instance 
	 */
	protected static final Log log = LogFactory.getLog(ManagedCategoryFeatures.class);
	
// Used later	
//	/**
//	 * Remote managed category edit mode : not used for the moment
//	 * Using depends on trustCategory parameter
//	 */	
//	private Editability edit;
	

	
	/*
	 ********************* INIT *********************************************/
	
	/** 
	 * Constructor
	 * @param mcp  Managed category profile concerned by these features
	 */
	protected ManagedCategoryFeatures(ManagedCategoryProfile mcp){
		super(mcp);
	}

	/*
	 *********************** METHODS **************************************/
	
	/**
	 * Used to update features directly, without any computing
	 * It only sets value in parameter
	 * @param visibility the visibility feature to update
	 */
	@Override
	protected void update(/*Editability edit, */ VisibilitySets visibility) {
		if (log.isDebugEnabled()){
			log.debug("update(visibility)");
		}
		super.update(visibility);
		//this.edit = edit;
		
	}
	
	/*
	 *********************** ACCESSORS **************************************/ 
	
}
