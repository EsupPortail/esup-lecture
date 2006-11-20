package org.esupportail.lecture.domain;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.dao.DaoService;
import org.esupportail.lecture.domain.beans.ContextBean;
import org.esupportail.lecture.domain.beans.UserBean;
import org.esupportail.lecture.domain.model.Channel;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

/**
 * Class to test calls to facadeService instead of web interface or command-line
 * @author gbouteil
 *
 */
public class DomainTest {
	protected static final Log log = LogFactory.getLog(DomainTest.class); 
	private static FacadeService facadeService;
	/* Controller local variables */
	private static String userId;
	private static String contextId;
	/**
	 * @param args non argumet needed
	 */
	public static void main(String[] args) {
		ClassPathResource res = new ClassPathResource("properties/applicationContext.xml");
		XmlBeanFactory factory = new XmlBeanFactory(res);
		facadeService = (FacadeService)factory.getBean("facadeService");

		testGetConnectedUser();
		//testGetContext();
		// TODO compléter pour chaque méthode à tester
	
		
	}

/*
 * Méthodes de Test
 */

	/**
	 * Test of servide "getConnectedUser"
	 */
	private static void testGetConnectedUser() {
		System.out.println("Test du service getConnectedUser() : ");
		userId = facadeService.getConnectedUserId();
		UserBean user = facadeService.getConnectedUser(userId);
		System.out.println(user.toString());
		System.out.println("\n");
	}
	
	/**
	 * Test of servide "getContext"
	 */
	private static void testGetContext() {
		System.out.println("Test du service getContext() : ");
		contextId = facadeService.getCurrentContextId();
		ContextBean context = facadeService.getContext(userId,contextId);

		System.out.println(context.toString());
		System.out.println("\n");
	}







	public FacadeService getFacadeService() {
		return facadeService;
	}

	public void setFacadeService(FacadeService service) {
		DomainTest.facadeService = service;
	}
}
