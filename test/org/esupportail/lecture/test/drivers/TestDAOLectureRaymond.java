/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.test.drivers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.beans.ContextUserBean;
import org.esupportail.lecture.dao.DaoService;
import org.esupportail.lecture.dao.impl.DaoServiceHibernate;
import org.esupportail.lecture.domain.model.Channel;
import org.esupportail.lecture.domain.model.Context;
import org.esupportail.lecture.domain.model.CustomContext;
import org.esupportail.lecture.domain.model.LectureTools;
import org.esupportail.lecture.domain.model.UserProfile;
import org.esupportail.lecture.domain.service.DomainService;

import java.io.IOException;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

/**
 * Stub to test classes in package org.esupportail.domain.model
 * @author gbouteil
 * @see org.esupportail.lecture.domain.model
 */
public class TestDAOLectureRaymond {
	
	protected static final Log log = LogFactory.getLog(Channel.class); 
	
	/**
	 * @param args non argumet needed
	 */
	public static void main(String[] args) {
	
		ClassPathResource res = new ClassPathResource("applicationContextRaymond.xml");
		XmlBeanFactory factory = new XmlBeanFactory(res);
		
		// get one UserProfile
		DaoService dao = (DaoService)factory.getBean("daoServiceImpl");
		UserProfile userProfile = dao.getUserProfile("bourges");
		if (userProfile != null) {
			System.out.println("userProfile.getUserId --> " + userProfile.getUserId());			
		}
		else {
			System.out.println("userProfile est null (bourges bot found)");
		}
		// set and get an other UserProfile
		userProfile = new UserProfile();
		userProfile.setUserId("test");
		//dao.addUserProfile(userProfile);
		userProfile = dao.getUserProfile("test");
		System.out.println("userProfile.getUserId --> " + userProfile.getUserId());
		CustomContext cc = userProfile.getCustomContext("c1");
		System.out.println("cc.getContextId --> " + cc.getContextId());		
		//TODO See how to use Spring for setDaoService
		LectureTools.setDaoService(dao);
//		DomainService ds = (DomainService)factory.getBean("domainService");
//		ds.loadChannel();
//		ContextUserBean cub = ds.getContextUserBean("test", "c1");
		//userProfile.
		//dao.
		
	}
}
