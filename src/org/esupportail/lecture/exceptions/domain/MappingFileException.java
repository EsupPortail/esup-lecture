package org.esupportail.lecture.exceptions.domain;

import org.apache.commons.configuration.ConfigurationException;

public class MappingFileException extends XMLFileException {

	public MappingFileException(String string) {
		super(string);
	}

	public MappingFileException(String string, ConfigurationException e) {
		super(string,e);
	}

}
