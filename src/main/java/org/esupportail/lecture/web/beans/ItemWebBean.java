package org.esupportail.lecture.web.beans;

/**
 * @author bourges
 * used to display item informations in view
 */
public class ItemWebBean {
	/**
	 * id of item.
	 */
	private String id;
	/**
	 * html content of item.
	 */
	private String htmlContent;
	/**
	 * Mobile html content of item.
	 */
	private String mobileHtmlContent;
	/**
	 * store if item is read or not.
	 */
	private boolean read;
	/**
	 * Dummy state of this bean.
	 */
	private boolean dummy;
	/**
	 * @return html content of item
	 */
	public String getHtmlContent() {
		return htmlContent;
	}
	/**
	 * @param htmlContent
	 */
	public void setHtmlContent(String htmlContent) {
		this.htmlContent = htmlContent;
	}
	/**
	 * @return the mobileHtmlContent
	 */
	public String getMobileHtmlContent() {
		return mobileHtmlContent;
	}
	/**
	 * @param mobileHtmlContent the mobileHtmlContent to set
	 */
	public void setMobileHtmlContent(String mobileHtmlContent) {
		this.mobileHtmlContent = mobileHtmlContent;
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
	public void setId(final String id) {
		this.id = id;
	}
	/**
	 * @return if item is read or not
	 */
	public boolean isRead() {
		return read;
	}
	/**
	 * @param read
	 */
	public void setRead(final boolean read) {
		this.read = read;
	}
	/**
	 * @param dummy
	 */
	public void setDummy(final boolean dummy) {
		this.dummy = dummy;
	}
	/**
	 * @return the dummy
	 */
	public boolean isDummy() {
		return dummy;
	}
	

}
