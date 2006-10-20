/**
 * ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
 * http://sourcesup.cru.fr/projects/esup-lecture
 */
package org.esupportail.lecture.batch; 

import org.esupportail.commons.services.application.ApplicationService;
import org.esupportail.commons.services.application.ApplicationUtils;
import org.esupportail.commons.services.authentication.AuthenticationUtils;
import org.esupportail.commons.services.exceptionHandling.ExceptionUtils;
import org.esupportail.commons.services.i18n.I18nUtils;
import org.esupportail.commons.services.ldap.LdapUtils;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.services.smtp.SmtpUtils;

/**
 * A class with a main method called by ant targets.
 */
public class Batch {

	/**
	 * A logger.
	 */
	private static final Logger LOG = new LoggerImpl(Batch.class);
	
	/**
	 * Bean constructor.
	 */
	private Batch() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Print the syntax and exit.
	 */
	private static void syntax() {
		throw new IllegalArgumentException(
				"syntax: " + Batch.class.getSimpleName() + " <options>"
				+ "\nwhere option can be:"
				+ "\n- check-version: initialize the database"
				+ "\n- init: initialize the database"
				+ "\n- upgrade: upgrade the database"
				+ "\n- test-beans: test the required beans");
	}
	
	/**
	 * print the version.
	 */
	private static void printVersion() {
		ApplicationService applicationService = ApplicationUtils.createApplicationService();
		LOG.info(applicationService.getName() + " v" + applicationService.getVersion());
	}

	/**
	 * Check the version.
	 */
	private static void checkVersion() {
		printVersion();
		BatchUtils.createVersionningService().checkVersion(false, true);
	}

	/**
	 * Initialize the application.
	 */
	protected static void init() {
		printVersion();
		BatchUtils.createVersionningService().init();
	}

	/**
	 * Upgrade the application.
	 */
	protected static void upgrade() {
		printVersion();
		BatchUtils.createVersionningService().upgrade();
	}

	/**
	 * Test the required beans.
	 */
	private static void testBeans() {
		ApplicationUtils.createApplicationService();
		AuthenticationUtils.createAuthenticationService();
		I18nUtils.createI18nService();
		LdapUtils.createLdapService();
		SmtpUtils.createSmtpService();
		BatchUtils.createVersionningService();
		ExceptionUtils.createExceptionService();
	}

	/**
	 * Dispatch dependaing on the arguments.
	 * @param args
	 */
	protected static void dispatch(final String[] args) {
		switch (args.length) {
		case 0:
			syntax();
			break;
		case 1:
			if ("init".equals(args[0])) {
				init();
			} else if ("upgrade".equals(args[0])) {
				upgrade();
			} else if ("check-version".equals(args[0])) {
				checkVersion();
			} else if ("test-beans".equals(args[0])) {
				testBeans();
			} else {
				syntax();
			}
			break;
		default:
			syntax();
			break;
		}
	}

	/**
	 * The main method, called by ant.
	 * @param args
	 */
	public static void main(final String[] args) {
		try {
			dispatch(args);
		} catch (Exception e) {
			ExceptionUtils.catchException(e);
		}
	}

}
