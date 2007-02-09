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
import org.esupportail.lecture.domain.model.TestENUM;
import org.esupportail.lecture.domain.model.UserProfile;
import org.esupportail.lecture.exceptions.dao.InfoDaoException;
import org.hibernate.LockMode;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * @author bourges
 */
public class DaoTestENUMImpl extends HibernateDaoSupport implements DaoTestENUM {
	/**
	 * Log instance 
	 */
	static final Log log = LogFactory.getLog(DaoTestENUMImpl.class);
	
	/**
	 * boolena flag in order to use flush during work
	 * should be false (true for test)
	 */
	private static final boolean useFlush = false;

	public TestENUM getTestENUM(String enumpk) {
		if (log.isDebugEnabled()) {
			log.debug("getTestENUM("+enumpk+")");			
		}
		TestENUM ret = null;
		if (enumpk != null) {
			String query = "select testENUM from TestENUM testENUM where testENUM.enumpk = ?";
		    List<TestENUM> list = getHibernateTemplate().find(query, enumpk);
		    if (list.size()>0) {
			    ret = list.get(0);				
			}
		}
		return ret;
	}

	public List<TestENUM> getTestENUMs() {
		if (log.isDebugEnabled()) {
			log.debug("getTestENUMs()");			
		}
		List<TestENUM> ret = null;
		String query = "select testENUM from TestENUM testENUM";
		ret = getHibernateTemplate().find(query);
		return ret;
	}

	public void saveTestENUM(TestENUM testENUM) {
		if (log.isDebugEnabled()) {
			log.debug("saveUserProfile PK="+testENUM.getTestENUMPK());			
		}
		getHibernateTemplate().saveOrUpdate(testENUM);
		if (useFlush) {
			getHibernateTemplate().flush();
		} 
	}
	public void deleteTestENUM(TestENUM testENUM) {
		if (log.isDebugEnabled()) {
			log.debug("deleteUserProfile PK="+testENUM.getTestENUMPK());			
		}
		getHibernateTemplate().delete(testENUM);
		if (useFlush) {
			getHibernateTemplate().flush();
		} 
	}

}
