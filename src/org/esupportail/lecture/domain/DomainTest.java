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
	private static String userId = "bourges";
	private static String contextId;
	private static List<String> categoryIds;
	private static String itemId;
	private static String sourceId;
	/**
	 * @param args non argumet needed
	 */
	public static void main(String[] args) {
		ClassPathResource res = new ClassPathResource("properties/applicationContext.xml");
		XmlBeanFactory factory = new XmlBeanFactory(res);
		facadeService = (FacadeService)factory.getBean("facadeService");
		
		/* Test normal behavior */
//		testGetConnectedUser();
//		testGetContext();
//		testGetVisibleCategories();
//		testGetVisibleSources();
//		testGetItems();
		//testMarkItemAsRead();
		//testSetTreeSize();
		//testFoldCategory();
		
		/* Test alternative behavior */
		testGetContextBis("ccc");
		testGetVisibleSourceAlternativeWay();
	
	}






/*
 * Méthodes de Test
 */

	/**
	 * Test of servide "getConnectedUser"
	 */
	private static void testGetConnectedUser() {
		printIntro("getConnectedUser");
		String userIdLocal = facadeService.getConnectedUserId();
		UserBean user = facadeService.getConnectedUser(userIdLocal);
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
	private static void testGetContextBis(String cid) {
		printIntro("getContext");
		ContextBean context = facadeService.getContext(userId,cid);
		System.out.println(context.toString());
	}

	/**
	 * Test of service "getCategories"
	 */
	private static void testGetVisibleCategories() {
		printIntro("getVisibleCategories");
		List<CategoryBean> categories = facadeService.getVisibleCategories(userId, contextId);
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
	private static void testGetVisibleSources() {
		printIntro("getVisibleSources");
		for(String catId : categoryIds){
			System.out.println(" **** cat "+catId+" **********");
			List<SourceBean> sources = facadeService.getVisibleSources(userId, catId);
			for(SourceBean so : sources){
				System.out.println("  **** source ****");
				System.out.println(so.toString());
				sourceId = so.getId();
			}
		}
		
	}


	/**
	 *  Test of service "getSources" in an alternative way :
	 *  - the parent category has not been got before
	 */
	private static void testGetVisibleSourceAlternativeWay() {	
		testGetContext();	
		printIntro("getVisibleSources - alternative way");
		categoryIds = new ArrayList<String>();
		categoryIds.add("cp1");
		categoryIds.add("cp2");
		for(String catId : categoryIds){
		System.out.println(" **** cat "+catId+" **********");
		List<SourceBean> sources = facadeService.getVisibleSources(userId, catId);
		for(SourceBean so : sources){
			System.out.println("  **** source ****");
			System.out.println(so.toString());
			sourceId = so.getId();
		}
	}
		// TODO Auto-generated method stub
		
	}


	/**
	 * Test of service "getItems"
	 */
	private static void testGetItems() {
		printIntro("getItems");
		System.out.println(" **** source "+sourceId+" **********");
		List<ItemBean> items = facadeService.getItems(userId,sourceId);
		for(ItemBean it : items){
			System.out.println("  **** item ****");
			System.out.println(it.toString());
			itemId = it.getId();
		}
		
	}
	
	/**
	 * Test of service markItemAsRead and markItemAsUnread
	 */
	private static void testMarkItemAsRead() {
		printIntro("markItemAsRead");
		System.out.println("Marquage de l'item "+itemId+" comme lu");
		facadeService.marckItemAsRead(userId, "un", itemId);
		testGetItems();
		System.out.println("Marquage de l'item "+itemId+" comme non lu");
		facadeService.marckItemAsUnread(userId, "un", itemId);
		testGetItems();
		
		
	}


	/**
	 * Test of service setTreeSize
	 */
	private static void testSetTreeSize() {
		printIntro("setTreeSize");
		int newTreeSize = 10;
		System.out.println("Set tree size to "+newTreeSize);
		facadeService.setTreeSize(userId,contextId,newTreeSize);
		testGetContext();	
	}
	

	/**
	 * Test of service foldCategory and unfoldCategory
	 */
	private static void testFoldCategory() {
		printIntro("foldCategory");
		System.out.println("Pliage de la categorie cp1 (deja pliée) => WARN");
		facadeService.foldCategory(userId, contextId, "cp1");
		System.out.println("Depliage de la categorie cp1 \n");
		facadeService.unFoldCategory(userId, contextId, "cp1");
		testGetVisibleCategories();
		System.out.println("Pliage de la categorie cp1 \n");
		facadeService.foldCategory(userId, contextId, "cp1");
		testGetVisibleCategories();
		
		
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
