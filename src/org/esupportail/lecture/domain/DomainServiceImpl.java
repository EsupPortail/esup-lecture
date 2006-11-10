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
import org.esupportail.lecture.exceptions.FatalException;

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
	 ************************** Méthodes ************************************/


	public UserBean getConnectedUser(String userId) {
		/* User profile creation */
		//UserProfile userProfile = channel.getUserProfile(userId);
		
		/* userBean creation */
		UserBean user = new UserBean();
		user.setUid(userId);
		
		return user;
	}

	
	
	public ContextBean getContext(String userId,String contextId) {
		
		/* Get current user profile and customContext */
		UserProfile userProfile = channel.getUserProfile(userId);
		CustomContext customContext = userProfile.getCustomContext(contextId);
		
		return null;
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

	public void marckItemasUnread(String uid, String sourceId, String itemId) {
		// TODO Auto-generated method stub
		
	}





	
}
