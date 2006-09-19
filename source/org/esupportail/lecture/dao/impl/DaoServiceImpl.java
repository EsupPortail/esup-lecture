package org.esupportail.lecture.dao.impl;

import org.esupportail.lecture.dao.DaoService;
import org.esupportail.lecture.domain.model.CustomContext;
import org.esupportail.lecture.domain.model.ManagedCategory;
import org.esupportail.lecture.domain.model.UserProfile;
import org.esupportail.lecture.utils.exception.ErrorException;
import org.esupportail.lecture.utils.exception.WarningException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class DaoServiceImpl implements DaoService {
	DaoServiceHibernate hibernateService;
	DaoServiceRemoteXML remoteXMLService;
	
	
	
	/**
	 * @see org.esupportail.lecture.dao.DaoService#getUserProfile(java.lang.String)
	 */
	public UserProfile getUserProfile(String userId) {
	    return hibernateService.getUserProfile(userId);
	}

	/**
	 * @see org.esupportail.lecture.dao.DaoService#addUserProfile(org.esupportail.lecture.domain.model.UserProfile)
	 */
	public void addUserProfile(UserProfile userProfile) {
		hibernateService.addUserProfile(userProfile);
	}	
	
	public CustomContext getCustomContext(int Id) {
		return hibernateService.getCustomContext(Id);
	}

	/**
	 * @throws WarningException 
	 * @see org.esupportail.lecture.dao.DaoService#getCategory(java.lang.String, int, java.lang.String)
	 */
	public ManagedCategory getCategory(String urlCategory, int ttl, String profileId) {
		return remoteXMLService.getCategory(urlCategory, ttl, profileId);
	}

	public void setHibernateService(DaoServiceHibernate hibernateService) {
		this.hibernateService = hibernateService;
	}

	public void setRemoteXMLService(DaoServiceRemoteXML remoteXML) {
		this.remoteXMLService = remoteXML;
	}

}
