package org.esupportail.lecture.exceptions.domain;

public class MappingNotFoundException extends PrivateException {

	public MappingNotFoundException(Exception e) {
		super(e);
	}

	public MappingNotFoundException(String string) {
		super(string);
	}

	public MappingNotFoundException() {
		super();
	}

}
