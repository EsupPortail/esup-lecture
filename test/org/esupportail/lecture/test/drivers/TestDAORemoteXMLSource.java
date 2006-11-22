/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.test.drivers;

import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.dao.DaoService;
import org.esupportail.lecture.domain.model.Channel;
import org.esupportail.lecture.domain.model.Item;
import org.esupportail.lecture.domain.model.ManagedCategoryProfile;
import org.esupportail.lecture.domain.model.ManagedSourceProfile;
import org.esupportail.lecture.domain.model.Source;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

/**
 * Stub to test classes in package org.esupportail.domain.model
 * @author gbouteil
 * @see org.esupportail.lecture.domain.model
 */
public class TestDAORemoteXMLSource {
	
	protected static final Log log = LogFactory.getLog(Channel.class); 
	
	/**
	 * @param args non argumet needed
	 */
	public static void main(String[] args) {
	
		ClassPathResource res = new ClassPathResource("properties/applicationContext.xml");
		XmlBeanFactory factory = new XmlBeanFactory(res);
		
		//TODO : RB test with DaoService and not DaoServiceImpl
		//DaoService dao = (DaoService)factory.getBean("daoServiceImpl");
		Channel channel = (Channel)factory.getBean("channel");
		DaoService dao = (DaoService)factory.getBean("daoService");
		Source src = null;
		ManagedCategoryProfile managedCategoryProfile = new ManagedCategoryProfile();
		managedCategoryProfile.setUrlCategory("http://perso.univ-rennes1.fr/raymond.bourges/categoryTest.xml");
		//test.init();
		managedCategoryProfile.setTtl(100);
		managedCategoryProfile.setId("test");
		ManagedSourceProfile managedSourceProfile = new ManagedSourceProfile(managedCategoryProfile);
		//test.("http://perso.univ-rennes1.fr/raymond.bourges/categoryTest.xml");
		//test.init();
		managedSourceProfile.setTtl(100);
		managedSourceProfile.setId("test");
		managedSourceProfile.setSourceURL("http://info.cri.univ-rennes1.fr/rss/rss.php");
		src = dao.getSource(managedSourceProfile);
		printSrc(src);
//		printSrc(src);
//		src = dao.getSource("http://perso.univ-rennes1.fr/raymond.bourges/esup-portlet-spring-JSF.xml", 100, "test", false);
//		printSrc(src);
//		src = dao.getSource("http://www.lemonde.fr/rss/sequence/0,2-3208,1-0,0.xml", 100, "test", false);
//		printSrc(src);
//		src = dao.getSource("http://federation.cru.fr/test/deploiement/cru-test-metadata.xml", 100, "test", false);
//		printSrc(src);		
	}

	private static void printSrc(Source src) {
		System.out.println("*********************************");
		System.out.println(" dtd --> "+src.getDtd());
		System.out.println(" rootElement --> "+src.getRootElement());
		System.out.println(" xmlns --> "+src.getXmlns());
		System.out.println(" xmlType --> "+src.getXmlType());
		//System.out.println(" xmlStream --> "+src.getXmlStream());
		System.out.println(" xsltURL --> "+src.getXsltURL());
		System.out.println(" itemXPath --> "+src.getItemXPath());
		List<Item> Items = src.getItems();
		Iterator<Item> iter = Items.iterator();
		while (iter.hasNext()) {
			Item item = iter.next();
			System.out.println("=========================================");
			System.out.println(" item avec Id "+item.getId()+" --> "+item.getHtmlContent());
		}
	}
}
