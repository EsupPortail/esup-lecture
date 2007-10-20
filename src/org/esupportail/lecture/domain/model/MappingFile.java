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
import java.util.Iterator;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.exceptions.domain.ChannelConfigException;
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
	 * Log instance 
	 */
	protected static final Log log = LogFactory.getLog(MappingFile.class);
	
	/**
	 * Instance of this class
	 */
	private static MappingFile singleton = null;
	
	/**
	 * XML file loaded
	 */
	private static XMLConfiguration xmlFile;
	
	/**
	 *  relative classpath of the file to load
	 */
	private static String filePath;
	
	/**
	 *  Base path of the file to load
	 */
	private static String fileBasePath;
	
	/**
	 * Last modified time of the file to load
	 */
	private static long fileLastModified;

	/**
	 * Indicates if file has been modified since the last getInstance() calling
	 */
	private static boolean modified = false;
	
	/**
	 * List of loaded mappings for file to be set in channel
	 */
	private static List<Mapping> mappingList;
	
	/*
	 ************************** INIT *********************************/	
	
	/**
	 * Private Constructor: load xml mapping file 
	 * @throws MappingFileException
	 */
	private MappingFile() throws MappingFileException {
		if (log.isDebugEnabled()){
			log.debug("MappingFile()");
		}
		
		try {
			xmlFile = new XMLConfiguration();
			xmlFile.setFileName(fileBasePath);
			xmlFile.setValidating(true);
			xmlFile.load();
			checkXmlFile();
			modified = true;

		} catch (ConfigurationException e) {
			String errorMsg = "Impossible to load XML Mapping file (mappings.xml)";
			log.error(errorMsg);
			throw new MappingFileException(errorMsg,e);	
		} 
	}	

	
	/*
	 *********************** METHODS**************************************/ 	
	

	/**
	 * Return a singleton of this class used to load mappings.
	 * @param mappingFilePath file path of the mapping file
	 * @return an instance of the file to load (singleton)
	 * @throws MappingFileException 
	 * @see MappingFile#singleton
	 */
	protected static synchronized MappingFile getInstance(String mappingFilePath) throws MappingFileException {
		filePath = mappingFilePath;
		return getInstance();
		
	}
	
	/**
	* Returns a singleton of this class used to load mappings
	 * @return an instance of the mapping file (singleton) to load
	 * @throws MappingFileException 
	 * @see MappingFile#singleton
	 */
	synchronized protected static MappingFile getInstance() throws MappingFileException  {
		if (log.isDebugEnabled()){
			log.debug("getInstance()");
		}
		
		if (filePath == null){
			String errorMsg = "Mapping file path not defined, see in domain.xml file.";
			log.error(errorMsg);
			throw new MappingFileException(errorMsg);
		}
		if (singleton == null) {
			URL url = MappingFile.class.getResource(filePath);
			if (url==null) {
				String ErrorMsg = "Mapping config file: "+filePath+" not found.";
				log.error(ErrorMsg);
				throw new MappingFileException(ErrorMsg);
			}
			File mappingFile = new File(url.getFile());
			fileBasePath = mappingFile.getAbsolutePath();
			fileLastModified = mappingFile.lastModified();		
			singleton = new MappingFile();
	
		}else {
			if (log.isDebugEnabled()){
				log.debug("getInstance :: "+"singleton not null ");
			}
			
			File mappingFile = new File(fileBasePath);
			long newDate = mappingFile.lastModified();
			if (fileLastModified < newDate) {
				if (log.isDebugEnabled()){
					log.debug("getInstance :: "+"Mappings reloading");
				}
				fileLastModified = newDate;
				singleton = new MappingFile();
			} else {
				log.debug("getInstance :: mappings not reloaded");
			}
		}
		return singleton;
	}



	/**
	 * Check syntax file that cannot be checked by DTD
	 * @throws MappingFileException 
	 */
	synchronized private static void checkXmlFile() throws MappingFileException {
		if (log.isDebugEnabled()){
			log.debug("checkXmlFile()");
		}
	
		int nbMappings = xmlFile.getMaxIndex("mapping") + 1;
		mappingList = new ArrayList<Mapping>();
		
		for(int i = 0; i<nbMappings;i++ ){
			String pathMapping = "mapping(" + i + ")";
			Mapping m = new Mapping();
			String sourceURL = xmlFile.getString(pathMapping+ "[@sourceURL]");
			String dtd = xmlFile.getString(pathMapping+ "[@dtd]");
			String xmlns = xmlFile.getString(pathMapping+ "[@xmlns]");
			String xmlType = xmlFile.getString(pathMapping+ "[@xmlType]");
			String rootElement = xmlFile.getString(pathMapping+ "[@rootElement]");
			
			if (sourceURL == null && dtd == null && xmlns == null && xmlType == null && rootElement == null){
				String errorMsg = "In mappingFile, mapping n°"+i+"is empty, you must declare sourceURL or dtd or xmlns or xmltype or rootElement in a mapping.";
				log.error(errorMsg);
				throw new MappingFileException(errorMsg);
			}
			
			if (sourceURL == null){
				m.setSourceURL("");
			}else{
				m.setSourceURL(sourceURL);
			}
			
			if (dtd == null){
				m.setDtd("");
			}else{
				m.setDtd(dtd);
			}
			
			if (xmlns == null){
				m.setXmlns("");
			}else{
				m.setXmlns(xmlns);
			}
			
			if (xmlType == null){
				m.setXmlType("");
			}else{
				m.setXmlType(xmlType);
			}	
				
			if (rootElement == null){
				m.setRootElement("");
			}else{
				m.setRootElement(rootElement);
			}
			
			m.setXsltUrl(xmlFile.getString(pathMapping+ "[@xsltFile]"));
			m.setItemXPath(xmlFile.getString(pathMapping+ "[@itemXPath]"));
			
			//loop on XPathNameSpace
			int nbXPathNameSpaces = xmlFile.getMaxIndex(pathMapping+ ".XPathNameSpace") + 1;
			HashMap<String, String> XPathNameSpaces = new HashMap<String, String>();
			for(int j = 0; j < nbXPathNameSpaces; j++) {
				String pathXPathNameSpace = pathMapping+ ".XPathNameSpace("+j+")";
				String prefix = xmlFile.getString(pathXPathNameSpace+"[@prefix]");
				String uri = xmlFile.getString(pathXPathNameSpace+"[@uri]");
				XPathNameSpaces.put(prefix, uri);
			}
			m.setXPathNameSpaces(XPathNameSpaces);

			mappingList.add(m);
		}
	}
	
	/**
	 * Load mappings in the channel 
	 * @param channel of the loading
	 */
	synchronized protected static void loadMappings(Channel channel) {
		if (log.isDebugEnabled()){
			log.debug("loadMappings()");
		}
		
		channel.setMappingList(mappingList);
	}

	/**
	 * Initialize hash mappings in channel
	 * @param channel of the initialization
	 */
	synchronized protected static void initChannelHashMappings(Channel channel){
		if (log.isDebugEnabled()){
			log.debug("initChannelHashMappings()");
		}
		Iterator<Mapping> iterator = channel.getMappingList().iterator();
		for(Mapping m = null; iterator.hasNext();){
			m = iterator.next();
			channel.addMapping(m);
			
//			String sourceURL = m.getSourceURL();
//			String dtd = m.getDtd();
//			String xmlns = m.getXmlns();
//			String xmlType = m.getXmlType();
//			String rootElement = m.getRootElement();
//			
//			if (!sourceURL.equals("")) {
//				channel.addMappingBySourceURL(m);
//			}
//			if (!dtd.equals("")) {
//				channel.addMappingByDtd(m);
//			}
//			if (!xmlns.equals("")) {
//				channel.addMappingByXmlns(m);
//			}
//			if (!xmlType.equals("")) {
//				channel.addMappingByXmlType(m);
//			}
//			if (!rootElement.equals("")) {
//				channel.addMappingByRootElement(m);
//			}
		}
	}
	
	/**
	 * Returns the string containing path of the mapping file 
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString(){
		String string = "path : "+ filePath ;
		return string ;
	}
	
	/*
	 *********************** ACCESSORS**************************************/ 
	
	
	/**
	 * Returns the classpath relative path of the mapping file
	 * @return mappingFilePath
	 * @see MappingFile#filePath
	 */
	protected static String getMappingFilePath() {
		return filePath;
	}
	
	/**
	 * Set the classpath relative file path of the mapping file
	 * @param mappingFilePath
	 * @see MappingFile#filePath
	 */
	synchronized protected static void setMappingFilePath(String mappingFilePath) {
		if(log.isDebugEnabled()){
			log.debug("setMappingFilePath("+mappingFilePath+")");
		}
		MappingFile.filePath = mappingFilePath;
	}

	/**
	 * @return Returns the modified.
	 */
	protected static boolean isModified() {
		return modified;
	}

//	/**
//	 * @param modified The modified to set.
//	 */
//	synchronized protected static void setModified(boolean modified) {
//		MappingFile.modified = modified;
//	}

}
