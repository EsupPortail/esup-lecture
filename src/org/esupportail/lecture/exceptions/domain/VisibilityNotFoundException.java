package org.esupportail.lecture.exceptions.domain;

public class VisibilityNotFoundException extends PrivateException {

	public VisibilityNotFoundException(String string) {
		super(string);
	}

	public VisibilityNotFoundException(ElementNotLoadedException e) {
		super(e);
	}

}
