/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.beans;

import org.esupportail.lecture.domain.model.UserProfile;

/**
 * 
 * used to store user informations.
 * @author bourges
 */
public class UserBean {

	/* 
	 *************************** PROPERTIES ******************************** */	
	
	/**
	 * id of source.
	 */
	private String uid;
	
	/*
	 *************************** INIT ************************************** */	
			
	/**
	 * Constructor initializing object.
	 * @param uid user ID
	 */
	public UserBean(final String uid) {
		this.uid = uid;
	}
	/**
	 * Constructor initializing object.
	 * @param userProfile
	 */
	public UserBean(final UserProfile userProfile) {
		uid = userProfile.getUserId();
	}
	
	/*
	 *************************** ACCESSORS ********************************* */	
	
	/**
	 * @return id of source
	 */
	public String getUid() {
		return uid;
	}
	/**
	 * @param id
	 */
	public void setUid(final String id) {
		this.uid = id;
	}
	
	/*
	 *************************** METHODS *********************************** */	

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String string = "";
		string += " uid = " + uid.toString() + "\n";
		return string;
	}
	
}
