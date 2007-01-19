/**
 * ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
 * For any information please refer to http://esup-helpdesk.sourceforge.net
 * You may obtain a copy of the licence at http://www.esup-portail.org/license/
 */
package org.esupportail.lecture.web.controllers;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.beans.CategoryBean;
import org.esupportail.lecture.domain.beans.SourceBean;
import org.esupportail.lecture.domain.model.AvailabilityMode;
import org.esupportail.lecture.exceptions.domain.DomainServiceException;
import org.esupportail.lecture.exceptions.domain.InternalExternalException;
import org.esupportail.lecture.exceptions.web.WebException;
import org.esupportail.lecture.web.beans.CategoryWebBean;
import org.esupportail.lecture.web.beans.ContextWebBean;
import org.esupportail.lecture.web.beans.SourceWebBean;
import org.springframework.util.Assert;

/**
 * @author : Raymond 
 */
public class EditController extends twoPanesController {
	/**
	 * Log instance 
	 */
	protected static final Log log = LogFactory.getLog(EditController.class);
	/**
	 * Key used to store the context in virtual session
	 */
	static final String CONTEXT = "contextInEditMode";
	/**
	 * @see org.esupportail.commons.web.controllers.Resettable#reset()
	 */
	public void reset() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see org.esupportail.lecture.web.controllers.AbstractContextAwareController#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		Assert.notNull(getFacadeService(), 
				"property facadeService of class " + this.getClass().getName() + " can not be null");
	}

	/**
	 * JSF action : select a category or a source from the tree, use categoryID and sourceID valued by t:updateActionListener
	 * @return JSF from-outcome
	 */
	public String selectElement() {
		if (log.isDebugEnabled()) log.debug("in selectElement");
		String catID = this.categoryId;
		CategoryWebBean cat = getCategorieByID(catID);
		// set category focused by user as selected category in the context
		ContextWebBean ctx = getContext();
		ctx.setSelectedCategory(cat);
		return "OK";
	}

	/**
	 * JSF action : Change subscrition status of a source
	 * @return JSF from-outcome
	 */
	public String toogleSourceSubcribtion() {
		CategoryWebBean selectedCategory = getContext().getSelectedCategory();
		SourceWebBean src = getSourceByID(selectedCategory, sourceId);
		AvailabilityMode type = null;
//		try {
			if (src.isNotSubscribed()) {
				//TODO (RB) call facadeservice
				//getFacadeService().marckSourceAsSubscribed(getUID(), getContextId(), selectedCategory.getId(), sourceId);
				type = AvailabilityMode.SUBSCRIBED;
			}
			if (src.isSubscribed()) {
				//TODO (RB) call facadeservice
				//getFacadeService().marckSourceAsUnsubscribed(getUID(), getContextId(), selectedCategory.getId(), sourceId);
				type = AvailabilityMode.NOTSUBSCRIBED;
			}
//		} catch (InternalExternalException e) {
//			throw new WebException("Error in getContext", e);
//		}
		if (type != null) {
			src.setType(type);
		}
		//TODO (RB) refresh context for normal mode !!!
		return "OK";		
	}
	
	/**
	 * @return the current selected category
	 */
	public CategoryWebBean getSelectedCat() {
		CategoryWebBean ret = null;
		ContextWebBean ctx = getContext();
		ret = ctx.getSelectedCategory();
		return ret;
	}
	
	/**
	 * Controller constructor
	 */
	public EditController() {
		super();
		//instatiate a virtual session
		virtualSession = new VirtualSession();
	}

	/**
	 * @param categoryBean
	 * @return list of visible sources
	 * @throws DomainServiceException 
	 */
	protected List<SourceBean> getSources(CategoryBean categoryBean) throws DomainServiceException {
		//this method need to be overwrite in edit controller
		List<SourceBean> sources = getFacadeService().getVisibleSources(getUID(), categoryBean.getId());
		return sources;
	}
	
}
