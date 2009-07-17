/**
 * Doa Service implementation 
 */
package org.esupportail.lecture.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.commons.services.authentication.AuthenticationService;
import org.esupportail.lecture.domain.model.CustomCategory;
import org.esupportail.lecture.domain.model.CustomContext;
import org.esupportail.lecture.domain.model.CustomSource;
import org.esupportail.lecture.domain.model.ManagedCategory;
import org.esupportail.lecture.domain.model.ManagedCategoryProfile;
import org.esupportail.lecture.domain.model.ManagedSourceProfile;
import org.esupportail.lecture.domain.model.Source;
import org.esupportail.lecture.domain.model.UserProfile;
import org.esupportail.lecture.domain.model.VersionManager;
import org.esupportail.lecture.exceptions.dao.InfoDaoException;
import org.esupportail.lecture.exceptions.dao.InternalDaoException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * @author bourges
 *
 */
public class DaoServiceImpl implements DaoService, InitializingBean {
	/**
	 * Log instance.
	 */
	private static final Log LOG = LogFactory.getLog(DaoServiceImpl.class);
	/**
	 * remote XML service class.
	 */
	private DaoServiceRemoteXML remoteXMLService;
	/**
	 * hibernate service class.
	 */
	private DaoServiceHibernate hibernateService;
	/**
	 * the authentication Service.
	 */
	private AuthenticationService authenticationService;

	/**
	 * @throws InternalDaoException 
	 * @see org.esupportail.lecture.dao.DaoService#getManagedCategory(
	 *  org.esupportail.lecture.domain.model.ManagedCategoryProfile)
	 */
	public ManagedCategory getManagedCategory(final ManagedCategoryProfile profile) throws InternalDaoException  {
		if (LOG.isDebugEnabled()) {
			LOG.debug("in getManagedCategory");
		}
		return remoteXMLService.getManagedCategory(profile);
	}

	/** 
	 * @throws InternalDaoException 
	 * @see org.esupportail.lecture.dao.DaoService#getManagedCategory(
	 *  org.esupportail.lecture.domain.model.ManagedCategoryProfile, java.lang.String)
	 */
	public ManagedCategory getManagedCategory(
			final ManagedCategoryProfile profile,
			final String ptCas) throws InternalDaoException {
		return remoteXMLService.getManagedCategory(profile, ptCas);
	}

	/**
	 * @throws InternalDaoException 
	 * @see org.esupportail.lecture.dao.DaoService#getSource(
	 *  org.esupportail.lecture.domain.model.ManagedSourceProfile)
	 */
	public Source getSource(final ManagedSourceProfile profile) throws InternalDaoException {
		return remoteXMLService.getSource(profile);
	}

	/**
	 * @throws InternalDaoException 
	 * @see org.esupportail.lecture.dao.DaoService#getSource(
	 *  org.esupportail.lecture.domain.model.ManagedSourceProfile, java.lang.String)
	 */
	public Source getSource(final ManagedSourceProfile profile,
			final String ptCas) throws InternalDaoException  {
		return remoteXMLService.getSource(profile, ptCas);
	}

	/**
	 * @see org.esupportail.lecture.dao.DaoService#getUserProfile(java.lang.String)
	 */
	public UserProfile getUserProfile(final String userId) {
		return hibernateService.getUserProfile(userId);
	}

	/**
	 * @see org.esupportail.lecture.dao.DaoService#refreshUserProfile(
	 *  org.esupportail.lecture.domain.model.UserProfile)
	 */
	public UserProfile refreshUserProfile(final UserProfile userProfile) {
		return hibernateService.refreshUserProfile(userProfile);
	}

	/**
	 * @see org.esupportail.lecture.dao.DaoService#saveUserProfile(org.esupportail.lecture.domain.model.UserProfile)
	 */
	public void saveUserProfile(final UserProfile userProfile) {
		hibernateService.saveUserProfile(userProfile);
	}

