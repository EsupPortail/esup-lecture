/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.service.impl;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.esupportail.lecture.domain.model.Category;
import org.esupportail.lecture.domain.model.CustomContext;
import org.esupportail.lecture.domain.model.Source;
import org.esupportail.lecture.domain.model.Context;
import org.esupportail.lecture.domain.model.tmp.Item;
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
	//List<Item> items;
	//List<SourceRB> sources;
	List<Category> categories;

	public List<Category> getCategories() {
		return categories;
	}

//	public List<Item> getItems(int sourceID) {
//		// Volontairement ici je ne tiens pas compte du parametre passé à la fonction...		
//		return getItems();
//	}

//	private List<SourceRB> getSourcesRB(Category cat) {
//		ArrayList<SourceRB> ret = new ArrayList<SourceRB>();
//		// Volontairement ici je ne tiens pas compte du parametre passé à la fonction...
//		ret.add(getSource(1));
//		ret.add(getSource(2));
//		ret.add(getSource(3));
//		ret.add(getSource(4));
//		return ret;
//	}

//	public SourceRB getSource(int sourceID) {
//		SourceRB ret = null;
//		List<SourceRB> sources = getSources();
//		if (sources != null) {
//			Iterator<SourceRB> ite = sources.iterator();
//			while (ite.hasNext()) {
//				SourceRB source = ite.next();
//				if (source.getId() == sourceID) {
//					ret=source;
//				}
//			}			
//		}
//		return ret;
//	}

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

	public Hashtable<String,Context> getContexts() {
		// TODO Auto-generated method stub
		return null;
	}

    // For spring injection
	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public String getUserName() {
		// TODO Auto-generated method stub
		return null;
	}

	public CustomContext getCustomContext() {
		// TODO Auto-generated method stub
		return null;
	}
}
