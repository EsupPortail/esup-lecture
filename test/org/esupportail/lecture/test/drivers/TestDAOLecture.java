/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.test.drivers;

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.dao.DaoService;
import org.esupportail.lecture.domain.model.CustomContext;
import org.esupportail.lecture.domain.model.UserProfile;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

/**
 * Stub to test classes in package org.esupportail.domain.model
 * @author gbouteil
 * @see org.esupportail.lecture.domain.model
 */
public class TestDAOLecture {
	
	protected static final Log log = LogFactory.getLog(TestDAOLecture.class); 
	
	/**
	 * @param args non argumet needed
	 */
	public static void main(String[] args) {
	
		ClassPathResource res = new ClassPathResource("properties/applicationContext.xml");
		XmlBeanFactory factory = new XmlBeanFactory(res);
		
		// get one UserProfile
		TestProxyDao testProxyDao = (TestProxyDao)factory.getBean("testProxyDao");
		DaoService dao = (DaoService)testProxyDao.getDaoService();
//		DaoService dao = (DaoService)factory.getBean("daoService2");
		UserProfile userProfile = dao.getUserProfile("test");
		if (userProfile != null) {
			System.out.println("after read");			
			System.out.println("userProfile.getUserId --> " + userProfile.getUserId());			
			System.out.println("userProfile.getUserProfilePK --> " + userProfile.getUserProfilePK());			
			//delete userProfile test
			//dao.deleteUserProfile(userProfile);
		}
		else {
			System.out.println("userProfile is null (test not found)");
		}
		//set userProfile test
		userProfile = new UserProfile("test");
		dao.saveUserProfile(userProfile);
		System.out.println("after save");			
		System.out.println("userProfile.getUserId --> " + userProfile.getUserId());			
		System.out.println("userProfile.getUserProfilePK --> " + userProfile.getUserProfilePK());			
		UserProfile userProfile2 = dao.getUserProfile("test");
		if (userProfile2 != null) {
			System.out.println("after second read");			
			System.out.println("userProfile.getUserId --> " + userProfile2.getUserId());			
			System.out.println("userProfile.getUserProfilePK --> " + userProfile2.getUserProfilePK());			
//			Map<String, CustomContext> ccs = userProfile2.getCustomContexts();
//			Iterator<String> iter = ccs.keySet().iterator();
//			while (iter.hasNext()) {
//				String element = (String) iter.next();
//				CustomContext cc = ccs.get(element);
//				System.out.println("treesize of customContext "+element+" = "+cc.getTreeSize());
//			}
			//delete userProfile test
//			dao.deleteUserProfile(userProfile2);
		}
		//add CustomContext to userProfile test
//		CustomContext cc = new CustomContext();
//		cc.setContextId("c1");
//		cc.setUserProfile(userProfile);
//		userProfile.getCustomContexts().put("c1", cc);
//		dao.addCustomContext(cc);
		
	}
}
