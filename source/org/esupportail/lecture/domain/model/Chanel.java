package org.esupportail.lecture.domain.model;


import java.util.*;
/**
 * 22.06.2006
 * @author gbouteil
 *
 * The "lecture" chanel
 */
public class Chanel {
/* ************************** PROPERTIES ******************************** */	
    /**
     * Contexts defined in the chanel
     */
	private Set<Context> contexts;
	/**
	 * Managed category profiles defined in the chanel
	 */
	private Set<ManagedCategoryProfile> managedCategoryProfiles;
	/**
	 * Xslt mappings defined in the chanel
	 */
	private Set<Mapping> mappings;
	/**
	 * User Profiles connected to the chanel
	 */
//	private Set<UserProfile> userProfiles;

	
/* ************************** ACCESSORS ********************************* */
	public Set<Context> getContexts() {
		return contexts;
	}
	public void setContexts(Set<Context> contexts) {
		this.contexts = contexts;
	}	

	public Set<ManagedCategoryProfile> getManagedCategoryProfiles() {
		return managedCategoryProfiles;
	}
	public void setManagedCategoryProfiles(Set<ManagedCategoryProfile> managedCategoryProfiles) {
		this.managedCategoryProfiles = managedCategoryProfiles;
	}

	public Set<Mapping> getMappings() {
		return mappings;
	}
	public void setMappings(Set<Mapping> mappings) {
		this.mappings = mappings;
	}

/*	public Set getUserProfiles() {
		return userProfiles;
	}
	public void setUserProfiles(Set userProfiles) {
		this.userProfiles = userProfiles;
	}
*/
	
/* ************************** METHODS *********************************** */




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
