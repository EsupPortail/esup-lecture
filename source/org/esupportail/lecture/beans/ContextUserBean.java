package org.esupportail.lecture.beans;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.model.Context;
import org.esupportail.lecture.domain.model.CustomContext;
/**
 * Bean to display a context according to a user profile
 * @author gbouteil
 *
 */
public class ContextUserBean {
	/*
	 ************************ PROPERTIES ******************************** */	

	/**
	 * Log instance
	 */
	protected static final Log log = LogFactory.getLog(ContextUserBean.class);

	
	/**
	 * Name of the context
	 */
	private String name;
	
	/**
	 * Description of the context
	 */
	private String description;
	
	/**
	 * Id of the context
	 */
	private String id;
	
	/**
	 * Used for tests
	 */
	private String test;
	
	/**
	 * Categories to display
	 */
	private List<CategoryUserBean> categories;
	
	/*
	 ************************ INITIALIZATION ******************************** */	

	/**
	 * Constructors
	 */
	public ContextUserBean(){
		categories = new ArrayList<CategoryUserBean>();
	}
	
	/**
	 * Init with the customContext to display
	 * @param customContext
	 */
	public void init(CustomContext customContext) {
		Context context = customContext.getContext();

		setName(context.getName());
		setDescription(context.getDescription());
		setId(context.getId());
		setTest(test);
	}
	
	
	/*
	 ************************ METHODS ******************************** */	

	/**
	 * Add a categoryUserBean to the list in this contextUserBean
	 * @param cat the categoryuserBean to add
	 */
	public void addCategoryUserBean(CategoryUserBean cat){
		log.debug("addCategoryUserBean : "+cat.getName());
		categories.add(cat);
	}

	/*
	 ************************ ACCESSORS ******************************** */	

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

	/**
	 * @param test The "test" to set.
	 */
	public void setTest(String test) {
		this.test=test;
		
	}
	/**
	 * @return Returns the test.
	 */
	public String getTest() {
		return test;
	}

	/**
	 * @return Returns the id.
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return Returns the categories.
	 */
	public List<CategoryUserBean> getCategories() {
		return categories;
	}

	
}
