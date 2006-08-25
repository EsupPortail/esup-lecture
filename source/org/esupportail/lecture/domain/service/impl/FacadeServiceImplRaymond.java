/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.esupportail.lecture.domain.model.Category;
import org.esupportail.lecture.domain.model.Source;
import org.esupportail.lecture.domain.model.Context;
import org.esupportail.lecture.domain.model.GlobalSource;
import org.esupportail.lecture.domain.model.tmp.SourceRB;
import org.esupportail.lecture.domain.model.tmp.CategoryRB;
import org.esupportail.lecture.domain.service.FacadeService;
import org.esupportail.lecture.utils.exception.FatalException;
import org.esupportail.lecture.utils.exception.MyException;

/**
 * Et la javadoc, elle est où ???
 * @author gbouteil
 *
 */
public class FacadeServiceImplRaymond implements FacadeService {

	public List<Category> getCategories() {
		ArrayList<Category> ret = new ArrayList<Category>();
		CategoryRB cat;
		cat = new CategoryRB();
		cat.setName("Bibliothèques");
		cat.setSelected(false);
		cat.setExpanded(false);
		ret.add(cat);
		cat = new CategoryRB();
		cat.setName("Vie culturelle");
		cat.setSelected(true);
		cat.setExpanded(true);
		cat.setSources(getSourcesRB(cat));
		ret.add(cat);
		cat = new CategoryRB();
		cat.setName("Vie de l'ENT");
		cat.setSelected(false);
		cat.setExpanded(false);
		ret.add(cat);
		return ret;
	}

	public List<Source> getSources(Category cat) {
		return null;
	}

	public List<SourceRB> getSourcesRB(Category cat) {
		ArrayList<SourceRB> ret = new ArrayList<SourceRB>();
		// Volontairement ici je ne tiens pas compte du parametre passé à la fonction...
		SourceRB src;
		src = new SourceRB();
		src.setName("Cinéma"); 
		src.setSelected(true);
		ret.add(src);
		src = new SourceRB();
		src.setName("Théatre");
		src.setWithUnread(false);
		ret.add(src);
		src = new SourceRB();
		src.setName("Concert");
		ret.add(src);
		src = new SourceRB();
		src.setName("Danse");
		src.setWithUnread(false);
		ret.add(src);
		return ret;
	}

	public void loadChannel() throws FatalException, MyException {
		// TODO Auto-generated method stub
		
	}

	public void newUserSession() throws MyException, FatalException {
		// TODO Auto-generated method stub
		
	}

	public void reloadChannelConfig() throws FatalException {
		// TODO Auto-generated method stub
		
	}

	public void reloadMappingFile() throws FatalException {
		// TODO Auto-generated method stub
		
	}

	public String channelToString() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<Context> getContexts() {
		// TODO Auto-generated method stub
		return null;
	}
}
