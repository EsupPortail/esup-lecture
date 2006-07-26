/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.esupportail.lecture.domain.model.Context;
import org.esupportail.lecture.domain.model.ManagedCategory;
import org.esupportail.lecture.domain.model.Category;
import org.esupportail.lecture.domain.model.GlobalSource;
import org.esupportail.lecture.domain.model.Source;
import org.esupportail.lecture.domain.service.FacadeService;

/**
 * Et la javadoc, elle est o� ???
 * @author gbouteil
 *
 */
public class FacadeServiceImplRaymond implements FacadeService {

	public List<Category> getCategories() {
		ArrayList<Category> ret = new ArrayList<Category>();
		Category cat;
		cat = new ManagedCategory();
		cat.setName("Biblioth�ques");
		ret.add(cat);
		cat = new ManagedCategory();
		cat.setName("Vie culturelle");
		ret.add(cat);
		cat = new ManagedCategory();
		cat.setName("Vie de l'ENT");
		ret.add(cat);
		return ret;
	}

	public List<Source> getSources(Category cat) {
		ArrayList<Source> ret = new ArrayList<Source>();
		// Volontairement ici je ne tiens pas compte du parametre pass� � la fonction...
		Source src;
		src = new GlobalSource();
		src.setName("Cin�ma"); 
		ret.add(src);
		src = new GlobalSource();
		src.setName("Th�atre");
		ret.add(src);
		src = new GlobalSource();
		src.setName("Concert");
		ret.add(src);
		src = new GlobalSource();
		src.setName("Danse");
		ret.add(src);
		return ret;
	}
	/**
	 * @deprecated
	 */
	public void loadChannel() {
		
	}

	/**
	 * @deprecated
	 */
	public void newUserSession(){
		
	}

	/**
	 * @deprecated
	 */
	public String channelToString() {
		// TODO Auto-generated method stub
		return null;
	}

	/** 
	 * deprecated
	 */
	public Set<Context> getContexts() {
		// TODO Auto-generated method stub
		return null;
	}
}
