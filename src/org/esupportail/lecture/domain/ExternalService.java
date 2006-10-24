package org.esupportail.lecture.domain;

import org.esupportail.lecture.domain.beans.UserBean;

public interface ExternalService {

	UserBean getConnectedUser();

	String getCurrentContextId();

}
