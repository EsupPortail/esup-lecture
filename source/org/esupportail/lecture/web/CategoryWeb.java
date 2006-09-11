/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.web;


import org.esupportail.lecture.domain.model.ManagedCategoryProfile;

/**
 * A supprimer ultérieurement
 * Not good  design : classe de tests
 * @author gbouteil
 *
 */
public class CategoryWeb {
	public String name;
	
	public CategoryWeb(ManagedCategoryProfile c){
		name = c.getName();
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