/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.beans;

import java.util.Date;
import java.util.List;

import org.esupportail.lecture.domain.model.Author;
import org.esupportail.lecture.domain.model.ComplexItem;
import org.esupportail.lecture.domain.model.CustomSource;
import org.esupportail.lecture.domain.model.Item;
import org.esupportail.lecture.domain.model.RubriquesPlublisher;

/**
 * used to store item informations.
 * @author bourges
 */
public class ItemBean {
	
	/* 
	 *************************** PROPERTIES ******************************** */	
	
	/**
	 * id of item.
	 */
	private String id;
	/**
	 * html content of item.
	 */
	private String htmlContent;
	/**
	 * html content of item for mobile view.
	 */
	private String mobileHtmlContent;
	/**
	 * store if item is read or not.
	 */
	private boolean read;	
	/**
	 * uidAuthor of item
	 */
	private String uidAuthor;
	/**
	 * rubriques of item
	 */
	private List<RubriquesPlublisher> rubriques;
	/**
	 * Author of item
	 */
	private Author author;
	/**
	 * pubDate of item
	 */
	private Date pubDate;
	
	/*
	 *************************** INIT ************************************** */	
	/**
	 * Default constructor.
	 */
	public ItemBean() {
		super();
	}

	/**
	 * Constructor initializing object.
	 * @param it
	 * @param customSource
	 */
	public ItemBean(final Item it, final CustomSource customSource) {
		id = it.getId();
		htmlContent = it.getHtmlContent();
		mobileHtmlContent = it.getMobileHtmlContent();
		read = customSource.isItemRead(id);
		//nouveau parametrage
		pubDate = it.getPubDate();
		if (it instanceof ComplexItem){
			ComplexItem cIt = (ComplexItem) it;
			this.setUidAuthor(cIt.getAuthor().getUid());
			this.setRubriques(cIt.getRubriques());
			this.setAuthor(cIt.getAuthor());
		}
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
	public void setHtmlContent(final String htmlContent) {
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
	 * @return false because it is not a dummy Item
	 */
	public boolean isDummy() {
		return false;
	}
	
	
	public String getUidAuthor() {
		return uidAuthor;
	}

	public void setUidAuthor(String uidAuthor) {
		this.uidAuthor = uidAuthor;
	}

	public List<RubriquesPlublisher> getRubriques() {
		return rubriques;
	}

	public void setRubriques(List<RubriquesPlublisher> rubriques) {
		this.rubriques = rubriques;
	}

	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}

	public Date getPubDate() {
		return pubDate;
	}

	public void setPubDate(Date pubDate) {
		this.pubDate = pubDate;
	}

	/*
	 *************************** METHODS *********************************** */	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String string = "";
		string += "     Id = " + id + "\n";
		string += "     Html = " + htmlContent + "\n";
		string += "     read = " + read + "\n";
		
		return string;
	}

}
