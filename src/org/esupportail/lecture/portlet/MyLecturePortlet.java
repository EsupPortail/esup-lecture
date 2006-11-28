/**
 * ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
 * For any information please refer to http://esup-helpdesk.sourceforge.net
 * You may obtain a copy of the licence at http://www.esup-portail.org/license/
 */
package org.esupportail.lecture.portlet;


import java.io.IOException;

import javax.portlet.PortletException;
import javax.portlet.PortletMode;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.portlet.MyFacesGenericPortlet;
import org.esupportail.commons.web.portlet.ExceptionHandlingFacesPortlet;


/**
 * Portlet of the "Lecture channel"
 * used to manage edit mode --> See http://wiki.apache.org/myfaces/UsingPortletModes
 * @author bourges
 */
//public class MyLecturePortlet extends ExceptionHandlingFacesPortlet {
public class MyLecturePortlet extends MyFacesGenericPortlet {
	
	
	/* ************************** PROPERTIES ******************************** */	
	
	
	/**
	 * Log instance 
	 */
	protected static final Log log = LogFactory.getLog(MyLecturePortlet.class); 
	
	
	/* ************************** METHODS ******************************** */	
	
	/**
	 * @see javax.portlet.GenericPortlet#render(javax.portlet.RenderRequest, javax.portlet.RenderResponse)
	 */
	public void render(RenderRequest request, RenderResponse response) 
	throws PortletException, IOException {
		PortletSession session = request.getPortletSession();
		PortletMode mode = (PortletMode)session.getAttribute("CurrentPortletMode");
		
		if (mode == null) {
			mode = request.getPortletMode();       
		}
		
		if (!mode.equals(request.getPortletMode())) {
			request.setAttribute("isPortletModeChanged", Boolean.TRUE);
			if (log.isDebugEnabled()) {
				log.debug("isPortletModeChanged in request set to TRUE");
			}
		} else {
			request.setAttribute("isPortletModeChanged", Boolean.FALSE);
			if (log.isDebugEnabled()) {
				log.debug("isPortletModeChanged in request set to FALSE");
			}
		}
		
		session.setAttribute("CurrentPortletMode", request.getPortletMode());
		if (log.isDebugEnabled()) {
			log.debug("CurrentPortletMode in session set to "+ request.getPortletMode());
		}
		super.render(request, response);
	}	
	
	/**
	 * @see org.apache.myfaces.portlet.MyFacesGenericPortlet#doEdit(javax.portlet.RenderRequest, javax.portlet.RenderResponse)
	 */
	protected void doEdit(RenderRequest request, RenderResponse response)
	throws PortletException, IOException {
		
		Boolean isPortletModeChanged = (Boolean)request.getAttribute("isPortletModeChanged");
		if (isPortletModeChanged.booleanValue()) {
			if (log.isDebugEnabled()) {
				log.debug("Changing to edit page");
			}
			setPortletRequestFlag(request);
			nonFacesRequest(request, response, "/stylesheets/edit.jsp");
			return;
		}
		facesRender(request, response);
	}	
	
}

