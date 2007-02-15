package org.esupportail.lecture.dao;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentType;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.esupportail.lecture.domain.model.Accessibility;
import org.esupportail.lecture.domain.model.GlobalSource;
import org.esupportail.lecture.domain.model.ManagedCategory;
import org.esupportail.lecture.domain.model.ManagedCategoryProfile;
import org.esupportail.lecture.domain.model.ManagedSourceProfile;
import org.esupportail.lecture.domain.model.Source;
import org.esupportail.lecture.domain.model.SourceProfile;
import org.esupportail.lecture.domain.model.VisibilitySets;
import org.esupportail.lecture.exceptions.dao.XMLParseException;

/**
 * Get a Freash Managed Category from a distinct Thread
 * @author bourges
 */
public class FreshSourceThread extends Thread {

	/**
	 * Log instance 
	 */
	private static final Log log = LogFactory.getLog(FreshSourceThread.class);
	private Exception exception;
	private Source source;
	private SourceProfile sourceProfile;

	public FreshSourceThread(SourceProfile sourceProfile) {
		this.sourceProfile = sourceProfile;
		this.exception = null;
	}

	/**
	 * @throws XMLParseException 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		try {
			this.source = getFreshSource(sourceProfile);
		} catch (XMLParseException e) {
			this.exception = e;
		}
	}

	/**
	 * get a source form the Web (without cache)
	 * @param sourceProfile source profile of source to get
	 * @return the source
	 * @throws XMLParseException 
	 */
	private synchronized Source getFreshSource(SourceProfile sourceProfile) throws XMLParseException {
		Source ret = new GlobalSource();
		try {
			String dtd = null;
			String root = null;
			String rootNamespace = null;
			String xmltype = null;
			String xml = null;

			//get the XML
			SAXReader reader = new SAXReader();
			String sourceURL = sourceProfile.getSourceURL();
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
			Namespace xmlSchemaNameSpace = rootElement.getNamespaceForURI("http://www.w3.org/2001/XMLSchema-instance");
			if (xmlSchemaNameSpace != null) {
				String xmlSchemaNameSpacePrefix = xmlSchemaNameSpace.getPrefix();
				for ( Iterator i = rootElement.attributeIterator(); i.hasNext(); ) {
					Attribute attribute = (Attribute) i.next();
					if (attribute.getQName().getNamespacePrefix().equals(xmlSchemaNameSpacePrefix) && attribute.getName().equals("schemaLocation")) {
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
			ret.setURL(sourceURL);
			//			ret.setItemXPath(sourceProfile.getItemXPath());
			//			ret.setXsltURL(sourceProfile.getXsltURL());
		} catch (DocumentException e) {
			String msg = "getSource with url="+sourceProfile.getSourceURL()+". Is it a valid XML Source ?";
			log.error(msg);
			throw new XMLParseException(msg ,e);
		}
		return ret;
	}

	public Exception getException() {
		return exception;
	}

	public Source getSource() {
		return source;
	}

}
