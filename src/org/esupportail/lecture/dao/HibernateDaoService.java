/**
 * ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
 * http://sourcesup.cru.fr/projects/esup-lecture
 */
package org.esupportail.lecture.dao;

import java.util.List;

import org.esupportail.lecture.domain.beans.Department;
import org.esupportail.lecture.domain.beans.DepartmentManager;
import org.esupportail.lecture.domain.beans.Thing;
import org.esupportail.lecture.domain.beans.User;
import org.esupportail.lecture.domain.beans.VersionManager;
import org.esupportail.commons.dao.AbstractHibernateDaoService;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 * The Hiberate implementation of the DAO service.
 * 
 * See /properties/dao/dao-example.xml
 */
public class HibernateDaoService extends AbstractHibernateDaoService implements DaoService {

	/**
	 * The name of the 'id' attribute.
	 */
	private static final String ID_ATTRIBUTE = "id";

	/**
	 * The name of the 'order' attribute.
	 */
	private static final String ORDER_ATTRIBUTE = "order";

	/**
	 * The name of the 'user' attribute.
	 */
	private static final String USER_ATTRIBUTE = "user";

	/**
	 * The name of the 'admin' attribute.
	 */
	private static final String ADMIN_ATTRIBUTE = "admin";

	/**
	 * The name of the 'department' attribute.
	 */
	private static final String DEPARTMENT_ATTRIBUTE = "department";

	/**
	 * The name of the 'label' attribute.
	 */
	private static final String LABEL_ATTRIBUTE = "label";

	/**
	 * Bean constructor.
	 */
	public HibernateDaoService() {
		super();
	}

	//////////////////////////////////////////////////////////////
	// User
	//////////////////////////////////////////////////////////////

	/**
	 * @see org.esupportail.lecture.dao.DaoService#getUser(java.lang.String)
	 */
	public User getUser(final String id) {
		return (User) getHibernateTemplate().get(User.class, id);
	}

	/**
	 * @see org.esupportail.lecture.dao.DaoService#getUsers()
	 */
	@SuppressWarnings("unchecked")
	public List<User> getUsers() {
		return getHibernateTemplate().loadAll(User.class);
	}

	/**
	 * @see org.esupportail.lecture.dao.DaoService#addUser(org.esupportail.lecture.domain.beans.User)
	 */
	public void addUser(final User user) {
		addObject(user);
	}

	/**
	 * @see org.esupportail.lecture.dao.DaoService#deleteUser(org.esupportail.lecture.domain.beans.User)
	 */
	public void deleteUser(final User user) {
		deleteObject(user);
	}

	/**
	 * @see org.esupportail.lecture.dao.DaoService#updateUser(org.esupportail.lecture.domain.beans.User)
	 */
	public void updateUser(final User user) {
		updateObject(user);
	}

	/**
	 * @see org.esupportail.lecture.dao.DaoService#getAdmins()
	 */
	@SuppressWarnings("unchecked")
	public List<User> getAdmins() {
		DetachedCriteria criteria = DetachedCriteria.forClass(User.class);
		criteria.addOrder(Order.asc(ID_ATTRIBUTE));
		criteria.add(Restrictions.eq(ADMIN_ATTRIBUTE, Boolean.TRUE));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	//////////////////////////////////////////////////////////////
	// Department
	//////////////////////////////////////////////////////////////

	/**
	 * @see org.esupportail.lecture.dao.DaoService#getDepartment(long)
	 */
	public Department getDepartment(final long id) {
		return (Department) this.getHibernateTemplate().get(Department.class, id);
	}

	/**
	 * @see org.esupportail.lecture.dao.DaoService#getDepartments()
	 */
	@SuppressWarnings("unchecked")
	public List<Department> getDepartments() {
		DetachedCriteria criteria = DetachedCriteria.forClass(Department.class);
		criteria.addOrder(Order.asc(ID_ATTRIBUTE));
		return  getHibernateTemplate().findByCriteria(criteria);
	}

	/**
	 * @see org.esupportail.lecture.dao.DaoService#addDepartment(org.esupportail.lecture.domain.beans.Department)
	 */
	public void addDepartment(final Department department) {
		addObject(department);
	}

	/**
	 * @see org.esupportail.lecture.dao.DaoService#updateDepartment(org.esupportail.lecture.domain.beans.Department)
	 */
	public void updateDepartment(final Department department) {
		updateObject(department);
	}

	/**
	 * @see org.esupportail.lecture.dao.DaoService#deleteDepartment(org.esupportail.lecture.domain.beans.Department)
	 */
	public void deleteDepartment(final Department department) {
		deleteObjects(getThings(department));
		deleteObjects(getDepartmentManagers(department));
		deleteObject(department);
	}

	/**
	 * @see org.esupportail.lecture.dao.DaoService#isDepartmentLabelUsed(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public boolean isDepartmentLabelUsed(final String label) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Department.class);
		criteria.add(Restrictions.eq(LABEL_ATTRIBUTE, label));
		List<Department> result = getHibernateTemplate().findByCriteria(criteria);
		return result.size() > 0;
	}

	/**
	 * @see org.esupportail.lecture.dao.DaoService#getThings(org.esupportail.lecture.domain.beans.Department)
	 */
	@SuppressWarnings("unchecked")
	public List<Thing> getThings(
			final Department department) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Thing.class);
		criteria.addOrder(Order.asc(ORDER_ATTRIBUTE));
		criteria.add(Restrictions.eq(DEPARTMENT_ATTRIBUTE, department));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/**
	 * @see org.esupportail.lecture.dao.DaoService#getThingsNumber(org.esupportail.lecture.domain.beans.Department)
	 */
	public int getThingsNumber(final Department department) {
		return getQueryIntResult(
				"select count(*) from Thing where department.id = " + department.getId());
	}

