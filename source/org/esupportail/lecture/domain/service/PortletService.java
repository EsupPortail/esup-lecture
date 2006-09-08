package org.esupportail.lecture.domain.service;
/**
 * Interface to access portlet services.
 * @author gbouteil
 *
 */
public interface PortletService {
	
	/**
	 * Get attribute value from the Portlet request
	 * @param attributeName
	 * @return the attribute value defined by "attributeNAme"
	 */
	public String getUserAttribute(String attributeName);
}
