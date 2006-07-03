package org.esupportail.lecture.web;

import org.esupportail.lecture.domain.service.LectureService;

public class FacadeService {
	
	private LectureService lectureService;
	
	public LectureService getLectureService() {
		return lectureService;
	}

	public void setLectureService(LectureService lectureService) {
		this.lectureService = lectureService;
	}
	
}
