/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.web;

import java.util.Iterator;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;


import org.esupportail.lecture.domain.model.Context;
/**
 * Essais
 * @author gbouteil
 *
 */
public class HomeContextBean {
	private List<ContextWeb> contextWebs;
	private FacadeWeb facadeWeb;
	
	public void setFacadeService(FacadeWeb facadeService) {
		this.facadeWeb = facadeService;
	}
	
	public List<ContextWeb> getContextWebs() {
		// calculer ça à chaque fois qu'on le demande ou le garder en mémoire ?
		// mieux de le mettre dans le constructeur
		contextWebs = new ArrayList<ContextWeb>();
		Set<Context> contexts = facadeWeb.getLectureService().getContexts();
		Iterator iterator = contexts.iterator();
		for (Context c = null; iterator.hasNext();){
			c=(Context)iterator.next();
			ContextWeb cw = new ContextWeb(c);
			contextWebs.add(cw);
		}
		
		return contextWebs;
	}


	/**
	 * @param contextWebs The contextWebs to set.
	 */
	protected void setContextWebs(List<ContextWeb> contextWebs) {
		this.contextWebs = contextWebs;
	}

	/**
	 * @return Returns the facadeService.
	 */
	protected FacadeWeb getFacadeService() {
		return facadeWeb;
	}
	



}
