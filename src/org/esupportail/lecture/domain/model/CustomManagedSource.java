/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Customizations on a managedSourceProfile for a user Profile
 * @author gbouteil
 *
 */
public class CustomManagedSource extends CustomSource{
	
	/*
	 ************************** PROPERTIES *********************************/	
	
	/**
	 * Log instance
	 */
	protected static final Log log = LogFactory.getLog(CustomManagedSource.class);
	/**
	 * managedSourceProfile refered by this CustomManagedSource
	 */
	private ManagedSourceProfile sourceProfile;
	
	/*
	 ************************** INIT *********************************/	
	
	/**
	 * Constructor
	 * @param profile profile refered by this CustomManagedSource
	 * @param user owner of this CustomManagedSource
	 */
	protected CustomManagedSource(ManagedSourceProfile profile, UserProfile user) {
		super(profile, user);
		if (log.isDebugEnabled()){
			log.debug("CustomManagedSource("+profile.getId()+","+user.getUserId()+")");
		}
		sourceProfile = profile;
	}
	
	/*
	 ************************** METHODS *********************************/	
	
	/*
	 ************************** ACCESSORS *********************************/
	
	/**
	 * Returns the ManagedSourceProfile of this CustomManagedSource
	 * @see org.esupportail.lecture.domain.model.CustomSource#getProfile()
	 */
	@Override
	public SourceProfile getProfile() {
		return sourceProfile;
	}

	
}

	

	

