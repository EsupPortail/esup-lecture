package org.esupportail.lecture.domain;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.dao.DaoService;
import org.esupportail.lecture.domain.beans.CategoryBean;
import org.esupportail.lecture.domain.beans.ContextBean;
import org.esupportail.lecture.domain.beans.ItemBean;
import org.esupportail.lecture.domain.beans.SourceBean;
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
	private static List<String> categoryIds;
	/**
	 * @param args non argumet needed
	 */
	public static void main(String[] args) {
		ClassPathResource res = new ClassPathResource("properties/applicationContext.xml");
		XmlBeanFactory factory = new XmlBeanFactory(res);
		facadeService = (FacadeService)factory.getBean("facadeService");

		testGetConnectedUser();
		testGetContext();
		testGetCategories();
		testGetSources();
		testGetItems();
	
		
	}

/*
 * Méthodes de Test
 */


	/**
	 * Test of servide "getConnectedUser"
	 */
	private static void testGetConnectedUser() {
		printIntro("getConnectedUser");
		userId = facadeService.getConnectedUserId();
		UserBean user = facadeService.getConnectedUser(userId);
		System.out.println(user.toString());
	}
	
	/**
	 * Test of service "getContext"
	 */
	private static void testGetContext() {
		printIntro("getContext");
		contextId = facadeService.getCurrentContextId();
		ContextBean context = facadeService.getContext(userId,contextId);
		System.out.println(context.toString());
	}

	/**
	 * Test of service "getCategories"
	 */
	private static void testGetCategories() {
		printIntro("getCategories");
		List<CategoryBean> categories = facadeService.getCategories(userId, contextId);
		categoryIds = new ArrayList<String>();
		for(CategoryBean cat : categories){
			categoryIds.add(cat.getId());
			System.out.println(" **** categorie ****");
			System.out.println(cat.toString());
		}
		
	}
	
	/**
	 * Test of service "getSources"
	 */
	private static void testGetSources() {
		printIntro("getSources");
		for(String catId : categoryIds){
			System.out.println(" **** cat "+catId+" **********");
			List<SourceBean> sources = facadeService.getSources(userId, catId);
			for(SourceBean so : sources){
				System.out.println("  **** source ****");
				System.out.println(so.toString());
			}
		}
		
	}


	private static void testGetItems() {
		printIntro("getItems");
		System.out.println(" **** source 'un' **********");
		List<ItemBean> items = facadeService.getItems("un",userId);
		for(ItemBean it : items){
			System.out.println("  **** item ****");
			System.out.println(it.toString());
		}
	}

	/**
	 * Affichage du service à tester
	 * @param nomService nom du service à tester
	 */
	private static void printIntro(String nomService){
		System.out.println("******************************************************");
		System.out.println("Test du service -"+nomService+"- \n");
	}
	
	/**
	 * @return facadeService
	 */
	public FacadeService getFacadeService() {
		return facadeService;
	}

	/**
	 * @param service facadeService
	 */
	public void setFacadeService(FacadeService service) {
		DomainTest.facadeService = service;
	}
}
