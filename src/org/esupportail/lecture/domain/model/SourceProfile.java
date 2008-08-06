/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.exceptions.domain.ComputeItemsException;
import org.esupportail.lecture.exceptions.domain.SourceNotLoadedException;

/**
 * Source profile element : a source profile can be a managed or personal one.
 * A Source profile references a source and is defined in a category
 * @author gbouteil
 * @see ElementProfile
 *
 */
public abstract class SourceProfile implements ElementProfile {

	/*
	 *************************** PROPERTIES ******************************** */
	
	/**
	 * Log instance. 
	 */
	protected static final Log LOG = LogFactory.getLog(SourceProfile.class);
	/**
	 * Default TTL.
	 */
	private static final int DEFAULTTTL = 3600;
	/**
	 * Default Time Out.
	 */
	private static final int DEFAULTTIMEOUT = 3000;
	
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

	/*
	 *************************** INIT	 ******************************** */	
		
	/**
	 * Constructor.
	 */
	public SourceProfile() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("SourceProfile()");
		}
		ttl = DEFAULTTTL;
		timeOut = DEFAULTTIMEOUT;
		xPathNameSpaces = new HashMap<String, String>();
	}

	/*
	 *************************** METHODS ******************************** */	

	/**
	 * Load the source referenced by this SourceProfile.
	 * @throws SourceNotLoadedException 
	 */
	protected abstract void loadSource() throws SourceNotLoadedException; 
	
	
	/**
	 * Return the list of items to be displayed for this source.
	 * @return a list of items
	 * @throws SourceNotLoadedException
	 * @throws ComputeItemsException 
	 */
	protected List<Item> getItems() throws SourceNotLoadedException, ComputeItemsException {
	   	if (LOG.isDebugEnabled()) {
    		LOG.debug("id = " + this.id + " - getItems()");
    	}
		loadSource();
		Source s = getElement();
		return s.getItems();
	}

	/**
	 * Make the (long)id of this sourceProfile (<type>:<parentId>:<interneId>).
	 * @param type = p | m  (personal or managed)
	 * @param parentId = 0 for a personal (no parent owner) | CategoryProfileId for a managed
	 * @param simpleId = interneId for a personal | fileId for a managed	  
	 * @return ID made from the three parameters
	 */
	protected String makeId(final String type, final String parentId, final String simpleId) {
	   	if (LOG.isDebugEnabled()) {
    		LOG.debug("id=" + this.id + " - makeId(" + type + "," + parentId + "," + simpleId + ")");
    	}
		id = type + ":" + parentId + ":" + simpleId;
		return id;
	}
	
	/**
	 * Returns source of this managed source profile (if loaded).
	 * @return source
	 * @throws SourceNotLoadedException
	 */
	protected Source getElement() throws SourceNotLoadedException {
	   	if (LOG.isDebugEnabled()) {
    		LOG.debug("id=" + this.id + " - getElement()");
    	}
		if (source == null) {
			loadSource();
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

	/**
	 * @param timeOut 
	 */
	public void setTimeOut(final int timeOut) {
		this.timeOut = timeOut;
	}

}
