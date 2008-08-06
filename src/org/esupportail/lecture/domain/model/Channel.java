/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;



import java.util.Hashtable;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.commons.exceptions.ConfigException;
import org.esupportail.lecture.dao.DaoService;
import org.esupportail.lecture.domain.DomainTools;
import org.esupportail.lecture.domain.ExternalService;
import org.esupportail.lecture.exceptions.domain.ChannelConfigException;
import org.esupportail.lecture.exceptions.domain.ContextNotFoundException;
import org.esupportail.lecture.exceptions.domain.FatalException;
import org.esupportail.lecture.exceptions.domain.ManagedCategoryProfileNotFoundException;
import org.esupportail.lecture.exceptions.domain.MappingFileException;
import org.esupportail.lecture.exceptions.domain.PrivateException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * The "lecture" channel : main domain model class
 * It is the owner of contexts and managed elements.
 * @author gbouteil
 */
public class Channel implements InitializingBean {

	/*
	 ************************** PROPERTIES *********************************/	
	
	/**
	 * Log instance. 
	 */
	protected static final Log LOG = LogFactory.getLog(Channel.class); 

	/* channel's elements */
	
	/**
     * Hashtable of contexts defined in the channel, indexed by their ids.
     */
	private Hashtable<String, Context> contextsHash;
	
	/**
	 * Hashtable of ManagedCategoryProfiles defined in the chanel, indexed by their Id.
	 */
	private Hashtable<String, ManagedCategoryProfile> managedCategoryProfilesHash;
	
	/* mappings */
	
	/**
	 * List of mappings defined by the mapping file.
	 */
	private List<Mapping> mappingList;
	
	/**
	 * Hash to access mappings by dtd.
	 */	
	private Hashtable<String, Mapping> mappingHashByDtd;
	
	/**
	 * Hash to access mappings by xmlns.
	 */	
	private Hashtable<String, Mapping> mappingHashByXmlns;
	
	/**
	 * Hash to access mappings by xmlType.
	 */	
	private Hashtable<String, Mapping> mappingHashByXmlType;
	
	/**
	 * Hash to access mappings by root element.
	 */	
	private Hashtable<String, Mapping> mappingHashByRootElement;

	/**
	 * Hash to access mappings by sourceURL.
	 */	
	private Hashtable<String, Mapping> mappingHashBySourceURL;

	/* config and mapping files */
	
	/**
	 * relative file path of the config file.
	 */
	private String configFilePath;
	
	/**
	 * configLoaded = true if channel config has ever been loaded in channel.
	 */
	private boolean configLoaded;
	
	/**
	 * relative file path of the mapping file.
	 */
	private String mappingFilePath;
	
	/**
	 * mappingsLoaded = true if channel config has ever been loaded in channel.
	 */
	private boolean mappingsLoaded;
	
	/* Some services */
	
	/**
	 * access to dao services.
	 */
	private DaoService daoService;
	
	/**
	 * acces to external service.
	 */
	private ExternalService externalService;

	
	/*
	 ************************** INIT *********************************/	

	/**
	 * default constructor.
	 */
	public Channel() {
		configLoaded = false;
		mappingsLoaded = false;
	}
	
