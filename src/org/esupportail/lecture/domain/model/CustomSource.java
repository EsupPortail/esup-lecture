package org.esupportail.lecture.domain.model;

import java.util.List;

import org.esupportail.lecture.domain.ExternalService;

public abstract class CustomSource implements CustomElement {

	
	/**
	 * The ID of related profilSource
	 */
	private String sourceProfileId;
	
	
	protected CustomSource(String sourceProfileId){
		this.sourceProfileId = sourceProfileId;
	}
	/**
	 * @param sourceProfile The sourceProfile to set.
	 */
	protected void setProfile(String sourceProfileId) {
		this.sourceProfileId = sourceProfileId;
	}

	public abstract SourceProfile getProfile();

//	 TODO : à retirer : pour les tests	
	public String getItemXPath() {
		return getProfile().getSource().getItemXPath();
	}
//	 TODO : à retirer : pour les tests	
	public String getXslt() {
		return getProfile().getSource().getXsltURL();
	}
	public List<Item> getItems(ExternalService externalService) {
		SourceProfile profile = getProfile();
		getProfile().getItems(externalService);
		return null;
	}
	public String getSourceProfileId() {
		return sourceProfileId;
	}
	public void setSourceProfileID(String sourceProfileId) {
		this.sourceProfileId = sourceProfileId;
	}
	
	

}
