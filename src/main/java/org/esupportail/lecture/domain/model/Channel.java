/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;



import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import org.springframework.util.StringUtils;

/**
 * The "lecture" channel : main domain model class
 * It is the owner of contexts and managed elements.
 * @author gbouteil
 */
/**
 * @author rihab
 *
 */
/**
 * @author rihab
 *
 */
public class Channel implements InitializingBean {

	/*
	 ************************** PROPERTIES *********************************/	
	
	/**
	 * Log instance. 
	 */
	protected static final Log LOG = LogFactory.getLog(Channel.class); 

	/**
	 * The default name for the cache.
	 */
	private static final String DEFAULT_CACHE_NAME = Channel.class.getName();
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
	
	/* for constant initializing */
	//private String gestUser
	
	/**
	 * contextString
	 */
	private String contextString;
	
	/**
	 * dummyTtl
	 */
	private int dummyTtl;
	
	/**
	 * defaultTtl
	 */
	private int defaultTtl;
	
	/**
	 * defaultTimeOut
	 */
	private int defaultTimeOut;
	
	/**
	 * maxTreeSize
	 */
	private int maxTreeSize;
	
	/**
	 * defaultTreeSize
	 */
	private int defaultTreeSize;

	/**
	 * configTtl
	 */
	private int configTtl;
	
	
	/* Some services */
	
	/**
	 * access to dao services.
	 */
	private DaoService daoService;
	
	/**
	 * acces to external service.
	 */
	private ExternalService externalService;

	/**
	 * the name of the cache.
	 */
	private String cacheName;
	/**
	 * the cacheManager.
	 */
	private CacheManager cacheManager;
	/**
	 * the cache.
	 */
	private Cache cache;
	
	/**
	 * the current view
	 */
	private String viewDef;

	/**
	 * Number of articles to display
	 */
	private String nbreArticle;
	
	/**
	 * link to see all
	 */
	private String lienVue;

	
	/*
	 ************************** INIT *********************************/	

	public String getLienVue() {
		return lienVue;
	}

	public void setLienVue(String lienVue) {
		this.lienVue = lienVue;
	}

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
		if (!StringUtils.hasText(cacheName)) {
			setDefaultCacheName();
			LOG.warn(getClass() + ": no cacheName attribute set, '" 
					+ cacheName + "' will be used");
		}
		Assert.notNull(cacheManager, 
				"property cacheManager of class " + getClass().getName() 
				+ " can not be null");
		if (!cacheManager.cacheExists(cacheName)) {
			cacheManager.addCache(cacheName);
		}
		cache = cacheManager.getCache(cacheName);

