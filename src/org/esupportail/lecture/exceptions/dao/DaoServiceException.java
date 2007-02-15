package org.esupportail.lecture.exceptions.dao;

import org.esupportail.commons.exceptions.EsupException;

public class DaoServiceException extends Exception {
	public DaoServiceException(String message) {
		super(message);
	}
	public DaoServiceException(Exception cause) {
		super(cause);
	}
	public DaoServiceException(String message,Exception cause) {
		super(message,cause);
	}
}
