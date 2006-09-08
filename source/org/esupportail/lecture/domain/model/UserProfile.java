package org.esupportail.lecture.domain.model;


import java.util.Collection;
import java.util.Hashtable;
import java.util.Set;


/**
 * @uml.dependency  supplier="SingleSource"
 */
public class UserProfile {
	
	
	/**
	 * Id of the user
	 */
	private String userId;
	
	
	/**
	 * Name of the user 
	 */
	private String name;

	/**
	 * Hashtable of CustomContexts defined for the user, indexed by their Id.
	 */
	private Hashtable<String,CustomContext> customContexts;
	
	protected void addContext(Context context){
		// TODO vérifier que le context n'est pas déjà défini pour le user
		CustomContext customContext = new CustomContext();
		customContext.setContext(context);
	}

	/**
	 * @return Returns the name.
	 */
	protected String getName() {
		return name;
	}

	/**
	 * @param name The name to set.
	 */
	protected void setName(String name) {
		this.name = name;
	}

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
