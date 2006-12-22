package org.esupportail.lecture.exceptions.domain;

public class InfoExternalException extends ExternalServiceException {
	public InfoExternalException(Exception e) {
		super(e);
	}
	
	public InfoExternalException(String message) {
		super(message);
	}
	
	public InfoExternalException(String message,Exception e) {
		super(message,e);
	}
}
