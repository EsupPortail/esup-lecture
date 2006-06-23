package org.esupportail.lecture.domain.model;


import java.util.Collection;
import java.util.Set;

/**
 * DefAndContentSets is composed of two parts :
 *  - the content of defined set after computing its definition
 * 	- the set definition, two ways :
 * 		- an enumeration of groups (groups)
 * 		- a set of regulars defining groups (regulars)
 *  - 
 * @author gbouteil
 *
 */
public class DefAndContentSets {
/* ************************** PROPERTIES ******************************** */	
	/**
	 * the group : a set of users
	 */
	private Set groups;
	
	/**
	 * a set of regular to define the group (or a part)
	 */
	private Set<RegularOfSet> regulars;
	
	/**
	 * the defined set content 
	 */
	private Set content;

	
/* ************************** ACCESSORS ******************************** */	

	public Set getGroups() {
		return groups;
	}
	public void setGroups(Set groups) {
		this.groups = groups;
	}
	
	public Set<RegularOfSet> getRegulars() {
		return regulars;
	}
	public void setRegulars(Set<RegularOfSet> regulars) {
		this.regulars = regulars;
	}

	public Set getContent() {
		return content;
	}
	public void setContent(Set content) {
		this.content = content;
	}

/* ************************** METHODS ******************************** */	
	
	public String toString(){
		
		String string="";
		string += "		groups : "+ groups.toString()+"\n";
		string += "		regulars : "+ regulars.toString()+"\n";
		//string += "	content : "+ content.toString()+"\n";
		
		return string;
	}
	
	
}
