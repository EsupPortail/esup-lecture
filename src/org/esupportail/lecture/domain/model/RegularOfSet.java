/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.ExternalService;
import org.esupportail.lecture.exceptions.domain.InternalExternalException;
import org.esupportail.lecture.exceptions.domain.NoExternalValueException;

/**
 * Regular definition of a group : 
 * Every user that "attribute x" has "value Y"
 * @author gbouteil
 *
 */
public class RegularOfSet {

	/*
	 *************************** PROPERTIES ******************************** */	
	/**
	 * Log instance 
	 */
	protected static final Log log = LogFactory.getLog(RegularOfSet.class);
	
	/**
	 * attribute required value 
	 */
	private String attribute = "";
	
	/**
	 * Value required by the attribute to be in the group that is defined
	 */
	private String value = "";


	/*
	 *************************** METHODS ******************************** */	
	
	/**
	 * Return true if user checks this regular
	 * @param portletService
	 * @return boolean
	 * @throws NoExternalAttributeValueException 
	 */
	public boolean evaluate(ExternalService ex) {
		if (log.isDebugEnabled()){
			log.debug("evaluate(externalService)");
		}
		
		String userAttributeValue;
		try {
			userAttributeValue = ex.getUserAttribute(attribute);
		} catch (NoExternalValueException e) {
			log.warn("User attribute evaluation impossible (NoExternalValueException) : "+e.getMessage());
			return false;
		} catch (InternalExternalException e) {
			log.error("User attribute evaluation impossible (external service unavailable) : "+e.getMessage());
			return false;
		}
		// TODO (GB) voir le cas ou il y est mais que le portail ne connait pas
		
		if (userAttributeValue.equals(value)) {
			return true;
		}
		return false;
	}
	
	
	/**
	 * Check existence of attributes names used in regular definition
	 */
	protected void checkNamesExistence(){
	   	if (log.isDebugEnabled()){
    		log.debug("checkNamesExistence()");
    	}
		// TODO (GB later) vérification de l'existence de l'attribut dans le portail :impossible
	   	// on ne peut verifier que sa declaration dans le portlet.xml ? + log.warn si pb
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		
		String string ="";
		string += "attribute : "+ attribute;
		string += ", value : "+ value ;
		
		return string;
	}

	/*
	 *************************** ACCESSORS ******************************** */	

	/**
	 * Returns attribute name
	 * @return attribute
	 * @see RegularOfSet#attribute
	 */
	protected String getAttribute() {
		return attribute;
	}
	/**
	 * Sets attribute name
	 * @param attribute
	 * @see RegularOfSet#attribute
	 */
	synchronized public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	/**
	 * Returns attribute required value
	 * @return value
	 * @see RegularOfSet#value
	 */
	protected String getValue() {
		return value;
	}
	/**
	 * Sets attribute required value
	 * @param value
	 * @see RegularOfSet#value
	 */
	synchronized public void setValue(String value) {
		this.value = value;
	}


	
}
