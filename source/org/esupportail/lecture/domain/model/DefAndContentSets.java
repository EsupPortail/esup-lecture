package org.esupportail.lecture.domain.model;


import java.util.Collection;
import java.util.Set;

/**
 * DefAndContentSets is composed of two parts :
 *  - a set of users that belongs to the group after computing its definition
 * 	- the group definition, two ways :
 * 		- an enumeration of groups
 * 		- a set of regulars defining groups
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
	 * a set of remote groups to define the group (or a part)
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

}
