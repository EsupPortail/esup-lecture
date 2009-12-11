package org.esupportail.lecture.exceptions.dao;

import org.esupportail.lecture.exceptions.LectureException;

/**
 * @author vrepain
 *
 */
public class DaoServiceException extends LectureException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	public DaoServiceException() {
		super();
	}
	/**
	 * @param message
	 */
	public DaoServiceException(String message) {
		super(message);
	}
	/**
	 * @param cause
	 */
	public DaoServiceException(Exception cause) {
		super(cause);
	}
	/**
	 * @param message
	 * @param cause
	 */
	public DaoServiceException(String message,Exception cause) {
		super(message,cause);
	}
}
