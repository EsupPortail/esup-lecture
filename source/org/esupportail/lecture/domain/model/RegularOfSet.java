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
	 * attribute required value 
	 */
	private String attribute = "";
	
	/**
	 * Value required by the attribute to be in the group that is defined
	 */
	private String value = "";


/* ************************** METHODS ******************************** */	
	/**
	 * Check existence of attributes names used in regular definition
	 */
	protected void checkNamesExistence(){
		// TODO vérification de l'existence de l'attribut dans le portail ?
		// TODO vérifiaction de l'existance de la valeur dans le portail ?
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		
		String string ="";
		string += "attribute : "+ attribute;
		string += ", value : "+ value ;
		
		return string;
	}

/* ************************** ACCESSORS ******************************** */	

	/**
	 * Returns attribute name
	 * @return attribute
	 * @see RegularOfSet#attribute
	 */
	protected String getAttribute() {
		return attribute;
	}
	/**
	 * Sets attribute name
	 * @param attribute
	 * @see RegularOfSet#attribute
	 */
	protected void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	/**
	 * Returns attribute required value
	 * @return value
	 * @see RegularOfSet#value
	 */
	protected String getValue() {
		return value;
	}
	/**
	 * Sets attribute required value
	 * @param value
	 * @see RegularOfSet#value
	 */
	protected void setValue(String value) {
		this.value = value;
	}
	
}
