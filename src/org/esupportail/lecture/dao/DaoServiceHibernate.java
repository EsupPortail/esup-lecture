/**
 * Hibernate Doa Service implementation 
 */
package org.esupportail.lecture.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.model.CustomCategory;
import org.esupportail.lecture.domain.model.CustomContext;
import org.esupportail.lecture.domain.model.CustomSource;
import org.esupportail.lecture.domain.model.UserProfile;
import org.esupportail.lecture.domain.model.VersionManager;
import org.hibernate.LockMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * @author bourges
 */
public class DaoServiceHibernate extends HibernateDaoSupport {

	/**
	 * Log instance. 
	 */
	private static final Log LOG = LogFactory.getLog(DaoServiceHibernate.class);
	
	/**
	 * boolean flag in order to use flush during work.
	 * should be false (true for test)
	 */
	private static final boolean USEFLUSH = false;

	/**
	 * Default constructor.
	 */
	public DaoServiceHibernate() {
		super();
	}

	/**
	 * @param userId 
	 * @return UserProfile
	 * @see org.esupportail.lecture.dao.DaoService#getUserProfile(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public UserProfile getUserProfile(final String userId) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("getUserProfile(" + userId + ")");			
		}
		UserProfile ret = null;
		if (userId != null) {
			String query = "select userProfile from UserProfile userProfile where userProfile.userId = ?";
		    List<UserProfile> list = getHibernateTemplate().find(query, userId);
		    if (list.size() > 0) {
			    ret = list.get(0);				
			}
		} else {
			String msg = "userId is null: can't find it in database";
			LOG.error(msg);
			throw new RuntimeException(msg);
		}
		return ret;
	}

	/**
	 * @param userProfile 
	 * @return userProfile
	 * @see org.esupportail.lecture.dao.DaoService#refreshUserProfile(org.esupportail.lecture.domain.model.UserProfile)
	 */
	public UserProfile refreshUserProfile(final UserProfile userProfile) {
		//TODO (RB) renommer en attachCleanUserProfile
		if (LOG.isDebugEnabled()) {
			LOG.debug("refreshUserProfile(" 
				+ userProfile + ")");			
		}
		UserProfile ret = userProfile;
		//getHibernateTemplate().lock(userProfile, LockMode.NONE);
		//ret = (UserProfile) getHibernateTemplate().merge(userProfile);
		// update object in the hibernate session
		getHibernateTemplate().update(userProfile); 
		return ret;
	}

	/**
	 * @param userProfile 
	 * @see org.esupportail.lecture.dao.DaoService#saveUserProfile(org.esupportail.lecture.domain.model.UserProfile)
	 */
	public void saveUserProfile(final UserProfile userProfile) {
		//TODO (RB/GB) Pourquoi n'existe-t-il pas de saveCustomContrxt, saveCustomCategory, saveCustomSource ? 
		if (LOG.isDebugEnabled()) {
			LOG.debug("saveUserProfile PK=" + userProfile.getUserProfilePK());			
		}
		//Object merged = getHibernateTemplate().merge(userProfile);
		getHibernateTemplate().saveOrUpdate(userProfile);
		if (USEFLUSH) {
			getHibernateTemplate().flush();
		} 
	}

