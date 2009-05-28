/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Flag : a abstract class for all flag category classes.
 * A flag class represent an action on one element, with an id and a date
 * @author vrepain
 * 
 */
/**
 * @author vrepain
 *
 */
public abstract class Flag {
	
	/* 
	 *************************** PROPERTIES *********************************/	

	/**
	 * Log instance. 
	 */
	protected static final Log LOG = LogFactory.getLog(Flag.class); 

	/**
	 *  Flag name.
	 */
	private Date date = null;
	
	
	/*
	 ************************** INITIALIZATION ******************************** */	
	
	/**
	 * Constructor. 
	 */
	public Flag() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Flag()");
		}
	}
	
	/*
	 ************************** METHODS *********************************/	
	
	
	
	
	/*
	 ************************** ACCESSORS *********************************/	
	
	/**
	 * Return the name of the category profile. 
	 * @return name
	 */
	public Date getDate() {
		return date;
	}
	
	/**
	 * Sets the name to the category profile.
	 * @param name
	 */
	public void setDate(final Date date) {
		this.date = date;
	}

}
