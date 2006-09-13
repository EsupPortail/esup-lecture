/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Visibility sets for managed categories and sources 
 * @author gbouteil
 *
 */
/**
 * @author gbouteil
 *
 */
public class VisibilitySets {

	/*
	 *************************** PROPERTIES ******************************** */	
	/**
	 * Log instance 
	 */
	protected static final Log log = LogFactory.getLog(VisibilitySets.class);
	/**
	 * Group of allowed users to subscribe to element
	 */
	private DefAndContentSets allowed = new DefAndContentSets();
	/**
	 * Group of autoSubribed users to element
	 */
	private DefAndContentSets autoSubscribed = new DefAndContentSets();
	/**
	 * Group of obliged users (no unsubscription possible) 
	 */
	private DefAndContentSets obliged = new DefAndContentSets();


	/*
	 ************************** METHODS ******************************** */	
	
	/**
	 * Check existence of group names, attributes names used in group definition
	 */
	protected void checkNamesExistence(){
	   	if (log.isDebugEnabled()){
    		log.debug("checkNamesExistence()");
    	}
		allowed.checkNamesExistence();
		obliged.checkNamesExistence();
		autoSubscribed.checkNamesExistence();
	}
	
	
	
	/**
	 * Returns a string containing VisibilitySets content : allowed group, autoSubscribed group
	 * and obliged group.
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		
		String string ="";
		string += "	   allowed : "+"\n"+ allowed.toString();
		string += "           autoSubscribed : "+"\n"+ autoSubscribed.toString()+"\n";
		string += "           obliged : "+"\n"+ obliged.toString()+"\n";
		
		return string;
	}


	/*
	 *************************** ACCESSORS ******************************** */	

	/**
	 * Returns allowed group visibility
	 * @return allowed
	 * @see VisibilitySets#allowed
	 */
	protected DefAndContentSets getAllowed() {
		return allowed;
	}
	/**
	 * Sets allowed group visibility
	 * @param allowed
	 * @see VisibilitySets#allowed
	 */
	protected void setAllowed(DefAndContentSets allowed) {
		this.allowed = allowed;
	}

	/**
	 * Returns autoSubscribed group visibility
	 * @return autoSubscribed
	 * @see VisibilitySets#autoSubscribed
	 */
	protected DefAndContentSets getAutoSubscribed() {
		return autoSubscribed;
	}
	/**
	 * Sets autoSubscribed group visibility
	 * @param autoSubscribed
	 * @see VisibilitySets#autoSubscribed
	 */
	protected void setAutoSubscribed(DefAndContentSets autoSubscribed) {
		this.autoSubscribed = autoSubscribed;
	}

	/**
	 * Returns obliged group visibility
	 * @return obliged
	 * @see VisibilitySets#obliged
	 */
	protected DefAndContentSets getObliged() {
		return obliged;
	}
	
	/**
	 * Sets obliged group visibility
	 * @param obliged
	 * @see VisibilitySets#obliged
	 */
	protected void setObliged(DefAndContentSets obliged) {
		this.obliged = obliged;
	}

	
}
