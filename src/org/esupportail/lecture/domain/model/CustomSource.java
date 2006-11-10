package org.esupportail.lecture.domain.model;

public abstract class CustomSource implements CustomElement {

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
	
//	 TODO : à retirer : pour les tests	
	public String getItemXPath() {
		return sourceProfile.getSource().getItemXPath();
	}
//	 TODO : à retirer : pour les tests	
	public String getXslt() {
		return sourceProfile.getSource().getXsltURL();
	}

}
