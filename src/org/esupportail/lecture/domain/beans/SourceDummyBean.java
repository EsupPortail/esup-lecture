/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.beans;

import org.esupportail.lecture.domain.model.AvailabilityMode;
import org.esupportail.lecture.domain.model.ItemDisplayMode;
import org.esupportail.lecture.exceptions.domain.DomainServiceException;
import org.esupportail.lecture.exceptions.domain.ElementDummyBeanException;
import org.esupportail.lecture.exceptions.domain.UnknownException;

/**
 * A DummyBean for a SourceBean : every methods are overriden from SourceBean and throw an ElementDummyBeanException
 * @author gbouteil
 *
 */
public class SourceDummyBean extends SourceBean implements DummyBean {
	// TODO (GB) : Add ElementDummyBeanException
	
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
	public SourceDummyBean() {
		cause = new UnknownException();
	}
	/**
	 * Constructor
	 * @param e exception cause of dummyBean creation
	 */
	public SourceDummyBean(DomainServiceException e) {
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
	public String getName() throws ElementDummyBeanException {
		throw new ElementDummyBeanException("Method getName() not available on a SourceDummyBean");
	}
		
	/**
	 * @see org.esupportail.lecture.domain.beans.SourceBean#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) throws ElementDummyBeanException {
		throw new ElementDummyBeanException("Method setName() not available on a SourceDummyBean");
	}

	/**
	 * @see org.esupportail.lecture.domain.beans.SourceBean#getId()
	 */
	@Override
	public String getId() throws ElementDummyBeanException {
		throw new ElementDummyBeanException("Method getId() not available on a SourceDummyBean");
	}
	
	/**
	 * @see org.esupportail.lecture.domain.beans.SourceBean#setId(java.lang.String)
	 */
	@Override
	public void setId(String id) throws ElementDummyBeanException {
		throw new ElementDummyBeanException("Method setId() not available on a SourceDummyBean");
	}

	/**
	 * @see org.esupportail.lecture.domain.beans.SourceBean#getType()
	 */
	@Override
	public AvailabilityMode getType() throws ElementDummyBeanException {
		throw new ElementDummyBeanException("Method getType() not available on a SourceDummyBean");
	}
	
	/**
	 * @see org.esupportail.lecture.domain.beans.SourceBean#setType(org.esupportail.lecture.domain.model.AvailabilityMode)
	 */
	@Override
	public void setType(AvailabilityMode type) throws ElementDummyBeanException {
		throw new ElementDummyBeanException("Method setType() not available on a SourceDummyBean");
	}
	
	
	/**
	 * @see org.esupportail.lecture.domain.beans.SourceBean#getItemDisplayMode()
	 */
	@Override
	public ItemDisplayMode getItemDisplayMode() throws ElementDummyBeanException {
		throw new ElementDummyBeanException("Method getItemDisplayMode() not available on a SourceDummyBean");
	}


	/**
	 * @see org.esupportail.lecture.domain.beans.SourceBean#setItemDisplayMode(org.esupportail.lecture.domain.model.ItemDisplayMode)
	 */
	@Override
	public void setItemDisplayMode(ItemDisplayMode itemDisplayMode) throws ElementDummyBeanException {
		throw new ElementDummyBeanException("Method setItemDisplayMode() not available on a SourceDummyBean");
	}
	
	
	/*
	 *************************** METHODS *********************************** */		

	/**
	 * @see org.esupportail.lecture.domain.beans.DummyBean#toString()
	 */
	@Override
	public String toString(){
		return "SourceDummyBean created because: " + cause.getMessage();
	}
	
	
}
