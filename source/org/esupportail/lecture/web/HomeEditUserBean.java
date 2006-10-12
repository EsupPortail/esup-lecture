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
import org.esupportail.lecture.beans.EditUserBean;
import org.esupportail.lecture.beans.UserBean;
import org.esupportail.lecture.utils.LectureTools;
import org.esupportail.lecture.domain.service.FacadeService;
import org.esupportail.lecture.domain.service.PortletService;
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
public class HomeEditUserBean {

	/*
	 ************************ PROPERTIES ******************************** */	
	/**
	 * Log instance 
	 */
	protected static final Log log = LogFactory.getLog(HomeEditUserBean.class); 

	/**
	 * Access to global services 
	 */
	private FacadeService facadeService;
	
	/**
	 * Access to portlet services
	 */
	private PortletService portletService;
	
	/**
	 * Access to multiple instance of channel in a one session (contexts)
	 */
	private VirtualSession virtualSession;
	
	
	/* Information to display */
	
	/**
	 * Informations to display about user
	 */
	private UserBean user;


	
	/*
	 ************************** Initialization *******************************/
	/*
	 *************************** METHODS ************************************/

	/**
	 * @return id of the current user of the session
	 */
	private String getCurrentUserId() {
		String userId = portletService.getUserAttribute(LectureTools.USER_ID);
		return userId;
	}

	/*
	 ************************** ACCESSORS for JSP DISPLAY ***********************************/

	/**
	 * To display information about the connected user
	 * @return Returns the user.
	 * @see HomeContextUserBean#user
	 */
	public UserBean getUser() {
		if (user == null){
			user = facadeService.getDomainService().getUserBean(getCurrentUserId());
		}
		log.debug("getUser() : UserBean id : "+user.getId());
		return user;
	}

/////////////////////////////////////////////////
	/**
	 * To display information about the mode custom the connected user
	 * @return Returns the custom mode.
	 * @throws ErrorException
	 */
	public EditUserBean getEdit() throws ErrorException {


		EditUserBean edit = (EditUserBean) virtualSession.get("CustomUserBean");
		String contextId = virtualSession.getCurrentContextId();
		
		if (edit == null){
			log.debug ("getCustom() : CustomUserBean is null");
			edit = facadeService.getDomainService().getEditUserBean(getCurrentUserId(),contextId );
			log.debug("getCustom() : CurrentContextId : "+ contextId);
			virtualSession.put("CustomUserBean",edit);
		}else{
			log.debug ("getCustom() :  Context already loaded : "+ contextId);
		}
		
		return edit;
	}

	

	
	/*
	 ************************** OTHER ACCESSORS ***********************************/

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
	 * @return Returns the portletService.
	 */
	public PortletService getPortletService() {
		return portletService;
	}


	/**
	 * @param portletService The portletService to set.
	 */
	public void setPortletService(PortletService portletService) {
		this.portletService = portletService;
	}


	/**
	 * @return Returns the virtualSession.
	 */
	public VirtualSession getVirtualSession() {
		return virtualSession;
	}


	/**
	 * @param virtualSession The virtualSession to set.
	 */
	public void setVirtualSession(VirtualSession virtualSession) {
		this.virtualSession = virtualSession;
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

