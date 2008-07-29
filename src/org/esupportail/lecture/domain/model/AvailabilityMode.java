/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;

/**
 * Availability mode of an element.
 * A user is
 * - subscribed to an element (for managed elements)
 * - not subscribed to an element (for managed elements)
 * - obliged to be subscribed to an element (for managed elements)
 * - owner of the element (for personnal elements)
 * (The element is visible, else availabiltiy has not got any signification)
 * @author gbouteil
 *
 */
// TODO (GB later) : A renommer (subscribeMode ?) : c'rest ambigue avec le VisibilityMode
// Le AvailabilityMode est utilisé pour répondre à getVisible...
// Le VisibilityMode est utilisé pour définir les réponses à getAvailableMode
public enum AvailabilityMode {
	
	/**
	 *  The (managed) source is subscribed by the user.
	 */
	SUBSCRIBED,
	/**
	 * The (managed) source is subscribed by the user.
	 */
	NOTSUBSCRIBED,
	/**
	 * The (managed) source is subscribed by the user 
	 * and he is obliged to : he cannot unsubscribe to.
	 */
	OBLIGED,
	/**
	 * The user is owner of the (personal) source.
	 */
	OWNER
}
