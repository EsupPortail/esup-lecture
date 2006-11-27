package org.esupportail.lecture.domain;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.dao.DaoService;
import org.esupportail.lecture.domain.model.Channel;

/**
 * Class where are defined differents user attributes, provided by portlet container,
 * used by the lecture channel and others tools
 * @author gbouteil
 *
 */public class DomainTools {
	/*
	 ************************** PROPERTIES *********************************/	

	/**
	 * Log instance
	 */
	protected static final Log log = LogFactory.getLog(DomainTools.class);

	/**
	 * Current DaoService initialised during portlet init
	 */
	private static DaoService daoService;

	/**
	 * Current Channel initialised duriant portlet init
	 */
	private static Channel channel;

	/**
	 * Attribute name used to identified the user profile.
	 * It is defined in the channel config
	 */
	public static String USER_ID;
	
	/**
	 * Name of the portlet preference that set a context to an instance of the channel
	 */
	public static final String CONTEXT = "context";
	
	
	/*
	 ************************** ACCESSORS *********************************/	
	/**
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
	 */
	public static void setDaoService(DaoService daoService) {
		DomainTools.daoService = daoService;
	}

	public static Channel getChannel() {
		return channel;
	}

	public static void setChannel(Channel channel) {
		DomainTools.channel = channel;
	}

}
