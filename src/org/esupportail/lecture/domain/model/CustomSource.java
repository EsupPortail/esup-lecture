/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.DomainTools;
import org.esupportail.lecture.domain.ExternalService;
import org.esupportail.lecture.exceptions.domain.CategoryNotLoadedException;
import org.esupportail.lecture.exceptions.domain.ComputeItemsException;
import org.esupportail.lecture.exceptions.domain.ManagedCategoryProfileNotFoundException;
import org.esupportail.lecture.exceptions.domain.MappingNotFoundException;
import org.esupportail.lecture.exceptions.domain.SourceNotLoadedException;
import org.esupportail.lecture.exceptions.domain.SourceProfileNotFoundException;
import org.esupportail.lecture.exceptions.domain.SourceTimeOutException;
import org.esupportail.lecture.exceptions.domain.Xml2HtmlException;

/**
 * Customizations on a SourceProfile for a user Profile 
 * @author gbouteil
 * @see CustomElement
 *
 */
public abstract class CustomSource implements CustomElement {
	/*
	 ************************** PROPERTIES *********************************/	
	/**
	 * Log instance
	 */
	protected static final Log log = LogFactory.getLog(CustomSource.class);
	/**
	 * Id of the source refered by this
	 */
	private String elementId;
	/**
	 * The userprofile parent 
	 */
	private UserProfile userProfile;
	/**
	 * Set of read item by User
	 */
	private Set<String> readItems = new HashSet<String>();
	
	/**
	 * item display mode of this customSource 
	 */
	private ItemDisplayMode itemDisplayMode = ItemDisplayMode.ALL;
	/**
	 * Database Primary Key
	 */
	private long customSourcePK;
	
	/* 
	 ************************** INIT **********************************/
	
	/**
	 * Constructor
	 * @param profile of the source refered by this CustomSource
	 * @param user owner of this  CustomSource
	 */
	protected CustomSource(SourceProfile profile,UserProfile user){
		if (log.isDebugEnabled()){
			log.debug("CustomSource("+profile.getId()+","+user.getUserId()+")");
		}
		userProfile = user;
		elementId = profile.getId();
	}

	/**
	 * Constructor
	 */
	protected CustomSource(){
		if (log.isDebugEnabled()){
			log.debug("CustomSource()");
		}
	}

	/* 
	 ************************** METHODS **********************************/
	
	/**
	 * Returns the list of items contained in the source referred by this customSource
	 * Items are ready to be displayed
	 * @param ex access to externalService
	 * @return the list of items
	 * @throws MappingNotFoundException
	 * @throws ComputeItemsException
	 * @throws Xml2HtmlException
	 * @throws SourceProfileNotFoundException 
	 * @throws CategoryNotLoadedException 
	 * @throws ManagedCategoryProfileNotFoundException 
	 * @throws SourceTimeOutException 
	 * @throws SourceNotLoadedException 
	 */
	public List<Item> getItems(ExternalService ex) 
		throws MappingNotFoundException, ComputeItemsException, Xml2HtmlException, 
		ManagedCategoryProfileNotFoundException, CategoryNotLoadedException, SourceProfileNotFoundException, 
		SourceTimeOutException, SourceNotLoadedException  {
		if (log.isDebugEnabled()){
			log.debug("id="+elementId+" - getItems(externalService)");
		}
	
		return getProfile().getItems(ex);
	}

	/**
	 * The name of the source profile associated to this CustomSource
	 * @throws SourceProfileNotFoundException 
	 * @throws CategoryNotLoadedException 
	 * @throws ManagedCategoryProfileNotFoundException 
	 * @see org.esupportail.lecture.domain.model.CustomElement#getName()
	 */
	public String getName() throws CategoryNotLoadedException, SourceProfileNotFoundException, ManagedCategoryProfileNotFoundException{
		if (log.isDebugEnabled()){
			log.debug("id="+elementId+" - getName()");
		}
		return getProfile().getName();
	}


	
	/**
	 * @param itemId id of the item to set as read
	 * @param isRead boolean : true=item is read | false=item is not read
	 */
	public void setItemReadMode(String itemId, boolean isRead) {
		if (log.isDebugEnabled()){
			log.debug("id="+elementId+" - setItemReadMode("+itemId+","+isRead+")");
		}
		if (isRead){
			readItems.add(itemId);	
		}else {
			readItems.remove(itemId);
		}
		DomainTools.getDaoService().updateCustomSource(this);
	}
	
	/**
	 * @param itemId id of the item
	 * @return true if the item is marked read
	 */
	public boolean isItemRead(String itemId) {
		if (log.isDebugEnabled()){
			log.debug("id="+elementId+" - isItemRead("+itemId+")");
		}
		return readItems.contains(itemId);
	}

	/**
	 * @param mode item display mode to set
	 */
	public void modifyItemDisplayMode(ItemDisplayMode mode){
		if (log.isDebugEnabled()){
			log.debug("id="+elementId+" - modifyItemDisplayMode("+mode+")");
		}
		/* old name was setItemDisplayMode but it has been changed to prevent 
		 * loop by calling dao
		 */
		this.itemDisplayMode = mode;
		DomainTools.getDaoService().updateCustomSource(this);
	}
	
	/**
	 * @return the item display mode of this customSource
	 */
	public ItemDisplayMode getItemDisplayMode(){
		if (log.isDebugEnabled()){
			log.debug("id="+elementId+" - itemDisplayMode("+itemDisplayMode+")");
		}
		return itemDisplayMode;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null) return false;
		if (!(o instanceof CustomSource)) return false;
		final CustomSource customSource = (CustomSource) o;
		if (!customSource.getElementId().equals(this.getElementId())) return false;
		return true;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return this.getElementId().hashCode();
	}
	

	/* 
	 ************************** ABSTRACT METHODS **********************************/

	/**
	 * The SourceProfile associated to this CustomSource
	 * @return the SourceProfile 
	 * @throws SourceProfileNotFoundException 
	 * @throws CategoryNotLoadedException 
	 * @throws ManagedCategoryProfileNotFoundException 
	 */
	public abstract SourceProfile getProfile() throws CategoryNotLoadedException, SourceProfileNotFoundException, ManagedCategoryProfileNotFoundException;
	


	/* 
	 ************************** ACCESSORS **********************************/
	/**
	 * The user Profile, owner of this CustomSource
	 * @see org.esupportail.lecture.domain.model.CustomElement#getUserProfile()
	 */
	public UserProfile getUserProfile(){
		return userProfile;
	}
	
	/**
	 * @see org.esupportail.lecture.domain.model.CustomElement#getElementId()
	 */
	public String getElementId(){
		return elementId;
	}

	/**
	 * @return database pk
	 */
	public long getCustomSourcePK() {
		return customSourcePK;
	}

	/**
	 * @param customSourcePK - - database Primary Key
	 */
	public void setCustomSourcePK(long customSourcePK) {
		this.customSourcePK = customSourcePK;
	}

	/**
	 * @param userProfile
	 */
	private void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}

	/**
	 * @return a set of read items ID
	 */
	protected Set<String> getReadItems() {
		return readItems;
	}

	/**
	 * @param readItems
	 */
	private void setReadItems(Set<String> readItems) {
		this.readItems = readItems;
	}

	/**
	 * @param elementId
	 */
	public void setElementId(String elementId) {
		this.elementId = elementId;
	}
	
	/**
	 * @param mode
	 */
	public void setItemDisplayMode(ItemDisplayMode mode){
		this.itemDisplayMode = mode;
	}
	
}
