/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;

/**
 * Editability mode of an element 
 * @author gbouteil
 *
 */
public enum Editability { 
	/**
	 * no editibility
	 */
	NO,
	/**
	 * Only personal element creation
	 */
	PERSONAL,
	/**
	 * Only managed element importation
	 */
	MANAGED,
	/**
	 * Personal element creation and managed element importaion
	 */
	ALL };