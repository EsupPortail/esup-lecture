/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.test.drivers;

import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.dao.DaoTestENUM;
import org.esupportail.lecture.domain.model.ItemDisplayMode;
import org.esupportail.lecture.domain.model.TestENUM;
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
public class TestDAOTestENUM {
	
	protected static final Log log = LogFactory.getLog(TestDAOTestENUM.class);
	private static SessionFactory sessionFactory;
	private static Transaction transaction;
	private static XmlBeanFactory factory; 
	
	private static DaoTestENUM getDAO() {
		sessionFactory = (SessionFactory) factory.getBean("sessionFactory");
		Session session = SessionFactoryUtils.getSession(sessionFactory, true);
		transaction = session.beginTransaction();
		//session.setFlushMode(FlushMode.);
	    TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session));
	    DaoTestENUM dao = (DaoTestENUM)factory.getBean("daoTestENUM");
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
			Method method = TestDAOTestENUM.class.getDeclaredMethod(methodName, params);
			method.invoke(TestDAOTestENUM.class.newInstance(), paramsObj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	private static void populate() {
		DaoTestENUM dao = getDAO();
		TestENUM testENUM = new TestENUM();
		testENUM.setItemDisplayMode(ItemDisplayMode.ALL);
		dao.saveTestENUM(testENUM);
		testENUM = new TestENUM();
		testENUM.setItemDisplayMode(ItemDisplayMode.UNREAD);
		dao.saveTestENUM(testENUM);
		testENUM = new TestENUM();
		testENUM.setItemDisplayMode(ItemDisplayMode.UNREADFIRST);
		dao.saveTestENUM(testENUM);
		releaseDAO();
	}
	
	private static void navigate() {
		DaoTestENUM dao = getDAO();
		List<TestENUM> list = dao.getTestENUMs();
		for(TestENUM testENUM : list) {
			out(testENUM.getTestENUMPK()+" --> "+testENUM.getItemDisplayMode());
		}
		releaseDAO();
	}
	
	private static void delete() {
		DaoTestENUM dao = getDAO();
		List<TestENUM> list = dao.getTestENUMs();
		for(TestENUM testENUM : list) {
			dao.deleteTestENUM(testENUM);
		}
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
		for (int i = 0; i < args.length; i++) {
			execute(args[i]);
		}
	}
}
