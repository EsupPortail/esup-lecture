package org.esupportail.lecture.domain.model;

import org.esupportail.lecture.exceptions.CategoryNotLoadedException;
import org.esupportail.lecture.exceptions.ElementNotLoadedException;
import org.esupportail.lecture.exceptions.ContextNotFoundException;
import org.esupportail.lecture.exceptions.ManagedCategoryProfileNotFoundException;

public interface CustomElement {

	public String getName() throws ContextNotFoundException, ManagedCategoryProfileNotFoundException, ElementNotLoadedException;
	

}
