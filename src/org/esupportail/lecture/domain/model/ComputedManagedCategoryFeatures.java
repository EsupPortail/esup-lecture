package org.esupportail.lecture.domain.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class that contains computed features of category :
 * It merges features (edit, visibility,tll) between managedCategoryProfile
 * and its managedCategory
 * @author gbouteil
 *
 */
public class ComputedManagedCategoryFeatures extends ComputedManagedElementFeatures {
	
	/*
	 *********************** PROPERTIES**************************************/ 
	
	/**
	 * Log instance 
	 */
	protected static final Log log = LogFactory.getLog(ComputedManagedCategoryFeatures.class);
	
//	/**
//	 * Remote managed category edit mode : not used for the moment
//	 * Using depends on trustCategory parameter
//	 */	
//	private Editability edit;
	

	
	/*
	 ********************* INITIALIZATION **************************************/
	
	/** 
	 * Constructor
	 * @param mcp  Managed category profile concerned by these features
	 */
	protected ComputedManagedCategoryFeatures(ManagedCategoryProfile mcp){
		super(mcp);
	}
	
	




	/*
	 *********************** METHODS **************************************/
	
	
	
	
	/**
	 * Update features simply
	 * It is called by the associated managed category profile when it has concretly computed features
	 * @param visibility
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
