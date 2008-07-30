/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.ExternalService;
import org.esupportail.lecture.exceptions.domain.CategoryNotLoadedException;
import org.esupportail.lecture.exceptions.domain.ComputeItemsException;
import org.esupportail.lecture.exceptions.domain.InfoDomainException;
import org.esupportail.lecture.exceptions.domain.MappingNotFoundException;
import org.esupportail.lecture.exceptions.domain.SourceNotLoadedException;
import org.esupportail.lecture.exceptions.domain.SourceTimeOutException;
import org.esupportail.lecture.exceptions.domain.Xml2HtmlException;

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
	
	/**
	 * Ttl of the remote source reloading.
	 */
	private int ttl;
	
	/*
	 *************************** INIT	 ******************************** */	
		


	/*
	 *************************** METHODS ******************************** */	

	/**
	 * Load the source referenced by this SourceProfile.
	 * @param ex
	 * @throws SourceTimeOutException 
	 * @throws CategoryNotLoadedException 
	 * @throws InfoDomainException 
	 */
	protected abstract void loadSource(ExternalService ex) 
	throws SourceTimeOutException, CategoryNotLoadedException, InfoDomainException ; 
	
	
	/**
	 * Return the list of items to be displayed for this source.
	 * @param ex access to externalService
	 * @return a list of items
	 * @throws SourceNotLoadedException
	 * @throws MappingNotFoundException
	 * @throws ComputeItemsException
	 * @throws Xml2HtmlException
	 * @throws SourceTimeOutException 
	 * @throws CategoryNotLoadedException 
	 * @throws InfoDomainException 
	 */
	protected List<Item> getItems(final ExternalService ex) 
	throws MappingNotFoundException, ComputeItemsException, Xml2HtmlException, 
	SourceTimeOutException, CategoryNotLoadedException, InfoDomainException  {
	   	if (LOG.isDebugEnabled()) {
    		LOG.debug("id = " + this.id + " - getItems(externalService)");
    	}
		loadSource(ex);
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
	public Source getElement() throws SourceNotLoadedException {
	   	if (LOG.isDebugEnabled()) {
    		LOG.debug("id=" + this.id + " - getElement()");
    	}
		if (source == null) {
			String errorMsg = "Source " + id + " is not loaded in profile";
			LOG.error(errorMsg);
			throw new SourceNotLoadedException(errorMsg);
		}
		return source;
	}
	
	
	/*
	 *************************** ACCESSORS ******************************** */	

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
	public void setId(final String id) {
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
	 * Sets source on the profile.
	 * @param source
	 */
	public void setElement(final Source source) {
		this.source = source;
	}


	/**
	 * @param string
	 */
	public void setXsltURL(final String string) {
		xsltURL = string;
		
	}


	/**
	 * @param string
	 */
	public void setItemXPath(final String string) {
		itemXPath = string;
	}


	/**
	 * @return itemXPath
	 */
	public String getItemXPath() {
		return itemXPath;
	}


	/**
	 * @return xsltURL
	 */
	@SuppressWarnings("unused")
	private String getXsltURL() {
		return xsltURL;
	}


	/**
	 * @return ttl
	 */
	public int getTtl() {
		return ttl;
	}


	/**
	 * @param ttl
	 */
	public void setTtl(final int ttl) {
		this.ttl = ttl;
	}

	/**
	 * @return timeOut
	 * @exception CategoryNotLoadedException
	 */
	public abstract int getTimeOut() throws CategoryNotLoadedException;


	/**
	 * @param timeOut
	 */
	public abstract void setTimeOut(int timeOut);

}
