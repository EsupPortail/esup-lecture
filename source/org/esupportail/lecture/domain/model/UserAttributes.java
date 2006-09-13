package org.esupportail.lecture.domain.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

//import java.util.ArrayList;
//import java.util.Hashtable;
//import java.util.List;

/**
 * Class where are defined differents user attributes, provided by portlet container,
 * used by the lecture channel.
 * @author gbouteil
 *
 */
/**
 * @author gbouteil
 *
 */
public class UserAttributes {
	
	/*
	 ************************** PROPERTIES *********************************/	

	/**
	 * Log instance
	 */
	protected static final Log log = LogFactory.getLog(UserAttributes.class);

	
	/**
	 * Attribute name used to identified the user profile.
	 * It is defined in the channel config
	 */
	public static String USER_ID;
	
	// voir leur 
	//lors de l'utilisation de ces attributs, verifier leur existance dans cette liste
//	/**
//	 * List of attributes used 
//	 */
//	private static List<String> ATTRIBUTES;

	/*
	 ************************** Initialization ************************************/

//	public static void init(){
//		ATTRIBUTES = new ArrayList();
//	}

	/*
	 *************************** METHODS ************************************/
	
	/* ************************** ACCESSORS ********************************* */

	/**
	 * @param userId
	 * @see UserAttributes#USER_ID
	 */
	public static void setUSER_ID(String userId){
		USER_ID=userId;
	}
	
	
	/**
	 * @return USER_ID
	 * @see UserAttributes#USER_ID
	 */
	public static String getUSER_ID(){
		return USER_ID;
	}
	
//	/**
//	 * @return Returns the aTTRIBUTES.
//	 */
//	public static List<String> getATTRIBUTES() {
//		return ATTRIBUTES;
//	}
	
//	/**
//	 * @param attributes The aTTRIBUTES to set.
//	 */
//	public static void setATTRIBUTES(List<String> attributes) {
//		ATTRIBUTES = attributes;
//	}
//
//	public static void setAttribute(String attributeName){
//		ATTRIBUTES.add(attributeName);
//	}
	

	
}
