/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;

/**
 * Items display mode : 
 * In a source, the list of items can be displayed :
 * - with no special order
 * - unread item only
 * - unread item ordered in first 
 * @author gbouteil
 *
 */
/**
 * @author gbouteil
 *
 */
public enum ItemDisplayMode {
	/**
	 * All items are displayed with no special order .
	 */
	ALL,
	/**
	 * only unread items are displayed.
	 */
	UNREAD,
	/**
	 * unread items are in first, and read items are at the end.
	 */
	UNREADFIRST
	

}
