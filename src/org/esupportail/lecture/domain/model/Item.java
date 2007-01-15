package org.esupportail.lecture.domain.model;

/**
 * @author bourges
 * It is the smallest unit information. 
 * It is self-supported : it represent an XML element extracted from an XML Stream get from a source.
 * This XML element is yet parsed and we can get the HTML content to be displayed on user interface.
 */
public class Item {

	/* 
	 *************************** PROPERTIES ******************************** */	

	/**
	 * Id of item
	 */
	private String id;
	/**
	 * html content of item
	 */
	private String htmlContent;

	/*
	 *************************** INIT ************************************** */	
	/*
	 *************************** METHODS *********************************** */	

	/*
	 *************************** ACCESSORS ********************************* */	

	/**
	 * @return html content of item
	 */
	public String getHtmlContent() {
		return htmlContent;
	}
	/**
	 * @param htmlContent
	 */
	synchronized protected void setHtmlContent(String htmlContent) {
		this.htmlContent = htmlContent;
	}
	/**
	 * @return id of item
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id
	 */
	synchronized protected void setId(String id) {
		this.id = id;
	}

	

}
