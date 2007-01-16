package org.esupportail.lecture.exceptions.domain;

/**
 * @author gbouteil
 * The Element is not found 
 * (it can be anything : customElement, CategoryProfile, Context, SourceProfile ...)
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
