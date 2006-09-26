package org.esupportail.lecture.domain.service.impl;
/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.DomainTools;
import org.esupportail.lecture.domain.model.Category;
import org.esupportail.lecture.domain.model.Context;
import org.esupportail.lecture.domain.model.Channel;
import org.esupportail.lecture.domain.model.CustomCategory;
import org.esupportail.lecture.domain.model.CustomContext;
import org.esupportail.lecture.domain.model.CustomManagedCategory;
import org.esupportail.lecture.domain.model.CustomSource;
import org.esupportail.lecture.utils.LectureTools;
import org.esupportail.lecture.domain.model.ManagedCategoryProfile;
import org.esupportail.lecture.domain.model.UserProfile;
import org.esupportail.lecture.domain.service.DomainService;
import org.esupportail.lecture.domain.service.FacadeService;
import org.esupportail.lecture.domain.service.PortletService;
import org.esupportail.lecture.utils.exception.*;
import org.esupportail.lecture.beans.CategoryUserBean;
import org.esupportail.lecture.beans.ContextUserBean;
import org.esupportail.lecture.beans.SelectedBean;
import org.esupportail.lecture.beans.SourceUserBean;
import org.esupportail.lecture.beans.UserBean;
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
	 * Access to Portlet Services 
	 */
	PortletService portletService;
	
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
		
		/* Get current user profile and customContext */
		UserProfile userProfile = myChannel.getUserProfile(userId);
		CustomContext customContext = userProfile.getCustomContext(contextId);
	
		/* Updating data for the customContext */
		customContext.updateData(portletService);

		
		//TODO a voir où mettre de façon intelligente
		// à cet endroit, le custom context ne peux pas etre null si le context s'est affiché à l'écran.
		DomainTools.getDaoService().addCustomContext(customContext);
		
		//TODO !!!! add not correct because userProfile already exist --> duplicate key in database !!!!
		//DomainTools.getDaoService().addUserProfile(userProfile);
		
		/* Make the contextUserBean to display */
		ContextUserBean contextUserBean = makeContextUserBean(customContext);

		return contextUserBean;		
	}
			
	private ContextUserBean makeContextUserBean(CustomContext customContext) {
		
		/* Context */
		ContextUserBean contextUserBean = new ContextUserBean();
		contextUserBean.init(customContext);
		
		/* selectedBean */
		SelectedBean selectedBean = new SelectedBean();
		// Un selectedBean dure moins longtemps qu'un ContextUserBean : peut changer à chaque requete.
		selectedBean.init(customContext);
		contextUserBean.setSelectedBean(selectedBean);
		
		/* Categories */
		List<CustomCategory> listCategories = customContext.getSortedCustomCategories();
		Iterator iterator = listCategories.iterator();
		while (iterator.hasNext()) {
			CustomCategory customCategory = (CustomCategory) iterator.next();
			CategoryUserBean categoryUserBean = makeCategoryUserBean(customCategory);
			contextUserBean.addCategoryUserBean(categoryUserBean) ;
		}
		return contextUserBean;
	}

	private CategoryUserBean makeCategoryUserBean(CustomCategory customCategory) {
		
		CategoryUserBean categoryUserBean = new CategoryUserBean();
		categoryUserBean.init(customCategory);
		List<CustomSource> listSources = customCategory.getSortedCustomSources();
		Iterator iterator = listSources.iterator();
		while (iterator.hasNext()) {
			CustomSource customSource = (CustomSource) iterator.next();
			SourceUserBean sourceUserBean = makeSourceUserBean(customSource);
			categoryUserBean.addSourceUserBean(sourceUserBean) ;
		}
		return categoryUserBean;
	}


	private SourceUserBean makeSourceUserBean(CustomSource customSource) {
		
		SourceUserBean sourceUserBean = new SourceUserBean();
		sourceUserBean.init(customSource);
	
		return sourceUserBean;
	}


	/* ************************** ACCESSORS ********************************* */
		
			
			
	
		


	
	







	
	
	
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


	/**
	 * @return Returns the portletService.
	 */
	public PortletService getPortletService() {
		return portletService;
	}


	/**
	 * @param portletService The portletService to set.
	 */
	public void setPortletService(PortletService portletService) {
		this.portletService = portletService;
	}







	

	



		

}
