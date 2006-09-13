/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.web;



import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.beans.ContextUserBean;
import org.esupportail.lecture.beans.UserBean;
import org.esupportail.lecture.domain.model.UserAttributes;
import org.esupportail.lecture.domain.service.FacadeService;
import org.esupportail.lecture.utils.exception.ErrorException;
//import org.esupportail.lecture.domain.model.CustomContext;
//import org.esupportail.lecture.domain.model.UserProfile;
//import org.esupportail.lecture.domain.service.DomainService;

/**
 * Bean for displaying a context, for a user profile
 * @author gbouteil
 *
 */
/**
 * @author gbouteil
 *
 */
public class HomeContextUserBean {

	/*
	 ************************ PROPERTIES ******************************** */	
	/**
	 * Log instance 
	 */
	protected static final Log log = LogFactory.getLog(HomeContextUserBean.class); 

	/**
	 * Access to services
	 */
	private FacadeService facadeService;
	
	/**
	 * Informations to display about user
	 */
	private UserBean user;
	

	

	
	
	/**
	 * Get session information that needs to be different from a instance channel to another
	 */
	private SessionManager sessionManager;

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
			user = facadeService.getDomainService().getUserBean(getCurrentUserId());
		}
		log.info("UserBean id : "+user.getId());
		return user;
	}

	/**
	 * @param user The user to set.
	 * @see HomeContextUserBean#user
	 */
	public void setUser(UserBean user) {
		this.user = user;
	}

	/**
	 * @return Returns the context.
	 * @throws ErrorException
	 */
	public ContextUserBean getContext() throws ErrorException {
		ContextUserBean context = sessionManager.getContextUserBean();
		
		if (context == null){
			log.info("Il est null !!!");
			context = facadeService.getDomainService().getContextUserBean(getCurrentUserId(),getCurrentContextId());
			log.info(" CurrentContextId : "+getCurrentContextId());
			sessionManager.addContextUserBean(context);
		}else{

			log.info("Context deja charge : "+context.getId());
		}
		return context;
	}



	/**
	 * @return Returns the current userId.
	 */
	public String getCurrentUserId() {
//		return facadeService.getPortletService().getUserAttribute(UserAttributes.USER_ID);
		return SessionManager.getUserAttribute(UserAttributes.USER_ID);
	}

	
	/**
	 * @return Returns the current contextId.
	 */
	public String getCurrentContextId() {
		return sessionManager.getCurrentContextId();
	}

	/**
	 * @return Returns the sessionManager.
	 */
	public SessionManager getSessionManager() {
		return sessionManager;
	}

	/**
	 * @param sessionManager The sessionManager to set.
	 */
	public void setSessionManager(SessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

	/**
	 * @return Returns the test.
	 */
	public String getTest() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
	    log.error("Test, external context : "+externalContext.toString() );
        PortletRequest request = (PortletRequest) externalContext.getRequest();
        log.error("Test, request : "+request.toString() );
        PortletPreferences portletPreferences = request.getPreferences();
        log.error("Test, portletPreferences : "+portletPreferences.toString() );
        String context = portletPreferences.getValue("context", "Context not define !");
        log.error("Test, context : "+context);
        return context;
	}


	
	

}
