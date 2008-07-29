/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.test.drivers;

import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.dao.DaoService;
import org.esupportail.lecture.domain.model.Channel;
import org.esupportail.lecture.domain.model.UserProfile;
import org.esupportail.lecture.exceptions.domain.CategoryProfileNotFoundException;
import org.esupportail.lecture.exceptions.domain.TreeSizeErrorException;
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
		out("executing test: " + methodName);
		try {
			Method method = TestDAOLecture.class.getDeclaredMethod(methodName, params);
			method.invoke(TestDAOLecture.class.newInstance(), paramsObj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	private static void navigate() throws CategoryProfileNotFoundException, CategoryNotLoadedException {
//		out("actions : read, save, read userProfile and navigate throw customcontext");
//		DaoService dao = getDAO();
//		UserProfile userProfile = dao.getUserProfile("test");
//		if (userProfile != null) {
//			out("after read");			
//			out("userProfile.getUserId --> " + userProfile.getUserId());			
//			out("userProfile.getUserProfilePK --> " + userProfile.getUserProfilePK());			
//			//delete userProfile test
//			//dao.deleteUserProfile(userProfile);
//		}
//		else {
//			out("userProfile is null (test not found)");
//		}
//		//set userProfile test
//		dao.saveUserProfile(userProfile);
//		out("after save");			
//		out("userProfile.getUserId --> " + userProfile.getUserId());			
//		out("userProfile.getUserProfilePK --> " + userProfile.getUserProfilePK());			
//		UserProfile userProfile2 = dao.getUserProfile("test");
//		if (userProfile2 != null) {
//			out("after second read");			
//			out("userProfile.getUserId --> " + userProfile2.getUserId());			
//			out("userProfile.getUserProfilePK --> " + userProfile2.getUserProfilePK());			
//			Map<String, CustomContext> ccs = userProfile2.getCustomContexts();
//			Iterator<String> iter = ccs.keySet().iterator();
//			while (iter.hasNext()) {
////				String element = (String) iter.next();
////				CustomContext cc = ccs.get(element);
////				out("treesize of customContext "+element+" = "+cc.getTreeSize());
////				Iterator<String> iter2 = cc.getSubscriptions().keySet().iterator();
////				while (iter2.hasNext()) {
////					String element2 = (String) iter2.next();
////					out("getSubscriptions key : "+element2);
////				}
//			}
//			Map<String, CustomCategory> ccats = userProfile2.getCustomCategories();
//			iter = ccats.keySet().iterator();
//			while (iter.hasNext()) {
//				String element = (String) iter.next();
//				CustomCategory ccat = ccats.get(element);
//				out("name of customCategory "+element+" = "+ccat.getName());
//			}
//		}
//		releaseDAO();
//	}
	
//	private static void delete() throws CategoryProfileNotFoundException {
//		out("actions : delete test userprofile");
//		DaoService dao = getDAO();
//		UserProfile userProfile = dao.getUserProfile("test");
//		if (userProfile != null) {
//			out("after read");			
//			out("userProfile.getUserId --> " + userProfile.getUserId());			
//			out("userProfile.getUserProfilePK --> " + userProfile.getUserProfilePK());	
//			//remove CustomContexts from userprofile
//			ArrayList<CustomContext> collec2 = new ArrayList<CustomContext>(userProfile.getCustomContexts().values());
//			for(CustomContext cc : collec2) {
////				userProfile.removeCustomContext(cc.getContextId());
////				dao.deleteCustomContext(cc);
//			}
//			//remove CustomCategories from userprofile
//			ArrayList<CustomCategory> collec = new ArrayList<CustomCategory>(userProfile.getCustomCategories().values());
//			for(CustomCategory cc : collec) {
////				userProfile.removeCustomCategory(cc.getProfileId());
////				dao.deleteCustomCategory(cc);
//			}
//			
//			//remove customSources from userprofile
//			ArrayList<CustomSource> collec3 = new ArrayList<CustomSource>(userProfile.getCustomSources().values());
//			for(CustomSource cc : collec3) {
////				userProfile.removeCustomSource(cc.getProfileId());
////				dao.deleteCustomSource(cc);
//			}
//			
//			//remove userprofile
//			dao.deleteUserProfile(userProfile);
//		}
//		else {
//			out("userProfile is null (test not found)");
//		}
//		releaseDAO();
//	}
	
	private static void populate() throws CategoryProfileNotFoundException, TreeSizeErrorException {
		out("actions : populate database from test userprofile");
		DaoService dao = getDAO();
		//create user profile
		UserProfile userProfile = new UserProfile("test");
		
		//create custom contexts
//		CustomContext cc1 = new CustomContext("c1",userProfile);
//		cc1.setTreeSize(10);
//		cc1.setUserProfile(userProfile);
//		userProfile.addCustomContext(cc1);
//		CustomContext cc2 = new CustomContext("c2",userProfile);
//		cc2.setTreeSize(20);
//		cc2.setUserProfile(userProfile);
//		userProfile.addCustomContext(cc2);
//		CustomContext cc3 = new CustomContext("c3",userProfile);
//		cc3.setTreeSize(30);
//		cc3.setUserProfile(userProfile);
//		userProfile.addCustomContext(cc3);
//		
//		//create custom categories
//		CustomManagedCategory ccat1 = new CustomManagedCategory("cp1", userProfile);
//		ccat1.setUserProfile(userProfile);
//		userProfile.addCustomCategory(ccat1);
//		CustomManagedCategory ccat2 = new CustomManagedCategory("cp2", userProfile);
//		ccat2.setUserProfile(userProfile);
//		userProfile.addCustomCategory(ccat2);
//		CustomManagedCategory ccat3 = new CustomManagedCategory("cp3", userProfile);
//		ccat3.setUserProfile(userProfile);
//		userProfile.addCustomCategory(ccat3);
//		
//		//subscribe some categories
//		cc1.addSubscription(ccat1.getProfile());
//		cc1.addSubscription(ccat2.getProfile());
//		cc1.addSubscription(ccat3.getProfile());
//		
//		//unfold some categogires
//		cc1.unfoldCategory(ccat2.getProfileId());
//		cc1.unfoldCategory(ccat3.getProfileId());
//
//		//create custom sources
//		ManagedCategoryProfile mcp = new ManagedCategoryProfile();
//		ManagedSourceProfile msp = new ManagedSourceProfile(mcp);
//		msp.setId("url1");
//		CustomManagedSource cs1 = new CustomManagedSource(msp, userProfile);
//		userProfile.addCustomSource(cs1);
//		msp.setId("url2");
//		CustomManagedSource cs2 = new CustomManagedSource(msp, userProfile);
//		userProfile.addCustomSource(cs2);
//		
//		//subcribe some sources
////genere duplicate key		
////		Collection<CustomCategory> collec = userProfile.getCustomCategories().values();
////		for(CustomCategory cc : collec) {
////			if (cc.getProfileId().equals("cp2")) {
////				CustomManagedCategory cmc = (CustomManagedCategory)cc;
////				cmc.addSubscription((ManagedSourceProfile)cs1.getProfile());
////			}
////			if (cc.getProfileId().equals("cp3")) {
////				CustomManagedCategory cmc = (CustomManagedCategory)cc;
////				cmc.addSubscription((ManagedSourceProfile)cs2.getProfile());
////			}
////		}
////ne manipule plus l'object ratach� � userprofile.... Cf. mail gwena�lle
////		ccat2.addSubscription((ManagedSourceProfile)cs1.getProfile());
////		ccat3.addSubscription((ManagedSourceProfile)cs2.getProfile());
//		
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
