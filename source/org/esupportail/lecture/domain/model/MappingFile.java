package org.esupportail.lecture.domain.model;
/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.utils.exception.*;


/**
 * Mapping file : used for loading and parsing XML mapping file.
 * @author gbouteil
 *
 */
public class MappingFile {

	/*
	 *********************** PROPERTIES**************************************/ 
	/**
	 * Instance of the mapping file
	 */
	private static MappingFile singleton = null;
	
	/**
	 * Log instance 
	 */
	protected static final Log log = LogFactory.getLog(MappingFile.class);
	
	/**
	 * XML file loaded
	 */
	private static XMLConfiguration xmlFile;
	
	/**
	 *  classpath relative path of the mapping file
	 */
	private static String mappingFilePath = "/mappings.xml";
	
	/**
	 *  Base path of the mapping file
	 */
	private static String mappingFileBasePath;
	
	/**
	 * Last modified time of the mapping file
	 */
	private static long mappingFileLastModified;

	/**
	 * Indicates if mapping file has been modified since the last loading
	 */
	private static boolean modified = false;
	
	/**
	 * List of loaded mappings for file to be set in channel
	 */
	private static List<Mapping> mappingList;
	

	
	/*
	 *********************** METHODS**************************************/ 	
	

	/**
	* Get an instance of the mapping file
	 * @return an instance of the mapping file (singleton)
	 * @throws ErrorException
	 * @throws WarningException
	 * @see MappingFile#singleton
	 */
	synchronized protected static MappingFile getInstance() throws ErrorException,WarningException  {
		if (log.isDebugEnabled()){
			log.debug("getInstance()");
		}
		if (singleton == null) {
			URL url = MappingFile.class.getResource(mappingFilePath);
			File mappingFile = new File(url.getFile());
			mappingFileBasePath = mappingFile.getAbsolutePath();
			mappingFileLastModified = mappingFile.lastModified();		
			singleton = new MappingFile();
	
		}else {
			if (log.isDebugEnabled()){
				log.debug("getInstance :: "+"singleton not null ");
			}
			
			File mappingFile = new File(mappingFileBasePath);
			long newDate = mappingFile.lastModified();
			if (mappingFileLastModified < newDate) {
				if (log.isDebugEnabled()){
					log.debug("getInstance :: "+"Mappings reloading");
				}
				mappingFileLastModified = newDate;
				singleton = new MappingFile();
			} else {
				log.debug("getInstance :: mappings not reloaded");
			}
		}
		return singleton;
	}


	/**
	 * Private Constructor: load xml mapping file 
	 * @throws ErrorException
	 * @throws WarningException
	 */
	protected MappingFile() throws ErrorException,WarningException {
		if (log.isDebugEnabled()){
			log.debug("MappingFile()");
		}
		
		try {
			xmlFile = new XMLConfiguration();
			xmlFile.setFileName(mappingFileBasePath);
			xmlFile.setValidating(true);
			xmlFile.load();
			checkXmlFile();
			modified = true;


		} catch (ConfigurationException e) {
			log.error("MappingFile :: ConfigurationException, "+e.getMessage());
			throw new ErrorException(e.getMessage());	
		} 
	}	

	/**
	 * Check syntax file that cannot be checked by DTD
	 * @throws ErrorException
	 * @throws WarningException
	 */
	private static void checkXmlFile() throws ErrorException,WarningException{
	
		int nbMappings = xmlFile.getMaxIndex("mapping") + 1;
		mappingList = new ArrayList<Mapping>();
		
		for(int i = 0; i<nbMappings;i++ ){
			String pathMapping = "mapping(" + i + ")";
			Mapping m = new Mapping();
			String dtd = xmlFile.getString(pathMapping+ "[@dtd]");
			String xmlns = xmlFile.getString(pathMapping+ "[@xmlns]");
			String xmlType = xmlFile.getString(pathMapping+ "[@xmlType]");
			
			if (dtd == null && xmlns == null && xmlType == null){
				throw new ErrorException("loadMappings :: you must declare dtd or xmlns or xmltype in a mapping.");
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
				
			m.setXsltFile(xmlFile.getString(pathMapping+ "[@xsltFile]"));
			m.setItemXPath(xmlFile.getString(pathMapping+ "[@itemXPath]"));
			mappingList.add(m);
		}
	}
	
	/**
	 * Load mappings in the channel 
	 * @param channel of the loading
	 */
	protected static void loadMappings(Channel channel) {
		if (log.isDebugEnabled()){
			log.debug("loadMappings()");
		}
		
		channel.setMappingList(mappingList);
	}

	/**
	 * Initialize hash mappings in channel
	 * @param channel of the initialization
	 */
	protected static void initChannelHashMappings(Channel channel){
		if (log.isDebugEnabled()){
			log.debug("initChannelHashMappings()");
		}
		Iterator iterator = channel.getMappingList().iterator();
		for(Mapping m = null; iterator.hasNext();){
			m = (Mapping)iterator.next();
			String dtd = m.getDtd();
			String xmlns = m.getXmlns();
			String xmlType = m.getXmlType();
			
			if (dtd != "") {
				channel.addMappingByDtd(m);
			}
			if (xmlns != "") {
				channel.addMappingByXmlns(m);
			}
			if (xmlType != "") {
				channel.addMappingByXmlType(m);
			}
		}
	}
	
	/**
	 * Returns the string containing path of the mapping file 
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		String string = "path : "+ mappingFilePath ;
		return string ;
	}
	
	/*
	 *********************** ACCESSORS**************************************/ 
	
	
	/**
	 * Returns the classpath relative path of the mapping file
	 * @return mappingFilePath
	 * @see MappingFile#mappingFilePath
	 */
	protected static String getMappingFilePath() {
		return mappingFilePath;
	}
	
	/**
	 * Set the classpath relative file path of the mapping file
	 * @param mappingFilePath
	 * @see MappingFile#mappingFilePath
	 */
	protected static void setMappingFilePath(String mappingFilePath) {
		log.debug("setMappingFilePath("+mappingFilePath+")");
		MappingFile.mappingFilePath = mappingFilePath;
	}

	/**
	 * @return Returns the modified.
	 */
	protected static boolean isModified() {
		return modified;
	}

	/**
	 * @param modified The modified to set.
	 */
	protected static void setModified(boolean modified) {
		MappingFile.modified = modified;
	}


	

}