	/**
	 * @throws FatalException
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws FatalException   {
		if (LOG.isDebugEnabled()) {
			LOG.debug("afterPropertiesSet()");
		}
		Assert.notNull(daoService, "property daoService can not be null");
		Assert.notNull(externalService, "property externalService can not be null");
		Assert.notNull(configFilePath, "property configFilePath cannot be null");
		Assert.notNull(mappingFilePath, "property mappingFilePath cannot be null");
		try {
			startup();
		} catch (PrivateException e) {
			String errorMsg = "Unable to startup channel because of a PrivateException.";
			LOG.fatal(errorMsg);
			throw new FatalException(errorMsg, e);
		} 
		DomainTools.setChannel(this);
		DomainTools.setDaoService(daoService);
		DomainTools.setExternalService(externalService);
	}
	
	/**
	 * Methods called to start channel (load the config and mapping file.
	 * if needed when files are modified from last loading)
	 * @throws ChannelConfigException 
	 * @throws ContextNotFoundException 
	 * @throws ManagedCategoryProfileNotFoundException 
	 * @throws MappingFileException 
	 */
	private synchronized void startup() 
		throws ManagedCategoryProfileNotFoundException, ContextNotFoundException,
		ChannelConfigException, MappingFileException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("startup()");
		}
		loadConfig();
		loadMappingFile();
	}
	
	/**
	 * Load config file if is modified since last loading (using ChannelConfig), 
	 * It gets contexts and managed category profiles definition and
	 * Initialize these elements.
	 * @throws ChannelConfigException 
	 * @throws ContextNotFoundException 
	 * @throws ManagedCategoryProfileNotFoundException 
	 */
	private synchronized void loadConfig() throws ChannelConfigException,
			ManagedCategoryProfileNotFoundException, ContextNotFoundException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("loadConfig()");
		}
		
		try {
			//ChannelConfig config = 
			ChannelConfig.getInstance(configFilePath);
			// TODO (GB later)
			// - utiliser l'objet config pour appeler les m�thodes apr�s (reset ...)
			// 		et faire une classe FileToLoad avec ces m�thodes en non static
			// - charger la config via un DAO ?
			
		} catch (ChannelConfigException e) {
			// TODO (GB later)
//			if (configLoaded) {
//				log.error("Unable to load new config : "+e.getMessage());
//			} else {
				String errorMsg = 
					"Unable to load config and start initialization : " + e.getMessage(); 
				LOG.error(errorMsg);
				throw new ChannelConfigException(errorMsg, e);
//			}
		}
		
		if (ChannelConfig.isModified()) {
			/* Reset channel properties loaded from config */
			resetChannelConfigProperties();
			
			/* Loading guest user name */
			ChannelConfig.loadGuestUser();
			
			/* Loading managed category profiles */
			ChannelConfig.loadManagedCategoryProfiles(this);
				
			/* Loading Contexts */
			ChannelConfig.loadContexts(this);
		
			/* Initialize Contexts and ManagedCategoryProfiles links */
			ChannelConfig.initContextManagedCategoryProfilesLinks(this);
		}
		if (!configLoaded) {
			configLoaded = true;
		}
		LOG.info("The channel config is loaded (file " + ChannelConfig.getfilePath() + ") in channel");
	}
	
	/**
	 * Initialize every channel properties that are set up by loading channel configuration file.
	 */
	private synchronized void resetChannelConfigProperties() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("resetChannelConfigProperties()");
		}
		
		// TODO (GB later) UserAttributes.init();
		contextsHash = new Hashtable<String, Context>();
		managedCategoryProfilesHash = new Hashtable<String, ManagedCategoryProfile>();
	}
	
	/**
	 * Load mapping file if is modified since last loading (using MappingFile)
	 * It gets list of mappings used by the channel and 
	 * Initialize these elements.
	 * @throws MappingFileException 
	 */	
	private synchronized void loadMappingFile() throws MappingFileException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("loadMappingFile()");
		}
		
		try {
			//MappingFile mappingFile = 
			MappingFile.getInstance(mappingFilePath);
			
		} catch (MappingFileException e) {
			// TODO (GB later)
//			if (mappingsLoaded) {
//				log.error("unable to load new mappings : "+e.getMessage());
//			} else {
				String errorMsg = "Unable to load mappings and start initialization : "
					+ e.getMessage(); 
				LOG.error(errorMsg);
				throw new MappingFileException(errorMsg);
//			}
		}
		
		if (MappingFile.isModified()) {
			/* Reset channel properties loaded from config */
			resetMappingFileProperties();
				
			/* Loading Mappings */
			MappingFile.loadMappings(this);
			
			/* Initialize hashs mapping if channel */
			MappingFile.initChannelHashMappings(this);

		}
		if (!mappingsLoaded) {
			mappingsLoaded = true;
		}
		LOG.info("The mapping is loaded (file " + MappingFile.getMappingFilePath() + ") in channel");
	}

	/**
	 * Initialize every channel properties that are set up by loading mapping file.
	 */
	private synchronized void resetMappingFileProperties() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("resetMappingFileProperties()");
		}
