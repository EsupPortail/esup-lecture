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
	
}
