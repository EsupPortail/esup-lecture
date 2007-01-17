/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.beans;

import org.esupportail.lecture.exceptions.domain.DomainServiceException;
import org.esupportail.lecture.exceptions.domain.UnknownException;

/**
 * A DummyBean for a CategoryBean : every methods are overriden from SourceBean throw the cause Exception
 * @author gbouteil
 *
 */
public class CategoryDummyBean extends CategoryBean implements DummyBean {
	// TODO (GB later) peut etre voir une autre conception :
	//categoryBean et ctageoryDummyBean étendent tous les deux une meme classe :
	// categoryInterBean (par ex) pour assurer que categoryDummyBean a les memes méthodes
	// que l'autre
	
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
	public CategoryDummyBean() {
		cause = new UnknownException();
	}
	/**
	 * Constructor
	 * @param e exception cause of dummyBean creation
	 */
	public CategoryDummyBean(DomainServiceException e){
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
	 * @see org.esupportail.lecture.domain.beans.CategoryBean#getDescription()
	 */
	@Override
	public String getDescription() throws DomainServiceException {
		throw cause;
	}

	
	/**
	 * @see org.esupportail.lecture.domain.beans.CategoryBean#setDescription(java.lang.String)
	 */
	@Override
	public void setDescription(String description) throws DomainServiceException {
		throw cause;
	}
	
	/**
	 * @see org.esupportail.lecture.domain.beans.CategoryBean#isFolded()
	 */
	@Override
	public boolean isFolded() throws DomainServiceException {
		throw cause;
	}
	
	
	/**
	 * @see org.esupportail.lecture.domain.beans.CategoryBean#setFolded(boolean)
	 */
	@Override
	public void setFolded(boolean folded) throws DomainServiceException {
		throw cause;
	}
	
	/**
	 * @see org.esupportail.lecture.domain.beans.CategoryBean#getId()
	 */
	@Override
	public String getId() throws DomainServiceException {
		throw cause;
	}
	
	/**
	 * @see org.esupportail.lecture.domain.beans.CategoryBean#setId(java.lang.String)
	 */
	@Override
	public void setId(String id) throws DomainServiceException {
		throw cause;
	}
	
	/**
	 * @see org.esupportail.lecture.domain.beans.CategoryBean#getName()
	 */
	@Override
	public String getName() throws DomainServiceException {
		throw cause;
	}
	
	/**
	 * @see org.esupportail.lecture.domain.beans.CategoryBean#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) throws DomainServiceException {
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
