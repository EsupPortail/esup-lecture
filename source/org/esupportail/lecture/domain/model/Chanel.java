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
public class Chanel implements InitializingBean {
/* ************************** PROPERTIES ******************************** */	
	/**
	 * Logs
	 */
	protected static final Log log = LogFactory.getLog(Chanel.class); 
	
    /**
     * Contexts defined in the chanel
     */
	private List<Context> contexts;
	/**
	 * Managed category profiles defined in the chanel
	 */
	private List<ManagedCategoryProfile> managedCategoryProfiles;
	/**
	 * Xslt mappings defined in the chanel
	 */
	private List<Mapping> mappingList;
	/**
	 * hash to access mappings by dtd
	 */	
	private Hashtable<String,Mapping> mappingHashByDtd;
	/**
	 * hash to access mappings by xmlns
	 */	
	private Hashtable<String,Mapping> mappingHashByXmlns;
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

	public List<ManagedCategoryProfile> getManagedCategoryProfiles() {
		return managedCategoryProfiles;
	}
	public void setManagedCategoryProfiles(List<ManagedCategoryProfile> managedCategoryProfiles) {
		this.managedCategoryProfiles = managedCategoryProfiles;
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
	
	public Hashtable<String,Mapping> getMappingHashByXmlns() {
		return mappingHashByXmlns;
	}
	public void setMappingHashByXmlns(Hashtable<String,Mapping> mappingHashByXmlns) {
		this.mappingHashByXmlns = mappingHashByXmlns;
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
	 * method called by Spring for initialization bean
	 */
	public void afterPropertiesSet (){
		mappingHashByDtd = new Hashtable<String,Mapping>();
		mappingHashByXmlns = new Hashtable<String,Mapping>();
		Iterator iterator = mappingList.iterator();
		while (iterator.hasNext()) {
	        Mapping m = (Mapping)iterator.next();
	        String dtd = m.getDtd();
	        String xmlns = m.getXmlns();
	        if (dtd != null){
	        	mappingHashByDtd.put(m.getDtd(),m);
	        }
	        if (xmlns != null){
	        	mappingHashByXmlns.put(m.getXmlns(),m);
	        }
	    }
		iterator = contexts.iterator();
		while(iterator.hasNext()){
			Context c = (Context)iterator.next();
			c.initManagedCategoryProfiles();
			log.warn("Ca logue :"+ c.getName());
		}
		

	}	
	
/*	public void init(){
		Iterator iterator = contexts.iterator();
		while(iterator.hasNext()){
			Context c = (Context)iterator.next();
			c.initManagedCategoryProfiles();
		}
	}
	*/
/* ************************** METHODS *********************************** */

	public String toString() {
		String string = "";
			
		/* Contexts */ 
		string += "***************** Contexts : \n\n";
		string += contexts.toString();
	    string += "\n";
				
		/* Managed categories profiles */
		string += "***************** Managed categories profiles : \n\n";
		string += managedCategoryProfiles.toString();
	    string += "\n";
		
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
		
		
		/* User Profiles connected to the chanel */
		string += "***************** User profiles : \n\n";
		string += " later ...";
        string += "\n";
		
        return string;
	}		
			
			


	
	
	
	
	
	
/* *********************************************************************/	


	/**
		 */
		public void newUserSession(){
		
		}

			
			/**
			 */
			public void loadConfig(){
			
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
