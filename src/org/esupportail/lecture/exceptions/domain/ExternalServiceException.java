package org.esupportail.lecture.exceptions.domain;


public class ExternalServiceException extends Exception {
	public ExternalServiceException(Exception e) {
		super(e);
	}
	
	public ExternalServiceException(String message) {
		super(message);
	}
	
	public ExternalServiceException(String message,Exception e) {
		super(message,e);
	}
}
