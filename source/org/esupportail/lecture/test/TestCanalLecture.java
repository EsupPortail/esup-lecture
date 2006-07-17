package org.esupportail.lecture.test;

import org.esupportail.lecture.domain.model.Channel;
import java.util.Iterator;
import java.util.List;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

/**
 * Stub to test classes in package org.esupportail.domain.model
 * @author gbouteil
 * @see org.esupportail.lecture.domain.model
 */
public class TestCanalLecture {

	/**
	 * @param args non argumet needed
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ClassPathResource res = new ClassPathResource("applicationContext.xml");
		XmlBeanFactory factory = new XmlBeanFactory(res);
		
		Channel myChannel = (Channel)factory.getBean("channel");
		try {
			myChannel.startup();
			System.out.println("Tapez une touche ...");
			System.in.read();
			myChannel.startup();
		
		} catch (Exception e) {
			System.out.println("Exception 1 !!!");
			System.out.println(e.getMessage());
		}
		
		System.out.println("***********************Configuration du canal lecture : ***********************");
		System.out.println(myChannel.toString());
	}
}
