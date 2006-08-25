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
import org.esupportail.lecture.domain.model.tmp.Item;
import org.esupportail.lecture.domain.model.tmp.SourceRB;
import org.esupportail.lecture.domain.model.tmp.CategoryRB;
import org.esupportail.lecture.domain.service.FacadeService;
import org.esupportail.lecture.utils.exception.FatalException;
import org.esupportail.lecture.utils.exception.MyException;

/**
 * Et la javadoc, elle est o� ???
 * @author gbouteil
 *
 */
public class FacadeServiceImplRaymond implements FacadeService {

	public List<Category> getCategories() {
		ArrayList<Category> ret = new ArrayList<Category>();
		CategoryRB cat;
		cat = new CategoryRB();
		cat.setName("Biblioth�ques");
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
	
	public List<Item> getItems(int sourceID) {
		ArrayList<Item> ret = new ArrayList<Item>();
		Item item;
		item = new Item();
		item.setBody("<p>Du 5 juillet au 30 ao�t : toute r�inscription doit se faire obligatoirement en ligne.</p>" +
				"<img src=\"http://portail0.univ-rennes1.fr/html/htmlStatique/images/carte_etudiant_mini.png\" />" +
				"Du 5 juillet au 30 ao�t 2006 : ouverture des inscriptions universitaires par internet, � partir de votre <b>Espace num�rique de travail </b>onglet<b> Me r�inscrire</b>" +
				"<br /><p>Cas particuliers�:<br />" +
				"- Les �tudiants de <b>l'IUT de Saint-Malo</b> doivent se r�inscrire avant le <b>31 juillet.</b> <br />" +
				"- Les �tudiants qui passent la <b>2�me session en septembre</b>, ne sont pas concern�s par la limite du 30 ao�t <br />" +
				"Durant cette p�riode, r�alisez votre inscription pour l'ann�e universitaire 2006-2007 en quelques clics avec paiement des droits d'inscription et de s�curit� sociale par carte bancaire.<br />" +
				"Cette inscription officialise votre statut d'�tudiant avec l'envoi	de votre carte d'�tudiant.</p>");
		item.setRead(false);
		ret.add(item);
		item = new Item();
		item.setBody("Article 2");
		item.setRead(true);
		ret.add(item);
		item = new Item();
		item.setBody("Article 3");
		item.setRead(false);
		ret.add(item);
		item = new Item();
		item.setBody("Article 4");
		item.setRead(true);
		ret.add(item);
		item = new Item();
		item.setBody("Article 5");
		item.setRead(false);
		ret.add(item);
		return ret;
	}

	private List<SourceRB> getSourcesRB(Category cat) {
		ArrayList<SourceRB> ret = new ArrayList<SourceRB>();
		// Volontairement ici je ne tiens pas compte du parametre pass� � la fonction...
		ret.add(getSource(1));
		ret.add(getSource(2));
		ret.add(getSource(3));
		ret.add(getSource(4));
		return ret;
	}

	public SourceRB getSource(int sourceID) {
		SourceRB src;
		src = new SourceRB();
		if (sourceID == 1) {
			src.setId(1);
			src.setName("Cin�ma"); 
			src.setSelected(true);			
		}
		if (sourceID == 2) {
			src.setId(2);
			src.setName("Th�atre");
			src.setWithUnread(false);
		}
		if (sourceID == 3) {
			src.setId(3);
			src.setName("Concert");
		}
		if (sourceID == 4) {
			src.setId(4);
			src.setName("Danse");
			src.setWithUnread(false);
		}
		return src;
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
