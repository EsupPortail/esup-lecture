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
 * DefinitionSets is a the set definition.
 * It can be defined by two ways :
 * 		- an enumeration of groups (groups)
 * 		- a set of regulars defining groups (regulars) *
 * @author gbouteil
 *
 */
public class DefinitionSets {
	
	/*
	 ************************** PROPERTIES ******************************** */	
	/**
	 * Log instance.
	 */
	protected static final Log LOG = LogFactory.getLog(DefinitionSets.class);
	/**
	 * groups : set definition by existent group listing.
	 */
	private List<String> groups = new ArrayList<String>();
	
	/**
	 * regulars : set definition by regulars.
	 */
	private List<RegularOfSet> regulars = new ArrayList<RegularOfSet>();
	
	/* 
	 ************************** INIT **********************************/

	/*
	 *************************** METHODS ******************************** */	
	
	/**
	 * Check existence of group names, attributes names used in group enumeration.
	 * and regulars definition
	 * Not used for the moment : see later
	 * Not ready to use without modification
	 * @deprecated
	 */
	@SuppressWarnings("deprecation")
	@Deprecated
	protected void checkNamesExistence() {
	   	if (LOG.isDebugEnabled()) {
    		LOG.debug("checkNamesExistence()");
    	}
//		Iterator<String> iteratorString;
//		iteratorString = groups.iterator();
//		for(String group = null; iteratorString.hasNext();){
//			group = iteratorString.next();
//			 TODO (GB later) vérification de l'existence du groupe dans le portail
			// si PB : log.warn();
			// PAs sure que c'est par là qu'on le fasse 
//		}
		
		Iterator<RegularOfSet> iteratorReg = regulars.iterator();
		for(RegularOfSet reg = null; iteratorReg.hasNext();) {
			reg = iteratorReg.next();
			reg.checkNamesExistence();
		}
	}
	
	/**
	 * Evaluate current user visibility for this DefinitionSets.
	 * @param ex access to externalService, in order to know user rights
	 * @return true if the user to the set defined by this DefinitionSets
	 */
	protected boolean evaluateVisibility(final ExternalService ex) {
	   	if (LOG.isDebugEnabled()) {
    		LOG.debug("evaluateVisibility(externalService)");
    	}
			
		/* group evaluation */
		Iterator<String> iteratorGroups = groups.iterator();
		while (iteratorGroups.hasNext()) {
			String group = iteratorGroups.next();
			if (LOG.isTraceEnabled()) {
				LOG.trace("DefinitionSets, evaluation on group : " + group);
			}
			try {
				if (ex.isUserInGroup(group)) {
					return true;
				}
			} catch (InternalExternalException e) {
				LOG.error("Group user evaluation impossible (external service unavailable) : "
						+ e.getMessage());
			}
			
		}
		
		/* regulars evaluation */
		Iterator<RegularOfSet> iteratorReg = regulars.iterator();
		while (iteratorReg.hasNext()){
			RegularOfSet reg = iteratorReg.next();
			if (LOG.isTraceEnabled()){
				LOG.trace("DefinionSets, evaluation regular : attr("+ reg.getAttribute() +") val("+ reg.getValue()+")");
			}
			if (reg.evaluate(ex)) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Add a group in groups enumeration of this DefintionSets.
	 * @param group group to add
	 * @see DefinitionSets#groups
	 */
	public synchronized void addGroup(final String group) {
	   	if (LOG.isDebugEnabled()) {
    		LOG.debug("addGroup(" + group + ")");
    	}
		this.groups.add(group);
	}
	
	/**
	 * Add a regulars in list of regulars of this DefintionSets.
	 * @param regular
	 * @see DefinitionSets#regulars
	 */
	public synchronized void addRegular(final RegularOfSet regular) {
	   	if (LOG.isDebugEnabled()) {
    		LOG.debug("addGroup(regular)");
    	}
		this.regulars.add(regular);
	}
	
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		
		String string = "";
		string += "		groups : " + groups.toString() + "\n";
		string += "		regulars : " + regulars.toString() + "\n";
		
		return string;
	}
	
	/* ************************** ACCESSORS ******************************** */	

}
