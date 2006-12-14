package org.esupportail.lecture.domain.utils;

/**
 * @author gbouteil
 * Interface to access external container
 */
public interface ModeService {

	/**
	 * Get preference value by given the preference name
	 * @param name name of the preference
	 * @return the value of the preference
	 */
	public String getPreference(String name);
	/**
	 * Get attribute value from the external service
	 * @param attribute
	 * @return the attribute value defined by "attributeNAme"
	 */
	public String getUserAttribute(String attribute);
	/**
	 * Return true if the current user of the is in "group" of the external service
	 * @param group
	 * @return true or false
	 */
	public boolean isUserInGroup(String group);

}
