package org.esupportail.lecture.exceptions.dao;

/**
 * @author vrepain
 *
 */
public class SourceInterruptedException extends InfoDaoException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param msg
	 * @param e
	 */
	public SourceInterruptedException(String msg, Exception e) {
		super(msg, e);
	}

}
