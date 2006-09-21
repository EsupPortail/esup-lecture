/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;


/**
 * Managed composant profile element
 * A managed composant profile can be a managed category or source profile
 * @author gbouteil
 *
 */
interface ManagedComposantProfile extends ComposantProfile {
	
	
	/**
	 * Initialization 
	 */
	public void init();
	/**
	 * Returns access mode of the composant
	 * @return access 
	 */
	public Accessibility getAccess(); 
	
	/**
	 * Sets access mode of the composant
	 * @param access access groups
	 */
	public void setAccess(Accessibility access);

	/**
	 * Returns visibility sets of the composant
	 * @return visibility 
	 */
	public VisibilitySets getVisibility();
	/**
	 * Sets visibility sets of the composant
	 * @param visibility
	 */
	public void setVisibility(VisibilitySets visibility);

	/**
	 * Returns ttl of the composant
	 * @return ttl 
	 */
	public int getTtl();
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
	 */
	public DefinitionSets getVisibilityAllowed();
	/**
	 * Sets autoSubscribed visibility group of the composant
	 * @param d subscribed group
	 */	
	public void setVisibilityAutoSubcribed(DefinitionSets d);
	/**
	 * Returns autoSubscribed visibility group of the composant
	 * @return autoSubscribed 
	 */
	public DefinitionSets getVisibilityAutoSubscribed();
	/**
	 * Sets obliged visibility group of the composant
	 * @param d obliged group
	 */	
	public void setVisibilityObliged(DefinitionSets d);
	/**
	 * Returns obliged visibility group of the composant
	 * @return d obliged group
	 */
	public DefinitionSets getVisibilityObliged();
	/**
	 * Computes rights on parameters shared between a ManagedComposantProfile and its
	 * ManagedComposant (edit, visibility)
	 */
	public void computeFeatures();
	

}
