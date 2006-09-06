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
	//TODO identification du user connecté
	private String userId;
	private String contextName;
	private CustomContext customContext;
	
	
	
	public String getUserId(){
		userId = facadeWeb.getPortletService().getUserAttribute("displayName");
		return userId;
	}
	
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

	
	

}
