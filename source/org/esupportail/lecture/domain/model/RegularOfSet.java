package org.esupportail.lecture.domain.model;

/**
 * Regular definition of a group : 
 * Every user that "attribute x" has "value Y"
 * @author gbouteil
 *
 */
public class RegularOfSet {
/* ************************** PROPERTIES ******************************** */	

	/**
	 * user attribute
	 */
	private String attribute = "";
	
	/**
	 * Value required by the attribute to be in the group that is defined
	 */
	private String value = "";

/* ************************** ACCESSORS ******************************** */	

	public String getAttribute() {
		return attribute;
	}
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
/* ************************** METHODS ******************************** */	
	
	public void checkNamesExistence(){
		// TODO vérification de l'existence de l'attribut dans le portail ?
		// TODO vérifiaction de l'existance de la valeur dans le portail ?
	}
	
	
	
	public String toString(){
		
		String string ="";
		string += "attribute : "+ attribute;
		string += ", value : "+ value ;
		
		return string;
	}
	
}