		Assert.notNull(daoService, "property daoService can not be null");
		Assert.notNull(externalService, "property externalService can not be null");
		Assert.notNull(configFilePath, "property configFilePath cannot be null");
		Assert.notNull(mappingFilePath, "property mappingFilePath cannot be null");
		//Assert.notNull(gestUser, "property gestUser cannot be null");
		Assert.notNull(contextString, "property context cannot be null");
		Assert.notNull(dummyTtl, "property dummyTlt cannot be null");
		Assert.notNull(defaultTtl, "property defaultTtl cannot be null");
		Assert.notNull(defaultTimeOut, "property defaultTimeOut cannot be null");
		Assert.notNull(maxTreeSize, "property maxTreeSize cannot be null");
		Assert.notNull(defaultTreeSize, "property defaultTreeSize cannot be null");
		Assert.notNull(configTtl, "property configTtl cannot be null");
		DomainTools.setConfigTtl(configTtl);
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
		DomainTools.setContext(contextString);
		DomainTools.setDummyTtl(dummyTtl);
		DomainTools.setDefaultTtl(defaultTtl);
		DomainTools.setDefaultTimeOut(defaultTimeOut);
		DomainTools.setMaxTreeSize(maxTreeSize);
		DomainTools.setDefaultTreeSize(defaultTreeSize);
		DomainTools.setViewDef(viewDef);
		DomainTools.setNbreArticle(nbreArticle);
		DomainTools.setLienVue(lienVue);
	}
	
	/**
	 * Methods called to start channel (load the config and mapping file.
	 * if needed when files are modified from last loading)
	 * @throws ChannelConfigException 
	 * @throws ManagedCategoryProfileNotFoundException 
	 * @throws MappingFileException 
	 */
	private synchronized void startup() 
		throws ManagedCategoryProfileNotFoundException, ChannelConfigException, MappingFileException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("startup()");
		}
		String cacheKey = "CHANNEL_CONFIG:" + configFilePath;
		Element element = cache.get(cacheKey);
		if (element != null) { 
			cache.remove(cacheKey);
		}
		loadConfig();
		loadMappingFile();
		cache.put(new Element(cacheKey, cacheKey));
		Element e = cache.get(cacheKey);
		int ttl = DomainTools.getConfigTtl();
		e.setTimeToLive(ttl);
	}
	
	/**
	 * Load config file, 
	 * It gets contexts and managed category profiles definition and
	 * Initialize these elements.
	 * @throws ChannelConfigException 
	 * @throws ManagedCategoryProfileNotFoundException 
	 * @throws ManagedCategoryProfileNotFoundException 
	 */
	private synchronized void loadConfig() throws ChannelConfigException, ManagedCategoryProfileNotFoundException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("loadConfig()");
		}
		try {
			ChannelConfig.getInstance(configFilePath, defaultTimeOut);
			ChannelConfig.getConfigFile();
			// TODO (GB later)
			// - utiliser l'objet config pour appeler les methodes apres (reset ...)
			// 		et faire une classe FileToLoad avec ces methodes en non static
			// - charger la config via un DAO ?
			
			/* Reset channel properties loaded from config */
			resetChannelConfigProperties();
			
			/* Loading guest user name */
			ChannelConfig.loadGuestUser();
			
			/* Loading configTtl */
			ChannelConfig.loadConfigTtl();
			ChannelConfig.loadContextsAndCategoryprofiles(this);
			
			/* Initialize Contexts and ManagedCategoryProfiles links */
			initContextManagedCategoryProfilesLinks(this);
			if (!configLoaded) {
				configLoaded = true;
			}
			LOG.info("The channel config is loaded (file " + ChannelConfig.getfilePath() + ") in channel");

		} catch (ChannelConfigException e) {
			/*
			if (configLoaded) {
				LOG.error("Unable to load new config : "+e.getMessage());
			} else {
			*/
				String errorMsg = 
					"Unable to load config and start initialization : " + e.getMessage(); 
				LOG.error(errorMsg);
				throw new ChannelConfigException(errorMsg, e);
			/*}*/

		}
	}
	
    /**
     * Initializes associations between contexts and managed category profiles.
     * defined in the channel config in channel
     * @param channel of the initialization
     * @throws ManagedCategoryProfileNotFoundException 
     * @throws ManagedCategoryProfileNotFoundException 
     */
	private synchronized void initContextManagedCategoryProfilesLinks(final Channel channel) 
		throws ManagedCategoryProfileNotFoundException {
    	if (LOG.isDebugEnabled()) {
    		LOG.debug("initContextManagedCategoryProfilesLinks()");
    	}
    	
    	Set<String> set = contextsHash.keySet();
    	Iterator<String> iterator = set.iterator();
    	while (iterator.hasNext()) {
    		String id = iterator.next();
    		Context c = contextsHash.get(id);
    		c.initManagedCategoryProfiles(channel);
    	}
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
			MappingFile.getInstance(mappingFilePath, defaultTimeOut);
			MappingFile.getMappingFile();
			/* Reset channel properties loaded from config */
			resetMappingFileProperties();
				
			/* Loading Mappings */
			MappingFile.loadMappings(this);
			
			/* Initialize hashs mapping if channel */
			MappingFile.initChannelHashMappings(this);

			if (!mappingsLoaded) {
				mappingsLoaded = true;
			}
			LOG.info("The mapping is loaded (file " + MappingFile.getMappingFilePath() + ") in channel");
			
		} catch (MappingFileException e) {
			if (mappingsLoaded) {
				LOG.error("unable to load new mappings : "+e.getMessage());
			} else {
				String errorMsg = "Unable to load mappings and start initialization : "
					+ e.getMessage(); 
				LOG.error(errorMsg);
				throw new MappingFileException(errorMsg);
			}
		}
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
	 * It takes it from the ThreadLocal if exists, else, it call getFreshUserProfile
	 * @param userId : identifient of the user profile
	 * @return the user profile
	 */
