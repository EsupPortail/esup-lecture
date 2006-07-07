package org.esupportail.lecture.domain.model;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



public class MappingFile {

/* ********************** PROPERTIES**************************************/ 
	/**
	 * The instance of the mapping file
	 */
	private static MappingFile singleton = null;
	
	protected static final Log log = LogFactory.getLog(MappingFile.class);
	
	/**
	 * The channel of the config
	 */
	private static Channel channel;
	
	private static XMLConfiguration mappingFile;
	
	/*path of the mapping file*/
	private static String mappingFilePath = "";
	private static long configFileLastModified;



	
/* ********************** ACCESSORS**************************************/ 
	/**
	 * Getter of the property <tt>mappingFilePath</tt>
	 * @return  Returns the mappingFilePath.
	 */
	public static String getMappingFilePath() {
		return mappingFilePath;
	}
	
	/**
	 * Setter of the property <tt>mappingFilePath</tt>
	 * @param mappingFilePath  The location to set.
	 */
	public static void setMappingFilePath(String url) {
		mappingFilePath = url;
	}
	
/* ********************** METHODS**************************************/ 	
	
	/**
	* Get an instance of the chanel configuration
	* @return an instance of the channel configuration
	* @throws Exception
	*/
	synchronized public static MappingFile getInstance(Channel c) throws Exception  {
		if (singleton == null) {
			singleton = new MappingFile(c);
		}else {
			// TODO : voir le chargt de fichier modifie + cf. new MappingFile()
//			URL url = MappingFile.class.getResource(configFilePath);
//			File configFile = new File(url.getFile());
//			
//			long newDate = configFile.lastModified();
//			if (configFileLastModified < newDate) {
//				log.debug("getInstance :: "+"Configuration reloaded");
//				configFileLastModified = newDate;
//				config = new MappingFile(c);
		}		
		return singleton;
	}

	/**
	 * Constructor
	 * @throws Exception
	 */
	private MappingFile(Channel c) throws Exception {
		this.channel = c;
		
		try {
			mappingFile = new XMLConfiguration();
			mappingFile.setFileName(mappingFilePath);
			mappingFile.setValidating(true);
			mappingFile.load();

			/* Loading Mappings */
			loadMappings();

		} catch (Exception e) {
			log.debug(e.getMessage());	
		}
	}	
	
	public void loadMappings() {
		int nbMappings = mappingFile.getMaxIndex("mapping") + 1;
		
		for(int i = 0; i<nbMappings;i++ ){
			String pathMapping = "mapping(" + i + ")";
			Mapping m = new Mapping();
			m.setDtd(mappingFile.getString(pathMapping+ "[@dtd]"));
			m.setXmlns(mappingFile.getString(pathMapping+ "[@xmlns]"));
			m.setXmlType(mappingFile.getString(pathMapping+ "[@xmlType]"));
			m.setXsltFile(mappingFile.getString(pathMapping+ "[@xsltFile]"));
			m.setItemXPath(mappingFile.getString(pathMapping+ "[@itemXPath]"));
			channel.setMapping(m);
		}
	}
	
	
	public String toString(){
		String string = "path : "+ mappingFilePath ;
		return string ;
	}
	
	

}
