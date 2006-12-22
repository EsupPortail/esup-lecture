package org.esupportail.lecture.exceptions.domain;

public class NoExternalValueException extends InternalExternalException {
	public NoExternalValueException(Exception e) {
		super(e);
	}
	
	public NoExternalValueException(String message) {
		super(message);
	}
	
	public NoExternalValueException(String message,Exception e) {
		super(message,e);
	}
}
