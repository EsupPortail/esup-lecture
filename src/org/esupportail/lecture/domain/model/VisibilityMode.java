/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;

/**
 * Visibility mode of a managed element : 
 * user is
 * - allowed to subscribe an element
 * - autosubscribe to an element
 * - obliged to be subscribe to an element
 * - is not allowed to subscribe it : the element is not visble
 * @author gbouteil
 *
 */
public enum VisibilityMode {
	/**
	 * Obliged visibility : automatic subscription with impossible unsubcription 
	 */
	OBLIGED,
	/**
	 * AutoSubscribed visibility : automatic subscription with possible unsubcription 
	 */
	AUTOSUBSCRIBED,
	/**
	 * Allowed visibility : subscription and unsubcription possible 
	 */
	ALLOWED,
	/**
	 * No visibility
	 */
	NOVISIBLE
}