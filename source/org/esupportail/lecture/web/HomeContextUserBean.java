/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.beans.UserBean;
import org.esupportail.lecture.domain.model.Channel;
import org.esupportail.lecture.domain.service.FacadeService;
//import org.esupportail.lecture.domain.model.CustomContext;
//import org.esupportail.lecture.domain.model.UserProfile;
//import org.esupportail.lecture.domain.service.DomainService;

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
	private FacadeService facadeService;
	
	
	
	/**
	 * Informations to display about user
	 */
	private UserBean user;

	
	/*
	 ************************** Initialization *******************************/
	/*
	 *************************** METHODS ************************************/


	/*
	 ************************** ACCESSORS ***********************************/

	/**
	 * @return Returns the facadeService.
	 * @see HomeContextUserBean#facadeService
	 */
	public FacadeService getFacadeService() {
		return facadeService;
	}

	/**
	 * @param facadeService The facadeService to set.
	 * @see HomeContextUserBean#facadeService
	 */
	public void setFacadeService(FacadeService facadeService) {
		this.facadeService = facadeService;
	}

	/**
	 * @return Returns the user.
	 * @see HomeContextUserBean#user
	 */
	public UserBean getUser() {
		if (user == null){
			user = facadeService.getDomainService().getUserBean();
		}
		return user;
	}

	/**
	 * @param user The user to set.
	 * @see HomeContextUserBean#user
	 */
	public void setUser(UserBean user) {
		this.user = user;
	}




	
	

}
