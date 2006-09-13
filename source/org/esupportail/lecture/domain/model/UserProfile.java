package org.esupportail.lecture.domain.model;


import java.util.Hashtable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



/**
 * Class where are defined user profile (and customizations ...)
 * @author gbouteil
 *
 */
public class UserProfile {
	
	/*
	 ************************** PROPERTIES *********************************/	
	
	/**
	 * Log instance
	 */
	protected static final Log log = LogFactory.getLog(UserProfile.class);

	
	/**
	 * Id of the user, get from portlet request by USER_ID, defined in the channel config
	 * @see UserAttributes#USER_ID
	 * @see ChannelConfig#loadUserId()
	 */
	private String userId;
	
	/**
	 * Hashtable of CustomContexts defined for the user, indexed by their Id.
	 */
	private Hashtable<String,CustomContext> customContexts;

	/*
	 ************************** Initialization ************************************/
	
	protected UserProfile(){
		customContexts = new Hashtable<String,CustomContext>();
	}
	
	/*
	 *************************** METHODS ************************************/

	
	/**
	 * Return the customContext identified by "id" if exists. Return null else
	 * @param id identifier of the customContext
	 * @return customContext (or null)
	 */
	public CustomContext getCustomContext(String id){
		//TODO acces dao
		CustomContext customContext = customContexts.get(id);
		if (customContext == null){
			customContext = new CustomContext();
			customContext.setContextId(id);
			addCustomContext(customContext);
			//TODO ajout en base
		}
		return customContext;
	}
	
	protected void addCustomContext(CustomContext customContext){
		// TODO a voir, acces dao
		// vérifier que le context n'est pas déjà défini pour le user
		customContexts.put(customContext.getContextId(),customContext);
	}
	
	/* ************************** ACCESSORS ********************************* */

	


	/**
	 * @return Returns the customContexts.
	 */
	public Hashtable<String, CustomContext> getCustomContexts() {
		return customContexts;
	}

	/**
	 * @param customContexts The customContexts to set.
	 */
	public void setCustomContexts(Hashtable<String, CustomContext> customContexts) {
		this.customContexts = customContexts;
	}

	/**
	 * @return Returns the userId.
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId The userId to set.
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

//	/**
//	 * @uml.property  name="customManagedCategories"
//	 * @uml.associationEnd  multiplicity="(0 -1)" aggregation="composite" inverse="userProfile:org.esupportail.lecture.domain.model.CustomManagedCategory"
//	 */
//	private Collection customManagedCategories;
//
//	/**
//	 * @uml.property  name="customManagedSources"
//	 * @uml.associationEnd  multiplicity="(0 -1)" aggregation="composite" inverse="userProfile:org.esupportail.lecture.domain.model.CustomManagedSource"
//	 */
//	private Collection customManagedSources;

	

}
