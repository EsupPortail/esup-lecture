package org.esupportail.lecture.dao;

import java.util.Hashtable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.dao.DaoService;
import org.esupportail.lecture.dao.impl.DaoServiceRemoteXML;
import org.esupportail.lecture.domain.model.Category;
import org.esupportail.lecture.domain.model.CustomContext;
import org.esupportail.lecture.domain.model.ManagedCategory;
import org.esupportail.lecture.domain.model.ManagedCategoryProfile;
import org.esupportail.lecture.domain.model.ManagedSourceProfile;
import org.esupportail.lecture.domain.model.Source;
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

	private DaoServiceRemoteXML remoteXMLService;	
	

	
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

	



	public void setRemoteXMLService(DaoServiceRemoteXML remoteXML) {
		this.remoteXMLService = remoteXML;
	}
	

	public void addCustomContext(CustomContext customContext) {
		// TODO Auto-generated method stub
		
	}
	

	public void deleteUserProfile(UserProfile userProfile) {
		// TODO Auto-generated method stub
		
	}

	public ManagedCategory getManagedCategory(ManagedCategoryProfile profile) {
		return remoteXMLService.getManagedCategory(profile);
		
	}

	/**
	 * @return Returns the remoteXMLService.
	 */
	public DaoServiceRemoteXML getRemoteXMLService() {
		return remoteXMLService;
	}

	public Category getManagedCategory(ManagedCategoryProfile profile, String ptCas) {
		// TODO Auto-generated method stub
		return null;
	}

	public Source getSource(ManagedSourceProfile profile, String ptCas) {
		// TODO Auto-generated method stub
		return null;
	}

	public Source getSource(ManagedSourceProfile profile) {
		// TODO Auto-generated method stub
		return null;
	}



}
