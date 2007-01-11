package org.esupportail.lecture.exceptions.domain;

public class ComputeFeaturesException extends PrivateException {

	public ComputeFeaturesException(CategoryNotLoadedException e) {
		super(e);
	}

	public ComputeFeaturesException(String string, CategoryNotLoadedException e) {
		super(string,e);
	}

}
