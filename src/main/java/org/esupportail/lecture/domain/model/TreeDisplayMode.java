/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;

/**
 * Tree display mode 
 * @author bourges
 *
 */
public enum TreeDisplayMode {
	/**
	 * Tree not visible but user can force its visibility
	 */
	NOTVISIBLE,
	/**
	 * Tree visible and user can hide it
	 */
	VISIBLE,
	/**
	 * Tree not visible and user CAN NOT force its visibility
	 */
	NEVERVISIBLE
}
