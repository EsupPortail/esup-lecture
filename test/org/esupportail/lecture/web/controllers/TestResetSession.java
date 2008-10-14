package org.esupportail.lecture.web.controllers;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.commons.batch.WebApplicationEnvironment;
import org.esupportail.commons.batch.WebApplicationFilter;
import org.esupportail.commons.services.authentication.AuthenticationService;
import org.esupportail.commons.services.authentication.OfflineFixedUserAuthenticator;
import org.esupportail.commons.test.AbstractTest;
import org.esupportail.lecture.web.beans.ItemWebBean;
import org.esupportail.lecture.web.beans.SourceWebBean;

public class TestResetSession extends AbstractTest {

	/**
	 * Log instance.
	 */
	private static final Log LOG = LogFactory.getLog(TestResetSession.class);

	/**
	 * reset current session with a specific overridden onSetUpBeforeTransaction function
	 * @throws Exception
	 */
	public void testResetSession() throws Exception {
		LOG.info("=========================== TestResetSession =================================");
	}

	/**
	 * @see org.esupportail.commons.test.AbstractTest#onSetUpBeforeTransaction()
	 */
	@Override
	protected void onSetUpBeforeTransaction() throws Exception {
		super.onSetUpBeforeTransaction();
		//false argument remove session from request
		webApplicationUtils = new WebApplicationEnvironment(false);
	}
	
	
}
