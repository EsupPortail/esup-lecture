/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.test.drivers;

import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.DomainTools;
import org.esupportail.lecture.domain.model.Channel;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

/**
 * Stub to test classes in package org.esupportail.domain.model
 * @author gbouteil
 * @see org.esupportail.lecture.domain.model
 */
public class TestDomainLecture {
	
	protected static final Log log = LogFactory.getLog(TestDomainLecture.class);
	private static SessionFactory sessionFactory;
	private static Transaction transaction;
	private static XmlBeanFactory factory; 
	
	private static void replaceAttribute() {
		String testBefore = null;
		String testAfter = null;
		testBefore = "test";
		testAfter = DomainTools.replaceWithUserAttributes(testBefore);
		out(testBefore+" --> "+testAfter);
		testBefore = "test{1}toto";
		testAfter = DomainTools.replaceWithUserAttributes(testBefore);
		out(testBefore+" --> "+testAfter);
		testBefore = "test{ARG1}toto?toto={ARG2}&titi={ARG3}&tt=rr";
		testAfter = DomainTools.replaceWithUserAttributes(testBefore);
		out(testBefore+" --> "+testAfter);
	}
	
	private static void execute(String methodName) {
		Class params[] = {};
		Object paramsObj[] = {};
		out("************************************************");
		out("executing test: "+methodName);
		try {
			Method method = TestDomainLecture.class.getDeclaredMethod(methodName, params);
			method.invoke(TestDomainLecture.class.newInstance(), paramsObj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void out(String string) {
		System.out.println(string);
	}

	/**
	 * @param args test to execute
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
