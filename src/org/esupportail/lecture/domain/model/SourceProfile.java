/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;

import java.util.List;

import org.esupportail.lecture.domain.DomainTools;
import org.esupportail.lecture.domain.ExternalService;
import org.esupportail.lecture.exceptions.CategoryNotLoadedException;
import org.esupportail.lecture.exceptions.ElementNotLoadedException;
import org.esupportail.lecture.exceptions.SourceNotLoadedException;

/**
 * Source profile element : a source profile can be a managed or personal one.
 * @author gbouteil
 *
 */
public abstract class SourceProfile implements ElementProfile {

/* ************************** PROPERTIES ******************************** */	

	/**
	 * Id of the source profile 
	 * A source profile id is like :
	 * <type>:<parentId>:<interneId>
	 */
	private String id;

	/**
	 * Name of the source 
	 */
	private String name = "";

	/**
	 * URL of the source 
	 */
	private String sourceURL = "";

	/**
	 * Source associated to this profile
	 */
	private Source source;
	
	private String xsltURL;

	private String itemXPath;
	
	private boolean specificUserContent = false; 
	
	private int ttl;
	


/* ************************** METHODS ******************************** */	
	
	
	


	protected abstract void loadSource(ExternalService externalService) throws ElementNotLoadedException; 
	
	
	public List<Item> getItems(ExternalService externalService) throws ElementNotLoadedException {
		loadSource(externalService);
		Source source = getElement();
		return source.getItems();
	}

	/**
	 * Make the id of this  (<type>:<parentId>:<interneId>)
	 * @param type = p | m  (persdonal or managed)
	 * @param parentId = 0 for a personal (no parent owner) | CategoryProfileId for a managed
	 * @param simpleId for a personal | fileId for a managed	  
	 * @return ID made from the three parameters
	 */
	protected String makeId(String type,String parentId,String interneId){
		String id = type+":"+parentId+":"+interneId;
		return id;
	}
	
	
/* ************************** ACCESSORS ******************************** */	

	/**
	 * Returns the source profile Id
	 * @return id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the source profile Id
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Returns the source profile name
	 * @return name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the source profile name
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the source URL of the source profile
	 * @return sourceURL
	 */
	public String getSourceURL() {
		return sourceURL;
	}
	
	/**
	 * Sets the source URL of the source profile
	 * @param sourceURL
	 */
	public void setSourceURL(String sourceURL) {
		this.sourceURL = sourceURL;
	}

	/**
	 * Returns source of this managed source profile (if loaded)
	 * @return source
	 * @throws SourceNotLoadedException 
	 */
	public Source getElement() throws SourceNotLoadedException {
		if (source == null){
			// TODO (GB) on pourrait faire un loadSource ?
			throw new SourceNotLoadedException("Source "+id+" is not loaded in profile");
		}
		return source;
	}
	
	/**
	 * Sets source on the profile
	 * @param source
	 */
	public void setElement(Source source) {
		this.source = source;
	}


	public void setXsltURL(String string) {
		xsltURL = string;
		
	}


	public void setItemXPath(String string) {
		itemXPath = string;
		
	}


	public String getItemXPath() {
		return itemXPath;
	}


	public String getXsltURL() {
		return xsltURL;
	}


	public int getTtl() {
		return ttl;
	}


	public void setTtl(int ttl) {
		this.ttl = ttl;
	}

	public boolean isSpecificUserContent() {
		return specificUserContent;
	}


	public void setSpecificUserContent(boolean specificUserContent) {
		this.specificUserContent = specificUserContent;
	}

}
