package org.esupportail.lecture.domain.beans;

import org.esupportail.lecture.exceptions.domain.DomainServiceException;
import org.esupportail.lecture.exceptions.domain.UnknownException;

public class SourceDummyBean extends SourceBean implements DummyBean {
	// TODO (GB later) cf categoryDummyBean 
	
	DomainServiceException cause;
	
	public SourceDummyBean(){
		cause = new UnknownException();
	}
	public SourceDummyBean(DomainServiceException e){
		cause = e;
	}
	public DomainServiceException getCause() {
		return cause;
	}

	/**
	 * @return name of source
	 * @throws DomainServiceException 
	 */
	public String getName() throws DomainServiceException {
		throw cause;
	}
	
	/**
	 * @param name
	 * @throws DomainServiceException 
	 */
	public void setName(String name) throws DomainServiceException {
		throw cause;
	}
	/**
	 * @return id of source
	 * @throws DomainServiceException 
	 */
	public String getId() throws DomainServiceException {
		throw cause;
	}
	/**
	 * @param id
	 * @throws DomainServiceException 
	 */
	public void setId(String id) throws DomainServiceException {
		throw cause;
	}
	/**
	 * @return type of source
	 * @throws DomainServiceException 
	 */
	public String getType() throws DomainServiceException {
		throw cause;
	}
	/**
	 * @param type
	 * @throws DomainServiceException 
	 */
	public void setType(String type) throws DomainServiceException {
		throw cause;
	}
	
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString(){
		return cause.getMessage();
	}
	
	
}
