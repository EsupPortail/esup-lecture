package org.esupportail.lecture.domain.model;

import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Context element.
 * @author gbouteil
 */

public class Context {
	
/* ************************** PROPERTIES ******************************** */
	/**
	 * Log instance
	 */
	protected static final Log log = LogFactory.getLog(Channel.class);

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

	/**
	 * The context edit mode : not used for the moment
	 */
	private Editability edit;

	/**
	 * Managed category profiles available in this context.
	 */
	private Set<ManagedCategoryProfile> managedCategoryProfilesSet = new HashSet<ManagedCategoryProfile>();

	/**
	 * Set of managed category profiles id available in this context.
	 */
	private Set<String> refIdManagedCategoryProfilesSet = new HashSet<String>();


/* *************************** Initialization ************************************/

	/**
	 * Initilizes associations to managed category profiles linked to this context.
	 * The channel is given because it contains managed cateories profiles objects to link with
	 * @param channel channel where the context is defined 
	 */
	protected void initManagedCategoryProfiles(Channel channel) {
		if (log.isDebugEnabled()){
			log.debug("initManagedCategoryProfiles()");
		}
		/* Connecting Managed category profiles and contexts */
		Iterator iterator = refIdManagedCategoryProfilesSet.iterator();

		while (iterator.hasNext()) {
			String id = (String) iterator.next();
			ManagedCategoryProfile mcp = channel.getManagedCategoryProfile(id);
			managedCategoryProfilesSet.add(mcp);
			mcp.addContext(this);
		}
	}

	/* ************************** METHODS ******************************** */

	/** 
	 * Return the string containing context content : 
	 * name, description, id, id category profiles set, managed category profiles available in this context
	 * @see java.lang.Object#toString()
	 */
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
		Iterator iterator = managedCategoryProfilesSet.iterator();
		for (ManagedCategoryProfile m = null; iterator.hasNext();) {
			m = (ManagedCategoryProfile) iterator.next();
			string += "         (" + m.getId() + "," + m.getName() + ")\n";
		}

		return string;
	}

/* ************************** ACCESSORS ******************************** */
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
	protected void setName(String name) {
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
	protected void setDescription(String description) {
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
	protected void setId(String id) {
		this.id = id;
	}
	
// utile plus tard
//	protected Editability getEdit() {
//		return edit;
//	}
//
//	protected void setEdit(Editability edit) {
//		this.edit = edit;
//	}


	public Set<ManagedCategoryProfile> getManagedCategoryProfilesSet() {
		return managedCategoryProfilesSet;
	}
	protected void setManagedCategoryProfilesSet(
			Set<ManagedCategoryProfile> managedCategoryProfilesSet) {
		this.managedCategoryProfilesSet = managedCategoryProfilesSet;
	}

	/**
	 * Add a managed category profile id to the set of id refered to in this context
	 * @param s the id to add
	 * @see Context#refIdManagedCategoryProfilesSet
	 */
	protected void addRefIdManagedCategoryProfile(String s) {
		refIdManagedCategoryProfilesSet.add(s);
	}
	
//	 TODO a retirer si inutile	
//	protected void setSetRefIdManagedCategoryProfiles(Set<String> s) {
//		refIdManagedCategoryProfilesSet = s;
//	}

	
}
