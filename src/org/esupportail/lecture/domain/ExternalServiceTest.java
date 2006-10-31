package org.esupportail.lecture.domain;

import org.esupportail.lecture.domain.beans.UserBean;

/**
 * @author bourges
 * an implementation of ExternalService for tests
 */
public class ExternalServiceTest implements ExternalService {

	/**
	 * @see org.esupportail.lecture.domain.ExternalService#getConnectedUser()
	 */
	public UserBean getConnectedUser() {
		UserBean ret = new UserBean();
		ret.setUid("bourges");
		return ret;
	}

	/**
	 * @see org.esupportail.lecture.domain.ExternalService#getCurrentContextId()
	 */
	public String getCurrentContextId() {
		return "1";
	}

}
