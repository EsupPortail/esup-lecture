package org.esupportail.lecture.exceptions.domain;

public class InternalExternalException extends ExternalServiceException {

	public InternalExternalException(Exception e) {
		super(e);
	}
	
	public InternalExternalException(String message) {
		super(message);
	}
	
	public InternalExternalException(String message,Exception e) {
		super(message,e);
	}

}
