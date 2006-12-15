/**
 * ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
 * For any information please refer to http://esup-helpdesk.sourceforge.net
 * You may obtain a copy of the licence at http://www.esup-portail.org/license/
 */
package org.esupportail.lecture.web.controllers;

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.dao.DaoService;
import org.esupportail.lecture.domain.model.CustomContext;
import org.esupportail.lecture.domain.model.UserProfile;

/**
 * @author : Raymond 
 */
public class TestController extends AbstractContextAwareController {
	/**
	 * Log instance 
	 */
	protected static final Log log = LogFactory.getLog(TestController.class);
	private DaoService dao;
	
	/**
	 * @see org.esupportail.commons.web.controllers.Resettable#reset()
	 */
	public void reset() {
		// TODO Auto-generated method stub
	}
	public String test1() {
		UserProfile userProfile = dao.getUserProfile("test");
		if (userProfile != null) {
			System.out.println("userProfile.getUserId --> " + userProfile.getUserId());			
		}
		else {
			System.out.println("userProfile is null (test not found)");
		}
		if (userProfile != null) {
			System.out.println("userProfile.getUserId --> " + userProfile.getUserId());	
			Map<String, CustomContext> ccs = userProfile.getCustomContexts();
			Iterator<String> iter = ccs.keySet().iterator();
			while (iter.hasNext()) {
				String element = (String) iter.next();
				CustomContext cc = ccs.get(element);
				System.out.println("treesize of customContext "+element+" = "+cc.getTreeSize());
			}
		}
		return "OK";
	}

	public String test2(){
		UserProfile userProfile = dao.getUserProfile("test");
		if (userProfile != null) {
			System.out.println("after read");			
			System.out.println("userProfile.getUserId --> " + userProfile.getUserId());			
			System.out.println("userProfile.getUserProfilePK --> " + userProfile.getUserProfilePK());			
		}
		else {
			System.out.println("userProfile is null (test not found)");
		}
		dao.saveUserProfile(userProfile);
		System.out.println("after save");			
		System.out.println("userProfile.getUserId --> " + userProfile.getUserId());			
		System.out.println("userProfile.getUserProfilePK --> " + userProfile.getUserProfilePK());			
		UserProfile userProfile2 = dao.getUserProfile("test");
		if (userProfile2 != null) {
			System.out.println("after second read");			
			System.out.println("userProfile.getUserId --> " + userProfile2.getUserId());			
			System.out.println("userProfile.getUserProfilePK --> " + userProfile2.getUserProfilePK());			
		}
		return "OK";
		
	}
	
	public DaoService getDaoService() {
		return dao;
	}
	public void setDaoService(DaoService daoService) {
		this.dao = daoService;
	}

}
