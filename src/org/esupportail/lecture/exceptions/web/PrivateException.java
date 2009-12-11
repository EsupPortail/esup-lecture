package org.esupportail.lecture.exceptions.web;

/**
 * @author vrepain
 *
 */
public class PrivateException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param e
	 */
	public PrivateException(Exception e) {
		super(e);
	}
	
	/**
	 * @param message
	 */
	public PrivateException(String message) {
		super(message);
	}
}
