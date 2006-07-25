package org.esupportail.lecture.domain.model;
/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
import java.io.File;
import java.util.Iterator;

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

/* ********************** PROPERTIES**************************************/ 
	/**
	 * Instance of the mapping file
	 */
	private static MappingFile singleton = null;
	
	/**
	 * Log instance 
	 */
	protected static final Log log = LogFactory.getLog(MappingFile.class);
	
	/**
	 * The channel that the mapping file belongs to
	 */
	private static Channel channel;
	
	/**
	 * XML file loaded
	 */
	private static XMLConfiguration mappings;
	
	/**
	 *  classpath relative path of the mapping file
	 */
	private static String mappingFilePath = "mappings.xml";
	
	/**
	 * Last modified time of the mapping file
	 */
	private static long fileLastModified;
	
	/**
	 * Instance of the mapping file
	 */
	private static File file = new File(mappingFilePath);


	
/* ********************** METHODS**************************************/ 	
	
	/**
	* Get an instance of the mapping file
	* @param c channel that the mapping file belongs to
	* @return an instance of the mapping file (singleton)
	* @throws MyException
	*/
	synchronized protected static MappingFile getInstance(Channel c) throws MyException  {
		if (log.isDebugEnabled()){
			log.debug("getInstance()");
		}
		if (singleton == null) {
			fileLastModified = file.lastModified();
			singleton = new MappingFile(c);
		}else {
			if (log.isDebugEnabled()){
				log.debug("getInstance :: "+"singleton not null ");
			}
			long newDate = file.lastModified();
			if (fileLastModified < newDate) {
				if (log.isDebugEnabled()){
					log.debug("getInstance :: "+"mapping file reloaded");
				}
				fileLastModified = newDate;
				singleton = new MappingFile(c);
			}				
		}
		return singleton;
	}

	/**
	 * Private Constructor: load mapping file and initilized these elements in the channel
	 * @param c channel that the mapping file belongs to 
	 * @throws MyException
	 */
	private MappingFile(Channel c) throws MyException {
		if (log.isDebugEnabled()){
			log.debug("MappingFile()");
		}
		MappingFile.channel = c;
		
		try {
			mappings = new XMLConfiguration();
			mappings.setFileName(mappingFilePath);
			mappings.setValidating(true);
			mappings.load();

			/* Reset channel properties loaded from mapping file */
			c.resetMappingFileProperties();
			
			/* Loading Mappings */
			loadMappings();
			
			/* Initialize hashs mapping if channel */
			initChannelHashMappings();

		} catch (ConfigurationException e) {
			log.fatal("MappingFile :: ConfigurationException, "+e.getMessage());	
		} catch (MyException e){
			log.fatal("MappingFile :: MyException, "+e.getMessage());
		}
	}	
	
	/**
	 * Load mappings from mapping file
	 * @throws MyException
	 */
	private void loadMappings() throws MyException {
		if (log.isDebugEnabled()){
			log.debug("loadMappings()");
		}
		int nbMappings = mappings.getMaxIndex("mapping") + 1;
		
		for(int i = 0; i<nbMappings;i++ ){
			String pathMapping = "mapping(" + i + ")";
			Mapping m = new Mapping();
			String dtd = mappings.getString(pathMapping+ "[@dtd]");
			String xmlns = mappings.getString(pathMapping+ "[@xmlns]");
			String xmlType = mappings.getString(pathMapping+ "[@xmlType]");
			
			if (dtd == null && xmlns == null && xmlType == null){
				throw new MyException("loadMappings :: you must declare dtd or xmlns or xmltype in a mapping.");
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
				
			m.setXsltFile(mappings.getString(pathMapping+ "[@xsltFile]"));
			m.setItemXPath(mappings.getString(pathMapping+ "[@itemXPath]"));
			channel.addMapping(m);
		}
	}
		
	/**
	 *  Initialize hash mappings in channel
	 */
	private void initChannelHashMappings(){
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
	
/* ********************** ACCESSORS**************************************/ 
	/**
	 * Returns the channel that the mapping file belongs to
	 * @return channel
	 * @see MappingFile#channel
	 */
	protected static Channel getChannel() {
		return channel;
	}
	
	/**
	 * Set channel properties
	 * @param channel the channel to set
	 * @see MappingFile#channel
	 */
	public static void setChannel(Channel channel) {
		MappingFile.channel = channel;
	}	
	
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
		file = new File(mappingFilePath);
	}


	

}
