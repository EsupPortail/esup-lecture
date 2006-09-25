package org.esupportail.lecture.domain.model;

import org.esupportail.lecture.domain.DomainTools;

public class CustomManagedSource extends CustomSource{

	protected CustomManagedSource(ManagedSourceProfile profile) {
		super(profile);
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

	

 
	
	
}
