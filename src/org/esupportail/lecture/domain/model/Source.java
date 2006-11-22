/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.esupportail.lecture.domain.DomainTools;
import org.esupportail.lecture.domain.beans.ItemBean;
import org.esupportail.lecture.exceptions.ErrorException;

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

	/**
	 * xmlStream (XML content) of the source
	 */
	private String xmlStream = "";

	/**
	 * profile Id of the source
	 */
	private int profileId;

	/**
	 * Log instance 
	 */
	protected static final Log log = LogFactory.getLog(Source.class); 

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
	private String xsltURL;
	
	/**
	 * Xpath to access item in the XML source file correspoding to this source profile
	 */
	private String itemXPath;
	
	/**
	 * flag used to know if computeXslt() used one time or not  
	 */
	private boolean isXsltComputed = false;

	/**
	 * flag used to know if computeItems() used one time or not  
	 */
	private boolean isItemComputed = false;
	
	/**
	 * Items List of this source
	 */
	private List<ItemBean> Items = new ArrayList<ItemBean>();
	
/* ************************** METHODS ******************************** */	
	
	/**
	 * find item XPath and url of Xslt file, in Mapping file, in fonction of dtd, xmlType, 
	 * xmlns or XML root element of the source XML content
	 */
	protected void computeXslt(){
		// TODO revoir cette fonction : à adapter, 
		//voir si on n'a pas aussi ici un computedFeatures 

		Channel channel = DomainTools.getChannel();
		String setXsltURL = xsltURL;
		String setItemXPath = itemXPath;
			
		log.debug("Source::computeXslt() : "+profileId);
		String dtd = getDtd();
		log.debug("DTD : "+dtd);
		String xmlType = getXmlType();
		log.debug("xmlType : "+xmlType);
		String xmlns = getXmlns();
		log.debug("xmlns : "+xmlns);
		String rootElement = getRootElement();
		log.debug("rootElement : "+rootElement);
			
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
			} else {
			if (rootElement != null) {
				m = channel.getMappingByRootElement(rootElement);
			} else {
				log.warn("Source "+profileId+" does not have any xslt information : no dtd, xmlType, xmlns, rootElement");
			}}}}
		
			if (m == null) {
				log.warn("Source "+profileId+" does not find xslt in mapping file ");
			} else {
		
				if (setXsltURL == null) {
					setXsltURL = m.getXsltUrl();
				}
				if (setItemXPath == null) {
					setItemXPath = m.getItemXPath();
				}
			}
		}
		this.itemXPath = setItemXPath;
		this.xsltURL = setXsltURL;
		isXsltComputed = true;
	}
	
	/**
	 * find Items objects in fonction of itemXPath, xsltURL, xmlStream
	 */
	protected void computeItems() {
		if (!isXsltComputed){
			computeXslt();
		}
//		SAXReader reader = new SAXReader();
//		try {
//			Document document = reader.read(xmlStream);
//			List list = document.selectNodes();
//
//	        for (Iterator iter = list.iterator(); iter.hasNext(); ) {
//	            Attribute attribute = (Attribute) iter.next();
//	            String url = attribute.getValue();
//	        }			
//		} catch (DocumentException e) {
//			if (log.isErrorEnabled()) {
//				log.error("Error parsing XML content of the source");
//			}
//			throw new ErrorException("Error parsing XML content of the source");
//		}
	}
	
//	public void update(String setItemXPath, String setXsltURL) {
//	//TODO  A mettre dans le computed feature de la ssource ?	
//		itemXPath = setItemXPath;
//		xsltUrl = setXsltURL;
//	}


/* ************************** ACCESSORS ******************************** */	


	/**
	 * @return the dtd of source XML content
	 */
	public String getDtd() {
		return dtd;
	}


	/**
	 * set the dtd of source XML content
	 * @param dtd
	 */
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


	/**
	 * @return XML Stream (XML content) of the source
	 */
	public String getXmlStream() {
		return xmlStream;
	}


	/**
	 * set XML Stream (XML content) of the source
	 * @param xmlStream
	 */
	public void setXmlStream(String xmlStream) {
		this.xmlStream = xmlStream;
	}
	
	/**
	 * @return Returns the itemXPath.
	 */
	public String getItemXPath() {
		//TODO public for test class
		if (!isXsltComputed){
			computeXslt();
		}
		return itemXPath;
	}

	/**
	 * @param itemXPath The itemXPath to set.
	 */
	protected void setItemXPath(String itemXPath) {
		this.itemXPath = itemXPath;
		isXsltComputed = false;
	}

	/**
	 * @return Returns the xsltURL.
	 */
	public String getXsltURL() {
		//TODO public for test class
		if (!isXsltComputed){
			computeXslt();
		}
		return xsltURL;
	}

	/**
	 * @param xsltURL The xsltURL to set.
	 */
	protected void setXsltURL(String xsltURL) {
		this.xsltURL = xsltURL;
		isXsltComputed = false;
	}

	/**
	 * @return the Content of source
	 */
	public String getContent() {
//		 TODO : normalemùent, c'est un html qu'on passe : xml transformé avec son xslt + itemXpath ...
		// pour l'instant, on simplilfie :
		return xmlStream;
		
		
	}

	/**
	 * get Items list of this source
	 * @return the items lits
	 */
	public List<ItemBean> getItems() {
		if (!isItemComputed){
			computeItems();
		}
		return Items;
	}
}
