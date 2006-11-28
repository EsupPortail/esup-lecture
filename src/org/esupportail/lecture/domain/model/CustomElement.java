package org.esupportail.lecture.domain.model;


import org.esupportail.lecture.exceptions.ElementNotFoundException;
import org.esupportail.lecture.exceptions.ElementNotLoadedException;
import org.esupportail.lecture.exceptions.ElementProfileNotFoundException;

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
	
	/**
	 * @return id of the custom element : used by database
	 */
	public int getId();
	
	/**
	 * @param id set id used by database
	 */
	public void setId(int id);
}
