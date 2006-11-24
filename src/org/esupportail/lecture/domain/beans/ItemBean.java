package org.esupportail.lecture.domain.beans;

import org.esupportail.lecture.domain.model.Item;

/**
 * @author bourges
 * used to store item informations
 */
public class ItemBean {
	/**
	 * id of item
	 */
	private String id;
	/**
	 * html content of item
	 */
	private String htmlContent;
	/**
	 * store if item is read or not
	 */
	private boolean read;
	
	public ItemBean(Item i){
		id = i.getId();
		htmlContent = i.getHtmlContent();
		read = i.isRead();
	}
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
	 * @return id of item
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id
	 */
	public void setId(String id) {
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
	public void setRead(boolean read) {
		this.read = read;
	}
	
	public String toString(){
		String string = "";
		string += "     Id = " + id + "\n";
		string += "     Html = " + htmlContent + "\n";
		string += "     read = " + read + "\n";
		
		return string;
	}

}
