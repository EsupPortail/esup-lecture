/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.service;

import java.util.Set;
import java.util.List;

import org.esupportail.lecture.domain.model.Context;
import org.esupportail.lecture.domain.model.Category;
import org.esupportail.lecture.domain.model.Source;
import org.esupportail.lecture.utils.exception.MyException;


/**
 * Interface to access domain services.
 * @author gbouteil
 *
 */
public interface FacadeService {
	
		/**
		 * Service called when the channel may be
		 * reloaded (by command line / at channel start)
		 * @exception MyException
		 */
		public void loadChannel()throws MyException;
		
		/**
		 * Service called when a user open a session
		 * @exception MyException
		 */
		public void newUserSession() throws MyException;
		/**
		 * Call toString method on channel
		 * @return string return by toString method of channel
		 */
		public String channelToString();
		
		/**
		 * Returns list of contexts (full of data) defiend in the channel
		 * @return a set of contexts defined in the channel
		 */
		public Set<Context> getContexts();
		
		/* ??? A quoi elle sert : doit disparaitre*/
		public List<Category> getCategories();
		/* ??? A quoi elle sert : doit disparaitre*/
		public List<Source> getSources(Category cat);
}
