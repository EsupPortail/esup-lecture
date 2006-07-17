package org.esupportail.lecture.domain.model;


import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
//import org.apache.commons.configuration.HierarchicalConfiguration; version 1.3

/**
 * Channel Config : used for loading and parsing XML channel file config.
 * @author gbouteil
 */
public class ChannelConfig {
	
/* ********************** PROPERTIES**************************************/ 

	/**
	 * Instance of the channel configuration
	 */
	private static ChannelConfig singleton = null;
	
	/**
	 * Log instance 
	 */
	protected static final Log log = LogFactory.getLog(ChannelConfig.class);
	
	/**
	 * The channel that the configuration file refers to 
	 */
	private static Channel channel;
	 
	/**
	 * XML file loaded
	 */
	private static XMLConfiguration config;
	
	/**
	 *  classpath relative path of the config file
	 */
	private static String configFilePath ="properties/test.xml";
	
	/**
	 * Last modified time of the config file
	 */
	private static long configFileLastModified;
	
	/**
	 * Instance of the configuration file
	 */
	// TODO à supprimer 
	private static File configFile;
		 

/* ********************** METHODS *****************************************/

	/**
	* Get an instance of the channel configuration
	* @param c channel that the config refers to
	* @return an instance of the channel configuration (singleton)
	* @see ChannelConfig#singleton
	* @throws Exception
	*/
	synchronized protected static ChannelConfig getInstance(Channel c) throws Exception  {
		if (singleton == null) {
			configFile = new File(configFilePath);
			configFileLastModified = configFile.lastModified();
//			 TODO remplacer par (+ supprimer configFile, 
			//configFileLastModified = config.getFile().lastModified();
			
			singleton = new ChannelConfig(c);
		}else {
			log.debug("getInstance :: "+"singleton non null ");
			long newDate = configFile.lastModified();
			// TODO remplacer par 
			// long newDate = config.getFile().lastModified();
			if (configFileLastModified < newDate) {
				log.debug("getInstance :: "+"Configuration reloaded");
				configFileLastModified = newDate;
				singleton = new ChannelConfig(c);
			}
		}		
		return singleton;
	}
			
	/**
	 * Private constructor : load config file and initilized these elements in the channel
	 * @param c channel that the config refers to
	 * @throws Exception
	 */
	private ChannelConfig(Channel c) throws Exception {
		ChannelConfig.channel = c;
		
		try {
			config = new XMLConfiguration();
			config.setFileName(configFilePath);
			config.setValidating(true);
			config.load();
			
		/* Reset channel properties loaded from config */
		c.resetChannelConfigProperties();
			
		/* Loading UrlMappingFile */
		loadUrlMappingFile();

		/* Loading managed category profiles */
		loadManagedCategoryProfiles();
		
		/* Loading Contexts */
		loadContexts();
		
		/* Initialize Contexts and ManagedCategoryProfiles links */
		initContextManagedCategoryProfilesLinks();

		} catch (ConfigurationException e) {
			if (log.isDebugEnabled()){
				log.debug(e.getMessage());	
			}
		}
	}

	/**
	 * Load Mapping file location from config file
	 *
	 */
	private void loadUrlMappingFile() {
		log.debug("Appel de setMappingFilePath");
	   	MappingFile.setMappingFilePath(config.getString("[@mappingFile]"));
    }	

	/**
	 * Load Managed Category profiles from config file
	 * @throws Exception
	 */
	private void loadManagedCategoryProfiles() throws Exception {
		int nbProfiles = config.getMaxIndex("categoryProfile") + 1;
		
		for(int i = 0; i<nbProfiles;i++ ){
			String pathCategoryProfile = "categoryProfile(" + i + ")";
			ManagedCategoryProfile mcp = new ManagedCategoryProfile();
			mcp.setId(config.getString(pathCategoryProfile+ "[@id]"));
			mcp.setName(config.getString(pathCategoryProfile+ "[@name]"));
			mcp.setUrlCategory(config.getString(pathCategoryProfile+ "[@urlCategory]"));
			//TODO : c.setEdit(...) le param edit plus tard
			mcp.setTrustCategory(config.getBoolean(pathCategoryProfile+ "[@trustCategory]"));
			mcp.setTtl(config.getInt(pathCategoryProfile+ "[@ttl]"));
			
		    // Accessibility
		    String access = config.getString(pathCategoryProfile+"[@access]");
		    if (access.equalsIgnoreCase("public")) {
				mcp.setAccess(Accessibility.PUBLIC);
			} else if (access.equalsIgnoreCase("cas")) {
				mcp.setAccess(Accessibility.CAS);
			} else {
				throw new Exception();
			}
			
		    // Visibility
		    VisibilitySets visibilitySets = new VisibilitySets();  
		    // foreach (allowed / autoSubscribed / Obliged
		    visibilitySets.setAllowed(loadDefAndContentSets("allowed",i));
		    visibilitySets.setAutoSubscribed(loadDefAndContentSets("autoSubscribed",i));
		   	visibilitySets.setObliged(loadDefAndContentSets("obliged",i));
		   	visibilitySets.checkNamesExistence();
		    mcp.setVisibility(visibilitySets);
		    
		    channel.addManagedCategoryProfile(mcp);    
		}
// Code pour la version commons-configuration 1.3
//		List configCategoryProfiles = config.configurationsAt("categoryProfiles");
//		for(Iterator it = configCategoryProfiles.iterator(); it.hasNext();) {
//		    HierarchicalConfiguration sub = (HierarchicalConfiguration) it.next();
//		    ManagedCategoryProfile mcp = new ManagedCategoryProfile();
//		    mcp.setId(sub.getInt("id"));
//		    mcp.setName(sub.getString("name"));
//		    mcp.setUrlCategory(sub.getString("urlCategory"));
//		    mcp.setTrustCategory(sub.getBoolean("trustCategory"));
//		    mcp.setTtl(sub.getInt("ttl"));
//		    
//		    // Accessibility
//		    String access = sub.getString("access");
//		    if (access == "PUBLIC") {
//				mcp.setAccess(Accessibility.PUBLIC);
//			} else if (access == "CAS"){
//				mcp.setAccess(Accessibility.CAS);
//			} else {
//				throw new Exception();
//			}
//		   // Visibility	
	}
	
