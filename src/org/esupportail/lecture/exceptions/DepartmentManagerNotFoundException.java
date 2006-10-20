/**
 * ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
 * http://sourcesup.cru.fr/projects/esup-lecture
 */
package org.esupportail.lecture.exceptions;

import org.esupportail.commons.exceptions.ObjectNotFoundException;

/**
 * An exception thrown when trying to retrieving non-existent departments.
 */
public class DepartmentManagerNotFoundException extends ObjectNotFoundException {
	/**
	 * the id for serialization.
	 */
	private static final long serialVersionUID = -2400087126758693830L;
	/**
	 * Bean constructor.
	 * @param message
	 * @param cause
	 */
	protected DepartmentManagerNotFoundException(final String message, final Exception cause) {
		super(message, cause);
	}
	/**
	 * Bean constructor.
	 * @param message
	 */
	public DepartmentManagerNotFoundException(final String message) {
		super(message);
	}
	/**
	 * Bean constructor.
	 * @param cause
	 */
	public DepartmentManagerNotFoundException(final Exception cause) {
		super(cause);
	}
}
