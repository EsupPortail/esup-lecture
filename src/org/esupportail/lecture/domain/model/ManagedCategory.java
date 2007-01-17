/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;

import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.ExternalService;
import org.esupportail.lecture.exceptions.domain.ComputeFeaturesException;


/**
 * Managed category element : loaded from a remote definition, transfered by an XML file
 * A category contains a set of sourceProfiles
 * @author gbouteil
 * @see Category
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
//	/**
//	 * Managed category edit mode : not used for the moment (if defined)
//	 * Using depends on trustCategory parameter in 
//	 * ManagedCategoryProfile corresponding
//	 */
//	private Editability edit;
	
	/*
	 *********************** INIT **************************************/ 


	
	/*
	 *********************** METHOD **************************************/ 
		
	/**
	 * Update the CustomManagedCategory linked to this ManagedCategory.
	 * It sets up subscriptions of customManagedCategory on managedSourcesProfiles
	 * defined in ths ManagedCategory, according to managedSourceProfiles visibility
	 * (there is not any loading of source at this time)
	 * @param customManagedCategory customManagedCategory to update
	 * @param  ex access to external service for visibility evaluation
	 */
	synchronized protected void updateCustom(CustomManagedCategory customManagedCategory,ExternalService ex) {
		if (log.isDebugEnabled()){
			log.debug("id="+this.getProfileId()+" - updateCustom("+customManagedCategory.getElementId()+",externalService)");
		}
		Iterator<SourceProfile> iterator = getSourceProfilesHash().values().iterator();
		
		while (iterator.hasNext()) {
			ManagedSourceProfile msp = (ManagedSourceProfile) iterator.next();
			log.debug("Managed Source profile ok");
			try {
				msp.updateCustomCategory(customManagedCategory,ex);
			} catch (ComputeFeaturesException e) {
				log.error("Impossible to update CustomCategory associated to category profile "+super.getProfileId()
						+" for managedSourceProfile "+msp.getId(),e);
			}
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
	synchronized public void setVisibility(VisibilitySets visibility) {
		this.visibility = visibility;
	}

//	/**
//	 * @return Returns the edit.
//	 */
//	public Editability getEdit() {
//		return edit;
//	}
//	
//
//	/**
//	 * @param edit The edit to set.
//	 */
//	synchronized public void setEdit(Editability edit) {
//		this.edit = edit;
//	}


		
	

}
