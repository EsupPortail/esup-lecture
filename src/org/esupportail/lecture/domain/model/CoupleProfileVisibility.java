/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;

/**
 * Couple associating a profile with its VisibilityMode.
 * @author gbouteil
 *
 */
public class CoupleProfileVisibility {
	/* 
	 *************************** PROPERTIES ******************************** */
	/**
	 * The profile interested.
	 */
	private ElementProfile profile;
	
	/**
	 * the visibility mode of the profile.
	 */
	private VisibilityMode mode;

	/*
	 *************************** INIT ************************************** */

	/**
	 * Constructor.
	 */
	public CoupleProfileVisibility() {
		// empty
	}

	
	/**
	 * Constructor.
	 * @param elt
	 * @param m
	 */
	public CoupleProfileVisibility(final ElementProfile elt, final VisibilityMode m) {
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
	public VisibilityMode getMode() {
		return mode;
	}

	/**
	 * @param mode
	 */
	protected void setMode(final VisibilityMode mode) {
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
