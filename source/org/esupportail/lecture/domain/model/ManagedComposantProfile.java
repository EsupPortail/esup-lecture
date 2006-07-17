package org.esupportail.lecture.domain.model;


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
	public void setVisibilityAllowed(DefAndContentSets d);
	
	/**
	 * Returns allowed visibility group of the composant
	 * @return allowed 
	 */
	public DefAndContentSets getVisibilityAllowed();
	/**
	 * Sets autoSubscribed visibility group of the composant
	 * @param d subscribed group
	 */	
	public void setVisibilityAutoSubcribed(DefAndContentSets d);
	/**
	 * Returns autoSubscribed visibility group of the composant
	 * @return autoSubscribed 
	 */
	public DefAndContentSets getVisibilityAutoSubscribed();
	/**
	 * Sets obliged visibility group of the composant
	 * @param d obliged group
	 */	
	public void setVisibilityObliged(DefAndContentSets d);
	/**
	 * Returns obliged visibility group of the composant
	 * @return d obliged group
	 */
	public DefAndContentSets getVisibilityObliged();
	

}
