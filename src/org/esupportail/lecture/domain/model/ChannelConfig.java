/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;

import java.io.File;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.DomainTools;
import org.esupportail.lecture.exceptions.domain.ChannelConfigException;
import org.esupportail.lecture.exceptions.domain.ContextNotFoundException;
import org.esupportail.lecture.exceptions.domain.ManagedCategoryProfileNotFoundException;

/**
 * Channel Config : class used to load and parse XML channel file config.
 * @author gbouteil
 */
public class ChannelConfig  {
	
	/* 
	 ********************** PROPERTIES**************************************/ 
	/**
	 * Log instance. 
	 */
	private static final Log LOG = LogFactory.getLog(ChannelConfig.class);
	
	/**
	 * Instance of this class.
	 */
	private static ChannelConfig singleton;
	
	/**
	 * XML file loaded.
	 */
	private static XMLConfiguration xmlFile;
	
	/**
	 *  relative classpath of the file to load.
	 */
	private static String filePath;
	
	/**
	 *  Base path of the file to load.
	 */
	private static String fileBasePath;
	
	/**
	 * Last modified time of the file to load.
	 */
	private static long fileLastModified;
	
	/**
	 * Indicates if file has been modified since last getInstance() calling.
	 */
	private static boolean modified;
	
	/**
	 * Numbers of category profiles declared in the xml file.
	 */
	private static int nbProfiles;
	
	/**
	 * Numbers of contexts declared in the xml file.
	 */
	private static int nbContexts;
	
	/*
	 ************************** INIT *********************************/	

	/**
	 * Private Constructor .
	 * @throws ChannelConfigException 
	 */
	private ChannelConfig() throws ChannelConfigException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("ChannelConfig()");
		}
		try {
			xmlFile = new XMLConfiguration();
			xmlFile.setFileName(fileBasePath);
			xmlFile.setValidating(true);
			xmlFile.load();
			checkXmlFile();
			modified = true;
		} catch (ConfigurationException e) {
			String errorMsg = "Impossible to load XML Channel config (esup-lecture.xml)";
			LOG.error(errorMsg);
			throw new ChannelConfigException(errorMsg, e);
		} 
	}

	/*
	 *********************** METHODS *****************************************/
	
	/**
	 * Return a singleton of this class used to load ChannelConfig file.
	 * @param configFilePath file path of the channel config
	 * @return an instance of the file to load (singleton)
	 * @throws ChannelConfigException 
	 * @see ChannelConfig#singleton
	 */
	protected static ChannelConfig getInstance(final String configFilePath) throws ChannelConfigException {
		filePath = configFilePath;
		return getInstance();
		
	}
	
	/**
	 * Return a singleton of this class used to load ChannelConfig file.
	 * @return an instance of the file to load (singleton)
	 * @throws ChannelConfigException 
	 * @see ChannelConfig#singleton
	 */
	protected static synchronized ChannelConfig getInstance() throws ChannelConfigException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("getInstance()");
		}
	
		if (filePath == null) {
			String errorMsg = "Config file path not defined, see in domain.xml file.";
			LOG.error(errorMsg);
			throw new ChannelConfigException(errorMsg);
		}
		modified = false;
		
		if (singleton == null) {
			URL url = ChannelConfig.class.getResource(filePath);
			if (url == null) {
				String errorMsg = "Config file: " + filePath + " not found.";
				LOG.error(errorMsg);
				throw new ChannelConfigException(errorMsg);
			}
			File file = new File(url.getFile());
			fileBasePath = file.getAbsolutePath();
			fileLastModified = file.lastModified();
			singleton = new ChannelConfig();

		} else {
			if (LOG.isDebugEnabled()) {
				LOG.debug("getInstance :: " + " singleton not null ");
			}
			
			File file = new File(fileBasePath);
			long newDate = file.lastModified();
			if (fileLastModified < newDate) {
				if (LOG.isDebugEnabled()) {
					LOG.debug("getInstance :: " + "Channel config loading");
				}
				
				fileLastModified = newDate;
				singleton = new ChannelConfig();
			} else {
				LOG.debug("getInstance :: configuration not reloaded");
			}
		}		
		return singleton;
	}
	
	/**
	 * Check syntax file that cannot be checked by DTD.
	 * @throws ChannelConfigException 
	 */
	private synchronized void checkXmlFile() throws ChannelConfigException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("checkXmlFile()");
		}
		
		nbProfiles = xmlFile.getMaxIndex("categoryProfile") + 1;
		if (nbProfiles == 0) {
			LOG.warn("checkXmlConfig :: No managed category profile declared in channel config");
		}
	
		// TODO (GB later) groupes visibility :
		// - v�rifier l'existances des noms d'attributs (et de groupes) 
		// 	 dans le portletContext (portlet.xml) : WARNING
		// - v�rifier qu'il y ait au moins un groupe de visibilit� pour 
		//   chq cat (au moins un des trois et non vide) : WARNING	
		//  (v�rifier que les attributs portail r�f�renc�s dans la config
	   	//      ont bien �t� d�clar�s dans le portlet.xml)
		if (false) {
			String errorMsg = "...";
			LOG.error(errorMsg);
			throw new ChannelConfigException(errorMsg);
		}
		
		nbContexts = xmlFile.getMaxIndex("context") + 1;
		
		if (nbContexts == 0) {
			LOG.warn("No context declared in channel config (esup-lecture.xml)");
		}
		
	}

