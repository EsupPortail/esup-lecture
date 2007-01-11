package org.esupportail.lecture.exceptions.domain;


public class DomainServiceException extends Exception {
	public DomainServiceException() {
		super();
	}
	public DomainServiceException(String message) {
		super(message);
	}
	public DomainServiceException(Exception cause) {
		super(cause);
	}
	public DomainServiceException(String message,Exception cause) {
		super(message,cause);
	}
}
