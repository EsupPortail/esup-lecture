package org.esupportail.lecture.domain.model;


import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
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
	 * groups : definition by existent group listing
	 */
	private List<String> groups = new ArrayList<String>();
	
	/**
	 * regulars : defintion by regulars to define groups
	 */
	private List<RegularOfSet> regulars = new ArrayList<RegularOfSet>();
	
	/**
	 * the defined set content : after evaluation of "goups" and "regulars"
	 */
	private Set<String> content = new HashSet<String>();

	
/* ************************** ACCESSORS ******************************** */	

	public List<String> getGroups() {
		return groups;
	}
	public void setGroups(List<String> groups) {
		this.groups = groups;
	}
	public void addGroup(String group) {
		this.groups.add(group);
	}
	public List<RegularOfSet> getRegulars() {
		return regulars;
	}
	public void setRegulars(List<RegularOfSet> regulars) {
		this.regulars = regulars;
	}
	public void addRegular(RegularOfSet regular) {
		this.regulars.add(regular);
	}
	
	public Set getContent() {
		return content;
	}
	public void setContent(Set<String> content) {
		this.content = content;
	}
	public void addContent(String aContent){
		this.content.add(aContent);
	}
/* ************************** METHODS ******************************** */	
	
	public void checkNamesExistence(){
		
		Iterator iterator;
		iterator = groups.iterator();
		for(String group = null; iterator.hasNext();){
			group = (String)iterator.next();
//			 TODO vérification de l'existence du groupe dans le portail
		}
		
		iterator = regulars.iterator();
		for(RegularOfSet reg = null; iterator.hasNext();){
			reg = (RegularOfSet)iterator.next();
			reg.checkNamesExistence();
		}
		
		
	
	}
	
	
	public String toString(){
		
		String string="";
		string += "		groups : "+ groups.toString()+"\n";
		string += "		regulars : "+ regulars.toString()+"\n";
		//string += "	content : "+ content.toString()+"\n";
		
		return string;
	}
	
	
}
