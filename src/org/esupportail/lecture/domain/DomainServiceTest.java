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
		UserBean user = new UserBean();
		user.setUid(userId);
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
	public List<CategoryBean> getCategories(String contextId, String uid) {
		List<CategoryBean> ret = null;
		ret = categories;
		return ret;
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#getSources(java.lang.String, java.lang.String)
	 */
	public List<SourceBean> getSources(String categoryId, String uid) {
		List<SourceBean> ret = null;
		ret = sources;
		return ret;
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#getItems(java.lang.String, java.lang.String)
	 */
	public List<ItemBean> getItems(String sourceId, String uid) {
		return items;
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#marckItemasRead(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void marckItemasRead(String uid, String itemId, String sourceId) {
		// TODO Auto-generated method stub

	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#marckItemasUnread(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void marckItemasUnread(String uid, String itemId, String sourceId) {
		// TODO Auto-generated method stub

	}

	public ContextBean getContext(String uid, String contextId, ExternalService externalService) {
		// TODO Auto-generated method stub
		return getContext(uid, contextId);
	}

	public List<CategoryBean> getCategories(String uid, String contextId, ExternalService externalService) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<SourceBean> getSources(String uid, String categoryId, ExternalService externalService) {
		// TODO Auto-generated method stub
		return null;
	}



}
