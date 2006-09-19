package org.esupportail.lecture.dao;

import java.util.Hashtable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.dao.DaoService;
import org.esupportail.lecture.domain.model.CustomContext;
import org.esupportail.lecture.domain.model.ManagedCategory;
import org.esupportail.lecture.domain.model.UserProfile;
/**
 * Stub Service to Data Access Object : use to test upper layers, instead of using 
 * a database for example
 * @author gbouteil
 *
 */
public class DaoServiceStub  implements DaoService {
	
	/* 
	 *************************** PROPERTIES *********************************/	

	/**
	 * Log instance
	 */
	protected static final Log log = LogFactory.getLog(DaoServiceStub.class);

	/**
	 * UserProfiles to be stored in the channel
	 */
	Hashtable<String,UserProfile> userProfiles;	
	

	
	/* 
	 *************************** INITIALIZATION *********************************/	

	/**
	 * Constructor
	 */
	public DaoServiceStub() {
		userProfiles = new Hashtable<String,UserProfile>();
	}
	
	/* 
	 *************************** ACCESSORS *********************************/	

	/**
	 * @see org.esupportail.lecture.dao.DaoService#getUserProfile(java.lang.String)
	 */
	public UserProfile getUserProfile(String userId) {
		return userProfiles.get(userId);
	}

	
	/**
	 * @see org.esupportail.lecture.dao.DaoService#addUserProfile(org.esupportail.lecture.domain.model.UserProfile)
	 */
	public void addUserProfile(UserProfile userProfile) {
		userProfiles.put(userProfile.getUserId(),userProfile);
	}

	/**
	 * @see org.esupportail.lecture.dao.DaoService#getCategory(java.lang.String, int, java.lang.String)
	 */
	public ManagedCategory getCategory(String urlCategory, int ttl, String profileId) {
		ManagedCategory cat = new ManagedCategory();
		cat.setName("Ma belle categorie de test : "+profileId);
		
		return cat;
	}

	public ManagedCategory getCategory(String urlCategory, int ttl, String profileId, String ptCAS) {
		ManagedCategory cat = new ManagedCategory();
		cat.setName("Ma belle categorie de test : "+profileId);
		
		return cat;
	}


}
