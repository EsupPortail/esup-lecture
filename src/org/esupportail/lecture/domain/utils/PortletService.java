package org.esupportail.lecture.domain.utils;

import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;

import org.apache.myfaces.portlet.PortletUtil;
import org.esupportail.lecture.exceptions.ErrorException;


/**
 * @author gbouteil
 * Access to external in portlet mode
 */
public class PortletService implements ModeService {

	/**
	 * @see org.esupportail.lecture.domain.utils.ModeService#getPreference(java.lang.String)
	 */
	public String getPreference(String name) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
	    PortletRequest request = (PortletRequest) externalContext.getRequest();
	    PortletPreferences portletPreferences = request.getPreferences();
	    return portletPreferences.getValue(name, "default");			
		
	}

	/**
	 * @see org.esupportail.lecture.domain.utils.ModeService#getUserAttribute(java.lang.String)
	 */
	public String getUserAttribute(String attribute) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		PortletRequest request = (PortletRequest) externalContext.getRequest();
		Map userInfo = (Map)request.getAttribute(PortletRequest.USER_INFO);
		String ret = (String)userInfo.get(attribute);
		if (ret == null) {
			throw new ErrorException("User Attribute "+attribute+" not found ! See your portlet.xml file for user-attribute definition.");
		}
		return ret;
	}

	/**
	 * @see org.esupportail.lecture.domain.utils.ModeService#isUserInGroup(java.lang.String)
	 */
	public boolean isUserInGroup(String group) {
		boolean ret= Boolean.FALSE;
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		PortletRequest request = (PortletRequest) externalContext.getRequest();
		if (request.isUserInRole(group)) {
			ret = Boolean.TRUE;
		} 
		return ret;
	}

}
