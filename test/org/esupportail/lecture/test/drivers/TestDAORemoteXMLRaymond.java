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
import org.esupportail.lecture.domain.model.ManagedCategoryProfile;
import org.esupportail.lecture.domain.model.ManagedSourceProfile;
import org.esupportail.lecture.domain.model.SourceProfile;
import org.esupportail.lecture.domain.model.UserProfile;
import org.esupportail.lecture.domain.service.DomainService;
import org.esupportail.lecture.utils.exception.ErrorException;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Set;

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
			for (int i = 0; i < 10; i++) {
				DaoService dao = (DaoService)factory.getBean("daoServiceImpl");
				ManagedCategoryProfile test = new ManagedCategoryProfile();
				test.setUrlCategory("http://perso.univ-rennes1.fr/raymond.bourges/categoryTest.xml");
				test.init();
				test.setTtl(100);
				test.setId("test");
				ManagedCategory cat = dao.getManagedCategory(test);
				System.out.println("name --> "+cat.getName());
				Set<ManagedSourceProfile> set = cat.getManagedSourceProfilesSet();
				Iterator<ManagedSourceProfile> iter = set.iterator();
				while (iter.hasNext()) {
					ManagedSourceProfile msp = (ManagedSourceProfile) iter.next();
					System.out.println("sp name --> "+msp.getName());
					//System.out.println("sp access --> "+msp.getAccess());
				}
			}
		} catch (ErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
