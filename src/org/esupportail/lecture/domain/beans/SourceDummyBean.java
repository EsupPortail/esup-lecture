/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.beans;

import org.esupportail.lecture.domain.model.AvailabilityMode;
import org.esupportail.lecture.exceptions.domain.DomainServiceException;
import org.esupportail.lecture.exceptions.domain.UnknownException;

/**
 * A DummyBean for a SourceBean : every methods are overriden from SourceBean throw the cause Exception
 * @author gbouteil
 *
 */
public class SourceDummyBean extends SourceBean implements DummyBean {
	// TODO (GB later ) :cf TODO de CategoryDummyBean
	
	/* 
	 *************************** PROPERTIES ******************************** */	
	/**
	 * Cause of the Dummy Bean
	 */
	DomainServiceException cause;

	/*
	 *************************** INIT ************************************** */	

	/**
	 * default constructor
	 */
	public SourceDummyBean(){
		cause = new UnknownException();
	}
	/**
	 * Constructor
	 * @param e exception cause of dummyBean creation
	 */
	public SourceDummyBean(DomainServiceException e){
		cause = e;
	}

	/*
	 *************************** ACCESSORS ********************************* */	
	/**
	 * @see org.esupportail.lecture.domain.beans.DummyBean#getCause()
	 */
	public DomainServiceException getCause() {
		return cause;
	}

	/**
	 * @see org.esupportail.lecture.domain.beans.SourceBean#getName()
	 */
	@Override
	public String getName() throws DomainServiceException {
		throw cause;
	}
		
	/**
	 * @see org.esupportail.lecture.domain.beans.SourceBean#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) throws DomainServiceException {
		throw cause;
	}

	/**
	 * @see org.esupportail.lecture.domain.beans.SourceBean#getId()
	 */
	@Override
	public String getId() throws DomainServiceException {
		throw cause;
	}
	
	/**
	 * @see org.esupportail.lecture.domain.beans.SourceBean#setId(java.lang.String)
	 */
	@Override
	public void setId(String id) throws DomainServiceException {
		throw cause;
	}

	/**
	 * @see org.esupportail.lecture.domain.beans.SourceBean#getType()
	 */
	@Override
	public AvailabilityMode getType() throws DomainServiceException {
		throw cause;
	}
	
	/**
	 * @see org.esupportail.lecture.domain.beans.SourceBean#setType(java.lang.String)
	 */
	@Override
	public void setType(AvailabilityMode type) throws DomainServiceException {
		throw cause;
	}
	
	/*
	 *************************** METHODS *********************************** */		

	/**
	 * @see org.esupportail.lecture.domain.beans.DummyBean#toString()
	 */
	@Override
	public String toString(){
		return cause.getMessage();
	}
	
	
}
