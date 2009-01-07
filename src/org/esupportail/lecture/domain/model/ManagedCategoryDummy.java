/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-lecture.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;

import java.util.List;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.DomainTools;
import org.esupportail.lecture.domain.utils.DummyInterface;
import org.esupportail.lecture.exceptions.LectureException;
import org.esupportail.lecture.exceptions.dao.InfoDaoException;

/**
 * CategoryDummy element : a category that cannot be created well.
 * @author gbouteil
 *
 */
@SuppressWarnings("serial")
public class ManagedCategoryDummy extends ManagedCategory implements DummyInterface {

	/* 
	 *************************** PROPERTIES ******************************** */
	/**
	 * log instance.
	 */
	protected static final Log LOG = LogFactory.getLog(Source.class); 
	/**
	 * Cause of the Dummy Bean.
	 */
	private InfoDaoException cause;
	

	/*
	 *************************** INIT ************************************** */
	/**
	 * Constructor.
	 * @param cp sourceProfile associated to this source
	 * @param e 
	 */
	public ManagedCategoryDummy(final ManagedCategoryProfile cp, final InfoDaoException e) {
		super(cp);
		cause = e;
	   	if (LOG.isDebugEnabled()) {
    		LOG.debug("ManagedCategoryDummy(" + cp.getId() + ")");
    	}  	
	}
	
	/*
	 *************************** METHODS *********************************** */
	
	/**
	 * @return the ttl of a dummy : declared in config
	 */
	@Override
	public int getTtl() {
		return DomainTools.getDummyTtl();
	}
	
	
	
	
	/* 
	 *************************** INNER CLASS ******************************** */	
	
	
	
	/* UPDATING */

	/**
	 * Update the CustomManagedCategory linked to this ManagedCategory.
	 * It sets up subscriptions of customManagedCategory on managedSourcesProfiles
	 * defined in ths ManagedCategory, according to managedSourceProfiles visibility
	 * (there is not any loading of source at this time)
	 * @param customManagedCategory customManagedCategory to update
	 */
	@Override
	protected synchronized void updateCustom(final CustomManagedCategory customManagedCategory) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("id = " + getProfileId() + " - updateCustom("
					+ customManagedCategory.getElementId() + ")");
		}
		// Nothing to do
	}
	
	/**
	 * Return a list of (SourceProfile,VisibilityMode).
	 * Corresponding to visible sources for user
	 * in this ManagedCategory and update it (like methode updateCustom): 
	 * It sets up subscriptions of customManagedCategory on managedSourcesProfiles
	 * defined in ths ManagedCategory, according to managedSourceProfiles visibility
	 * (there is not any loading of source at this time)
	 * @param customManagedCategory custom to update
	 * @return list of CoupleProfileVisibility
	 */
	@Override
	protected List<CoupleProfileVisibility> getVisibleSourcesAndUpdateCustom(
			final CustomManagedCategory customManagedCategory) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("id=" + getProfileId() + " - getVisibleSourcesAndUpdateCustom("
					+ getProfileId() + ")");
		}
		List<CoupleProfileVisibility> couplesVisib = new Vector<CoupleProfileVisibility>();
		return couplesVisib;
	}

	
	
	/*
	 *************************** ACCESSORS ********************************* */

	/**
	 * @see org.esupportail.lecture.domain.utils.DummyInterface#getCause()
	 */
	public LectureException getCause() {
		return cause;
	}

}
