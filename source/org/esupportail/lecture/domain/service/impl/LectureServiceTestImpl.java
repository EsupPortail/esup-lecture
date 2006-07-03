package org.esupportail.lecture.domain.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.esupportail.lecture.domain.model.Category;
import org.esupportail.lecture.domain.model.Source;
import org.esupportail.lecture.domain.service.LectureService;

public class LectureServiceTestImpl implements LectureService {

	public List<Category> getCategories() {
		ArrayList<Category> ret = new ArrayList<Category>();
		Category cat;
		cat = new Category();
		cat.setName("Bibliothèques");
		ret.add(cat);
		cat = new Category();
		cat.setName("Vie culturelle");
		ret.add(cat);
		cat = new Category();
		cat.setName("Vie de l'ENT");
		ret.add(cat);
		return ret;
	}

	public List<Source> getSources(Category cat) {
		ArrayList<Source> ret = new ArrayList<Source>();
		// Volontairement ici je ne tiens pas compte du parametre passé à la fonction...
		Source src;
		src = new Source();
		src.setName("Cinéma");
		ret.add(src);
		src = new Source();
		src.setName("Théatre");
		ret.add(src);
		src = new Source();
		src.setName("Concert");
		ret.add(src);
		src = new Source();
		src.setName("Danse");
		ret.add(src);
		return ret;
	}

}
