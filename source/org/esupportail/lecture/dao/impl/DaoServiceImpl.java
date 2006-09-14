package org.esupportail.lecture.dao.impl;

import org.esupportail.lecture.dao.DaoService;
import org.esupportail.lecture.domain.model.CustomContext;
import org.esupportail.lecture.domain.model.UserProfile;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class DaoServiceImpl implements DaoService {
	DaoServiceHibernate hibernateService;
	DaoServiceRemoteXML remoteXMLService;
	
	
	
	public UserProfile getUserProfile(String userId) {
	    return hibernateService.getUserProfile(userId);
	}

	public void addUserProfile(UserProfile userProfile) {
		hibernateService.addUserProfile(userProfile);
	}	
	
	public CustomContext getCustomContext(int Id) {
		return hibernateService.getCustomContext(Id);
	}

	public void setHibernateService(DaoServiceHibernate hb) {
		this.hibernateService = hb;
	}

	public void setRemoteXMLService(DaoServiceRemoteXML remoteXML) {
		this.remoteXMLService = remoteXML;
	}


}
