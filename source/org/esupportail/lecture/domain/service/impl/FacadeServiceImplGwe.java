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
import org.esupportail.lecture.domain.model.tmp.Item;
import org.esupportail.lecture.domain.model.tmp.SourceRB;
import org.esupportail.lecture.domain.service.FacadeService;
import org.esupportail.lecture.utils.exception.*;
/**
 * Impl�mentation des services pour le test 
 * Utilis�e par Gw�na�lle
 * @author gbouteil
 *
 */
public class FacadeServiceImplGwe implements FacadeService {

	Channel myChannel; 
	/**
	 * Log instance 
	 */
	protected static final Log log = LogFactory.getLog(Channel.class); 
	
	
	/**
	 * Lecture service cr�ation : loads config and mapping files
	 */
	public FacadeServiceImplGwe(){
		myChannel = new Channel();
	}
	
	
	/** 
	 * @see org.esupportail.lecture.domain.service.FacadeService#loadChannel()
	 */
	public void loadChannel() throws FatalException,MyException {
		myChannel.startup();
	}

	/**
	 * @see org.esupportail.lecture.domain.service.FacadeService#newUserSession()
	 */
	public void newUserSession() throws MyException,FatalException {
		myChannel.startup();
		// TODO le reste � propos du user
	}

	
	/**
	 * @see org.esupportail.lecture.domain.service.FacadeService#reloadChannelConfig()
	 */
	public void reloadChannelConfig() throws FatalException{
		myChannel.loadConfig();
	}
	
	/**
	 * @see org.esupportail.lecture.domain.service.FacadeService#reloadMappingFile()
	 */
	public void reloadMappingFile()throws FatalException {
		myChannel.loadMappingFile();
	}
	
	/**
	 * @see org.esupportail.lecture.domain.service.FacadeService#channelToString()
	 */
	public String channelToString(){
		return myChannel.toString();
	}
	
	/**
	 * @see org.esupportail.lecture.domain.service.FacadeService#getContexts()
	 */
	public Set<Context> getContexts() {
		return myChannel.getContexts();
	}
	
	public List<Category> getCategories() {
		// TODO Auto-generated method stub
		return null;
	}

	public void toogleItemReadState(int sourceID, int itemID) {
		// TODO Auto-generated method stub
		
	}




		

}
