/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.DomainTools;
import org.esupportail.lecture.exceptions.domain.ComputeItemsException;
import org.esupportail.lecture.exceptions.domain.ManagedCategoryNotLoadedException;
import org.esupportail.lecture.exceptions.domain.SourceNotLoadedException;

/**
 * Source profile element : a source profile can be a managed or personal one.
 * A Source profile references a source and is defined in a category
 * @author gbouteil
 * @see ElementProfile
 *
 */
public abstract class SourceProfile implements ElementProfile, Serializable {

	/*
	 *************************** PROPERTIES ******************************** */
	
	/**
	 * Log instance. 
	 */
	protected static final Log LOG = LogFactory.getLog(SourceProfile.class);
		
	/**
	 * Id of the source profile. 
	 * A source profile id is like :
	 * <type>:<parentId>:<interneId>
	 */
	private String id;
	/**
	 * Name of the source. 
	 */
	private String name = "";
	/**
	 * URL of the source. 
	 */
	private String sourceURL = "";
	/**
	 * Source associated to this profile.
	 */
	private Source source;	
	/**
	 * URL of the xslt file. 
	 */
	private String xsltURL;
	/**
	 * URL of the xslt file for mobile. 
	 */
	private String mobileXsltURL;
	
	/**
	 * Xpath of an item. 
	 */
	private String itemXPath;
	// TODO (RB <-- GB) Not used ???
	/**
	 * Map of namespaces used by Xpath (key: NamesSpace prefix; value: NamaSpace URI).
	 */
	private HashMap<String, String> xPathNameSpaces;

	/**
	 * Ttl of remote reloading.
	 */
	private int ttl;
	/**
	 * Time Out of remote reloading.
	 */
	private int timeOut;

	public boolean isComplexItems() {
		return complexItems;
	}

	public void setComplexItems(boolean complexItems) {
		this.complexItems = complexItems;
	}

	private boolean complexItems;
	/*
	 *************************** INIT	 ******************************** */	
		
