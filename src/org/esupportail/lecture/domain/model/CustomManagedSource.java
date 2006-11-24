package org.esupportail.lecture.domain.model;

public class CustomManagedSource extends CustomSource{

	protected CustomManagedSource(ManagedSourceProfile profile) {
		super(profile);
		profileId = profile.getId();
		
	}



	private String profileId;
	
	public void setSourceProfileID(String profileId) {
		this.profileId = profileId;
		
	}
	
	
	
	/**
	 * @param managedSourceProfileId The managedSourceProfileId to set.
	 */
	protected void setManagedCategoryProfileId(String managedSourceProfileId) {
		managedSourceProfileId = managedSourceProfileId;
	}



	public String getName() {
		return getSourceProfile().getName();
	}



	public String getContent() {
		return getSourceProfile().getContent();
	}

	

 
	
	
}
