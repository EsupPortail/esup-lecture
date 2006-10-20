/**
 * ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
 * http://sourcesup.cru.fr/projects/esup-lecture
 */
package org.esupportail.lecture.domain;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.esupportail.lecture.dao.DaoService;
import org.esupportail.lecture.domain.beans.Department;
import org.esupportail.lecture.domain.beans.DepartmentManager;
import org.esupportail.lecture.domain.beans.Thing;
import org.esupportail.lecture.domain.beans.User;
import org.esupportail.lecture.domain.beans.VersionManager;
import org.esupportail.lecture.exceptions.DepartmentManagerNotFoundException;
import org.esupportail.lecture.exceptions.DepartmentNotFoundException;
import org.esupportail.commons.exceptions.ConfigException;
import org.esupportail.commons.exceptions.UserNotFoundException;
import org.esupportail.commons.services.application.Version;
import org.esupportail.commons.services.ldap.LdapService;
import org.esupportail.commons.services.ldap.LdapUser;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.util.Assert;

/**
 * The basic implementation of DomainService.
 * 
 * See /properties/domain/domain-example.xml
 */
public class DomainServiceImpl implements DomainService, InitializingBean {

	/**
	 * {@link DaoService}.
	 */
	private DaoService daoService;

	/**
	 * {@link LdapService}.
	 */
	private LdapService ldapService;

