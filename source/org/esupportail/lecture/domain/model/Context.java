package org.esupportail.lecture.domain.model;

import java.util.*;

import org.springframework.beans.factory.InitializingBean;


/**
 * 
 * @author gbouteil
 * 
 *
 */

public class Context implements InitializingBean {
/* ************************** PROPERTIES ******************************** */	
	/**
	 *  The context name 
	 */
	private String name = "";
	
	/**
	 * The context description
	 */
	private String description = "";
	
	/**
	 * The context id
	 */
	private int id;

	/**
	 * The context edit mode : not used for the moment
	 */
	private Editability edit;
	
	/**
	 * Managed category profiles available in this context.
	 */
	private List<ManagedCategoryProfile> managedCategoryProfilesList;
	
/* ************************** ACCESSORS ******************************** */	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public Editability getEdit() {
		return edit;
	}

	public void setEdit(Editability edit) {
		this.edit = edit;
	}

/*	public Hashtable getManagedCategoryProfilesHash() {
		return managedCategoryProfilesHash;
	}
	public void setManagedCategoryProfilesHash(Hashtable managedCategoryProfilesHash) {
		this.managedCategoryProfilesHash = managedCategoryProfilesHash;
	}
*/
	public List<ManagedCategoryProfile> getManagedCategoryProfilesList() {
		return managedCategoryProfilesList;
	}
	public void setManagedCategoryProfilesList(List<ManagedCategoryProfile> managedCategoryProfilesList) {
		this.managedCategoryProfilesList = managedCategoryProfilesList;
	}

/* ************************** Initialization *********************************** */
	public void afterPropertiesSet (){
		/* Connecting Managed category profiles and contexts */
/*		Iterator iterator=managedCategoryProfilesList.iterator();

		while (iterator.hasNext()) {
	        ManagedCategoryProfile m = (ManagedCategoryProfile)iterator.next();
	        m.addContext(this);    
		}
*/	}
	
/* ************************** METHODS ******************************** */

	public void initManagedCategoryProfiles (){
		/* Connecting Managed category profiles and contexts */
		Iterator iterator=managedCategoryProfilesList.iterator();

		while (iterator.hasNext()) {
	        ManagedCategoryProfile m = (ManagedCategoryProfile)iterator.next();
	        m.addContext(this);    
		}
	}
	
	
	public String toString(){
		
		String string = "";
	
	/* The context name */
		string += "	name : "+ name +"\n";
		
    /* The context description */
		string += "	description : "+ description +"\n";
	
	/* The context id */
		string += "	id : "+ id +"\n";

	/* The context edit mode : not used for the moment */
	// 	string += "	edit : "+ edit +"\n";;
	
	/* Managed category profiles available in this context */
		string += "	managedCategoryProfilesList : \n";
		Iterator iterator = managedCategoryProfilesList.iterator();
		for (ManagedCategoryProfile m = null; iterator.hasNext();) {
			m = (ManagedCategoryProfile)iterator.next();
			string += "          ("+ m.getId() + "," + m.getName()+")\n";
		}
		
		return string;
	}

}
