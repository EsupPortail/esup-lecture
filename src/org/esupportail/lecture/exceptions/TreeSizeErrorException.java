package org.esupportail.lecture.exceptions;

public class TreeSizeErrorException extends InfoFacadeServiceException {

	public TreeSizeErrorException(Exception cause) {
		super(cause);
	}
	public TreeSizeErrorException(String message) {
		super(message);
	}
	public TreeSizeErrorException(String message, Exception cause) {
		super(message,cause);
	}

}
