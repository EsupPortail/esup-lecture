package org.esupportail.lecture.domain;

import java.util.List;

import org.esupportail.lecture.domain.beans.CategoryBean;
import org.esupportail.lecture.domain.beans.ContextBean;
import org.esupportail.lecture.domain.beans.SourceBean;

/**
 * @author bourges
 * an implementation of DomainService for tests
 */
public class DomainServiceTest implements DomainService {

	/** 
	 * @see org.esupportail.lecture.domain.DomainService#getContext(java.lang.String)
	 */
	public ContextBean getContext(String contextId) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#getCategories(java.lang.String, java.lang.String)
	 */
	public List<CategoryBean> getCategories(String contextId, String uid) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.esupportail.lecture.domain.DomainService#getSources(java.lang.String, java.lang.String)
	 */
	public List<SourceBean> getSources(String categoryId, String uid) {
		// TODO Auto-generated method stub
		return null;
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

}
