/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;


import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.ExternalService;
import org.esupportail.lecture.exceptions.domain.InternalExternalException;
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
	
	/*
	 ************************** PROPERTIES ******************************** */	
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
	

	/*
	 *************************** METHODS ******************************** */	
	
	/**
	 * Check existence of group names, attributes names used in group enumeration
	 * and regulars definition
	 * Not used for the moment : see later
	 * Not ready to use without modification
	 */
	synchronized protected void checkNamesExistence(){
	   	if (log.isDebugEnabled()){
    		log.debug("checkNamesExistence()");
    	}
		Iterator<String> iteratorString;
		iteratorString = groups.iterator();
		for(String group = null; iteratorString.hasNext();){
			group = iteratorString.next();
//			 TODO (GB later) vérification de l'existence du groupe dans le portail
			// si PB : log.warn();
			// PAs sure que c'est par là qu'on le fasse 
		}
		
		Iterator<RegularOfSet> iteratorReg = regulars.iterator();
		for(RegularOfSet reg = null; iteratorReg.hasNext();){
			reg = iteratorReg.next();
			reg.checkNamesExistence();
		}
	}
	
	/**
	 * Evaluate current user visibility for this DefinitionSets
	 * @param portletService for portletContainer access, in order to know user rights
	 * @return true if the user is define in this DefinitionSets
	 */
	synchronized protected boolean evaluateVisibility(ExternalService ex) {
	   	if (log.isDebugEnabled()){
    		log.debug("evaluateVisibility(externalService)");
    	}
			
		/* group evaluation */
		Iterator<String> iteratorGroups = groups.iterator();
		while (iteratorGroups.hasNext()){
			String group = iteratorGroups.next();
			if (log.isTraceEnabled()){
				log.trace("DefinitionSets, evaluation on group : "+group);
			}
			try {
				if (ex.isUserInGroup(group)){
					return true;
				}
			} catch (InternalExternalException e) {
				log.error("Group user evaluation impossible (external service unavailable) : "+e.getMessage());
			}
			
		}
		
		/* regulars evaluation */
		Iterator<RegularOfSet> iteratorReg = regulars.iterator();
		while (iteratorReg.hasNext()){
			RegularOfSet reg = iteratorReg.next();
			if (log.isTraceEnabled()){
				log.trace("DefinionSets, evaluation regular : attr("+ reg.getAttribute() +") val("+ reg.getValue()+")");
			}
			if (reg.evaluate(ex)){
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Add a group in groups enumeration
	 * @param group group to add
	 * @see DefinitionSets#groups
	 */
	synchronized public void addGroup(String group) {
	   	if (log.isDebugEnabled()){
    		log.debug("addGroup("+group+")");
    	}
		this.groups.add(group);
	}
	
	/**
	 * Add a regulars in list of regulars of this object
	 * @param regular
	 * @see DefinitionSets#regulars
	 */
	synchronized public void addRegular(RegularOfSet regular) {
	   	if (log.isDebugEnabled()){
    		log.debug("addGroup(regular)");
    	}
		this.regulars.add(regular);
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
	synchronized protected void setGroups(List<String> groups) {
		this.groups = groups;
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
	synchronized protected void setRegulars(List<RegularOfSet> regulars) {
		this.regulars = regulars;
	}

	




	
}
