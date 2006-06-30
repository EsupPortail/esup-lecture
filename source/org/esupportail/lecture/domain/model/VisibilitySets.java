package org.esupportail.lecture.domain.model;


import java.util.*;

/**
 * group Visibility sets for managed categories and sources 
 * @author gbouteil
 *
 */
public class VisibilitySets {

/* ************************** PROPERTIES ******************************** */	
	/**
	 * Group of allowed users to subscribe to element
	 */
	private DefAndContentSets allowed = new DefAndContentSets();
	/**
	 * Group of autoSubribed users to element
	 */
	private DefAndContentSets autoSubscribed = new DefAndContentSets();
	/**
	 * Group of obliged users (no unsubscription possible) 
	 */
	private DefAndContentSets obliged = new DefAndContentSets();

/* ************************** ACCESSORS ******************************** */	

	public DefAndContentSets getAllowed() {
		return allowed;
	}
	public void setAllowed(DefAndContentSets allowed) {
		this.allowed = allowed;
	}

	public DefAndContentSets getAutoSubscribed() {
		return autoSubscribed;
	}
	public void setAutoSubscribed(DefAndContentSets autoSubscribed) {
		this.autoSubscribed = autoSubscribed;
	}

	public DefAndContentSets getObliged() {
		return obliged;
	}
	public void setObliged(DefAndContentSets obliged) {
		this.obliged = obliged;
	}

/* ************************** METHODS ******************************** */	
	
	public String toString(){
		
		String string ="";
		string += "	   allowed : "+"\n"+ allowed.toString();
		string += "    autoSubscribed : "+ autoSubscribed.toString()+"\n";
		string += "    obliged : "+ obliged.toString()+"\n";
		
		return string;
	}
	
	
}
