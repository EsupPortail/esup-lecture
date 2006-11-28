/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;

import org.esupportail.lecture.exceptions.CategoryNotLoadedException;
import org.esupportail.lecture.exceptions.ElementNotLoadedException;


/**
 * Element profile 
 * An element profile can be a source or a category profile.
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
	 * Returns the name of the element profile (should be the same as the correcponding element)
	 * @return String name
	 */
	public String getName()throws ElementNotLoadedException;
	/**
	 * Sets the name of the element profile (should be the same as the correcponding element)
	 * @param name
	 */
	public void setName(String name);

//	/**
//	 * Getter of the property <tt>refreshTimer</tt>
//	 * @return  Returns the refreshTimer.
//	 */
//	public int getRefreshTimer();
//
//	/**
//	 * Setter of the property <tt>refreshTimer</tt>
//	 * @param refreshTimer  The refreshTimer to set.
//	 */
//	public void setRefreshTimer(int refreshTimer);
//	/**
//	 */
//	public void refresh();
//	/**
//	 */
//	public boolean isTimeToReload();
//	/**
//	 */
//	public void forceRefreshTimer();
}
