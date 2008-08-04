/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;

/**
 * An element can be a source or a category.
 * @author gbouteil
 *
 */
interface Element {
		
	/**
	 * @return the profile associated to this element
	 */
	ElementProfile getProfile();
	
	/**
	 * @return the profile ID
	 */
	String getProfileId();
	
	/**
	 * @return the element name
	 */
	String getName();
	
}
