/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;

import java.io.File;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.esupportail.lecture.utils.exception.*;
//import org.apache.commons.configuration.HierarchicalConfiguration; version 1.3

/**
 * Channel Config : used for loading and parsing XML channel file config.
 * @author gbouteil
 */
public class ChannelConfig  {
/* ********************** PROPERTIES**************************************/ 
	/**
	 * Log instance 
	 */
	protected static final Log log = LogFactory.getLog(ChannelConfig.class);
	
	/**
	 * Instance of the loaded Channel Config
	 */
	private static ChannelConfig singleton = null;
	
	/**
	 * XML file loaded
	 */
	protected static XMLConfiguration xmlFile;
	
	/**
	 *  classpath relative path of the file to load
	 */
	private static String filePath ="/lecture-config.xml";
	
	/**
	 *  Base path of the file  to load
	 */
	private static String fileBasePath;
	
	/**
	 * Last modified time of the file to load
	 */
	private static long fileLastModified;
	
	/**
	 * Indicates if file has been modified since last loading
	 */
	private static boolean modified = false;
	
	/**
	 * Numbers of category profiles declared in the xml file
	 */
	private static int nbProfiles = 0;
	
	/**
	 * Numbers of contexts declared in the xml file
	 */
	private static int nbContexts = 0;
	

/* ********************** METHODS *****************************************/

	/**
	 * Get an instance of the file to load
	 * @return an instance of the file to load (singleton)
	 * @throws ErrorException
	 * @throws WarningException
	 * @see ChannelConfig#singleton
	 */
	synchronized protected static ChannelConfig getInstance()throws ErrorException,WarningException {
		if (log.isDebugEnabled()){
			log.debug("getInstance()");
		}
		modified = false;
		
		if (singleton == null) {
			URL url = ChannelConfig.class.getResource(filePath);
			File file = new File(url.getFile());
			fileBasePath = file.getAbsolutePath();
			fileLastModified = file.lastModified();
			singleton = new ChannelConfig();

		}else {
			if (log.isDebugEnabled()){
				log.debug("getInstance :: "+" singleton not null ");
			}
			
			File file = new File(fileBasePath);
			long newDate = file.lastModified();
			if (fileLastModified < newDate) {
				if (log.isDebugEnabled()){
					log.debug("getInstance :: "+"Channel config loading");
				}
				
				fileLastModified = newDate;
				singleton = new ChannelConfig();
			} else {
				log.debug("getInstance :: configuration not reloaded");
			}
		}		
		return singleton;
	}
	
	/**
	 * Private Constructor: load xml config file
	 * @throws ErrorException
	 * @throws WarningException
	 */
	private ChannelConfig() throws ErrorException,WarningException {
		if (log.isDebugEnabled()){
			log.debug("ChannelConfig()");
		}
		
		try {
			xmlFile = new XMLConfiguration();
			xmlFile.setFileName(fileBasePath);
			xmlFile.setValidating(true);
			xmlFile.load();
			checkXmlFile();
			modified = true;

		} catch (ConfigurationException e) {
			log.error("ChannelConfig :: ConfigurationException, "+e.getMessage());
			throw new ErrorException();
		} 
	}
	
	/**
	 * Check syntax file that cannot be checked by DTD
	 * @throws ErrorException
	 * @throws WarningException
	 */
	private void checkXmlFile() throws ErrorException,WarningException{
		
		nbProfiles = xmlFile.getMaxIndex("categoryProfile") + 1;
		if (nbProfiles == 0) {
			log.warn("checkXmlConfig :: No managed category profile declared in channel config");
		}
	
		// TODO groupes visibility :
		// - vérifier l'existances des noms de groupes et d'attributs dans le portletContext (portlet.xml) : WARNING
		// - vérifier l'existance d'au moins un groupe de visibilité (au moins un des trois et non vide) : WARNING	
		// => throw new WarningException("...");
		if (false){
			throw new WarningException("...");
		}
		
		nbContexts = xmlFile.getMaxIndex("context") + 1;
		
		if (nbContexts == 0) {
			log.warn("checkXmlConfig :: No context declared in channel config");
		}
		
	}
	

	/**
	 * Load attribute name provided by portlet request 
	 * to identified user profiles (userId)
	 */
	public static void loadUserId() {
		if (log.isDebugEnabled()){
			log.debug("loadUserId()");
		}
		UserAttributes.setUSER_ID(xmlFile.getString("userId"));
	}

	
	

