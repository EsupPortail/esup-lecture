package org.esupportail.lecture.exceptions.domain;

import org.apache.commons.configuration.ConfigurationException;

public class XMLFileException extends PrivateException {

	public XMLFileException(String errorMsg) {
		super(errorMsg);
	}

	public XMLFileException(String errorMsg, Exception e) {
		super(errorMsg,e);
	}

}
