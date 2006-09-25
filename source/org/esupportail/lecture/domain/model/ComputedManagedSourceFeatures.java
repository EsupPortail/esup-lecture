package org.esupportail.lecture.domain.model;

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
	 * @param setTtl ttl
	 * @param setAccess access
	 * @param setItemXPath itemXpath
	 * @param setXsltURL xsltURL
	 */
	public void update(VisibilitySets setVisib, int setTtl, Accessibility setAccess) {
		super.update(setVisib,setTtl);
		access = setAccess;
	}


	

	/*
	 *********************** ACCESSORS **************************************/ 
	/**
	 * @return Returns the access.
	 */
	protected Accessibility getAccess() {
		if (!super.isComputed()){
			super.compute();
		}
		return access;
	}



}
