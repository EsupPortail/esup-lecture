/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.web;

import org.esupportail.lecture.domain.service.LectureService;

/**
 * Entry point to access domain service, used by org.esupportail.lecture.web package
 * @author gbouteil
 *
 */
public class FacadeService {
	
	private LectureService lectureService;
	
	/**
	 * Return a LectureService
	 * @return lectureService
	 */
	public LectureService getLectureService() {
		return lectureService;
	}

	/**
	 * Sets a LectureService
	 * @param lectureService
	 */
	public void setLectureService(LectureService lectureService) {
		this.lectureService = lectureService;
	}
	
}
