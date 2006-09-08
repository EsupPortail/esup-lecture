package org.esupportail.lecture.domain.service.impl;

import org.esupportail.commons.utils.PortletRequestUtils;
import org.esupportail.lecture.domain.service.PortletService;

/**
 * @see org.esupportail.lecture.domain.service.PortletService
 * Implementation with org.essuportail.commons.utils
 * @author gbouteil
 *
 */
public class PortletServiceImpl implements PortletService{

	/*
	 ************************** PROPERTIES *********************************/	

	/**
	 * access to portlet request  
	 */
	PortletRequestUtils requestUtils;
	
	/*
	 ************************** Initialization ************************************/
	
	/*
	 *************************** METHODS ************************************/
	
	/**
	 * @see org.esupportail.lecture.domain.service.PortletService#getUserAttribute(java.lang.String)
	 */
	public String getUserAttribute(String attributeName) {
		return PortletRequestUtils.getUserAttribute(attributeName);
	}
	
	
	/* ************************** ACCESSORS ********************************* */

	
	
	/**
	 * @return Returns the requestUtils.
	 */
	public PortletRequestUtils getRequestUtils() {
		return requestUtils;
	}


	/**
	 * @param requestUtils The requestUtils to set.
	 */
	public void setRequestUtils(PortletRequestUtils requestUtils) {
		this.requestUtils = requestUtils;
	}
}
