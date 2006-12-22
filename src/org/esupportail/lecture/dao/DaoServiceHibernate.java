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
import org.esupportail.lecture.exceptions.ErrorException;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * @author bourges
 */
public class DaoServiceHibernate extends HibernateDaoSupport {
	/**
	 * Log instance 
	 */
	private static final Log log = LogFactory.getLog(DaoServiceHibernate.class);
	private static final boolean useFlush = false;

	/**
	 * @see org.esupportail.lecture.dao.DaoService#getUserProfile(java.lang.String)
	 */
	public UserProfile getUserProfile(String userId) {
		if (log.isDebugEnabled()) {
			log.debug("getUserProfile("+userId+")");			
		}
		UserProfile ret = null;
		if (userId != null) {
			String query = "select userProfile from UserProfile userProfile where userProfile.userId = ?";
		    List<UserProfile> list = getHibernateTemplate().find(query, userId);
		    if (list.size()>0) {
			    ret = list.get(0);				
			}
		}
		else {
			String msg = "userId is null: can't find it in database";
			log.error(msg);
			throw new ErrorException(msg);
		}
		return ret;
	}

	/**
	 * @see org.esupportail.lecture.dao.DaoService#saveUserProfile(org.esupportail.lecture.domain.model.UserProfile)
	 */
	public void saveUserProfile(UserProfile userProfile) {
		if (log.isDebugEnabled()) {
			log.debug("saveUserProfile PK="+userProfile.getUserProfilePK());			
		}
		getHibernateTemplate().saveOrUpdate(userProfile);
	}

	/**
	 * @see org.esupportail.lecture.dao.DaoService#deleteUserProfile(org.esupportail.lecture.domain.model.UserProfile)
	 */
	public void deleteUserProfile(UserProfile userProfile) {
		if (log.isDebugEnabled()) {
			log.debug("deleteUserProfile PK="+userProfile.getUserProfilePK());			
		}
		getHibernateTemplate().delete(userProfile);
		if (useFlush) {
			getHibernateTemplate().flush();
		} 
	}

	/**
	 * @see org.esupportail.lecture.dao.DaoService#updateUserProfile(org.esupportail.lecture.domain.model.UserProfile)
	 */
	public void updateUserProfile(UserProfile userProfile) {
		if (log.isDebugEnabled()) {
			log.debug("updateUserProfile PK="+userProfile.getUserProfilePK());			
		}
		getHibernateTemplate().saveOrUpdate(userProfile);
	}

	/**
	 * @see org.esupportail.lecture.dao.DaoService#updateCustomContext(org.esupportail.lecture.domain.model.CustomContext)
	 */
	public void updateCustomContext(CustomContext customContext) {
		if (log.isDebugEnabled()) {
			log.debug("updateCustomContext PK="+customContext.getCustomContextPK());			
		}
		getHibernateTemplate().saveOrUpdate(customContext);
	}

	/**
	 * @see org.esupportail.lecture.dao.DaoService#deleteCustomContext(org.esupportail.lecture.domain.model.CustomContext)
	 */
	public void deleteCustomContext(CustomContext cco) {
		if (log.isDebugEnabled()) {
			log.debug("deleteCustomContext PK="+cco.getCustomContextPK());			
		}
		getHibernateTemplate().delete(cco);
		if (useFlush) {
			getHibernateTemplate().flush();
		} 
	}

	/**
	 * @see org.esupportail.lecture.dao.DaoService#deleteCustomCategory(org.esupportail.lecture.domain.model.CustomCategory)
	 */
	public void deleteCustomCategory(CustomCategory cca) {
		if (log.isDebugEnabled()) {
			log.debug("deleteCustomCategory PK="+cca.getCustomCategoryPK());			
		}
		getHibernateTemplate().delete(cca);
		if (useFlush) {
			getHibernateTemplate().flush();
		} 
	}

	/**
	 * @see org.esupportail.lecture.dao.DaoService#updateCustomCategory(org.esupportail.lecture.domain.model.CustomCategory)
	 */
	public void updateCustomCategory(CustomCategory cca) {
		if (log.isDebugEnabled()) {
			log.debug("updateCustomCategory PK="+cca.getCustomCategoryPK());			
		}
		getHibernateTemplate().saveOrUpdate(cca);
	}

	/**
	 * @see org.esupportail.lecture.dao.DaoService#deleteCustomSource(org.esupportail.lecture.domain.model.CustomSource)
	 */
	public void deleteCustomSource(CustomSource cs) {
		if (log.isDebugEnabled()) {
			log.debug("deleteCustomSource PK=?????");			
		}
		getHibernateTemplate().delete(cs);
		if (useFlush) {
			getHibernateTemplate().flush();
		} 
	}

	/**
	 * @see org.esupportail.lecture.dao.DaoService#updateCustomSource(org.esupportail.lecture.domain.model.CustomSource)
	 */
	public void updateCustomSource(CustomSource source) {
		if (log.isDebugEnabled()) {
			log.debug("updateCustomSource PK=?????");			
		}
		getHibernateTemplate().saveOrUpdate(source);
	}
}
