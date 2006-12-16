/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.test.drivers;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.dao.DaoService;
import org.esupportail.lecture.domain.model.Channel;
import org.esupportail.lecture.domain.model.CustomCategory;
import org.esupportail.lecture.domain.model.CustomContext;
import org.esupportail.lecture.domain.model.CustomManagedCategory;
import org.esupportail.lecture.domain.model.UserProfile;
import org.esupportail.lecture.exceptions.CategoryNotLoadedException;
import org.esupportail.lecture.exceptions.CategoryProfileNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Stub to test classes in package org.esupportail.domain.model
 * @author gbouteil
 * @see org.esupportail.lecture.domain.model
 */
public class TestDAOLecture {
	
	protected static final Log log = LogFactory.getLog(TestDAOLecture.class);
	private static SessionFactory sessionFactory;
	private static Transaction transaction;
	private static XmlBeanFactory factory; 
	
	private static DaoService getDAO() {
		sessionFactory = (SessionFactory) factory.getBean("sessionFactory");
		Session session = SessionFactoryUtils.getSession(sessionFactory, true);
		transaction = session.beginTransaction();
		//session.setFlushMode(FlushMode.);
	    TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session));
		DaoService dao = (DaoService)factory.getBean("daoService2");
		return dao;
	}
	
	private static void releaseDAO() {
	    SessionHolder sessionHolder = (SessionHolder) TransactionSynchronizationManager.unbindResource(sessionFactory);
	    Session session = sessionHolder.getSession();
	    transaction.commit();
	    SessionFactoryUtils.closeSession(session);
	}
	
	private static void execute(String methodName) {
		Class params[] = {};
		Object paramsObj[] = {};
		out("************************************************");
		out("executing test: "+methodName);
		try {
			Method method = TestDAOLecture.class.getDeclaredMethod(methodName, params);
			method.invoke(TestDAOLecture.class.newInstance(), paramsObj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void navigate() throws CategoryProfileNotFoundException, CategoryNotLoadedException {
		out("actions : read, save, read userProfile and navigate throw customcontext");
		DaoService dao = getDAO();
		UserProfile userProfile = dao.getUserProfile("test");
		if (userProfile != null) {
			out("after read");			
			out("userProfile.getUserId --> " + userProfile.getUserId());			
			out("userProfile.getUserProfilePK --> " + userProfile.getUserProfilePK());			
			//delete userProfile test
			//dao.deleteUserProfile(userProfile);
		}
		else {
			out("userProfile is null (test not found)");
		}
		//set userProfile test
		dao.saveUserProfile(userProfile);
		out("after save");			
		out("userProfile.getUserId --> " + userProfile.getUserId());			
		out("userProfile.getUserProfilePK --> " + userProfile.getUserProfilePK());			
		UserProfile userProfile2 = dao.getUserProfile("test");
		if (userProfile2 != null) {
			out("after second read");			
			out("userProfile.getUserId --> " + userProfile2.getUserId());			
			out("userProfile.getUserProfilePK --> " + userProfile2.getUserProfilePK());			
			Map<String, CustomContext> ccs = userProfile2.getCustomContexts();
			Iterator<String> iter = ccs.keySet().iterator();
			while (iter.hasNext()) {
				String element = (String) iter.next();
				CustomContext cc = ccs.get(element);
				out("treesize of customContext "+element+" = "+cc.getTreeSize());
			}
			Map<String, CustomCategory> ccats = userProfile2.getCustomCategories();
			iter = ccats.keySet().iterator();
			while (iter.hasNext()) {
				String element = (String) iter.next();
				CustomCategory ccat = ccats.get(element);
				out("name of customCategory "+element+" = "+ccat.getName());
			}
		}
		releaseDAO();
	}
	
	private static void delete() {
		out("actions : delete test userprofile");
		DaoService dao = getDAO();
		UserProfile userProfile = dao.getUserProfile("test");
		if (userProfile != null) {
			out("after read");			
			out("userProfile.getUserId --> " + userProfile.getUserId());			
			out("userProfile.getUserProfilePK --> " + userProfile.getUserProfilePK());			
			//delete userProfile test
			dao.deleteUserProfile(userProfile);
		}
		else {
			out("userProfile is null (test not found)");
		}
		releaseDAO();
	}
	
	private static void populate() {
		out("actions : populate database from test userprofile");
		DaoService dao = getDAO();
		//create user profile
		UserProfile userProfile = new UserProfile("test");
		//create custom contexts
		CustomContext cc = new CustomContext("c1",userProfile);
		cc.setTreeSize(10);
		userProfile.addCustomContext(cc);
		cc = new CustomContext("c2",userProfile);
		cc.setTreeSize(20);
		userProfile.addCustomContext(cc);
		cc = new CustomContext("c3",userProfile);
		cc.setTreeSize(30);
		userProfile.addCustomContext(cc);
		//create custom categories
		CustomCategory ccat = new CustomManagedCategory("cp1", userProfile);
		userProfile.addCustomCategory(ccat);
		ccat = new CustomManagedCategory("cp2", userProfile);
		userProfile.addCustomCategory(ccat);
		ccat = new CustomManagedCategory("cp3", userProfile);
		userProfile.addCustomCategory(ccat);
		//save
		dao.saveUserProfile(userProfile);
		releaseDAO();
	}
	
	private static void out(String string) {
		System.out.println(string);
	}

	/**
	 * @param args test to execute
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 */
	public static void main(String[] args) {
		ClassPathResource res = new ClassPathResource("properties/applicationContext.xml");
		factory = new XmlBeanFactory(res);
		Channel channel = (Channel)factory.getBean("channel");
		for (int i = 0; i < args.length; i++) {
			execute(args[i]);
		}
	}
}
