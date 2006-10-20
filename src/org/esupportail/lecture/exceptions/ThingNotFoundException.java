/**
 * ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
 * http://sourcesup.cru.fr/projects/esup-lecture
 */
package org.esupportail.lecture.exceptions;

import org.esupportail.commons.exceptions.ObjectNotFoundException;

/**
 * An exception thrown when trying to retrieving non-existent things.
 */
public class ThingNotFoundException extends ObjectNotFoundException {

	/**
	 * The id for serialization.
	 */
	private static final long serialVersionUID = 786801176096817728L;

	/**
	 * Bean constructor.
	 * @param message
	 * @param cause
	 */
	protected ThingNotFoundException(final String message, final Exception cause) {
		super(message, cause);
	}
	
	/**
	 * Bean constructor.
	 * @param message
	 */
	public ThingNotFoundException(final String message) {
		super(message);
	}
	
	/**
	 * Bean constructor.
	 * @param cause
	 */
	public ThingNotFoundException(final Exception cause) {
		super(cause);
	}
}
