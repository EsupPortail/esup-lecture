/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.portlet;


import java.util.Enumeration;

import javax.portlet.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.portlet.MyFacesGenericPortlet;
import org.esupportail.lecture.dao.DaoService;
import org.esupportail.lecture.domain.DomainTools;
import org.esupportail.lecture.domain.model.Channel;
import org.esupportail.lecture.domain.service.DomainService;
import org.springframework.web.context.WebApplicationContext;

/**
 * Portlet of the "Lecture channel"
 * @author gbouteil
 *
 */
public class MyLecturePortlet extends MyFacesGenericPortlet {
	

/* ************************** PROPERTIES ******************************** */	
	

	private DomainService domainService;
	private WebApplicationContext appCtx;
	private PortletContext portletCtx;

	/**
	 * Log instance 
	 */
	protected static final Log log = LogFactory.getLog(MyLecturePortlet.class); 
	

/* ************************** METHODS ******************************** */	
	
	/**
	 * Make channel loading, recover the application context
	 * @see org.apache.myfaces.portlet.MyFacesGenericPortlet#init()
	 */
	@Override
	public void init() throws PortletException, UnavailableException {
	
		super.init();
		if (log.isDebugEnabled()){
			log.debug("init()");
		}
		portletCtx = (PortletContext)super.getPortletContext();
		if (log.isDebugEnabled()){
			Enumeration<String> attributesNames = portletCtx.getAttributeNames();
			while (attributesNames.hasMoreElements()) {
				String attribute = (String) attributesNames.nextElement();
				log.debug("PortletContext attribute :"+attribute);
			}
			
		}
		appCtx = (WebApplicationContext)portletCtx.getAttribute("org.springframework.web.context.WebApplicationContext.ROOT");
		
		/* load configurations files */
		domainService = (DomainService)appCtx.getBean("domainService");
		domainService.loadChannel();
		
		/* Add channel and daoService to static property of DomainTools */
		Channel channel = (Channel)appCtx.getBean("channel");
		DomainTools.setChannel(channel);
		DaoService daoService = (DaoService)appCtx.getBean("daoService");
		DomainTools.setDaoService(daoService);
		
	}


}
