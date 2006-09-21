/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;

/**
 * Source profile element : a source profile can be a managed or personal one.
 * @author gbouteil
 *
 */
public abstract class SourceProfile {

/* ************************** PROPERTIES ******************************** */	

	private String id;

	private String name = "";

	private String sourceURL = "";

	/**
	 * URL of the xslt file to display remote source
	 */
	private String xsltURL = "";
	
	/**
	 * Xpath to access item in the XML source file correspoding to this source profile
	 */
	private String itemXPath = "";


/* ************************** METHODS ******************************** */	

/* ************************** ACCESSORS ******************************** */	

	/**
	 * Returns the source profile Id
	 * @return id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the source profile Id
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Returns the source profile name
	 * @return name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the source profile name
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the source URL of the source profile
	 * @return sourceURL
	 */
	public String getSourceURL() {
		return sourceURL;
	}
	
	/**
	 * Sets the source URL of the source profile
	 * @param sourceURL
	 */
	public void setSourceURL(String sourceURL) {
		this.sourceURL = sourceURL;
	}

	/**
	 * @return Returns the itemXPath.
	 */
	protected String getItemXPath() {
		return itemXPath;
	}

	/**
	 * @param itemXPath The itemXPath to set.
	 */
	protected void setItemXPath(String itemXPath) {
		this.itemXPath = itemXPath;
	}

	/**
	 * @return Returns the xsltURL.
	 */
	protected String getXsltURL() {
		return xsltURL;
	}

	/**
	 * @param xsltURL The xsltURL to set.
	 */
	protected void setXsltURL(String xsltURL) {
		this.xsltURL = xsltURL;
	}

}
