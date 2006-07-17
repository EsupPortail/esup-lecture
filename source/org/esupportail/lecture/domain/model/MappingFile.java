package org.esupportail.lecture.domain.model;

import java.io.File;
import java.util.Iterator;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



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
	private static String mappingFilePath = "";
	
	/**
	 * Last modified time of the mapping file
	 */
	private static long fileLastModified;
	
	/**
	 * Instance of the mapping file
	 */
	// TODO à supprimer ?
	private static File file;
	
	

	
/* ********************** METHODS**************************************/ 	
	
	/**
	* Get an instance of the mapping file
	* @param c channel that the mapping file belongs to
	* @return an instance of the mapping file (singleton)
	* @throws Exception
	*/
	synchronized protected static MappingFile getInstance(Channel c) throws Exception  {
		if (singleton == null) {
			fileLastModified = file.lastModified();
//			 TODO remplacer par (+ supprimer file, 
			//fileLastModified = mappings.getFile().lastModified();
			
			singleton = new MappingFile(c);
		}else {
			log.debug("getInstance :: "+"singleton non null ");
			long newDate = file.lastModified();
			// TODO remplacer par
			// long newDate = mappings.getFile().lastModified();
			if (fileLastModified < newDate) {
				log.debug("getInstance :: "+"mapping file reloaded");
				fileLastModified = newDate;
				singleton = new MappingFile(c);
			}				
		}
		return singleton;
	}

	/**
	 * Private Constructor: load mapping file and initilized these elements in the channel
	 * @param c channel that the mapping file belongs to 
	 * @throws Exception
	 */
	private MappingFile(Channel c) throws Exception {
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

		} catch (Exception e) {
			log.debug(e.getMessage());	
		}
	}	
	
	/**
	 * Load mappings from mapping file
	 * @throws Exception
	 */
	private void loadMappings() throws Exception {
		int nbMappings = mappings.getMaxIndex("mapping") + 1;
		
		for(int i = 0; i<nbMappings;i++ ){
			String pathMapping = "mapping(" + i + ")";
			Mapping m = new Mapping();
			String dtd = mappings.getString(pathMapping+ "[@dtd]");
			String xmlns = mappings.getString(pathMapping+ "[@xmlns]");
			String xmlType = mappings.getString(pathMapping+ "[@xmlType]");
			
			if (dtd == null && xmlns == null && xmlType == null){
				throw new Exception("You must declare dtd or xmlns or xmltype in a mapping.");
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
		log.debug("Nouveau mappingFilePath : "+mappingFilePath);
		// TODO : a refaire !!!
		MappingFile.mappingFilePath = mappingFilePath;
		file = new File(mappingFilePath);
	}


	

}
