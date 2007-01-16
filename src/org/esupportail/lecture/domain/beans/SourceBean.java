/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.beans;

import org.esupportail.lecture.domain.model.CustomSource;
import org.esupportail.lecture.domain.model.SourceProfile;
import org.esupportail.lecture.exceptions.domain.CategoryNotLoadedException;
import org.esupportail.lecture.exceptions.domain.DomainServiceException;
import org.esupportail.lecture.exceptions.domain.ManagedCategoryProfileNotFoundException;
import org.esupportail.lecture.exceptions.domain.SourceProfileNotFoundException;

/**
 * @author bourges
 * used to store source informations
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
	 */
	private String type;
	/**
	 * SUBSCRIBED type
	 */
	public final static String SUBSCRIBED = "subscribed";
	/**
	 * NOTSUBSCRIBED type
	 */
	public final static String NOTSUBSCRIBED = "notSubscribed";
	/**
	 * OBLIGED type
	 */
	public final static String OBLIGED = "obliged";
	
	// TODO (GB --> RB) pourquoi pas utiliser un attribut de la classe VisibilityMode ?
	// TODO (GB later) revoir comment concevoir cela : il faut aussi tenir compte des personnalSources qui n'ont pas de type

	/*
	 *************************** INIT ************************************** */	
	
	/**
	 * default constructor
	 */
	public SourceBean(){}
	
	/**
	 * constructor initializing object
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
	public String getType()  throws DomainServiceException{
		return type;
	}
	/**
	 * @param type
	 * @throws DomainServiceException 
	 */
	public void setType(String type) throws DomainServiceException {
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
