package org.esupportail.lecture.beans;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.model.ManagedCategoryProfile;
import org.esupportail.lecture.web.HomeEditUserBean;

public class EditCategoryBean {
	// Pour les tests
	String profileId;

	/**
	 * Log instance 
	 */
	protected static final Log log = LogFactory.getLog(EditCategoryBean.class); 
	public void init(ManagedCategoryProfile profile) {
		log.debug("init");
		profileId = profile.getId();
		
	}

	/**
	 * @return Returns the profileId.
	 */
	public String getProfileId() {
		return profileId;
	}

	/**
	 * @param profileId The profileId to set.
	 */
	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}

}
