package org.esupportail.lecture.domain.service.impl;
import java.util.List;

import org.esupportail.lecture.domain.model.Category;
import org.esupportail.lecture.domain.model.Source;
import org.esupportail.lecture.domain.model.Channel;
import org.esupportail.lecture.domain.service.LectureService;
import org.esupportail.lecture.utils.exception.*;
/**
 * Implémentation des services pour le test 
 * Utilisée par Gwénaëlle
 * @author gbouteil
 *
 */
public class LectureServiceImplGwe implements LectureService {

	Channel myChannel = new Channel();
	/** 
	 * @see org.esupportail.lecture.domain.service.LectureService#startChannel()
	 */
	public void startChannel() throws MyException {
		myChannel.startup();
	}

	/**
	 * @see org.esupportail.lecture.domain.service.LectureService#newUserSession()
	 */
	public void newUserSession() throws MyException {
		myChannel.startup();
		// TODO le reste à propos du user
	}

	/**
	 * @see org.esupportail.lecture.domain.service.LectureService#channelToString()
	 */
	public String channelToString(){
		return myChannel.toString();
	}
	
	/**
	 * @deprecated
	 */
	public List<Category> getCategories() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @deprecated
	 */
	public List<Source> getSources(Category cat) {
		// TODO Auto-generated method stub
		return null;
	}

		

}
