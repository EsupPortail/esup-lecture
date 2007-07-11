/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.ExternalService;
import org.esupportail.lecture.exceptions.domain.CategoryNotLoadedException;
import org.esupportail.lecture.exceptions.domain.CategoryTimeOutException;
import org.esupportail.lecture.exceptions.domain.ManagedCategoryProfileNotFoundException;

/**
 * Context element : it is the environnement context of an instance
 * of lecture channel. It displays category, leaded by categoryProfiles
 * @author gbouteil
 */

public class Context {
	
	/*
	 *************************** PROPERTIES ******************************** */
	/**
	 * Log instance
	 */
	protected static final Log log = LogFactory.getLog(Context.class);

	/**
	 * The context name
	 */
	private String name = "";

	/**
	 * The context description
	 */
	private String description = "";

	/**
	 * The context id
	 */
	private String id;

	// later 
//	/**
//	 * The context edit mode : not used for the moment
//	 */
//	private Editability edit;

	/**
	 * Managed category profiles available in this Context.
	 */
	private Set<ManagedCategoryProfile> managedCategoryProfilesSet = new HashSet<ManagedCategoryProfile>();

	/**
	 * Set of managed category profiles id available in this Context.
	 */
	private Set<String> refIdManagedCategoryProfilesSet = new HashSet<String>();

	/*
	 *************************** INIT *********************************/

	/**
	 * Initilizes associations with managed category profiles linked to this Context.
	 * This method needs channel because it provides links to managed cateories profiles
	 * @param channel channel where the context is defined 
	 * @throws ManagedCategoryProfileNotFoundException 
	 */
	synchronized protected void initManagedCategoryProfiles(Channel channel) throws ManagedCategoryProfileNotFoundException  {
		if (log.isDebugEnabled()){
			log.debug("id="+id+" - initManagedCategoryProfiles(channel)");
		}
		/* Connecting Managed category profiles and contexts */
		Iterator<String> iterator = refIdManagedCategoryProfilesSet.iterator();

		while (iterator.hasNext()) {
			String profileId = iterator.next();
			ManagedCategoryProfile mcp = channel.getManagedCategoryProfile(profileId);
			managedCategoryProfilesSet.add(mcp);
			mcp.addContext(this);
		}
	}

	/* 
	 *************************** METHODS ******************************** */

	/**
	 * Update the customContext linked to this Context.
	 * It sets up subscriptions of customContext on managedCategoryProfiles
	 * defined in this Context, according to managedCategory visibilities
	 * @param customContext customContext to update
	 * @param ex access to external service for visibility evaluation
	 */
	synchronized protected void updateCustom(CustomContext customContext, ExternalService ex){
		if (log.isDebugEnabled()) {
			log.debug("id=" + id + " - updateCustom(" + customContext.getElementId() + ",externalService)");
		}
		//TODO (GB later) optimise evaluation process (trustCategory + real loadding)
		
		// update for managedCategories defined in this context
		for (ManagedCategoryProfile mcp : managedCategoryProfilesSet) {
			try {
				mcp.updateCustomContext(customContext, ex);
			} catch (CategoryTimeOutException e) {
				log.error("Impossible to update CustomContext associated to context " + getId()
						+ " for managedCategoryProfile " + mcp.getId()
						+ " because the remote category is in Time Out", e);
			} catch (CategoryNotLoadedException e){
				log.error("Impossible to update CustomContext associated to context " + getId()
						+ " for managedCategoryProfile " + mcp.getId()
						+ " because it category is not loaded : very strange because a loadCategory has been called ...", e);
			}
		}
		// update for managedCategories not anymore in this context
		updateCustomForVanishedSubscriptions(customContext);
	}

	/**
	 * Update customContext for managedCategories not anymore in this context
	 * @param customContext
	 */
	synchronized private void updateCustomForVanishedSubscriptions(CustomContext customContext) {
		List<String> cids = new ArrayList<String>();
		for (String categoryId : customContext.getSubscriptions().keySet()){
			cids.add(categoryId);
		}
		for (String categoryId : cids) {
			if (!containsCategory(categoryId)){
				customContext.removeCustomManagedCategory(categoryId);
				UserProfile user = customContext.getUserProfile();
				user.removeCustomManagedCategoryIfOrphan(categoryId);
			}
		}
	}

	/**
	 * @param categoryId
	 * @return true if this context refers the category identified by categoryId
	 */
	synchronized public boolean containsCategory(String categoryId) {
	   	if (log.isDebugEnabled()){
    		log.debug("id="+id+" - containsCategory("+categoryId+")");
    	}
		return refIdManagedCategoryProfilesSet.contains(categoryId);
	}
	
	
	/**
	 * Add a managed category profile id to this context
	 * @param s the id to add
	 */
	synchronized protected void addRefIdManagedCategoryProfile(String s) {
		if (log.isDebugEnabled()){
			log.debug("id="+id+" - addRefIdManagedCategoryProfile("+s+")");
		}
		refIdManagedCategoryProfilesSet.add(s);
	}
	
	/** 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		String string = "";

		/* The context name */
		string += "	name : " + name + "\n";

		/* The context description */
		string += "	description : " + description + "\n";

		/* The context id */
		string += "	id : " + id + "\n";

		/* The Id Category profiles Set */
		string += "	managedCategoryProfile ids : \n";
		string += "           " + refIdManagedCategoryProfilesSet.toString();
		string += "\n";

		/* The context edit mode : not used for the moment */
		// string += " edit : "+ edit +"\n";;
		/* Managed category profiles available in this context */
		string += "	managedCategoryProfilesSet : \n";
		Iterator<ManagedCategoryProfile> iterator = managedCategoryProfilesSet.iterator();
		for (ManagedCategoryProfile m = null; iterator.hasNext();) {
			m = iterator.next();
			string += "         (" + m.getId() + "," + m.getName() + ")\n";
		}

		return string;
	}

	/*
	 * ************************** ACCESSORS ******************************** */
	
	/**
	 * Returns the name of the context 
	 * @return name
	 * @see Context#name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the context
	 * @param name the name to set
	 * @see Context#name
	 */
	synchronized protected void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the description of the context
	 * @return description
	 * @see Context#description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description of the context
	 * @param description the description to set
	 * @see Context#description
	 */
	synchronized protected void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns the id of the context
	 * @return id
	 * @see Context#id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the id of the context
	 * @param id the id to set
	 * @see Context#id
	 */
	synchronized protected void setId(String id) {
		this.id = id;
	}
	
// later
//	protected Editability getEdit() {
//		return edit;
//	}
//
//	protected void setEdit(Editability edit) {
//		this.edit = edit;
//	}

	
//	/**
//	 * Returns set of Managed category profiles defined in the context
//	 * @return managedCategoryProfilesSet
//	 */
//	public Set<ManagedCategoryProfile> getManagedCategoryProfilesSet() {
//		return managedCategoryProfilesSet;
//	}
	
//	 /**
//	  * Sets the set of managed category profiles in the Context
//	 * @param managedCategoryProfilesSet
//	 */
//	synchronized protected void setManagedCategoryProfilesSet(
//			Set<ManagedCategoryProfile> managedCategoryProfilesSet) {
//		this.managedCategoryProfilesSet = managedCategoryProfilesSet;
//	}


//	protected void setSetRefIdManagedCategoryProfiles(Set<String> s) {
//		refIdManagedCategoryProfilesSet = s;
//	}

	
}
