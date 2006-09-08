package org.esupportail.lecture.dao.hibernate;

import org.esupportail.lecture.dao.DaoService;
import org.esupportail.lecture.domain.model.UserProfile;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
//TODO : not used for the moment !
public class DaoServiceHibernate extends HibernateDaoSupport implements DaoService {
	
	public UserProfile getUserProfile(String userId) {
	    return (UserProfile)getHibernateTemplate().load(UserProfile.class, userId);
	}

	public void addUserProfile(UserProfile userProfile) {
		getHibernateTemplate().persist(userProfile);
		// TODO Auto-generated method stub
	}	


}