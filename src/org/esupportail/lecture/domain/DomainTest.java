/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.beans.CategoryBean;
import org.esupportail.lecture.domain.beans.ContextBean;
import org.esupportail.lecture.domain.beans.ItemBean;
import org.esupportail.lecture.domain.beans.SourceBean;
import org.esupportail.lecture.domain.beans.UserBean;
import org.esupportail.lecture.domain.model.ItemDisplayMode;
import org.esupportail.lecture.exceptions.domain.CategoryNotLoadedException;
import org.esupportail.lecture.exceptions.domain.CategoryNotVisibleException;
import org.esupportail.lecture.exceptions.domain.ContextNotFoundException;
import org.esupportail.lecture.exceptions.domain.DomainServiceException;
import org.esupportail.lecture.exceptions.domain.InfoDomainException;
import org.esupportail.lecture.exceptions.domain.InternalDomainException;
import org.esupportail.lecture.exceptions.domain.InternalExternalException;
import org.esupportail.lecture.exceptions.domain.ManagedCategoryProfileNotFoundException;
import org.esupportail.lecture.exceptions.domain.SourceNotLoadedException;
import org.esupportail.lecture.exceptions.domain.SourceProfileNotFoundException;
import org.esupportail.lecture.exceptions.domain.SourceTimeOutException;
import org.esupportail.lecture.exceptions.domain.TreeSizeErrorException;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

/**
 * Class to test calls to facadeService instead of web interface or command-line.
 * @author gbouteil
 *
 */
// TODO (GB later) : refaire un vrai driver des tests
public class DomainTest {
	/*
	 ************************** PROPERTIES ******************************** */	

	/**
	 * Log instance 
	 */
	protected static final Log LOG = LogFactory.getLog(DomainTest.class); 
	/**
	 * Access to facadeService
	 */
	private static FacadeService facadeService;

	/* Controller local variables */
	/**
	 * user ID for tests
	 */
	private static String userId = "bourges";
	/**
	 * context ID for tests
	 */
	private static String contextId;
	/**
	 * list of category IDs for tests
	 */
	private static List<String> categoryIds;
	/**
	 * item ID for tests
	 */
	private static String itemId;
	/**
	 * source ID for tests
	 */
	private static String sourceId;
	
	
	/*
	 ************************** MAIN *************************************** */	

	/**
	 * @param args non argumet needed
	 */
	public static void main(String[] args)  {
		ClassPathResource res = new ClassPathResource("properties/applicationContext.test.domain.xml");
		XmlBeanFactory factory = new XmlBeanFactory(res);
		facadeService = (FacadeService)factory.getBean("facadeService");
		
		try {
			/* Test alternative behavior */
//			testGetContextBis("c1");
//			testGetAvailableSourceAlternativeWay(); 
			
			/* Test normal behavior */
//			testGetConnectedUser();
//			testGetContext();
//			testGetAvailableCategories();
//			testGetAvailableSources();
//			testGetItems();
		
			/* small actions */
//			testMarkItemReadMode();
//			testSetTreeSize();
//			testFoldCategory();
//			testSetItemDisplayMode();
			
			/* test mode EDIT */
//			testGetConnectedUser();
//			testGetContext();
//			testGetVisibleCategories();
//			testGetVisibleSources();
//			testSubUnSubscribeToSource();

			testGetConnectedUser();
			testGetContext();
			testGetVisibleCategories();
			testSubUnSubscribeToCategory();
			
			/* test timeout values */
//			testGetConnectedUser();
//			testGetContext();
//			testGetAvailableCategories();
//			testGetAvailableSources();
//			testTimeOutValues();
		
			// TODO (GB later) : tester pour un user OBLIGED et ALLOWED opour une source : le OBLIGED est prioritaire
			
		} catch (InternalExternalException e) {
			System.out.println("\n!!! EXCEPTION !!!");
			System.out.println("\n!!! Catching InternalExternalException");
			e.printStackTrace();
		}catch (InfoDomainException e) {
			System.out.println("\n!!! EXCEPTION !!!");
			System.out.println("\n!!! Catching InfoDomainException");
			e.printStackTrace();
//		} catch (InternalDomainException e) {
//			System.out.println("\n!!! EXCEPTION !!!");
//			System.out.println("\n!!! Catching InternalDomainException");
//			e.printStackTrace();
		} catch (DomainServiceException e) {
			System.out.println("\n!!! EXCEPTION !!!");
			System.out.println("\n!!! Catching DomainServiceException");
			e.printStackTrace();
		} 
		

	
	}


