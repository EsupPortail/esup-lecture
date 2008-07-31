/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;

import org.esupportail.lecture.exceptions.domain.CategoryNotLoadedException;


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
	 * Returns the id of the element profile (same id as the corresponding element).
	 * @return String id
	 */
	String getId();
	/**
	 * Sets the id of the element profile (same id as the corresponding element).
	 * @param id
	 */
	 void setId(String id);
	/**
	 * Returns the name of the element profile (the same as the referrencing element).
	 * @return String name
	 */
	String getName();
	
// 	GB : pour restreindre la visibilité	
//	/**
//	 * Sets the name of the element profile (should be the same as the corresponding element).
//	 * @param name
//	 * @throws CategoryNotLoadedException 
//	 */
//	void setName(String name) throws CategoryNotLoadedException;
//
//	/**
//	 * @return the element associated to this profile
//	 * @throws ElementNotLoadedException 
//	 */
//	Element getElement() throws ElementNotLoadedException;
	
}
