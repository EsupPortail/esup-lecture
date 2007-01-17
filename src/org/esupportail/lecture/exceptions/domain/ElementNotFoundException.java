package org.esupportail.lecture.exceptions.domain;

/**
 * The Element is not found 
 * (it can be anything : customElement, CategoryProfile, Context, SourceProfile ...)
 * @author gbouteil
 */
public class ElementNotFoundException extends InfoDomainException {

	/**
	 * @param string
	 */
	public ElementNotFoundException(String string) {
		super(string);
	}
	
	/**
	 * Default constructor
	 */
	public ElementNotFoundException() {
		super();
	}

}
