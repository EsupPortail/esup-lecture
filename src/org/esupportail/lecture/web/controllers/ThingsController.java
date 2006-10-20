/**
 * ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
 * http://sourcesup.cru.fr/projects/esup-lecture
 */
package org.esupportail.lecture.web.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.esupportail.lecture.domain.beans.Department;
import org.esupportail.lecture.domain.beans.Thing;
import org.esupportail.lecture.domain.beans.User;
import org.esupportail.commons.utils.ExternalContextUtils;

/**
 * A bean to manage things.
 */
public class ThingsController extends AbstractContextAwareController {

	/**
	 * The thing to update.
	 */
	private Thing thingToUpdate;

	/**
	 * Constructor.
	 *
	 */
	public ThingsController() {
		super();
	}

	/**
	 * @see org.esupportail.commons.web.controllers.Resettable#reset()
	 */
	public void reset() {
		thingToUpdate = null;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getClass().getSimpleName() + "#" + hashCode() + "[thingToUpdate=" + thingToUpdate + "]";
	}

	/**
	 * @return true if the current user is allowed to view the page.
	 */
	public boolean isPageAuthorized() {
		User currentUser = getCurrentUser();
		if (currentUser == null) {
			return false;
		}
		if (currentUser.getAdmin()) {
			return true;
		}
		return getDomainService().isDepartmentManager(currentUser);
	}

	/**
	 * @return The departments managed by the current user, as a list of SelectItem.
	 */
	public List<SelectItem> getDepartmentItems() {
		List<SelectItem> departmentItems = new ArrayList<SelectItem>();
		departmentItems.clear();
		departmentItems.add(new SelectItem("", getString("THINGS.TEXT.DEPARTMENT_SELECTION.DEFAULT")));
		List<Department> departments = getDomainService().getManagedDepartments(getCurrentUser());
		for (Department department : departments) {
			departmentItems.add(new SelectItem(department, department.getLabel()));
		}
		return departmentItems;
	}
	
	/**
	 * @return the things cached.
	 */
	@SuppressWarnings("unchecked")
	private List<Thing> getCachedThings() {
		return (List<Thing>) ExternalContextUtils.getRequestVar("things");
	}

	/**
	 * Cache things.
	 * @param things 
	 */
	private void cacheThings(List<Thing> things) {
		ExternalContextUtils.setRequestVar("things", things);
	}

	/**
	 * Uncache things.
	 */
	private void uncacheThings() {
		cacheThings(null);
	}

	/**
	 * @return the things of the current department.
	 */
	public List<Thing> getThings() {
		if (getCachedThings() == null) {
			cacheThings(getDomainService().getThings(getSessionController().getDepartment()));
		}
		return getCachedThings();
	}

	/**
	 * @return the number of things of the current department.
	 */
	public int getThingsNumber() {
		return getThings().size();
	}

	/**
	 * @return 'true' if the current user can add things.
	 */
	public boolean isCurrentUserCanAddThing() {
		Department currentDepartment = getSessionController().getDepartment();
		if (currentDepartment == null) {
			return false;
		}
		return getDomainService().userCanAddThing(getCurrentUser(), currentDepartment);
	}

	/**
	 * @return 'true' if the current user can view things.
	 */
	public boolean isCurrentUserCanViewThings() {
		return getDomainService().userCanViewThings(getCurrentUser(), getSessionController().getDepartment());
	}

	/**
	 * @return 'true' if the current user can delete things.
	 */
	public boolean isCurrentUserCanDeleteThings() {
		return getDomainService().userCanDeleteThings(getCurrentUser(), getSessionController().getDepartment());
	}

	/**
	 * @return 'true' if the current user can edit things.
	 */
	public boolean isCurrentUserCanEditThings() {
		return getDomainService().userCanEditThings(getCurrentUser(), getSessionController().getDepartment());
	}

	/**
	 * @return a String.
	 */
	public String addThing() {
		Department currentDepartment = getSessionController().getDepartment();
		if (currentDepartment == null) {
			addUnauthorizedActionMessage();
			return null;
		}
		if (!isCurrentUserCanAddThing()) {
			addUnauthorizedActionMessage();
			return null;
		}
		Thing thing = getDomainService().addThing(
				currentDepartment, getCurrentUser(), System.currentTimeMillis());
		setThingToUpdate(thing);
		addInfoMessage(null, "THINGS.MESSAGE.THING_ADDED");
		return "thingAdded";
	}

	/**
	 * @return a String.
	 */
	public String updateThing() {
		if (!isCurrentUserCanEditThings()) {
			addUnauthorizedActionMessage();
			return null;
		}
		getDomainService().updateThing(thingToUpdate, getCurrentUser(), System.currentTimeMillis());
		addInfoMessage(null, "THINGS.MESSAGE.THING_UPDATED");
		return "thingUpdated";
	}

	/**
	 * @return a String.
	 */
	public String confimDeleteThing() {
		if (!isCurrentUserCanDeleteThings()) {
			addUnauthorizedActionMessage();
			return null;
		}
		getDomainService().deleteThing(thingToUpdate);
		addInfoMessage(null, "THINGS.MESSAGE.THING_DELETED");
		return "thingDeleted";
	}

	/**
	 * @return a String.
	 */
	public String moveFirst() {
		getDomainService().moveFirst(thingToUpdate);
		uncacheThings();
		return null;
	}
	
	/**
	 * @return a String.
	 */
	public String moveLast() {
		getDomainService().moveLast(thingToUpdate);
		uncacheThings();
		return null;
	}
	
	/**
	 * @return a String.
	 */
	public String moveUp() {
		getDomainService().moveUp(thingToUpdate);
		uncacheThings();
		return null;
	}
	
	/**
	 * @return a String.
	 */
	public String moveDown() {
		getDomainService().moveDown(thingToUpdate);
		uncacheThings();
		return null;
	}
	
	/**
	 * @return the thingToUpdate
	 */
	public Thing getThingToUpdate() {
		return this.thingToUpdate;
	}

	/**
	 * @param thingToUpdate the thingToUpdate to set
	 */
	public void setThingToUpdate(final Thing thingToUpdate) {
		this.thingToUpdate = new Thing(thingToUpdate);
	}

}
