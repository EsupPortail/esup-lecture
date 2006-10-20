/**
 * ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
 * http://sourcesup.cru.fr/projects/esup-lecture
 */
package org.esupportail.lecture.web.servlet;

import org.esupportail.lecture.batch.VersionningService;
import org.esupportail.lecture.batch.BatchUtils;
import org.esupportail.commons.exceptions.ConfigException;
import org.esupportail.commons.web.servlet.ExceptionHandlingFacesServlet;

/**
 * The main servlet.
 */
public class Servlet extends ExceptionHandlingFacesServlet {

	/**
	 * Constructor.
	 */
	public Servlet() {
		super();
	}

	/**
	 * @see org.esupportail.commons.web.servlet.ExceptionHandlingFacesServlet#checkVersion()
	 */
	@Override
	protected void checkVersion() throws ConfigException {
		VersionningService bean = BatchUtils.createVersionningService();
		bean.checkVersion(true, false);
	}

}
