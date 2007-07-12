/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.beans;

import org.esupportail.lecture.domain.model.Context;
import org.esupportail.lecture.domain.model.CustomContext;
import org.esupportail.lecture.exceptions.domain.ContextNotFoundException;

/**
 * used to store context informations
 * @author bourges
 */
public class ContextBean {

	/* 
	 *************************** PROPERTIES ******************************** */	
	/**
	 * id of context
	 */
	private String id;
	/**
	 * name of context
	 */
	private String name;
	/**
	 * description of the context
	 */
	private String description;
	
	/**
	 * size of tree window 
	 */
	private int treeSize;

	/*
	 *************************** INIT ************************************** */	

	/**
	 * default contructor
	 */
	public ContextBean(){
		// empty
	}
	
	/**
	 * Constructor initializing object
	 * @param customContext
	 * @throws ContextNotFoundException 
	 */
	public ContextBean(CustomContext customContext) throws ContextNotFoundException{
		Context context = customContext.getContext();

		setTreeSize(customContext.getTreeSize());
		
		setName(context.getName());
		setDescription(context.getDescription());
		setId(context.getId());
		
	}
	
	/*
	 *************************** ACCESSORS ********************************* */	
	
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

	/**
	 * @return description of context
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description description of the context
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * @return tree size of context
	 */
	public int getTreeSize() {
		return treeSize;
	}

	/**
	 * @param treeSize tree size to set
	 */
	public void setTreeSize(int treeSize) {
		this.treeSize = treeSize;
	}
	
	/*
	 *************************** METHODS *********************************** */	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString(){
		String string = "";
		string += " Id = " + id.toString() + "\n";
		string += " Name = " + name.toString() + "\n";
		string += " Desc = " + description.toString() + "\n";
		string += " TreeSize = " + treeSize +  "\n";
		return string;
	}
	
}
