package org.esupportail.lecture.domain.service;

import java.util.Set;
import java.util.List;

import org.esupportail.lecture.domain.model.Context;
import org.esupportail.lecture.domain.model.Category;
import org.esupportail.lecture.domain.model.Source;
import org.esupportail.lecture.utils.exception.MyException;

public interface LectureService {
		/* démarrage du canal + ligne de commande */
		/**
		 * Service called when the channel starts and 
		 * when config must be reloaded by command line
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
		 * @return
		 */
		public Set<Context> getContexts();
		
		/* ??? A quoi elle sert */
		public List<Category> getCategories();
		/* ??? A quoi elle sert*/
		public List<Source> getSources(Category cat);
}
