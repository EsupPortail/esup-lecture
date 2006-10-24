package org.esupportail.lecture.domain;

import org.esupportail.lecture.domain.beans.UserBean;

/**
 * @author bourges
 * external (portlet or servlet) service interface
 */
public interface ExternalService {

	/**
	 * @return UserBean
	 * @see FacadeService#getConnectedUser()
	 */
	UserBean getConnectedUser();

	/**
	 * @return string
	 * @see FacadeService#getCurrentContextId()
	 */
	String getCurrentContextId();

}
