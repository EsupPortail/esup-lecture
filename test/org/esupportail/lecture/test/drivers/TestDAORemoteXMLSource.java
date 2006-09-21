/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.test.drivers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.dao.DaoService;
import org.esupportail.lecture.dao.impl.DaoServiceImpl;
import org.esupportail.lecture.domain.model.Channel;
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
	
		ClassPathResource res = new ClassPathResource("applicationContextRaymond.xml");
		XmlBeanFactory factory = new XmlBeanFactory(res);
		
		//TODO : RB test with DaoService and not DaoServiceImpl
		//DaoService dao = (DaoService)factory.getBean("daoServiceImpl");
		DaoServiceImpl dao = (DaoServiceImpl)factory.getBean("daoServiceImpl");
		Source src = null;
		src = dao.getSource("http://perso.univ-rennes1.fr/raymond.bourges/categoryTest.xml", 100, "test", false);
		printSrc(src);
		src = dao.getSource("http://perso.univ-rennes1.fr/raymond.bourges/esup-portlet-spring-JSF.xml", 100, "test", false);
		printSrc(src);
		src = dao.getSource("http://www.lemonde.fr/rss/sequence/0,2-3208,1-0,0.xml", 100, "test", false);
		printSrc(src);
		src = dao.getSource("http://federation.cru.fr/test/deploiement/cru-test-metadata.xml", 100, "test", false);
		printSrc(src);		
	}

	private static void printSrc(Source src) {
		System.out.println("*********************************");
		System.out.println(" dtd --> "+src.getDtd());
		System.out.println(" rootElement --> "+src.getRootElement());
		System.out.println(" xmlns --> "+src.getXmlns());
		System.out.println(" xmlType --> "+src.getXmlType());
		//System.out.println(" xmlStream --> "+src.getXmlStream());
		
	}
}
