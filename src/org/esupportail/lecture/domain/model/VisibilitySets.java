/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.ExternalService;


/**
 * Visibility sets for managed categories and managed sources. 
 * @author gbouteil
 *
 */
public class VisibilitySets {

	/*
	 *************************** PROPERTIES ******************************** */	
	/**
	 * Log instance.
	 */
	protected static final Log LOG = LogFactory.getLog(VisibilitySets.class);
	/**
	 * Group of allowed users to subscribe to element.
	 */
	private DefinitionSets allowed = new DefinitionSets();
	/**
	 * Group of autoSubribed users to element (subscribed automatically, unsubscription possible).
	 */
	private DefinitionSets autoSubscribed = new DefinitionSets();
	/**
	 * Group of obliged users (no unsubscription possible).
	 */
	private DefinitionSets obliged = new DefinitionSets();

	/*
	 *************************** INIT  *********************************** */	


	/*
	 ************************** METHODS ********************************** */	
	
	/**
	 * Check existence of group names, attributes names used in group definition.
	 * Not used for the moment : see later
	 * Not ready to use without modification
	 * @deprecated
	 */
	@SuppressWarnings("deprecation")
	@Deprecated
	synchronized protected void checkNamesExistence(){
	   	if (LOG.isDebugEnabled()){
    		LOG.debug("checkNamesExistence()");
    	}
		allowed.checkNamesExistence();
		obliged.checkNamesExistence();
		autoSubscribed.checkNamesExistence();
	}
	
	
	/**
	 * Return the visibility mode for current user (user connected to externalService).
	 * @param ex externalService
	 * @return visibilityMode 
	 */
	synchronized protected VisibilityMode whichVisibility(final ExternalService ex){
		
		VisibilityMode mode = VisibilityMode.NOVISIBLE;
		
		boolean isVisible = false;
		
		isVisible = obliged.evaluateVisibility(ex);
		if (isVisible) {
			mode = VisibilityMode.OBLIGED;
		
		} else {
			isVisible = autoSubscribed.evaluateVisibility(ex);
			if (isVisible) {
				mode = VisibilityMode.AUTOSUBSCRIBED;
			
			} else {
				isVisible = allowed.evaluateVisibility(ex);
				if (isVisible) {
					mode = VisibilityMode.ALLOWED;
				} else {
					mode = VisibilityMode.NOVISIBLE;
				}
			}
		}
		return mode;
	}
	
	/**
	 * Returns a string containing VisibilitySets content : allowed group, autoSubscribed group
	 * and obliged group.
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		
		String string = "";
		string += "	   allowed : " + "\n" + allowed.toString();
		string += "           autoSubscribed : " + "\n" + autoSubscribed.toString() + "\n";
		string += "           obliged : " + "\n" +  obliged.toString() + "\n";
		
		return string;
	}


	/*
	 *************************** ACCESSORS ******************************** */	

	/**
	 * Returns allowed group visibility.
	 * @return allowed
	 * @see VisibilitySets#allowed
	 */
	public DefinitionSets getAllowed() {
		return allowed;
	}
	
	/**
	 * Sets allowed group visibility.
	 * @param allowed
	 * @see VisibilitySets#allowed
	 */
	public void setAllowed(final DefinitionSets allowed) {
		this.allowed = allowed;
	}

	/**
	 * Returns autoSubscribed group visibility.
	 * @return autoSubscribed
	 * @see VisibilitySets#autoSubscribed
	 */
	public DefinitionSets getAutoSubscribed() {
		return autoSubscribed;
	}
	
	/**
	 * Sets autoSubscribed group visibility.
	 * @param autoSubscribed
	 * @see VisibilitySets#autoSubscribed
	 */
	public void setAutoSubscribed(final DefinitionSets autoSubscribed) {
		this.autoSubscribed = autoSubscribed;
	}

	/**
	 * Returns obliged group visibility.
	 * @return obliged
	 * @see VisibilitySets#obliged
	 */
	public DefinitionSets getObliged() {
		return obliged;
	}
	
	/**
	 * Sets obliged group visibility.
	 * @param obliged
	 * @see VisibilitySets#obliged
	 */
	 public void setObliged(final DefinitionSets obliged) {
		this.obliged = obliged;
	}

	 /**
	  * @return if VisibilitySets is Empty or not
	  */
	 public boolean isEmpty() {
		 boolean ret = false;
		 if (allowed.isEmpty() && obliged.isEmpty() && autoSubscribed.isEmpty()) {
			 ret = true;
		 }
		 return ret;
	 }

}
