/**
 * ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
 * For any information please refer to http://esup-helpdesk.sourceforge.net
 * You may obtain a copy of the licence at http://www.esup-portail.org/license/
 */
package org.esupportail.lecture.web.controllers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.beans.CategoryBean;
import org.esupportail.lecture.domain.beans.ContextBean;
import org.esupportail.lecture.domain.beans.ItemBean;
import org.esupportail.lecture.domain.beans.SourceBean;
import org.esupportail.lecture.domain.beans.UserBean;
import org.esupportail.lecture.web.beans.CategoryWebBean;
import org.esupportail.lecture.web.beans.ContextWebBean;
import org.esupportail.lecture.web.beans.ItemWebBean;
import org.esupportail.lecture.web.beans.SourceWebBean;
import org.esupportail.lecture.domain.FacadeService;
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
	 * Access to multiple instance of channel in a one session (contexts)
	 */
	private VirtualSession virtualSession;
	/**
	 * UID of the connected user
	 */
	private String UID = null;

	/**
	 * @see org.esupportail.commons.web.controllers.Resettable#reset()
	 */
	public void reset() {
		// TODO Auto-generated method stub
	}

	/**
	 * To display information about the custom Context of the connected user
	 * @return Returns the context.
	 */
	public ContextWebBean getContext() {
		ContextWebBean context = (ContextWebBean) virtualSession.get(CONTEXT);
		if (context == null){
			if (log.isDebugEnabled()) 
				log.debug ("getContext() :  Context not yet loaded : loading...");
			//We evalute the context and we put it in the virtual session
			context = new ContextWebBean();
			String contextId = getFacadeService().getCurrentContextId(); 
			ContextBean contextBean = getFacadeService().getContext(getUID(), contextId);
			context.setName(contextBean.getName());
			context.setId(contextBean.getId());
			List<CategoryBean> categories = getFacadeService().getVisibleCategories(getUID(), contextId);
			List<CategoryWebBean> categoriesWeb = new ArrayList<CategoryWebBean>();
			if (categories != null) {
				Iterator<CategoryBean> iter = categories.iterator();
				while (iter.hasNext()) {
					CategoryBean categoryBean = iter.next();
					CategoryWebBean categoryWebBean =  new CategoryWebBean();
					categoryWebBean.setId(categoryBean.getId());
					categoryWebBean.setName(categoryBean.getName());
					//find sources in this category
					List<SourceBean> sources = getFacadeService().getAvailableSources(getUID(), categoryBean.getId());
					List<SourceWebBean> sourcesWeb = new ArrayList<SourceWebBean>();
					if (sources != null) {
						Iterator<SourceBean> iter2 = sources.iterator();
						while (iter2.hasNext()) {
							SourceBean sourceBean = iter2.next();
							SourceWebBean sourceWebBean = new SourceWebBean();
							sourceWebBean.setId(sourceBean.getId());
							sourceWebBean.setName(sourceBean.getName());
							sourcesWeb.add(sourceWebBean);
						}
					}
					categoryWebBean.setSources(sourcesWeb);
					categoriesWeb.add(categoryWebBean);
				}
			}
			context.setCategories(categoriesWeb);
			virtualSession.put(CONTEXT,context);
		}
		return context;
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
	 * @return the connected user UID
	 */
	private String getUID() {
		if (UID == null) {
			//init the user
			String userId = getFacadeService().getConnectedUserId();
			UserBean userBean = getFacadeService().getConnectedUser(userId);
			UID = userBean.getUid();
		}
		return UID;
	}

	/**
	 * Controller constructor
	 */
	public EditController() {
		super();
		//instatiate a virtual session
		virtualSession = new VirtualSession();
	}

}
