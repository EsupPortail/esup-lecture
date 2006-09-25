package org.esupportail.lecture.domain.model;

public abstract class CustomSource {

	private SourceProfile sourceProfile;
	
	
	
	protected CustomSource(SourceProfile profile){
		sourceProfile = profile;
	}
	/**
	 * @param sourceProfile The sourceProfile to set.
	 */
	protected void setSourceProfile(SourceProfile sourceProfile) {
		this.sourceProfile = sourceProfile;
	}

	public SourceProfile getSourceProfile(){
		return sourceProfile;
	}

}
