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
import org.esupportail.lecture.domain.model.CustomContext;
import org.esupportail.lecture.domain.model.UserProfile;
import org.esupportail.lecture.domain.service.DomainService;
import org.esupportail.lecture.domain.service.FacadeService;
//import org.esupportail.lecture.domain.service.PortletService;
import org.esupportail.lecture.utils.exception.*;
//import org.esupportail.lecture.beans.ContextUserBean;
import org.esupportail.lecture.beans.ContextUserBean;
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
	static FacadeService facadeService;
	/** 
	 * Main domain model class
	 */
	static Channel myChannel; 
	
	/**
	 * Log instance 
	 */
	protected static final Log log = LogFactory.getLog(DomainServiceImplGwe.class); 
	
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
	 * @see org.esupportail.lecture.domain.service.DomainService#getUserBean(java.lang.String)
	 */
	public UserBean getUserBean(String userId) {
		/* Get user profile */
		UserProfile userProfile = myChannel.getUserProfile(userId);
		
		/* Create userBean */
		UserBean userBean = new UserBean();
		userBean.setId(userProfile.getUserId());
			
		return userBean;
	}
	
	/**
	 * @see org.esupportail.lecture.domain.service.DomainService#getContextUserBean(java.lang.String, java.lang.String)
	 */
	public ContextUserBean getContextUserBean(String userId,String contextId) throws ErrorException {
		/* Get custom context */
		Context context = myChannel.getContext(contextId);
		if (context == null) {
			throw new ErrorException("Context "+contextId+" is not defined in this channel");
		}
		
		/* Create ContextUserBean */
		ContextUserBean contextUserBean = new ContextUserBean();
		contextUserBean.setName(context.getName());
		contextUserBean.setDescription(context.getDescription());
		contextUserBean.setId(contextId);
		
		UserProfile userProfile = myChannel.getUserProfile(userId);
		CustomContext customContext = userProfile.getCustomContext(contextId);
		contextUserBean.setTest(customContext.test);
		return contextUserBean;
		
	}

	
	
//	private ContextUserBean
//		
//		
//		CustomContext customContext = userProfile.getCustomContext(contextId);
//		
//		
//		context
//		
//		
//		return userBean;
//	}
	






	
	
	
	/** 
	 * @deprecated
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
	 * @deprecated
	 * Tests method
	 * @see org.esupportail.lecture.domain.service.DomainService#getContexts()
	 */
	public Hashtable<String, Context> getContexts() {
		// TODO Auto-generated method stub
		return null;
	}


	/**
	 * @deprecated
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
	 * @param facadeService The portletService to set.
	 */
	public void setFacadeService(FacadeService facadeService) {
		DomainServiceImplGwe.facadeService = facadeService;
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
		DomainServiceImplGwe.myChannel = myChannel;
	}







	

	



		

}
