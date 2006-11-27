package org.esupportail.lecture.domain.model;

import org.esupportail.lecture.exceptions.ComposantNotLoadedException;

/**
 * Class that contains computed features of a source :
 * It merges features  between managedSourceProfile
 * and its managedSource or else
 * @author gbouteil
 *
 */
public class ComputedManagedSourceFeatures extends ComputedManagedComposantFeatures {

	/*
	 *********************** PROPERTIES**************************************/ 
	private Accessibility access;
	
	
	
	
	/*
	 ********************* INITIALIZATION **************************************/
	protected ComputedManagedSourceFeatures(ManagedSourceProfile mcp) {
		super(mcp);
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
		super.update(setVisib);
		access = setAccess;
	}


	

	/*
	 *********************** ACCESSORS **************************************/ 
	/**
	 * @return Returns the access.
	 * @throws ComposantNotLoadedException 
	 */
	protected Accessibility getAccess() throws ComposantNotLoadedException {
		if (!super.isComputed()){
			super.compute();
		}
		return access;
	}



}
