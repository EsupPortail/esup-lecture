/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;


import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * DefAndContentSets is composed of two parts :
 *  - the content of defined set after computing its definition
 * 	- the set definition, two ways :
 * 		- an enumeration of groups (groups)
 * 		- a set of regulars defining groups (regulars)
 *
 * @author gbouteil
 *
 */
public class DefAndContentSets {
/* ************************** PROPERTIES ******************************** */	
	/**
	 * Log instance 
	 */
	protected static final Log log = LogFactory.getLog(ChannelConfig.class);
	/**
	 * groups : set definition by existent group listing
	 */
	private List<String> groups = new ArrayList<String>();
	
	/**
	 * regulars : set definition by regulars 
	 */
	private List<RegularOfSet> regulars = new ArrayList<RegularOfSet>();
	
	/**
	 * the defined set content : after evaluation of "groups" and "regulars"
	 */
	private Set<String> content = new HashSet<String>();

	

/* ************************** METHODS ******************************** */	
	/**
	 * Check existence of group names, attributes names used in group enumeration
	 * and regulars definition
	 */
	protected void checkNamesExistence(){
	   	if (log.isDebugEnabled()){
    		log.debug("checkNamesExistence()");
    	}
		Iterator iterator;
		iterator = groups.iterator();
		for(String group = null; iterator.hasNext();){
			group = (String)iterator.next();
//			 TODO vérification de l'existence du groupe dans le portail
			// si PB : log.warn();
		}
		
		iterator = regulars.iterator();
		for(RegularOfSet reg = null; iterator.hasNext();){
			reg = (RegularOfSet)iterator.next();
			reg.checkNamesExistence();
		}
	}
	
	
	/**
	 * Returns a string containing this object content :groups, regulars and content sets
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		
		String string="";
		string += "		groups : "+ groups.toString()+"\n";
		string += "		regulars : "+ regulars.toString()+"\n";
		//string += "	content : "+ content.toString()+"\n";
		
		return string;
	}
	
	/* ************************** ACCESSORS ******************************** */	

	/**
	 * Returns groups enumeration of this object
	 * @return groups
	 * @see DefAndContentSets#groups
	 */
	protected List<String> getGroups() {
		return groups;
	}
	/**
	 * Sets groups enumeration of this object
	 * @param groups 
	 * @see DefAndContentSets#groups
	 */
	protected void setGroups(List<String> groups) {
		this.groups = groups;
	}
	/**
	 * Add a group in groups enumeration
	 * @param group group to add
	 * @see DefAndContentSets#groups
	 */
	protected void addGroup(String group) {
		this.groups.add(group);
	}
	/**
	 * Returns regulars of this object
	 * @return regulars
	 * @see DefAndContentSets#regulars
	 */
	protected List<RegularOfSet> getRegulars() {
		return regulars;
	}
	/**
	 * Sets regulars of this object
	 * @param regulars
	 * @see DefAndContentSets#regulars
	 */
	protected void setRegulars(List<RegularOfSet> regulars) {
		this.regulars = regulars;
	}
	/**
	 * Add a regulars in list of regulars of this object
	 * @param regular
	 * @see DefAndContentSets#regulars
	 */
	protected void addRegular(RegularOfSet regular) {
		this.regulars.add(regular);
	}
	
	/**
	 * Returns set content of this object
	 * (No setter for this attribute : it is evaluated from "groups" and "regulars"
	 * @return content
	 * @see DefAndContentSets#content
	 */
	protected Set getContent() {
		return content;
	}

	
}
