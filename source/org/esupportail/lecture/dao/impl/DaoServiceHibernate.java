package org.esupportail.lecture.dao.impl;

import org.esupportail.lecture.domain.model.CustomContext;
import org.esupportail.lecture.domain.model.UserProfile;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class DaoServiceHibernate extends HibernateDaoSupport {
	
	public UserProfile getUserProfile(String userId) {
	    return (UserProfile)getHibernateTemplate().get(UserProfile.class, userId);
	}

	public void addUserProfile(UserProfile userProfile) {
		getHibernateTemplate().persist(userProfile);
	}	
	
	public CustomContext getCustomContext(int Id) {
		return (CustomContext)getHibernateTemplate().get(CustomContext.class, Id);
	}


}
