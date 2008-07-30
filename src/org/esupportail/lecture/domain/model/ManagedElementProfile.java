/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;

import org.esupportail.lecture.exceptions.domain.CategoryNotLoadedException;
import org.esupportail.lecture.exceptions.domain.ElementNotLoadedException;

/**
 * Managed Element profile element
 * A managed Element profile can be a managed category or managed source profile
 * It references an element.
 * @author gbouteil
 *
 */
interface ManagedElementProfile extends ElementProfile {
		
	/**
	 * Returns access mode of the Element.
	 * @return access 
	 * @throws CategoryNotLoadedException 
	 */
	Accessibility getAccess() throws CategoryNotLoadedException; 
	
	/**
	 * Sets access mode of the Element.
	 * @param access access groups
	 */
	void setAccess(Accessibility access);

	/**
	 * Returns visibility sets of the Element.
	 * @return visibility 
	 * @throws CategoryNotLoadedException 
	 */
	VisibilitySets getVisibility() throws  CategoryNotLoadedException;
	
	/**
	 * Sets visibility sets of the Element.
	 * @param visibility
	 */
	void setVisibility(VisibilitySets visibility);

	/**
	 * Returns ttl of the Element.
	 * @return ttl 
	 * @throws ElementNotLoadedException 
	 */
	int getTtl() throws ElementNotLoadedException;
	
	/**
	 * Sets ttl of the Element.
	 * @param ttl
	 */		
	void setTtl(int ttl);
	
	/**
	 * Sets allowed visibility group of the Element
	 * @param d allowed group
	 */	
//	public void setVisibilityAllowed(DefinitionSets d);
//	
//	/**
//	 * Returns allowed visibility group of the Element
//	 * @return allowed 
//	 * @throws ElementNotLoadedException 
//	 */
//	public DefinitionSets getVisibilityAllowed() ;
//	/**
//	 * Sets autoSubscribed visibility group of the Element
//	 * @param d subscribed group
//	 */	
//	public void setVisibilityAutoSubcribed(DefinitionSets d);
//	/**
//	 * Returns autoSubscribed visibility group of the Element
//	 * @return autoSubscribed 
//	 * @throws ElementNotLoadedException 
//	 */
//	public DefinitionSets getVisibilityAutoSubscribed();
//	/**
//	 * Sets obliged visibility group of the Element
//	 * @param d obliged group
//	 */	
//	public void setVisibilityObliged(DefinitionSets d);
//	/**
//	 * Returns obliged visibility group of the Element
//	 * @return d obliged group
//	 * @throws ElementNotLoadedException 
//	 */
//	public DefinitionSets getVisibilityObliged();


}
