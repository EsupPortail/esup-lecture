/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.dom4j.XPath;
import org.dom4j.XPathException;
import org.esupportail.lecture.domain.DomainTools;
import org.esupportail.lecture.exceptions.domain.ComputeItemsException;
import org.esupportail.lecture.exceptions.domain.MappingNotFoundException;
import org.esupportail.lecture.exceptions.domain.Xml2HtmlException;

/**
 * Source element : a source can be a managed or personal one. A source is got
 * from an URL, given by source Profile. This source contains an xmlStream that,
 * after xslt parsing, provides a list of items that can be displed on user
 * interface
 *
 * @author gbouteil
 *
 */

public abstract class Source implements Element, Serializable {

	/*
	 *************************** PROPERTIES ********************************
	 */

	/**
	 * log instance.
	 */
	protected static final Log LOG = LogFactory.getLog(Source.class);
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * profile Id of the source.
	 */
	private String profileId;
	/**
	 * sourceProfile associated to this source.
	 */
	private SourceProfile profile;

	/**
	 * Optional : DTD of the source (one of these parameter is required : xmlns,
	 * xmlType, dtd,rootElement).
	 */
	private String dtd;
	/**
	 * Optional : xmlType of the source (one of these parameter is required :
	 * xmlns, xmlType, dtd,rootElement).
	 */
	private String xmlType;
	/**
	 * Optional : xmlns of the source (one of these parameter is required :
	 * xmlns, xmlType, dtd,rootElement).
	 */
	private String xmlns;
	/**
	 * Optionnal : rootElement of the xmlStream. (one of these parameter is
	 * required : xmlns, xmlType, dtd,rootElement)
	 */
	private String rootElement;

	/**
	 * xmlStream (XML content) of the source.
	 */
	private String xmlStream = "";

	/**
	 * flag used to know if computeXslt() used one time or not.
	 */
	private boolean xsltComputed;
	/**
	 * URL of the xslt file to display xml content.
	 */
	private String xsltURL;
	/**
	 * URL of the xslt file to display xml content on mobile.
	 */
	private String mobileXsltURL;
	/**
	 * Xpath to access item in the XML source file correspoding to this source
	 * profile.
	 */
	private String itemXPath;
	// TODO (RB <-- GB) Not used ???
	/**
	 * Map of namespaces used by Xpath (key: NamesSpace prefix; value: NamaSpace
	 * URI).
	 */
	private HashMap<String, String> xPathNameSpaces;

	/**
	 * Items List of this source.
	 */
	private List<Item> items = new ArrayList<Item>();
	/**
	 * flag used to know if items are computed.
	 */
	private boolean itemComputed;

	/*
	 *************************** INIT ********************************
	 */

