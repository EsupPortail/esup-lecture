package org.esupportail.lecture.dao;

import java.util.Hashtable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.dao.DaoService;
import org.esupportail.lecture.dao.DaoServiceRemoteXML;
import org.esupportail.lecture.domain.model.CustomCategory;
import org.esupportail.lecture.domain.model.CustomContext;
import org.esupportail.lecture.domain.model.CustomSource;
import org.esupportail.lecture.domain.model.ManagedCategory;
import org.esupportail.lecture.domain.model.ManagedCategoryProfile;
import org.esupportail.lecture.domain.model.ManagedSourceProfile;
import org.esupportail.lecture.domain.model.Source;
import org.esupportail.lecture.domain.model.UserProfile;
import org.esupportail.lecture.exceptions.dao.TimeoutException;
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

	/**
	 *  Access to DaoServiceRemoteXml
	 */
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
	 *************************** METHODS *********************************/	
	
	/* Remote Data */
	
	public ManagedCategory getManagedCategory(ManagedCategoryProfile profile) throws TimeoutException {
		log.debug("getManagedCategory("+profile.getId()+")");
		return remoteXMLService.getManagedCategory(profile);
	}
	
	public ManagedCategory getManagedCategory(ManagedCategoryProfile profile, String ptCas) {
		return null;
	}
	
	public Source getSource(ManagedSourceProfile profile) throws TimeoutException {
		return this.remoteXMLService.getSource(profile);
	}

	public Source getSource(ManagedSourceProfile profile, String ptCas) {
		return null;
	}
	
	/* user Profiles */
	
	/**
	 * @see org.esupportail.lecture.dao.DaoService#getUserProfile(java.lang.String)
	 */
	public UserProfile getUserProfile(String userId) {
		if (log.isDebugEnabled()){
			log.debug("getUserProfile("+userId+")");
		}
		return userProfiles.get(userId);
	}

	/**
	 * @see org.esupportail.lecture.dao.DaoService#refreshUserProfile(org.esupportail.lecture.domain.model.UserProfile)
	 */
	public UserProfile refreshUserProfile(UserProfile userProfile) {
		if (log.isDebugEnabled()){
			log.debug("refresh("+userProfile.getUserId()+")");
		}
		return userProfile;
	}

	public void saveUserProfile(UserProfile userProfile) {
		if (log.isDebugEnabled()){
			log.debug("saveUserProfile("+userProfile.getUserId()+")");
		}
		userProfiles.put(userProfile.getUserId(),userProfile);
	}
	
	public void deleteUserProfile(UserProfile userProfile) {
		if (log.isDebugEnabled()){
			log.debug("deleteUserProfile("+userProfile.getUserId()+")");
		}
		userProfiles.remove(userProfile.getUserId());
		
	}
	
	public void updateUserProfile(UserProfile userProfile) {
		if (log.isDebugEnabled()){
			log.debug("updateUserProfile("+userProfile.getUserId()+")");
		}
	}

	/* CustomContext */
	
	public void updateCustomContext(CustomContext customContext) {
		if (log.isDebugEnabled()){
			log.debug("updateCustomContext("+customContext.getElementId()+")");
		}
	}

	public void deleteCustomContext(CustomContext cco) {
		if (log.isDebugEnabled()){
			log.debug("deleteCustomContext("+cco.getElementId()+")");
		}
	}

	/* CustomCategory */
	
	public void updateCustomCategory(CustomCategory cca) {
		if (log.isDebugEnabled()){
			log.debug("updateCustomCategory("+cca.getElementId()+"");
		}
	}

	public void deleteCustomCategory(CustomCategory cca) {
		if (log.isDebugEnabled()){
			log.debug("deleteCustomCategory("+cca.getElementId()+")");
		}
	}

	/* CustomSource */
	
	public void deleteCustomSource(CustomSource cs) {
		if (log.isDebugEnabled()){
			log.debug("deleteCustomSource("+cs.getElementId()+")");
		}
	}

	public void updateCustomSource(CustomSource source) {
		if (log.isDebugEnabled()){
			log.debug("updateCustomSource("+source.getElementId()+")");
		}
	}

	

	/* 
	 *************************** ACCESSORS *********************************/	
	



	public void setRemoteXMLService(DaoServiceRemoteXML remoteXML) {
		this.remoteXMLService = remoteXML;
	}
	

	/**
	 * @return Returns the remoteXMLService.
	 */
	public DaoServiceRemoteXML getRemoteXMLService() {
		return remoteXMLService;
	}


}
