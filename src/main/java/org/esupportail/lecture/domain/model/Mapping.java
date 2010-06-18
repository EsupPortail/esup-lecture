/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A mapping provides a xsltURL and xpath to source view and item access.
 * A mapping can have several entries (at least one):
 * - sourceURL
 * - dtd
 * - xmlns
 * - xmlType
 * - rootElement
 * An XPath can use a set of NamesSpace definition
 * @author gbouteil
 *
 */
public class Mapping {

	/*
	 ************************** PROPERTIES ******************************** */	

	/**
	 * Log instance.
	 */
	@SuppressWarnings("unused")
	private static final Log LOG = LogFactory.getLog(Mapping.class);
	
	/**
	 * Source URL.
	 */
	private String sourceURL = "";
	
	/**
	 * Name of the dtd.
	 */
	private String dtd = "";
	
	/**
	 * Xmlns of the source xml stream.
	 */
	private String xmlns = "";
	
	/**
	 * Xml type.
	 */
	private String xmlType;

	/**
	 * Path of the xslt file.
	 */
	private String xsltUrl = "";
	
	/**
	 * Xpath to get an item in the source xml stream.
	 */
	private String itemXPath = "";

	/**
	 * Optionnal : rootElement of the xmlStream
	 * (one of these parameter is required : sourceURL, xmlns, xmlType, dtd, rootElement).
	 */
	private String rootElement;
	
	/**
	 * Map of namespaces used by Xpath (key: NamesSpace prefix; value: NamaSpace URI).
	 */
	private HashMap<String, String> xPathNameSpaces;
	
	/*
	 ************************** INIT *********************************/	
	
	/**
	 * Constructor. 
	 */
	Mapping() {
		xPathNameSpaces = new HashMap<String, String>();
	}
	
	/*
	 ************************** METHODS ******************************** */	

//	/**
//	 * Return a string containing mapping content : dtd, xsltfile, itemXPath, xmlns and xmlType 
//	 * @see java.lang.Object#toString()
//	 */
//	public String toString(){
//	
//		String string="";
//		
//		/* The dtd name*/
//		string += "	dtd : "+ dtd +"\n";
//
//		/* The path of the xslt file */
//		string += "	xsltFile : "+ xsltUrl +"\n";
//	
//		/* The Xpath to get an item in the source xml stream */
//		string += "	itemXPath : "+ itemXPath +"\n";
//	
//		/* The xmlns of the source xml stream */
//		string += "	xmlns : "+ xmlns +"\n";
//	
//		/* The xml type ??? */
//		string += "	xmlType : "+ xmlType +"\n";
//	
//		return string;
//	}

	/*
	 ************************** ACCESSORS ******************************** */	

	/**
	 * Returns the dtd of the mapping.
	 * @return dtd
	 * @see Mapping#dtd
	 */
	protected String getDtd() {
		return dtd;
	}
	/**
	 * Sets the dtd.
	 * @param dtd
	 * @see Mapping#dtd
	 */
	protected synchronized  void setDtd(final String dtd) {
		this.dtd = dtd;
	}

	/**
	 * Returns the xslt url.
	 * @return xsltUrl
	 */
	protected String getXsltUrl() {
		return xsltUrl;
	}
	
	/**
	 * Sets the xsltFile.
	 * @param xsltUrl xslt URL
	 * @see Mapping#xsltUrl
	 */
	protected void setXsltUrl(final String xsltUrl) {
		this.xsltUrl = xsltUrl;
	}

	/**
	 * Returns the item XPath.
	 * @return itemXPath
	 * @see Mapping#itemXPath
	 */
	protected String getItemXPath() {
		return itemXPath;
	}
	
	/**
	 * Sets the item XPath.
	 * @param itemXPath
	 * @see Mapping#itemXPath
	 */
	protected void setItemXPath(final String itemXPath) {
		this.itemXPath = itemXPath;
	}

	/**
	 * Returns the xmlns.
	 * @return xmlns
	 * @see Mapping#xmlns
	 */
	protected String getXmlns() {
		return xmlns;
	}
	/**
	 * Sets the xmlns.
	 * @param xmlns
	 * @see Mapping#xmlns
	 */
	protected void setXmlns(final String xmlns) {
		this.xmlns = xmlns;
	}

	/**
	 * Returns the XML type.
	 * @return xmlType
	 * @see Mapping#xmlType
	 */
	protected String getXmlType() {
		return xmlType;
	}
	
	/**
	 * Sets the XML type.
	 * @param xmlType
	 * @see Mapping#xmlType
	 */
	protected void setXmlType(final String xmlType) {
		this.xmlType = xmlType;
	}

	/**
	 * Returns the root element of the XML.
	 * @return root element
	 */
	protected String getRootElement() {
		return rootElement;
	}

	/**
	 * Sets the root element of the XML.
	 * @param rootElement root element
	 */
	protected void setRootElement(final String rootElement) {
		this.rootElement = rootElement;
	}
	
	/**
	 * @return source URL
	 */
	protected String getSourceURL() {
		return sourceURL;
	}
	
	/**
	 * set source URL.
	 * @param sourceURL
	 */
	protected void setSourceURL(final String sourceURL) {
		this.sourceURL = sourceURL;
	}
	
	/**
	 * @return map of XPathNameSpaces
	 */
	protected HashMap<String, String> getXPathNameSpaces() {
		return xPathNameSpaces;
	}
	
	/**
	 * set map of XPathNameSpaces.
	 * @param pathNameSpace
	 */
	protected void setXPathNameSpaces(final HashMap<String, String> pathNameSpace) {
		xPathNameSpaces = pathNameSpace;
	}
	
	
}
