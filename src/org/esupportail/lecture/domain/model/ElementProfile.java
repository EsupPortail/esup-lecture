/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;




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
	 * Returns the name of the element profile.
	 * @return String name
	 */
	String getName();
	
	/**
	 * Sets the name of the element profile.
	 * @param name
	 */
	void setName(String name);
	
	
	/**
	 * Returns the id of the element profile (same id as the corresponding element).
	 * @return String id
	 */
	String getId();

	



	
}
