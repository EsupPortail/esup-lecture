/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.converters.StringConverter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Namespace;
import org.dom4j.Node;
import org.dom4j.XPath;
import org.dom4j.XPathException;
import org.dom4j.io.SAXReader;
import org.esupportail.lecture.domain.DomainTools;
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
	 * URL of the source (used by mapping to find ItemXpath and Xslt File)
	 */
	private String URL = "";

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
	 * Map of namespaces used by Xpath (key: NamesSpace prefix; value: NamaSpace URI)
	 */
	private HashMap<String, String> XPathNameSpaces;

	/**
	 * Items List of this source
	 */
	private List<Item> Items = new ArrayList<Item>();
	
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
			
		if (log.isDebugEnabled()) {
			log.debug("Source::computeXslt() : "+profileId);			
		}
		String dtd = getDtd();
		if (log.isDebugEnabled()) {
			log.debug("DTD : "+dtd);
		}
		String xmlType = getXmlType();
		if (log.isDebugEnabled()) {
			log.debug("xmlType : "+xmlType);		
		}
		String xmlns = getXmlns();
		if (log.isDebugEnabled()) {
			log.debug("xmlns : "+xmlns);		
		}
		String rootElement = getRootElement();
		if (log.isDebugEnabled()) {
			log.debug("rootElement : "+rootElement);			
		}
			
		Mapping m = new Mapping();
		
		if (setXsltURL == null || setItemXPath == null) {
			//TODO introduire le mapping par source URL
			if (URL != null) {
				m = channel.getMappingBySourceURL(URL);
			} else {
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
				log.warn("Source "+profileId+" does not have any xslt information : no sourceURL, dtd, xmlType, xmlns, rootElement");
			}}}}}
		
			if (m == null) {
				log.warn("Source "+profileId+" does not find xslt in mapping file ");
			} else {
				if (setXsltURL == null) {
					setXsltURL = m.getXsltUrl();
				}
				if (setItemXPath == null) {
					setItemXPath = m.getItemXPath();
				}
				if (XPathNameSpaces == null) {
					//TODO RB : peut-être définir un XPathNameSpaces au niveau de la définition de la source dans la CategoryProfile !!!
					XPathNameSpaces = m.getXPathNameSpaces();
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
		SAXReader reader = new SAXReader();
		try {
			//create dom4j document
			Document document = DocumentHelper.parseText(xmlStream);
			//generate map of namespaces
			HashMap nameSpaces = new HashMap();
			//lauch Xpath find
			XPath xpath = document.createXPath(itemXPath);
			xpath.setNamespaceURIs(XPathNameSpaces);
			List<Node> list = xpath.selectNodes(document);
			//List<Node> list = document.selectNodes(itemXPath);
			Iterator<Node> iter = list.iterator();
			while (iter.hasNext()) {
				Node node = iter.next();
				Item item = new Item();
				String XML = node.asXML();
				//TODO use xslt to have real htmlcontent!!!
				item.setHtmlContent(XML);
				//find MD5 of item content for his ID
				byte[] hash = MessageDigest.getInstance("MD5").digest(XML.getBytes());
				StringBuffer hashString = new StringBuffer();
				for ( int i = 0; i < hash.length; ++i ) {
					String hex = Integer.toHexString(hash[i]);
					if ( hex.length() == 1 ) {
						hashString.append('0');
						hashString.append(hex.charAt(hex.length()-1));
					} 
					else {
						hashString.append(hex.substring(hex.length()-2));
					}
				}
				item.setId(hashString.toString());
				Items.add(item);
			}
		} catch (DocumentException e) {
			if (log.isErrorEnabled()) {
				log.error("Error parsing XML content of the source");
			}
			throw new ErrorException("Error parsing XML content of the source");
		} catch (NoSuchAlgorithmException e) {
			if (log.isErrorEnabled()) {
				log.error("MD5 algorithm not supported");
			}
			throw new ErrorException("MD5 algorithm not supported");
		} catch (XPathException e) {
			if (log.isErrorEnabled()) {
				log.error(e.getMessage());
			}
			throw new ErrorException("Xpath with NamaSpace not specified in mappings.xml");
		}
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
	public List<Item> getItems() {
		if (!isItemComputed){
			computeItems();
		}
		return Items;
	}

	/**
	 * set the Source URL
	 * @param url
	 */
	public void setURL(String url) {
		URL = url;
	}
}
