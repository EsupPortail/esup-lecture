package org.esupportail.lecture.exceptions.domain;

import org.apache.commons.configuration.ConfigurationException;

public class ChannelConfigException extends XMLFileException {

	public ChannelConfigException(String errorMsg) {
		super(errorMsg);
	}

	public ChannelConfigException(String errorMsg, Exception e) {
		super(errorMsg,e);
	}

	

}
