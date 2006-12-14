/**
 * Doa Service implementation 
 */
package org.esupportail.lecture.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.model.CustomCategory;
import org.esupportail.lecture.domain.model.CustomContext;
import org.esupportail.lecture.domain.model.CustomSource;
import org.esupportail.lecture.domain.model.ManagedCategory;
import org.esupportail.lecture.domain.model.ManagedCategoryProfile;
import org.esupportail.lecture.domain.model.ManagedSourceProfile;
import org.esupportail.lecture.domain.model.Source;
import org.esupportail.lecture.domain.model.UserProfile;
import org.hibernate.Transaction;

import sun.net.www.http.Hurryable;

/**
 * @author bourges
 *
 */
public class DaoServiceImpl implements DaoService {
	/**
	 * Log instance 
	 */
	private static final Log log = LogFactory.getLog(DaoServiceImpl.class);
	/**
	 * remote XML service class
	 */
	private DaoServiceRemoteXML remoteXMLService;
	/**
	 * hibernate service class
	 */
	private DaoServiceHibernate hibernateService;

	/**
	 * @see org.esupportail.lecture.dao.DaoService#getManagedCategory(org.esupportail.lecture.domain.model.ManagedCategoryProfile)
	 */
	public ManagedCategory getManagedCategory(ManagedCategoryProfile profile) {
		return remoteXMLService.getManagedCategory(profile);
	}

	/**
	 * @see org.esupportail.lecture.dao.DaoService#getManagedCategory(org.esupportail.lecture.domain.model.ManagedCategoryProfile, java.lang.String)
	 */
	public ManagedCategory getManagedCategory(ManagedCategoryProfile profile,
			String ptCas) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.esupportail.lecture.dao.DaoService#getSource(org.esupportail.lecture.domain.model.ManagedSourceProfile)
	 */
	public Source getSource(ManagedSourceProfile profile) {
		return remoteXMLService.getSource(profile);
	}

	/**
	 * @see org.esupportail.lecture.dao.DaoService#getSource(org.esupportail.lecture.domain.model.ManagedSourceProfile, java.lang.String)
	 */
	public Source getSource(ManagedSourceProfile profile, String ptCas) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.esupportail.lecture.dao.DaoService#getUserProfile(java.lang.String)
	 */
	public UserProfile getUserProfile(String userId) {
		return hibernateService.getUserProfile(userId);
	}

	/**
	 * @see org.esupportail.lecture.dao.DaoService#saveUserProfile(org.esupportail.lecture.domain.model.UserProfile)
	 */
	public void saveUserProfile(UserProfile userProfile) {
		hibernateService.saveUserProfile(userProfile);
	}

	/**
	 * @see org.esupportail.lecture.dao.DaoService#deleteUserProfile(org.esupportail.lecture.domain.model.UserProfile)
	 */
	public void deleteUserProfile(UserProfile userProfile) {
		hibernateService.deleteUserProfile(userProfile);
	}

	/**
	 * @see org.esupportail.lecture.dao.DaoService#updateUserProfile(org.esupportail.lecture.domain.model.UserProfile)
	 */
	public void updateUserProfile(UserProfile userProfile) {
		hibernateService.updateUserProfile(userProfile);
	}

	/**
	 * @see org.esupportail.lecture.dao.DaoService#updateCustomContext(org.esupportail.lecture.domain.model.CustomContext)
	 */
	public void updateCustomContext(CustomContext customContext) {
		hibernateService.updateCustomContext(customContext);
	}

	/**
	 * @see org.esupportail.lecture.dao.DaoService#deleteCustomContext(org.esupportail.lecture.domain.model.CustomContext)
	 */
	public void deleteCustomContext(CustomContext cco) {
		hibernateService.deleteCustomContext(cco);
	}

	/**
	 * @see org.esupportail.lecture.dao.DaoService#deleteCustomCategory(org.esupportail.lecture.domain.model.CustomCategory)
	 */
	public void deleteCustomCategory(CustomCategory cca) {
		hibernateService.deleteCustomCategory(cca);
	}

	/**
	 * @see org.esupportail.lecture.dao.DaoService#updateCustomCategory(org.esupportail.lecture.domain.model.CustomCategory)
	 */
	public void updateCustomCategory(CustomCategory cca) {
		hibernateService.updateCustomCategory(cca);
	}

	/**
	 * @see org.esupportail.lecture.dao.DaoService#deleteCustomSource(org.esupportail.lecture.domain.model.CustomSource)
	 */
	public void deleteCustomSource(CustomSource cs) {
		hibernateService.deleteCustomSource(cs);
	}

	/**
	 * @see org.esupportail.lecture.dao.DaoService#updateCustomSource(org.esupportail.lecture.domain.model.CustomSource)
	 */
	public void updateCustomSource(CustomSource source) {
		hibernateService.updateCustomSource(source);
	}

	/**
	 * used by Spring
	 * @param hibernateService class reference
	 */
	public void setHibernateService(DaoServiceHibernate hibernateService) {
		this.hibernateService = hibernateService;
	}

	/**
	 * used by Spring
	 * @param remoteXMLService class reference
	 */
	public void setRemoteXMLService(DaoServiceRemoteXML remoteXMLService) {
		this.remoteXMLService = remoteXMLService;
	}
	
}
