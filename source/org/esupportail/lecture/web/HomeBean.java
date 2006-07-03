package org.esupportail.lecture.web;

import java.util.List;
import org.esupportail.lecture.domain.model.Category;

public class HomeBean {
	private List<Category> categories;
	private FacadeService facadeService;
	
	public void setFacadeService(FacadeService facadeService) {
		this.facadeService = facadeService;
	}
	
	public List<Category> getCategories() {
		this.categories = facadeService.getLectureService().getCategories();
		return this.categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}
}
