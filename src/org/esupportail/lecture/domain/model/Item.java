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

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null) return false;
		if (!(o instanceof Item)) return false;
		final Item item = (Item) o;
		if (!item.getId().equals(this.getId())) return false;
		return true;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return this.getId().hashCode();
	}

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
