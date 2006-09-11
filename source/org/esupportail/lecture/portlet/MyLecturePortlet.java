/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.portlet;


import javax.portlet.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.portlet.MyFacesGenericPortlet;
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
	

	private DomainService facadeService;
	private WebApplicationContext appCtx;
	private PortletContext portletCtx;

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
			portletCtx = (PortletContext)super.getPortletContext();
			appCtx = (WebApplicationContext)portletCtx.getAttribute("org.springframework.web.context.WebApplicationContext.ROOT");
			facadeService = (DomainService)appCtx.getBean("facadeService");
			
//			ServletContext servlCtx = (ServletContext) appCtx.getServletContext();
//			Enumeration enumerator= servlCtx.getAttributeNames();
//			while(enumerator.hasMoreElements()) {
//				String s = enumerator.nextElement().toString();
//				log.debug("Attribut : "+s );
//			}
//	

//			log.debug("objWeb : "+ objWeb.toString());
//			log.debug("objCtxList : "+objCtxList.toString());
//			log.debug("objRunConf : "+objRunConf.toString());
//			log.debug("objFaces : "+objFaces.toString());
//			log.debug("objRess : "+objRess.toString());
//			
			facadeService.loadChannel();
	
		} catch (Exception e) {
			log.fatal("init() :: "+e.getMessage());
//		} catch (IOException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
	}
	

	
	/**
	 * @return Returns the facadeService.
	 */
	public DomainService getFacadeService() {
		return facadeService;
	}
	/**
	 * @param facadeService The facadeService to set.
	 */
	public void setFacadeService(DomainService facadeService) {
		this.facadeService = facadeService;
	}

}
