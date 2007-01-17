package org.esupportail.lecture.dao.beans;

import java.util.Hashtable;

public class MappingDaoBean {

	private String sourceURL;
	private String dtd;
	private String xmlns;
	private String xmlType;
	private String rootElement;
	private String xsltFile;
	private String itemXpath;
	/* Couple <prefix,uri> */
	private Hashtable<String,String> xPathNameSpaces = new Hashtable<String, String>();

	public String getDtd() {
		return dtd;
	}
	public void setDtd(String dtd) {
		this.dtd = dtd;
	}
	public String getItemXpath() {
		return itemXpath;
	}
	public void setItemXpath(String itemXpath) {
		this.itemXpath = itemXpath;
	}
	public String getRootElement() {
		return rootElement;
	}
	public void setRootElement(String rootElement) {
		this.rootElement = rootElement;
	}
	public String getSourceURL() {
		return sourceURL;
	}
	public void setSourceURL(String sourceURL) {
		this.sourceURL = sourceURL;
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
	public Hashtable<String, String> getXPathNameSpaces() {
		return xPathNameSpaces;
	}
	public void setXPathNameSpaces(Hashtable<String, String> pathNameSpaces) {
		xPathNameSpaces = pathNameSpaces;
	}
	public String getXsltFile() {
		return xsltFile;
	}
	public void setXsltFile(String xsltFile) {
		this.xsltFile = xsltFile;
	}
	
}