	/*
	 ************************** Méthodes de Test ******************************** */	

	


	


	/**
	 * Test of servide "getConnectedUser"
	 * @throws InternalExternalException 
	 */
	private static void testGetConnectedUser() throws InternalExternalException {
		printIntro("getConnectedUser");
		String userIdLocal = facadeService.getConnectedUserId();
		UserBean user = facadeService.getConnectedUser(userIdLocal);
		System.out.println(user.toString());
	}
	
	/**
	 * Test of service "getContext"
	 * @throws InternalExternalException 
	 * @throws ContextNotFoundException 
	 */
	private static void testGetContext() throws InternalExternalException, ContextNotFoundException{
		printIntro("getContext");
		contextId = facadeService.getCurrentContextId();
		ContextBean context = facadeService.getContext(userId,contextId);
		System.out.println(context.toString());
	}
	/**
	 * @param cid
	 * @throws ContextNotFoundException
	 */
	@SuppressWarnings("unused")
	private static void testGetContextBis(String cid) throws ContextNotFoundException  {
		printIntro("getContext");
		ContextBean context = facadeService.getContext(userId,cid);
		System.out.println(context.toString());
	}

	/**
	 * Test of service "getCategories"
	 * @throws ContextNotFoundException 
	 * @throws DomainServiceException 
	 */
	private static void testGetAvailableCategories() throws ContextNotFoundException,DomainServiceException{
		printIntro("getAvailableCategories");
		List<CategoryBean> categories = facadeService.getAvailableCategories(userId, contextId);
		categoryIds = new ArrayList<String>();
		for(CategoryBean cat : categories){
			categoryIds.add(cat.getId());
			System.out.println(" **** categorie ****");
			System.out.println(cat.toString());
		}
		
	}
	
	/**
	 * Test of service "getAvailableSources"
	 * @throws DomainServiceException 
	 */
	private static void testGetAvailableSources() throws DomainServiceException  {
		printIntro("getAvailableSources");
		for(String catId : categoryIds){
			System.out.println(" **** cat "+catId+" **********");
			List<SourceBean> sources = facadeService.getAvailableSources(userId, catId);
			for(SourceBean so : sources){
				System.out.println("  **** source ****");
				System.out.println(so.toString());
				sourceId = so.getId();
			}
		}
		
	}
	
	/**
	 * Test of service "getVisibleCategories"
	 */
	@SuppressWarnings("unused")
	private static void testGetVisibleCategories()  {
		printIntro("getVisibleCategories");
		List<CategoryBean> cats;
		try {
			cats = facadeService.getVisibleCategories(userId, contextId);
			for(CategoryBean ca : cats){
				System.out.println("  **** category ****");
				System.out.println(ca.toString());
			}	
		} catch (ContextNotFoundException e) {
			System.out.println("ContextNotFoundException !!!! sur context "+contextId);
		}
	}
	
	/**
	 * Test of service "getVisibleSources"
	 * @throws DomainServiceException 
	 */
	@SuppressWarnings("unused")
	private static void testGetVisibleSources() throws DomainServiceException {
		printIntro("getVisibleSources");
		categoryIds = new ArrayList<String>();
		categoryIds.add("cp1");
		categoryIds.add("cp2");
		//categoryIds.add("cp3");
		//categoryIds.add("cp4");
		categoryIds.add("cp5");

		for(String catId : categoryIds){
			System.out.println(" **** cat "+catId+" **********");
			try {
				List<SourceBean> sources = facadeService.getVisibleSources(userId, catId);
				for(SourceBean so : sources){
					System.out.println("  **** source ****");
					System.out.println(so.toString());
					sourceId = so.getId();
				}
			}catch (CategoryNotVisibleException e){
				System.out.println("CategoryNotVisibleException !!!! sur category "+catId);
			}
		}
	}

