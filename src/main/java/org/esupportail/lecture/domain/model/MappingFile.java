package org.esupportail.lecture.domain.model;
/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.esupportail.lecture.dao.FreshXmlFileThread;
import org.esupportail.lecture.exceptions.dao.XMLParseException;
import org.esupportail.lecture.exceptions.domain.MappingFileException;


/**
 * Mapping file : used for loading and parsing XML file where are declared mappings.
 * @see Mapping
 * @author gbouteil
 *
 */
// TODO (GB later) factoriser cette classe avec ChannelConfig (et les passer dans le DAO)
public class MappingFile {

	/*
	 *********************** PROPERTIES**************************************/ 
	/**
	 * Log instance. 
	 */
	protected static final Log LOG = LogFactory.getLog(MappingFile.class);
	
	/**
	 * Instance of this class.
	 */
	private static MappingFile singleton;
	
	/**
	 *  relative classpath of the file to load.
	 */
	private static String filePath;
	
	/**
	 *  Base path of the file to load.
	 */
	private static String fileBasePath;
	
	/**
	 * Indicates if file has been modified since the last getInstance() calling.
	 */
	private static boolean modified;
	
	/**
	 * List of loaded mappings for file to be set in channel.
	 */
	private static List<Mapping> mappingList;

	/**
	 * timeout of mapping file
	 */
	private static int xmlFileTimeOut;

	/**
	 * number of mappings found
	 */
	private static int nbMappings;
	
	/*
	 ************************** INIT *********************************/	
	
