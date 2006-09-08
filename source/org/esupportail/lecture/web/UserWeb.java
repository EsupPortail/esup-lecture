package org.esupportail.lecture.web;

/**
 * Informations to display about a user
 * @author gbouteil
 *
 */
public class UserWeb {
	/*
	 ************************ PROPERTIES ******************************** */	

	/**
	 * Access to services
	 */
	private FacadeWeb facadeWeb;
	
	/**
	 * Id of the user
	 */
	private String id;
	

	/*
	 ************************** Initialization *******************************/
	/*
	 *************************** METHODS ************************************/


	/*
	 ************************** ACCESSORS ***********************************/

	

	/**
	 * @return Returns the id.
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return Returns the facadeWeb.
	 */
	public FacadeWeb getFacadeWeb() {
		return facadeWeb;
	}

	/**
	 * @param facadeWeb The facadeWeb to set.
	 */
	public void setFacadeWeb(FacadeWeb facadeWeb) {
		this.facadeWeb = facadeWeb;
	}
	
	
}
