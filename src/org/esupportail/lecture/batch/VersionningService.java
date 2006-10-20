/**
 * ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
 * http://sourcesup.cru.fr/projects/esup-lecture
 */
package org.esupportail.lecture.batch; 

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.esupportail.lecture.domain.beans.Department;
import org.esupportail.lecture.domain.beans.DepartmentManager;
import org.esupportail.lecture.domain.beans.Thing;
import org.esupportail.lecture.domain.beans.User;
import org.esupportail.lecture.web.controllers.AbstractDomainAwareBean;
import org.esupportail.commons.dao.HibernateUtils;
import org.esupportail.commons.exceptions.ConfigException;
import org.esupportail.commons.services.application.Version;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.springframework.util.Assert;

/**
 * A bean for versionning management.
 */
public class VersionningService extends AbstractDomainAwareBean {

	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());
	
	/**
	 * The id of the first administrator.
	 */
	private String firstAdministratorId;
	
	/**
	 * Bean constructor.
	 */
	public VersionningService() {
		super();
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		Assert.notNull(this.firstAdministratorId, 
				"property firstAdministratorId of class " + this.getClass().getName() 
				+ " can not be null");
	}

	/**
	 * print the last version available.
	 */
	private void printLastVersion() {
		Version latestVersion = getApplicationService().getLatestVersion();
		if (latestVersion != null) {
			logger.info("Latest version available: " + latestVersion);
		}
	}
	
	/**
	 * Set the database version.
	 * @param version 
	 * @param silent true to omit info messages
	 */
	public void setDatabaseVersion(
			final String version, 
			final boolean silent) {
		getDomainService().setDatabaseVersion(version);
		if (!silent) {
			logger.info("database version set to " + version + ".");
		}
	}

	/**
	 * @return the database version.
	 */
	public Version getDatabaseVersion() {
		return getDomainService().getDatabaseVersion();
	}

	/**
	 * Initialize the database.
	 */
	public void init() {
		HibernateUtils.createDatabase();
		logger.info("creating the first user of the application thanks to " 
				+ getClass().getName() + ".firstAdministratorId...");
		User firstAdministrator = getDomainService().getUser(firstAdministratorId);
		getDomainService().addAdmin(firstAdministrator);
		logger.info("creating the first department...");
		Department department = new Department();
		department.setLabel(getString("VERSIONNING.TEXT.FIRST_DEPARTMENT.LABEL"));
		department.setXlabel(getString("VERSIONNING.TEXT.FIRST_DEPARTMENT.XLABEL"));
		getDomainService().addDepartment(department);
		logger.info("letting user [" + firstAdministratorId + "] manage the department...");
		DepartmentManager departmentManager = new DepartmentManager();
		departmentManager.setUser(firstAdministrator);
		departmentManager.setDepartment(department);
		departmentManager.setManageThings(true);
		getDomainService().addDepartmentManager(departmentManager);
		logger.info("creating the first thing...");
		long now = System.currentTimeMillis();
		Thing thing = getDomainService().addThing(department, firstAdministrator, now);
		thing.setDepartment(department);
		thing.setValue(getString("VERSIONNING.TEXT.FIRST_THING.VALUE"));
		getDomainService().updateThing(thing, firstAdministrator, now);
		logger.info("the database has been created.");
		setDatabaseVersion("0.0.0", true);
		upgrade();
	}

	/**
	 * check the database version, silently upgrade if possible.
	 * @param throwException 
	 * @param printLatestVersion 
	 * @throws ConfigException 
	 */
	public void checkVersion(
			final boolean throwException,
			final boolean printLatestVersion) throws ConfigException {
		Version databaseVersion = getDomainService().getDatabaseVersion();
		Version applicationVersion = getApplicationService().getVersion();
		if (databaseVersion == null) {
			String msg = "Your database is not initialized, please run 'ant init'.";
			if (throwException) {
				throw new ConfigException(msg);
			}
			logger.error(msg);
			if (printLatestVersion) {
				printLastVersion();
			}
			return;
		}
		if (applicationVersion.equals(databaseVersion)) {
			String msg = "The database is up to date.";
			if (throwException) {
				if (logger.isDebugEnabled()) {
					logger.debug(msg);
				}
			} else {
				logger.info(msg);
			}
			if (printLatestVersion) {
				printLastVersion();
			}
			return;
		}
		if (applicationVersion.isSameMajorAndMinor(databaseVersion)) {
			logger.info("Database version is " + databaseVersion + ", upgrading...");
			upgrade();
			if (printLatestVersion) {
				printLastVersion();
			}
			return;
		}
		if (databaseVersion.isOlderThan(applicationVersion)) {
			String msg = "The database is too old (" + databaseVersion + "), please run 'ant upgrade'.";
			if (throwException) {
				throw new ConfigException(msg);
			}
			logger.error(msg);
			if (printLatestVersion) {
				printLastVersion();
			}
			return;
		}
		String msg = "The application is too old (" + databaseVersion + "), please upgrade.";
		if (throwException) {
			throw new ConfigException(msg);
		}
		if (printLatestVersion) {
			printLastVersion();
		}
		logger.error(msg);
	}
	
	/**
	 * Print a message saying that the database version is older than ...
	 * @param version the new version
	 */	
	protected void printOlderThanMessage(final String version) {
		logger.info(new StringBuffer("database version (")
				.append(getDomainService().getDatabaseVersion())
				.append(") is older than ")
				.append(version)
				.append(", upgrading..."));
	}
	
	/**
	 * Upgrade the database to version 0.1.0.
	 */
	public void upgrade0d9d0() {
		// nothing to do yet
	}

	/**
	 * Upgrade the database to a given version, if needed.
	 * @param version 
	 */
	private void upgradeIfNeeded(final String version) {
		if (!getDatabaseVersion().isOlderThan(version)) {
			return;
		}
		printOlderThanMessage(version);
		String methodName = "upgrade" + version.replace('.', 'd');
		Class [] methodArgs = new Class [] {};
		Method method;
		try {
			method = getClass().getMethod(methodName, methodArgs);
		} catch (SecurityException e) {
			throw new ConfigException(
					"access to the information of class " + getClass() + " was denied", e);
		} catch (NoSuchMethodException e) {
			throw new ConfigException(
					"could no find method " + getClass() + "." + methodName + "()", e);
		}
		Exception invocationException = null;
		try {
			method.invoke(this, new Object[] {});
			setDatabaseVersion(version, false);
			return;
		} catch (IllegalArgumentException e) {
			invocationException = e;
		} catch (IllegalAccessException e) {
			invocationException = e;
		} catch (InvocationTargetException e) {
			if (e.getCause() == null) {
				invocationException = e;
			} else if (e.getCause() instanceof Exception) {
				invocationException = (Exception) e.getCause();
			} else {
				invocationException = e;
			}
		}
		throw new ConfigException(
				"could no invoke method " + getClass() + "." + methodName + "()", 
				invocationException);
	}

	/**
	 * Upgrade the database.
	 */
	public void upgrade() {

		if (getDatabaseVersion().equals(getApplicationService().getVersion())) {
			logger.info("The database is up to date, no need to upgrade.");
			return;
		}

		HibernateUtils.updateDatabase();
		upgradeIfNeeded("0.9.0");

		if (!getDatabaseVersion().equals(getApplicationService().getVersion())) {
			setDatabaseVersion(getApplicationService().getVersion().toString(), false);
		}
	}

	/**
	 * @see org.esupportail.commons.web.controllers.Resettable#reset()
	 */
	public void reset() {
		// nothing to reset
	}

	/**
	 * @return the firstAdministratorId
	 */
	public String getFirstAdministratorId() {
		return firstAdministratorId;
	}

	/**
	 * @param firstAdministratorId the firstAdministratorId to set
	 */
	public void setFirstAdministratorId(final String firstAdministratorId) {
		this.firstAdministratorId = firstAdministratorId;
	}

}
