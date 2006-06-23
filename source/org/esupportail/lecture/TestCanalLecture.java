package org.esupportail.lecture;

import org.esupportail.lecture.domain.model.Chanel;
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
		
		Chanel myChanel = (Chanel)factory.getBean("chanel");
		
		System.out.println("***********************Configuration du canal lecture : ***********************");
		System.out.println(myChanel.toString());
	}

}
