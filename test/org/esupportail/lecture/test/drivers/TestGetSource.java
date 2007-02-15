package org.esupportail.lecture.test.drivers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.dao.DaoServiceRemoteXML;
import org.esupportail.lecture.domain.model.Channel;
import org.esupportail.lecture.domain.model.ManagedSourceProfile;
import org.esupportail.lecture.domain.model.Source;
import org.esupportail.lecture.domain.model.SourceProfile;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

public class TestGetSource {
	protected static final Log log = LogFactory.getLog(TestGetSource.class);

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		ClassPathResource res = new ClassPathResource("properties/applicationContext.xml");
		XmlBeanFactory factory = new XmlBeanFactory(res);
		Channel channel = (Channel)factory.getBean("channel");
		DaoServiceRemoteXML daoServiceRemoteXML = (DaoServiceRemoteXML)factory.getBean("daoServiceRemoteXML");
//		InputStream inputStream = daoServiceRemoteXML.getContent("file:///d:/tmp/conf_lecture_gwe_ray/jeux-flash.xml"); 
//		Reader reader = new InputStreamReader(inputStream);
//		BufferedReader bufferedReader = new BufferedReader(reader);
//		String line;
//		while ((line = bufferedReader.readLine()) != null) {
//			log.info(line);
//		}
	}

}
