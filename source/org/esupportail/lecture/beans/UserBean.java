package org.esupportail.lecture.beans;

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
	 * Access to services
	 */
	private FacadeService facadeService;
	
	/**
	 * Id of the user
	 */
	private String id;
	
	/**
	 * User profile
	 */
	
	/**
	 * Context to display for the user
	 */
	private ContextUserBean context;
	

	
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

	/**
	 * @return Returns the context.
	 * 
	 */
//	public ContextUserBean getContext() {
//		// Context must or not be refresh to each request ?
//		if (context == null) {
//			context = facadeService.getDomainService().getContextUserBean(this);
//		}
//		return context;
//	}

	/**
	 * @param context The context to set.
	 */
	public void setContext(ContextUserBean context) {
		this.context = context;
	}


	
}
