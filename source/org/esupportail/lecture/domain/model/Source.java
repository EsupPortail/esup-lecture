/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;

/**
 * Source element : a source can be a managed or personal one.
 * @author gbouteil
 *
 */
/**
 * @author gbouteil
 *
 */
public abstract class Source {
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
	
/* ************************** METHODS ******************************** */	

/* ************************** ACCESSORS ******************************** */	


	protected String getDtd() {
		return dtd;
	}


	protected void setDtd(String dtd) {
		this.dtd = dtd;
//	}
//
//	protected String getXmlStream() {
//		return xmlStream;
//	}
//
//	protected void setXmlStream(String xmlStream) {
//		this.xmlStream = xmlStream;
//	}
//
	
	

}


	/**
	 * @return Returns the rootElement.
	 */
	protected String getRootElement() {
		return rootElement;
	}


	/**
	 * @param rootElement The rootElement to set.
	 */
	protected void setRootElement(String rootElement) {
		this.rootElement = rootElement;
	}


	/**
	 * @return Returns the xmlns.
	 */
	protected String getXmlns() {
		return xmlns;
	}


	/**
	 * @param xmlns The xmlns to set.
	 */
	protected void setXmlns(String xmlns) {
		this.xmlns = xmlns;
	}


	/**
	 * @return Returns the xmlType.
	 */
	protected String getXmlType() {
		return xmlType;
	}


	/**
	 * @param xmlType The xmlType to set.
	 */
	protected void setXmlType(String xmlType) {
		this.xmlType = xmlType;
	}
}
