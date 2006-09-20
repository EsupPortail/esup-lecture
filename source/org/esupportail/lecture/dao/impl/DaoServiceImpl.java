package org.esupportail.lecture.dao.impl;

import org.esupportail.lecture.dao.DaoService;
import org.esupportail.lecture.domain.model.CustomContext;
import org.esupportail.lecture.domain.model.ManagedCategory;
import org.esupportail.lecture.domain.model.Source;
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
	public ManagedCategory getManagedCategory(String urlCategory, int ttl, String profileId) {
		return remoteXMLService.getManagedCategory(urlCategory, ttl, profileId);
	}

	public void setHibernateService(DaoServiceHibernate hibernateService) {
		this.hibernateService = hibernateService;
	}

	public void setRemoteXMLService(DaoServiceRemoteXML remoteXML) {
		this.remoteXMLService = remoteXML;
	}

	/**
	 * @see org.esupportail.lecture.dao.DaoService#addCustomContext(org.esupportail.lecture.domain.model.CustomContext)
	 */
	public void addCustomContext(CustomContext customContext) {
		this.hibernateService.addCustomContext(customContext);
	}

	/**
	 * @see org.esupportail.lecture.dao.DaoService#deleteUserProfile(java.lang.String)
	 */
	public void deleteUserProfile(UserProfile userProfile) {
		this.hibernateService.deleteUserProfile(userProfile);
	}

	/**
	 * @see org.esupportail.lecture.dao.DaoService#getManagedCategory(java.lang.String, int, java.lang.String, java.lang.String)
	 */
	public ManagedCategory getManagedCategory(String urlCategory, int ttl, String profileId, String ptCAS) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.esupportail.lecture.dao.DaoService#getSource(java.lang.String, int, java.lang.String, boolean)
	 */
	public Source getSource(String urlSource, int ttl, String profileId, boolean specificUserContent) {
		return remoteXMLService.getSource(urlSource, ttl, profileId, specificUserContent);
	}

}
