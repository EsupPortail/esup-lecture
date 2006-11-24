package org.esupportail.lecture.domain.model;

import java.util.List;

import org.esupportail.lecture.domain.ExternalService;

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
	
	public String getSourceProfileId(){
		return sourceProfile.getId();
	}
	
//	 TODO : à retirer : pour les tests	
	public String getItemXPath() {
		return sourceProfile.getSource().getItemXPath();
	}
//	 TODO : à retirer : pour les tests	
	public String getXslt() {
		return sourceProfile.getSource().getXsltURL();
	}
	
	public List<Item> getItems(ExternalService externalService) {
		return sourceProfile.getItems(externalService);
	}

}