	/**
	 * Load a DefAndContentSets that is used to define visibility groups of a managed category profile
	 * @param fatherName name of the father XML element refered to (which visibility group)
	 * @param index index of the XML element category profile
	 * @return the initialized DefAndContentSets
	 */
	private DefAndContentSets loadDefAndContentSets(String fatherName,int index){
		DefAndContentSets defAndContentSets = new DefAndContentSets();
		String fatherPath = "categoryProfile("+index+ ").visibility." + fatherName;
		
		// Definition by group enumeration
		int nbGroups = config.getMaxIndex(fatherPath + ".group") +1;
		for (int i=0;i<nbGroups;i++){
			defAndContentSets.addGroup(config.getString(fatherPath + ".group("+i+")[@name]"));
		}
   		// Definition by regular 
   		int nbRegulars = config.getMaxIndex(fatherPath + ".regular") +1;   	
   		for (int i=0;i<nbRegulars;i++){
   			RegularOfSet regular = new RegularOfSet();
   			regular.setAttribute(config.getString(fatherPath + ".regular("+i+")[@attribute]"));
   			regular.setValue(config.getString(fatherPath + ".regular("+i+")[@value]"));	
   			defAndContentSets.addRegular(regular);
   		}
   		return defAndContentSets;
	}
	
	/**
	 * Load Contexts from config file
	 */
    private void loadContexts(){
		int nbContexts = config.getMaxIndex("context") + 1;
		
		for(int i = 0; i<nbContexts;i++ ){
			String pathContext = "context(" + i + ")";
			Context c = new Context();
			c.setId(config.getString(pathContext+ "[@id]"));
			c.setName(config.getString(pathContext+ "[@name]"));
			c.setDescription(config.getString(pathContext+".description"));
			//TODO : c.setEdit(...) le param edit plus tard
			List refIdList = config.getList(pathContext+ ".refCategoryProfile[@refId]");
			Iterator iterator = refIdList.iterator();
			for (String s = null; iterator.hasNext();){
				s = (String)iterator.next();
				c.addRefIdManagedCategoryProfile(s);
			}
			channel.addContext(c);
		}
    }    
    
    /**
     * Initialises associations between contexts and managed category profiles 
     * defined in the channel config
     */
    private void initContextManagedCategoryProfilesLinks(){
    	
    	Iterator iterator = channel.getContexts().iterator();
    	while(iterator.hasNext()){
    		Context c = (Context)iterator.next();
    		c.initManagedCategoryProfiles(channel);
    	}
	}
/* ********************** ACCESSORS *****************************************/
	
	/**
	 * Set channel properties
	 * @param c the channel to set
	 * @see ChannelConfig#channel
	 */
	protected void setChannel(Channel c){
		channel = c;
	}
	 
	/**
	 * Returns the channel that the config refers to
	 * @return channel
	 * @see ChannelConfig#channel
	 */
	protected Channel getChannel(){
		return channel;
	}
	
	/**
	 * Returns the classpath relative path of the channel config
	 * @return configFilePath
	 * @see ChannelConfig#configFilePath
	 */
	protected static String getConfigFilePath() {
		return configFilePath;
	}

	/**
	 * Set the classpath relative file path of the channel config
	 * @param configFilePath
	 * @see ChannelConfig#configFilePath
	 */
	protected static void setConfigFilePath(String configFilePath) {
		ChannelConfig.configFilePath = configFilePath;
		// TODO refaire un nouveau fichier ??? cf MappingFile
	}
   

}
