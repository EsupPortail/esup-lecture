package org.esupportail.lecture.domain.service;

import java.util.List;

import org.esupportail.lecture.domain.model.Category;
import org.esupportail.lecture.domain.model.Source;

public interface LectureService {
	List<Category> getCategories();
	List<Source> getSources(Category cat);
}