	/**
	 * Private Constructor: load xml mapping file. 
	 */
	private MappingFile() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("MappingFile()");
		}
	}	

	
	/*
	 *********************** METHODS**************************************/ 	
	

	/**
	 * Return a singleton of this class used to load mappings.
	 * @param configFilePath 
	 * @param defaultTimeOut 
	 * @return an instance of the file to load (singleton)
	 * @see MappingFile#singleton
	 */
	protected static MappingFile getInstance(final String configFilePath, final int defaultTimeOut) {
		filePath = configFilePath;
		xmlFileTimeOut = defaultTimeOut;
		return getInstance();
		
	}
	
	/**
	* Returns a singleton of this class used to load mappings.
	 * @return an instance of the mapping file (singleton) to load
	 * @see MappingFile#singleton
	 */
	protected static synchronized MappingFile getInstance()  {
		if (LOG.isDebugEnabled()) {
			LOG.debug("getInstance()");
		}
		
		if (singleton == null) {
			singleton = new MappingFile();
		}
		return singleton;
	}

	/**
	 * Load mappings in the channel.
	 * @param channel of the loading
	 */
	protected static synchronized void loadMappings(final Channel channel) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("loadMappings()");
		}
		
		channel.setMappingList(mappingList);
	}

	/**
	 * Initialize hash mappings in channel.
	 * @param channel of the initialization
	 */
	protected static synchronized void initChannelHashMappings(final Channel channel) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("initChannelHashMappings()");
		}
		for (Mapping m : channel.getMappingList()) {
			channel.addMapping(m);
			
		}
	}
	
	/**
	 * Returns the string containing path of the mapping file. 
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String string = "path : " + filePath;
		return string;
	}
	
	/*
	 *********************** ACCESSORS**************************************/ 
	
	
	/**
	 * Returns the classpath relative path of the mapping file.
	 * @return mappingFilePath
	 * @see MappingFile#filePath
	 */
	protected static String getMappingFilePath() {
		return filePath;
	}
	
	/**
	 * Set the classpath relative file path of the mapping file.
	 * @param mappingFilePath
	 * @see MappingFile#filePath
	 */
	protected static synchronized void setMappingFilePath(final String mappingFilePath) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("setMappingFilePath(" + mappingFilePath + ")");
		}
		MappingFile.filePath = mappingFilePath;
	}

	/**
	 * @return Returns the modified.
	 */
	protected static boolean isModified() {
		return modified;
	}


	/**
	 * @throws MappingFileException 
	 */
	protected static synchronized void getMappingFile() throws MappingFileException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("getMappingFile()");
		}
	
		if (filePath == null) {
			String errorMsg = "Mapping file path not defined, see in domain.xml file.";
			LOG.error(errorMsg);
			throw new MappingFileException(errorMsg);
		}
		
		URL url = ChannelConfig.class.getResource(filePath);
		if (url == null) {
			String errorMsg = "Mapping file: " + filePath + " not found.";
			LOG.error(errorMsg);
			throw new MappingFileException(errorMsg);
		}
		File file = new File(url.getFile());
		fileBasePath = file.getAbsolutePath();
		Document xmlFileLoading = null;
		
		xmlFileLoading = getFreshMappingFile(fileBasePath);
		
		if (xmlFileLoading != null) {
			xmlFileLoading = checkMappingFile(xmlFileLoading);
		}
		if (xmlFileLoading == null) {
			String errorMsg = "Impossible to load XML Channel config (" + fileBasePath + ")";
			LOG.error(errorMsg);
			throw new MappingFileException(errorMsg);
		}
	}


	/**
	 * @param configFilePath 
	 * @return Document
	 */
	@SuppressWarnings("finally")
	protected synchronized static Document getFreshMappingFile(String configFilePath) {
		// Assign null to configFileLoaded during the loading
		Document ret = null;
		// Launch thread
		FreshXmlFileThread thread = new FreshXmlFileThread(configFilePath);
		
		int timeout = 0;
		try {
			thread.start();
			timeout = xmlFileTimeOut;
			thread.join(timeout);
			Exception e = thread.getException();
			if (e != null) {
				String msg = "Thread getting Mapping launches XMLParseException";
				LOG.warn(msg);
				throw new XMLParseException(msg, e);
			}
	        if (thread.isAlive()) {
	    		thread.interrupt();
				String msg = "MappingFile not loaded in " + timeout + " milliseconds";
				LOG.warn(msg);
	        }	
			ret = thread.getXmlFile();
		} catch (InterruptedException e) {
			String msg = "Thread getting MappingFile interrupted";
			LOG.warn(msg);
			ret = null;
		} catch (IllegalThreadStateException e) {
			String msg = "Thread getting MappingFile launches IllegalThreadStateException";
			LOG.warn(msg);
			ret = null;
		} catch (XMLParseException e) {
			String msg = "Thread getting MappingFile launches XMLParseException";
			LOG.warn(msg);
			ret = null;
		} finally {
			return ret;
		}
	}


		/**
		 * Check syntax file that cannot be checked by DTD.
		 * @param xmlFileChecked 
		 * @return Document
		 * @throws MappingFileException 
		 */
		@SuppressWarnings("unchecked")
		private synchronized static Document checkMappingFile(Document xmlFileChecked) throws MappingFileException {
			if (LOG.isDebugEnabled()) {
				LOG.debug("checkXmlFile()");
			}
			// Merge categoryProfilesUrl and check number of contexts + categories
			Document xmlFileLoading = xmlFileChecked;
			Element mappingFile = xmlFileLoading.getRootElement();
			List<Element> mappings = mappingFile.selectNodes("mapping");
			nbMappings = mappings.size();
			if (nbMappings == 0) {
				LOG.warn("No mapping declared in mapping file (mappings.xml)");
			}
			
			mappingList = new ArrayList<Mapping>();
			for (Element mapping : mappings) {
				Mapping m = new Mapping();
				String sourceURL = mapping.valueOf("@sourceURL");
				String dtd = mapping.valueOf("@dtd");
				String xmlns = mapping.valueOf("@xmlns");
				String xmlType = mapping.valueOf("@xmlType");
				String rootElement = mapping.valueOf("@rootElement");
				
				if (sourceURL == null) {
					sourceURL = "";
				}
				if (dtd == null) {
					dtd = "";
				}
				if (xmlns == null) {
					xmlns = "";
				}
				if (xmlType == null) {
					xmlType = "";
				}
				if (rootElement == null) {
					rootElement = "";
				}
				if (sourceURL + dtd + xmlns + xmlType + rootElement == "") {
					String errorMsg = "In mappingFile, a mapping element " 
					+ "is empty, you must declare sourceURL or dtd or " 
					+ "xmlns or xmltype or rootElement in a mapping.";
					LOG.error(errorMsg);
					throw new MappingFileException(errorMsg);
				}

				m.setSourceURL(sourceURL);
				m.setDtd(dtd);
				m.setXmlns(xmlns);
				m.setXmlType(xmlType);
				m.setRootElement(rootElement);
				m.setXsltUrl(mapping.valueOf("@xsltFile"));
				m.setItemXPath(mapping.valueOf("@itemXPath"));
				String mobileXsltFile = mapping.valueOf("@mobileXsltFile");
				if (mobileXsltFile == null || mobileXsltFile.equals("")) {
					m.setMobileXsltUrl(m.getXsltUrl());
				}
				else {
					m.setMobileXsltUrl(mobileXsltFile);
				}
				//loop on XPathNameSpace
				List<Node> xPathNameSpaces = mapping.selectNodes("XPathNameSpace");
				HashMap<String, String> xPathNameSpacesHash = new HashMap<String, String>();
				for (Node xPathNameSpace : xPathNameSpaces) {
					String prefix = xPathNameSpace.valueOf("@prefix");
					String uri = xPathNameSpace.valueOf("@uri");
					xPathNameSpacesHash.put(prefix, uri);
				}
				m.setXPathNameSpaces(xPathNameSpacesHash);

				mappingList.add(m);

			}
			return xmlFileLoading;
			
		}
	
	
	
	

}
