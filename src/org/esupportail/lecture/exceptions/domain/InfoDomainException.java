package org.esupportail.lecture.exceptions.domain;


public class InfoDomainException extends DomainServiceException {

	public InfoDomainException(Exception cause) {
		super(cause);
	}
	public InfoDomainException(String message) {
		super(message);
	}
	public InfoDomainException(String message,Exception cause) {
		super(message,cause);
	}
	

}
