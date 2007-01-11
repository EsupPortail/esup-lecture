package org.esupportail.lecture.domain.beans;

import org.esupportail.lecture.exceptions.domain.DomainServiceException;
import org.esupportail.lecture.exceptions.domain.UnknownException;

public class CategoryDummyBean extends CategoryBean implements DummyBean {
	// TODO (GB later) peut etre voir une autre conception :
	//categoryBean et ctageoryDummyBean étendent tous les deux une meme classe :
	// categoryInterBean (par ex) pour assurer que categoryDummyBean a les memes méthodes
	// que l'autre
	DomainServiceException cause;
	
	public CategoryDummyBean() {
		cause = new UnknownException();
	}
	public CategoryDummyBean(DomainServiceException e){
		cause = e;
	}
	
	public DomainServiceException getCause() {
		return cause;
	}

	public String getDescription() throws DomainServiceException {
		throw cause;
	}

	/**
	 * @param description description of the category
	 * @throws DomainServiceException 
	 */
	public void setDescription(String description) throws DomainServiceException {
		throw cause;
	}
	/**
	 * @return if category is folded or not
	 * @throws DomainServiceException 
	 */
	public boolean isFolded() throws DomainServiceException {
		throw cause;
	}
	/**
	 * @param folded
	 * @throws DomainServiceException 
	 */
	public void setFolded(boolean folded) throws DomainServiceException {
		throw cause;
	}
	/**
	 * @return id of category
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
	 * @return name of category
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
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString(){
		return cause.getMessage();
	}
	
	
}
