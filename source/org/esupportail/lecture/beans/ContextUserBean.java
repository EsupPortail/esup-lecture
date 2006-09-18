package org.esupportail.lecture.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.service.FacadeService;

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
	 * Access to services
	 */
	private FacadeService facadeService;
	
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
	 * @return Returns the facadeService.
	 */
	public FacadeService getFacadeService() {
		return facadeService;
	}

	/**
	 * @param facadeService The facadeService to set.
	 */
	public void setFacadeService(FacadeService facadeService) {
		this.facadeService = facadeService;
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
