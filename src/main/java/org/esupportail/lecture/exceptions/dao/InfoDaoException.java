package org.esupportail.lecture.exceptions.dao;


/**
 * @author vrepain
 *
 */
public class InfoDaoException extends DaoServiceException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	public InfoDaoException() {
		super();
	}
	
	/**
	 * @param cause
	 */
	public InfoDaoException(Exception cause) {
		super(cause);
	}
	/**
	 * @param message
	 */
	public InfoDaoException(String message) {
		super(message);
	}
	/**
	 * @param message
	 * @param cause
	 */
	public InfoDaoException(String message,Exception cause) {
		super(message,cause);
	}
	

}
