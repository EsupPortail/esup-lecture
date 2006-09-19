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
import org.esupportail.lecture.dao.DaoService;
import org.esupportail.lecture.domain.model.LectureTools;
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
		try {
			portletCtx = (PortletContext)super.getPortletContext();
			appCtx = (WebApplicationContext)portletCtx.getAttribute("org.springframework.web.context.WebApplicationContext.ROOT");
			domainService = (DomainService)appCtx.getBean("domainService");
			
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
			domainService.loadChannel();
			//Add dosService to static property of LectureTools
			DaoService daoService = (DaoService)appCtx.getBean("daoService");
			LectureTools.setDaoService(daoService);
	
		} catch (Exception e) {
			log.fatal("init() :: "+e.getMessage());
//		} catch (IOException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
	}
	

	
	/**
	 * @return Returns the domainService.
	 */
	public DomainService getDomainService() {
		return domainService;
	}
	/**
	 * @param domainService The domainService to set.
	 */
	public void setDomainService(DomainService domainService) {
		this.domainService = domainService;
	}

}
