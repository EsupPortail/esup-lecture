package org.esupportail.lecture.exceptions.dao;

public class TimeoutException extends InfoDaoException {

	public TimeoutException(String msg, Exception e) {
		super(msg, e);
	}

	public TimeoutException(String msg) {
		super(msg);
	}

}
