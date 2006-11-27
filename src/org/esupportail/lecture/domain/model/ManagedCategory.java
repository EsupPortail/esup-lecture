/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.DomainServiceImpl;
import org.esupportail.lecture.domain.ExternalService;
import org.esupportail.lecture.exceptions.ComposantNotLoadedException;




/**
 * Managed category element : loaded from a remote definition, transfered by an XML file
 * @author gbouteil
 *
 */
public class ManagedCategory extends Category {

	/*
	 *********************** PROPERTIES**************************************/ 

	/**
	 * Log instance 
	 */
	protected static final Log log = LogFactory.getLog(ManagedCategory.class);

	
	/**
	 * Visibility sets of this category (if defined)
	 * Using depends on trustCategory parameter in 
	 * ManagedCategoryProfile corresponding 
	 */
	private VisibilitySets visibility;

	/**
	 * Ttl of this object reloading from remote definition (if defined)
	 * Using depends on trustCategory parameter in 
	 * ManagedCategoryProfile corresponding
	 */
	private int ttl;

	/**
	 * Managed category edit mode : not used for the moment (if defined)
	 * Using depends on trustCategory parameter in 
	 * ManagedCategoryProfile corresponding
	 */
	private Editability edit;
	
	


	/*
	 *********************** INIT **************************************/ 

	/**
	 * Constructor
	 */
	public ManagedCategory() {
		super.setSourceProfilesHash(new Hashtable<String,SourceProfile>());
	}
	
	/*
	 *********************** METHOD **************************************/ 
		
	/**
	 * Evaluate user visibility on managed source profiles of this managed category 
	 * And update the customManagedCategory associated with, according to visibilities
	 * But there is not any loading of source at this time
	 * @param customManagedCategory customManagedCAtegory to update
	 * @param portletService Access to portlet service
	 * @throws ComposantNotLoadedException 
	 */
	public void updateCustom(CustomManagedCategory customManagedCategory,ExternalService externalService) throws ComposantNotLoadedException {
		Iterator iterator = getSourceProfilesHash().values().iterator();
		
		while (iterator.hasNext()) {
			ManagedSourceProfile msp = (ManagedSourceProfile) iterator.next();
			log.debug("Managed Source profile ok");
			msp.updateCustomCategory(customManagedCategory,externalService);
		}
	}
	

	/*
	 *********************** ACCESSORS**************************************/ 

	
	
	/**
	 * Returns visibility sets of this managed category (if defined)
	 * @return visibility
	 */
	protected VisibilitySets getVisibility() {
		return visibility;
	}


	/**
	 * Sets visibility sets of this managed category
	 * @param visibility
	 */
	public void setVisibility(VisibilitySets visibility) {
		this.visibility = visibility;
	}


	/**
	 * Returns ttl of this managed category (if defined)
	 * @return ttl
	 */
	protected int getTtl() {
		return ttl;
	}


	/**
	 * Sets ttl of this managed category
	 * @param ttl
	 */
	public void setTtl(int ttl) {
		this.ttl = ttl;
	}

	/**
	 * @return Returns the edit.
	 */
	public Editability getEdit() {
		return edit;
	}
	
	
	

	/**
	 * @param edit The edit to set.
	 */
	public void setEdit(Editability edit) {
		this.edit = edit;
	}

	

	




	


//	protected Editability getEdit() {
//		return edit;
//	}


//	protected void setEdit(Editability edit) {
//		this.edit = edit;
//	}
//	

//	/** 
//	 * @uml.property name="managedChildren"
//	 * @uml.associationEnd multiplicity="(0 -1)" aggregation="composite" inverse="managedCategory:org.esupportail.lecture.domain.model.ManagedComposantProfile"
//	 */
//	private Collection managedComposantProfile;
//
//	/** 
//	 * Getter of the property <tt>managedChildren</tt>
//	 * @return  Returns the managedComposantProfile.
//	 * @uml.property  name="managedChildren"
//	 */
//	protected Collection getManagedChildren() {
//		return managedComposantProfile;
//	}
//
//	/** 
//	 * Setter of the property <tt>managedChildren</tt>
//	 * @param managedChildren  The managedComposantProfile to set.
//	 * @uml.property  name="managedChildren"
//	 */
//	protected void setManagedChildren(Collection managedChildren) {
//		managedComposantProfile = managedChildren;
//	}

		
	

}
