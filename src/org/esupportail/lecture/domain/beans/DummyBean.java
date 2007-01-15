package org.esupportail.lecture.domain.beans;

import org.esupportail.lecture.exceptions.domain.DomainServiceException;

public interface DummyBean {

	public DomainServiceException getCause();
	
	public String toString();
}
