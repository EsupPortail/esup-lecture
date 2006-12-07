package org.esupportail.lecture.dao.beans;

public class SourceDaoBean {

	private String dtd;
	private String rootElement;
	private String xmlns;
	private String xmlStream;
	private String xmlType;
	private String url;
	
	public String getDtd() {
		return dtd;
	}
	public void setDtd(String dtd) {
		this.dtd = dtd;
	}
	public String getRootElement() {
		return rootElement;
	}
	public void setRootElement(String rootElement) {
		this.rootElement = rootElement;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getXmlns() {
		return xmlns;
	}
	public void setXmlns(String xmlns) {
		this.xmlns = xmlns;
	}
	public String getXmlStream() {
		return xmlStream;
	}
	public void setXmlStream(String xmlStream) {
		this.xmlStream = xmlStream;
	}
	public String getXmlType() {
		return xmlType;
	}
	public void setXmlType(String xmlType) {
		this.xmlType = xmlType;
	}
}
