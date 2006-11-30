package org.esupportail.lecture.domain;

import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
		FacesContext facesContext = FacesContext.getCurrentInstance();
	    ExternalContext externalContext = facesContext.getExternalContext();
        PortletRequest request = (PortletRequest) externalContext.getRequest();
        PortletPreferences portletPreferences = request.getPreferences();
        String value = portletPreferences.getValue(name, "default");
        if (log.isDebugEnabled()) {
			log.debug("getPreferences("+name+") return "+value);
		}
        return value;
	}

	/**
	 * @see org.esupportail.lecture.domain.ExternalService#getUserAttribute(java.lang.String)
	 */
	public String getUserAttribute(String attribute) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
	    ExternalContext externalContext = facesContext.getExternalContext();
        PortletRequest request = (PortletRequest) externalContext.getRequest();
		Map userInfo = (Map)request.getAttribute(PortletRequest.USER_INFO);
		String attributeValue = (String)userInfo.get(attribute);
		if (attributeValue == null) {
			throw new ErrorException("User Attribute "+attribute+" not found ! See your portlet.xml file for user-attribute definition.");
		}
        if (log.isDebugEnabled()) {
			log.debug("getUserAttribute("+attribute+") return "+attributeValue);
		}
		return attributeValue;
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
	    ExternalContext externalContext = facesContext.getExternalContext();
		PortletRequest request = (PortletRequest) externalContext.getRequest();
		if (request.isUserInRole(group)) {
			ret = Boolean.TRUE;
		} 
        if (log.isDebugEnabled()) {
			log.debug("isUserInRole("+group+") return "+ret);
		}
		return ret;
	}

}
