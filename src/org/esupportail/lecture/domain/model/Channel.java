/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;


import java.util.*;

//import org.springframework.beans.factory.InitializingBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.dao.DaoService;
import org.esupportail.lecture.domain.DomainTools;
import org.esupportail.lecture.exceptions.*;
import org.springframework.beans.factory.InitializingBean;
/**
 * The "lecture" channel : main domain model class
 * @author gbouteil
 */
public class Channel implements InitializingBean {

	/*
	 ************************** PROPERTIES *********************************/	
	
	/**
	 * Log instance 
	 */
	protected static final Log log = LogFactory.getLog(Channel.class); 

// 	A retirer si inutile
//	/**
//	 * Channel configuration from xml file.
//	 */
//	private ChannelConfig config; 
	

	/**
     * Hashtable of contexts defined in the channel, indexed by their ids.
     */
	private Hashtable<String,Context> contextsHash;
	
	/**
	 * Hashtable of ManagedCategoryProfiles defined in the chanel, indexed by their Id.
	 */
	private Hashtable<String,ManagedCategoryProfile> managedCategoryProfilesHash;
	
	/**
	 * The mapping File from xml file.
	 */
	private MappingFile mappingFile;
	
	/**
	 * List of mappings defined by the mapping file.
	 */
	private List<Mapping> mappingList;
	
	/**
	 * Hash to access mappings by dtd
	 */	
	private Hashtable<String,Mapping> mappingHashByDtd;
	
	/**
	 * Hash to access mappings by xmlns
	 */	
	private Hashtable<String,Mapping> mappingHashByXmlns;
	
	/**
	 * Hash to access mappings by xmlType.
	 */	
	private Hashtable<String,Mapping> mappingHashByXmlType;
	
	/**
	 * Hash to access mappings by root element.
	 */	
	private Hashtable<String,Mapping> mappingHashByRootElement;

	/**
	 * Hash to access mappings by sourceURL.
	 */	
	private Hashtable<String,Mapping> mappingHashBySourceURL;
	
	/**
	 * configLoaded = true if channel config has ever been loaded in channel
	 */
	private boolean configLoaded = false;
	
	/**
	 * mappingsLoaded = true if channel config has ever been loaded in channel
	 */
	private boolean mappingsLoaded = false;
	
	/**
	 * access to dao services
	 */
	private DaoService daoService;
	