	/**
	 * Load Managed Category profiles from config file in the channel
	 * @param channel of the loading
	 */
	protected static void loadManagedCategoryProfiles(Channel channel)  {
		if (log.isDebugEnabled()){
			log.debug("loadManagedCategoryProfiles()");
		}
		
		for(int i = 0; i<nbProfiles;i++ ){
			String pathCategoryProfile = "categoryProfile(" + i + ")";
			ManagedCategoryProfile mcp = new ManagedCategoryProfile();
			mcp.setId(xmlFile.getString(pathCategoryProfile+ "[@id]"));
			mcp.setName(xmlFile.getString(pathCategoryProfile+ "[@name]"));
			mcp.setUrlCategory(xmlFile.getString(pathCategoryProfile+ "[@urlCategory]"));
			//TODO : c.setEdit(...) le param edit plus tard
			mcp.setTrustCategory(xmlFile.getBoolean(pathCategoryProfile+ "[@trustCategory]"));
			mcp.setTtl(xmlFile.getInt(pathCategoryProfile+ "[@ttl]"));
			
		    // Accessibility
		    String access = xmlFile.getString(pathCategoryProfile+"[@access]");
		    if (access.equalsIgnoreCase("public")) {
				mcp.setAccess(Accessibility.PUBLIC);
			} else if (access.equalsIgnoreCase("cas")) {
				mcp.setAccess(Accessibility.CAS);
			}
			
		    // Visibility
		    VisibilitySets visibilitySets = new VisibilitySets();  
		    // foreach (allowed / autoSubscribed / Obliged
		    visibilitySets.setAllowed(loadDefAndContentSets("allowed",i));
		    visibilitySets.setAutoSubscribed(loadDefAndContentSets("autoSubscribed",i));
		   	visibilitySets.setObliged(loadDefAndContentSets("obliged",i));
		   	//TODO : visibilitySets.checkNamesExistence(); => devient warning, pas de groupe défini ... !
		    mcp.setVisibility(visibilitySets);
		    // TODO forcer_timer_categorie(mcp)
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
//				throw new MyException();
//			}
//		   // Visibility	
	}
	
	/**
	 * Load a DefAndContentSets that is used to define visibility groups of a managed category profile
	 * @param fatherName name of the father XML element refered to (which visibility group)
	 * @param index index of the XML element category profile
	 * @return the initialized DefAndContentSets
	 */
	private static DefAndContentSets loadDefAndContentSets(String fatherName,int index){
		if (log.isDebugEnabled()){
			log.debug("loadDefAndContentSets("+fatherName+","+index+")");
		}
		DefAndContentSets defAndContentSets = new DefAndContentSets();
		String fatherPath = "categoryProfile("+index+ ").visibility." + fatherName;
		
		// Definition by group enumeration
		int nbGroups = xmlFile.getMaxIndex(fatherPath + ".group") +1;
		for (int i=0;i<nbGroups;i++){
			defAndContentSets.addGroup(xmlFile.getString(fatherPath + ".group("+i+")[@name]"));
		}
   		// Definition by regular 
   		int nbRegulars = xmlFile.getMaxIndex(fatherPath + ".regular") +1;   	
   		for (int i=0;i<nbRegulars;i++){
   			RegularOfSet regular = new RegularOfSet();
   			regular.setAttribute(xmlFile.getString(fatherPath + ".regular("+i+")[@attribute]"));
   			regular.setValue(xmlFile.getString(fatherPath + ".regular("+i+")[@value]"));	
   			defAndContentSets.addRegular(regular);
   		}
   		return defAndContentSets;
	}
	
	
	/**
	 * Load Contexts from config file in the channel
	 * @param channel of the loading
	 */
	protected static void loadContexts(Channel channel){
    	if (log.isDebugEnabled()){
    		log.debug("loadContexts()");
    	}

		for(int i = 0; i<nbContexts;i++ ){
			String pathContext = "context(" + i + ")";
			Context c = new Context();
			c.setId(xmlFile.getString(pathContext+ "[@id]"));
			c.setName(xmlFile.getString(pathContext+ "[@name]"));
			c.setDescription(xmlFile.getString(pathContext+".description"));
			//TODO : c.setEdit(...) le param edit plus tard
			List refIdList = xmlFile.getList(pathContext+ ".refCategoryProfile[@refId]");
			Iterator iterator = refIdList.iterator();
			for (String s = null; iterator.hasNext();){
				s = (String)iterator.next();
				c.addRefIdManagedCategoryProfile(s);
			}
			channel.addContext(c);
		};
    }    
    
    /**
     * Initialises associations between contexts and managed category profiles 
     * defined in the channel config in channel
     * @param channel of the initialization
     */
	protected static void initContextManagedCategoryProfilesLinks(Channel channel){
    	if (log.isDebugEnabled()){
    		log.debug("initContextManagedCategoryProfilesLinks()");
    	}
    	
    	Set<String> set = channel.getContextsHash().keySet();
    	Iterator iterator =set.iterator();
    	while(iterator.hasNext()){
    		String id = (String)iterator.next();
    		Context c = channel.getContextById(id);
    		c.initManagedCategoryProfiles(channel);
    	}
	}
/* ********************** ACCESSORS *****************************************/

	/**
	 * Returns the classpath relative path of the channel config
	 * @return configFilePath
	 * @see ChannelConfig#filePath
	 */
	protected static String getfilePath() {
		return filePath;
	}

	/**
	 * Set the classpath relative file path of the channel config
	 * @param filePath
	 * @see ChannelConfig#filePath
	 */
	protected static void setfilePath(String filePath) {
		log.debug("setFilePath("+filePath+")");
		ChannelConfig.filePath = filePath;
	}
	/**
	 * @return Returns the loadedFlag.
	 */
	protected static boolean isModified() {
		return modified;
	}

	/**
	 * @param modified The modified to set.
	 */
	protected static void setModified(boolean modified) {
		ChannelConfig.modified = modified;
	}


	
}
