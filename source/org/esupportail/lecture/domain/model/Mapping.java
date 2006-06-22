package org.esupportail.lecture.domain.model;

/**
 * An xslt and xpath mapping to source view and item acess
 * @author gbouteil
 *
 */

public class Mapping {
/* ************************** PROPERTIES ******************************** */	

	/**
	 * The name of the dtd
	 */
	private String dtd = "";
	
	/**
	 * The path of the xslt file
	 */
	private String xsltFile = "";
	
	/**
	 * The Xpath to get an item in the source xml stream
	 */
	private String itemXPath = "";
	
	/**
	 * The xmlns of the source xml stream
	 */
	private String xmlns = "";
	
	/**
	 * The xml type ???
	 */
	private String xmlType;

/* ************************** ACCESSORS ******************************** */	

	public String getDtd() {
		return dtd;
	}
	public void setDtd(String dtd) {
		this.dtd = dtd;
	}

	public String getXsltFile() {
		return xsltFile;
	}
	public void setXsltFile(String xsltFile) {
		this.xsltFile = xsltFile;
	}

	public String getItemXPath() {
		return itemXPath;
	}
	public void setItemXPath(String itemXPath) {
		this.itemXPath = itemXPath;
	}

	public String getXmlns() {
		return xmlns;
	}
	public void setXmlns(String xmlns) {
		this.xmlns = xmlns;
	}

	public String getXmlType() {
		return xmlType;
	}
	public void setXmlType(String xmlType) {
		this.xmlType = xmlType;
	}

}
