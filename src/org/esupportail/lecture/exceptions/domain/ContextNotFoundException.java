package org.esupportail.lecture.exceptions.domain;

import org.esupportail.commons.exceptions.EsupException;

public class ContextNotFoundException extends ElementNotFoundException {

	public ContextNotFoundException(String string) {
		super(string);
	}

	public ContextNotFoundException() {
		super();
	}
	

}
