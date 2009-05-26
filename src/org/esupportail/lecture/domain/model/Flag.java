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
	 *  Flag id.
	 */
	private String id;
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
	
	
	
	/**
	 * @return id
	 * @see Flag#id
	 * @see ElementProfile#getId()
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * @param id
	 * @see Flag#id
	 */
	public void setId(final String id) {
		this.id = id;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/** 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()  {
		
		String string = "";
		
		/* The Flag Id */
		string += "	id : " + getId() + "\n";
			
		/* The Flag date */
		//string += "	id : " + getId() + "\n";
		
		return string;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Flag other = (Flag) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
