package org.esupportail.lecture.exceptions;

import org.esupportail.commons.exceptions.EsupException;

public class FacadeServiceException extends EsupException{
	public FacadeServiceException(String message) {
		super(message);
	}
	public FacadeServiceException(Exception cause) {
		super(cause);
	}
	public FacadeServiceException(String message,Exception cause) {
		super(message,cause);
	}
}
