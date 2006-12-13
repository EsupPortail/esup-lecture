package org.esupportail.lecture.exceptions;

public class DomainException extends Exception {
	public DomainException(Exception e) {
		super(e);
	}
	
	public DomainException(String message) {
		super(message);
	}
}
