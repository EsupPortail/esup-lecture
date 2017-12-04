package org.esupportail.lecture.dao;

import java.io.IOException;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentType;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.io.SAXReader;
import org.esupportail.lecture.domain.model.GlobalSource;
import org.esupportail.lecture.domain.model.Source;
import org.esupportail.lecture.domain.model.SourceProfile;
import org.esupportail.lecture.exceptions.dao.XMLParseException;

/**
 * Get a Freash Managed Category from a distinct Thread.
 * @author bourges
 */
public class FreshSourceThread extends Thread {

	/**
	 * Log instance.
	 */
	private static final Log LOG = LogFactory.getLog(FreshSourceThread.class);
	/**
	 * Exception generated in this Thread.
	 */
	private Exception exception;
	/**
	 * Source to return by this Thread.
	 */
	private Source source;
	/**
	 * SourceProfile used to return a Source.
	 */
	private SourceProfile profile;
	/**
	 * Proxy ticket CAS 
	 * null for anonymous access.
	 */
	private String ptCas;

	private HttpClient httpClient;

	/**
	 * Constructor.
	 * @param sourceProfile used to return a Source
	 * @param ptCas
	 */
	public FreshSourceThread(final SourceProfile sourceProfile, final String ptCas, final HttpClient httpClient) {
		this.profile = sourceProfile;
		this.ptCas = ptCas;
		this.exception = null;
		this.httpClient = httpClient;
	}

	/**
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		try {
			this.source = getFreshSource(profile, ptCas);
		} catch (XMLParseException e) {
			this.exception = e;
		}
	}

	/**
	 * get a source form the Web (without cache).
	 * @param sourceProfile source profile of source to get
	 * @param ptCasGet 
	 * @return the source
	 * @throws XMLParseException 
	 */
	@SuppressWarnings("unchecked")
	private Source getFreshSource(final SourceProfile sourceProfile, 
			final String ptCasGet) throws XMLParseException {
		Source ret = new GlobalSource(sourceProfile);
		String sourceURL = null;
		try {
			String dtd = null;
			String root = null;
			String rootNamespace = null;
			String xmltype = null;
			String xml = null;

			//get the XML
			sourceURL = sourceProfile.getSourceURL();
			if (ptCasGet != null) {
				if (sourceURL.contains("?")) { 
					sourceURL = sourceURL + "&ticket=" + ptCasGet;
				} else {
					sourceURL = sourceURL + "?ticket=" + ptCasGet;
				}
			}
			SAXReader reader = new SAXReader();
			Document document;
			if (sourceURL.startsWith("http")) {
				HttpGet getRequest = new HttpGet(sourceURL);
				getRequest.setHeader("accept", "application/xml");
				HttpResponse response = httpClient.execute(getRequest);
				document = reader.read(response.getEntity().getContent());
			} else {
				document = reader.read(sourceURL);
			}
			//find the dtd
			DocumentType doctype = document.getDocType();
			if (doctype != null) {
				dtd = doctype.getSystemID();
			}
			//find root Element
			Element rootElement = document.getRootElement();
			root = rootElement.getName();
			//find xmlns on root element
			Namespace ns = rootElement.getNamespace();
			if (!ns.getURI().equals("")) {
				rootNamespace = ns.getURI();
			}
			//find XML Schema URL
			Namespace xmlSchemaNameSpace = 
				rootElement.getNamespaceForURI("http://www.w3.org/2001/XMLSchema-instance");
			if (xmlSchemaNameSpace != null) {
				String xmlSchemaNameSpacePrefix = xmlSchemaNameSpace.getPrefix();
				Iterator<Attribute> i = rootElement.attributeIterator();
				while (i.hasNext()) {
					Attribute attribute = i.next();
					if (attribute.getQName().getNamespacePrefix().equals(xmlSchemaNameSpacePrefix) 
							&& attribute.getName().equals("schemaLocation")) {
						xmltype = attribute.getValue();
					}
				}
			}
			//get XML as String
			xml = document.asXML();
			ret.setDtd(dtd);
			ret.setRootElement(root);
			ret.setXmlns(rootNamespace);
			ret.setXmlStream(xml);
			ret.setXmlType(xmltype);
		} catch (IOException e) {
			String profileId = "null";
			if (profile != null) {
				profileId = profile.getId();
			}
			String msg = "getFreshSource(" + profileId + "). Error on requesting url " + sourceURL + "'";
			LOG.error(msg, e);
			throw new XMLParseException(msg, e);
		} catch (DocumentException e) {
			String msg = "getSource with url=" 
				+ sourceProfile.getSourceURL() + ". Is it a valid XML Source ? ";
			LOG.error(msg + e.getMessage(), e);
			throw new XMLParseException(msg, e);
		}
		return ret;
	}

	/**
	 * @return exception thowed during run
	 */
	public Exception getException() {
		return exception;
	}

	/**
	 * @return Source genereted during run
	 */
	public Source getSource() {
		return source;
	}

}
