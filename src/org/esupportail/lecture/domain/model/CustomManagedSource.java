package org.esupportail.lecture.domain.model;


/**
 * Customizations on a managedSource for a user Profile
 * @author gbouteil
 *
 */
public class CustomManagedSource extends CustomSource{
	
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

	

	

