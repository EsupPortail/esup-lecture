/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;

import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.DomainTools;
import org.esupportail.lecture.exceptions.domain.ManagedCategoryNotLoadedException;
import org.esupportail.lecture.exceptions.domain.ManagedCategoryProfileNotFoundException;
import org.esupportail.lecture.exceptions.domain.SourceProfileNotFoundException;

/**
 * Customizations on a managedSourceProfile for a user Profile.
 * @author gbouteil
 * @see CustomSource
 *
 */
public class CustomManagedSource extends CustomSource {
	
	/*
	 ************************** PROPERTIES *********************************/	
	
	/**
	 * Log instance.
	 */
	protected static final Log LOG = LogFactory.getLog(CustomManagedSource.class);
	
	/**
	 * managedSourceProfile refered by this CustomManagedSource.
	 */
	private ManagedSourceProfile sourceProfile;
	
	/*
	 ************************** INIT *********************************/	
	
	/**
	 * Constructor.
	 * @param profile profile refered by this CustomManagedSource
	 * @param user owner of this CustomManagedSource
	 */
	protected CustomManagedSource(final ManagedSourceProfile profile, final UserProfile user) {
		super(profile, user);
		if (LOG.isDebugEnabled()) {
			LOG.debug("CustomManagedSource(" + profile.getId() + "," + user.getUserId() + ")");
		}
		sourceProfile = profile;
	}
	
	/**
	 * default constructor.
	 */
	protected CustomManagedSource() {
		super();
		if (LOG.isDebugEnabled()) {
			LOG.debug("CustomManagedSource()");
		}
	}	
	
	/*
	 ************************** METHODS *********************************/	
	
	
	
	/* MISCELLANEOUS */
	
	/**
	 * Returns the ManagedSourceProfile of this CustomManagedSource.
	 * @throws SourceProfileNotFoundException 
	 * @throws ManagedCategoryProfileNotFoundException 
	 * @throws ManagedCategoryNotLoadedException 
	 * @see org.esupportail.lecture.domain.model.CustomSource#getProfile()
	 */
	@Override
	public SourceProfile getProfile() 
	throws SourceProfileNotFoundException, ManagedCategoryProfileNotFoundException, ManagedCategoryNotLoadedException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("id=" + super.getElementId() + " - getProfile()");
		}
		if (sourceProfile == null) {
			String categoryId = getManagedSourceProfileParentId();
			Channel channel = DomainTools.getChannel();
			ManagedCategoryProfile catProfile = channel.getManagedCategoryProfile(categoryId);
			sourceProfile = catProfile.getSourceProfileById(getElementId());
		}
		return sourceProfile;
	}
	
	/**
	 * Returns the Id of the Parent (a managedCategory) of sourceProfile referred by this CustomManagedSource.
	 * It gets it from the SourceProfile id (m:parentId:interneId)
	 * @return "parentId"  It is ID of the ManagedCategoryProfile
	 */
	public String getManagedSourceProfileParentId() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("getManagedSourceProfileParentId()");
		}
		// TODO (VR) : elementId has changed (contextId:m:profileId:sourceId)
		Pattern p = Pattern.compile(":");
		String[ ] items = p.split(this.getElementId());
		if (LOG.isDebugEnabled()) {
			LOG.debug(
					"" + "getManagedSourceProfileParentId() - decomposed ID : contextId=" + items[0] + " typeId=" + items[1] 
					+ " profileId=" + items[2] + " interneId=" + items[3]);
		}
		String parentId = items[0] + ":" + items[1] + ":" + items[2];
		return parentId;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer string = new StringBuffer(getClass().getSimpleName() + "#" + hashCode() 
				+ "[elementId=[" + getElementId()
				+ "], customSourcePK=[" + getCustomSourcePK() 
				+ "], userProfile.id=[" + getUserProfile().getUserId() 
				+ "], ItemDisplayMode=[" + getItemDisplayMode()
				+ "],");
		// readItems
		string.append("\n readItems=[");
		for (String it : getReadItems().keySet()) {
			string.append(it).append(", ");
		}
		string.append("]\n]");
		return string.toString();
	}
	
	
	/*
	 ************************** ACCESSORS *********************************/
	




	
}

	

	

