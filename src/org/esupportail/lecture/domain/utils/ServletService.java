package org.esupportail.lecture.domain.utils;
/**
 * @author gbouteil
 * Access to external in serlvet mode
 */
// TODO make better
public class ServletService implements ModeService {

	/**
	 * @see org.esupportail.lecture.domain.utils.ModeService#getPreference(java.lang.String)
	 */
	public String getPreference(String name) {
//		default return value in case of serlvet use case (not normal mode)
		return name;
	}

	/**
	 * @see org.esupportail.lecture.domain.utils.ModeService#getUserAttribute(java.lang.String)
	 */
	public String getUserAttribute(String attribute) {
//		default return value in case of serlvet use case (not normal mode)
		return attribute;
	}

	/**
	 * @see org.esupportail.lecture.domain.utils.ModeService#isUserInGroup(java.lang.String)
	 */
	public boolean isUserInGroup(String group) {
//		default return value in case of serlvet use case (not normal mode)
		return false;
	}

}