//		mappingList = new ArrayList<Mapping>();
		mappingHashByDtd = new Hashtable<String, Mapping>();
		mappingHashByXmlns = new Hashtable<String, Mapping>();
		mappingHashByXmlType = new Hashtable<String, Mapping>();
		mappingHashByRootElement = new Hashtable<String, Mapping>();
		mappingHashBySourceURL = new Hashtable<String, Mapping>();
	}

	
	/*
	 *************************** METHODS ************************************/

	/**
	 * return the user profile identified by "userId". 
	 * It takes it from the dao if exists, else, it create a user profile
	 * @param userId : identifient of the user profile
	 * @return the user profile
	 */
	public UserProfile getUserProfile(final String userId) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("getUserProfile(" + userId + ")");
		}
	
		if (userId == null) {
			// TODO (RB <-- GB) Pourras tu noter dans le msg, le lieu où se configure le userId ? Merci
			String mes = "userId not found. Check configuration.";
			LOG.error(mes);
			throw new ConfigException(mes);
		}
//		UserProfile userProfile = userProfilesHash.get(userId);
//		if (userProfile == null) {
		UserProfile userProfile = DomainTools.getDaoService().getUserProfile(userId);
		if (userProfile == null) {
			userProfile = new UserProfile(userId);
			DomainTools.getDaoService().saveUserProfile(userProfile);
		}
