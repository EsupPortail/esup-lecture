package org.esupportail.lecture.dao;

import java.util.Hashtable;
import java.util.List;

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
import org.esupportail.lecture.domain.model.VersionManager;
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
	
	/**
	 * @see org.esupportail.lecture.dao.DaoService#getManagedCategory(org.esupportail.lecture.domain.model.ManagedCategoryProfile)
	 */
	public ManagedCategory getManagedCategory(ManagedCategoryProfile profile) throws TimeoutException {
		log.debug("getManagedCategory("+profile.getId()+")");
		return remoteXMLService.getManagedCategory(profile);
	}
	
	/**
	 * @see org.esupportail.lecture.dao.DaoService#getManagedCategory(org.esupportail.lecture.domain.model.ManagedCategoryProfile, java.lang.String)
	 */
	public ManagedCategory getManagedCategory(@SuppressWarnings("unused")
	ManagedCategoryProfile profile, @SuppressWarnings("unused")
	String ptCas) {
		return null;
	}
	
	/**
	 * @see org.esupportail.lecture.dao.DaoService#getSource(org.esupportail.lecture.domain.model.ManagedSourceProfile)
	 */
	public Source getSource(ManagedSourceProfile profile) throws TimeoutException {
		return this.remoteXMLService.getSource(profile);
	}

	/**
	 * @see org.esupportail.lecture.dao.DaoService#getSource(org.esupportail.lecture.domain.model.ManagedSourceProfile, java.lang.String)
	 */
	public Source getSource(@SuppressWarnings("unused")
	ManagedSourceProfile profile, @SuppressWarnings("unused")
	String ptCas) {
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

	/**
	 * @see org.esupportail.lecture.dao.DaoService#saveUserProfile(org.esupportail.lecture.domain.model.UserProfile)
	 */
	public void saveUserProfile(UserProfile userProfile) {
		if (log.isDebugEnabled()){
			log.debug("saveUserProfile("+userProfile.getUserId()+")");
		}
		userProfiles.put(userProfile.getUserId(),userProfile);
	}
	
	/**
	 * @see org.esupportail.lecture.dao.DaoService#deleteUserProfile(org.esupportail.lecture.domain.model.UserProfile)
	 */
	public void deleteUserProfile(UserProfile userProfile) {
		if (log.isDebugEnabled()){
			log.debug("deleteUserProfile("+userProfile.getUserId()+")");
		}
		userProfiles.remove(userProfile.getUserId());
		
	}
	
	/**
	 * @see org.esupportail.lecture.dao.DaoService#updateUserProfile(org.esupportail.lecture.domain.model.UserProfile)
	 */
	public void updateUserProfile(UserProfile userProfile) {
		if (log.isDebugEnabled()){
			log.debug("updateUserProfile("+userProfile.getUserId()+")");
		}
	}

	/* CustomContext */
	
	/**
	 * @see org.esupportail.lecture.dao.DaoService#updateCustomContext(org.esupportail.lecture.domain.model.CustomContext)
	 */	
	public void updateCustomContext(CustomContext customContext) {
		if (log.isDebugEnabled()){
			log.debug("updateCustomContext("+customContext.getElementId()+")");
		}
	}

	/**
	 * @see org.esupportail.lecture.dao.DaoService#deleteCustomContext(org.esupportail.lecture.domain.model.CustomContext)
	 */
	public void deleteCustomContext(CustomContext cco) {
		if (log.isDebugEnabled()){
			log.debug("deleteCustomContext("+cco.getElementId()+")");
		}
	}

	/* CustomCategory */
	
	/**
	 * @see org.esupportail.lecture.dao.DaoService#updateCustomCategory(org.esupportail.lecture.domain.model.CustomCategory)
	 */
	public void updateCustomCategory(CustomCategory cca) {
		if (log.isDebugEnabled()){
			log.debug("updateCustomCategory("+cca.getElementId()+"");
		}
	}

	/**
	 * @see org.esupportail.lecture.dao.DaoService#deleteCustomCategory(org.esupportail.lecture.domain.model.CustomCategory)
	 */
	public void deleteCustomCategory(CustomCategory cca) {
		if (log.isDebugEnabled()){
			log.debug("deleteCustomCategory("+cca.getElementId()+")");
		}
	}
	
	/* CustomSource */
	

	/**
	 * @see org.esupportail.lecture.dao.DaoService#deleteCustomSource(org.esupportail.lecture.domain.model.CustomSource)
	 */
	public void deleteCustomSource(CustomSource cs) {
		if (log.isDebugEnabled()){
			log.debug("deleteCustomSource("+cs.getElementId()+")");
		}
	}

	/**
	 * @see org.esupportail.lecture.dao.DaoService#updateCustomSource(org.esupportail.lecture.domain.model.CustomSource)
	 */
	public void updateCustomSource(CustomSource source) {
		if (log.isDebugEnabled()){
			log.debug("updateCustomSource("+source.getElementId()+")");
		}
	}

	

	/* 
	 *************************** ACCESSORS *********************************/	
	

	/**
	 * @param remoteXML
	 */
	public void setRemoteXMLService(DaoServiceRemoteXML remoteXML) {
		this.remoteXMLService = remoteXML;
	}
	

	/**
	 * @return Returns the remoteXMLService.
	 */
	public DaoServiceRemoteXML getRemoteXMLService() {
		return remoteXMLService;
	}

	/**
	 * @see org.esupportail.lecture.dao.DaoService#getVersionManagers()
	 */
	public List<VersionManager> getVersionManagers() {
		return null;
	}

	/**
	 * @see org.esupportail.lecture.dao.DaoService#addVersionManager(org.esupportail.lecture.domain.model.VersionManager)
	 */
	public void addVersionManager(@SuppressWarnings("unused")
	VersionManager versionManager) {
		// TODO Auto-generated method stub
	}

	/**
	 * @see org.esupportail.lecture.dao.DaoService#updateVersionManager(org.esupportail.lecture.domain.model.VersionManager)
	 */
	public void updateVersionManager(@SuppressWarnings("unused")
	VersionManager versionManager) {
		// TODO Auto-generated method stub
		
	}


}
