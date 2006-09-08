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
import org.esupportail.lecture.domain.service.FacadeService;

public class HomeContextUserBean {

	/* ************************** PROPERTIES ******************************** */	
	/**
	 * Log instance 
	 */
	protected static final Log log = LogFactory.getLog(Channel.class); 

	
	private FacadeWeb facadeWeb;
	private UserWeb user;
//	private ContextUserWeb contextUser;
	

	
//	public ContextUserWeb getContextUser(){
//		if (contextUser==null){
//			contextUser = ;
//		}
//		return contextUser;
//	}
	
	/**
	 * @return Returns the facadeWeb.
	 */
	public FacadeWeb getFacadeWeb() {
		return facadeWeb;
	}

	/**
	 * @param facadeWeb The facadeWeb to set.
	 */
	public void setFacadeWeb(FacadeWeb facadeWeb) {
		this.facadeWeb = facadeWeb;
	}

	/**
	 * @return Returns the user.
	 */
	public UserWeb getUser() {
		if (user == null){
			user = facadeWeb.getFacadeService().getUserWeb();
		}
		return user;
	}

	/**
	 * @param user The user to set.
	 */
	public void setUser(UserWeb user) {
		this.user = user;
	}



	
	

}