	/**
	 * @param userProfile 
	 * @see org.esupportail.lecture.dao.DaoService#deleteUserProfile(org.esupportail.lecture.domain.model.UserProfile)
	 */
	public void deleteUserProfile(final UserProfile userProfile) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("deleteUserProfile PK=" + userProfile.getUserProfilePK());			
		}
		getHibernateTemplate().delete(userProfile);
		if (USEFLUSH) {
			getHibernateTemplate().flush();
		} 
	}

	/**
	 * @param userProfile 
	 * @see org.esupportail.lecture.dao.DaoService#updateUserProfile(org.esupportail.lecture.domain.model.UserProfile)
	 */
	public void updateUserProfile(final UserProfile userProfile) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("updateUserProfile(" + userProfile + ")");			
		}
		//Object merged = getHibernateTemplate().merge(userProfile);
		getHibernateTemplate().saveOrUpdate(userProfile);
		if (USEFLUSH) {
			getHibernateTemplate().flush();
		} 
	}

	/**
	 * @param customContext 
	 * @see org.esupportail.lecture.dao.DaoService#updateCustomContext(org.esupportail.lecture.domain.model.CustomContext)
	 */
	public void updateCustomContext(final CustomContext customContext) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("updateCustomContext PK=" + customContext.getCustomContextPK());			
		}
		//Object merged = getHibernateTemplate().merge(customContext);
		getHibernateTemplate().saveOrUpdate(customContext);
	}

	/**
	 * @param cco 
	 * @see org.esupportail.lecture.dao.DaoService#deleteCustomContext(org.esupportail.lecture.domain.model.CustomContext)
	 */
	public void deleteCustomContext(final CustomContext cco) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("deleteCustomContext PK=" + cco.getCustomContextPK());			
		}
		getHibernateTemplate().delete(cco);
		if (USEFLUSH) {
			getHibernateTemplate().flush();
		} 
	}

	/**
	 * @param cca 
	 * @see org.esupportail.lecture.dao.DaoService#deleteCustomCategory(org.esupportail.lecture.domain.model.CustomCategory)
	 */
	public void deleteCustomCategory(final CustomCategory cca) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("deleteCustomCategory PK=" + cca.getCustomCategoryPK());			
		}
		getHibernateTemplate().delete(cca);
		if (USEFLUSH) {
			getHibernateTemplate().flush();
		} 
	}

	/**
	 * @param cca 
	 * @see org.esupportail.lecture.dao.DaoService#updateCustomCategory(org.esupportail.lecture.domain.model.CustomCategory)
	 */
	public void updateCustomCategory(final CustomCategory cca) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("updateCustomCategory PK=" + cca.getCustomCategoryPK());			
		}
		//Object merged = getHibernateTemplate().merge(cca);
		getHibernateTemplate().saveOrUpdate(cca);
		if (USEFLUSH) {
			getHibernateTemplate().flush();
		} 
	}

	/**
	 * @param cs 
	 * @see org.esupportail.lecture.dao.DaoService#deleteCustomSource(org.esupportail.lecture.domain.model.CustomSource)
	 */
	public void deleteCustomSource(final CustomSource cs) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("deleteCustomSource PK=" + cs.getCustomSourcePK());			
		}
		getHibernateTemplate().delete(cs);
		if (USEFLUSH) {
			getHibernateTemplate().flush();
		} 
	}

	/**
	 * @param source 
	 * @see org.esupportail.lecture.dao.DaoService#updateCustomSource(org.esupportail.lecture.domain.model.CustomSource)
	 */
	public void updateCustomSource(final CustomSource source) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("updateCustomSource PK=" + source.getElementId());			
		}
		//Object merged = getHibernateTemplate().merge(source);
		getHibernateTemplate().saveOrUpdate(source);
		if (USEFLUSH) {
			getHibernateTemplate().flush();
		} 
	}

	/**
	 * @return all the VersionManager instances of the database.
	 */
	@SuppressWarnings("unchecked")
	public List<VersionManager> getVersionManagers() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("getVersionManagers()");			
		}
		DetachedCriteria criteria = DetachedCriteria.forClass(VersionManager.class);
		criteria.addOrder(Order.asc("id"));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/**
	 * @param versionManager 
	 * @see org.esupportail.lecture.dao.DaoService#addVersionManager(org.esupportail.lecture.domain.model.VersionManager)
	 */
	public void addVersionManager(final VersionManager versionManager) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("addVersionManager(" + versionManager + ")");			
		}
		getHibernateTemplate().save(versionManager);
	}

	/**
	 * @param versionManager 
	 * @see org.esupportail.lecture.dao.DaoService#updateVersionManager(org.esupportail.lecture.domain.model.VersionManager)
	 */
	public void updateVersionManager(final VersionManager versionManager) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("updateVersionManager(" + versionManager + ")");			
		}
		getHibernateTemplate().save(versionManager);
	}

	
}
