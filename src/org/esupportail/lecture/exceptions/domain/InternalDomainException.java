package org.esupportail.lecture.exceptions.domain;

import org.esupportail.commons.exceptions.EsupException;

public class InternalDomainException extends DomainServiceException {

	public InternalDomainException(Exception e) {
		super(e);
	}

	public InternalDomainException(String errorMsg, Exception e) {
		super(errorMsg,e);
	}

                                                                                                                                                                                                                                                                                                                                                                   
	
}
