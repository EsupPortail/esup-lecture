package org.esupportail.lecture.web.controllers;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.commons.batch.WebApplicationFilter;
import org.esupportail.commons.services.authentication.AuthenticationService;
import org.esupportail.commons.services.authentication.OfflineFixedUserAuthenticator;
import org.esupportail.commons.test.AbstractTest;
import org.esupportail.lecture.web.beans.ItemWebBean;
import org.esupportail.lecture.web.beans.SourceWebBean;

public class TestUserAuth extends AbstractTest {

	/**
	 * Log instance.
	 */
	private static final Log LOG = LogFactory.getLog(TestUserAuth.class);
	/**
	 * user for Test
	 */
	private String user = "toto";
	
	private static int nbRun = 0;
	
	public void testHome() throws Exception {
		LOG.info("=========================== TestUserAuth =================================");
		user = ManyHomeController.users.get(nbRun++);
		LOG.info("User = " + user);
		new WebApplicationFilter(webApplicationUtils, new FilterChain() {

			public void doFilter(
					final ServletRequest arg0, 
					final ServletResponse arg1) 
			throws IOException, ServletException {
				OfflineFixedUserAuthenticator authenticationService = new OfflineFixedUserAuthenticator();
				authenticationService.setUserId(user);
				HomeController homeController = 
					(HomeController) applicationContext.getBean("homeController");
				//attach new authenticationService
				homeController.getSessionController().setAuthenticationService(authenticationService);
			}
		}).run();

	}
}