	/**
	 * Constructor.
	 *
	 * @param sp
	 *            sourceProfile associated to this source
	 */
	protected Source(final SourceProfile sp) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Source(" + sp.getId() + ")");
		}
		profile = sp;
		profileId = sp.getId();
		itemComputed = false;
		xPathNameSpaces = new HashMap<String, String>();
	}

	/*
	 *************************** METHODS ********************************
	 */

	/**
	 * Returns the source URL (defined in the source profile).
	 *
	 * @return sourceURL
	 */
	private String getSourceURL() {
		return getProfile().getSourceURL();
	}

	/**
	 * @return the name of the source
	 */
	public String getName() {
		return getProfile().getName();
	}

	/**
	 * @return the ttl of the sourceProfile for this source
	 */
	public int getTtl() {
		return getProfile().getTtl();
	}

	/**
	 * @return Returns the itemXPath.
	 * @throws MappingNotFoundException
	 */
	private String getItemXPath() throws MappingNotFoundException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("id=" + this.profileId + " - getItemXPath()");
		}
		computeXslt();
		return itemXPath;
	}

	/**
	 * @return Returns the xsltURL.
	 * @throws MappingNotFoundException
	 */
	private String getXsltURL() throws MappingNotFoundException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("id=" + this.profileId + " - getXsltURL()");
		}
		computeXslt();
		return xsltURL;
	}

	/**
	 * @return Returns the xsltURL.
	 * @throws MappingNotFoundException
	 */
	private String getMobileXsltURL() throws MappingNotFoundException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("id=" + this.profileId + " - getMobileXsltURL()");
		}
		computeXslt();
		return mobileXsltURL;
	}

	/**
	 * @return the hashMap containing xPathNameSpaces
	 * @throws MappingNotFoundException
	 */
	private HashMap<String, String> getXPathNameSpaces() throws MappingNotFoundException {
		computeXslt();
		return xPathNameSpaces;
	}

	/**
	 * find item XPath and url of Xslt file, in list of Mappings in channel
	 * (from mapping file). In fonction of dtd, xmlType, xmlns or XML root
	 * element of the source XML content
	 *
	 * @throws MappingNotFoundException
	 */
	private void computeXslt() throws MappingNotFoundException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("id = " + this.profileId + " - computeXslt()");
		}

		Channel channel = DomainTools.getChannel();

		if (LOG.isDebugEnabled()) {
			LOG.debug("Source::computeXslt() : " + profileId);
			LOG.debug("DTD : " + dtd);
			LOG.debug("xmlType : " + xmlType);
			LOG.debug("xmlns : " + xmlns);
			LOG.debug("rootElement : " + rootElement);
		}

		if (!xsltComputed) {
			SourceProfile p = getProfile();
			xsltURL = p.getXsltURL();
			mobileXsltURL = p.getMobileXsltURL();
			itemXPath = p.getItemXPath();
			xPathNameSpaces = p.getXPathNameSpaces();

			if (xsltURL == null || itemXPath == null || xPathNameSpaces.size() == 0) {
				Mapping m = new Mapping();
				String url = getSourceURL();
				if (url != null) {
					// Try to find a mapping from url
					m = channel.getMappingBySourceURL(url);
				} else {
					LOG.error("Source " + this.profileId + "does not have any URL defined");
				}
				// no mapping find from url so using XML content caracteristics
				if (m == null) {
					if (dtd != null) {
						m = channel.getMappingByDtd(dtd);
					}
				}
				if (m == null) {
					if (xmlType != null) {
						m = channel.getMappingByXmlType(xmlType);
					}
				}
				if (m == null) {
					if (xmlns != null) {
						m = channel.getMappingByXmlns(xmlns);
					}
				}
				if (m == null) {
					if (rootElement != null) {
						m = channel.getMappingByRootElement(rootElement);
					}
				}
				if (m == null) {
					LOG.warn("Source " + profileId + " does not have any entry key to find xslt information : "
							+ "no dtd, xmlType, xmlns, rootElement");
					String errorMsg = "Mapping not found for source " + profileId;
					LOG.error(errorMsg);
					throw new MappingNotFoundException("Mapping not found for source " + profileId);
				}
				if (xsltURL == null || xsltURL.equals("")) {
					xsltURL = m.getXsltUrl();
				}
				if (mobileXsltURL == null || mobileXsltURL.equals("")) {
					mobileXsltURL = m.getMobileXsltUrl();
				}
				if (itemXPath == null || itemXPath.equals("")) {
					itemXPath = m.getItemXPath();
				}
				if (xPathNameSpaces.size() == 0) {
					xPathNameSpaces = m.getXPathNameSpaces();
				}
			}
			xsltComputed = true;
		}
	}

	/**
	 * Make Items objects in fonction of itemXPath, xsltURL, xmlStream.
	 *
	 * @throws ComputeItemsException
	 */
	@SuppressWarnings("unchecked")
	synchronized private void computeItems(ItemParser parser) throws ComputeItemsException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("id=" + this.profileId + " - computeItems()");
		}
		if (!itemComputed) {
			boolean isComplex = (parser != null);
			try {
				// lauch Xpath find
				String x = getItemXPath();
				// create dom4j document
				Document document = null;

				if (isComplex) {
					parser.setItemXpath(this.getItemXPath());
					if (LOG.isTraceEnabled()) {
						LOG.trace("computeItems() - will parse xml from ItemParser: " + parser.getXMLStream());
					}
					document = DocumentHelper.parseText(parser.getXMLStream());
				} else {
					document = DocumentHelper.parseText(xmlStream);
				}
				// get encoding
				String encoding = document.getXMLEncoding();

				XPath xpath = document.createXPath(x);
				xpath.setNamespaceURIs(getXPathNameSpaces());
				List<Node> list = xpath.selectNodes(document);
				// List<Node> list = document.selectNodes(getItemXPath());
				Iterator<Node> iter = list.iterator();
				while (iter.hasNext()) {
					Node node = iter.next();
					Item item;
					if (!isComplex) {
						item = new Item(this);
					} else {
						// pour le nouveau parametrage
						item = new ComplexItem(this);
					}

					StringBuffer xml = new StringBuffer("<?xml version=\"1.0\" encoding=\"");
					xml.append(encoding);
					xml.append("\" ?>");
					xml.append(node.asXML());
					String xmlAsString = xml.toString();
					String htmlContent = xml2html(xmlAsString, getXsltURL(), encoding);
					item.setHtmlContent(htmlContent);
					String MobileHtmlContent = xml2html(xmlAsString, getMobileXsltURL(), encoding);
					item.setMobileHtmlContent(MobileHtmlContent);
					// find MD5 of item content for his ID

					byte[] hash = MessageDigest.getInstance("MD5").digest(xmlAsString.getBytes());
					StringBuffer hashString = new StringBuffer();
					for (int i = 0; i < hash.length; ++i) {
						String hex = Integer.toHexString(hash[i]);
						if (hex.length() == 1) {
							hashString.append('0');
							hashString.append(hex.charAt(hex.length() - 1));
						} else {
							hashString.append(hex.substring(hex.length() - 2));
						}
					}
					item.setId(hashString.toString());
					
					Node nodeDate;
					if (isComplex) {
						Node itemIdNode =  node.selectSingleNode("uuid");
						if (itemIdNode != null && item instanceof ComplexItem) {
							String itemId = itemIdNode.getText();
							((ComplexItem) item).setAuthor(parser.getItemAuthors().get(itemId));
							((ComplexItem) item).setRubriques(parser.getRubriquesItem().get(itemId));
							((ComplexItem) item).setVisibility(parser.getItemsVisibility().get(itemId));
						}
						nodeDate = node.selectSingleNode("article/pubDate");
					} else {
						nodeDate = node.selectSingleNode("pubDate");
					}
					if (nodeDate == null) {
							nodeDate = node.selectSingleNode("item/pubDate");
					} 
					try {
						if (nodeDate != null) {
							SimpleDateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);
							Date odt = df.parse(nodeDate.getText());
							item.setPubDate(odt);
						} else {
							LOG.debug("No date to parse in node :\n" + node.asXML());
						}
					} catch (Exception e) {
						String errorMsg = "Error parsing Date of the item " + node.asXML();
						LOG.error(errorMsg, e);
					}
					items.add(item);
				}
			} catch (DocumentException e) {
				String errorMsg = "Error parsing XML content of the source";
				LOG.error(errorMsg, e);
				throw new ComputeItemsException(errorMsg, e);
			} catch (NoSuchAlgorithmException e) {
				String errorMsg = "MD5 algorithm not supported";
				LOG.error(errorMsg, e);
				throw new ComputeItemsException(errorMsg, e);
			} catch (XPathException e) {
				String errorMsg = "Xpath with NameSpace not specified in mappings.xml";
				LOG.error(errorMsg, e);
				throw new ComputeItemsException(errorMsg, e);
			} catch (MappingNotFoundException e) {
				String errorMsg = "Impossible to get itemXPath,XPathNameSpaces and xsltURL";
				LOG.error(errorMsg, e);
				throw new ComputeItemsException(errorMsg, e);
			} catch (Xml2HtmlException e) {
				String errorMsg = "Impossible to make html content";
				LOG.error(errorMsg, e);
				throw new ComputeItemsException(errorMsg, e);
			}
			itemComputed = true;
		}
	}

	/**
	 * transform a xml String in a html String with an XSLT.
	 *
	 * @param xml
	 *            to transform
	 * @param xsltFileURL
	 *            URL of XSLT file
	 * @param encoding
	 *            of xml to transform
	 * @return html content
	 * @throws Xml2HtmlException
	 */
	private String xml2html(final String xml, final String xsltFileURL, final String encoding)
			throws Xml2HtmlException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("id=" + this.profileId + " - xml2html(xml,xsltFileURL)");
		}
		LOG.debug("voici le xsltFileUrl : " + xsltFileURL);
		String ret = null;
		try {
			// 1. Instantiate a TransformerFactory.
			TransformerFactory tFactory = TransformerFactory.newInstance();
			// 2. Use the TransformerFactory to process the stylesheet Source
			// and
			// generate a Transformer.
			Transformer transformer;
			String xsltFileContent = DomainTools.getXsltFile(xsltFileURL);
			// create dom4j document
			Document document = DocumentHelper.parseText(xsltFileContent);
			// get encoding
			String xslEncoding = document.getXMLEncoding();
			if (xslEncoding == null) {
				xslEncoding = "UTF-8";
			}
			LOG.debug("voici le xsltFileContent : " + xsltFileContent);
			ByteArrayInputStream inputXsltFile = new ByteArrayInputStream(xsltFileContent.getBytes(xslEncoding));
			StreamSource sourceXsltFile = new StreamSource(inputXsltFile);
			transformer = tFactory.newTransformer(sourceXsltFile);
			// 3. Use the Transformer to transform an XML Source and send the
			// output to a Result object.
			ByteArrayInputStream inputStream = new ByteArrayInputStream(xml.getBytes(encoding));
			StreamSource source = new StreamSource(inputStream);
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			StreamResult result = new StreamResult(outputStream);
			transformer.transform(source, result);
			ret = outputStream.toString("UTF-8");
		} catch (TransformerConfigurationException e) {
			String errorMsg = "Error in XSTL Transformation configuration";
			LOG.error(errorMsg);
			throw new Xml2HtmlException(errorMsg, e);
		} catch (TransformerException e) {
			String errorMsg = "Error in XSTL Transformation";
			LOG.error(errorMsg);
			throw new Xml2HtmlException(errorMsg, e);
		} catch (IOException e) {
			String errorMsg = "IO Error in xml2html";
			LOG.error(errorMsg);
			throw new Xml2HtmlException(errorMsg, e);
		} catch (DocumentException e) {
			String errorMsg = "Error determining XSLT encoding";
			LOG.error(errorMsg);
			throw new Xml2HtmlException(errorMsg, e);
		}
		return ret;
	}

	/**
	 * get Items list of this source.
	 *
	 * @return the items lits
	 * @throws ComputeItemsException
	 */
	protected List<Item> getItems(ItemParser parser) throws ComputeItemsException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("id=" + this.profileId + " - getItems()");
		}
		computeItems(parser);
		return items;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer out = new StringBuffer();
		out.append("\n dtd --> ").append(getDtd());
		out.append("\n rootElement --> ").append(getRootElement());
		out.append("\n xmlns --> ").append(getXmlns());
		out.append("\n xmlType --> ").append(getXmlType());
		out.append("\n xmlStream --> ").append(getXmlStream());
		try {
			out.append("\n xsltURL --> ").append(getXsltURL());
			out.append("\n itemXPath --> ").append(getItemXPath());
		} catch (MappingNotFoundException e) {
			out.append("\n MappingNotFoundException !");
		}
		return out.toString();
	}

	/*
	 ************************* ACCESSORS ********************************
	 */

	/**
	 * @return the ID of the sourceProfile associated to this source
	 */
	public String getProfileId() {
		return profileId;
	}

	/**
	 * @return the sourceProfile associated to this source
	 */
	public SourceProfile getProfile() {
		return profile;
	}

	/**
	 * @return the dtd of source XML content
	 */
	private String getDtd() {
		return dtd;
	}

	/**
	 * set the dtd of source XML content.
	 *
	 * @param dtd
	 */
	public synchronized void setDtd(final String dtd) {
		this.dtd = dtd;
	}

	/**
	 * @return Returns the rootElement.
	 */
	private String getRootElement() {
		return rootElement;
	}

	/**
	 * @param rootElement
	 *            The rootElement to set.
	 */
	public void setRootElement(final String rootElement) {
		this.rootElement = rootElement;
	}

	/**
	 * @return Returns the xmlns.
	 */
	private String getXmlns() {
		return xmlns;
	}

	/**
	 * @param xmlns
	 *            The xmlns to set.
	 */
	public void setXmlns(final String xmlns) {
		this.xmlns = xmlns;
	}

	/**
	 * @return Returns the xmlType.
	 */
	private String getXmlType() {
		return xmlType;
	}

	/**
	 * @param xmlType
	 *            The xmlType to set.
	 */
	public void setXmlType(final String xmlType) {
		this.xmlType = xmlType;
	}

	/**
	 * @return XML Stream (XML content) of the source
	 */
	private String getXmlStream() {
		return xmlStream;
	}

	/**
	 * set XML Stream (XML content) of the source.
	 *
	 * @param xmlStream
	 */
	public void setXmlStream(final String xmlStream) {
		this.xmlStream = xmlStream;
	}

}
