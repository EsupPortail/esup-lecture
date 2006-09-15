package org.esupportail.lecture.dao.impl;

import org.esupportail.lecture.domain.model.CustomContext;
import org.esupportail.lecture.domain.model.UserProfile;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class DaoServiceHibernate extends HibernateDaoSupport {
	
	/**
	 * @see org.esupportail.lecture.dao.DaoService#getUserProfile(java.lang.String)
	 */
	public UserProfile getUserProfile(String userId) {
	    return (UserProfile)getHibernateTemplate().get(UserProfile.class, userId);
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


}
