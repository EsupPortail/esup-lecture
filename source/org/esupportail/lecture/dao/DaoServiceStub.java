package org.esupportail.lecture.dao;

import java.util.Hashtable;

import org.esupportail.lecture.dao.DaoService;
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
	 * UserProfiles to be stored in the channel
	 */
	Hashtable<String,UserProfile> userProfiles = new Hashtable<String,UserProfile>();	
	
	
	
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

}
