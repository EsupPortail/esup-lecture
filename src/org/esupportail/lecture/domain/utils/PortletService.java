/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.utils;

import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.exceptions.domain.InternalExternalException;
import org.esupportail.lecture.exceptions.domain.NoExternalValueException;


/**
 * @author gbouteil
 * Access to external in portlet mode
 */
public class PortletService implements ModeService {


	/*
	 ************************** PROPERTIES ******************************** */	
	/**
	 * Log instance.
	 */
	protected static final Log LOG = LogFactory.getLog(PortletService.class);

	/* 
	 ************************** INIT ****************************************/
	/**
	 * Default constructor.
	 */
	public PortletService() {
		super();
	}

	/* 
	 ************************** METHODS *************************************/

	/**
	 * @throws InternalExternalException,NoExternalValueException  
	 * @see org.esupportail.lecture.domain.utils.ModeService#getPreference(java.lang.String)
	 */
	public String getPreference(final String name)throws InternalExternalException  {
		if (LOG.isDebugEnabled()) {
			LOG.debug("getPreference(" + name + ")");
		}
		String value;
		try {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ExternalContext externalContext = facesContext.getExternalContext();
			PortletRequest request = (PortletRequest) externalContext.getRequest();
			PortletPreferences portletPreferences = request.getPreferences();
			value = portletPreferences.getValue(name, "default");
		} catch (Exception e) {
			throw new InternalExternalException(e);
		}
		if (value == null) {
			throw new NoExternalValueException("No value for portlet preference '"
				+ name + "' returned by external service");
		}
		return value;
		
	}

	/**
	 * @throws InternalExternalException,NoExternalValueException 
	 * @see org.esupportail.lecture.domain.utils.ModeService#getUserAttribute(java.lang.String)
	 */
	public String getUserAttribute(final String attribute) throws InternalExternalException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("getUserAttribute(" + attribute + ")");
		}
		String value; 
		try {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ExternalContext externalContext = facesContext.getExternalContext();
			PortletRequest request = (PortletRequest) externalContext.getRequest();
			Map<String, String> userInfo = (Map)request.getAttribute(PortletRequest.USER_INFO);
			value = userInfo.get(attribute);
		} catch (Exception e) {
			throw new InternalExternalException(e);
		}
		if (value == null) {
			throw new NoExternalValueException("User Attribute "
				+ attribute + " not found ! See your portlet.xml file for user-attribute definition.");
		}
		return value;
	}

	/**
	 * @throws InternalExternalException 
	 * @see org.esupportail.lecture.domain.utils.ModeService#isUserInGroup(java.lang.String)
	 */
	public boolean isUserInGroup(final String group) throws InternalExternalException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("isUserInGroup(" + group + ")");
		}
		boolean value = Boolean.FALSE;
		try {			
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ExternalContext externalContext = facesContext.getExternalContext();
			PortletRequest request = (PortletRequest) externalContext.getRequest();
			if (request.isUserInRole(group)) {
				value = Boolean.TRUE;
			} 
		} catch (Exception e) {
			throw new InternalExternalException(e);
		}
		return value;
	}

	/*
	 ************************** Accessors ************************************/

	
}
