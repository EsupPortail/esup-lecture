package org.esupportail.lecture.domain.model;


import java.util.*;

import org.springframework.beans.factory.InitializingBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 22.06.2006
 * @author gbouteil
 *
 * The "lecture" chanel
 */
public class Channel  {

/* ************************** PROPERTIES ******************************** */	
	/**
	 * Logs
	 */
	protected static final Log log = LogFactory.getLog(Channel.class); 
	
	/**
	 * Configuration
	 */
	private ChannelConfig config;
	
    /**
     * Contexts defined in the chanel
     */
	private Set<Context> contexts = new HashSet<Context>();
	/**
	 * HAsh of References id on Managed category profiles defined in the chanel
	 */
	private Hashtable<Integer,ManagedCategoryProfile> managedCategoryProfilesHash = new Hashtable<Integer,ManagedCategoryProfile>();
	/**
	 * The  mapping File (xslt)
	 */
	private MappingFile mappingFile;
	/**
	 * Xslt mappings defined in the chanel
	 */
	private List<Mapping> mappingList = new ArrayList<Mapping>();
	/**
	 * hash to access mappings by dtd
	 */	
	private Hashtable<String,Mapping> mappingHashByDtd = new Hashtable<String,Mapping>();
	/**
	 * hash to access mappings by xmlns
	 */	
	private Hashtable<String,Mapping> mappingHashByXmlns = new Hashtable<String,Mapping>();
	/**
	 * User Profiles connected to the chanel
	 */
//	private Set<UserProfile> userProfiles = new HAshSet<UserProfile>();;

	
/* ************************** ACCESSORS ********************************* */
	public ChannelConfig getChannelConfig(){
		return config;
	}
	public void setChannelConfig (ChannelConfig config){
		this.config = config;
	}
	
	public Set<Context> getContexts() {
		return contexts;
	}
	public void setContexts(Set<Context> contexts) {
		this.contexts = contexts;
	}	
	
	public void setContext(Context c) {
		this.contexts.add(c);
	}	

	public Hashtable<Integer,ManagedCategoryProfile> getManagedCategoryProfilesHash() {
		return managedCategoryProfilesHash;
	}
	public void setManagedCategoryProfiles(Hashtable<Integer,ManagedCategoryProfile> managedCategoryProfilesHash) {
		this.managedCategoryProfilesHash = managedCategoryProfilesHash;
	}

	public ManagedCategoryProfile getManagedCategoryProfile(int i){
		return managedCategoryProfilesHash.get(i);
	}
	public void setManagedCategoryProfile(ManagedCategoryProfile m) {
		this.managedCategoryProfilesHash.put(m.getId(),m);
	}
	
	public void setMappingFile(MappingFile m){
		this.mappingFile = m;
	}
	public MappingFile getMappingFile(){
		return this.mappingFile;
	}
	
	public List<Mapping> getMappingList() {
		return mappingList;
	}
	public void setMappingList(List<Mapping> mappingList) {
		this.mappingList = mappingList;
	}
	
	public void setMapping(Mapping m){
		this.mappingList.add(m);
	}
	
	public Hashtable<String,Mapping> getMappingHashByDtd() {
		return mappingHashByDtd;
	}
	public void setMappingHashByDtd(Hashtable<String,Mapping> mappingHashByDtd) {
		this.mappingHashByDtd = mappingHashByDtd;
	}
	public void setMappingByDtd(String s,Mapping m) {
		this.mappingHashByDtd.put(s,m);
	}
	
	public Hashtable<String,Mapping> getMappingHashByXmlns() {
		return mappingHashByXmlns;
	}
	public void setMappingHashByXmlns(Hashtable<String,Mapping> mappingHashByXmlns) {
		this.mappingHashByXmlns = mappingHashByXmlns;
	}	
	
	public void setMappingByXmlns(String s,Mapping m) {
		this.mappingHashByXmlns.put(s,m);
	}	
	
/*	public Set getUserProfiles() {
		return userProfiles;
	}
	public void setUserProfiles(Set userProfiles) {
		this.userProfiles = userProfiles;
	}
*/

/* ************************** Initialization *********************************** */
	/**
	 * Load config file, containing contexts and managed category profiles definition.
	 * Initialize these elements.
	 */
	public void loadConfig()throws Exception{
		
		// load config
		config = ChannelConfig.getInstance(this);
		
		// initialize category and contexts
		Iterator iterator = contexts.iterator();
		while(iterator.hasNext()){
			Context c = (Context)iterator.next();
			c.initManagedCategoryProfiles(this);
		}

		//TODO relier les contexts avec les profiles de category
		//TODO charger les mappings dans la mappingList
		//TODO faire le hash mapping by dtd
		//TODO fair le hash mapping by xmlns

		
	}
	
	
	
//	/**
//	 * method called by Spring for initialization bean
//	 */
//	public void afterPropertiesSet () throws Exception{
//		mappingHashByDtd = new Hashtable<String,Mapping>();
//		mappingHashByXmlns = new Hashtable<String,Mapping>();
//		Iterator iterator = mappingList.iterator();
//		while (iterator.hasNext()) {
//	        Mapping m = (Mapping)iterator.next();
//	        String dtd = m.getDtd();
//	        String xmlns = m.getXmlns();
//	        if (dtd != null){
//	        	mappingHashByDtd.put(m.getDtd(),m);
//	        }
//	        if (xmlns != null){
//	        	mappingHashByXmlns.put(m.getXmlns(),m);
//	        }
//	    }
//	}	
	
	
/* ************************** METHODS *********************************** */

	public String toString() {
		String string = "";
			
		/* mappingFile */
		string += "***************** Mapping File ***************";
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
//		
//		/* User Profiles connected to the chanel */
//		string += "***************** User profiles : \n\n";
//		string += " later ...";
//        string += "\n";
//		
        return string;
	}		
			
			


	
	
	
	
	
	
/* *********************************************************************/	


	/**
		 */
		public void newUserSession(){
		
		}

			

				
				/**
				 */
				public void loadManagedCategoryProfiles(){
				
				}



					
					/**
					 */
					public void initialize(){
					
					}

	

}
