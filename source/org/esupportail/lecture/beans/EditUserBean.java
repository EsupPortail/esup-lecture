package org.esupportail.lecture.beans;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.model.Context;
import org.esupportail.lecture.domain.model.CustomContext;
import org.esupportail.lecture.domain.model.UserProfile;
/**
 * Bean to display a context according to a user profile
 * @author gbouteil
 *
 */
public class EditUserBean {
	/*
	 ************************ PROPERTIES ******************************** */	

	/**
	 * Log instance
	 */
	protected static final Log log = LogFactory.getLog(EditUserBean.class);

	/* name of the context */
	private String contextName;
	
	
	private EditCategoryBean selectedCategory;
	
	
	private Set<String> categories;
	

	public EditUserBean(){
		categories = new HashSet<String>();
	}
	



	public void addCategory(String name) {
		categories.add(name);
		
	}







	/**
	 * @return Returns the categories.
	 */
	public Set<String> getCategories() {
		return categories;
	}







	/**
	 * @param categories The categories to set.
	 */
	public void setCategories(Set<String> categories) {
		this.categories = categories;
	}







	/**
	 * @return Returns the name.
	 */
	public String getContextName() {
		return contextName;
	}







	/**
	 * @param name The name to set.
	 */
	public void setContextName(String name) {
		this.contextName = name;
	}




	/**
	 * @return Returns the selectedCategory.
	 */
	public EditCategoryBean getSelectedCategory() {
		return selectedCategory;
	}




	/**
	 * @param selectedCategory The selectedCategory to set.
	 */
	public void setSelectedCategory(EditCategoryBean selectedCategory) {
		this.selectedCategory = selectedCategory;
	}

	
}
