package org.esupportail.lecture.exceptions.domain;


public class SourceNotLoadedException extends ElementNotLoadedException {

	public SourceNotLoadedException(String string) {
		super(string);
	}

	public SourceNotLoadedException(Exception e) {
		super(e);
	}

	public SourceNotLoadedException(String errorMsg, ComputeFeaturesException e) {
		super(errorMsg,e);
	}

}
