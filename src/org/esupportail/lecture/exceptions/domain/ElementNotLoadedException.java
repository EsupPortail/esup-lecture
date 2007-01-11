package org.esupportail.lecture.exceptions.domain;

public class ElementNotLoadedException extends InfoDomainException {

	public ElementNotLoadedException(String string) {
		super(string);
	}
	public ElementNotLoadedException(Exception e) {
		super(e);
	}
	public ElementNotLoadedException(String string,Exception e) {
		super(string,e);
	}


}
