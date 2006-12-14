package org.esupportail.lecture.domain.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Customizations on a managedSource for a user Profile
 * @author gbouteil
 *
 */
public class CustomManagedSource extends CustomSource{
	
	protected static final Log log = LogFactory.getLog(CustomManagedSource.class);
	/**
	 * managedSourcePRofile refered by this
	 */
	private ManagedSourceProfile sourceProfile;

	/**
	 * Constructor
	 * @param profile profile of eleemnt refered by this
	 * @param user owner of this custom element
	 */
	public CustomManagedSource(ManagedSourceProfile profile, UserProfile user) {
		super(profile, user);
		if (log.isDebugEnabled()){
			log.debug("CustomManagedSource("+profile.getId()+","+user.getUserId()+")");
		}
		sourceProfile = profile;
	}

	/**
	 * @see org.esupportail.lecture.domain.model.CustomSource#getProfile()
	 */
	@Override
	public SourceProfile getProfile() {
		return sourceProfile;
	}

	
}

	

	

