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
	
	private String itemXPath;
	
	private String xsltUrl;
	
	
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
	public void update(VisibilitySets setVisib, int setTtl, Accessibility setAccess, String setItemXPath, String setXsltURL) {
		super.update(setVisib,setTtl);
		access = setAccess;
		itemXPath = setItemXPath;
		xsltUrl = setXsltURL;
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


	/**
	 * @return Returns the itemXPath.
	 */
	protected String getItemXPath() {
		if (!super.isComputed()){
			super.compute();
		}
		return itemXPath;
	}


	/**
	 * @return Returns the xsltUrl.
	 */
	protected String getXsltUrl() {
		if (!super.isComputed()){
			super.compute();
		}
		return xsltUrl;
	}
}
