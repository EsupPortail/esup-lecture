package org.esupportail.lecture.web;

import java.util.Hashtable;
import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.beans.ContextUserBean;
import org.esupportail.lecture.domain.model.UserAttributes;
import org.esupportail.lecture.domain.service.FacadeService;
import org.esupportail.lecture.domain.service.PortletService;
import org.esupportail.lecture.domain.service.impl.DomainServiceImplGwe;

// TODO : a travailler
public class SessionManager {

	/**
	 * User Id of current connected user
	 */
	private String userId;
	
	/**
	 * Log instance 
	 */
	protected static final Log log = LogFactory.getLog(SessionManager.class); 
	
	private Hashtable<String,ContextUserBean> contextUserBeans;
	
	public SessionManager(){
		contextUserBeans = new Hashtable<String,ContextUserBean>();
	}
	
	/**
	 * @param userId The userId to set.
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	
	public String getCurrentContextId() {
		return getPreferences("context");
	}

	public static String getPreferences(String name){
			
	    ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
	    log.error("Preferences, external context : "+externalContext.toString() );
	    PortletRequest request = (PortletRequest) externalContext.getRequest();
	    PortletPreferences portletPreferences = request.getPreferences();
	    String value = portletPreferences.getValue(name, name+" not define !");
	    return value;
		
	}


	public ContextUserBean getContextUserBean() {
		
		return contextUserBeans.get(getCurrentContextId());
	}


	public void addContextUserBean(ContextUserBean context) {
		contextUserBeans.put(context.getId(),context);
		
	}


	
	public static String getUserAttribute(String attributeName) {
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		log.error("UserAttribute, external context : "+externalContext.toString() );
		PortletRequest request = (PortletRequest) externalContext.getRequest();
				
		Map userInfo = (Map)request.getAttribute(PortletRequest.USER_INFO);
		String attributeValue = (String)userInfo.get(attributeName);
		//TODO gerer attribute non trouvé
		return attributeValue;
	}

	
}
