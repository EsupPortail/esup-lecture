package org.esupportail.lecture.beans;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Informations to display about a user
 * @author gbouteil
 *
 */
public class UserBean {
	/*
	 ************************ PROPERTIES ******************************** */	

	/**
	 * Log instance
	 */
	protected static final Log log = LogFactory.getLog(UserBean.class);

	/**
	 * Id of the user
	 */
	private String id;
	
	
	

	
	/*
	 ************************** Initialization *******************************/
	/*
	 *************************** METHODS ************************************/

	
	/*
	 ************************** ACCESSORS ***********************************/

	

	/**
	 * @return Returns the id.
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(String id) {
		this.id = id;
	}

	
}