//			userProfilesHash.put(userId, userProfile);
//		} else {
//			// Reatach userProfile to hibernate session
//			userProfile = DomainTools.getDaoService().refreshUserProfile(userProfile);
//		}
		return userProfile;
	}
	
	/**
	 * return the context identified by "contextId".
	 * if it is defined in channel
	 * @param contextId
	 * @return  the context identified by "contextId"
	 * @throws ContextNotFoundException 
	 */
	protected Context getContext(final String contextId) throws ContextNotFoundException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("getContext(" + contextId + ")");
		}
		Context context = contextsHash.get(contextId);
		if (context == null) {
			String errorMsg = "Context " + contextId + " is not defined in channel";
			LOG.error(errorMsg);
			throw new ContextNotFoundException(errorMsg);
		}
		return context;
	}
	
	/**
	 * @param contextId id of the context
	 * @return true if the context is defined in this Channel
	 */
	protected boolean isThereContext(final String contextId) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("getContext(" + contextId + ")");
		}
		return contextsHash.containsKey(contextId);		
	}
	
	/**
	 * Add a context to this Channel.
	 * @param c context to add
	 */	
	protected synchronized void addContext(final Context c) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("addContext(" + c.getId() + ")");
		} 
		this.contextsHash.put(c.getId(), c);
	}	

	/**
	 * Returns the managed category profile by identified by Id.
	 * @param id the managed category profile Id
	 * @return managedCategoryProfile
	 * @throws ManagedCategoryProfileNotFoundException 
	 */
	protected ManagedCategoryProfile getManagedCategoryProfile(final String id) 
			throws ManagedCategoryProfileNotFoundException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("getManagedCategoryProfile(" + id + ")");
		}
		ManagedCategoryProfile mcp = managedCategoryProfilesHash.get(id);
		if (mcp == null) {
			String errorMsg = 
				"ManagedCategoryProfile " + id + " is not defined in channel";
			LOG.error(errorMsg);
			throw new ManagedCategoryProfileNotFoundException(errorMsg);
		}
		return mcp;
	}
	
	/**
	 * Add a managed category profile to this Channel.
	 * @param m the managed category profile to add
	 */
	protected synchronized void addManagedCategoryProfile(final ManagedCategoryProfile m) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("addManagedCategoryProfile(" + m.getId() + ")");
		}
		this.managedCategoryProfilesHash.put(m.getId(), m);
	}
	
	/**
	 * Add a mapping to the list of mappings defined in this Channel.
	 * @param m the mapping to add
	 */
	protected void addMapping(final Mapping m) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("addMapping(" + m.getRootElement() + ")");
		}
		String sourceURL = m.getSourceURL();
		String dtd = m.getDtd();
		String xmlns = m.getXmlns();
		String xmlType = m.getXmlType();
		String rootElement = m.getRootElement();
		
		if (!sourceURL.equals("")) {
			addMappingBySourceURL(m);
		}
		if (!dtd.equals("")) {
			addMappingByDtd(m);
		}
		if (!xmlns.equals("")) {
			addMappingByXmlns(m);
		}
		if (!xmlType.equals("")) {
			addMappingByXmlType(m);
		}
		if (!rootElement.equals("")) {
			addMappingByRootElement(m);
		}
	}
	
	/**
	 * Add a mapping (defined in the channel) to the hash of mappings indexed by its dtd.
	 * @param m the mapping to add
	 * @see Channel#mappingHashByDtd
	 */
	private synchronized void addMappingByDtd(final Mapping m) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("addMappingByDtd(" + m.getRootElement() + ")");
		}
		this.mappingHashByDtd.put(m.getDtd(), m);
	}
	
	/**
	 * @param dtd
	 * @return mapping associated with dtd
	 */
	protected Mapping getMappingByDtd(final String dtd) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("getMappingByDtd(" + dtd + ")");
		}
		return mappingHashByDtd.get(dtd);
	}

	/**
	 * Add a mapping (defined in the channel) to the hash of mappings indexed by its xmlns.
	 * @param m the mapping to add
	 * @see Channel#mappingHashByXmlns
	 */
	private synchronized void addMappingByXmlns(final Mapping m) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("addMappingByXmlns(" + m.getRootElement() + ")");
		}
		this.mappingHashByXmlns.put(m.getXmlns(), m);
	}	
	/**
	 * @param xmlns
	 * @return mapping associated with xmlns
	 */
	protected Mapping getMappingByXmlns(final String xmlns) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("getMappingByXmlns(" + xmlns + ")");
		}
		return mappingHashByXmlns.get(xmlns);
	}
	
	/**
	 * Add a mapping (defined in the channel) to the hash of mappings indexed by its xmlType.
	 * @param m the mapping to add
	 * @see Channel#mappingHashByXmlType
	 */
	private synchronized void addMappingByXmlType(final Mapping m) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("addMappingByXmlType(" + m.getRootElement() + ")");
		}
		this.mappingHashByXmlType.put(m.getXmlType(), m);
	}	

	/**
	 * @param xmlType
	 * @return mapping associated with xmlType
	 */
	protected Mapping getMappingByXmlType(final String xmlType) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("getMappingByXmlType(" + xmlType + ")");
		}
		return mappingHashByXmlType.get(xmlType);
	}

	/**
	 * Add a mapping (defined in the channel) to the hash of mappings indexed by its RootElement.
	 * @param m the mapping to add
	 * @see Channel#mappingHashByRootElement
	 */
	private synchronized void addMappingByRootElement(final Mapping m) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("addMappingByRootElement(" + m.getRootElement() + ")");
		}
		this.mappingHashByRootElement.put(m.getRootElement(), m);
	}

	/**
	 * @param rootElement
	 * @return mapping associated with rootElement
	 */
	protected Mapping getMappingByRootElement(final String rootElement) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("getMappingByRootElement(" + rootElement + ")");
		}
		return mappingHashByRootElement.get(rootElement);
	}

	/**
	 * Add a mapping (defined in the channel) to the hash of mappings indexed by its sourceURL.
	 * @param m the mapping to add
	 * @see Channel#mappingHashBySourceURL
	 */
	private synchronized void addMappingBySourceURL(final Mapping m) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("addMappingBySourceURL(" + m.getRootElement() + ")");
		}
		this.mappingHashBySourceURL.put(m.getSourceURL(), m);
	}

	/**
	 * @param sourceURL
	 * @return mapping for this sourceURL
	 */
	protected Mapping getMappingBySourceURL(final String sourceURL) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("getMappingBySourceURL(" + sourceURL + ")");
		}
		return mappingHashBySourceURL.get(sourceURL);
	}
