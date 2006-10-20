/**
 * ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
 * http://sourcesup.cru.fr/projects/esup-lecture
 */
package org.esupportail.lecture.exceptions;

import org.esupportail.commons.exceptions.ObjectNotFoundException;

/**
 * An exception thrown when trying to retrieving non-existent departments.
 */
public class DepartmentNotFoundException extends ObjectNotFoundException {

	/**
	 * the id for serialization.
	 */
	private static final long serialVersionUID = 5562762090023429413L;
	
	/**
	 * Bean constructor.
	 * @param message
	 * @param cause
	 */
	protected DepartmentNotFoundException(final String message, final Exception cause) {
		super(message, cause);
	}
	
	/**
	 * Bean constructor.
	 * @param message
	 */
	public DepartmentNotFoundException(final String message) {
		super(message);
	}
	
	/**
	 * Bean constructor.
	 * @param cause
	 */
	public DepartmentNotFoundException(final Exception cause) {
		super(cause);
	}
}
