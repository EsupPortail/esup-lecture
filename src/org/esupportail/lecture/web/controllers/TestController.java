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
	private String test = "TEST!!!";
	private DaoService dao;
	
	/**
	 * @see org.esupportail.commons.web.controllers.Resettable#reset()
	 */
	public void reset() {
		// TODO Auto-generated method stub
	}
	public String getTest() {
		UserProfile userProfile = dao.getUserProfile("test");
		if (userProfile != null) {
			System.out.println("userProfile.getUserId --> " + userProfile.getUserId());			
			//delete userProfile test
			//dao.deleteUserProfile(userProfile);
		}
		else {
			System.out.println("userProfile is null (test not found)");
		}
		//set userProfile test
		userProfile = new UserProfile("test");
		//dao.saveUserProfile(userProfile);
		UserProfile userProfile2 = dao.getUserProfile("test");
		if (userProfile2 != null) {
			System.out.println("userProfile.getUserId --> " + userProfile2.getUserId());	
			Map<String, CustomContext> ccs = userProfile2.getCustomContexts();
			Iterator<String> iter = ccs.keySet().iterator();
			while (iter.hasNext()) {
				String element = (String) iter.next();
				CustomContext cc = ccs.get(element);
				System.out.println("treesize of customContext "+element+" = "+cc.getTreeSize());
			}
			//delete userProfile test
//			dao.deleteUserProfile(userProfile2);
		}
		return test;
	}
	public void setTest(String test) {
		this.test = test;
	}
	public DaoService getDaoService() {
		return dao;
	}
	public void setDaoService(DaoService daoService) {
		this.dao = daoService;
	}

}
