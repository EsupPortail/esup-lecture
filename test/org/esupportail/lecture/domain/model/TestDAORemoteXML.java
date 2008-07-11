/**
 * ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
 * For any information please refer to http://esup-helpdesk.sourceforge.net
 * You may obtain a copy of the licence at http://www.esup-portail.org/license/
 */
package org.esupportail.lecture.domain.model;

import java.util.Enumeration;
import java.util.Hashtable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.dao.DaoService;
import org.esupportail.lecture.exceptions.dao.InfoDaoException;
import org.esupportail.lecture.exceptions.dao.TimeoutException;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

/**
 * Stub to test classes in package org.esupportail.domain.model
 * @author gbouteil
 * @see org.esupportail.lecture.domain.model
 */
public class TestDAORemoteXML {

	protected static final Log log = LogFactory.getLog(TestDAORemoteXML.class); 

	/**
	 * @param args non argumet needed
	 * @throws TimeoutException 
	 */
	public static void main(String[] args) throws InfoDaoException {

		ClassPathResource res = new ClassPathResource("properties/applicationContext.xml");
		XmlBeanFactory factory = new XmlBeanFactory(res);

		DaoService dao = (DaoService)factory.getBean("daoService");
		ManagedCategoryProfile test = new ManagedCategoryProfile();
		String url = "http://localhost:8080/data/demo2.xml";
		test.setUrlCategory(url);
		//test.init();
		test.setTtl(100);
		test.setId("test");
		ManagedCategory cat = dao.getManagedCategory(test);
		System.out.println("name --> "+cat.getName());
		Hashtable<String,SourceProfile> hash = cat.getSourceProfilesHash();
		Enumeration<String> keys = hash.keys();
		while (keys.hasMoreElements()) {
			String element = (String) keys.nextElement();
			ManagedSourceProfile msp = (ManagedSourceProfile)hash.get(element);
			System.out.println("sp name --> "+msp.getName());
			//System.out.println("sp access --> "+msp.getAccess().name());
		}
	}
}