	/**
	 * Test of service "subscribeToSource" and "unsubscribeToSource"
	 * @throws DomainServiceException 
	 */
	@SuppressWarnings("unused")
	private static void testSubUnSubscribeToSource() throws DomainServiceException  {
		printIntro("getSubscribeToSource");
		
		try {
			/* source obliged */
			System.out.println(" **** category cp5 : subscribe to source 'un' **********");
			facadeService.subscribeToSource(userId, "cp5", "m:cp5:un");
			/* source allowed or autosub */
//			System.out.println(" **** category cp5 : subscribe to source 'deux' **********");
//			facadeService.subscribeToSource(userId, "cp5", "m:cp5:deux");
			System.out.println(" **** category cp5 : subscribe to source 'trois' **********");
			facadeService.subscribeToSource(userId, "cp5", "m:cp5:trois");
			/* source no visible */
			System.out.println(" **** category cp5 : subscribe to source 'quatre' **********");
			facadeService.subscribeToSource(userId, "cp5", "m:cp5:quatre");
			
			/* category not subcribed to */
			System.out.println(" **** category cp3 : subscribe to source 'trois' **********");
			facadeService.subscribeToSource(userId, "cp3", "m:cp5:trois");
		} catch (DomainServiceException e) {
			System.out.println("DomainServiceException !!!! ");
			e.printStackTrace();
		}
		
		testGetVisibleSources();
		try {
			/* source obliged */
			System.out.println(" **** category cp5 : UNsubscribe to source 'un' **********");
			facadeService.unsubscribeToSource(userId, "cp5", "m:cp5:un");
		} catch (DomainServiceException e) {
			System.out.println("DomainServiceException !!!! ");
			e.printStackTrace();
		}
		try {
			/* source allowed or autosubscribed */
			System.out.println(" **** category cp5 : UNsubscribe to source 'deux' **********");
			facadeService.unsubscribeToSource(userId, "cp5", "m:cp5:deux");			
		} catch (DomainServiceException e) {
			System.out.println("DomainServiceException !!!! ");
			e.printStackTrace();
		}
		
		try{
			System.out.println(" **** category cp5 : UNsubscribe to source 'trois' **********");
			facadeService.unsubscribeToSource(userId, "cp5", "m:cp5:trois");
		} catch (DomainServiceException e) {
			System.out.println("DomainServiceException !!!! ");
			e.printStackTrace();
		}
		try{
			System.out.println(" **** category cp5 AGAIN : UNsubscribe to source 'trois' **********");
			facadeService.unsubscribeToSource(userId, "cp5", "m:cp5:trois");
		} catch (DomainServiceException e) {
			System.out.println("DomainServiceException !!!! ");
			e.printStackTrace();
		}
		
		try{
			/* source no visible */
			System.out.println(" **** category cp5 : UNsubscribe to source 'quatre' **********");
			facadeService.unsubscribeToSource(userId, "cp5", "m:cp5:quatre");
		} catch (DomainServiceException e) {
			System.out.println("DomainServiceException !!!! ");
			e.printStackTrace();
		}
		try{
			/* category not subcribed to */
			System.out.println(" **** category cp3 : UNsubscribe to source 'trois' **********");
			facadeService.unsubscribeToSource(userId, "cp3", "m:cp5:trois");
		} catch (DomainServiceException e) {
			System.out.println("DomainServiceException !!!! ");
			e.printStackTrace();
		}
		
		categoryIds = new ArrayList<String>();
		categoryIds.add("cp5");
		testGetAvailableSources();
	}
	
