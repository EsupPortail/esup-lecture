package org.esupportail.lecture.domain.model;


import org.esupportail.lecture.exceptions.domain.ElementNotFoundException;

/**
 * @author gbouteil
 * Customization of an element for a user
 * An element can be a source, a category or a context
 */
public interface CustomElement {

	/**
	 * @return element name 
	 * @throws ElementNotFoundException
	 */
	public String getName()throws ElementNotFoundException;
	
	/**
	 * @return user profile : owner of this customElement
	 */
	public UserProfile getUserProfile();
	
	/**
	 * @return id of the element refered by this CustomElement
	 */
	public String getElementId();
	

	
}
