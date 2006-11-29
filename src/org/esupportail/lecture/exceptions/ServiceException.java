package org.esupportail.lecture.exceptions;

import org.esupportail.commons.exceptions.EsupException;

public class ServiceException extends FacadeServiceException {

	public ServiceException(Exception e) {
		super(e);
	}

	
}
