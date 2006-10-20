/**
 * ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
 * http://sourcesup.cru.fr/projects/esup-lecture
 */
package org.esupportail.lecture.domain;

import java.util.List;

import org.esupportail.lecture.domain.beans.Department;
import org.esupportail.lecture.domain.beans.DepartmentManager;
import org.esupportail.lecture.domain.beans.Thing;
import org.esupportail.lecture.domain.beans.User;
import org.esupportail.lecture.exceptions.DepartmentManagerNotFoundException;
import org.esupportail.lecture.exceptions.DepartmentNotFoundException;
import org.esupportail.commons.exceptions.ConfigException;
import org.esupportail.commons.exceptions.UserNotFoundException;
import org.esupportail.commons.services.application.Version;

/**
 * The domain service interface.
 */
public interface DomainService {

	//////////////////////////////////////////////////////////////
	// User
	//////////////////////////////////////////////////////////////

	/**
	 * @param id
	 * @return the User instance that corresponds to an id.
	 * @throws UserNotFoundException
	 */
	User getUser(String id) throws UserNotFoundException;

	/**
	 * @return the list of all the users.
	 */
	List<User> getUsers();

	/**
	 * Update a user.
	 * @param user
	 */
	void updateUser(User user);

	/**
	 * Update a user's information (retrieved from the LDAP directory for instance).
	 * @param user
	 */
	void updateUserInfo(User user);
	
	/**
	 * Add an administrator.
	 * @param user
	 */
	void addAdmin(User user);

	/**
	 * Delete an administrator.
	 * @param user
	 */
	void deleteAdmin(User user);

	/**
	 * @return the list of all the administrators.
	 */
	List<User> getAdmins();

	//////////////////////////////////////////////////////////////
	// Department
	//////////////////////////////////////////////////////////////

	/**
	 * @param id
	 * @return the Department instance that corresponds to an id.
	 * @throws DepartmentNotFoundException 
	 */
	Department getDepartment(long id) throws DepartmentNotFoundException;

	/**
	 * @return the departments.
	 */
	List<Department> getDepartments();

	/**
	 * Add a department.
	 * @param department
	 */
	void addDepartment(Department department);

	/**
	 * Update a department.
	 * @param department
	 */
	void updateDepartment(Department department);

	/**
	 * Delete a department.
	 * @param department
	 */
	void deleteDepartment(Department department);

	/**
	 * @param label
	 * @return 'true' if a department has the same label. 
	 */
	boolean isDepartmentLabelUsed(String label);

	
	//////////////////////////////////////////////////////////////
	// DepartmentManager
	//////////////////////////////////////////////////////////////

	/**
	 * @param department 
	 * @param user 
	 * @return the departmentManager instance that corresponds to a department and a user.
	 * @throws DepartmentManagerNotFoundException 
	 */
	DepartmentManager getDepartmentManager(Department department, User user) 
	throws DepartmentManagerNotFoundException;

	/**
	 * @param department 
	 * @param user 
	 * @return 'true' if user is a manager of the department.
	 */
	boolean isDepartmentManager(Department department, User user);

	/**
	 * @param department
	 * @return the managers of a department.
	 */
	List<DepartmentManager> getDepartmentManagers(Department department);

	/**
	 * Add a department manager.
	 * @param departmentManager
	 */
	void addDepartmentManager(DepartmentManager departmentManager);

	/**
	 * Delete a department manager.
	 * @param departmentManager
	 */
	void deleteDepartmentManager(DepartmentManager departmentManager);

	/**
	 * Update a departmentManager.
	 * @param departmentManager
	 */
	void updateDepartmentManager(DepartmentManager departmentManager);

	/**
	 * @param user 
	 * @return the departments managed by a user.
	 */
	List<Department> getManagedDepartments(User user);

	/**
	 * @param user 
	 * @return 'true' if the user is a manager.
	 */
	boolean isDepartmentManager(User user);
	
	//////////////////////////////////////////////////////////////
	// Thing
	//////////////////////////////////////////////////////////////

	/**
	 * @return the things of a department.
	 * @param department
	 */
	List<Thing> getThings(Department department);

