package org.esupportail.lecture.exceptions.domain;

import org.esupportail.commons.exceptions.EsupException;

public class DomainServiceException extends EsupException {
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
