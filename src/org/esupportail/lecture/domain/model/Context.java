/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.ExternalService;
import org.esupportail.lecture.exceptions.domain.CategoryNotLoadedException;
import org.esupportail.lecture.exceptions.domain.InfoDomainException;
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
	 * Log instance.
	 */
	private static final Log LOG = LogFactory.getLog(Context.class);

	/**
	 * The context name.
	 */
	private String name = "";

	/**
	 * The context description.
	 */
	private String description = "";

	/**
	 * The context id.
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

	/**
	 * orderedSourceIDs store SourceID and ordering order in the CategoryProfile definition.
	 */
	private Map<String, Integer> orderedCategoryIDs = Collections.synchronizedMap(new HashMap<String, Integer>());	
	
	/*
	 *************************** INIT *********************************/

	/**
	 * Constructor.
	 */
	public Context() {
		super();
	}

	/**
	 * Initilizes associations with managed category profiles linked to this Context.
	 * This method needs channel because it provides links to managed cateories profiles
	 * @param channel channel where the context is defined 
	 * @throws ManagedCategoryProfileNotFoundException 
	 */
	protected synchronized void initManagedCategoryProfiles(final Channel channel) 
			throws ManagedCategoryProfileNotFoundException  {
		if (LOG.isDebugEnabled()) {
			LOG.debug("id = " + id + " - initManagedCategoryProfiles(channel)");
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
	protected synchronized void updateCustom(final CustomContext customContext, final ExternalService ex) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("id=" + id + " - updateCustom(" + customContext.getElementId() + ",externalService)");
		}
		//TODO (GB later) optimise evaluation process (trustCategory + real loadding)
		
		// update for managedCategories defined in this context
		for (ManagedCategoryProfile mcp : managedCategoryProfilesSet) {
			try {
				mcp.updateCustomContext(customContext, ex);
			} catch (CategoryNotLoadedException e) {
				LOG.error("Impossible to update CustomContext associated to context " + getId()
						+ " for managedCategoryProfile " + mcp.getId()
						+ " because its category is not loaded - " 
						+ " It is very strange because loadCategory() has " 
						+ "been called before in mcp.updateCustomContext() ...", e);
			} catch (InfoDomainException e) {
				LOG.error("Impossible to update CustomContext associated to context " + getId()
						+ " for managedCategoryProfile " + mcp.getId());
			}
		}
		// update for managedCategories not anymore in this context
		updateCustomForVanishedSubscriptions(customContext);
	}

	/**
	 * Update customContext for managedCategories not anymore in this context.
	 * @param customContext
	 */
	private synchronized void updateCustomForVanishedSubscriptions(final CustomContext customContext) {
		List<String> cids = new ArrayList<String>();
		for (String categoryId : customContext.getSubscriptions().keySet()) {
			cids.add(categoryId);
		}
		for (String categoryId : cids) {
			if (!containsCategory(categoryId)) {
				customContext.removeCustomManagedCategory(categoryId);
				UserProfile user = customContext.getUserProfile();
				user.removeCustomManagedCategoryIfOrphan(categoryId);
			}
		}
	}

	/**
	 * Return a list of (CategoryProfile, VisibilityMode).
	 * This list corresponding to visible categories for user, 
	 * in this Context and update its related custom (like method updateCustom): 
	 * It sets up subscriptions of customContext on managedCategoriesProfiles
	 * defined in this Context, according to managedCategoriesProfiles visibility
	 * @param customContext custom to update
	 * @param ex access to externalService
	 * @return list of CoupleProfileVisibility
	 * @see Context#updateCustom(CustomContext, ExternalService)
	 */
	protected synchronized List<CoupleProfileVisibility> getVisibleCategoriesAndUpdateCustom(
			final CustomContext customContext, final ExternalService ex) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("id=" + this.getId() + " - getVisibleCategoriesAndUpdateCustom("
					+ this.getId() + ",externalService)");
		}
		List<CoupleProfileVisibility> couplesVisib = new Vector<CoupleProfileVisibility>();
		Iterator<ManagedCategoryProfile> iterator = managedCategoryProfilesSet.iterator();
		
		// update and get managedSources defined in this managedCategory 
		while (iterator.hasNext()) {
			ManagedCategoryProfile mcp = iterator.next();
			CoupleProfileVisibility couple;
			try {				
				VisibilityMode mode = mcp.updateCustomContext(customContext, ex);
				if (mode != VisibilityMode.NOVISIBLE) {
					couple = new CoupleProfileVisibility(mcp, mode);
					couplesVisib.add(couple);
				}
			} catch (CategoryNotLoadedException e) {
				LOG.error("Impossible to update CustomContext associated to context " + getId()
						+ " for managedCategoryProfile " + mcp.getId()
						+ " because its category is not loaded - " 
						+ " It is very strange because loadCategory() has " 
						+ "been called before in mcp.updateCustomContext() ...", e);
			} catch (InfoDomainException e) {
				LOG.error("Impossible to update CustomContext associated to context " + getId()
						+ " for managedCategoryProfile " + mcp.getId());
			}
		}
		
		// update for managedCategories not anymore in this Context
		updateCustomForVanishedSubscriptions(customContext);
		
		return couplesVisib;
	}
	

	/**
	 * @param categoryId
	 * @return true if this context refers the category identified by categoryId
	 */
	public boolean containsCategory(final String categoryId) {
	   	if (LOG.isDebugEnabled()) {
    		LOG.debug("id = " + id + " - containsCategory(" + categoryId + ")");
    	}
		return refIdManagedCategoryProfilesSet.contains(categoryId);
	}
	
	
	/**
	 * Add a managed category profile id to this context.
	 * @param s the id to add
	 */
	protected synchronized void addRefIdManagedCategoryProfile(final String s) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("id=" + id + " - addRefIdManagedCategoryProfile(" + s + ")");
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

		for (ManagedCategoryProfile m : managedCategoryProfilesSet) {
			string += "         (" + m.getId() + "," + m.getName() + ")\n";
		}
		
		return string;
	}

	/*
	 * ************************** ACCESSORS ******************************** */

	/**
	 * Returns the managedCategoryProfile identified by id, referred by this Context.
	 * @param categoryId id of the categoryProfile to get
	 * @return the categoryProfile
	 * @throws ManagedCategoryProfileNotFoundException 
	 */
	protected ManagedCategoryProfile getCatProfileById(final String categoryId) 
		throws ManagedCategoryProfileNotFoundException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("id=" + this.getId() + " - getCatProfileById(" + categoryId + ")");
		}
		
		
		if (refIdManagedCategoryProfilesSet.contains(categoryId)) {
			for (ManagedCategoryProfile m : managedCategoryProfilesSet) {
				if (m.getId().equals(categoryId)) {
					return m;
				}
			}
		} 
		String errorMsg = "ManagedCategoryProfile " + categoryId + " is not found in Context " + this.id;
		LOG.error(errorMsg);
		throw new ManagedCategoryProfileNotFoundException(errorMsg);
	}
	
	
	/**
	 * Returns the name of the context. 
	 * @return name
	 * @see Context#name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the context.
	 * @param name the name to set
	 * @see Context#name
	 */
	protected void setName(final String name) {
		this.name = name;
	}

	/**
	 * Returns the description of the context.
	 * @return description
	 * @see Context#description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description of the context.
	 * @param description the description to set
	 * @see Context#description
	 */
	protected void setDescription(final String description) {
		this.description = description;
	}

	/**
	 * Returns the id of the context.
	 * @return id
	 * @see Context#id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the id of the context.
	 * @param id the id to set
	 * @see Context#id
	 */
	protected void setId(final String id) {
		this.id = id;
	}

	/**
	 * @return the orderedCategoryIDs
	 */
	public Map<String, Integer> getOrderedCategoryIDs() {
		return orderedCategoryIDs;
	}

	/**
	 * @param orderedCategoryIDs the orderedCategoryIDs to set
	 */
	public void setOrderedCategoryIDs(final Map<String, Integer> orderedCategoryIDs) {
		this.orderedCategoryIDs = orderedCategoryIDs;
	}
	
}
