/**
 * ESUP-Portail Blank Application - Copyright (c) 2006 ESUP-Portail consortium
 * http://sourcesup.cru.fr/projects/esup-blank
 */
package org.esupportail.lecture.services.application; 

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.DomainServiceImpl;
import org.esupportail.lecture.domain.beans.User;
import org.esupportail.lecture.web.controllers.AbstractDomainAwareBean;
import org.esupportail.commons.dao.HibernateUtils;
import org.esupportail.commons.exceptions.ConfigException;
import org.esupportail.commons.services.application.Version;
import org.esupportail.commons.services.application.VersionningService;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.utils.Assert;

/**
 * A bean for versionning management.
 */
public class VersionningServiceImpl extends AbstractDomainAwareBean implements VersionningService {

	/**
	 * A logger.
	 */
	private static final Log log = LogFactory.getLog(DomainServiceImpl.class);

	/**
	 * @see org.esupportail.commons.services.application.VersionningService#initDatabase()
	 */
	public void initDatabase() {
		// TODO Auto-generated method stub
		log.info("ici!!!!");
		HibernateUtils.create();
		getDomainService().getConnectedUser("foo");
		HibernateUtils.close(false);
	}

	/**
	 * @see org.esupportail.commons.services.application.VersionningService#checkVersion(boolean, boolean)
	 */
	public void checkVersion(boolean throwException, boolean printLatestVersion) throws ConfigException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see org.esupportail.commons.services.application.VersionningService#upgradeDatabase()
	 */
	public void upgradeDatabase() {
		// TODO Auto-generated method stub
		
	}
}
