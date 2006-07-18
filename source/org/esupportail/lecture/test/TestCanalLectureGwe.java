package org.esupportail.lecture.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.model.Channel;
import org.esupportail.lecture.domain.service.LectureService;

import java.io.IOException;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

/**
 * Stub to test classes in package org.esupportail.domain.model
 * @author gbouteil
 * @see org.esupportail.lecture.domain.model
 */
public class TestCanalLectureGwe {
	
	protected static final Log log = LogFactory.getLog(Channel.class); 
	
	/**
	 * @param args non argumet needed
	 */
	public static void main(String[] args) {
	
		ClassPathResource res = new ClassPathResource("applicationContextGwe.xml");
		XmlBeanFactory factory = new XmlBeanFactory(res);
		
		LectureService myServices = (LectureService)factory.getBean("services");
		try {
			myServices.startChannel();
			System.out.println("Tapez une touche ...");
			System.in.read();
			myServices.startChannel();
		
		} catch (IOException e) {
			log.fatal("Exception I/O !!!");
			log.fatal(e.getMessage());
		} catch (Exception e) {
			log.fatal("Exception on startChannel !!!");
			log.fatal(e.getMessage());
		}
		
		System.out.println("*********************** Configuration du canal lecture : ***********************");
		System.out.println(myServices.channelToString());
	}
}
