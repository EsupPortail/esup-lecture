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
import org.esupportail.lecture.domain.service.PortletService;
/**
 * DefinitionSets is composed of the set definition, 
 * by two ways :
 * 		- an enumeration of groups (groups)
 * 		- a set of regulars defining groups (regulars)
 *
 * @author gbouteil
 *
 */
public class DefinitionSets {
/* ************************** PROPERTIES ******************************** */	
	/**
	 * Log instance 
	 */
	protected static final Log log = LogFactory.getLog(DefinitionSets.class);
	/**
	 * groups : set definition by existent group listing
	 */
	private List<String> groups = new ArrayList<String>();
	
	/**
	 * regulars : set definition by regulars 
	 */
	private List<RegularOfSet> regulars = new ArrayList<RegularOfSet>();
	

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
			// PAs sure que c'est par là qu'on le fasse 
		}
		
		iterator = regulars.iterator();
		for(RegularOfSet reg = null; iterator.hasNext();){
			reg = (RegularOfSet)iterator.next();
			reg.checkNamesExistence();
		}
	}
	
	/**
	 * Evaluate current user visibility for this DefinitionSets
	 * @param portletService for portletContainer access, in order to know user rights
	 * @return true if the user is define in this DefinitionSets
	 */
	protected boolean evaluateVisibility(PortletService portletService) {
			
		/* group evaluation */
		Iterator iteratorGroups = groups.iterator();
		while (iteratorGroups.hasNext()){
			String group = (String) iteratorGroups.next();
			log.debug("DefinionSets, group evalue : "+group);
			if (portletService.isUserInRole(group)){
				return true;
			}
		}
		
		/* regulars evaluation */
		Iterator iteratorReg = regulars.iterator();
		while (iteratorReg.hasNext()){
			RegularOfSet reg = (RegularOfSet) iteratorReg.next();
			if (reg.evaluate(portletService)){
				return true;
			}
		}
		
		
		return false;
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
	 * @see DefinitionSets#groups
	 */
	public List<String> getGroups() {
		return groups;
	}
	/**
	 * Sets groups enumeration of this object
	 * @param groups 
	 * @see DefinitionSets#groups
	 */
	protected void setGroups(List<String> groups) {
		this.groups = groups;
	}
	/**
	 * Add a group in groups enumeration
	 * @param group group to add
	 * @see DefinitionSets#groups
	 */
	public void addGroup(String group) {
		this.groups.add(group);
	}
	/**
	 * Returns regulars of this object
	 * @return regulars
	 * @see DefinitionSets#regulars
	 */
	public List<RegularOfSet> getRegulars() {
		return regulars;
	}
	/**
	 * Sets regulars of this object
	 * @param regulars
	 * @see DefinitionSets#regulars
	 */
	protected void setRegulars(List<RegularOfSet> regulars) {
		this.regulars = regulars;
	}
	/**
	 * Add a regulars in list of regulars of this object
	 * @param regular
	 * @see DefinitionSets#regulars
	 */
	public void addRegular(RegularOfSet regular) {
		this.regulars.add(regular);
	}
	
	




	
}
