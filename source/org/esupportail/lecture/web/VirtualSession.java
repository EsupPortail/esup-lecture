package org.esupportail.lecture.web;

import java.util.Hashtable;

import org.esupportail.lecture.domain.model.LectureTools;
import org.esupportail.lecture.domain.service.PortletService;


/**
 * Class to store informations of multiples instances of a channel 
 * in a same user session.
 * @author gbouteil
 *
 */
public class VirtualSession {

	/*
	 ************************ PROPERTIES ******************************** */	
	
	/**
	 * Hastable of sessions : indexed by their contextId 
	 */
	private Hashtable<String,Hashtable<String,Object>> sessions;
	
	/**
	 * Access to portlet services
	 */
	private PortletService portletService;
	
	
	/*
	 ************************** Initialization *******************************/
	
	/**
	 * Constructor
	 */
	public VirtualSession(){
		sessions = new Hashtable<String,Hashtable<String,Object>>();
	}
	
	/*
	 *************************** METHODS ************************************/
	
	
	/**
	 * Add the couple <key,value> in the hashtable of currentVirtualSession 
	 * @param key
	 * @param value
	 */
	public void put(String key,Object value){
		Hashtable<String,Object> currentVirtualSession = getCurrentVirtualSession();
		currentVirtualSession.put(key,value);
	}


	/**
	 * Get the value corresponding to "key" in the hashtable of the currentVirtualSession
	 * @param key
	 * @return the value
	 */
	public Object get(String key){
		Hashtable<String,Object> currentVirtualSession = getCurrentVirtualSession();
		return currentVirtualSession.get(key);
	}
	
	/**
	 * @return the hashtable of the current virtual session
	 */
	private Hashtable<String,Object> getCurrentVirtualSession() {
		
		String currentContextId = getCurrentContextId();
		Hashtable<String,Object> currentVirtualSession = (Hashtable<String,Object>)sessions.get(currentContextId);
		
		if (currentVirtualSession == null) {
			currentVirtualSession = new Hashtable<String,Object>();
			sessions.put(currentContextId,currentVirtualSession);
		}	
		return currentVirtualSession;
	}
	
	/**
	 * @return the current context id of the channel
	 */
	public String getCurrentContextId() {
		// TODO en attendant la resolution du bug dans uPortal
		String currentContextId = "c1";
		// String currentContextId = portletService.getPreferences(UserAttributes.CONTEXT);
		return currentContextId;
	}
	
	/*
	 ************************** ACCESSORS ***********************************/
	
	/**
	 * @return Returns the portletService.
	 */
	public PortletService getPortletService() {
		return portletService;
	}

	/**
	 * @param portletService The portletService to set.
	 */
	public void setPortletService(PortletService portletService) {
		this.portletService = portletService;
	}

}
