/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;

/**
 * Editability mode of an element :
 * - no : no editibility
 * - personal : only personal element creation
 * - managed : only managed eleemnt importation
 * - all : personal element creation and managed element importaion
 * @author gbouteil
 *
 */
public enum Editability { NO,PERSONAL,MANAGED,ALL };