	/**
	 * The LDAP attribute that contains the display name. 
	 */
	private String displayNameLdapAttribute;

	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());

	/**
	 * Bean constructor.
	 */
	public DomainServiceImpl() {
		super();
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.daoService, 
				"property daoService of class " + this.getClass().getName() + " can not be null");
		Assert.notNull(this.ldapService, 
				"property ldapService of class " + this.getClass().getName() + " can not be null");
		Assert.notNull(this.displayNameLdapAttribute, 
				"property displayNameLdapAttribute of class " + this.getClass().getName() 
				+ " can not be null");
	}

	//////////////////////////////////////////////////////////////
	// User
	//////////////////////////////////////////////////////////////

	/**
	 * Set the information of a user from a ldapUser.
	 * @param user 
	 * @param ldapUser 
	 */
	private void setUserInfo(
			final User user, 
			final LdapUser ldapUser) {
		String displayName = ldapUser.getAttributes().get(displayNameLdapAttribute).get(0);
		if (displayName == null) {
			displayName = user.getId();
		}
		user.setDisplayName(displayName);
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#updateUserInfo(org.esupportail.lecture.domain.beans.User)
	 */
	public void updateUserInfo(final User user) {
		setUserInfo(user, ldapService.getLdapUser(user.getId()));
		updateUser(user);
	}

	/**
	 * If the user is not found in the database, try to create it from a LDAP search.
	 * @see org.esupportail.lecture.domain.DomainService#getUser(java.lang.String)
	 */
	public User getUser(final String id) throws UserNotFoundException {
		User user = daoService.getUser(id);
		if (user == null) {
			LdapUser ldapUser = this.ldapService.getLdapUser(id);
			user = new User();
			user.setId(ldapUser.getId());
			setUserInfo(user, ldapUser);
			daoService.addUser(user);
			logger.info("user '" + user.getId() + "' has been added to the database");
		}
		return user;
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#getUsers()
	 */
	public List<User> getUsers() {
		return this.daoService.getUsers();
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#updateUser(org.esupportail.lecture.domain.beans.User)
	 */
	public void updateUser(final User user) {
		this.daoService.updateUser(user);
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#addAdmin(org.esupportail.lecture.domain.beans.User)
	 */
	public void addAdmin(
			final User user) {
		user.setAdmin(true);
		updateUser(user);
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#deleteAdmin(org.esupportail.lecture.domain.beans.User)
	 */
	public void deleteAdmin(
			final User user) {
		user.setAdmin(false);
		updateUser(user);
	}
	
	/**
	 * @see org.esupportail.lecture.domain.DomainService#getAdmins()
	 */
	public List<User> getAdmins() {
		return this.daoService.getAdmins();
	}

	/**
	 * @param displayNameLdapAttribute the displayNameLdapAttribute to set
	 */
	public void setDisplayNameLdapAttribute(final String displayNameLdapAttribute) {
		this.displayNameLdapAttribute = displayNameLdapAttribute;
	}

	//////////////////////////////////////////////////////////////
	// Department
	//////////////////////////////////////////////////////////////

	/**
	 * @see org.esupportail.lecture.domain.DomainService#getDepartment(long)
	 */
	public Department getDepartment(final long id) 
	throws DepartmentNotFoundException {
		Department department = this.daoService.getDepartment(id); 
		if (department == null) {
			throw new DepartmentNotFoundException("no department found with id [" + id + "]");
		}
		return department;
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#getDepartments()
	 */
	public List<Department> getDepartments() {
		return this.daoService.getDepartments();
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#addDepartment(org.esupportail.lecture.domain.beans.Department)
	 */
	public void addDepartment(final Department department) {
		this.daoService.addDepartment(department);
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#updateDepartment(org.esupportail.lecture.domain.beans.Department)
	 */
	public void updateDepartment(
			final Department department) {
		this.daoService.updateDepartment(department);
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#deleteDepartment(org.esupportail.lecture.domain.beans.Department)
	 */
	public void deleteDepartment(
			final Department department) {
		this.daoService.deleteDepartment(department);
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#isDepartmentLabelUsed(java.lang.String)
	 */
	public boolean isDepartmentLabelUsed(final String label) {
		return this.daoService.isDepartmentLabelUsed(label);
	}

	//////////////////////////////////////////////////////////////
	// DepartmentManager
	//////////////////////////////////////////////////////////////

	/**
	 * @see org.esupportail.lecture.domain.DomainService#getDepartmentManager(
	 * org.esupportail.lecture.domain.beans.Department, org.esupportail.lecture.domain.beans.User)
	 */
	public DepartmentManager getDepartmentManager(
			final Department department,
			final User user) 
	throws DepartmentManagerNotFoundException {
		DepartmentManager manager = daoService.getDepartmentManager(department, user);
		if (manager == null) {
			throw new DepartmentManagerNotFoundException("user [" + user.getId() 
					+ "] is not a manager of department [" + department.getLabel() + "]");
		}
		return manager;
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#isDepartmentManager(
	 * org.esupportail.lecture.domain.beans.Department, org.esupportail.lecture.domain.beans.User)
	 */
	public boolean isDepartmentManager(
			final Department department, 
			final User user) {
		try {
			getDepartmentManager(department, user);
		} catch (DepartmentManagerNotFoundException e) {
			return false;
		}
		return true;
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#getDepartmentManagers(
	 * org.esupportail.lecture.domain.beans.Department)
	 */
	public List<DepartmentManager> getDepartmentManagers(
			final Department department) {
		return this.daoService.getDepartmentManagers(department);
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#addDepartmentManager(
	 * org.esupportail.lecture.domain.beans.DepartmentManager)
	 */
	public void addDepartmentManager(
			final DepartmentManager departmentManager) {
		this.daoService.addDepartmentManager(departmentManager); 
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#deleteDepartmentManager(
	 * org.esupportail.lecture.domain.beans.DepartmentManager)
	 */
	public void deleteDepartmentManager(
			final DepartmentManager departmentManager) {
		this.daoService.deleteDepartmentManager(departmentManager);
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#updateDepartmentManager(
	 * org.esupportail.lecture.domain.beans.DepartmentManager)
	 */
	public void updateDepartmentManager(
			final DepartmentManager departmentManager) {
		this.daoService.updateDepartmentManager(departmentManager);
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#getManagedDepartments(
	 * org.esupportail.lecture.domain.beans.User)
	 */
	public List<Department> getManagedDepartments(final User user) {
		if (user.getAdmin()) {
			return getDepartments();
		}
		List<DepartmentManager> managers = daoService.getDepartmentManagers(user);
		List<Department> departments = new ArrayList<Department>();
		for (DepartmentManager manager : managers) {
			departments.add(manager.getDepartment());
		}
		return departments;
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#isDepartmentManager(org.esupportail.lecture.domain.beans.User)
	 */
	public boolean isDepartmentManager(final User user) {
		List<DepartmentManager> managers = daoService.getDepartmentManagers(user);
		return !managers.isEmpty();
	}

	//////////////////////////////////////////////////////////////
	// Thing
	//////////////////////////////////////////////////////////////

	/**
	 * @see org.esupportail.lecture.domain.DomainService#getThings(org.esupportail.lecture.domain.beans.Department)
	 */
	public List<Thing> getThings(
			final Department department) {
		return this.daoService.getThings(department);
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#addThing(
	 * org.esupportail.lecture.domain.beans.Department, org.esupportail.lecture.domain.beans.User, long)
	 */
	public Thing addThing(
			final Department department,
			final User user,
			final long date) {
		Thing thing = new Thing();
		thing.setDepartment(department);
		thing.setUser(user);
		thing.setDate(new Timestamp(date));
		thing.setOrder(daoService.getThingsNumber(department));
		this.daoService.addThing(thing);
		return thing;
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#updateThing(
	 * org.esupportail.lecture.domain.beans.Thing, org.esupportail.lecture.domain.beans.User, long)
	 */
	public void updateThing(
			final Thing thing,
			final User user,
			final long date) {
		if (user != null) {
			thing.setUser(user);
			thing.setDate(new Timestamp(date));
		}
		this.daoService.updateThing(thing);
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#deleteThing(org.esupportail.lecture.domain.beans.Thing)
	 */
	public void deleteThing(
			final Thing thing) {
		this.daoService.deleteThing(thing);
	}

	/**
	 * Reorder the things of a department.
	 * @param department
	 */
	private void reorderThings(final Department department) {
		List<Thing> things = getThings(department);
		int i = 0;
		for (Thing thing : things) {
			thing.setOrder(i++);
			updateThing(thing, null, -1);
		}
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#moveFirst(org.esupportail.lecture.domain.beans.Thing)
	 */
	public void moveFirst(final Thing thing) {
		thing.setOrder(Integer.MIN_VALUE);
		updateThing(thing, null, -1);
		reorderThings(thing.getDepartment());
	}
	
	/**
	 * @see org.esupportail.lecture.domain.DomainService#moveLast(org.esupportail.lecture.domain.beans.Thing)
	 */
	public void moveLast(final Thing thing) {
		thing.setOrder(Integer.MAX_VALUE);
		updateThing(thing, null, -1);
		reorderThings(thing.getDepartment());
	}
	
	/**
	 * @see org.esupportail.lecture.domain.DomainService#moveUp(org.esupportail.lecture.domain.beans.Thing)
	 */
	public void moveUp(final Thing thing) {
		Thing previousThing = daoService.getThingByOrder(thing.getDepartment(), thing.getOrder() - 1);
		if (previousThing != null) {
			thing.setOrder(thing.getOrder() - 1);
			updateThing(thing, null, -1);
			previousThing.setOrder(previousThing.getOrder() + 1);
			updateThing(previousThing, null, -1);
		}
	}
	
	/**
	 * @see org.esupportail.lecture.domain.DomainService#moveDown(org.esupportail.lecture.domain.beans.Thing)
	 */
	public void moveDown(final Thing thing) {
		Thing nextThing = daoService.getThingByOrder(thing.getDepartment(), thing.getOrder() + 1);
		if (nextThing != null) {
			thing.setOrder(thing.getOrder() + 1);
			updateThing(thing, null, -1);
			nextThing.setOrder(nextThing.getOrder() - 1);
			updateThing(thing, null, -1);
		}
	}
	
	//////////////////////////////////////////////////////////////
	// VersionManager
	//////////////////////////////////////////////////////////////

	/**
	 * @return the first (and only) VersionManager instance of the database.
	 * @throws ConfigException 
	 */
	private VersionManager getVersionManager() throws ConfigException {
		List<VersionManager> versionManagers = null;
		try {
			versionManagers = daoService.getVersionManagers();
		} catch (BadSqlGrammarException e) {
			throw new ConfigException("your database is not initialized, please run 'ant init'", e);
		}
		if (versionManagers.isEmpty()) {
			return null;
		}
		return versionManagers.get(0);
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#getDatabaseVersion()
	 */
	public Version getDatabaseVersion() throws ConfigException {
		VersionManager versionManager = getVersionManager();
		if (versionManager == null) {
			return null;
		}
		return new Version(versionManager.getVersion());
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#setDatabaseVersion(java.lang.String)
	 */
	public void setDatabaseVersion(final String version) {
		if (logger.isDebugEnabled()) {
			logger.debug("setting database version to '" + version + "'...");
		}
		VersionManager versionManager = getVersionManager();
		if (versionManager == null) {
			versionManager = new VersionManager();
			versionManager.setVersion(version);
			daoService.addVersionManager(versionManager);
		} else {
			versionManager.setVersion(version);
			daoService.updateVersionManager(versionManager);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("database version set to '" + version + "'.");
		}
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#setDatabaseVersion(
	 * org.esupportail.commons.services.application.Version)
	 */
	public void setDatabaseVersion(final Version version) {
		setDatabaseVersion(version.toString());
	}

	//////////////////////////////////////////////////////////////
	// Authorizations
	//////////////////////////////////////////////////////////////

	/**
	 * @see org.esupportail.lecture.domain.DomainService#userCanViewAdmins(org.esupportail.lecture.domain.beans.User)
	 */
	public boolean userCanViewAdmins(final User user) {
		if (user == null) {
			return false;
		}
		return user.getAdmin() || isDepartmentManager(user);
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#userCanAddAdmin(org.esupportail.lecture.domain.beans.User)
	 */
	public boolean userCanAddAdmin(final User user) {
		if (user == null) {
			return false;
		}
		return user.getAdmin();
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#userCanDeleteAdmin(
	 * org.esupportail.lecture.domain.beans.User, org.esupportail.lecture.domain.beans.User)
	 */
	public boolean userCanDeleteAdmin(final User user, final User admin) {
		if (user == null) {
			return false;
		}
		if (!user.getAdmin()) {
			return false;
		}
		return !user.equals(admin);
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#userCanAddDepartment(org.esupportail.lecture.domain.beans.User)
	 */
	public boolean userCanAddDepartment(final User user) {
		if (user == null) {
			return false;
		}
		return user.getAdmin();
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#userCanDeleteDepartment(
	 * org.esupportail.lecture.domain.beans.User, org.esupportail.lecture.domain.beans.Department)
	 */
	public boolean userCanDeleteDepartment(
			final User user,
			@SuppressWarnings("unused") final Department department) {
		if (user == null) {
			return false;
		}
		return user.getAdmin();
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#userCanViewDepartment(
	 * org.esupportail.lecture.domain.beans.User, org.esupportail.lecture.domain.beans.Department)
	 */
	public boolean userCanViewDepartment(
			final User user, 
			final Department department) {
		if (user == null) {
			return false;
		}
		if (user.getAdmin()) {
			return true;
		}
		return isDepartmentManager(department, user);
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#getVisibleDepartments(org.esupportail.lecture.domain.beans.User)
	 */
	public List<Department> getVisibleDepartments(
			final User user) {
		List<Department> result = new ArrayList<Department>(); 
		if (user == null) {
			return result;
		}
		for (Department department : getDepartments()) {
			if (department.getLdapFilter() == null) {
				result.add(department);
			} else if (ldapService.userMatchesFilter(user.getId(), department.getLdapFilter())) {
				result.add(department);
			}
		}
		return result;
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#userCanEditDepartmentProperties(
	 * org.esupportail.lecture.domain.beans.User, org.esupportail.lecture.domain.beans.Department)
	 */
	public boolean userCanEditDepartmentProperties(
			final User user, 
			final Department department) {
		if (user == null) {
			return false;
		}
		if (user.getAdmin()) {
			return true;
		}
		try {
			DepartmentManager manager = getDepartmentManager(department, user);
			return manager.getManageDepartment();
		} catch (DepartmentManagerNotFoundException e) {
			return false;
		}
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#userCanEditDepartmentManagers(
	 * org.esupportail.lecture.domain.beans.User, org.esupportail.lecture.domain.beans.Department)
	 */
	public boolean userCanEditDepartmentManagers(
			final User user, 
			final Department department) {
		if (user == null) {
			return false;
		}
		if (user.getAdmin()) {
			return true;
		}
		try {
			DepartmentManager manager = getDepartmentManager(department, user);
			return manager.getManageManagers();
		} catch (DepartmentManagerNotFoundException e) {
			return false;
		}
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#userCanAddDepartmentManager(
	 * org.esupportail.lecture.domain.beans.User, org.esupportail.lecture.domain.beans.Department)
	 */
	public boolean userCanAddDepartmentManager(
			final User user, 
			final Department department) {
		return userCanEditDepartmentManagers(user, department);
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#userCanDeleteManager(
	 * org.esupportail.lecture.domain.beans.User, org.esupportail.lecture.domain.beans.DepartmentManager)
	 */
	public boolean userCanDeleteManager(
			final User user,
			final DepartmentManager departmentManager) {
		return userCanEditDepartmentManagers(user, departmentManager.getDepartment());
	}

	/**
	 * @param user 
	 * @param department
	 * @return @return 'true' if the user can manage the things fo a department.
	 */
	private boolean userCanManageThings(final User user, final Department department) {
		if (user == null) {
			return false;
		}
		try {
			DepartmentManager manager = getDepartmentManager(department, user);
			return manager.getManageThings();
		} catch (DepartmentManagerNotFoundException e) {
			// not allowed
		}
		return false;
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#userCanAddThing(
	 * org.esupportail.lecture.domain.beans.User, org.esupportail.lecture.domain.beans.Department)
	 */
	public boolean userCanAddThing(final User user, final Department department) {
		return userCanManageThings(user, department);
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#userCanEditThings(
	 * org.esupportail.lecture.domain.beans.User, org.esupportail.lecture.domain.beans.Department)
	 */
	public boolean userCanEditThings(final User user, final Department department) {
		return userCanManageThings(user, department);
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#userCanDeleteThings(
	 * org.esupportail.lecture.domain.beans.User, org.esupportail.lecture.domain.beans.Department)
	 */
	public boolean userCanDeleteThings(final User user, final Department department) {
		return userCanManageThings(user, department);
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#userCanViewThings(
	 * org.esupportail.lecture.domain.beans.User, org.esupportail.lecture.domain.beans.Department)
	 */
	public boolean userCanViewThings(final User user, final Department department) {
		return userCanViewDepartment(user, department);
	}

	//////////////////////////////////////////////////////////////
	// Misc
	//////////////////////////////////////////////////////////////

	/**
	 * @param daoService the daoService to set
	 */
	public void setDaoService(final DaoService daoService) {
		this.daoService = daoService;
	}

	/**
	 * @param ldapService the ldapService to set
	 */
	public void setLdapService(final LdapService ldapService) {
		this.ldapService = ldapService;
	}

}