	/**
	 * @see org.esupportail.lecture.dao.DaoService#getThingByOrder(org.esupportail.lecture.domain.beans.Department, int)
	 */
	@SuppressWarnings("unchecked")
	public Thing getThingByOrder(Department department, int order) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Thing.class);
		criteria.add(Restrictions.eq(DEPARTMENT_ATTRIBUTE, department));
		criteria.add(Restrictions.eq(ORDER_ATTRIBUTE, new Integer(order)));
		List<Thing> things = getHibernateTemplate().findByCriteria(criteria);  
		if (things.isEmpty()) {
			return null;
		}
		return things.get(0);
	}

	//////////////////////////////////////////////////////////////
	// DepartmentManager
	//////////////////////////////////////////////////////////////

	/**
	 * @see org.esupportail.lecture.dao.DaoService#getDepartmentManager(
	 * org.esupportail.lecture.domain.beans.Department, org.esupportail.lecture.domain.beans.User)
	 */
	@SuppressWarnings("unchecked")
	public DepartmentManager getDepartmentManager(
			final Department department, 
			final User user) {
		DetachedCriteria criteria = DetachedCriteria.forClass(DepartmentManager.class);
		criteria.add(Restrictions.eq(DEPARTMENT_ATTRIBUTE, department));
		criteria.add(Restrictions.eq(USER_ATTRIBUTE, user));
		List<DepartmentManager> managers = getHibernateTemplate().findByCriteria(criteria);
		if (managers.isEmpty()) {
			return null;
		}
		return managers.get(0);
	}

	/**
	 * @see org.esupportail.lecture.dao.DaoService#getDepartmentManagers(org.esupportail.lecture.domain.beans.Department)
	 */
	@SuppressWarnings("unchecked")
	public List<DepartmentManager> getDepartmentManagers(
			final Department department) {
		DetachedCriteria criteria = DetachedCriteria.forClass(DepartmentManager.class);
		criteria.addOrder(Order.asc(ID_ATTRIBUTE));
		criteria.add(Restrictions.eq(DEPARTMENT_ATTRIBUTE, department));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/**
	 * @see org.esupportail.lecture.dao.DaoService#addDepartmentManager(
	 * org.esupportail.lecture.domain.beans.DepartmentManager)
	 */
	public void addDepartmentManager(
			final DepartmentManager departmentManager) {
		addObject(departmentManager);
	}

	/**
	 * @see org.esupportail.lecture.dao.DaoService#updateDepartmentManager(
	 * org.esupportail.lecture.domain.beans.DepartmentManager)
	 */
	public void updateDepartmentManager(
			final DepartmentManager departmentManager) {
		updateObject(departmentManager);
	}

	/**
	 * @see org.esupportail.lecture.dao.DaoService#deleteDepartmentManager(
	 * org.esupportail.lecture.domain.beans.DepartmentManager)
	 */
	public void deleteDepartmentManager(
			final DepartmentManager departmentManager) {
		deleteObject(departmentManager);
	}

	/**
	 * @see org.esupportail.lecture.dao.DaoService#getDepartmentManagers(org.esupportail.lecture.domain.beans.User)
	 */
	@SuppressWarnings("unchecked")
	public List<DepartmentManager> getDepartmentManagers(final User user) {
		DetachedCriteria criteria = DetachedCriteria.forClass(DepartmentManager.class);
		criteria.addOrder(Order.asc(ID_ATTRIBUTE));
		criteria.add(Restrictions.eq(USER_ATTRIBUTE, user));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	//////////////////////////////////////////////////////////////
	// Thing
	//////////////////////////////////////////////////////////////

	/**
	 * @see org.esupportail.lecture.dao.DaoService#addThing(org.esupportail.lecture.domain.beans.Thing)
	 */
	public void addThing(final Thing thing) {
		addObject(thing);
	}

	/**
	 * @see org.esupportail.lecture.dao.DaoService#updateThing(org.esupportail.lecture.domain.beans.Thing)
	 */
	public void updateThing(final Thing thing) {
		updateObject(thing);
	}

	/**
	 * @see org.esupportail.lecture.dao.DaoService#deleteThing(org.esupportail.lecture.domain.beans.Thing)
	 */
	public void deleteThing(final Thing thing) {
		deleteObject(thing);
	}

	//////////////////////////////////////////////////////////////
	// VersionManager
	//////////////////////////////////////////////////////////////

	/**
	 * @see org.esupportail.lecture.dao.DaoService#getVersionManagers()
	 */
	@SuppressWarnings("unchecked")
	public List<VersionManager> getVersionManagers() {
		DetachedCriteria criteria = DetachedCriteria.forClass(VersionManager.class);
		criteria.addOrder(Order.asc(ID_ATTRIBUTE));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/**
	 * @see org.esupportail.lecture.dao.DaoService#addVersionManager(org.esupportail.lecture.domain.beans.VersionManager)
	 */
	public void addVersionManager(final VersionManager versionManager) {
		addObject(versionManager);
	}

	/**
	 * @see org.esupportail.lecture.dao.DaoService#updateVersionManager(org.esupportail.lecture.domain.beans.VersionManager)
	 */
	public void updateVersionManager(final VersionManager versionManager) {
		updateObject(versionManager);
	}

	//////////////////////////////////////////////////////////////
	// misc
	//////////////////////////////////////////////////////////////

}
