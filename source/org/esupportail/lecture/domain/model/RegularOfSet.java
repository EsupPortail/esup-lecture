/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
	 * Check existence of attributes names used in regular definition
	 */
	protected void checkNamesExistence(){
	   	if (log.isDebugEnabled()){
    		log.debug("checkNamesExistence()");
    	}
		// TODO vérification de l'existence de l'attribut dans le portail ? + log.warn si pb
		// TODO vérifiaction de l'existance de la valeur dans le portail ? + log.warn si pb
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
	protected void setAttribute(String attribute) {
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
	protected void setValue(String value) {
		this.value = value;
	}
	
}
