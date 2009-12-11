/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;


/**
 * Global source element :
 * Source defined in global environnement channel.
 * This source is loaded for every users
 * @author gbouteil
 * @see Source
 *
 */ 
public class GlobalSource extends Source {

	


/* 
 *************************** PROPERTIES ******************************** */	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*
 *************************** INIT ************************************** */	
	
	/**
	 * Constructor.
	 * @param profile 
	 */
	public GlobalSource(final SourceProfile profile) {
		super(profile);
	   	if (LOG.isDebugEnabled()) {
    		LOG.debug("GlobalSource(" + profile.getId() + ")");
    	}
	}
	
/*
 *************************** METHODS *********************************** */	

/*
 *************************** ACCESSORS ********************************* */	

}
