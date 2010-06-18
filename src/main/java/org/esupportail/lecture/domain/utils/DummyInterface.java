/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.utils;

import org.esupportail.lecture.exceptions.LectureException;
/**
 * Bean simulator. It is created when a real bean cannot be create 
 * while throwing an exception is too much.
 * @author gbouteil
 */
public interface DummyInterface {

	/**
	 * @return the Exception causing DummyBean creation 
	 */
	LectureException getCause();
	
	/**
	 * @return message of the Exception cause
	 */
	String toString();
}
