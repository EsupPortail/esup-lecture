package org.esupportail.lecture.domain.model;

import java.util.List;

import org.esupportail.lecture.domain.ExternalService;
import org.esupportail.lecture.exceptions.ComposantNotLoadedException;
import org.esupportail.lecture.exceptions.SourceNotLoadedException;

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
	
//	 TODO (GB) à retirer : pour les tests	
	public String getItemXPath() throws SourceNotLoadedException {
		return sourceProfile.getSource().getItemXPath();
	}
//	 TODO (GB) à retirer : pour les tests	
	public String getXslt() throws SourceNotLoadedException {
		return sourceProfile.getSource().getXsltURL();
	}
	
	public List<Item> getItems(ExternalService externalService) throws ComposantNotLoadedException, SourceNotLoadedException {
		return sourceProfile.getItems(externalService);
	}

}
