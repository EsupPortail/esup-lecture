package org.esupportail.lecture.domain.model;

public class CustomManagedSource extends CustomSource{

	protected CustomManagedSource(String profileId ) {
		super(profileId);
		
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
		return getProfile().getName();
	}



	public String getContent() {
		return getProfile().getContent();
	}

	

 
	
	
}
