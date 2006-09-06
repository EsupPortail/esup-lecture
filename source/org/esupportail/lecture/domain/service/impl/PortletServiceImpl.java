package org.esupportail.lecture.domain.service.impl;

import org.esupportail.commons.utils.PortletRequestUtils;
import org.esupportail.lecture.domain.service.PortletService;

public class PortletServiceImpl implements PortletService{
	PortletRequestUtils requestUtils;
	
	
	
	public String getUserAttribute(String attributeName) {
		String id = PortletRequestUtils.getUserAttribute(attributeName);
		// TODO Auto-generated method stub
		return id;
	}
	
	
	
	
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
