package org.esupportail.lecture.domain;

import java.util.List;

import org.esupportail.lecture.domain.beans.CategoryBean;
import org.esupportail.lecture.domain.beans.ContextBean;
import org.esupportail.lecture.domain.beans.ItemBean;
import org.esupportail.lecture.domain.beans.SourceBean;
import org.esupportail.lecture.domain.beans.UserBean;

/**
 * @author bourges
 * an implementation of DomainService for tests
 */
public class DomainServiceTest implements DomainService {
	/**
	 * Test context
	 */
	private ContextBean context;
	/**
	 * List of test categories
	 */
	private List<CategoryBean> categories;
	/**
	 * List of test sources
	 */
	private List<SourceBean> sources;
	/**
	 * List of test items
	 */
	private List<ItemBean> items;
	

	/**
	 * constructor used by Spring to instantiate this Test Class
	 * @param context
	 * @param categories
	 * @param sources
	 * @param items
	 */
	public DomainServiceTest(ContextBean context, List<CategoryBean> categories, List<SourceBean> sources, List<ItemBean> items) {
		super();
		this.context = context;
		this.categories = categories;
		this.sources = sources;
		this.items = items;
	}

	public UserBean getConnectedUser(String userId){
		UserBean user = new UserBean(userId);
		return user;
	}
	

	/**
	 * @see org.esupportail.lecture.domain.DomainService#getContext(java.lang.String, java.lang.String)
	 */
	public ContextBean getContext(String uid, String contextId) {
		ContextBean ret = null;
		if (context.getId().equals(contextId)) {
			ret = context;
		}
		return ret;
	}


	/**
	 * @see org.esupportail.lecture.domain.DomainService#getCategories(java.lang.String, java.lang.String)
	 */
	public List<CategoryBean> getVisibleCategories(String contextId, String uid) {
		List<CategoryBean> ret = null;
		ret = categories;
		return ret;
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#getSources(java.lang.String, java.lang.String)
	 */
	public List<SourceBean> getVisibleSources(String categoryId, String uid) {
		List<SourceBean> ret = null;
		ret = sources;
		return ret;
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#getItems(java.lang.String, java.lang.String)
	 */
	public List<ItemBean> getItems(String sourceId, String uid,ExternalService ex) {
		return items;
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#marckItemasRead(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void marckItemAsRead(String uid, String itemId, String sourceId) {
		// TODO Auto-generated method stub

	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#marckItemasUnread(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void marckItemAsUnread(String uid, String itemId, String sourceId) {
		// TODO Auto-generated method stub

	}

	public ContextBean getContext(String uid, String contextId, ExternalService ex) {
		return getContext(uid, contextId);
	}

	public List<CategoryBean> getVisibleCategories(String uid, String contextId, ExternalService ex) {
		return getVisibleCategories(contextId, uid);
	}

	public List<SourceBean> getVisibleSources(String uid, String categoryId, ExternalService ex) {
		return getVisibleSources(categoryId, uid);
	}

	public List<SourceBean> getAvailableSources(String uid, String categoryId, ExternalService ex) {
		List<SourceBean> ret = null;
		ret = sources;
		return ret;
	}

	public void setTreeSize(String uid, String contextId, int size) {
		// TODO Auto-generated method stub
		
	}

	public void foldCategory(String uid, String cxtId, String catId) {
		// TODO Auto-generated method stub
		
	}

	public void unfoldCategory(String uid, String cxtId, String catId) {
		// TODO Auto-generated method stub
		
	}



}
