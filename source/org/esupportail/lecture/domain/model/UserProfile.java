package org.esupportail.lecture.domain.model;


import java.util.Hashtable;



/**
 * Class where are defined user profile (and customizations ...)
 * @author gbouteil
 *
 */
public class UserProfile {
	
	/*
	 ************************** PROPERTIES *********************************/	
	
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
	/*
	 *************************** METHODS ************************************/

	protected void addContext(Context context){
		// TODO a voir 
		// v�rifier que le context n'est pas d�j� d�fini pour le user
		CustomContext customContext = new CustomContext();
		customContext.setContext(context);
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
