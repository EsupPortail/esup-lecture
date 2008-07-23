/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;

/**
 * Used to set availability of a user on an elementProfile.
 * @author gbouteil
 *
 */
public class ProfileAvailability {
	/* 
	 *************************** PROPERTIES ******************************** */
	/**
	 * The profile interested.
	 */
	private ElementProfile profile;
	
	/**
	 * the availability mode of the profile.
	 */
	private AvailabilityMode mode;

	/*
	 *************************** INIT ************************************** */

	/**
	 * Constructor.
	 * @param elt
	 * @param m
	 */
	public ProfileAvailability(final ElementProfile elt, final AvailabilityMode m) {
		this.profile = elt;
		this.mode = m;
	}

	/*
	 *************************** METHODS *********************************** */

	/*
	 *************************** ACCESSORS ********************************* */
	/**
	 * @return mode
	 */
	public AvailabilityMode getMode() {
		return mode;
	}

	/**
	 * @param mode
	 */
	protected void setMode(final AvailabilityMode mode) {
		this.mode = mode;
	}

	/**
	 * @return ElementProfile
	 */
	public ElementProfile getProfile() {
		return profile;
	}

	/**
	 * @param profile
	 */
	protected void setProfile(final ElementProfile profile) {
		this.profile = profile;
	}
}
