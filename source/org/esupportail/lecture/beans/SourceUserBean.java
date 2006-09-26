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
	
	// Que pour les tests 
	// TODO : à retirer => passer par selectedBEan
	private String content;
	
	// TODO : à retirer : pour les tests
	private String itemXPath;
	private String xslt;
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
		
		// TODO : à retirer => fait par selectedBean (ici que pour les tests)
		content = customSource.getContent();
		
		// TODO : à retirer : pour les tests	
		itemXPath = customSource.getItemXPath();
		xslt = customSource.getXslt();
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
	/**
	 * @return Returns the content.
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param content The content to set.
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * @return Returns the itemXPath.
	 */
	public String getItemXPath() {
		return itemXPath;
	}
	/**
	 * @param itemXPath The itemXPath to set.
	 */
	public void setItemXPath(String itemXPath) {
		this.itemXPath = itemXPath;
	}
	/**
	 * @return Returns the xslt.
	 */
	public String getXslt() {
		return xslt;
	}
	/**
	 * @param xslt The xslt to set.
	 */
	public void setXslt(String xslt) {
		this.xslt = xslt;
	}
	


}
