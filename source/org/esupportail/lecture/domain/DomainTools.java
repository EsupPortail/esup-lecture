package org.esupportail.lecture.domain;

import org.esupportail.lecture.dao.DaoService;
import org.esupportail.lecture.domain.model.Channel;

public class DomainTools {

	/**
	 * Current DaoService initialised during portlet init
	 */
	private static DaoService daoService;

	/**
	 * Current Channel initialised duriant portlet init
	 */
	private static Channel channel;

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