// TODO (RB <-- GB) : Raymond, peux tu confirmer ? : 
//	Code inutile depuis que le userId est récupéré via l'authenticationService	
//	/**
//	 * Load attribute name provided by portlet request.
//	 * to identified user profiles (userId)
//	 */
//	protected static synchronized void loadUserId() {
//		if (LOG.isDebugEnabled()) {
//			LOG.debug("loadUserId()");
//		}
//		DomainTools.setUserID(xmlFile.getString("userId"));
//	}

	/**
	 * Load attribute that identified guest user name (guestUser).
	 */
	protected static synchronized void loadGuestUser() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("loadGuestUser()");
		}
		String guestUser = xmlFile.getString("guestUser");
		if (guestUser == null || guestUser.equals("")) {
			guestUser = "guest";
		}
		DomainTools.setGuestUser(guestUser);
	}

	/**
	 * Load Managed Category profiles from config file into the channel.
	 * @param channel of the loading
	 */
	protected static synchronized void loadManagedCategoryProfiles(final Channel channel)  {
		if (LOG.isDebugEnabled()) {
			LOG.debug("loadManagedCategoryProfiles()");
		}
		
		for (int i = 0; i < nbProfiles; i++ ) {
			String pathCategoryProfile = "categoryProfile(" + i + ")";
			ManagedCategoryProfile mcp = new ManagedCategoryProfile();
			mcp.setId(xmlFile.getString(pathCategoryProfile + "[@id]"));
			mcp.setName(xmlFile.getString(pathCategoryProfile + "[@name]"));
			mcp.setCategoryURL(xmlFile.getString(pathCategoryProfile + "[@urlCategory]"));
			//TODO (GB later) c.setEdit(...) param edit
			mcp.setTrustCategory(xmlFile.getBoolean(pathCategoryProfile + "[@trustCategory]"));
			mcp.setTtl(xmlFile.getInt(pathCategoryProfile + "[@ttl]"));
			mcp.setTimeOut(xmlFile.getInt(pathCategoryProfile + "[@timeout]"));
			
		    // Accessibility
		    String access = xmlFile.getString(pathCategoryProfile + "[@access]");
		    if (access.equalsIgnoreCase("public")) {
				mcp.setAccess(Accessibility.PUBLIC);
			} else if (access.equalsIgnoreCase("cas")) {
				mcp.setAccess(Accessibility.CAS);
			}
			
		    // Visibility
		    VisibilitySets visibilitySets = new VisibilitySets();  
		    // foreach (allowed / autoSubscribed / Obliged
		    visibilitySets.setAllowed(loadDefAndContentSets("allowed", i));
		    visibilitySets.setAutoSubscribed(loadDefAndContentSets("autoSubscribed", i));
		   	visibilitySets.setObliged(loadDefAndContentSets("obliged", i));
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
//				throw new MyException();
//			}
//		   // Visibility	
	}
	
	/**
	 * Load a DefinitionSets that is used to define visibility groups of a managed category profile.
	 * @param fatherName name of the father XML element refered to (which visibility group)
	 * @param index index of the XML element category profile
	 * @return the initialized DefinitionSets
	 */
	private static synchronized DefinitionSets loadDefAndContentSets(final String fatherName, final int index) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("loadDefAndContentSets(" + fatherName + "," + index + ")");
		}
		DefinitionSets defAndContentSets = new DefinitionSets();
		String fatherPath = "categoryProfile(" + index + ").visibility." + fatherName;
		
		// Definition by group enumeration
		int nbGroups = xmlFile.getMaxIndex(fatherPath + ".group") + 1;
		for (int i = 0; i < nbGroups; i++) {
			defAndContentSets.addGroup(xmlFile.getString(fatherPath + ".group(" + i + ")[@name]"));
		}
   		// Definition by regular 
   		int nbRegulars = xmlFile.getMaxIndex(fatherPath + ".regular") + 1;   	
   		for (int i = 0; i < nbRegulars; i++) {
   			RegularOfSet regular = new RegularOfSet();
   			regular.setAttribute(xmlFile.getString(fatherPath + ".regular(" + i + ")[@attribute]"));
   			regular.setValue(xmlFile.getString(fatherPath + ".regular(" + i + ")[@value]"));	
   			defAndContentSets.addRegular(regular);
   		}
   		return defAndContentSets;
	}
	
	
	/**
	 * Load Contexts from config file into the channel.
	 * @param channel of the loading
	 */
	@SuppressWarnings("unchecked")
	protected static synchronized void loadContexts(final Channel channel) {
    	if (LOG.isDebugEnabled()) {
    		LOG.debug("loadContexts()");
    	}

		for (int i = 0; i < nbContexts; i++ ) {
			String pathContext = "context(" + i + ")";
			Context c = new Context();
			c.setId(xmlFile.getString(pathContext + "[@id]"));
			c.setName(xmlFile.getString(pathContext + "[@name]"));
			c.setDescription(xmlFile.getString(pathContext + ".description"));
			//TODO (GB later) c.setEdit(...) param edit 
			List<String> refIdList = xmlFile.getList(pathContext + ".refCategoryProfile[@refId]");
			Map<String, Integer> orderedCategoryIDs = 
				Collections.synchronizedMap(new HashMap<String, Integer>());
			int xmlOrder = 1;
			for (String refId : refIdList) {
				c.addRefIdManagedCategoryProfile(refId);
				orderedCategoryIDs.put(refId, xmlOrder);
				xmlOrder += 1;				
			}
			c.setOrderedCategoryIDs(orderedCategoryIDs);
			channel.addContext(c);
		}
    }    
    
    /**
     * Initializes associations between contexts and managed category profiles.
     * defined in the channel config in channel
     * @param channel of the initialization
     * @throws ContextNotFoundException 
     * @throws ContextNotFoundException 
     * @throws ManagedCategoryProfileNotFoundException 
     * @throws ManagedCategoryProfileNotFoundException 
     */
	protected static synchronized void initContextManagedCategoryProfilesLinks(final Channel channel) 
		throws ContextNotFoundException, ManagedCategoryProfileNotFoundException {
    	if (LOG.isDebugEnabled()) {
    		LOG.debug("initContextManagedCategoryProfilesLinks()");
    	}
    	
    	Set<String> set = channel.getContextsHash().keySet();
    	Iterator<String> iterator = set.iterator();
    	while (iterator.hasNext()) {
    		String id = iterator.next();
    		Context c = channel.getContext(id);
    		c.initManagedCategoryProfiles(channel);
    	}
	}
	
	/*
	 *********************** ACCESSORS *****************************************/

	/**
	 * Returns the relative classpath file path of the channel config.
	 * @return configFilePath
	 * @see ChannelConfig#filePath
	 */
	protected static String getfilePath() {
		return filePath;
	}

	/**
	 * Set the relative classpath file path of the channel config.
	 * @param filePath
	 * @see ChannelConfig#filePath
	 */
	protected static synchronized void setfilePath(final String filePath) {
		// TODO (GB later) sera utilis� lorsque le file sera externalis�
		LOG.debug("setFilePath(" + filePath + ")");
		ChannelConfig.filePath = filePath;
	}
	/**
	 * @return true if the channel config file has been modified since last "getInstance"
	 */
	protected static boolean isModified() {
		return modified;
	}

}
