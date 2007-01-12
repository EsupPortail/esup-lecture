package org.esupportail.lecture.exceptions.web;

public class WebException  extends RuntimeException {

	/**
	 * Constructor.
	 * @param message 
	 */
	public WebException(final String message) {
		super(message);
	}

	/**
	 * Constructor.
	 * @param message 
	 * @param cause 
	 */
	public WebException(final String message, final Exception cause) {
		super(message, cause);
	}

}
