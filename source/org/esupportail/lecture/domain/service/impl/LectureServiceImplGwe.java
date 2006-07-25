package org.esupportail.lecture.domain.service.impl;
/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
import java.util.Set;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.model.Category;
import org.esupportail.lecture.domain.model.Context;
import org.esupportail.lecture.domain.model.Source;
import org.esupportail.lecture.domain.model.Channel;
import org.esupportail.lecture.domain.service.LectureService;
import org.esupportail.lecture.utils.exception.*;
/**
 * Implémentation des services pour le test 
 * Utilisée par Gwénaëlle
 * @author gbouteil
 *
 */
public class LectureServiceImplGwe implements LectureService {

	Channel myChannel; 
	/**
	 * Log instance 
	 */
	protected static final Log log = LogFactory.getLog(Channel.class); 
	
	
	/**
	 * Lecture service création : loads config and mapping files
	 */
	public LectureServiceImplGwe(){
		/* Chargement du canal au lancement de Tomcat */
		myChannel = new Channel();
		try {
			loadChannel();
		} catch (Exception e){
			log.fatal("LectureServiceImplGwe() :: "+e.getMessage());
		}

	}
	
	
	/** 
	 * @see org.esupportail.lecture.domain.service.LectureService#loadChannel()
	 */
	public void loadChannel() throws MyException {
		myChannel.startup();
	}

	/**
	 * @see org.esupportail.lecture.domain.service.LectureService#newUserSession()
	 */
	public void newUserSession() throws MyException {
		myChannel.startup();
		// TODO le reste à propos du user
	}

	/**
	 * @see org.esupportail.lecture.domain.service.LectureService#channelToString()
	 */
	public String channelToString(){
		return myChannel.toString();
	}
	
	/**
	 * @see org.esupportail.lecture.domain.service.LectureService#getContexts()
	 */
	public Set<Context> getContexts() {
		return myChannel.getContexts();
	}
	
	/**
	 * @deprecated
	 */
	public List<Category> getCategories() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @deprecated
	 */
	public List<Source> getSources(Category cat) {
		// TODO Auto-generated method stub
		return null;
	}




		

}
