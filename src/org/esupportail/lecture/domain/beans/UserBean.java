package org.esupportail.lecture.domain.beans;

import org.esupportail.lecture.domain.model.UserProfile;

/**
 * @author bourges
 * used to store user informations
 */
public class UserBean {
	/**
	 * id of source
	 */
	private String uid;
	
	public UserBean(String uid) {
		this.uid = uid;
	}
	public UserBean(UserProfile userProfile) {
		uid = userProfile.getUserId();
	}
	/**
	 * @return id of source
	 */
	public String getUid() {
		return uid;
	}
	/**
	 * @param id
	 */
	public void setUid(String id) {
		this.uid = id;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		String string = "";
		string +=" uid = " + uid.toString()+"\n";
		return string;
	}
	
}
