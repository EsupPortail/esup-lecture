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
import org.esupportail.lecture.domain.model.tmp.Item;
import org.esupportail.lecture.domain.model.tmp.SourceRB;
import org.esupportail.lecture.utils.exception.*;


/**
 * Interface to access domain services.
 * @author gbouteil
 *
 */
public interface FacadeService {
	
		/**
		 * Service called when the all channel need to be loaded or reloaded
		 * (by command line / at channel start)
		 * @exception MyException
		 * @exception FatalException
		 */
		public void loadChannel()throws FatalException,MyException;
		
		/**
		 * Service called when a user open a session
		 * @exception MyException
		 * @exception FatalException
		 */
		public void newUserSession() throws MyException,FatalException;
		
		/**
		 * Service called to reload the channel config
		 * @throws FatalException
		 */
		public void reloadChannelConfig() throws FatalException;
		
		/**
		 * Service called to reload the mapping file
		 * @throws FatalException
		 */
		public void reloadMappingFile()throws FatalException;
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
		
		/**
		 * Returns list of Categories of the current context
		 * @return a List of Categories of the current context
		 * @author bourges
		 */
		public List<Category> getCategories();

		/* ??? A quoi elle sert : doit disparaitre*/
//		public List<Source> getSources(Category cat);

		/**
		 * Returns a Source with a specific ID
		 * @param SourceID ID of Source to return
		 * @return Source
		 * @author bourges
		 */
		public SourceRB getSource(int sourceID);
		
		/**
		 * Returns Items form a Source with a specific ID
		 * @param SourceID ID of Source to return
		 * @return A List of Items
		 * @author bourges
		 */
		public List<Item> getItems(int sourceID);
}
