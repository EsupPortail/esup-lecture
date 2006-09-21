package org.esupportail.lecture.dao.impl;

import org.esupportail.lecture.dao.DaoService;
import org.esupportail.lecture.domain.model.Category;
import org.esupportail.lecture.domain.model.CustomContext;
import org.esupportail.lecture.domain.model.ManagedCategory;
import org.esupportail.lecture.domain.model.ManagedCategoryProfile;
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

	
	

	public ManagedCategory getManagedCategory(ManagedCategoryProfile profile) {
		// TODO Auto-generated method stub
		return null;
	}

	public Category getManagedCategory(ManagedCategoryProfile profile, String ptCas) {
		// TODO Auto-generated method stub
		return null;
	}

	//TODO : TMP !!
	public Source getSource(String urlSource, int ttl, String profileId, boolean specificUserContent) {
		return this.remoteXMLService.getSource(urlSource, ttl, profileId, specificUserContent);
	}

}
