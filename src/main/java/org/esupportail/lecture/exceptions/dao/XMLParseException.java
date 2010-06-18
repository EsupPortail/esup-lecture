package org.esupportail.lecture.exceptions.dao;

/**
 * @author vrepain
 *
 */
public class XMLParseException extends InfoDaoException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param msg
	 * @param e
	 */
	public XMLParseException(String msg, Exception e) {
		super(msg, e);
	}

}
