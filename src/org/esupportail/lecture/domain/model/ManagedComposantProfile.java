/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;

import org.esupportail.lecture.exceptions.CategoryNotLoadedException;
import org.esupportail.lecture.exceptions.ComposantNotLoadedException;


/**
 * Managed composant profile element
 * A managed composant profile can be a managed category or source profile
 * @author gbouteil
 *
 */
interface ManagedComposantProfile extends ComposantProfile {
	
	
	
	/**
	 * Returns access mode of the composant
	 * @return access 
	 * @throws ComposantNotLoadedException 
	 */
	public Accessibility getAccess() throws ComposantNotLoadedException; 
	
	/**
	 * Sets access mode of the composant
	 * @param access access groups
	 */
	public void setAccess(Accessibility access);

	/**
	 * Returns visibility sets of the composant
	 * @return visibility 
	 * @throws ComposantNotLoadedException 
	 */
	public VisibilitySets getVisibility() throws ComposantNotLoadedException;
	/**
	 * Sets visibility sets of the composant
	 * @param visibility
	 */
	public void setVisibility(VisibilitySets visibility);

	/**
	 * Returns ttl of the composant
	 * @return ttl 
	 * @throws ComposantNotLoadedException 
	 */
	public int getTtl() throws ComposantNotLoadedException;
	/**
	 * Sets ttl of the composant
	 * @param ttl
	 */	
	public void setTtl(int ttl) ;
	/**
	 * Sets allowed visibility group of the composant
	 * @param d allowed group
	 */	
	public void setVisibilityAllowed(DefinitionSets d);
	
	/**
	 * Returns allowed visibility group of the composant
	 * @return allowed 
	 * @throws ComposantNotLoadedException 
	 */
	public DefinitionSets getVisibilityAllowed() throws ComposantNotLoadedException;
	/**
	 * Sets autoSubscribed visibility group of the composant
	 * @param d subscribed group
	 */	
	public void setVisibilityAutoSubcribed(DefinitionSets d);
	/**
	 * Returns autoSubscribed visibility group of the composant
	 * @return autoSubscribed 
	 * @throws ComposantNotLoadedException 
	 */
	public DefinitionSets getVisibilityAutoSubscribed() throws ComposantNotLoadedException;
	/**
	 * Sets obliged visibility group of the composant
	 * @param d obliged group
	 */	
	public void setVisibilityObliged(DefinitionSets d);
	/**
	 * Returns obliged visibility group of the composant
	 * @return d obliged group
	 * @throws ComposantNotLoadedException 
	 */
	public DefinitionSets getVisibilityObliged() throws ComposantNotLoadedException;
	/**
	 * Computes rights on parameters shared between a ManagedComposantProfile and its
	 * ManagedComposant (edit, visibility)
	 * @throws ComposantNotLoadedException 
	 */
	public void computeFeatures() throws ComposantNotLoadedException;
	

}
