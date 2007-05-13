/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain;

import javax.faces.context.FacesContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.portlet.PortletUtil;
import org.esupportail.lecture.domain.utils.ModeService;
import org.esupportail.lecture.domain.utils.PortletService;
import org.esupportail.lecture.domain.utils.ServletService;
import org.esupportail.lecture.exceptions.domain.InternalExternalException;
import org.esupportail.lecture.exceptions.domain.NoExternalValueException;

/**
 * @author bourges
 * Implementation of interface ExternalService where
 * externalService can be :
 * - portlet service
 * - servlet service
 * The modeService (portlet or servlet) is dynamically defined at every method calls
 */
public class ExternalServiceImpl implements ExternalService {

	/* 
	 *************************** PROPERTIES ******************************** */	

	/**
	 * the logger for this class.
	 */
	protected static final Log log = LogFactory.getLog(ExternalServiceImpl.class);

	/**
	 * portlet version of ExternalService.
	 */
	//TODO (RB) inject by spring (gb : => so no more static)
	static PortletService portletService = new PortletService();

	/**
	 * servlet version of ExternalService.
	 */
	//TODO (RB) inject by spring (gb : => so no more static)
	static ServletService servletService = new ServletService();
	
	/**
	 * default version of ExternalService.
	 */
	//TODO (RB) inject by spring (gb : => so no more static)
	static ServletService defaultService = servletService;
	
	/*
	 *************************** INIT ************************************** */	

	/*
	 *************************** METHODS *********************************** */	

	/**
	 * @throws InternalExternalException 
	 * @throws NoExternalValueException 
	 * @see org.esupportail.lecture.domain.ExternalService#getConnectedUserId()
	 */
	public String getConnectedUserId() throws NoExternalValueException, InternalExternalException {
		return getUserAttribute(DomainTools.USER_ID);
	}
	/**
	 * Return ID of the current context (from channel instantiation : portlet preference with name "context")).
	 * @throws InternalExternalException 
	 * @throws NoExternalValueException 
	 * @see org.esupportail.lecture.domain.ExternalService#getCurrentContextId()
	 */
	public String getCurrentContextId() throws NoExternalValueException, InternalExternalException {
		return getPreferences(DomainTools.CONTEXT);
	}

	/**
	 * @throws InternalExternalException 
	 * @throws NoExternalValueException 
	 * @see org.esupportail.lecture.domain.ExternalService#getPreferences(java.lang.String)
	 */
	public String getPreferences(final String name) throws NoExternalValueException, InternalExternalException {
	     String ret = getModeService().getPreference(name);
	 
	     if (log.isTraceEnabled()) {
			log.trace("getPreferences(" + name + ") return " + ret);
		}
 
        return ret;
	}

	/**
	 * @throws InternalExternalException 
	 * @throws NoExternalValueException 
	 * @see org.esupportail.lecture.domain.ExternalService#getUserAttribute(java.lang.String)
	 */
	public String getUserAttribute(final String attribute) 
		throws NoExternalValueException, InternalExternalException {
		String ret = getModeService().getUserAttribute(attribute);
		
		if (log.isTraceEnabled()) {
			log.trace("getUserAttribute(" + attribute + ") return " + ret);
		}
		return ret;
	}

	/**
	 * @see org.esupportail.lecture.domain.ExternalService#getUserProxyTicketCAS()
	 */
	public String getUserProxyTicketCAS() {
	    if (log.isDebugEnabled()) {
			log.debug("getUserProxyTicketCAS() - not yet implemented");
		}
		return null;
	}

	/**
	 * @throws InternalExternalException 
	 * @see org.esupportail.lecture.domain.ExternalService#isUserInGroup(java.lang.String)
	 */
	public boolean isUserInGroup(final String group) throws InternalExternalException {
	    boolean ret = getModeService().isUserInGroup(group);
	   
        if (log.isDebugEnabled()) {
			log.debug("isUserInRole(" + group + ") return " + ret);
		}
		return ret;
	}

	/**
	 * Get the current mode service.
	 * It can be : 
	 * 	- portletService
	 *  - servletService
	 *  - defaultService
	 * Computes it dynamically and not in contructor because Spring can't find facesContext at startup
	 * @return ModeService - the current mode service
	 */
	private ModeService getModeService() {
		ModeService ret = null;
		// Dynamic instantiation for portlet/servlet context
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext != null) {
			if (PortletUtil.isPortletRequest(facesContext)) {
				ret = portletService;
			} else {
				// TODO (RB/GB) make better
				ret = servletService;
			}			
		} else {
			ret = defaultService;
		}
		return ret;
	}

	/*
	 *************************** ACCESSORS ********************************* */	

	
}
