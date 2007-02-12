package org.esupportail.lecture.web.controllers;

import java.util.Hashtable;

import org.esupportail.lecture.domain.DomainTools;
import org.esupportail.lecture.domain.FacadeService;
import org.esupportail.lecture.exceptions.domain.InternalExternalException;
import org.esupportail.lecture.exceptions.web.WebException;


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
	 * context ID defined via the portlet preference "context"
	 */
	private String contextId;
	
	
	/*
	 ************************** Initialization *******************************/
	
	/**
	 * Constructor
	 */
	public VirtualSession(String contextId){
		sessions = new Hashtable<String,Hashtable<String,Object>>();
		this.contextId = contextId;
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
	 * remove the value corresponding to "key" in the hashtable of the currentVirtualSession
	 * @param key
	 * @return the value
	 */
	public Object remove(String key) {
		Hashtable<String,Object> currentVirtualSession = getCurrentVirtualSession();
		return currentVirtualSession.remove(key);		
	}
	
	/**
	 * @return the hashtable of the current virtual session
	 */
	private Hashtable<String,Object> getCurrentVirtualSession() {
		
		Hashtable<String,Object> currentVirtualSession = sessions.get(contextId);
		
		if (currentVirtualSession == null) {
			currentVirtualSession = new Hashtable<String,Object>();
			sessions.put(contextId,currentVirtualSession);
		}	
		return currentVirtualSession;
	}
	
	/*
	 ************************** ACCESSORS ***********************************/
	
	/**
	 * just used for debug trace
	 * @return Hastable of sessions
	 */
	protected Hashtable<String, Hashtable<String, Object>> getSessions() {
		return sessions;
	}

}