//	public UserProfile getUserProfile(final String userId) {
//		if (LOG.isDebugEnabled()) {
//			LOG.debug("getUserProfile(" + userId + ")");
//		}
//		if (userId == null) {
//			// TODO (RB <-- GB) Pourras tu noter dans le msg, le lieu où se configure le userId ? Merci
//			String mes = "userId not found. Check configuration.";
//			LOG.error(mes);
//			throw new ConfigException(mes);
//		}
//		UserProfile userProfile = UserProfileThreadLocal.get();
//		if (userProfile == null) {
//			userProfile = getFreshUserProfile(userId);
//		} 
//		return userProfile;
//	}
	
	/**
	 * return the context identified by "contextId".
	 * if it is defined in channel
	 * reload the config if needed (ttl expired)
	 * @param contextId
	 * @return  the context identified by "contextId"
	 * @throws ContextNotFoundException 
	 */
	protected Context getContext(final String contextId) throws ContextNotFoundException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("getContext(" + contextId + ")");
		}
		String cacheKey = "CHANNEL_CONFIG:" + configFilePath;
		Element element = cache.get(cacheKey);
		if (element == null) { 
	
			try {
				loadConfigIfNeeded();
			} catch (ChannelConfigException e) {
				String errorMsg = "Context " + contextId + " is not defined in channel because channel is not loaded";
				LOG.error(errorMsg);
				throw new ContextNotFoundException(errorMsg);
			} catch (MappingFileException e) {
				String errorMsg = "Context " + contextId + " is not defined in channel because channel is not loaded";
				LOG.error(errorMsg);
				throw new ContextNotFoundException(errorMsg);
			} catch (ManagedCategoryProfileNotFoundException e) {
				String errorMsg = "Context " + contextId + " is not defined in channel because channel is not loaded";
				LOG.error(errorMsg);
				throw new ContextNotFoundException(errorMsg);
			}
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
	 * @throws ManagedCategoryProfileNotFoundException
	 * @throws ChannelConfigException
	 * @throws MappingFileException
	 */
	private synchronized void loadConfigIfNeeded() throws ManagedCategoryProfileNotFoundException, 
		ChannelConfigException, MappingFileException {
		String cacheKey = "CHANNEL_CONFIG:" + configFilePath;
		Element element = cache.get(cacheKey);
		if (element == null) { 
			// channel config is not in cache : read and put in cache
			loadConfig();
			loadMappingFile();
			cache.put(new Element(cacheKey, cacheKey));
			Element e = cache.get(cacheKey);
			int ttl = DomainTools.getConfigTtl();
			e.setTimeToLive(ttl);
		} else {
			LOG.debug("The channel config is already in cache : "
					+ " Ttl: " + String.valueOf(element.getTimeToLive()));
		}
	}

	/**
	 * @param contextId id of the context
	 * @return true if the context is defined in this Channel
	 * @throws ContextNotFoundException 
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
		Mapping ret = null;
		if (LOG.isDebugEnabled()) {
			LOG.debug("getMappingBySourceURL(" + sourceURL + ")");
		}
		for (String mappingURL  : mappingHashBySourceURL.keySet()) {
			if (sourceURL.startsWith(mappingURL)) {
				ret = mappingHashBySourceURL.get(mappingURL);
				//exit at first mapping corresponding to the beginning of sourceURL
				return ret;
			}
		}
		return ret;
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

	/**
	 * @param contextString
	 */
	public void setContextString(String contextString) {
		this.contextString = contextString;
	}

	/**
	 * @param dummyTtl
	 */
	public void setDummyTtl(int dummyTtl) {
		this.dummyTtl = dummyTtl;
	}

	/**
	 * @param defaultTtl
	 */
	public void setDefaultTtl(int defaultTtl) {
		this.defaultTtl = defaultTtl;
	}

	/**
	 * @param defaultTimeOut
	 */
	public void setDefaultTimeOut(int defaultTimeOut) {
		this.defaultTimeOut = defaultTimeOut;
	}

	/**
	 * @param maxTreeSize
	 */
	public void setMaxTreeSize(int maxTreeSize) {
		this.maxTreeSize = maxTreeSize;
	}

	/**
	 * @param defaultTreeSize
	 */
	public void setDefaultTreeSize(int defaultTreeSize) {
		this.defaultTreeSize = defaultTreeSize;
	}

	/**
	 * @param configTtl
	 */
	public void setConfigTtl(int configTtl) {
		this.configTtl = configTtl;
	}

	/**
	 * set the default cacheName.
	 */
	protected void setDefaultCacheName() {
		this.cacheName = DEFAULT_CACHE_NAME;
	}

	/**
	 * @param cacheName the cacheName to set
	 */
	public void setCacheName(final String cacheName) {
		this.cacheName = cacheName;
	}

	/**
	 * @param cacheManager the cacheManager to set
	 */
	public void setCacheManager(final CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	public String getViewDef() {
		return viewDef;
	}

	public void setViewDef(String viewDef) {
		this.viewDef = viewDef;
	}

	public String getNbreArticle() {
		return nbreArticle;
	}

	public void setNbreArticle(String nbreArticle) {
		this.nbreArticle = nbreArticle;
	}
	
	
	
}
