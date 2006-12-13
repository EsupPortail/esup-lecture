package org.esupportail.lecture.exceptions;

public class InfoFacadeServiceException extends FacadeServiceException {

	public InfoFacadeServiceException(Exception cause) {
		super(cause);
	}
	public InfoFacadeServiceException(String message) {
		super(message);
	}
	public InfoFacadeServiceException(String message,Exception cause) {
		super(message,cause);
	}
	

}
