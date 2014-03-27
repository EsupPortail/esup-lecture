package org.esupportail.lecture.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
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
	 * @param xmlFilePath 
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
	 * @param xmlFilePathGet 
	 * @return Managed category
	 * @throws XMLParseException 
	 */
	private Document getFreshXmlFileThread(final String xmlFilePathGet)
		throws XMLParseException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("in getFreshConfigFileThread");
		}
		// TODO (RB <-- GB) gestion des attributs xml IMPLIED 
		Document document = null;
		try {
			SAXReader reader = new SAXReader();
			document = reader.read(xmlFilePathGet);
			
		} catch (DocumentException e) {
			String msg = "getFreshXmlFileThread(" + xmlFilePathGet + "). Can't read configuration file.";
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
	 * @return xmlFile
	 */
	public Document getXmlFile() {
		return xmlFile;
	}

}
