/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;

import java.io.Serializable;

import org.esupportail.lecture.domain.DomainTools;

/**
 * Source element : a source can be a managed or personal one.
 * @author gbouteil
 *
 */
/**
 * @author gbouteil
 *
 */
public abstract class Source implements Serializable {
///* ************************** PROPERTIES ******************************** */	

	private String xmlStream = "";

	private int profileId;
	


	

	/**
	 * Opitionnal : DTD of the source (one of these parameter is required : xmlns, xmlType, dtd,rootElement)
	 */
	private String dtd;

	/**
	 * Optionnal : xmlType of the source (one of these parameter is required : xmlns, xmlType, dtd,rootElement)
	 */
	private String xmlType;
	/**
	 * Optionnal : xmlns of the source (one of these parameter is required : xmlns, xmlType, dtd,rootElement)
	 */
	private String xmlns;
	/**
	 * Optionnal : rootElement of the xmlStream (one of these parameter is required : xmlns, xmlType, dtd,rootElement)
	 */
	private String rootElement;
	
	
	/**
	 * URL of the xslt file to display remote source
	 */
	private String xsltURL = "";
	
	/**
	 * Xpath to access item in the XML source file correspoding to this source profile
	 */
	private String itemXPath = "";
	
/* ************************** METHODS ******************************** */	
	
	protected void computeXslt(){
		// TODO revoir cette fonction : à adapter, 
		//voir si on n'a pas aussi ici un computedFeatures 

		Channel channel = DomainTools.getChannel();
		String setXsltURL = getXsltURL();
		String setItemXPath = getItemXPath();
			
		String dtd = getDtd();
		String xmlType = getXmlType();
		String xmlns = getXmlns();
		//TODO faire le root element
		//String rootElement = source.getRootElement();
			
		Mapping m = new Mapping();
		
		if (setXsltURL == null || setItemXPath == null) {
			if (dtd != null) {
				m = channel.getMappingByDtd(dtd);
			} else {
			if (xmlType != null) {
				m = channel.getMappingByXmlType(xmlType);
			} else {
			if (xmlns != null) {
				m = channel.getMappingByXmlns(xmlns);
			}}}
		
			if (setXsltURL == null) {
				setXsltURL = m.getXsltUrl();
			}
			if (setItemXPath == null) {
				setItemXPath = m.getItemXPath();
			}
		}
		
	}
	
	
	
//	public void update(String setItemXPath, String setXsltURL) {
//	//TODO  A mettre dans le computed feature de la ssource ?	
//		itemXPath = setItemXPath;
//		xsltUrl = setXsltURL;
//	}


/* ************************** ACCESSORS ******************************** */	


	public String getDtd() {
		return dtd;
	}


	public void setDtd(String dtd) {
		this.dtd = dtd;
	}


	/**
	 * @return Returns the rootElement.
	 */
	public String getRootElement() {
		return rootElement;
	}


	/**
	 * @param rootElement The rootElement to set.
	 */
	public void setRootElement(String rootElement) {
		this.rootElement = rootElement;
	}


	/**
	 * @return Returns the xmlns.
	 */
	public String getXmlns() {
		return xmlns;
	}


	/**
	 * @param xmlns The xmlns to set.
	 */
	public void setXmlns(String xmlns) {
		this.xmlns = xmlns;
	}


	/**
	 * @return Returns the xmlType.
	 */
	public String getXmlType() {
		return xmlType;
	}


	/**
	 * @param xmlType The xmlType to set.
	 */
	public void setXmlType(String xmlType) {
		this.xmlType = xmlType;
	}


	public String getXmlStream() {
		return xmlStream;
	}


	public void setXmlStream(String xmlStream) {
		this.xmlStream = xmlStream;
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
