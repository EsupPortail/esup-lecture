/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.exceptions.domain.ComputeItemsException;
import org.esupportail.lecture.exceptions.domain.DomainServiceException;
import org.esupportail.lecture.exceptions.domain.ElementNotFoundException;
import org.esupportail.lecture.exceptions.domain.InfoDomainException;
import org.esupportail.lecture.exceptions.domain.InternalDomainException;
import org.esupportail.lecture.exceptions.domain.ManagedCategoryNotLoadedException;
import org.esupportail.lecture.exceptions.domain.ManagedCategoryProfileNotFoundException;
import org.esupportail.lecture.exceptions.domain.SourceNotLoadedException;
import org.esupportail.lecture.exceptions.domain.SourceProfileNotFoundException;

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
	 * Used in log messages.
	 */
	private static final  String IDEGAL = "id=";
	
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
	private Map<String, ReadItemFlag> readItems = new Hashtable<String, ReadItemFlag>();
	
	/**
	 * item display mode of this customSource. 
	 */
	private ItemDisplayMode itemDisplayMode = ItemDisplayMode.UNREAD;
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
	 * @throws ManagedCategoryNotLoadedException 
	 * @throws SourceNotLoadedException 
	 * @throws ManagedCategoryNotLoadedException 
	 * @throws ComputeItemsException 
	 * @throws InternalDomainException 
	 */
	public List<Item> getItems() 
	throws SourceNotLoadedException, ManagedCategoryNotLoadedException, 
	ComputeItemsException, InternalDomainException {
		if (LOG.isDebugEnabled()) {
			LOG.debug(IDEGAL + elementId + " - getItems()");
		}
		SourceProfile profile;
		try {
			profile = getProfile();
		} catch (ElementNotFoundException e1) {
			String errorMsg = "Unable to get items because of an element is not found";
			LOG.error(errorMsg);
			throw new InternalDomainException(errorMsg, e1);
		} catch (DomainServiceException e1) {
			String errorMsg = "Unable to get items because of an element is not found";
			LOG.error(errorMsg);
			throw new InternalDomainException(errorMsg, e1);
		}
		List<Item> listItems = null;
		try {
			listItems = profile.getItems();
			// pas de catch de categoryNotLoaded : cela entraine trop de compliaction
		} catch (SourceNotLoadedException e) {
			// Dans ce cas : la mise à jour du customCategory n'a pas été effectuée
			LOG.error("Impossible to getItems for customSource " + getElementId()
					+ " because its source is not loaded - " 
					+ " It is very strange because loadSource() has been "
					+ "called before in mcp.updateCustomContext() ...", e);
			throw e;
		}
		return listItems;
	}

	/**
	 * The name of the source profile associated to this CustomSource.
	 * @throws InternalDomainException 
	 * @throws ManagedCategoryNotLoadedException 
	 * @see org.esupportail.lecture.domain.model.CustomElement#getName()
	 */
	public String getName() throws InternalDomainException, ManagedCategoryNotLoadedException {
		if (LOG.isDebugEnabled()) {
			LOG.debug(IDEGAL + elementId + " - getName()");
		}
		SourceProfile sp = null;
		try {
			sp = getProfile();
		} catch (ElementNotFoundException e) {
			String errorMsg = "Unable to get name because of an element is not found";
			LOG.error(errorMsg);
			throw new InternalDomainException(errorMsg, e);
		} catch (DomainServiceException e) {
			String errorMsg = "Unable to get name because of an element is not found";
			LOG.error(errorMsg);
			throw new InternalDomainException(errorMsg, e);
		}
		String name = sp.getName();
		return name;
	}


	
	/**
	 * @param itemId id of the item to set as read
	 * @param isRead boolean : true=item is read | false=item is not read
	 */
	public void setItemReadMode(final String itemId, final boolean isRead) {
		if (LOG.isDebugEnabled()) {
			LOG.debug(IDEGAL + elementId + " - setItemReadMode(" + itemId + "," + isRead + ")");
		}
		ReadItemFlag itemChanged = new ReadItemFlag(this, itemId);
		Date datejour = new Date();
		itemChanged.setDate(datejour);
		if (isRead) {
			if (!readItems.containsKey(itemId)) {
				readItems.put(itemId, itemChanged);	
			} else {
				LOG.warn(IDEGAL + elementId + "setItemReadMode(" + isRead + itemId + ") is called in customSource " + elementId 
						+ " but this item allready in readItems");
			}
		} else {
			LOG.debug(IDEGAL + elementId + " - setItemReadMode(" + itemId + "," + isRead + ") : remove");
			readItems.remove(itemId);
		}
	}
	
	/**
	 * @param itemId id of the item
	 * @return true if the item is marked read
	 */
	public boolean isItemRead(final String itemId) {
		if (LOG.isDebugEnabled()) {
			LOG.debug(IDEGAL + elementId + " - isItemRead(" + itemId + ")");
		}
		ReadItemFlag itemTested = new ReadItemFlag(this, itemId);
		return readItems.containsKey(itemId);
	}

	/**
	 * @param mode item display mode to set
	 */
	public void modifyItemDisplayMode(final ItemDisplayMode mode) {
		if (LOG.isDebugEnabled()) {
			LOG.debug(IDEGAL + elementId + " - modifyItemDisplayMode(" + mode + ")");
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
			LOG.debug(IDEGAL + elementId + " - itemDisplayMode(" + itemDisplayMode + ")");
		}
		return itemDisplayMode;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		CustomSource other = (CustomSource) obj;
		if (elementId == null) {
			if (other.elementId != null) {
				return false;
			}
		} else if (!elementId.equals(other.elementId)) {
			return false;
		}
		if (userProfile == null) {
			if (other.userProfile != null) {
				return false;
			}
		} else if (!userProfile.equals(other.userProfile)) {
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
	 * @throws ManagedCategoryNotLoadedException 
	 * @throws ManagedCategoryProfileNotFoundException 
	 * @throws DomainServiceException 
	 */
	public abstract SourceProfile getProfile() 
	throws ManagedCategoryNotLoadedException, SourceProfileNotFoundException, 
	ManagedCategoryProfileNotFoundException, DomainServiceException;
	


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
	protected void setUserProfile(final UserProfile userProfile) {
		this.userProfile = userProfile;
		//Needed by Hibernate
	}

	/**
	 * @return a set of read items ID
	 */
	protected Map<String, ReadItemFlag> getReadItems() {
		return readItems;
	}

	/**
	 * @param readItems
	 */
	@SuppressWarnings("unused")
	private void setReadItems(final Map<String, ReadItemFlag> readItems) {
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
