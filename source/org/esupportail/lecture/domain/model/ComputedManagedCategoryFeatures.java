package org.esupportail.lecture.domain.model;

/**
 * Class that contains computed features of category :
 * It merges features (edit, visibility,tll) between managedCategoryProfile
 * and its managedCategory
 * @author gbouteil
 *
 */
public class ComputedManagedCategoryFeatures extends ComputedManagedComposantFeatures {
	
	/*
	 *********************** PROPERTIES**************************************/ 
	
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
	 * @param ttl
	 */
	protected void update(/*Editability edit, */ VisibilitySets visibility, int ttl) {
		super.update(visibility,ttl);
		//this.edit = edit;
		
	}

	
	
	/*
	 *********************** ACCESSORS **************************************/ 
	



	

	
	
	



	

	
	
	
}
