package org.esupportail.lecture.beans;

import java.util.List;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.model.CategoryProfile;
import org.esupportail.lecture.domain.model.CustomCategory;

/**
 * Bean to display a category according to a user profile
 * @author gbouteil
 *
 */
public class CategoryUserBean {
	
	/*
	 ************************ PROPERTIES ******************************** */
	
	/**
	 * Log instance
	 */
	protected static final Log log = LogFactory.getLog(ContextUserBean.class);


	/**
	 * Name of the category
	 */
	private String name;
	
	/**
	 *  Description of the category
	 */
	private String description;

	/**
	 * Sources to display
	 */
	private List<SourceUserBean> sources;
	
	/*
	 ************************ INIT ******************************** */	
	public CategoryUserBean() {
		sources = new Vector<SourceUserBean>();
	}
	
	/**
	 * Init with the customCategory to display
	 * @param customCategory
	 */
	public void init(CustomCategory customCategory) {
		
		CategoryProfile profile = customCategory.getCategoryProfile();
		//TODO a voir quel name on met (cat ou profileCat)
		setName(profile.getName());
		setDescription(profile.getDescription());
	}
	
	public void addSourceUserBean(SourceUserBean sourceUserBean) {
		log.debug("addSourceUserBean : "+ sourceUserBean.getName());
		sources.add(sourceUserBean);
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
	 * @return Returns the sources.
	 */
	public List<SourceUserBean> getSources() {
		return sources;
	}

	/**
	 * @param sources The sources to set.
	 */
	public void setSources(List<SourceUserBean> sources) {
		this.sources = sources;
	}

	



	
	
}
