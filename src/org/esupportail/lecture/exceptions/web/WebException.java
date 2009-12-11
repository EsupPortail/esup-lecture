package org.esupportail.lecture.exceptions.web;

/**
 * @author vrepain
 *
 */
public class WebException  extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
