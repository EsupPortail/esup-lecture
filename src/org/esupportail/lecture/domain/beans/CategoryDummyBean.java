/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.beans;

import org.esupportail.lecture.exceptions.domain.DomainServiceException;
import org.esupportail.lecture.exceptions.domain.ElementDummyBeanException;
import org.esupportail.lecture.exceptions.domain.UnknownException;

/**
 * A DummyBean for a CategoryBean : every methods are overriden from CategoryBean
 * and throw an ElementDummyBeanException.
 * @author gbouteil
 *
 */
public class CategoryDummyBean extends CategoryBean implements DummyBean {
	// TODO (RB/GB) Revoir le traitement des Dummy (autre que par des ifs) 
	// pour faire du vrai objet
	
	/* 
	 *************************** PROPERTIES ******************************** */	
	/**
	 * Cause of the Dummy Bean.
	 */
	private DomainServiceException cause;
	
	/*
	 *************************** INIT ************************************** */	
	/**
	 * default constructor.
	 */
	public CategoryDummyBean() {
		cause = new UnknownException();
		super.setId(null);
		super.setName(null);
		super.setType(null);
		super.setDescription(null);
		super.setFolded(false);
	
	}
	/**
	 * Constructor.
	 * @param e exception cause of dummyBean creation.
	 */
	public CategoryDummyBean(final DomainServiceException e) {
		new SourceDummyBean();
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


	/*
	 *************************** METHODS *********************************** */
	
	/**
	 * @see org.esupportail.lecture.domain.beans.DummyBean#toString()
	 */
	@Override
	public String toString() {
		return "CategoryDummyBean created because: " + cause.getMessage();
	}
	
	
	
	
}
