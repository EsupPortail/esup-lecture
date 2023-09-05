/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.beans;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.model.AvailabilityMode;
import org.esupportail.lecture.domain.model.CoupleProfileAvailability;
import org.esupportail.lecture.domain.model.CustomManagedSource;
import org.esupportail.lecture.domain.model.CustomSource;
import org.esupportail.lecture.domain.model.ElementProfile;
import org.esupportail.lecture.domain.model.ItemDisplayMode;
import org.esupportail.lecture.domain.model.ManagedSourceProfile;
import org.esupportail.lecture.domain.model.SourceProfile;
import org.esupportail.lecture.exceptions.domain.DomainServiceException;
import org.esupportail.lecture.exceptions.domain.ElementNotFoundException;
import org.esupportail.lecture.exceptions.domain.InternalDomainException;
import org.esupportail.lecture.exceptions.domain.SourceProfileNotFoundException;

/**
 * used to store source informations.
 *
 * @author bourges
 */
public class SourceBean {

	/*
	 *************************** PROPERTIES ********************************
	 */
	/**
	 * Log instance.
	 */
	protected static final Log LOG = LogFactory.getLog(CustomManagedSource.class);
	/**
	 * id of source.
	 */
	private String id;
	/**
	 * name of source.
	 */
	private String name;
	private int uid;

	/**
	 * type of source. "subscribed" --> The source is allowed and subscribed by
	 * the user "notSubscribed" --> The source is allowed and not yet subscribed
	 * by the user (used in edit mode) "obliged" --> The source is obliged: user
	 * can't subscribe or unsubscribe this source "owner" --> For personal
	 * sources
	 */
	private AvailabilityMode type;

	/**
	 * the item display mode of the source.
	 */
	private ItemDisplayMode itemDisplayMode = ItemDisplayMode.ALL;

	private String color;
	private Boolean highlight;
	private Boolean hiddenIfEmpty;
	/*
	 *************************** INIT **************************************
	 */

	/**
	 * default constructor.
	 */
	public SourceBean() {
		// empty
	}

	/**
	 * constructor initializing object with a customSource.
	 *
	 * @param customSource
	 * @throws DomainServiceException
	 */
	public SourceBean(final CustomSource customSource) throws DomainServiceException {
		SourceProfile profile;
		try {
			profile = customSource.getProfile();
		} catch (SourceProfileNotFoundException e) {
			LOG.error("Error on service 'getProfile()' : ");
			throw new DomainServiceException(e);
		} catch (ElementNotFoundException e) {
			String errorMsg = "Unable to create SourceBean" + ") because of an element not found";
			LOG.error(errorMsg);
			throw new InternalDomainException(errorMsg, e);
		}

		this.name = profile.getName();
		this.id = profile.getId();
		this.itemDisplayMode = customSource.getItemDisplayMode();
		if (profile instanceof ManagedSourceProfile) {
			ManagedSourceProfile msp = (ManagedSourceProfile) profile;
			this.setColor(msp.getColor());
			this.setHighlight(msp.isHighLight());
			this.setUid(msp.getUuid());
			this.setHiddenIfEmpty(msp.isHiddenIfEmpty());
		}
	}

	/**
	 * constructor initializing object with CoupleProfileAvailability.
	 *
	 * @param profAv
	 *            CoupleProfileAvailability
	 */
	public SourceBean(final CoupleProfileAvailability profAv) {
		ElementProfile elt = profAv.getProfile();
		this.name = elt.getName();
		this.id = elt.getId();
		this.type = profAv.getMode();

	}

	/*
	 *************************** ACCESSORS *********************************
	 */

	/**
	 * @return name of source
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * @return id of source
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 */
	public void setId(final String id) {
		this.id = id;
	}

	/**
	 * @return type of source
	 */
	public AvailabilityMode getType() {
		return type;
	}

	/**
	 * @param type
	 */
	public void setType(final AvailabilityMode type) {
		this.type = type;
	}

	/**
	 * @return item display mode
	 */
	public ItemDisplayMode getItemDisplayMode() {
		return itemDisplayMode;
	}

	/**
	 * @param itemDisplayMode
	 */
	public void setItemDisplayMode(final ItemDisplayMode itemDisplayMode) {
		this.itemDisplayMode = itemDisplayMode;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Boolean getHighlight() {
		return highlight;
	}

	public void setHighlight(Boolean highlight) {
		this.highlight = highlight;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public Boolean getHiddenIfEmpty(){
		return this.hiddenIfEmpty;
	}

	public void setHiddenIfEmpty(Boolean hiddenIfEmpty){
		this.hiddenIfEmpty = hiddenIfEmpty;
	}

	/*
	 *************************** METHODS ***********************************
	 */
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String string = "";
		string += "     Id = " + id.toString() + "\n";
		string += "     Name = " + name.toString() + "\n";
		string += "     Type = ";
		if (type != null) {
			string += type;
		}
		string += "\n     displayMode = " + itemDisplayMode.toString() + "\n";

		return string;
	}

}
