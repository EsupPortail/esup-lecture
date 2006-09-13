package org.esupportail.lecture.beans;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.service.FacadeService;

/**
 * Informations to display about a user
 * @author gbouteil
 *
 */
public class UserBean {
	/*
	 ************************ PROPERTIES ******************************** */	

	/**
	 * Log instance
	 */
	protected static final Log log = LogFactory.getLog(UserBean.class);

	/**
	 * Access to services
	 */
	private FacadeService facadeService;
	
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
	 * @return Returns the facadeService.
	 */
	public FacadeService getFacadeService() {
		return facadeService;
	}

	/**
	 * @param facadeService The facadeService to set.
	 */
	public void setFacadeService(FacadeService facadeService) {
		this.facadeService = facadeService;
	}

	
}
