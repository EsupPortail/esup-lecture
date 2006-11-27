package org.esupportail.lecture.domain.model;

public class CustomManagedSource extends CustomSource{

	private String profileId;
	
	protected CustomManagedSource(ManagedSourceProfile profile) {
		super(profile);
		profileId = profile.getId();
		
	}

	
	public void setSourceProfileID(String profileId) {
		this.profileId = profileId;
		
	}
	
	
	public String getName() {
		return getSourceProfile().getName();
	}
	
}
