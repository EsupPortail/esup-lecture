package org.esupportail.lecture.exceptions.domain;

import org.dom4j.XPathException;

public class ComputeItemsException extends PrivateException {

	public ComputeItemsException(String string) {
		super(string);
	}

	public ComputeItemsException(String string,Exception e) {
		super(string,e);
	}


}
