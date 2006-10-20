/**
 * ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
 * http://sourcesup.cru.fr/projects/esup-lecture
 */
package org.esupportail.lecture.web.beans;

import java.util.List;

import org.esupportail.lecture.domain.beans.Department;
import org.esupportail.lecture.domain.beans.Thing;


/**
 * A class to temporally store the things of a user for a department.
 */
public class PrintableThingSet {

	/**
	 * The department.
	 */
	private Department department;

	/**
	 * The things.
	 */
	private List<Thing> things;

	/**
	 * Constructor.
	 * @param department 
	 * @param things  
	 */
	public PrintableThingSet(
			final Department department,
			final List<Thing> things) {
		super();
		this.department = department;
		this.things = things;
	}

	/**
	 * @return the department
	 */
	public Department getDepartment() {
		return department;
	}

	/**
	 * @return the things
	 */
	public List<Thing> getThings() {
		return things;
	}
	
}
