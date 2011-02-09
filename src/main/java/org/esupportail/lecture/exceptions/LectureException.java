/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-lecture.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.exceptions;

/**
 * @author gbouteil
 * Most global Exception in esup-lecture
 *
 */
public class LectureException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Default constructor 
	 */
	public LectureException() {
		super();
	}
	
	/**
	 * @param message
	 */
	public LectureException(String message) {
		super(message);
	}
	/**
	 * @param cause
	 */
	public LectureException(Exception cause) {
		super(cause);
	}
	/**
	 * @param message
	 * @param cause
	 */
	public LectureException(String message,Exception cause) {
		super(message,cause);
	}
}
