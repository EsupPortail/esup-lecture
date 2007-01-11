package org.esupportail.lecture.exceptions.domain;

public class CategoryNotVisibleException extends InfoDomainException {

	public CategoryNotVisibleException(Exception e) {
		super(e);
	}
	public CategoryNotVisibleException(String message) {
		super(message);
	}

}
