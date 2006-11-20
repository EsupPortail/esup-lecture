package org.esupportail.lecture.domain;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.beans.CategoryBean;
import org.esupportail.lecture.domain.beans.ContextBean;
import org.esupportail.lecture.domain.beans.ItemBean;
import org.esupportail.lecture.domain.beans.SourceBean;
import org.esupportail.lecture.domain.beans.UserBean;
import org.esupportail.lecture.domain.model.Channel;
import org.esupportail.lecture.domain.model.CustomContext;
import org.esupportail.lecture.domain.model.UserProfile;

/**
 * Implémentation des services offerts par la couche métier
 * @author gbouteil
 *
 */
public class DomainServiceImpl implements DomainService {
	/** 
	 * Main domain model class
	 */
	static Channel channel; 
	
	/**
	 * Log instance 
	 */
	protected static final Log log = LogFactory.getLog(DomainServiceImpl.class); 
	
	/*
	 ************************** Initialization ************************************/
	

	
	/*
	 ************************** Methodes ************************************/

	/*
	 * User Connected
	 */

	/**
	 * @see org.esupportail.lecture.domain.DomainService#getConnectedUser(java.lang.String)
	 */
	public UserBean getConnectedUser(String userId) {
		/* User profile creation */
		UserProfile userProfile = channel.getUserProfile(userId);
		
		/* userBean creation */
		UserBean user = new UserBean();
		user.setUid(userProfile.getUserId());
		
		return user;
	}

	
	
	/*
	 * Current Context
	 */
	
	/**
	 * @see org.esupportail.lecture.domain.DomainService#getContext(java.lang.String, java.lang.String, org.esupportail.lecture.domain.ExternalService)
	 */
	public ContextBean getContext(String userId,String contextId,ExternalService externalService) {
		
		/* Get current user profile and customContext */
		UserProfile userProfile = channel.getUserProfile(userId);
		CustomContext customContext = userProfile.getCustomContext(contextId,externalService);
		
		// TODO mise à jour du DAO ?
//		DomainTools.getDaoService().updateCustomContext(customContext);
//		DomainTools.getDaoService().updateUserProfile(userProfile);
		
		/* Make the contextUserBean to display */
		ContextBean contextBean = new ContextBean(customContext);
		
		return contextBean;		
	}

	/*
	 * Categories of the current context
	 */
	
	/**
	 * @see org.esupportail.lecture.domain.DomainService#getCategories(java.lang.String, java.lang.String)
	 */
	public List<CategoryBean> getCategories(String uid, String contextId) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<ItemBean> getItems(String uid, String sourceId) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<SourceBean> getSources(String uid, String categoryId) {
		// TODO Auto-generated method stub
		return null;
	}

	public void marckItemasRead(String uid, String sourceId, String itemId) {
		// TODO Auto-generated method stub
		
	}
	



	/*
	 ************************** Accessors ************************************/

	public Channel getChannel() {
		// J'ai retiré le caractère static de la méthode pour la config spring
		return channel;
	}

	public void setChannel(Channel channel) {
		// J'ai retiré le caractère static de la méthode pour la config spring
		DomainServiceImpl.channel = channel;
	}



	public void marckItemasUnread(String uid, String sourceId, String itemId) {
		// TODO Auto-generated method stub
		
	}





	
}
