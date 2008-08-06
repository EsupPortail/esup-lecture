/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;


import org.esupportail.lecture.exceptions.domain.InternalDomainException;
import org.esupportail.lecture.exceptions.domain.ManagedCategoryNotLoadedException;

/**
 * @author gbouteil
 * Customization of an element for a user
 * An element can be a source, a category or a context
 */
public interface CustomElement {

	/**
	 * @return element name 
	 * @throws InternalDomainException 
	 * @throws ManagedCategoryNotLoadedException 
	 */
	String getName() throws InternalDomainException, ManagedCategoryNotLoadedException;
	
	/**
	 * @return user profile : owner of this customElement
	 */
	UserProfile getUserProfile();
	
	/**
	 * @return id of the element refered by this CustomElement
	 */
	String getElementId();
	

	
}