	/**
	 * Test of service "subscribeToCategory" and "unsubscribeToCategory"
	 * @throws DomainServiceException 
	 */
	private static void testSubUnSubscribeToCategory() {
		printIntro("getSubscribeToCategory");
		
		try {
			/* category obliged */
			System.out.println(" **** subscribe to category 'cp2' **********");
			facadeService.subscribeToCategory(userId, contextId, "cp2");
		} catch (DomainServiceException e) {
			System.out.println("DomainServiceException !!!! ");
			System.out.println("Exception : "+e.getMessage()); 
		}
		try {
			/* category allowed*/
			System.out.println(" **** subscribe to category 'cp4' **********");
			facadeService.subscribeToCategory(userId, contextId, "cp4");
			System.out.println(" **** subscribe to category 'cp4' **********");
			facadeService.subscribeToCategory(userId, contextId, "cp4");
		} catch (DomainServiceException e) {
			System.out.println("DomainServiceException !!!! ");
			System.out.println("Exception : "+e.getMessage()); 
		}
		try {
			/* category autosubscribed*/
			System.out.println(" **** subscribe to category 'cp1' **********");
			facadeService.subscribeToCategory(userId, contextId, "cp1");
			System.out.println(" **** subscribe to category 'cp1' **********");
			facadeService.subscribeToCategory(userId, contextId, "cp1");
		} catch (DomainServiceException e) {
			System.out.println("DomainServiceException !!!! ");
			System.out.println("Exception : "+e.getMessage()); 
		}
		try {
			/* category no visible */
			System.out.println(" **** subscribe to category 'cp3' **********");
			facadeService.subscribeToCategory(userId, contextId, "cp3");
		} catch (DomainServiceException e) {
			System.out.println("DomainServiceException !!!! ");
			System.out.println("Exception : "+e.getMessage()); 
		}
	
			testGetVisibleCategories();
		
			try {
				/* category obliged */
				System.out.println(" **** unsubscribe to category 'cp2' **********");
				facadeService.unsubscribeToCategory(userId, contextId, "cp2");
			} catch (DomainServiceException e) {
				System.out.println("DomainServiceException !!!! ");
				System.out.println("Exception : "+e.getMessage()); 
			}
			try {
				/* category allowed*/
				System.out.println(" **** unsubscribe to category 'cp4' **********");
				facadeService.unsubscribeToCategory(userId, contextId, "cp4");

				System.out.println(" **** unsubscribe to category 'cp4' **********");
				facadeService.unsubscribeToCategory(userId, contextId, "cp4");
			} catch (DomainServiceException e) {
				System.out.println("DomainServiceException !!!! ");
				System.out.println("Exception : "+e.getMessage()); 
			}
			try {
				/* category autosubscribed*/
				System.out.println(" **** unsubscribe to category 'cp1' **********");
				facadeService.unsubscribeToCategory(userId, contextId, "cp1");
				System.out.println(" **** unsubscribe to category 'cp1' **********");
				facadeService.unsubscribeToCategory(userId, contextId, "cp1");
			} catch (DomainServiceException e) {
				System.out.println("DomainServiceException !!!! ");
				System.out.println("Exception : "+e.getMessage()); 
			}
			try {
				/* category no visible */
				System.out.println(" **** unsubscribe to category 'cp3' **********");
				facadeService.unsubscribeToCategory(userId, contextId, "cp3");
			} catch (DomainServiceException e) {
				System.out.println("DomainServiceException !!!! ");
				System.out.println("Exception : "+e.getMessage()); 
			}
			testGetVisibleCategories();
		
	}
	
	
	/**
	 *  Test of service "getAvailableSources" in an alternative way :
	 *  - the parent category has not been got before
	 * @throws InternalExternalException 
	 * @throws DomainServiceException 
	 */
	@SuppressWarnings("unused")
	private static void testGetAvailableSourceAlternativeWay() throws InternalExternalException, DomainServiceException  {	
		testGetContext();	
		printIntro("getAvailableSources - alternative way");
		categoryIds = new ArrayList<String>();
		categoryIds.add("cp1");
		categoryIds.add("cp2");
		for(String catId : categoryIds){
			System.out.println(" **** cat "+catId+" **********");
			List<SourceBean> sources = facadeService.getAvailableSources(userId, catId);
			for(SourceBean so : sources){
				System.out.println("  **** source ****");
				System.out.println(so.toString());
				sourceId = so.getId();
			}
		}
	}


