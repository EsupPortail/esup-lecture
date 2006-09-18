/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.service;

import org.esupportail.lecture.domain.service.DomainService;
//import org.esupportail.lecture.domain.service.PortletService;

/**
 * Entry point to global access services (but not portlet services),
 * used by org.esupportail.lecture.web package
 * @author gbouteil
 *
 */
public class FacadeService {

	/* ************************** PROPERTIES ******************************** */	
	
	/**
	 * Access to domain service
	 */
	private DomainService domainService;
	
	/**
	 * Access to portlet service
	 */
	private PortletService portletService;
	
	/*
	 ************************** Initialization ******************************/
	
	
	/*
	 *************************** METHODS ************************************/

	/* ************************** ACCESSORS ********************************* */


	/**
	 * Return a DomainService
	 * @return domainService
	 */
	public DomainService getDomainService() {
		return domainService;
	}

	/**
	 * Sets a DomainService
	 * @param domainService
	 */
	public void setDomainService(DomainService domainService) {
		this.domainService = domainService;
	}

	/**
	 * @return Returns the portletService.
	 */
	public PortletService getPortletService() {
		return portletService;
	}

	/**
	 * @param portletService The portletService to set.
	 */
	public void setPortletService(PortletService portletService) {
		this.portletService = portletService;
	}
	
}
