/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.utils.exception;


/**
 * Exception throws by channel code. Destinated to be concepted better later.
 * @author gbouteil
 *
 */
public class MyException extends java.lang.RuntimeException {

	
	
	/**
	 * Exception constructor with exception message in parameter
	 * @param message
	 */
	public MyException(String message){
		super(message);
	}
	
	
}