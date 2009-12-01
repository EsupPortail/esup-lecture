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
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.esupportail.lecture.dao.FreshXmlFileThread;
import org.esupportail.lecture.domain.DomainTools;
import org.esupportail.lecture.exceptions.dao.XMLParseException;
import org.esupportail.lecture.exceptions.domain.ChannelConfigException;

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
	private static Document xmlFile;
	
	/**
	 *  relative classpath of the file to load.
	 */
	private static String filePath;
	
	/**
	 *  Base path of the file to load.
	 */
	private static String fileBasePath;
	
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

	private static int xmlFileTimeOut;
	
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
	}

	/*
	 *********************** METHODS *****************************************/
	
	/**
	 * Return a singleton of this class used to load ChannelConfig file.
	 * @param configFilePath file path of the channel config
	 * @param defaultTtl 
	 * @return an instance of the file to load (singleton)
	 * @throws ChannelConfigException 
	 * @see ChannelConfig#singleton
	 */
	protected static ChannelConfig getInstance(final String configFilePath, final int defaultTimeOut) throws ChannelConfigException {
		filePath = configFilePath;
		xmlFileTimeOut = defaultTimeOut;
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

		if (singleton == null) {
			singleton = new ChannelConfig();
		}
		return singleton;
	}
	
	protected static synchronized void getConfigFile() throws ChannelConfigException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("getConfigFile()");
		}
	
		if (filePath == null) {
			String errorMsg = "Config file path not defined, see in domain.xml file.";
			LOG.error(errorMsg);
			throw new ChannelConfigException(errorMsg);
		}
		
		URL url = ChannelConfig.class.getResource(filePath);
		if (url == null) {
			String errorMsg = "Config file: " + filePath + " not found.";
			LOG.error(errorMsg);
			throw new ChannelConfigException(errorMsg);
		}
		File file = new File(url.getFile());
		fileBasePath = file.getAbsolutePath();

		Document xmlFileLoading = getFreshConfigFile();
		Boolean xmlFileOk = checkXmlFile(xmlFileLoading);
		if ((xmlFileLoading == null) || (!xmlFileOk)) {
			String errorMsg = "Impossible to load XML Channel config (esup-lecture.xml)";
			LOG.error(errorMsg);
			throw new ChannelConfigException(errorMsg);
		} else {
			xmlFile = xmlFileLoading;
		}
	}
	/**
	 * Check syntax file that cannot be checked by DTD.
	 * @throws ChannelConfigException 
	 */
	private synchronized static boolean checkXmlFile(Document xmlFileChecked) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("checkXmlFile()");
		}
		
		nbProfiles = xmlFileChecked.getMaxIndex("categoryProfile") + 1;
		if (nbProfiles == 0) {
			LOG.warn("checkXmlConfig :: No managed category profile declared in channel config");
		}
	
		// TODO (GB later) groupes visibility :
		// - verifier l'existances des noms d'attributs (et de groupes) 
		// 	 dans le portletContext (portlet.xml) : WARNING
		// - verifier qu'il y ait au moins un groupe de visibilite pour 
		//   chq cat (au moins un des trois et non vide) : WARNING	
		//  (verifier que les attributs portail references dans la config
	   	//      ont bien ete declares dans le portlet.xml)
		if (false) {
			String errorMsg = "...";
			LOG.error(errorMsg);
			return false;
		}
		
		nbContexts = xmlFile.getMaxIndex("context") + 1;
		
		if (nbContexts == 0) {
			LOG.warn("No context declared in channel config (esup-lecture.xml)");
		}
		return true;
		
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
	 * @return
	 */
	protected synchronized static Document getFreshConfigFile() {
		// Assign null to configFileLoaded during the loading
		Document ret = null;
		// Launch thread
		FreshXmlFileThread thread = new FreshXmlFileThread(fileBasePath);
		
		int timeout = 0;
		try {
			thread.start();
			timeout = xmlFileTimeOut;
			thread.join(timeout);
			Exception e = thread.getException();
			if (e != null) {
				String msg = "Thread getting Source launches XMLParseException";
				LOG.warn(msg);
				throw new XMLParseException(msg, e);
			}
	        if (thread.isAlive()) {
	    		thread.interrupt();
				String msg = "configFile not loaded in " + timeout + " milliseconds";
				LOG.warn(msg);
	        }	
			ret = thread.getXmlFile();
		} catch (InterruptedException e) {
			String msg = "Thread getting ConfigFile interrupted";
			LOG.warn(msg);
			ret = null;
		} catch (IllegalThreadStateException e) {
			String msg = "Thread getting ConfigFile launches IllegalThreadStateException";
			LOG.warn(msg);
			ret = null;
		} catch (XMLParseException e) {
			String msg = "Thread getting Source launches XMLParseException";
			LOG.warn(msg);
			ret = null;
		} finally {
			return ret;
		}
	}

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
	 * Load the ttl of the config file.
	 */
	protected static synchronized void loadConfigTtl() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("loadGuestUser()");
		}
		//int configTtl = xmlFile.getInt("ttl", DomainTools.getConfigTtl());
		Element root = xmlFile.getRootElement();
		String configTtl = root.valueOf("/channelConfig/ttl");
		DomainTools.setConfigTtl(Integer.parseInt(configTtl));
	}

	/**
	 * Load a DefinitionSets that is used to define visibility groups of a managed category profile.
	 * @param fatherName name of the father XML element refered to (which visibility group)
	 * @param pathCategoryProfile index of the XML element category profile
	 * @return the initialized DefinitionSets
	 */
	private static synchronized DefinitionSets loadDefAndContentSets(final String fatherName, final String pathCategoryProfile) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("loadDefAndContentSets(" + fatherName + "," + pathCategoryProfile + ")");
		}
		DefinitionSets defAndContentSets = new DefinitionSets();
		String fatherPath = pathCategoryProfile + ".visibility." + fatherName;
		
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
			c.setTreeVisible(xmlFile.getBoolean(pathContext + "[@treeVisible]", true));
	    	if (LOG.isDebugEnabled()) {
	    		LOG.debug("loadContexts() : treeVisible " + c.getTreeVisible());
	    	}
			c.setDescription(xmlFile.getString(pathContext + ".description"));
			//TODO (GB later) c.setEdit(...) param edit 
			List<String> refIdList = xmlFile.getList(pathContext + ".refCategoryProfile[@refId]");
			Map<String, Integer> orderedCategoryIDs = 
				Collections.synchronizedMap(new HashMap<String, Integer>());
			int xmlOrder = 1;
			for (String refId : refIdList) {
				// Ajout mcp
				c.addRefIdManagedCategoryProfile(refId);
				orderedCategoryIDs.put(refId, xmlOrder);
				xmlOrder += 1;				
			}
			c.setOrderedCategoryIDs(orderedCategoryIDs);
			channel.addContext(c);
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
		// TODO (GB later) sera utilisé lorsque le file sera externalisé
		LOG.debug("setFilePath(" + filePath + ")");
		ChannelConfig.filePath = filePath;
	}
	/**
	 * @return true if the channel config file has been modified since last "getInstance"
	 */
	protected static boolean isModified() {
		return modified;
	}

	/**
	 * @param channel
	 */
	public static void loadContextsAndCategoryprofiles(final Channel channel) {
    	if (LOG.isDebugEnabled()) {
    		LOG.debug("loadContextsAndCategoryprofiles()");
    	}
		nbContexts = xmlFile.getMaxIndex("context") + 1;
		String pathCategoryProfile = "categoryProfile(" + 0 + ")";
		String categoryProfileId = "";
		for (int i = 0; i < nbContexts; i++ ) {
			String pathContext = "context(" + i + ")";
			Context c = new Context();
			c.setId(xmlFile.getString(pathContext + "[@id]"));
			c.setName(xmlFile.getString(pathContext + "[@name]"));
			c.setTreeVisible(xmlFile.getBoolean(pathContext + "[@treeVisible]", true));
	    	if (LOG.isDebugEnabled()) {
	    		LOG.debug("loadContextsAndCategoryprofiles() : contextId " + c.getId());
	    	}
			c.setDescription(xmlFile.getString(pathContext + ".description"));
			//TODO (GB later) c.setEdit(...) param edit 
			List<String> refCategoryProfileList = xmlFile.getList(pathContext + ".refCategoryProfile");

			// Lire les refCategoryProfilesUrl puis :
			// - les transformer en refCategoryProfile ds le context
			// - ajouter les categoryProfile
			// A faire dans checkXmlFile ?
			
			List<String> refIdList = xmlFile.getList(pathContext + ".refCategoryProfile[@refId]");
			//List<String> categoryProfileList = xmlFile.getList("categoryProfile");
			nbProfiles = xmlFile.getMaxIndex("categoryProfile") + 1;
			Map<String, Integer> orderedCategoryIDs = 
				Collections.synchronizedMap(new HashMap<String, Integer>());
			int xmlOrder = 1;
			for (String refId : refIdList) {
				// Ajout mcp
		    	if (LOG.isDebugEnabled()) {
		    		LOG.debug("loadContextsAndCategoryprofiles() : refCategoryProfileId " + refId );
		    	}
				boolean profileFound = false;
				for (int j = 0; j < nbProfiles; j++ ) {
					pathCategoryProfile = "categoryProfile(" + j + ")";
					categoryProfileId = xmlFile.getString(pathCategoryProfile + "[@id]");
			    	if (LOG.isDebugEnabled()) {
			    		LOG.debug("loadContextsAndCategoryprofiles() : is categoryProfileId " + categoryProfileId + " matching ?");
			    	}
					if (categoryProfileId.compareTo(refId) == 0) {
						profileFound = true;
				    	if (LOG.isDebugEnabled()) {
				    		LOG.debug("loadContextsAndCategoryprofiles() : categoryProfileId " + refId + " matches... create mcp");
				    	}
						break;
					}
				}
				if (profileFound) {
					// Add mcp in Context
					ManagedCategoryProfile mcp = new ManagedCategoryProfile();
					// Id = long Id
					String mcpProfileID = xmlFile.getString(pathCategoryProfile + "[@id]");
					mcp.setFileId(c.getId(), mcpProfileID);
			    	if (LOG.isDebugEnabled()) {
			    		LOG.debug("loadContextsAndCategoryprofiles() : categoryProfileId " + mcp.getId() + " matches... create mcp");
			    	}
	
					mcp.setName(xmlFile.getString(pathCategoryProfile + "[@name]"));
					mcp.setCategoryURL(xmlFile.getString(pathCategoryProfile + "[@urlCategory]"));
					//TODO (GB later) c.setEdit(...) param edit
					mcp.setTrustCategory(xmlFile.getBoolean(pathCategoryProfile + "[@trustCategory]"));
					mcp.setUserCanMarkRead(xmlFile.getBoolean(pathCategoryProfile + "[@userCanMarkRead]", true));
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
				    visibilitySets.setAllowed(loadDefAndContentSets("allowed", pathCategoryProfile));
				    visibilitySets.setAutoSubscribed(loadDefAndContentSets("autoSubscribed", pathCategoryProfile));
				   	visibilitySets.setObliged(loadDefAndContentSets("obliged", pathCategoryProfile));
				    mcp.setVisibility(visibilitySets);
				    
				    channel.addManagedCategoryProfile(mcp);    
					c.addRefIdManagedCategoryProfile(mcp.getId());
					orderedCategoryIDs.put(mcp.getId(), xmlOrder);
				}
				xmlOrder += 1;				
			}
			c.setOrderedCategoryIDs(orderedCategoryIDs);
			channel.addContext(c);
		}
    }    
 		

}
