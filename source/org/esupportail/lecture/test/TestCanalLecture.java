package org.esupportail.lecture.test;

import org.esupportail.lecture.domain.model.Channel;
import java.util.Iterator;
import java.util.List;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

public class TestCanalLecture {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ClassPathResource res = new ClassPathResource("applicationContext.xml");
		XmlBeanFactory factory = new XmlBeanFactory(res);
		
		Channel myChannel = (Channel)factory.getBean("channel");
		try {
			myChannel.startup();
		} catch (Exception e) {
			System.out.println("Exception !!!");
		}

		System.out.println("***********************Configuration du canal lecture : ***********************");
		System.out.println(myChannel.toString());
	
	
		
	}

}
