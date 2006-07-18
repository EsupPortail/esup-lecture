package org.esupportail.lecture.domain.service;

import java.util.List;

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
		public void startChannel()throws MyException;
		
		
		/**
		 * Service called when a user open a session
		 */
		public void newUserSession() throws MyException;
		/**
		 * Call toString method on channel
		 * @return string return by toString method of channel
		 */
		public String channelToString();
		
		/* ??? A quoi elle sert */
		public List<Category> getCategories();
		/* ??? A quoi elle sert*/
		public List<Source> getSources(Category cat);
}
