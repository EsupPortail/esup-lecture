/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;

import org.esupportail.lecture.exceptions.domain.ManagedCategoryNotLoadedException;

/**
 * Managed Element profile element
 * A managed Element profile can be a managed category or managed source profile
 * It references an element.
 * @author gbouteil
 *
 */
interface ManagedElementProfile extends ElementProfile {
		
	/**
	 * Returns visibility of the ManagedElementProfile, ready to use (taking care of inheritance rules).
	 * @return visibility 
	 * @throws ManagedCategoryNotLoadedException 
	 */
	VisibilitySets getVisibility() throws ManagedCategoryNotLoadedException;
	
	/**
	 * Sets visibility of the ManagedElementProfile (value defined in XML File).
	 * @param visibility
	 */
	void setVisibility(VisibilitySets visibility);
	
	/**
	 * Returns access mode of the ManagedElementProfile.
	 * @return access  
	 * @throws ManagedCategoryNotLoadedException 
	 */
	Accessibility getAccess() throws ManagedCategoryNotLoadedException; 
	
	/**
	 * Sets access mode of the ManagedElementProfile.
	 * @param access access groups
	 */
	void setAccess(Accessibility access);

	/**
	 * Return timeOut of the element.
	 * @return timeOut
	 * @throws ManagedCategoryNotLoadedException 
	 */
	int getTimeOut() throws ManagedCategoryNotLoadedException;
	
	/**
	 * Sets timeOut of the element.
	 * @param timeOut
	 */
	void setTimeOut(int timeOut);
	
	/**
	 * Returns ttl of the ManagedElementProfile.
	 * @return ttl 
	 * @throws ManagedCategoryNotLoadedException 
	 */
	int getTtl() throws ManagedCategoryNotLoadedException;


}
