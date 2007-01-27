/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.beans;

import org.esupportail.lecture.domain.model.AvailabilityMode;
import org.esupportail.lecture.domain.model.CustomSource;
import org.esupportail.lecture.domain.model.ElementProfile;
import org.esupportail.lecture.domain.model.ProfileAvailability;
import org.esupportail.lecture.domain.model.SourceProfile;
import org.esupportail.lecture.exceptions.domain.CategoryNotLoadedException;
import org.esupportail.lecture.exceptions.domain.DomainServiceException;
import org.esupportail.lecture.exceptions.domain.ManagedCategoryProfileNotFoundException;
import org.esupportail.lecture.exceptions.domain.SourceProfileNotFoundException;

/**
 * used to store source informations
 * @author bourges
 */
public class SourceBean {
	
	/* 
	 *************************** PROPERTIES ******************************** */	
	/**
	 * id of source
	 */
	private String id;
	/**
	 * name of source
	 */
	private String name;
	/**
	 * type of source
	 * "subscribed" --> The source is alloweb and subscribed by the user
	 * "notSubscribed" --> The source is alloweb and not yet subscribed by the user (used in edit mode)
	 * "obliged" --> The source is obliged: user can't subscribe or unsubscribe this source
	 * "owner" --> For personal sources
	 */
	private AvailabilityMode type;

	/*
	 *************************** INIT ************************************** */	
	
	/**
	 * default constructor
	 */
	public SourceBean(){}
	
	/**
	 * constructor initializing object with a customSource
	 * @param customSource
	 * @throws SourceProfileNotFoundException 
	 * @throws CategoryNotLoadedException 
	 * @throws ManagedCategoryProfileNotFoundException 
	 */
	public SourceBean(CustomSource customSource) throws ManagedCategoryProfileNotFoundException, CategoryNotLoadedException, SourceProfileNotFoundException{
		SourceProfile profile = customSource.getProfile();
		
		this.name = profile.getName();
		this.id = profile.getId();
		
	}

	/**
	 * constructor initializing object with ProfileAvailability
	 * @param profAv ProfileAvailability
	 */
	public SourceBean(ProfileAvailability profAv) {
		ElementProfile elt = profAv.getProfile();
		this.name = elt.getName();
		this.id = elt.getId();
		this.type = profAv.getMode();
	}
	
	/*
	 *************************** ACCESSORS ********************************* */	
	


	/**
	 * @return name of source
	 * @throws DomainServiceException 
	 */
	public String getName() throws DomainServiceException {
		return name;
	}
	
	/**
	 * @param name
	 * @throws DomainServiceException 
	 */
	public void setName(String name) throws DomainServiceException {
		this.name = name;
	}
	/**
	 * @return id of source
	 */
	public String getId()  throws DomainServiceException{
		return id;
	}
	/**
	 * @param id
	 */
	public void setId(String id)  throws DomainServiceException{
		this.id = id;
	}
	/**
	 * @return type of source
	 */
	public AvailabilityMode getType()  throws DomainServiceException{
		return type;
	}
	/**
	 * @param type
	 * @throws DomainServiceException 
	 */
	public void setType(AvailabilityMode type) throws DomainServiceException {
		this.type = type;
	}
	
	/*
	 *************************** METHODS *********************************** */	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString(){
		String string = "";
		string += "     Id = " + id.toString() + "\n";
		string += "     Name = " + name.toString() + "\n";
		string += "     Type = "; 
		if (type != null){
			string += type + "\n";
		}
		
		return string;
	}
	
	
}
