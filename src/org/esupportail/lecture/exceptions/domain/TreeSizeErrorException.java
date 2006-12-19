package org.esupportail.lecture.exceptions.domain;

public class TreeSizeErrorException extends InfoDomainException {

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
