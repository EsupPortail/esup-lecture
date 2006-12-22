package org.esupportail.lecture.domain.utils;

import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;


import org.esupportail.lecture.exceptions.ErrorException;
import org.esupportail.lecture.exceptions.domain.InternalExternalException;
import org.esupportail.lecture.exceptions.domain.NoExternalValueException;


/**
 * @author gbouteil
 * Access to external in portlet mode
 */
public class PortletService implements ModeService {

	/**
	 * @throws InternalExternalException 
	 * @see org.esupportail.lecture.domain.utils.ModeService#getPreference(java.lang.String)
	 */
	public String getPreference(String name)throws InternalExternalException,NoExternalValueException  {
		String value;
		try {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ExternalContext externalContext = facesContext.getExternalContext();
			PortletRequest request = (PortletRequest) externalContext.getRequest();
			PortletPreferences portletPreferences = request.getPreferences();
			value = portletPreferences.getValue(name, "default");
		} catch (Exception e){
			throw new InternalExternalException(e);
		}
		if (value == null){
			throw new NoExternalValueException("No value for portlet preference '"+ name +"' returned by external service");
		}
		return value;
		
	}

	/**
	 * @throws InternalExternalException,NoExternalValueException 
	 * @see org.esupportail.lecture.domain.utils.ModeService#getUserAttribute(java.lang.String)
	 */
	public String getUserAttribute(String attribute) throws InternalExternalException,NoExternalValueException {
		String value; 
		try {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ExternalContext externalContext = facesContext.getExternalContext();
			PortletRequest request = (PortletRequest) externalContext.getRequest();
			Map userInfo = (Map)request.getAttribute(PortletRequest.USER_INFO);
			value = (String)userInfo.get(attribute);
		} catch (Exception e){
			throw new InternalExternalException(e);
		}
		if (value == null) {
			throw new NoExternalValueException("User Attribute "+attribute+" not found ! See your portlet.xml file for user-attribute definition.");
		}
		return value;
	}

	/**
	 * @throws InternalExternalException 
	 * @see org.esupportail.lecture.domain.utils.ModeService#isUserInGroup(java.lang.String)
	 */
	public boolean isUserInGroup(String group) throws InternalExternalException {
		boolean value = Boolean.FALSE;
		try {			
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ExternalContext externalContext = facesContext.getExternalContext();
			PortletRequest request = (PortletRequest) externalContext.getRequest();
			if (request.isUserInRole(group)) {
				value = Boolean.TRUE;
			} 
		} catch (Exception e){
			throw new InternalExternalException(e);
		}
		return value;
	}

}