	/**
	 * Add a thing.
	 * @param department 
	 * @param user 
	 * @param date 
	 * @return the newly created thing.
	 */
	Thing addThing(Department department, User user, long date);

	/**
	 * Update a thing.
	 * @param thing
	 * @param user 
	 * @param date 
	 */
	void updateThing(Thing thing, User user, long date);

	/**
	 * Delete a thing.
	 * @param thing
	 */
	void deleteThing(Thing thing);
	
	/**
	 * Move a thing to the first position.
	 * @param thing
	 */
	void moveFirst(Thing thing);

	/**
	 * Move a thing to the last position.
	 * @param thing
	 */
	void moveLast(Thing thing);

	/**
	 * Move a thing up in its department.
	 * @param thing
	 */
	void moveUp(Thing thing);

	/**
	 * Move a thing down in its department.
	 * @param thing
	 */
	void moveDown(Thing thing);

	//////////////////////////////////////////////////////////////
	// VersionManager
	//////////////////////////////////////////////////////////////
	
	/**
	 * @return the database version.
	 * @throws ConfigException when the database is not initialized
	 */
	Version getDatabaseVersion() throws ConfigException;
	
	/**
	 * Set the database version.
	 * @param version 
	 */
	void setDatabaseVersion(Version version);
	
	/**
	 * Set the database version.
	 * @param version 
	 */
	void setDatabaseVersion(String version);
	

	//////////////////////////////////////////////////////////////
	// Authorizations
	//////////////////////////////////////////////////////////////

	/**
	 * @param currentUser
	 * @return 'true' if the user can view administrators.
	 */
	boolean userCanViewAdmins(User currentUser);
	
	/**
	 * @param user 
	 * @return 'true' if the user can grant the privileges of administrator.
	 */
	boolean userCanAddAdmin(User user);

	/**
	 * @param user 
	 * @param admin
	 * @return 'true' if the user can revoke the privileges of an administrator.
	 */
	boolean userCanDeleteAdmin(User user, User admin);

	/**
	 * @param user 
	 * @return 'true' if the user can add a department.
	 */
	boolean userCanAddDepartment(User user);

	/**
	 * @param user 
	 * @param department 
	 * @return 'true' if the user can delete a department.
	 */
	boolean userCanDeleteDepartment(User user, Department department);

	/**
	 * @param user 
	 * @param department 
	 * @return 'true' if the user can edit the properties of a department.
	 */
	boolean userCanEditDepartmentProperties(User user, Department department);

	/**
	 * @param user 
	 * @param department 
	 * @return 'true' if the user can add a manager to a department.
	 */
	boolean userCanAddDepartmentManager(User user, Department department);

	/**
	 * @param user 
	 * @param department 
	 * @return 'true' if the user can edit the managers of a department.
	 */
	boolean userCanEditDepartmentManagers(User user, Department department);

	/**
	 * @param user 
	 * @param departmentManager 
	 * @return 'true' if the user can delete the department manager.
	 */
	boolean userCanDeleteManager(User user, DepartmentManager departmentManager);

	/**
	 * @param user 
	 * @param department 
	 * @return 'true' if the user can view the properties of a department.
	 */
	boolean userCanViewDepartment(User user, Department department);

	/**
	 * @param user
	 * @return The department visible by a user.
	 */
	List<Department> getVisibleDepartments(User user);
	
	/**
	 * @param user 
	 * @param department
	 * @return @return 'true' if the user can add a thing to a department.
	 */
	boolean userCanAddThing(User user, Department department);

	/**
	 * @param user 
	 * @param department
	 * @return @return 'true' if the user can edit the things of a department.
	 */
	boolean userCanEditThings(User user, Department department);

	/**
	 * @param user 
	 * @param department
	 * @return @return 'true' if the user can delete the things of a department.
	 */
	boolean userCanDeleteThings(User user, Department department);

	/**
	 * @param user 
	 * @param department
	 * @return @return 'true' if the user can test the things of a department.
	 */
	boolean userCanViewThings(User user, Department department);

}
