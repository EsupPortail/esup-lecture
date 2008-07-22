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
	// TODO (GB later) peut etre voir une autre conception :
	//categoryBean et ctageoryDummyBean �tendent tous les deux une meme classe :
	// categoryInterBean (par ex) pour assurer que categoryDummyBean a les memes m�thodes
	// que l'autre
	
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
	}
	/**
	 * Constructor.
	 * @param e exception cause of dummyBean creation
	 */
	public CategoryDummyBean(final DomainServiceException e) {
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
	public String getDescription() throws ElementDummyBeanException {
		throw new ElementDummyBeanException("Method getDescription() not available on a CategoryDummyBean");
	}

	
	/**
	 * @see org.esupportail.lecture.domain.beans.CategoryBean#setDescription(java.lang.String)
	 */
	@Override 
	public void setDescription(@SuppressWarnings("unused") final String description) 
	throws ElementDummyBeanException {
		throw new ElementDummyBeanException("Method setDescription() not available on a CategoryDummyBean");
	}
	
	/**
	 * @see org.esupportail.lecture.domain.beans.CategoryBean#isFolded()
	 */
	@Override
	public boolean isFolded() throws ElementDummyBeanException {
		throw new ElementDummyBeanException("Method isFolded() not available on a CategoryDummyBean");
	}
	
	
	/**
	 * @see org.esupportail.lecture.domain.beans.CategoryBean#setFolded(boolean)
	 */
	@Override
	public void setFolded(@SuppressWarnings("unused") final	boolean folded) 
	throws ElementDummyBeanException {
		throw new ElementDummyBeanException("Method setFolded() not available on a CategoryDummyBean");
	}
	
	/**
	 * @see org.esupportail.lecture.domain.beans.CategoryBean#getId()
	 */
	@Override
	public String getId() throws ElementDummyBeanException {
		throw new ElementDummyBeanException("Method getId() not available on a CategoryDummyBean");
	}
	
	/**
	 * @see org.esupportail.lecture.domain.beans.CategoryBean#setId(java.lang.String)
	 */
	@Override
	public void setId(@SuppressWarnings("unused") final String id) throws ElementDummyBeanException {
		throw new ElementDummyBeanException("Method setId() not available on a CategoryDummyBean");
	}
	
	/**
	 * @see org.esupportail.lecture.domain.beans.CategoryBean#getName()
	 */
	@Override
	public String getName() throws ElementDummyBeanException {
		throw new ElementDummyBeanException("Method getName() not available on a CategoryDummyBean");
	}
	
	/**
	 * @see org.esupportail.lecture.domain.beans.CategoryBean#setName(java.lang.String)
	 */
	@Override
	public void setName(@SuppressWarnings("unused") final String name) throws ElementDummyBeanException {
		throw new ElementDummyBeanException("Method setName() not available on a CategoryDummyBean");
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
