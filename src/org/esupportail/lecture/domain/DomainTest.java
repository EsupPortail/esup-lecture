package org.esupportail.lecture.domain;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
	
	/**
	 * @param args non argumet needed
	 */
	public static void main(String[] args) {
		ClassPathResource res = new ClassPathResource("applicationContext.xml");
		XmlBeanFactory factory = new XmlBeanFactory(res);
		
		facadeService = (FacadeService)factory.getBean("facadeService");

		System.out.println("Initialisation .......................");
		facadeService.initialize();
		System.out.println("Initialisation terminée.......................");
		// TODO compléter pour chaque méthode à tester

		
		
	}



	public FacadeService getFacadeService() {
		return facadeService;
	}

	public void setFacadeService(FacadeService service) {
		DomainTest.facadeService = service;
	}
}
