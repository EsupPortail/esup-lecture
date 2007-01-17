/**
 * ESUP-Portail Blank Application - Copyright (c) 2006 ESUP-Portail consortium
 * http://sourcesup.cru.fr/projects/esup-blank
 */
package org.esupportail.lecture.batch; 

import org.esupportail.commons.services.application.ApplicationService;
import org.esupportail.commons.services.application.ApplicationUtils;
import org.esupportail.commons.services.exceptionHandling.ExceptionUtils;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;

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
				+ "\n- test-beans: test the required beans");
	}
	
	/**
	 * Test the required beans.
	 */
	private static void testBeans() {
//		ApplicationUtils.createApplicationService();
//		AuthenticationUtils.createAuthenticationService();
//		I18nUtils.createI18nService();
//		LdapUtils.createLdapService();
//		PortalUtils.createPortalService();
//		SmtpUtils.createSmtpService();
//		UrlGenerationUtils.createUrlGenerator();
//		DeepLinkingUtils.createDeepLinkingRedirector();
//		VersionningUtils.createVersionningService();
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
			if ("test-beans".equals(args[0])) {
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
			ApplicationService applicationService = ApplicationUtils.createApplicationService();
			LOG.info(applicationService.getName() + " v" + applicationService.getVersion());
			dispatch(args);
		} catch (Exception e) {
			ExceptionUtils.catchException(e);
		}
	}

}
