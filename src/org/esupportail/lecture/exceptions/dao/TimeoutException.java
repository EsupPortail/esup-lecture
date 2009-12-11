package org.esupportail.lecture.exceptions.dao;

/**
 * @author vrepain
 *
 */
public class TimeoutException extends InfoDaoException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param msg
	 * @param e
	 */
	public TimeoutException(String msg, Exception e) {
		super(msg, e);
	}

	/**
	 * @param msg
	 */
	public TimeoutException(String msg) {
		super(msg);
	}

}
