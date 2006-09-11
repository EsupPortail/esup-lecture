package org.esupportail.lecture.domain.service.impl;
/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
import java.util.Hashtable;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.model.Category;
import org.esupportail.lecture.domain.model.Context;
//import org.esupportail.lecture.domain.model.CustomContext;
//import org.esupportail.lecture.domain.model.Source;
import org.esupportail.lecture.domain.model.Channel;
import org.esupportail.lecture.domain.model.UserProfile;
import org.esupportail.lecture.domain.model.UserAttributes;
import org.esupportail.lecture.domain.service.DomainService;
import org.esupportail.lecture.domain.service.FacadeService;
import org.esupportail.lecture.domain.service.PortletService;
import org.esupportail.lecture.utils.exception.*;
//import org.esupportail.lecture.beans.ContextUserBean;
import org.esupportail.lecture.beans.UserBean;
//import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
/**
 * Implémentation des services pour le test 
 * Utilisée par Gwénaëlle
 * @author gbouteil
 *
 */
public class DomainServiceImplGwe implements DomainService {

	/*
	 ************************** PROPERTIES *********************************/	

	/**
	 * Services provided by portlet request
	 */
	FacadeService facadeService;
	/** 
	 * Main domain model class
	 */
	Channel myChannel; 
	
	/**
	 * Log instance 
	 */
	protected static final Log log = LogFactory.getLog(Channel.class); 
	
	/*
	 ************************** Initialization ************************************/
	
	/** 
	 * @see org.esupportail.lecture.domain.service.DomainService#loadChannel()
	 */
	public void loadChannel() throws FatalException,MyException {
		myChannel.startup();
	}
	
	
	/*
	 *************************** METHODS ************************************/

	/**
	 * @see org.esupportail.lecture.domain.service.DomainService#getUserBean()
	 */
	public UserBean getUserBean() {
		/* Get user profile */
		String userId = facadeService.getPortletService().getUserAttribute(UserAttributes.USER_ID);
		UserProfile userProfile = myChannel.getUserProfile(userId);
		
		/* Create userBean */
		UserBean userBean = new UserBean();
		userBean.setId(userProfile.getUserId());
	
		//userBean.setContext(userProfile);
		
		return userBean;
	}
	
//	private ContextUserBean
//		
//		/* Get custom context */
//		String contextId = portletService.getCurrentContext();
//		Context context = myChannel.getContext(contextId);
//		if (context == null) {
//			throw ErrorException("Context "+contextId+" is not defiend in this channel");
//		}
//		CustomContext customContext = userProfile.getCustomContext(contextId);
//		
//		/* Create ContextWeb */
//		ContextUserBean contextUserBean = new ContextUserBean();
//		context
//		
//		
//		return userBean;
//	}
	



	
	
	
	/** 
	 * Tests method
	 * @return hashtable of context defined in myChannel
	 * @see org.esupportail.lecture.domain.model.Channel
	 */
	public Hashtable<String,Context> getContextsHash() {
		return myChannel.getContextsHash();
	}


	/**
	 * @see org.esupportail.lecture.domain.service.DomainService#channelToString()
	 */
	public String channelToString(){
		return myChannel.toString();
	}
	

	/**
	 * @see org.esupportail.lecture.domain.service.DomainService#getContexts()
	 */
	public Hashtable<String, Context> getContexts() {
		// TODO Auto-generated method stub
		return null;
	}


	/**
	 * @see org.esupportail.lecture.domain.service.DomainService#getCategories()
	 */
	public List<Category> getCategories() {
		// TODO Auto-generated method stub
		return null;
	}


	/* ************************** ACCESSORS ********************************* */
	
	/**
	 * @return Returns the portletService.
	 */
	public FacadeService getFacadeService() {
		return facadeService;
	}

	/**
	 * @param portletService The portletService to set.
	 */
	public void setFacadeService(FacadeService facadeService) {
		this.facadeService = facadeService;
	}

	/**
	 * @return Returns the myChannel.
	 */
	public Channel getMyChannel() {
		return myChannel;
	}

	/**
	 * @param myChannel The myChannel to set.
	 */
	public void setMyChannel(Channel myChannel) {
		this.myChannel = myChannel;
	}




	

	



		

}
