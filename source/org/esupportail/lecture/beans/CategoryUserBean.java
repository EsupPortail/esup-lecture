package org.esupportail.lecture.beans;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.esupportail.lecture.domain.model.Category;
import org.esupportail.lecture.domain.model.CategoryProfile;
import org.esupportail.lecture.domain.model.Context;
import org.esupportail.lecture.domain.model.CustomCategory;
import org.esupportail.lecture.domain.model.CustomSource;
import org.esupportail.lecture.domain.model.SourceProfile;

/**
 * Bean to display a category according to a user profile
 * @author gbouteil
 *
 */
public class CategoryUserBean {
	
	/*
	 ************************ PROPERTIES ******************************** */	

	/**
	 * Name of the category
	 */
	private String name;
	
	/**
	 *  Description of the category
	 */
	private String description;

	private List<String> sourceNames;
	/*
	 ************************ INIT ******************************** */	
	
	
	
	public void init(CustomCategory customCategory) {
		
		CategoryProfile profile = customCategory.getCategoryProfile();
		//TODO a voir quel name on met (cat ou profileCat)
		setName(profile.getName());
		setDescription(profile.getDescription());
		
		sourceNames = new Vector<String>();
		Iterator iterator = customCategory.getSortedCustomSources().iterator();
		while(iterator.hasNext()){
			CustomSource customSource = (CustomSource)iterator.next();
			SourceProfile sourceProfile = customSource.getSourceProfile();
			sourceNames.add(sourceProfile.getName());
			
		}
	}
	
	/*
	 ************************ ACCESSORS ******************************** */	

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
	 * @return Returns the sourceNames.
	 */
	public List<String> getSourceNames() {
		return sourceNames;
	}



	
	
	
}