	/*
	 ************************** Initialization ************************************/
	

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		startup();
		DomainTools.setChannel(this);
		DomainTools.setDaoService(daoService);
		
	}

	
	/**
	 * Methods call to load the config and mapping file 
	 * if needed (when files are modified from last loading)
	 * @throws FatalException
	 */
	public void startup() {
		if (log.isDebugEnabled()){
			log.debug("startup()");
		}
		loadConfig();
		loadMappingFile();
	}
	
	/**
	 * Load config file if needed (using ChannelConfig), containing contexts and managed category profiles definition.
	 * Initialize these elements.
	 * @see org.esupportail.lecture.domain.model.ChannelConfig#getInstance()
	 * @exception FatalException
	 */
	public void loadConfig()throws FatalException {
		if (log.isDebugEnabled()){
			log.debug("loadConfig()");
		}
		try {
			ChannelConfig config = (ChannelConfig)ChannelConfig.getInstance();
			// TODO plus tard 
			// - utiliser l'objet config pour appeler les méthodes après (reset ...)
			// 		et faire une classe FileToLoad avec ces méthodes en non static
			// - charger la config via un DAO ?
		} catch (WarningException w) {
			log.warn(w.getMessage());
			
		} catch (ErrorException e) {
			if (configLoaded) {
				log.error("loadConfig :: unable to load new config : "+e.getMessage());
			} else {
				log.fatal("loadConfig :: unable to load config and start initialization : "+e.getMessage());
				throw new FatalException();
			}
		}
		
		if (ChannelConfig.isModified()){
			/* Reset channel properties loaded from config */
			resetChannelConfigProperties();
			
			/*Loading user attributes name for user id */
			ChannelConfig.loadUserId();
			
			/* Loading managed category profiles */
			ChannelConfig.loadManagedCategoryProfiles(this);
		
			
			
			/* Loading Contexts */
			ChannelConfig.loadContexts(this);
		
			/* Initialize Contexts and ManagedCategoryProfiles links */
			ChannelConfig.initContextManagedCategoryProfilesLinks(this);
			
		}
		if (!configLoaded){
			configLoaded = true;
		}
	}
	
	/**
	 * Initialize every channel properties that are set up by loading channel configuration file
	 */
	private void resetChannelConfigProperties(){
		if (log.isDebugEnabled()){
			log.debug("resetChannelConfigProperties()");
		}
		
		// TODO UserAttributes.init();
		contextsHash = new Hashtable<String,Context>();
		managedCategoryProfilesHash = new Hashtable<String,ManagedCategoryProfile>();
	}
	
	/**
	 * Load mapping file if needed (using MappingFile), containing list of mappings used by the channel.
	 * Initialize these elements.
	 * @see org.esupportail.lecture.domain.model.MappingFile#getInstance()
	 * @exception FatalException
	 */	
	public void loadMappingFile() throws FatalException {
		if (log.isDebugEnabled()){
			log.debug("loadMappingFile()");
		}
		
		try {
			mappingFile = MappingFile.getInstance();
			
		} catch (WarningException w) {
			log.warn(w.getMessage());
			
		} catch (ErrorException e) {
			if (mappingsLoaded) {
				log.error("loadMappingFile :: unable to load new mappings : "+e.getMessage());
			} else {
				log.fatal("loadMappingFile :: unable to load mappings and start initialization : "+e.getMessage());
				throw new FatalException();
			}
		}
		
		if (MappingFile.isModified()){
			/* Reset channel properties loaded from config */
			resetMappingFileProperties();
				
			/* Loading Mappings */
			MappingFile.loadMappings(this);
			
			/* Initialize hashs mapping if channel */
			MappingFile.initChannelHashMappings(this);

		}
		if (!configLoaded){
			configLoaded = true;
		}
		
		
	}

	/**
	 * Initialize every channel properties that are set up by loading mapping file
	 */
	protected void resetMappingFileProperties(){
		if (log.isDebugEnabled()){
			log.debug("resetMappingFileProperties()");
		}
		mappingList = new ArrayList<Mapping>();
		mappingHashByDtd = new Hashtable<String,Mapping>();
		mappingHashByXmlns = new Hashtable<String,Mapping>();
		mappingHashByXmlType = new Hashtable<String,Mapping>();
		mappingHashByRootElement = new Hashtable<String,Mapping>();
		mappingHashBySourceURL= new Hashtable<String,Mapping>();
	}

	
	/*
	 *************************** METHODS ************************************/

	/* user profiles */
	
	/**
	 * return the user profile identified by "userId". 
	 * It take it from the dao if exists, else, it create a user profile
	 * @param userId : identifient of the user profile
	 * @return the user profile
	 */
	public UserProfile getUserProfile(String userId){
	
		UserProfile userProfile = DomainTools.getDaoService().getUserProfile(userId);
		
		if (userProfile == null){
			userProfile = new UserProfile(userId);
			DomainTools.getDaoService().addUserProfile(userProfile);
		}
		return userProfile;
	}
	
	/* Contexts */
	
	/**
	 * return the context identified by "contextId".
	 * The context is defiend in channel config if exists
	 * @param contextId
	 * @return  the context identified by "contextId"
	 */
	public Context getContext(String contextId) {
		Context context = getContextById(contextId);
		return context;
	}
	
	/* see later */
	
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

	/* ************************** ACCESSORS ********************************* */
	 
//  A retirer si inutile
//	public ChannelConfig getChannelConfig(){
//		return config;
//	}
//	public void setChannelConfig (ChannelConfig config){
//		this.config = config;
//	}
	
	/* contextsHash */
	
	/**
	 * Returns a hashtable of contexts, indexed by their ids
	 * @return contextsHash
	 * @see Channel#contextsHash
	 */
	public Hashtable<String,Context> getContextsHash() {
		return contextsHash;
	}
  
	/**
	 * Add a context to the hashtable of contexts, indexed by its id
	 * @param c context to add
	 * @see Channel#contextsHash
	 */
	protected void addContext(Context c) {
		this.contextsHash.put(c.getId(),c);
	}	

	/**
	 * Return the context identified "id"
	 * @param id 
	 * @return a context
	 */
	protected Context getContextById(String id){
		return contextsHash.get(id);
	}
	
	/* ManagedCategoryProfilesHash */
	
	/**
	 * Returns a hashtable of ManagedCategoryProfile, indexed by their ids
	 * @return managedCategoryProfilesHash
	 * @see Channel#managedCategoryProfilesHash
	 */
	public Hashtable<String,ManagedCategoryProfile> getManagedCategoryProfilesHash() {
		return managedCategoryProfilesHash;
	}
	
	/**
	 * Set Hashtable of managedCategoryProfiles, indexed by their ids
	 * @param managedCategoryProfilesHash
	 * @see Channel#managedCategoryProfilesHash
	 */
	public void setManagedCategoryProfilesHash(Hashtable<String,ManagedCategoryProfile> managedCategoryProfilesHash) {
		this.managedCategoryProfilesHash = managedCategoryProfilesHash;
	}

	/**
	 * Returns the managed category profile by giving its Id.
	 * @param s the managed category profile Id
	 * @return managedCategoryProfilesHash
	 * @see Channel#managedCategoryProfilesHash
	 */
	protected ManagedCategoryProfile getManagedCategoryProfile(String s){
		return managedCategoryProfilesHash.get(s);
	}
	
	/**
	 * Add a managed category profile to the hash(indexed by their id) of managed category profiles defined in the channel.
	 * @param m the managed category profile
	 * @see Channel#managedCategoryProfilesHash
	 */
	public void addManagedCategoryProfile(ManagedCategoryProfile m) {
		this.managedCategoryProfilesHash.put(m.getId(),m);
	}

	/* Mappings */
	
