package org.esupportail.lecture.domain.model;


import org.esupportail.lecture.exceptions.domain.ElementNotFoundException;

/**
 * @author gbouteil
 * Customization of a user on an element
 */
public interface CustomElement {


	/**
	 * @return element name 
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
