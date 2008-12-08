package org.esupportail.lecture.exceptions.dao;


public class InfoDaoException extends DaoServiceException {

	public InfoDaoException() {
		super();
	}
	
	public InfoDaoException(Exception cause) {
		super(cause);
	}
	public InfoDaoException(String message) {
		super(message);
	}
	public InfoDaoException(String message,Exception cause) {
		super(message,cause);
	}
	

}