	/**
	 * @see org.esupportail.lecture.dao.DaoService#deleteUserProfile(
	 *  org.esupportail.lecture.domain.model.UserProfile)
	 */
	public void deleteUserProfile(final UserProfile userProfile) {
		hibernateService.deleteUserProfile(userProfile);
	}

	/**
	 * @see org.esupportail.lecture.dao.DaoService#updateUserProfile(
	 *  org.esupportail.lecture.domain.model.UserProfile)
	 */
	public void updateUserProfile(final UserProfile userProfile) {
		hibernateService.updateUserProfile(userProfile);
	}

	/**
	 * @see org.esupportail.lecture.dao.DaoService#updateCustomContext(
	 *  org.esupportail.lecture.domain.model.CustomContext)
	 */
	public void updateCustomContext(final CustomContext customContext) {
		hibernateService.updateCustomContext(customContext);
	}

	/**
	 * @see org.esupportail.lecture.dao.DaoService#deleteCustomContext(
	 *  org.esupportail.lecture.domain.model.CustomContext)
	 */
	public void deleteCustomContext(final CustomContext cco) {
		hibernateService.deleteCustomContext(cco);
	}

	/**
	 * @see org.esupportail.lecture.dao.DaoService#deleteCustomCategory(
	 *  org.esupportail.lecture.domain.model.CustomCategory)
	 */
	public void deleteCustomCategory(final CustomCategory cca) {
		hibernateService.deleteCustomCategory(cca);
	}

	/**
	 * @see org.esupportail.lecture.dao.DaoService#updateCustomCategory(
	 *  org.esupportail.lecture.domain.model.CustomCategory)
	 */
	public void updateCustomCategory(final CustomCategory cca) {
		hibernateService.updateCustomCategory(cca);
	}

	/**
	 * @see org.esupportail.lecture.dao.DaoService#deleteCustomSource(
	 *  org.esupportail.lecture.domain.model.CustomSource)
	 */
	public void deleteCustomSource(final CustomSource cs) {
		hibernateService.deleteCustomSource(cs);
	}

	/**
	 * @see org.esupportail.lecture.dao.DaoService#updateCustomSource(
	 *  org.esupportail.lecture.domain.model.CustomSource)
	 */
	public void updateCustomSource(final CustomSource source) {
		hibernateService.updateCustomSource(source);
	}

	/**
	 * used by Spring.
	 * @param hibernateService class reference
	 */
	public void setHibernateService(final DaoServiceHibernate hibernateService) {
		this.hibernateService = hibernateService;
	}

	/**
	 * used by Spring.
	 * @param remoteXMLService class reference
	 */
	public void setRemoteXMLService(final DaoServiceRemoteXML remoteXMLService) {
		this.remoteXMLService = remoteXMLService;
	}

	/**
	 * @see org.esupportail.lecture.dao.DaoService#getVersionManagers()
	 */
	public List<VersionManager> getVersionManagers() {
		return hibernateService.getVersionManagers();
	}

	/**
	 * @see org.esupportail.lecture.dao.DaoService#addVersionManager(
	 *  org.esupportail.lecture.domain.model.VersionManager)
	 */
	public void addVersionManager(final VersionManager versionManager) {
		hibernateService.addVersionManager(versionManager);
	}

	/**
	 * @see org.esupportail.lecture.dao.DaoService#updateVersionManager(
	 *  org.esupportail.lecture.domain.model.VersionManager)
	 */
	public void updateVersionManager(final VersionManager versionManager) {
		hibernateService.updateVersionManager(versionManager);
	}

	/**
	 * @param authenticationService the authenticationService to set
	 */
	public void setAuthenticationService(final AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() {
		Assert.notNull(authenticationService, "property authenticationService of class "
				+ this.getClass().getName() + " can not be null");
		Assert.notNull(hibernateService, "property hibernateService of class "
				+ this.getClass().getName() + " can not be null");
		Assert.notNull(remoteXMLService, "property remoteXMLService of class "
				+ this.getClass().getName() + " can not be null");
	}

	@Override
	public UserProfile mergeUserProfile(UserProfile userProfile) {
		return hibernateService.mergeUserProfile(userProfile);
	}

}
