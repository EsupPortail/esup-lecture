package org.esupportail.lecture.exceptions.domain;

/**
 * The Element is not found.
 * (it can be anything : customElement, CategoryProfile, Context, SourceProfile ...)
 * @author gbouteil
 */
public class ElementNotFoundException extends PrivateException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param string
	 */
	public ElementNotFoundException(String string) {
		super(string);
	}
	
	/**
	 * Default constructor.
	 */
	public ElementNotFoundException() {
		super();
	}

}
