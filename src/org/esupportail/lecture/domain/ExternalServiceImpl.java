package org.esupportail.lecture.domain;

import javax.faces.context.FacesContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.portlet.PortletUtil;
import org.esupportail.lecture.domain.utils.ModeService;
import org.esupportail.lecture.domain.utils.PortletService;
import org.esupportail.lecture.domain.utils.ServletService;
import org.esupportail.lecture.exceptions.domain.CategoryNotLoadedException;
import org.esupportail.lecture.exceptions.domain.InternalExternalException;
import org.esupportail.lecture.exceptions.domain.NoExternalValueException;

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
	 * portlet version of ExternalService
	 */
	//TODO (RB) inject by spring (gb : => so no more static)
	static PortletService portletService = new PortletService();

	/**
	 * servlet version of ExternalService
	 */
	//TODO (RB) inject by spring (gb : => so no more static)
	static ServletService servletService = new ServletService();
	
	/**
	 * @throws InternalExternalException 
	 * @throws NoExternalValueException 
	 * @see org.esupportail.lecture.domain.ExternalService#getConnectedUserId()
	 */
	public String getConnectedUserId() throws NoExternalValueException, InternalExternalException {
		return getUserAttribute(DomainTools.USER_ID);
	}
	/**
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
	public String getPreferences(String name) throws NoExternalValueException, InternalExternalException {
	   if (log.isDebugEnabled()) {
			log.debug("getPreferences("+name+")");
		}
	  
	    String ret = getModeService().getPreference(name);
	 
	     if (log.isTraceEnabled()) {
			log.trace("getPreferences("+name+") return "+ret);
		}
 
        return ret;
	}

	/**
	 * @throws InternalExternalException 
	 * @throws NoExternalValueException 
	 * @see org.esupportail.lecture.domain.ExternalService#getUserAttribute(java.lang.String)
	 */
	public String getUserAttribute(String attribute) throws NoExternalValueException, InternalExternalException {
	    if (log.isDebugEnabled()) {
			log.debug("getUserAttribute("+attribute+")");
		}
		String ret = getModeService().getUserAttribute(attribute);
		
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
	 * @throws InternalExternalException 
	 * @see org.esupportail.lecture.domain.ExternalService#isUserInGroup(java.lang.String)
	 */
	public boolean isUserInGroup(String group) throws InternalExternalException {
	    if (log.isDebugEnabled()) {
			log.debug("isUserInRole("+group+")");
		}
	    
	    boolean ret = getModeService().isUserInGroup(group);
	   
        if (log.isDebugEnabled()) {
			log.debug("isUserInRole("+group+") return "+ret);
		}
		return ret;
	}

	/**
	 * used to get mode service computed just in time and not in contructor because Spring can't find facesContext at startup
	 * @return ModeService - the current mode service
	 */
	private ModeService getModeService() {
		ModeService ret = null;
		// Dynamic instantiation for portlet/servlet context
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (PortletUtil.isPortletRequest(facesContext)) {
			ret = portletService;
		} else {
			// TODO make better
			ret = servletService;
		}
		return ret;
	}

}
