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
import org.esupportail.lecture.utils.exception.*;
/**
 * The "lecture" channel
 * @author gbouteil
 */
public class Channel  {

/* ************************** PROPERTIES ******************************** */	
	/**
	 * Log instance 
	 */
	protected static final Log log = LogFactory.getLog(Channel.class); 
	
	/**
	 * Channel configuration from xml file.
	 */
	private ChannelConfig config; 
	
	/**
     * Set of contexts defined in the channel.
     */
	private Set<Context> contexts;
	
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
	
	private boolean configLoaded = false;
	
	private boolean mappingsLoaded = false;
	
	// Utile plus tard
//	/**
//	 * User Profiles connected to the chanel
//	 */
//	private Set<UserProfile> userProfiles = new HAshSet<UserProfile>();;

	
/* ************************** Initialization *********************************** */
	
	/**
	 * Methods call to load the config and mapping file 
	 * if needed (when files are modified from last loading)
	 * @throws MyException
	 * @throws FatalException
	 */

	public void startup() throws FatalException, MyException {
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
			// TODO plustard : utiliser l'objet config pour appeler les méthodes après (reset ...)
			// et faire une classe FileToLoad avec ces méthodes en non static
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
		contexts = new HashSet<Context>();
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
	}

	
/* ************************** METHODS *********************************** */

	/**
	 * Return a string containing channel content : mapping file, contexts, managed category profiles,
	 * xslt mappings, hash mappings by dtd, Hash mappings by xmlns,Hash mappings by xmlType
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String string = "";
			
		/* mappingFile */
		string += "***************** Mapping File : \n\n";
		string += mappingFile.toString();
		string += "\n";
		
//		/* Contexts */ 
		string += "***************** Contexts : \n\n";
		string += contexts.toString();
	    string += "\n";
//				
//		/* Managed categories profiles */
		string += "***************** Managed categories profiles : \n\n";
		string += managedCategoryProfilesHash.toString();
	    string += "\n";
//		
		/* Xslt mappings*/
		string += "***************** Xslt mappings : \n\n";
		string += mappingList.toString();
		string += "\n";
		
		/* Hash to access mappings by dtd */
		string += "***************** Hash mappings by dtd : \n\n";
		string += mappingHashByDtd.toString();
		string += "\n";
				
		/* Hash to access mappings by xmlns */
		string += "***************** Hash mappings by xmlns : \n\n";
		string += mappingHashByXmlns.toString();      
	    string += "\n";
		
		/* Hash to access mappings by xmlType */
		string += "***************** Hash mappings by xmlType : \n\n";
		string += mappingHashByXmlType.toString();      
	    string += "\n";
		
//		/* User Profiles connected to the chanel */
//		string += "***************** User profiles : \n\n";
//		string += " later ...";
//        string += "\n";
		
        return string;
	}		
	/* ************************** ACCESSORS ********************************* */
	 
//  TODO A retirer si inutile
//	public ChannelConfig getChannelConfig(){
//		return config;
//	}
//	public void setChannelConfig (ChannelConfig config){
//		this.config = config;
//	}
	
	/**
	 * Returns a set of contexts defined in the channel
	 * @return contexts
	 * @see Channel#contexts
	 */
	public Set<Context> getContexts() {
		return contexts;
	}

//  TODO A retirer si inutile
//	protected void setContexts(Set<Context> contexts) {
//		this.contexts = contexts;
//	}	
	
	/**
	 * Add a context to the set of contexts defined in the channel.
	 * @param c context to add
	 * @see Channel#contexts
	 */
	protected void addContext(Context c) {
		this.contexts.add(c);
	}	

//  TODO A retirer si inutile	
//	public Hashtable<String,ManagedCategoryProfile> getManagedCategoryProfilesHash() {
//		return managedCategoryProfilesHash;
//	}
//	public void setManagedCategoryProfiles(Hashtable<String,ManagedCategoryProfile> managedCategoryProfilesHash) {
//		this.managedCategoryProfilesHash = managedCategoryProfilesHash;
//	}

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

//  TODO A retirer si inutile	
//	public void setMappingFile(MappingFile m){
//		this.mappingFile = m;
//	}
//	public MappingFile getMappingFile(){
//		return this.mappingFile;
//	}
	
	/**
	 * Returns a list of mappings defined in the channel.
	 * @return mappingList
	 * @see Channel#mappingList
	 */
	protected List<Mapping> getMappingList() {
		return mappingList;
	}
	

	/**
	 * Sets the mappings list in the channel
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
	
//  TODO A retirer si inutile	
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
//  TODO A retirer si inutile		
//	public Hashtable<String,Mapping> getMappingHashByXmlns() {
//		return mappingHashByXmlns;
//	}
//	public void setMappingHashByXmlns(Hashtable<String,Mapping> mappingHashByXmlns) {
//		this.mappingHashByXmlns = mappingHashByXmlns;
//	}	

	/**
	 * Add a mapping to the hash of mappings indexed by its xmlns, defined in the channel
	 * @param m the mapping to add
	 * @see Channel#mappingHashByXmlns
	 */
	protected void addMappingByXmlns(Mapping m) {
		this.mappingHashByXmlns.put(m.getXmlns(),m);
	}	
	
//  TODO A retirer si inutile			
//	public Hashtable<String,Mapping> getMappingHashByXmlType() {
//		return mappingHashByXmlType;
//	}
//	public void setMappingHashByXmlType(Hashtable<String,Mapping> mappingHashByXmlType) {
//		this.mappingHashByXmlType = mappingHashByXmlType;
//	}	
	
	/**
	 * Add a mapping to the hash of mappings indexed by its xmlType, defined in the channel
	 * @param m the mapping to add
	 * @see Channel#mappingHashByXmlType
	 */
	protected void addMappingByXmlType(Mapping m) {
		this.mappingHashByXmlType.put(m.getXmlType(),m);
	}	
	
// utile plus tard	
/*	public Set getUserProfiles() {
		return userProfiles;
	}
	public void setUserProfiles(Set userProfiles) {
		this.userProfiles = userProfiles;
	}
*/
		

}
