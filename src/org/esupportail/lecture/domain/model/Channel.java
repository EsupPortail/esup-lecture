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
import org.esupportail.commons.exceptions.ConfigException;
import org.esupportail.lecture.dao.DaoService;
import org.esupportail.lecture.domain.DomainTools;
import org.esupportail.lecture.domain.ExternalService;
import org.esupportail.lecture.exceptions.*;
import org.esupportail.lecture.exceptions.domain.ContextNotFoundException;
import org.esupportail.lecture.exceptions.domain.ManagedCategoryProfileNotFoundException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
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

	/* channel's elements */
	
	/**
     * Hashtable of contexts defined in the channel, indexed by their ids.
     */
	private Hashtable<String,Context> contextsHash;
	
	/**
	 * Hashtable of ManagedCategoryProfiles defined in the chanel, indexed by their Id.
	 */
	private Hashtable<String,ManagedCategoryProfile> managedCategoryProfilesHash;
	
	/**
	 * Hashtable of UserProfiles loaded from database, indexed by their userId.
	 */
	private Hashtable<String,UserProfile> userProfilesHash;
	
	/* mappings */
	
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

	/* config and mapping files */
	
	/**
	 * configLoaded = true if channel config has ever been loaded in channel
	 */
	private boolean configLoaded = false;
	
	/**
	 * mappingsLoaded = true if channel config has ever been loaded in channel
	 */
	private boolean mappingsLoaded = false;
	
	/**
	 * The mapping File from xml file.
	 */
	private MappingFile mappingFile;
	
	/**
	 * access to dao services
	 */
	private DaoService daoService;
	
	/**
	 * acces to external service
	 */
	private ExternalService externalService;
	

	
	/*
	 ************************** Initialization ************************************/

	/**
	 * default constructor
	 */
	public Channel(){
		userProfilesHash = new Hashtable<String,UserProfile>();
	}
	
	
	/**
	 * @throws ManagedCategoryProfileNotFoundException 
	 * @throws ContextNotFoundException 
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws ContextNotFoundException, ManagedCategoryProfileNotFoundException  {
		if (log.isDebugEnabled()){
			log.debug("afterPropertiesSet()");
		}
		Assert.notNull(daoService,"property daoService can not be null");
		Assert.notNull(externalService,"property externalService can not be null");
		startup();
		DomainTools.setChannel(this);
		DomainTools.setDaoService(daoService);
		DomainTools.setExternalService(externalService);
	}
	
	/**
	 * Methods call to load the config and mapping file 
	 * if needed (when files are modified from last loading)
	 * @throws ManagedCategoryProfileNotFoundException 
	 * @throws ContextNotFoundException 
	 */
	synchronized private void startup() throws ContextNotFoundException, ManagedCategoryProfileNotFoundException {
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
	 * @throws ManagedCategoryProfileNotFoundException 
	 * @throws ContextNotFoundException 
	 */
	synchronized private void loadConfig()throws ConfigException, ContextNotFoundException, ManagedCategoryProfileNotFoundException {
		if (log.isDebugEnabled()){
			log.debug("loadConfig()");
		}
		try {
			ChannelConfig config = (ChannelConfig)ChannelConfig.getInstance();
			// TODO (GB later)
			// - utiliser l'objet config pour appeler les méthodes après (reset ...)
			// 		et faire une classe FileToLoad avec ces méthodes en non static
			// - charger la config via un DAO ?
		} catch (WarningException w) {
			log.warn(w.getMessage());
			
		} catch (ConfigException e) {
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
	synchronized private void resetChannelConfigProperties(){
		if (log.isDebugEnabled()){
			log.debug("resetChannelConfigProperties()");
		}
		
		// TODO (GB later) UserAttributes.init();
		contextsHash = new Hashtable<String,Context>();
		managedCategoryProfilesHash = new Hashtable<String,ManagedCategoryProfile>();
	}
	
	/**
	 * Load mapping file if needed (using MappingFile), containing list of mappings used by the channel.
	 * Initialize these elements.
	 * @see org.esupportail.lecture.domain.model.MappingFile#getInstance()
	 * @exception FatalException
	 */	
	synchronized private void loadMappingFile() throws FatalException {
		if (log.isDebugEnabled()){
			log.debug("loadMappingFile()");
		}
		
		try {
			mappingFile = MappingFile.getInstance();
			
		} catch (WarningException w) {
			log.warn(w.getMessage());
			
		} catch (ConfigException e) {
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
		if (!mappingsLoaded){
			mappingsLoaded = true;
		}
	}

	/**
	 * Initialize every channel properties that are set up by loading mapping file
	 */
	synchronized private void resetMappingFileProperties(){
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

	/**
	 * return the user profile identified by "userId". 
	 * It take it from the dao if exists, else, it create a user profile
	 * @param userId : identifient of the user profile
	 * @return the user profile
	 */
	synchronized public UserProfile getUserProfile(String userId){
		if (log.isDebugEnabled()){
			log.debug("getUserProfile("+userId+")");
		}
	
		UserProfile userProfile = userProfilesHash.get(userId);
		if (userProfile == null) {
			userProfile = DomainTools.getDaoService().getUserProfile(userId);
		
			if (userProfile == null){
				userProfile = new UserProfile(userId);
				DomainTools.getDaoService().saveUserProfile(userProfile);
			}
			userProfilesHash.put(userId, userProfile);
		}
		return userProfile;
	}
	
	/**
	 * return the context identified by "contextId".
	 * The context is defiend in channel config if exists
	 * @param contextId
	 * @return  the context identified by "contextId"
	 * @throws ContextNotFoundException 
	 */
	public Context getContext(String contextId) throws ContextNotFoundException {
		if (log.isDebugEnabled()){
			log.debug("getContext("+contextId+")");
		}
		Context context = contextsHash.get(contextId);
		if (context == null) {
			throw new ContextNotFoundException("Context "+contextId+" is not defined in channel");
		}
		return context;
	}
	
	/**
	 * Add a context to the hashtable of contexts, indexed by its id
	 * @param c context to add
	 * @see Channel#contextsHash
	 */
	
	synchronized protected void addContext(Context c) {
		if (log.isDebugEnabled()){
			log.debug("addContext("+c.getId()+")");
		}
		this.contextsHash.put(c.getId(),c);
	}	


	/**
	 * Returns the managed category profile by giving its Id.
	 * @param id the managed category profile Id
	 * @return managedCategoryProfile
	 * @throws ManagedCategoryProfileNotFoundException 
	 * @see Channel#managedCategoryProfilesHash
	 */
	protected ManagedCategoryProfile getManagedCategoryProfile(String id) throws ManagedCategoryProfileNotFoundException{
		if (log.isDebugEnabled()){
			log.debug("getManagedCategoryProfile("+id+")");
		}
		ManagedCategoryProfile mcp = managedCategoryProfilesHash.get(id);
		if (mcp == null) {
			throw new ManagedCategoryProfileNotFoundException("ManagedCategoryProfile "+id+" is not defined in channel");
		}
		return mcp;
	}
	
	/**
	 * Add a managed category profile to the hash(indexed by their id) of managed category profiles defined in the channel.
	 * @param m the managed category profile
	 * @see Channel#managedCategoryProfilesHash
	 */
	synchronized protected void addManagedCategoryProfile(ManagedCategoryProfile m) {
		if (log.isDebugEnabled()){
			log.debug("addManagedCategoryProfile("+m.getId()+")");
		}
		this.managedCategoryProfilesHash.put(m.getId(),m);
	}
	
	/**
	 * Add a mapping to the list of mappings defined in the channel.
	 * @param m the mapping to add
	 * @see Channel#mappingList
	 */
	synchronized protected void addMapping(Mapping m){
		if (log.isDebugEnabled()){
			log.debug("addMapping("+m.getRootElement()+")");
		}
		this.mappingList.add(m);
	}
	
	/**
	 * Add a mapping to the hash of mappings indexed by its dtd, defined in the channel
	 * @param m the mapping to add
	 * @see Channel#mappingHashByDtd
	 */
	synchronized protected void addMappingByDtd(Mapping m) {
		if (log.isDebugEnabled()){
			log.debug("addMappingByDtd("+m.getRootElement()+")");
		}
		this.mappingHashByDtd.put(m.getDtd(),m);
	}
	
	/**
	 * @param dtd
	 * @return mapping for this dtd
	 */
	protected Mapping getMappingByDtd(String dtd){
		if (log.isDebugEnabled()){
			log.debug("getMappingByDtd("+dtd+")");
		}
		return mappingHashByDtd.get(dtd);
	}

	/**
	 * Add a mapping to the hash of mappings indexed by its xmlns, defined in the channel
	 * @param m the mapping to add
	 * @see Channel#mappingHashByXmlns
	 */
	synchronized protected void addMappingByXmlns(Mapping m) {
		if (log.isDebugEnabled()){
			log.debug("addMappingByXmlns("+m.getRootElement()+")");
		}
		this.mappingHashByXmlns.put(m.getXmlns(),m);
	}	
	/**
	 * @param xmlns
	 * @return mapping for this xmlns
	 */
	protected Mapping getMappingByXmlns(String xmlns){
		if (log.isDebugEnabled()){
			log.debug("getMappingByXmlns("+xmlns+")");
		}
		return mappingHashByXmlns.get(xmlns);
	}
	
	/**
	 * Add a mapping to the hash of mappings indexed by its xmlType, defined in the channel
	 * @param m the mapping to add
	 * @see Channel#mappingHashByXmlType
	 */
	synchronized protected void addMappingByXmlType(Mapping m) {
		if (log.isDebugEnabled()){
			log.debug("addMappingByXmlType("+m.getRootElement()+")");
		}
		this.mappingHashByXmlType.put(m.getXmlType(),m);
	}	

	/**
	 * @param xmlType
	 * @return mapping for this xmlType
	 */
	protected Mapping getMappingByXmlType(String xmlType){
		if (log.isDebugEnabled()){
			log.debug("getMappingByXmlType("+xmlType+")");
		}
		return mappingHashByXmlType.get(xmlType);
	}

	/**
	 * Add a mapping to the hash of mappings indexed by its RootElement, defined in the channel
	 * @param m the mapping to add
	 * @see Channel#mappingHashByRootElement
	 */
	synchronized protected void addMappingByRootElement(Mapping m) {
		if (log.isDebugEnabled()){
			log.debug("addMappingByRootElement("+m.getRootElement()+")");
		}
		this.mappingHashByRootElement.put(m.getRootElement(), m);
	}

	/**
	 * @param rootElement
	 * @return mapping for this rootElement
	 */
	protected Mapping getMappingByRootElement(String rootElement){
		if (log.isDebugEnabled()){
			log.debug("getMappingByRootElement("+rootElement+")");
		}
		return mappingHashByRootElement.get(rootElement);
	}

	/**
	 * Add a mapping to the hash of mappings indexed by its sourceURL, defined in the channel
	 * @param m
	 * @see Channel#mappingHashBySourceURL
	 */
	synchronized protected void addMappingBySourceURL(Mapping m) {
		if (log.isDebugEnabled()){
			log.debug("addMappingBySourceURL("+m.getRootElement()+")");
		}
		this.mappingHashBySourceURL.put(m.getSourceURL(), m);
	}

	/**
	 * @param sourceURL
	 * @return mapping for this sourceURL
	 */
	protected Mapping getMappingBySourceURL(String sourceURL){
		if (log.isDebugEnabled()){
			log.debug("getMappingBySourceURL("+sourceURL+")");
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

	/* ************************** ACCESSORS ********************************* */
	
	/**
	 * Returns a hashtable of contexts, indexed by their ids
	 * @return contextsHash
	 * @see Channel#contextsHash
	 */
	public Hashtable<String,Context> getContextsHash() {
		return contextsHash;
	}
  

	
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
	synchronized public void setManagedCategoryProfilesHash(Hashtable<String,ManagedCategoryProfile> managedCategoryProfilesHash) {
		this.managedCategoryProfilesHash = managedCategoryProfilesHash;
	}

	/**
	 * Returns a hashtable of UserProfile, indexed by their ids
	 * @return userProfilesHash
	 * @see Channel#userProfilesHash
	 */
	public Hashtable<String, UserProfile> getUserProfilesHash() {
		return userProfilesHash;
	}
	
	/**
	 * Set Hashtable of userProfiles, indexed by their ids
	 * @param userProfilesHash
	 * @see Channel#userProfilesHash
	 */
	synchronized public void setUserProfilesHash(Hashtable<String, UserProfile> userProfilesHash) {
		this.userProfilesHash = userProfilesHash;
	}

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
	synchronized public void setMappingList(List<Mapping> mappingList) {
		this.mappingList = mappingList;
	}


	/**
	 * @return daoService
	 */
	public DaoService getDaoService() {
		return daoService;
	}

	/**
	 * set DaoService
	 * @param daoService
	 */
	synchronized public void setDaoService(DaoService daoService) {
		this.daoService = daoService;
	}

	/**
	 * @return the external service
	 */
	public ExternalService getExternalService() {
		return externalService;
	}

	/**
	 * @param externalService
	 */
	public void setExternalService(ExternalService externalService) {
		this.externalService = externalService;
	}

}
