/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.model.Channel;
import org.esupportail.lecture.domain.model.CustomContext;
import org.esupportail.lecture.domain.model.UserProfile;
import org.esupportail.lecture.domain.service.DomainService;

/**
 * Bean for displaying a context, for a user profile
 * @author gbouteil
 *
 */
public class HomeContextUserBean {

	/*
	 ************************ PROPERTIES ******************************** */	
	/**
	 * Log instance 
	 */
	protected static final Log log = LogFactory.getLog(Channel.class); 

	/**
	 * Access to services
	 */
	private FacadeWeb facadeWeb;
	
	
	
	/**
	 * Informations to display about user
	 */
	private UserWeb user;

	
	/*
	 ************************** Initialization *******************************/
	/*
	 *************************** METHODS ************************************/


	/*
	 ************************** ACCESSORS ***********************************/

	/**
	 * @return Returns the facadeWeb.
	 * @see HomeContextUserBean#facadeWeb
	 */
	public FacadeWeb getFacadeWeb() {
		return facadeWeb;
	}

	/**
	 * @param facadeWeb The facadeWeb to set.
	 * @see HomeContextUserBean#facadeWeb
	 */
	public void setFacadeWeb(FacadeWeb facadeWeb) {
		this.facadeWeb = facadeWeb;
	}

	/**
	 * @return Returns the user.
	 * @see HomeContextUserBean#user
	 */
	public UserWeb getUser() {
		if (user == null){
			user = facadeWeb.getFacadeService().getUserWeb();
		}
		return user;
	}

	/**
	 * @param user The user to set.
	 * @see HomeContextUserBean#user
	 */
	public void setUser(UserWeb user) {
		this.user = user;
	}




	
	

}