	/**
	 * Test of service "getItems"
	 * @throws ManagedCategoryProfileNotFoundException 
	 * @throws SourceNotLoadedException 
	 * @throws CategoryNotLoadedException 
	 * @throws SourceTimeOutException 
	 * @throws InternalDomainException 
	 */
	@SuppressWarnings("unused")
	private static void testTimeOutValues() throws ManagedCategoryProfileNotFoundException, SourceNotLoadedException, CategoryNotLoadedException, SourceTimeOutException, InternalDomainException {
		printIntro("testTimeOutValues");
		System.out.println(" ** category CP1 ( no trust category) **********");
		System.out.println(" ***** source un (timeout present) **********");
		facadeService.getItems(userId,"m:cp1:un");
		System.out.println(" ***** source deux (timeout abscent) ********");
		facadeService.getItems(userId,"m:cp1:deux");
		System.out.println("\n");
		System.out.println(" ** category CP5 ( trust category) **********");
		System.out.println(" ***** source un (timeout present) **********");
		facadeService.getItems(userId,"m:cp5:un");
		System.out.println(" ***** source deux (timeout abscent) ********");
		facadeService.getItems(userId,"m:cp5:deux");
		
		
	}
	
	/**
	 * Test of service markItemAsRead and markItemAsUnread
	 * @throws InternalDomainException 
	 * @throws SourceNotLoadedException 
	 * @throws SourceProfileNotFoundException 
	 * @throws CategoryNotLoadedException 
	 * @throws ManagedCategoryProfileNotFoundException 
	 * @throws SourceTimeOutException 
	 */
	@SuppressWarnings("unused")
	private static void testMarkItemReadMode() throws InternalDomainException, SourceNotLoadedException, ManagedCategoryProfileNotFoundException, CategoryNotLoadedException, SourceProfileNotFoundException, SourceTimeOutException {
		printIntro("markItemReadMode");
		System.out.println("Marquage de l'item "+itemId+" comme lu");
		facadeService.marckItemReadMode(userId, "m:cp1:quatre", itemId,true);
		testGetItems();
		System.out.println("Marquage de l'item "+itemId+" comme non lu");
		facadeService.marckItemReadMode(userId, "m:cp1:quatre", itemId,false);
		testGetItems();
		
		
	}

	/**
	 * @throws DomainServiceException
	 */
	@SuppressWarnings("unused")
	private static void testSetItemDisplayMode() throws DomainServiceException {
		printIntro("markItemDisplayMode");
		System.out.println("Marquage de la source 'm:cp5:un' comme UNREADFIRST");
		facadeService.marckItemDisplayMode(userId, "m:cp5:un", ItemDisplayMode.UNREADFIRST);
		testGetAvailableSources();
	}
	
	
	/**
	 * Test of service setTreeSize
	 * @throws TreeSizeErrorException 
	 * @throws ContextNotFoundException 
	 * @throws InternalExternalException 
	 */
	@SuppressWarnings("unused")
	private static void testSetTreeSize() throws ContextNotFoundException, TreeSizeErrorException, InternalExternalException {
		printIntro("setTreeSize");
		int newTreeSize = 10;
		System.out.println("Set tree size to "+newTreeSize);
		facadeService.setTreeSize(userId,contextId,newTreeSize);
		testGetContext();	
	}
	

	/**
	 * Test of service foldCategory and unfoldCategory
	 * @throws DomainServiceException 
	 */
	@SuppressWarnings("unused")
	private static void testFoldCategory() throws DomainServiceException {
		printIntro("foldCategory");
		System.out.println("Pliage de la categorie cp1 (deja pliée) => WARN");
		facadeService.foldCategory(userId, contextId, "cp1");
		System.out.println("Depliage de la categorie cp1 \n");
		facadeService.unfoldCategory(userId, contextId, "cp1");
		testGetAvailableCategories();
		System.out.println("Pliage de la categorie cp1 \n");
		facadeService.foldCategory(userId, contextId, "cp1");
		testGetAvailableCategories();
		
		
	}

	/**
	 * Test of timeOut Values
	 * @throws SourceNotLoadedException
	 * @throws InternalDomainException 
	 * @throws ManagedCategoryProfileNotFoundException 
	 * @throws CategoryNotLoadedException 
	 * @throws SourceProfileNotFoundException 
	 * @throws SourceTimeOutException 
	 */
	@SuppressWarnings("unused")
	private static void testGetItems() throws SourceNotLoadedException, InternalDomainException, ManagedCategoryProfileNotFoundException, CategoryNotLoadedException, SourceProfileNotFoundException, SourceTimeOutException  {
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