//  A retirer si inutile	
//	public void setMappingFile(MappingFile m){
//		this.mappingFile = m;
//	}
//	public MappingFile getMappingFile(){
//		return this.mappingFile;
//	}
	
	/**
	 * Returns the list of mappings defined in the channel.
	 * @return mappingList
	 * @see Channel#mappingList
	 */
	protected List<Mapping> getMappingList() {
		return mappingList;
	}
	
	/**
	 * Sets a mappings list in the channel
	 * @param mappingList
	 */
	public void setMappingList(List<Mapping> mappingList) {
		this.mappingList = mappingList;
	}
	
	/**
	 * Add a mapping to the list of mappings defined in the channel.
	 * @param m the mapping to add
	 * @see Channel#mappingList
	 */
	protected void addMapping(Mapping m){
		this.mappingList.add(m);
	}
	
//  A retirer si inutile	
//	public Hashtable<String,Mapping> getMappingHashByDtd() {
//		return mappingHashByDtd;
//	}
//	public void setMappingHashByDtd(Hashtable<String,Mapping> mappingHashByDtd) {
//		this.mappingHashByDtd = mappingHashByDtd;
//	}
	
	/**
	 * Add a mapping to the hash of mappings indexed by its dtd, defined in the channel
	 * @param m the mapping to add
	 * @see Channel#mappingHashByDtd
	 */
	protected void addMappingByDtd(Mapping m) {
		this.mappingHashByDtd.put(m.getDtd(),m);
	}
	
	/**
	 * @param dtd
	 * @return mapping for this dtd
	 */
	protected Mapping getMappingByDtd(String dtd){
		return mappingHashByDtd.get(dtd);
	}
	

	/**
	 * Add a mapping to the hash of mappings indexed by its xmlns, defined in the channel
	 * @param m the mapping to add
	 * @see Channel#mappingHashByXmlns
	 */
	protected void addMappingByXmlns(Mapping m) {
		this.mappingHashByXmlns.put(m.getXmlns(),m);
	}	
	/**
	 * @param xmlns
	 * @return mapping for this xmlns
	 */
	protected Mapping getMappingByXmlns(String xmlns){
		return mappingHashByXmlns.get(xmlns);
	}
	

	/**
	 * Add a mapping to the hash of mappings indexed by its xmlType, defined in the channel
	 * @param m the mapping to add
	 * @see Channel#mappingHashByXmlType
	 */
	protected void addMappingByXmlType(Mapping m) {
		this.mappingHashByXmlType.put(m.getXmlType(),m);
	}	
	

	/**
	 * @param xmlType
	 * @return mapping for this xmlType
	 */
	protected Mapping getMappingByXmlType(String xmlType){
		return mappingHashByXmlType.get(xmlType);
	}

	/**
	 * Add a mapping to the hash of mappings indexed by its RootElement, defined in the channel
	 * @param m the mapping to add
	 * @see Channel#mappingHashByRootElement
	 */
	protected void addMappingByRootElement(Mapping m) {
		this.mappingHashByRootElement.put(m.getRootElement(), m);
	}

	/**
	 * @param rootElement
	 * @return mapping for this rootElement
	 */
	protected Mapping getMappingByRootElement(String rootElement){
		return mappingHashByRootElement.get(rootElement);
	}

	/**
	 * Add a mapping to the hash of mappings indexed by its sourceURL, defined in the channel
	 * @param m
	 * @see Channel#mappingHashBySourceURL
	 */
	public void addMappingBySourceURL(Mapping m) {
		this.mappingHashBySourceURL.put(m.getSourceURL(), m);
	}

	/**
	 * @param sourceURL
	 * @return mapping for this sourceURL
	 */
	protected Mapping getMappingBySourceURL(String sourceURL){
		return mappingHashBySourceURL.get(sourceURL);
	}

	/**
	 * @return dosService
	 */
	public DaoService getDaoService() {
		return daoService;
	}


	/**
	 * set DaoService
	 * @param daoService
	 */
	public void setDaoService(DaoService daoService) {
		this.daoService = daoService;
	}



}