	/**
	 * Constructor.
	 */
	public SourceProfile() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("SourceProfile()");
		}
		ttl = DomainTools.getDefaultTtl();
		timeOut = DomainTools.getDefaultTimeOut();
		xPathNameSpaces = new HashMap<String, String>();
	}

	/*
	 *************************** METHODS ******************************** */	

	/**
	 * Load the source referenced by this SourceProfile.
	 * @throws SourceNotLoadedException 
	 * @throws ManagedCategoryNotLoadedException 
	 */
	protected abstract void loadSource() throws SourceNotLoadedException, ManagedCategoryNotLoadedException; 
	
	
	/**
	 * Return the list of items to be displayed for this source.
	 * @return a list of items
	 * @throws SourceNotLoadedException
	 * @throws ComputeItemsException 
	 * @throws ManagedCategoryNotLoadedException 
	 */
	protected List<Item> getItems() 
	throws SourceNotLoadedException, ComputeItemsException, ManagedCategoryNotLoadedException {
	   	if (LOG.isDebugEnabled()) {
    		LOG.debug("id = " + this.id + " - getItems()");
    	}
		// GB : ligne a supprimer loadSource();
		Source s = getElement();
		List<Item> ret = null;
	   	if ( this.isComplexItems()){
	   		//Produit le flux xml (liste d'items) des flux publisher : 
	   		//	gere la visibilité; les rubriques; permet de generer les rubriques et les auteurs
	   		ItemParser parser = new ItemParser(s);
	   		 ret = s.getItems(true, parser);
	   	}else{
	   		 ret = s.getItems(false, null);
	   		}
		if (this.id.startsWith("ActualitesEtab:m:publisher-376")) {
			LOG.warn("TRACE getItems this : " + this.toString());
			LOG.warn("TRACE getItems items : " + ret.toString());
		}
		return ret;
		//return s.getItems();
	}

	/**
	 * Make the (long)id of this sourceProfile (<parentId>:<interneId>).
	 * @param type = p | m  (personal or managed)
	 * @param parentId = 0 for a personal (no parent owner) | CategoryProfileId for a managed
	 * @param simpleId = interneId for a personal | fileId for a managed	  
	 * @return ID made from the three parameters
	 */
	protected String makeId(final String type, final String parentId, final String simpleId) {
	   	if (LOG.isDebugEnabled()) {
    		LOG.debug("id=" + this.id + " - makeId(" + type + "," + parentId + "," + simpleId + ")");
    	}
		// id = type + ":" + parentId + ":" + simpleId;
	   	// type is no longer used (it is included in parentId now)
		id = parentId + ":" + simpleId;
		return id;
	}
	
	/**
	 * Returns source of this managed source profile (if loaded).
	 * @return source
	 * @throws SourceNotLoadedException
	 * @throws ManagedCategoryNotLoadedException 
	 */
	protected Source getElement() throws SourceNotLoadedException, ManagedCategoryNotLoadedException {
	   	if (LOG.isDebugEnabled()) {
    		LOG.debug("id=" + this.id + " - getElement()");
    	}
		// GB : pour pouvoir rafraichir, je commente
	   	//if (source == null) {
			loadSource();
		//}
		if (this.id.startsWith("ActualitesEtab:m:publisher-376")) {
			LOG.warn("TRACE getElement : " + this.source);
		}
		return source;
		
	}
	
	
	/*
	 *************************** ACCESSORS ******************************** */	


	/**
	 * Sets source on the profile.
	 * @param source
	 */
	protected void setElement(final Source source) {
		this.source = source;
	}
	
	/**
	 * Returns the source profile Id.
	 * @return id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the source profile Id.
	 * @param id
	 */
	protected void setId(final String id) {
		this.id = id;
	}

	/**
	 * Returns the source profile name.
	 * @return name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the source profile name.
	 * @param name
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * Returns the source URL of the source profile.
	 * @return sourceURL
	 */
	public String getSourceURL() {
		return sourceURL;
	}
	
	/**
	 * Sets the source URL of the source profile.
	 * @param sourceURL
	 */
	public void setSourceURL(final String sourceURL) {
		//RB sourceURL = DomainTools.replaceWithUserAttributes(sourceURL);
		this.sourceURL = sourceURL;
	}

	/**
	 * @return xsltURL
	 */
	protected String getXsltURL() {
		return xsltURL;
	}
	
	/**
	 * @param string
	 */
	public void setXsltURL(final String string) {
		xsltURL = string;
	}
	
	/**
	 * @return the mobileXsltURL
	 */
	public String getMobileXsltURL() {
		return mobileXsltURL;
	}

	/**
	 * @param mobileXsltURL the mobileXsltURL to set
	 */
	public void setMobileXsltURL(String mobileXsltURL) {
		this.mobileXsltURL = mobileXsltURL;
	}

	/**
	 * @return itemXPath
	 */
	protected String getItemXPath() {
		return itemXPath;
	}

	/**
	 * @param string
	 */
	public void setItemXPath(final String string) {
		itemXPath = string;
	}

	/**
	 * @return xPathNameSpaces
	 */
	protected HashMap<String, String> getXPathNameSpaces() {
		return xPathNameSpaces;
	}

	/**
	 * @param pathNameSpaces
	 */
	public void setXPathNameSpaces(final HashMap<String, String> pathNameSpaces) {
		xPathNameSpaces = pathNameSpaces;
	}

	/**
	 * @return ttl
	 */
	public int getTtl() {
		return ttl;
	}
	
	/**
	 * @return timeOut
	 */
	public int getTimeOut() {
		return timeOut;
	}

	@Override
	public String toString() {
		return "SourceProfile{" +
				"id='" + id + '\'' +
				", name='" + name + '\'' +
				", complexItems=" + complexItems +
				", sourceURL='" + sourceURL + '\'' +
				", source=" + source +
				", xsltURL='" + xsltURL + '\'' +
				", mobileXsltURL='" + mobileXsltURL + '\'' +
				", itemXPath='" + itemXPath + '\'' +
				", xPathNameSpaces=" + xPathNameSpaces +
				", ttl=" + ttl +
				", timeOut=" + timeOut +
				'}';
	}
}
