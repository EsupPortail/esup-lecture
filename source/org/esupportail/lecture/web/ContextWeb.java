/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.web;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.esupportail.lecture.domain.model.Context;
import org.esupportail.lecture.domain.model.ManagedCategoryProfile;


/**
 * Not a good design : bean context : classe de tests
 * @author gbouteil
 *
 */
public class ContextWeb {
	
	/**
	 * Name of the context
	 */
	public String name;
	
	/**
	 * Description of the context 
	 */
	public String description;
	
	/**
	 * List of category to display in this context 
	 */
	public List<CategoryWeb> categoryWebs;
	
	/**
	 * ContextWeb constructor.
	 * A contextWeb is made from a context
	 * @param c
	 */
	public ContextWeb(Context c){
		name = c.getName();
		description = c.getDescription();
		categoryWebs = new ArrayList<CategoryWeb>();

		Set<ManagedCategoryProfile> categories = c.getManagedCategoryProfilesSet();
		Iterator iterator = categories.iterator();
		for (ManagedCategoryProfile m = null; iterator.hasNext();){
			m=(ManagedCategoryProfile)iterator.next();
			CategoryWeb cw = new CategoryWeb(m);
			categoryWebs.add(cw);
		}
	}

	/**
	 * Returns the categoryWebs displayed by this context
	 * @return Returns the categoryWebs.
	 */
	public List<CategoryWeb> getCategoryWebs() {
		return categoryWebs;
	}

	/**
	 * Sets the CategoryWeb list to display in this context 
	 * @param categoryWebs The categoryWebs to set.
	 */
	public void setCategoryWebs(List<CategoryWeb> categoryWebs) {
		this.categoryWebs = categoryWebs;
	}

	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
}