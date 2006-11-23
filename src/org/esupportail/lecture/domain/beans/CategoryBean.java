package org.esupportail.lecture.domain.beans;

import org.esupportail.lecture.domain.model.CategoryProfile;
import org.esupportail.lecture.domain.model.CustomCategory;

/**
 * @author bourges
 * used to store category informations
 */
public class CategoryBean {
	/**
	 * id of categery
	 */
	private String id;
	/**
	 * name of category
	 */
	private String name;
	/**
	 * description of the category
	 */
	private String description;
	/**
	 * store if category is folded or not
	 */
	private boolean folded;
	
	/**
	 * Constructor
	 */
	public CategoryBean(){}
	
	public CategoryBean(CustomCategory customCategory){
		CategoryProfile profile = customCategory.getProfile();
		
		setName(profile.getName());
		setDescription(profile.getDescription());
		setId(profile.getId());
	}
	
	/**
	 * @return description of the category
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description description of the category
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return if category is folded or not
	 */
	public boolean isFolded() {
		return folded;
	}
	/**
	 * @param folded
	 */
	public void setFolded(boolean folded) {
		this.folded = folded;
	}
	/**
	 * @return id of category
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return name of category
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString(){
		String string = "";
		string += "     Id = " + id.toString() + "\n";
		string += "     Name = " + name.toString() + "\n";
		string += "     Desc = "; 
		if (description != null){
			string += description.toString() + "\n";
		}
		
		return string;
	}
	
}
