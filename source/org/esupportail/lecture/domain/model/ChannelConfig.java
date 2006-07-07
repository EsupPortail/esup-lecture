package org.esupportail.lecture.domain.model;


import java.io.File;
import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.HierarchicalConfiguration;

/**
 * 26/06/2006
 * Channel Config : class for loading and parsing chanel file config.
 * @author gbouteil
 *
 */
public class ChannelConfig {
	
/* ********************** PROPERTIES**************************************/ 

	/**
	 * The instance of the channel configuration
	 */
	private static ChannelConfig singleton = null;
	
	protected static final Log log = LogFactory.getLog(ChannelConfig.class);
	
	/**
	 * The channel of the config
	 */
	private static Channel channel;
	 
	private static XMLConfiguration config;
	/**
	 *  path of the config file
	 */
	private static String configFilePath ="properties/test.xml";
	
	/**
	 * time of the config file last modified
	 */
	private static long configFileLastModified;
		
	
/* ********************** ACCESSORS *****************************************/
	
	public void setChannel(Channel c){
		channel = c;
	}
	
	public Channel getChannel(){
		return channel;
	}
	

/* ********************** METHODS *****************************************/

	/**
	* Get an instance of the chanel configuration
	* @return an instance of the channel configuration
	* @throws Exception
	*/
	synchronized public static ChannelConfig getInstance(Channel c) throws Exception  {
		if (singleton == null) {
			singleton = new ChannelConfig(c);
		}else {
			// TODO : voir le chargt de fichier modifie + cf. new ChannelConfig()
//			//sync
			
//		URL url = ChannelConfig.class.getResource(configFilePath);
//			File configFile = new File(url.getFile());
//			
//			long newDate = configFile.lastModified();
//			if (configFileLastModified < newDate) {
//				log.debug("getInstance :: "+"Configuration reloaded");
//				configFileLastModified = newDate;
//				config = new ChannelConfig(c);
			//sync
		}		
		return singleton;
	}
			
	/**
	 * Constructor
	 * @throws Exception
	 */
	private ChannelConfig(Channel c) throws Exception {
		this.channel = c;
		
		try {
			config = new XMLConfiguration();
			config.setFileName(configFilePath);
			config.setValidating(true);
			config.load();
			
		/* Loading UrlMappingFile */
		loadUrlMappingFile();

		/* Loading managed category profiles */
		loadManagedCategoryProfiles();
		
		/* Loading Contexts */
		loadContexts();

		} catch (ConfigurationException e) {
			if (log.isDebugEnabled()){
				log.debug(e.getMessage());	
			}
		}
	}

	/**
	 * Load Mapping file location 
	 *
	 */
	private void loadUrlMappingFile() {
	   	MappingFile.setMappingFilePath(config.getString("[@mappingFile]"));
    }	

	private void loadManagedCategoryProfiles() throws Exception {
		int nbProfiles = config.getMaxIndex("categoryProfile") + 1;
		
		for(int i = 0; i<nbProfiles;i++ ){
			String pathCategoryProfile = "categoryProfile(" + i + ")";
			ManagedCategoryProfile mcp = new ManagedCategoryProfile();
			mcp.setId(config.getString(pathCategoryProfile+ "[@id]"));
			mcp.setName(config.getString(pathCategoryProfile+ "[@name]"));
			mcp.setUrlCategory(config.getString(pathCategoryProfile+ "[@urlCategory]"));
			mcp.setTrustCategory(config.getBoolean(pathCategoryProfile+ "[@trustCategory]"));
			mcp.setTtl(config.getInt(pathCategoryProfile+ "[@ttl]"));
			mcp.setDescription(config.getString(pathCategoryProfile+".description"));
			
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
		    mcp.setVisibility(visibilitySets);
		    
		    channel.setManagedCategoryProfile(mcp);    
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
	    
    private void loadContexts(){
		int nbContexts = config.getMaxIndex("context") + 1;
		
		for(int i = 0; i<nbContexts;i++ ){
			String pathContext = "context(" + i + ")";
			Context c = new Context();
			c.setId(config.getString(pathContext+ "[@id]"));
			c.setName(config.getString(pathContext+ "[@name]"));
			c.setDescription(config.getString(pathContext+".description"));
			List refIdList = config.getList(pathContext+ ".refCategoryProfile[@refId]");
			Iterator iterator = refIdList.iterator();
			for (String s = null; iterator.hasNext();){
				s = (String)iterator.next();
				c.setRefIdManagedCategoryProfile(s);
			}
			channel.setContext(c);
		}
		
    }
    
 
    
 
        
        
	
}
