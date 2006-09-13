/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.service;

import java.util.Hashtable;
import java.util.List;

import org.esupportail.lecture.domain.model.Context;
import org.esupportail.lecture.domain.model.Category;
//import org.esupportail.lecture.domain.model.CustomContext;
//import org.esupportail.lecture.domain.model.Source;
//import org.esupportail.lecture.domain.model.UserProfile;
//import org.esupportail.lecture.domain.model.tmp.Item;
//import org.esupportail.lecture.domain.model.tmp.SourceRB;
import org.esupportail.lecture.utils.exception.*;
import org.esupportail.lecture.beans.ContextUserBean;
import org.esupportail.lecture.beans.UserBean;


/**
 * Interface to access domain services.
 * @author gbouteil
 *
 */
public interface DomainService {
	
		/**
		 * Service called when the all channel need to be loaded or reloaded
		 * (by command line / at channel start)
		 * @exception MyException
		 * @exception FatalException
		 */
		public void loadChannel()throws FatalException,MyException;
			
		/**
		 * Call toString method on channel
		 * @return string return by toString method of channel
		 */
		public String channelToString();
		
		/**
		 * Returns list of contexts (full of data) defined in the channel
		 * @return a set of contexts defined in the channel
		 */
		public Hashtable<String,Context> getContexts();
		
		/**
		 * Returns list of Categories of the current context
		 * @return a List of Categories of the current context
		 * @author bourges
		 */
		public List<Category> getCategories();
		
		/**
		 * A UserBean contains informations about a user profile to be displayed
		 * @param userId identifiant of the user
		 * @return a UserBean
		 * @see org.esupportail.lecture.beans.UserBean
		 */
		public UserBean getUserBean(String userId)  ;
		
		/**
		 * This bean contains informations about a context to be displayed
		 * according to a user profile identified by "userId"
		 * @param userId identifiant of the user
		 * @param contextId identifiant of the context
		 * @return a ContextUserBean
		 * @exception ErrorException
		 * @see org.esupportail.lecture.beans.ContextUserBean
		 */
		public ContextUserBean getContextUserBean(String userId,String contextId) throws ErrorException;


		

}
