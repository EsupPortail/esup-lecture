package org.esupportail.lecture.dao;

import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

	/**
	 * Constructor.
	 * @param sourceProfile used to return a Source
	 * @param creds - user and password. null for anonymous access
	 */
	public FreshSourceThread(final SourceProfile sourceProfile, final String ptCas) {
		this.profile = sourceProfile;
		this.ptCas = ptCas;
		this.exception = null;
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
	 * @param creds - user and password. null for anonymous access
	 * @return the source
	 * @throws XMLParseException 
	 */
	@SuppressWarnings("unchecked")
	private synchronized Source getFreshSource(final SourceProfile sourceProfile, 
			final String ptCas) throws XMLParseException {
		Source ret = new GlobalSource(sourceProfile);
		try {
			String dtd = null;
			String root = null;
			String rootNamespace = null;
			String xmltype = null;
			String xml = null;

			//get the XML
			String sourceURL = sourceProfile.getSourceURL();
			if (ptCas != null) {
				if (sourceURL.contains("?")) { 
					sourceURL = sourceURL + "&ticket=" + ptCas;
				} else {
					sourceURL = sourceURL + "?ticket=" + ptCas;
				}
			}
			SAXReader reader = new SAXReader();
			Document document = reader.read(sourceURL);
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
		} catch (DocumentException e) {
			String msg = "getSource with url=" 
				+ sourceProfile.getSourceURL() + ". Is it a valid XML Source ?";
			LOG.error(msg);
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
