package org.esupportail.lecture.domain;

import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.portlet.PortletUtil;
import org.esupportail.lecture.exceptions.ErrorException;

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
		String ret = null;
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (PortletUtil.isPortletRequest(facesContext)) {
		    ExternalContext externalContext = facesContext.getExternalContext();
	        PortletRequest request = (PortletRequest) externalContext.getRequest();
	        PortletPreferences portletPreferences = request.getPreferences();
	        ret = portletPreferences.getValue(name, "default");			
		}
		else {
			//default return value in case of serlvet use case (not normal mode)
			ret = name;
		}
        if (log.isDebugEnabled()) {
			log.debug("getPreferences("+name+") return "+ret);
		}
        return ret;
	}

	/**
	 * @see org.esupportail.lecture.domain.ExternalService#getUserAttribute(java.lang.String)
	 */
	public String getUserAttribute(String attribute) {
		String ret =  null;
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (PortletUtil.isPortletRequest(facesContext)) {
			ExternalContext externalContext = facesContext.getExternalContext();
			PortletRequest request = (PortletRequest) externalContext.getRequest();
			Map userInfo = (Map)request.getAttribute(PortletRequest.USER_INFO);
			ret = (String)userInfo.get(attribute);
			if (ret == null) {
				throw new ErrorException("User Attribute "+attribute+" not found ! See your portlet.xml file for user-attribute definition.");
			}
		}
		else {
			//default return value in case of serlvet use case (not normal mode)
			ret = attribute;
		}
		if (log.isDebugEnabled()) {
			log.debug("getUserAttribute("+attribute+") return "+ret);
		}
		return ret;
	}

	/**
	 * @see org.esupportail.lecture.domain.ExternalService#getUserProxyTicketCAS()
	 */
	public String getUserProxyTicketCAS() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.esupportail.lecture.domain.ExternalService#isUserInRole(java.lang.String)
	 */
	public boolean isUserInRole(String group) {
		boolean ret = Boolean.FALSE;
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (PortletUtil.isPortletRequest(facesContext)) {
			ExternalContext externalContext = facesContext.getExternalContext();
			PortletRequest request = (PortletRequest) externalContext.getRequest();
			if (request.isUserInRole(group)) {
				ret = Boolean.TRUE;
			} 
		}
        if (log.isDebugEnabled()) {
			log.debug("isUserInRole("+group+") return "+ret);
		}
		return ret;
	}

}
