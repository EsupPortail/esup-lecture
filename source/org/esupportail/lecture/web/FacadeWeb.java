/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.web;

import org.esupportail.lecture.domain.service.DomainService;
import org.esupportail.lecture.domain.service.PortletService;

/**
 * Entry point to access services, used by org.esupportail.lecture.web package
 * @author gbouteil
 *
 */
public class FacadeWeb {

	/* ************************** PROPERTIES ******************************** */	
	
	/**
	 * Access to domain service
	 */
	private DomainService facadeService;
	
	/**
	 * Access to portlet request services 
	 */
	private PortletService portletService;

	/*
	 ************************** Initialization ******************************/
	
	
	/*
	 *************************** METHODS ************************************/

	/* ************************** ACCESSORS ********************************* */

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

	/**
	 * Return a DomainService
	 * @return facadeService
	 */
	public DomainService getFacadeService() {
		return facadeService;
	}

	/**
	 * Sets a DomainService
	 * @param facadeService
	 */
	public void setFacadeService(DomainService facadeService) {
		this.facadeService = facadeService;
	}
	
}
