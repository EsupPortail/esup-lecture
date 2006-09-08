package org.esupportail.lecture.domain.model;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Class where are defined differents user attributes used by the lecture channel.
 * @author gbouteil
 *
 */
public class UserAttributes {

	public static String USER_ID;
	//TODO : lors de l'utilisation de ces attributs, verifier leur existance dans cette liste
	private static List<String> ATTRIBUTES;
	
	public static void init(){
		ATTRIBUTES = new ArrayList();
	}
	
	/**
	 * @return Returns the aTTRIBUTES.
	 */
	public static List<String> getATTRIBUTES() {
		return ATTRIBUTES;
	}
	/**
	 * @param attributes The aTTRIBUTES to set.
	 */
	public static void setATTRIBUTES(List<String> attributes) {
		ATTRIBUTES = attributes;
	}

	public static void setAttribute(String attributeName){
		ATTRIBUTES.add(attributeName);
	}
	
	public static void setUSER_ID(String userId){
		USER_ID=userId;
	}
	
}
