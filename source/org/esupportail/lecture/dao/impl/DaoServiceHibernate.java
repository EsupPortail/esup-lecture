package org.esupportail.lecture.dao.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.model.CustomContext;
import org.esupportail.lecture.domain.model.UserProfile;
import org.esupportail.lecture.utils.exception.ErrorException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class DaoServiceHibernate extends HibernateDaoSupport {
	/**
	 * Log instance 
	 */
	private static final Log log = LogFactory.getLog(DaoServiceRemoteXML.class);
	
	/**
	 * @see org.esupportail.lecture.dao.DaoService#getUserProfile(java.lang.String)
	 */
	public UserProfile getUserProfile(String userId) {
		UserProfile ret = null;
		if (userId != null) {
		    ret = (UserProfile)getHibernateTemplate().get(UserProfile.class, userId);			
		}
		else {
			log.error("userId is null : userId attribute in your config file is defined as user-attribute in your portlet.xml file and exist in your portal ?");
			throw new ErrorException();
		}
		return ret;
	}

	/**
	 * @see org.esupportail.lecture.dao.DaoService#addUserProfile(org.esupportail.lecture.domain.model.UserProfile)
	 */
	public void addUserProfile(UserProfile userProfile) {
		getHibernateTemplate().persist(userProfile);
	}	
	
	public CustomContext getCustomContext(int Id) {
		return (CustomContext)getHibernateTemplate().get(CustomContext.class, Id);
	}

	public void addCustomContext(CustomContext customContext) {
		//TODO : !!!! customContext is null !!!!
		//getHibernateTemplate().persist(customContext);
	}


	public void deleteUserProfile(UserProfile userProfile) {
		getHibernateTemplate().delete(userProfile);
	}

	public void updateCustomContext(CustomContext customContext) {
		getHibernateTemplate().update(customContext);
	}


}