//	/**
//	 * Return a string containing channel content : mapping file, contexts, managed category profiles,
//	 * xslt mappings, hash mappings by dtd, Hash mappings by xmlns,Hash mappings by xmlType
//	 * 
//	 * @see java.lang.Object#toString()
//	 */
//	public String toString() {
//		String string = "";
//			
//		/* mappingFile */
//		string += "***************** Mapping File : \n\n";
//		string += mappingFile.toString();
//		string += "\n";
//		
////		/* Contexts */ 
//		string += "***************** Contexts : \n\n";
//		string += contextsHash.toString();
//	    string += "\n";
////				
////		/* Managed categories profiles */
//		string += "***************** Managed categories profiles : \n\n";
//		string += managedCategoryProfilesHash.toString();
//	    string += "\n";
////		
//		/* Xslt mappings*/
//		string += "***************** Xslt mappings : \n\n";
//		string += mappingList.toString();
//		string += "\n";
//		
//		/* Hash to access mappings by dtd */
//		string += "***************** Hash mappings by dtd : \n\n";
//		string += mappingHashByDtd.toString();
//		string += "\n";
//				
//		/* Hash to access mappings by xmlns */
//		string += "***************** Hash mappings by xmlns : \n\n";
//		string += mappingHashByXmlns.toString();      
//	    string += "\n";
//		
//		/* Hash to access mappings by xmlType */
//		string += "***************** Hash mappings by xmlType : \n\n";
//		string += mappingHashByXmlType.toString();      
//	    string += "\n";
//		
////		/* User Profiles connected to the chanel */
////		string += "***************** User profiles : \n\n";
////		string += " later ...";
////        string += "\n";
//		
//        return string;
//	}		

	/* 
	 *************************** ACCESSORS ********************************* */
	
	/**
	 * Returns the hashtable of contexts, indexed by their ids.
	 * @return contextsHash
	 * @see Channel#contextsHash
	 */
	protected Hashtable<String, Context> getContextsHash() {
		return contextsHash;
	}
  

	/**
	 * Returns the list of mappings defined in this Channel.
	 * @return mappingList
	 * @see Channel#mappingList
	 */
	protected List<Mapping> getMappingList() {
		return mappingList;
	}
	
	/**
	 * Sets the mappings list of this Channel.
	 * @param mappingList
	 */
	protected void setMappingList(final List<Mapping> mappingList) {
		this.mappingList = mappingList;
	}

	/**
	 * set DaoService.
	 * @param daoService
	 */
	public void setDaoService(final DaoService daoService) {
		this.daoService = daoService;
	}

	/**
	 * @param externalService
	 */
	public void setExternalService(final ExternalService externalService) {
		this.externalService = externalService;
	}

	/**
	 * @return the file path of the config file
	 */
	public String getConfigFilePath() {
		return configFilePath;
	}

	/**
	 * set the file path of the config file.
	 * @param configFilePath 
	 */
	public void setConfigFilePath(final String configFilePath) {
		this.configFilePath = configFilePath;
	}

	/**
	 * @return the fil path of the mapping file
	 */
	public String getMappingFilePath() {
		return mappingFilePath;
	}

	/**
	 * set the file path of the mapping file.
	 * @param mappingFilePath
	 */
	public void setMappingFilePath(final String mappingFilePath) {
		this.mappingFilePath = mappingFilePath;
	}
	
}
