package org.esupportail.lecture.domain.beans;

/**
 * @author bourges
 * used to store context informations
 */
public class ContextBean {
	/**
	 * id of context
	 */
	private String id;
	/**
	 * name of context
	 */
	private String name;
	/**
	 * get the id of the context
	 * @return id of context
	 */
	public String getId() {
		return id;
	}
	/**
	 * set the id of the context
	 * @param id id of context
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * get the name of the context
	 * @return name of context
	 */
	public String getName() {
		return name;
	}
	/** 
	 * set the name of the context
	 * @param name name of the context
	 */
	public void setName(String name) {
		this.name = name;
	}

}
