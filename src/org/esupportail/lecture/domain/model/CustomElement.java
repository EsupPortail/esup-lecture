package org.esupportail.lecture.domain.model;


import org.esupportail.lecture.exceptions.domain.ElementNotFoundException;
import org.esupportail.lecture.exceptions.domain.ElementNotLoadedException;
import org.esupportail.lecture.exceptions.domain.ElementProfileNotFoundException;

/**
 * @author gbouteil
 * Customization of a user on an element
 */
public interface CustomElement {


	/**
	 * @return element name 
	 * @throws ElementNotFoundException
	 * @throws ElementProfileNotFoundException
	 * @throws ElementNotLoadedException
	 */
	public String getName() throws ElementNotFoundException, ElementProfileNotFoundException, ElementNotLoadedException;
	
	/**
	 * @return user profile : owner of this customElement
	 */
	public UserProfile getUserProfile();
	
	/**
	 * @return id of the element refered by this CustomElement
	 */
	public String getElementId();
	

	
}
