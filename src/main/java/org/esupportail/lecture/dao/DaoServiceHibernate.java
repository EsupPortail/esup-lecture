/**
 * Hibernate Doa Service implementation 
 */
package org.esupportail.lecture.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.model.CustomCategory;
import org.esupportail.lecture.domain.model.CustomContext;
import org.esupportail.lecture.domain.model.CustomSource;
import org.esupportail.lecture.domain.model.UserProfile;
import org.esupportail.lecture.domain.model.VersionManager;

/**
 * @author bourges
 */
public class DaoServiceHibernate {

	/**
	 * JPA entity manager
	 */
	EntityManager entityManager;

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

	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.entityManager = em;
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
			Query q = entityManager.createQuery("select userProfile from UserProfile userProfile where userProfile.userId = '" + userId + "'");
			q.setLockMode(LockModeType.PESSIMISTIC_READ);
			List<UserProfile> list = (List<UserProfile>)q.getResultList();
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
	 * @see org.esupportail.lecture.dao.DaoService#refreshUserProfile(
	 * 	org.esupportail.lecture.domain.model.UserProfile)
	 */
	public UserProfile refreshUserProfile(final UserProfile userProfile) {
		//TODO (RB) rename attachCleanUserProfile
		if (LOG.isDebugEnabled()) {
			LOG.debug("refreshUserProfile(" 
					+ userProfile.getUserId() + ")");			
		}
		UserProfile ret = userProfile;
		entityManager.merge(userProfile);
		return ret;
	}

	/**
	 * @param userProfile 
	 * @see org.esupportail.lecture.dao.DaoService#saveUserProfile(org.esupportail.lecture.domain.model.UserProfile)
	 */
	public void saveUserProfile(final UserProfile userProfile) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("saveUserProfile(" + userProfile.getUserId() + ")");			
		}
		entityManager.merge(userProfile);
		if (USEFLUSH) {
			entityManager.flush();
		} 
	}

	/**
	 * @param userProfile 
	 * @return userProfile
	 * @see org.esupportail.lecture.dao.DaoService#mergeUserProfile(UserProfile)
	 */
	public UserProfile mergeUserProfile(final UserProfile userProfile) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("mergeUserProfile(" + userProfile.getUserId() + ")");			
		}
		UserProfile merged = (UserProfile) entityManager.merge(userProfile);
		if (USEFLUSH) {
			entityManager.flush();
		} 
		return merged;
	}

	/**
	 * @param userProfile 
	 * @see org.esupportail.lecture.dao.DaoService#deleteUserProfile(
	 *  org.esupportail.lecture.domain.model.UserProfile)
	 */
	public void deleteUserProfile(final UserProfile userProfile) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("deleteUserProfile(" + userProfile.getUserId() + ")");			
		}
		entityManager.refresh(userProfile);
		if (USEFLUSH) {
			entityManager.flush();
		} 
	}

	/**
	 * @param userProfile 
	 * @see org.esupportail.lecture.dao.DaoService#updateUserProfile(
	 *  org.esupportail.lecture.domain.model.UserProfile)
	 */
	public void updateUserProfile(final UserProfile userProfile) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("updateUserProfile(" + userProfile.getUserId() + ")");			
		}
		//Object merged = getHibernateTemplate().merge(userProfile);
		entityManager.merge(userProfile);
		if (USEFLUSH) {
			entityManager.flush();
		} 
	}

	/**
	 * @param customContext 
	 * @see org.esupportail.lecture.dao.DaoService#updateCustomContext(
	 *  org.esupportail.lecture.domain.model.CustomContext)
	 */
	public void updateCustomContext(final CustomContext customContext) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("updateCustomContext PK=" + customContext.getCustomContextPK());			
		}
		//Object merged = getHibernateTemplate().merge(customContext);
		entityManager.merge(customContext);
	}
	/**
	 * @param cco 
	 * @see org.esupportail.lecture.dao.DaoService#deleteCustomContext(
	 *  org.esupportail.lecture.domain.model.CustomContext)
	 */
	public void deleteCustomContext(final CustomContext cco) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("deleteCustomContext PK=" + cco.getCustomContextPK());			
		}
		entityManager.remove(cco);
		if (USEFLUSH) {
			entityManager.flush();
		} 
	}

	/**
	 * @param cca 
	 * @see org.esupportail.lecture.dao.DaoService#deleteCustomCategory(
	 *  org.esupportail.lecture.domain.model.CustomCategory)
	 */
	public void deleteCustomCategory(final CustomCategory cca) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("deleteCustomCategory PK=" + cca.getCustomCategoryPK());			
		}
		entityManager.remove(cca);
		if (USEFLUSH) {
			entityManager.flush();
		} 
	}

	/**
	 * @param cca 
	 * @see org.esupportail.lecture.dao.DaoService#updateCustomCategory(
	 *  org.esupportail.lecture.domain.model.CustomCategory)
	 */
	public void updateCustomCategory(final CustomCategory cca) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("updateCustomCategory PK=" + cca.getCustomCategoryPK());			
		}
		//Object merged = getHibernateTemplate().merge(cca);
		entityManager.merge(cca);
		if (USEFLUSH) {
			entityManager.flush();
		} 
	}

	/**
	 * @param cs 
	 * @see org.esupportail.lecture.dao.DaoService#deleteCustomSource(
	 *  org.esupportail.lecture.domain.model.CustomSource)
	 */
	public void deleteCustomSource(final CustomSource cs) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("deleteCustomSource PK=" + cs.getCustomSourcePK());			
		}
		entityManager.remove(cs);
		if (USEFLUSH) {
			entityManager.flush();
		} 
	}

	/**
	 * @param source 
	 * @see org.esupportail.lecture.dao.DaoService#updateCustomSource(
	 *  org.esupportail.lecture.domain.model.CustomSource)
	 */
	public void updateCustomSource(final CustomSource source) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("updateCustomSource PK=" + source.getElementId());			
		}
		//Object merged = getHibernateTemplate().merge(source);
		entityManager.merge(source);
		if (USEFLUSH) {
			entityManager.flush();
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
		Query q = entityManager.createQuery("select versionManager from VersionManager order by versionManager.id");
		List<VersionManager> list = (List<VersionManager>)q.getResultList();
		return list;
	}

	/**
	 * @param versionManager 
	 * @see org.esupportail.lecture.dao.DaoService#addVersionManager(
	 *  org.esupportail.lecture.domain.model.VersionManager)
	 */
	public void addVersionManager(final VersionManager versionManager) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("addVersionManager(" + versionManager + ")");			
		}
		entityManager.merge(versionManager);
	}

	/**
	 * @param versionManager 
	 * @see org.esupportail.lecture.dao.DaoService#updateVersionManager(
	 *  org.esupportail.lecture.domain.model.VersionManager)
	 */
	public void updateVersionManager(final VersionManager versionManager) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("updateVersionManager(" + versionManager + ")");			
		}
		entityManager.merge(versionManager);
	}

	/**
	 * @param query
	 */
	public void updateSQL(final String query) {
		// TODO Auto-generated method stub
		//getSqlQuery(query).executeUpdate();

	}

	public Query getQuery(String hqlQuery) {
		// TODO Auto-generated method stub
		return null;
	}


}
