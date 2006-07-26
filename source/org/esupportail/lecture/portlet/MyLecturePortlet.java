/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.portlet;

import javax.portlet.PortletException;
import javax.portlet.UnavailableException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.portlet.MyFacesGenericPortlet;
import org.esupportail.lecture.domain.model.Channel;
import org.esupportail.lecture.domain.model.ChannelConfig;
import org.esupportail.lecture.domain.service.FacadeService;
import org.esupportail.lecture.utils.exception.MyException;
import org.springframework.context.ApplicationContext;


/**
 * Portlet of the "Lecture channel"
 * @author gbouteil
 *
 */
public class MyLecturePortlet extends MyFacesGenericPortlet {
	

/* ************************** PROPERTIES ******************************** */	
	

	private FacadeService facadeService;
	private ApplicationContext appCtx;

	/**
	 * Log instance 
	 */
	protected static final Log log = LogFactory.getLog(Channel.class); 
	

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
		try {
			appCtx = (ApplicationContext)super.getPortletContext().getAttribute("org.springframework.web.context.WebApplicationContext.ROOT");
			facadeService = (FacadeService)appCtx.getBean("facadeService");
			facadeService.loadChannel();
		} catch (MyException e) {
			log.fatal("init() :: "+e.getMessage());
		}
	}
	
	/**
	 * @return Returns the facadeService.
	 */
	public FacadeService getFacadeService() {
		return facadeService;
	}
	/**
	 * @param facadeService The facadeService to set.
	 */
	public void setFacadeService(FacadeService facadeService) {
		this.facadeService = facadeService;
	}

}
