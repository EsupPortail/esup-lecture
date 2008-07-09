/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.beans;

import org.esupportail.lecture.domain.model.AvailabilityMode;
import org.esupportail.lecture.domain.model.CustomSource;
import org.esupportail.lecture.domain.model.ElementProfile;
import org.esupportail.lecture.domain.model.ItemDisplayMode;
import org.esupportail.lecture.domain.model.ProfileAvailability;
import org.esupportail.lecture.domain.model.SourceProfile;
import org.esupportail.lecture.exceptions.domain.CategoryNotLoadedException;
import org.esupportail.lecture.exceptions.domain.ElementDummyBeanException;
import org.esupportail.lecture.exceptions.domain.ManagedCategoryProfileNotFoundException;
import org.esupportail.lecture.exceptions.domain.SourceProfileNotFoundException;

/**
 * used to store source informations.
 * @author bourges
 */
public class SourceBean {
	
	/* 
	 *************************** PROPERTIES ******************************** */	
	/**
	 * id of source.
	 */
	private String id;
	/**
	 * name of source.
	 */
	private String name;
	/**
	 * type of source.
	 * "subscribed" --> The source is allowed and subscribed by the user
	 * "notSubscribed" --> The source is allowed and not yet subscribed by the user (used in edit mode)
	 * "obliged" --> The source is obliged: user can't subscribe or unsubscribe this source
	 * "owner" --> For personal sources
	 */
	private AvailabilityMode type;
	
	/**
	 * the item display mode of the source.
	 */
	private ItemDisplayMode itemDisplayMode = ItemDisplayMode.ALL;
	/**
	 * xmlOrder id used to store the order of the corresponding sourceProfile in an Category XML file.
	 */
	private int xmlOrder;

	/*
	 *************************** INIT ************************************** */	
	
	/**
	 * default constructor.
	 */
	public SourceBean() {
		// empty
	}
	
	/**
	 * constructor initializing object with a customSource.
	 * @param customSource
	 * @throws SourceProfileNotFoundException 
	 * @throws CategoryNotLoadedException 
	 * @throws ManagedCategoryProfileNotFoundException 
	 */
	public SourceBean(final CustomSource customSource) throws ManagedCategoryProfileNotFoundException, 
			CategoryNotLoadedException, SourceProfileNotFoundException {
		SourceProfile profile = customSource.getProfile();
		
		this.name = profile.getName();
		this.id = profile.getId();
		this.itemDisplayMode = customSource.getItemDisplayMode();
	}

	/**
	 * constructor initializing object with ProfileAvailability.
	 * @param profAv ProfileAvailability
	 */
	public SourceBean(final ProfileAvailability profAv) {
		ElementProfile elt = profAv.getProfile();
		this.name = elt.getName();
		this.id = elt.getId();
		this.type = profAv.getMode();
		
	}
	
	/*
	 *************************** ACCESSORS ********************************* */	
	


	/**
	 * @return name of source
	 * @throws ElementDummyBeanException 
	 */
	@SuppressWarnings("unused")
	public String getName() throws ElementDummyBeanException {
		return name;
	}
	
	/**
	 * @param name
	 * @throws ElementDummyBeanException 
	 */
	@SuppressWarnings("unused")
	public void setName(final String name) throws ElementDummyBeanException {
		this.name = name;
	}
	/**
	 * @return id of source
	 * @throws ElementDummyBeanException 
	 */
	@SuppressWarnings("unused")
	public String getId()  throws ElementDummyBeanException {
		return id;
	}
	/**
	 * @param id
	 * @throws ElementDummyBeanException 
	 */
	@SuppressWarnings("unused")
	public void setId(final String id)  throws ElementDummyBeanException {
		this.id = id;
	}
	
	/**
	 * @return type of source
	 * @throws ElementDummyBeanException 
	 */
	@SuppressWarnings("unused")
	public AvailabilityMode getType()  throws ElementDummyBeanException {
		return type;
	}
	
	/**
	 * @param type
	 * @throws ElementDummyBeanException 
	 */
	@SuppressWarnings("unused")
	public void setType(final AvailabilityMode type) throws ElementDummyBeanException {
		this.type = type;
	}
	
	/**
	 * @return item display mode
	 * @throws ElementDummyBeanException 
	 */
	@SuppressWarnings("unused")
	public ItemDisplayMode getItemDisplayMode() throws ElementDummyBeanException {
		return itemDisplayMode;
	}

	/**
	 * @param itemDisplayMode
	 * @throws ElementDummyBeanException 
	 */
	@SuppressWarnings("unused")
	public void setItemDisplayMode(final ItemDisplayMode itemDisplayMode) throws ElementDummyBeanException {
		this.itemDisplayMode = itemDisplayMode;
	}
	/*
	 *************************** METHODS *********************************** */	
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

	/**
	 * @return the xmlOrder
	 */
	public int getXmlOrder() {
		return xmlOrder;
	}

	/**
	 * @param xmlOrder the xmlOrder to set
	 */
	public void setXmlOrder(final int xmlOrder) {
		this.xmlOrder = xmlOrder;
	}


	
	
}
