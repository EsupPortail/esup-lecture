package org.esupportail.lecture.domain;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.beans.UserBean;
import org.esupportail.lecture.domain.DomainTools;

/**
 * @author bourges
 * an implementation of ExternalService for tests
 */
public class ExternalServiceTest implements ExternalService {
	protected static final Log log = LogFactory.getLog(ExternalServiceTest.class); 

	/**
	 * @see org.esupportail.lecture.domain.ExternalService#getConnectedUserId()
	 */
	public String getConnectedUserId() {
		return getUserAttribute(DomainTools.USER_ID);
	}

	/**
	 * @see org.esupportail.lecture.domain.ExternalService#getCurrentContextId()
	 */
	public String getCurrentContextId() {
		return getPreferences(DomainTools.CONTEXT);
	}

	/**
	 * @see org.esupportail.lecture.domain.ExternalService#getPreferences(java.lang.String)
	 */
	public String getPreferences(String name) {
		if (name.equalsIgnoreCase(DomainTools.CONTEXT)){
			return "c1";
		}else {
			return null;
		}
	}

	public String getUserAttribute(String attribute) {
		if (attribute.equalsIgnoreCase(DomainTools.USER_ID)){
			return "bourges";
		}else if (attribute.equalsIgnoreCase("")){
			return "";
			// TODO compléter avec des attributs définis dans les groupes de visibilité
		}else if(attribute.equalsIgnoreCase("sn")){
			return "User";
		}
		return "";
	}

	public String getUserProxyTicketCAS() {
		// non encore utilisée
		return null;
	}

	public boolean isUserInRole(String group) {
		if(group.equalsIgnoreCase("")){
			return false;
		}else if(group.equalsIgnoreCase("local.0")){
			return true;
		}
		return false;
		
	}

}
