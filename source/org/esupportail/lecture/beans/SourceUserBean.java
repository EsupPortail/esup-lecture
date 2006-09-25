package org.esupportail.lecture.beans;

import org.esupportail.lecture.domain.model.CategoryProfile;
import org.esupportail.lecture.domain.model.CustomSource;
import org.esupportail.lecture.domain.model.SourceProfile;

/**
 * Bean to display a source according to a user profile
 * @author gbouteil
 *
 */
public class SourceUserBean {
	/*
	 ************************ PROPERTIES ******************************** */	
	/**
	 * Name of the source
	 */
	private String name;
	
	
	/*
	 ************************ INIT ******************************** */	
	
	/**
	 * Init with the customSource to display in left part of screen
	 * (source content is displayed by another way)
	 * @param customSource
	 */
	public void init(CustomSource customSource) {
		SourceProfile profile = customSource.getSourceProfile();
		//TODO a voir quel name on met (cat ou profileCat)
		setName(profile.getName());
	}	
	/*
	 ************************ ACCESSORS ******************************** */


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
