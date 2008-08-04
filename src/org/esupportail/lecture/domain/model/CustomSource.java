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
import org.esupportail.lecture.exceptions.domain.CategoryNotLoadedException;
import org.esupportail.lecture.exceptions.domain.ComputeItemsException;
import org.esupportail.lecture.exceptions.domain.InfoDomainException;
import org.esupportail.lecture.exceptions.domain.ManagedCategoryProfileNotFoundException;
import org.esupportail.lecture.exceptions.domain.MappingNotFoundException;
import org.esupportail.lecture.exceptions.domain.SourceNotLoadedException;
import org.esupportail.lecture.exceptions.domain.SourceProfileNotFoundException;
import org.esupportail.lecture.exceptions.domain.SourceTimeOutException;
import org.esupportail.lecture.exceptions.domain.Xml2HtmlException;

/**
 * Customizations on a SourceProfile for a user Profile.
 * @author gbouteil
 * @see CustomElement
 *
 */
public abstract class CustomSource implements CustomElement {
	/*
	 ************************** PROPERTIES *********************************/	
	/**
	 * Log instance.
	 */
	private static final Log LOG = LogFactory.getLog(CustomSource.class);
	/**
	 * Id of the source refered by this.
	 */
	private String elementId;
	/**
	 * The userprofile parent.
	 */
	private UserProfile userProfile;
	/**
	 * Set of read item by User.
	 */
	private Set<String> readItems = new HashSet<String>();
	
	/**
	 * item display mode of this customSource. 
	 */
	private ItemDisplayMode itemDisplayMode = ItemDisplayMode.ALL;
	/**
	 * Database Primary Key.
	 */
	private long customSourcePK;
	
	/* 
	 ************************** INIT **********************************/
	
	/**
	 * Constructor.
	 * @param profile of the source refered by this CustomSource
	 * @param user owner of this  CustomSource
	 */
	protected CustomSource(final SourceProfile profile, final UserProfile user) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("CustomSource(" + profile.getId() + "," + user.getUserId() + ")");
		}
		userProfile = user;
		elementId = profile.getId();
	}

	/**
	 * Constructor.
	 */
	protected CustomSource() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("CustomSource()");
		}
	}

	/* 
	 ************************** METHODS **********************************/
	
	/**
	 * Returns the list of items contained in the source referred by this customSource.
	 * Items are ready to be displayed
	 * @return the list of items
	 * @throws SourceProfileNotFoundException 
	 * @throws CategoryNotLoadedException 
	 * @throws ManagedCategoryProfileNotFoundException 
	 * @throws MappingNotFoundException
	 * @throws ComputeItemsException
	 * @throws Xml2HtmlException 
	 * @throws CategoryNotLoadedException 
	 * @throws ManagedCategoryProfileNotFoundException 
	 * @throws Xml2HtmlException 
	 * @throws ComputeItemsException 
	 * @throws MappingNotFoundException 
	 * @throws SourceNotLoadedException 
	 */
	public List<Item> getItems() throws ManagedCategoryProfileNotFoundException, CategoryNotLoadedException, 
	SourceProfileNotFoundException, MappingNotFoundException, ComputeItemsException, Xml2HtmlException, SourceNotLoadedException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("id=" + elementId + " - getItems()");
		}
		SourceProfile profile = getProfile();
		List<Item> listItems = null;
		try {
			listItems = profile.getItems();
			// pas de catch de categoryNotLoaded : cela entraine trop de compliaction
		} catch (SourceNotLoadedException e) {
			// Dans ce cas : la mise à jour du customCategory n'a pas été effectuée
			LOG.error("Impossible to update getItems for customSource " + getElementId()
					+ " because its source is not loaded - " 
					+ " It is very strange because loadSource() has been "
					+ "called before in mcp.updateCustomContext() ...", e);
			throw e;
		}
		return listItems;
	}

	/**
	 * The name of the source profile associated to this CustomSource.
	 * @throws SourceProfileNotFoundException 
	 * @throws CategoryNotLoadedException 
	 * @throws ManagedCategoryProfileNotFoundException 
	 * @see org.esupportail.lecture.domain.model.CustomElement#getName()
	 */
	public String getName() 
	throws CategoryNotLoadedException, SourceProfileNotFoundException, ManagedCategoryProfileNotFoundException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("id=" + elementId + " - getName()");
		}
		return getProfile().getName();
	}


	
	/**
	 * @param itemId id of the item to set as read
	 * @param isRead boolean : true=item is read | false=item is not read
	 */
	public void setItemReadMode(final String itemId, final boolean isRead) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("id=" + elementId + " - setItemReadMode(" + itemId + "," + isRead + ")");
		}
		if (isRead) {
			readItems.add(itemId);	
		} else {
			readItems.remove(itemId);
		}
//		DomainTools.getDaoService().updateCustomSource(this);
	}
	
	/**
	 * @param itemId id of the item
	 * @return true if the item is marked read
	 */
	public boolean isItemRead(final String itemId) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("id=" + elementId + " - isItemRead(" + itemId + ")");
		}
		return readItems.contains(itemId);
	}

	/**
	 * @param mode item display mode to set
	 */
	public void modifyItemDisplayMode(final ItemDisplayMode mode) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("id=" + elementId + " - modifyItemDisplayMode(" + mode + ")");
		}
		/* old name was setItemDisplayMode but it has been changed to prevent 
		 * loop by calling dao
		 */
		this.itemDisplayMode = mode;
//		DomainTools.getDaoService().updateCustomSource(this);
	}
	
	/**
	 * @return the item display mode of this customSource
	 */
	public ItemDisplayMode getItemDisplayMode() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("id=" + elementId + " - itemDisplayMode(" + itemDisplayMode + ")");
		}
		return itemDisplayMode;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null) {
			return false;
		}
		if (!(o instanceof CustomSource)) {
			return false;
		}
		final CustomSource customSource = (CustomSource) o;
		if (!customSource.getElementId().equals(this.getElementId())) {
			return false;
		}
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
	 * The SourceProfile associated to this CustomSource.
	 * @return the SourceProfile 
	 * @throws SourceProfileNotFoundException 
	 * @throws CategoryNotLoadedException 
	 * @throws ManagedCategoryProfileNotFoundException 
	 */
	public abstract SourceProfile getProfile() 
	throws CategoryNotLoadedException, SourceProfileNotFoundException, ManagedCategoryProfileNotFoundException;
	


	/* 
	 ************************** ACCESSORS **********************************/
	/**
	 * The user Profile, owner of this CustomSource.
	 * @see org.esupportail.lecture.domain.model.CustomElement#getUserProfile()
	 */
	public UserProfile getUserProfile() {
		return userProfile;
	}
	
	/**
	 * @see org.esupportail.lecture.domain.model.CustomElement#getElementId()
	 */
	public String getElementId() {
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
	public void setCustomSourcePK(final long customSourcePK) {
		this.customSourcePK = customSourcePK;
	}

	/**
	 * @param userProfile
	 */
	@SuppressWarnings("unused")
	private void setUserProfile(final UserProfile userProfile) {
		this.userProfile = userProfile;
		//Needed by Hibernate
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
	@SuppressWarnings("unused")
	private void setReadItems(final Set<String> readItems) {
		this.readItems = readItems;
		//Needed by Hibernate
	}

	/**
	 * @param elementId
	 */
	public void setElementId(final String elementId) {
		this.elementId = elementId;
	}
	
	/**
	 * @param mode
	 */
	public void setItemDisplayMode(final ItemDisplayMode mode) {
		this.itemDisplayMode = mode;
	}
	
}
