package org.esupportail.lecture.domain;

import org.esupportail.lecture.domain.beans.ContextBean;
import org.esupportail.lecture.domain.beans.UserBean;

public interface DomainService {

	ContextBean getContext(String contextId);

}
