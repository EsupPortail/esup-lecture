/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.dao.DaoService;
import org.esupportail.lecture.domain.model.Channel;
import org.esupportail.lecture.exceptions.domain.InternalExternalException;
import org.esupportail.lecture.exceptions.domain.NoExternalValueException;

/**
 * Provide various tools :
 * - constants defintion for user attributes provided by externalService. 
 * - single access to DaoService, externalService, channel for domain layer
 * @author gbouteil
 */
public class DomainTools {
	
	/*
	 ************************** PROPERTIES *********************************/	

	/**
	 * Log instance
	 */
	protected static final Log log = LogFactory.getLog(DomainTools.class);

	/* 
	 * Single Access to layers or principal classes */
	
	/**
	 * Current DaoService initialised during portlet init
	 */
	private static DaoService daoService;

	/**
	 * current External Service during portlet init
	 */
	private static ExternalService externalService;

	/**
	 * Current Channel initialised during portlet init
	 */
	private static Channel channel;

	/*
	 * Constants definition */
	
	/**
	 * Attribute name used to identified the user profile.
	 * It is defined in the channel config
	 */
	public static String USER_ID;
	
	/**
	 * Name of the portlet preference that set a context to an instance of the channel
	 */
	public static final String CONTEXT = "context";
	// TODO (GB later) externaliser cette chaine ?
	
	/*
	 ************************** INIT ******************************** */	

	
	
	/*
	 *************************** METHODS ******************************** */	

	/**
	 *
	 * @param value - string where we try to find user attribute to replace
	 * @return value with {attribute} replaced by this attribute according to current connected user
	 */
	// TODO (RB <-- GB) Peux tu compléter la javadoc ?
	public static String replaceWithUserAttributes(String value) {
		if (log.isDebugEnabled()){
			log.debug("replaceWithUserAttributes("+value+")");
		}
		String ret = null;
		if (value!=null) {
			// find and replace {...}
			int firstIndex = value.indexOf("{");
			int lastIndex = value.indexOf("}");
			//is { and } in value
			if (firstIndex!=-1 && lastIndex!=-1) {
				String begin = value.substring(0, firstIndex);
				//med = attribute to replace
				String med = value.substring(firstIndex+1, lastIndex);
				String end = value.substring(lastIndex+1, value.length());
				//replacing med value
				String realMedValue;
				try {
					realMedValue = externalService.getUserAttribute(med);
				} catch (NoExternalValueException e) {
					log.warn("replaceWithUserAttributes("+value+") generate "+e.getMessage());
				} catch (InternalExternalException e) {
					log.warn("replaceWithUserAttributes("+value+") generate "+e.getMessage());
				} finally {
					realMedValue = med;
				}
				//generate return value
				ret = begin+realMedValue+end;								
				if (log.isDebugEnabled()){
					log.debug("replaceWithUserAttributes"+" :: replace "+value+" by "+ret);
				}
				//is there some other { } ?
				int firstIndex2 = ret.indexOf("{");
				int lastIndex2 = ret.indexOf("}");
				//is { and } in value
				if (firstIndex2!=-1 && lastIndex2!=-1) {
					ret = replaceWithUserAttributes(ret);
				}
			}
			// no { and } in value
			else {
				ret = value;
			}
		}
		return ret;
	}
	
	
	
	
	/*
	 ************************** ACCESSORS *********************************/	
	/**
	 * Set string that defines user ID in externalService 
	 * @param userId
	 * @see DomainTools#USER_ID
	 */
	public static void setUSER_ID(String userId){
		USER_ID=userId;
	}
	/**
	 * Return an instance of current DaoService initialised by Spring
	 * @return current DomainService
	 */
	public static DaoService getDaoService() {
		return DomainTools.daoService;
	}

	/**
	 * set current DaoService (used by Spring)
	 * @param daoService 
	 */
	public static void setDaoService(DaoService daoService) {
		DomainTools.daoService = daoService;
	}

	/**
	 * Returns current channel instance (main class)
	 * @return current channel
	 */
	public static Channel getChannel() {
		return channel;
	}

	/**
	 * set current channel instance (used by Spring)
	 * @param channel
	 */
	public static void setChannel(Channel channel) {
		DomainTools.channel = channel;
	}
	

	/**
	 * @return external service
	 */
	public static ExternalService getExternalService() {
		return externalService;
	}
	
	/**
	 * @param externalService
	 */
	public static void setExternalService(ExternalService externalService) {
		DomainTools.externalService = externalService;
	}

}
