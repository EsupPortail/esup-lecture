package org.esupportail.lecture.beans;

import org.esupportail.lecture.domain.service.FacadeService;

/**
 * Bean to display a context according to a user profile
 * @author gbouteil
 *
 */
public class ContextUserBean {
	/*
	 ************************ PROPERTIES ******************************** */	

	/**
	 * Access to services
	 */
	private FacadeService facadeService;
	
	/**
	 * Name of the context
	 */
	private String name;
	
	/**
	 * Description of the context
	 */
	private String description;

	/*
	 ************************ ACCESSORS ******************************** */	

	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
}
