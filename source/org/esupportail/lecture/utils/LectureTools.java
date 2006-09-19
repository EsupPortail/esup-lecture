package org.esupportail.lecture.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.dao.DaoService;

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
public class LectureTools {
	
	/*
	 ************************** PROPERTIES *********************************/	

	/**
	 * Log instance
	 */
	protected static final Log log = LogFactory.getLog(LectureTools.class);

	
	/**
	 * Attribute name used to identified the user profile.
	 * It is defined in the channel config
	 */
	public static String USER_ID;
	
	/**
	 * Name of the portlet preference that set a context to an instance of the channel
	 */
	public static final String CONTEXT = "context";
	
	/**
	 * @param userId
	 * @see LectureTools#USER_ID
	 */
	public static void setUSER_ID(String userId){
		USER_ID=userId;
	}
	
	
	/**
	 * @return USER_ID
	 * @see LectureTools#USER_ID
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
