package org.esupportail.lecture.exceptions.dao;

import org.esupportail.lecture.exceptions.LectureException;

public class DaoServiceException extends LectureException {
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
