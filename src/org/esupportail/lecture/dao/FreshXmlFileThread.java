package org.esupportail.lecture.dao;

import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.esupportail.lecture.domain.model.Accessibility;
import org.esupportail.lecture.domain.model.ManagedCategory;
import org.esupportail.lecture.domain.model.ManagedCategoryProfile;
import org.esupportail.lecture.domain.model.ManagedSourceProfile;
import org.esupportail.lecture.domain.model.SourceProfile;
import org.esupportail.lecture.domain.model.VisibilitySets;
import org.esupportail.lecture.exceptions.dao.XMLParseException;

/**
 * Get a Freash config File from a distinct Thread.
 * @author vrepain
 */
public class FreshXmlFileThread extends Thread {
	/**
	 * Log instance.
	 */
	private static final Log LOG = LogFactory.getLog(FreshXmlFileThread.class);
	/**
	 * ManagedCategory to return by this Thread.
	 */
	private Document xmlFile;
	/**
	 * xmlFilePath used to return a xmlFile.
	 */
	private String xmlFilePath;
	/**
	 * Exception generated in this Thread.
	 */
	private Exception exception;
	
	/**
	 * Constructor.
	 * @param profile used to return a ManagedCategory
	 * @param ptCas - user and password. null for anonymous access
	 */
	public FreshXmlFileThread(final String xmlFilePath) {
		this.xmlFilePath = xmlFilePath;
	}

	/**
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		try {
			this.xmlFile = getFreshXmlFileThread(xmlFilePath);
		} catch (XMLParseException e) {
			this.exception = e;
		}
	}

	/**
	 * get a managed category from the web without cache.
	 * @param profile ManagedCategoryProfile of Managed category to get
	 * @param ptCas - user and password. null for anonymous access
	 * @return Managed category
	 * @throws XMLParseException 
	 */
	@SuppressWarnings("unchecked")
	private Document getFreshXmlFileThread(final String xmlFilePath)
		throws XMLParseException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("in getFreshConfigFileThread");
		}
		// TODO (RB <-- GB) gestion des attributs xml IMPLIED 
		Document document = null;
		try {
			SAXReader reader = new SAXReader();
			document = reader.read(xmlFilePath);
			
		} catch (DocumentException e) {
			String msg = "getFreshXmlFileThread(" + xmlFilePath + "). Can't read configuration file.";
			LOG.error(msg);
			throw new XMLParseException(msg , e);
		}
		return document;
	}

	/**
	 * @return exception throwed during run
	 */
	public Exception getException() {
		return exception;
	}

	/**
	 * @return
	 * @throws XMLParseException
	 */
	public Document getXmlFile() throws XMLParseException {
		return xmlFile;
	}

}
