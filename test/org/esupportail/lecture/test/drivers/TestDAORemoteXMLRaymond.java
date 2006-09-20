/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.test.drivers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.dao.DaoService;
import org.esupportail.lecture.dao.impl.DaoServiceHibernate;
import org.esupportail.lecture.domain.model.Channel;
import org.esupportail.lecture.domain.model.CustomContext;
import org.esupportail.lecture.domain.model.ManagedCategory;
import org.esupportail.lecture.domain.model.ManagedSourceProfile;
import org.esupportail.lecture.domain.model.UserProfile;
import org.esupportail.lecture.domain.service.DomainService;
import org.esupportail.lecture.utils.exception.ErrorException;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;

import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

/**
 * Stub to test classes in package org.esupportail.domain.model
 * @author gbouteil
 * @see org.esupportail.lecture.domain.model
 */
public class TestDAORemoteXMLRaymond {
	
	protected static final Log log = LogFactory.getLog(Channel.class); 
	
	/**
	 * @param args non argumet needed
	 */
	public static void main(String[] args) {
	
		ClassPathResource res = new ClassPathResource("applicationContextRaymond.xml");
		XmlBeanFactory factory = new XmlBeanFactory(res);
		
		try {
			DaoService dao = (DaoService)factory.getBean("daoServiceImpl");
			//ManagedCategory cat = dao.getCategory("http://perso.univ-rennes1.fr/raymond.bourges/categoryTest.xml", 100, "test");
			//System.out.println("name --> "+cat.getName());
			//Enumeration<String> keys = cat.getSourceProfiles().keys();
//			while (keys.hasMoreElements()) {
//				String key = (String) keys.nextElement();
//				ManagedSourceProfile sp = (ManagedSourceProfile)cat.getSourceProfiles().get(key);
//				System.out.println("sp name --> "+sp.getName());
//				System.out.println("sp access --> "+sp.getAccess());
//				
//			}
		} catch (ErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
