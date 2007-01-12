/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;

import org.esupportail.lecture.exceptions.domain.ElementNotLoadedException;


/**
 * Element profile : it covers an element and is referred by a customElement to be 
 * used in a userProfile
 * An element profile can be a sourceProfile or a categoryProfile.
 * @author gbouteil
 *
 */
public interface ElementProfile {

/* ************************** ACCESSORS ******************************** */	
	/**
	 * Returns the id of the element profile (same id as the corresponding element)
	 * @return String id
	 */
	public String getId();
	/**
	 * Sets the id of the element profile (same id as the corresponding element)
	 * @param id
	 */
	public void setId(String id);
	/**
	 * Returns the name of the element profile (should be the same as the corresponding element)
	 * @return String name
	 */
	public String getName();
	/**
	 * Sets the name of the element profile (should be the same as the correcponding element)
	 * @param name
	 */
	public void setName(String name);
	
	/**
	 * @return the element associated to this profile
	 * @throws ElementNotLoadedException 
	 * @throws ElementNotLoadedException
	 */
	public Element getElement() throws ElementNotLoadedException ;
	

}
