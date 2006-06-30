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
	private List<Context> contexts = new ArrayList();
	/**
	 * HAsh of References id on Managed category profiles defined in the chanel
	 */
	private Hashtable<Integer,ManagedCategoryProfile> managedCategoryProfilesHash = new Hashtable();
	/**
	 * The  mapping File (xslt)
	 */
	private MappingFile mappingFile;
	/**
	 * Xslt mappings defined in the chanel
	 */
	private List<Mapping> mappingList = new ArrayList();
	/**
	 * hash to access mappings by dtd
	 */	
	private Hashtable<String,Mapping> mappingHashByDtd = new Hashtable();
	/**
	 * hash to access mappings by xmlns
	 */	
	private Hashtable<String,Mapping> mappingHashByXmlns = new Hashtable();
	/**
	 * User Profiles connected to the chanel
	 */
//	private List<UserProfile> userProfiles;

	
/* ************************** ACCESSORS ********************************* */
	public List<Context> getContexts() {
		return contexts;
	}
	public void setContexts(List<Context> contexts) {
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
	
/*	public List getUserProfiles() {
		return userProfiles;
	}
	public void setUserProfiles(List userProfiles) {
		this.userProfiles = userProfiles;
	}
*/

/* ************************** Initialization *********************************** */
	/**
	 * Load config file, containing contexts and managed category profiles definition.
	 * Initialize these elements.
	 */
	public void loadConfig()throws Exception{
		
		config = ChannelConfig.getInstance(this);
		log.error("OK");
//		contexts = config.listContexts;
//		Iterator iterator = contexts.iterator();
//		while(iterator.hasNext()){
//			Context c = (Context)iterator.next();
//			c.initManagedCategoryProfiles();
//			log.warn("Ca logue :"+ c.getName());
//		}
		
	}
	
	
	
	/**
	 * method called by Spring for initialization bean
	 */
	public void afterPropertiesSet () throws Exception{
		mappingHashByDtd = new Hashtable<String,Mapping>();
		mappingHashByXmlns = new Hashtable<String,Mapping>();
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
		
		
	

	}	
	
	
/* ************************** METHODS *********************************** */

	public String toString() {
		String string = "";
			
		/* mappingFile */
		string += "***************** Mapping File ***************";
		string += mappingFile.toString();
		string += "\n";
		
//		/* Contexts */ 
//		string += "***************** Contexts : \n\n";
//		string += contexts.toString();
//	    string += "\n";
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
