package org.esupportail.lecture.dao.impl;

import org.esupportail.lecture.dao.DaoService;
import org.esupportail.lecture.domain.model.CustomContext;
import org.esupportail.lecture.domain.model.UserProfile;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
//TODO : not used for the moment !
public class DaoServiceHibernate extends HibernateDaoSupport implements DaoService {
	
	public UserProfile getUserProfile(String userId) {
	    return (UserProfile)getHibernateTemplate().get(UserProfile.class, userId);
	}

	public void addUserProfile(UserProfile userProfile) {
		getHibernateTemplate().persist(userProfile);
		// TODO Auto-generated method stub
	}	
	
	public CustomContext getCustomContext(int Id) {
		return (CustomContext)getHibernateTemplate().get(CustomContext.class, Id);
	}


}
