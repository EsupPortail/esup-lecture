package org.esupportail.lecture.domain.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
	 * Log instance.
	 */
	private static final Log LOG = LogFactory.getLog(ManagedSourceProfile.class); 
	/**
	 * Id of item.
	 */
	private String id;
	/**
	 * html content of item.
	 */
	private String htmlContent;
	
	/**
	 * Source parent of this item.
	 */
	private Source parent;

	/*
	 *************************** INIT ************************************** */	

	/**
	 * Constructor.
	 * @param source parent of the item
	 */
	public Item(final Source source) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Item(" + source.getProfileId() + ")");
		}
		parent = source;
	}
	

	/*
	 *************************** METHODS *********************************** */	

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object object) {
		boolean ret = false;
		if (this == object) {
			ret = true;
		}
		if (object == null) {
			ret = false;
		}
		if (!(object instanceof Item)) {
			ret = false;
		}
		final Item item = (Item) object;
		if (!item.getId().equals(this.getId())) {
			ret = false;
		}
		return ret;
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
	protected void setHtmlContent(final String htmlContent) {
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
	protected void setId(final String id) {
		this.id = id;
	}

	/**
	 * @return parent of the item
	 */
	public Source getParent() {
		return parent;
	}
	

}
