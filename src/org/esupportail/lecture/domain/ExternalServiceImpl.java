package org.esupportail.lecture.domain;

import javax.faces.context.FacesContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.portlet.PortletUtil;
import org.esupportail.lecture.domain.utils.ModeService;
import org.esupportail.lecture.domain.utils.PortletService;
import org.esupportail.lecture.domain.utils.ServletService;

/**
 * @author bourges
 * an implementation of ExternalService for tests
 */
public class ExternalServiceImpl implements ExternalService {

	/**
	 * the logger for this class
	 */
	protected static final Log log = LogFactory.getLog(ExternalServiceImpl.class);

	/** 
	 * Composant for mode servlet or mode portlet
	 */
	private ModeService modeService;
	
	
	/**
	 * Constructor 
	 */
	public ExternalServiceImpl(){
		// Dynamic instantiation for portlet/servlet context
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (PortletUtil.isPortletRequest(facesContext)) {
			modeService = new PortletService();
		} else {// TODO (GB/RB) make better
			modeService = new ServletService();
		}
	}
	
	/**
	 * @see org.esupportail.lecture.domain.ExternalService#getConnectedUserId()
	 */
	public String getConnectedUserId() {
		return getUserAttribute(DomainTools.USER_ID);
	}
	/**
	 * @see org.esupportail.lecture.domain.ExternalService#getCurrentContextId()
	 */
	public String getCurrentContextId() {
		return getPreferences(DomainTools.CONTEXT);
	}

	/**
	 * @see org.esupportail.lecture.domain.ExternalService#getPreferences(java.lang.String)
	 */
	public String getPreferences(String name) {
	   if (log.isDebugEnabled()) {
			log.debug("getPreferences("+name+")");
		}
	    String ret = modeService.getPreference(name);
	   
        if (log.isTraceEnabled()) {
			log.trace("getPreferences("+name+") return "+ret);
		}
 
        return ret;
	}

	/**
	 * @see org.esupportail.lecture.domain.ExternalService#getUserAttribute(java.lang.String)
	 */
	public String getUserAttribute(String attribute) {
	    if (log.isDebugEnabled()) {
			log.debug("getUserAttribute("+attribute+")");
		}
		String ret = modeService.getUserAttribute(attribute);
		
		if (log.isTraceEnabled()) {
			log.trace("getUserAttribute("+attribute+") return "+ret);
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
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.esupportail.lecture.domain.ExternalService#isUserInRole(java.lang.String)
	 */
	public boolean isUserInGroup(String group) {
	    if (log.isDebugEnabled()) {
			log.debug("isUserInRole("+group+")");
		}
	    
	    boolean ret = modeService.isUserInGroup(group);
	   
        if (log.isDebugEnabled()) {
			log.debug("isUserInRole("+group+") return "+ret);
		}
		return ret;
	}

}
