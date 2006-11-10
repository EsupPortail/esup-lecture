package org.esupportail.lecture.domain;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.beans.CategoryBean;
import org.esupportail.lecture.domain.beans.ContextBean;
import org.esupportail.lecture.domain.beans.ItemBean;
import org.esupportail.lecture.domain.beans.SourceBean;
import org.esupportail.lecture.domain.model.Channel;
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
	
	/** 
	 * @see org.esupportail.lecture.domain.service.DomainService#loadChannel()
	 */
	public void initialize(){
		channel.startup();
	}

	/*
	 ************************** Méthodes ************************************/
	public List<CategoryBean> getCategories(String contextId, String uid) {
		// TODO Auto-generated method stub
		return null;
	}



	public ContextBean getContext(String contextId) {
		// TODO Auto-generated method stub
		return null;
	}



	public List<SourceBean> getSources(String categoryId, String uid) {
		// TODO Auto-generated method stub
		return null;
	}



	public void marckItemasRead(String uid, String itemId, String sourceId) {
		// TODO Auto-generated method stub
		
	}



	public void marckItemasUnread(String uid, String itemId, String sourceId) {
		// TODO Auto-generated method stub
		
	}
	public List<ItemBean> getItems(String sourceId, String uid) {
		// TODO Auto-generated method stub
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





	
}
