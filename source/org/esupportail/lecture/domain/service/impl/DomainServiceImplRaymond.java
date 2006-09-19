/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.service.impl;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.esupportail.lecture.domain.model.Category;
import org.esupportail.lecture.domain.model.Channel;
import org.esupportail.lecture.domain.model.CustomContext;
import org.esupportail.lecture.domain.model.CustomManagedCategory;
import org.esupportail.lecture.domain.model.ManagedCategoryProfile;
import org.esupportail.lecture.domain.model.Source;
import org.esupportail.lecture.domain.model.Context;
import org.esupportail.lecture.domain.model.UserProfile;
import org.esupportail.lecture.domain.model.tmp.Item;
import org.esupportail.lecture.domain.model.tmp.SourceRB;
import org.esupportail.lecture.domain.model.tmp.CategoryRB;
import org.esupportail.lecture.domain.service.DomainService;
import org.esupportail.lecture.domain.service.FacadeService;
import org.esupportail.lecture.domain.service.PortletService;
import org.esupportail.lecture.utils.exception.ErrorException;
import org.esupportail.lecture.utils.exception.FatalException;
import org.esupportail.lecture.utils.exception.MyException;
import org.esupportail.lecture.beans.CategoryUserBean;
import org.esupportail.lecture.beans.ContextUserBean;
import org.esupportail.lecture.beans.UserBean;

/**
 * Et la javadoc, elle est où ???
 * @author gbouteil
 *
 */
public class DomainServiceImplRaymond implements DomainService {
	//List<Item> items;
	//List<SourceRB> sources;
	List<Category> categories;
	static Channel myChannel; 
	static FacadeService facadeService;

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
		myChannel.startup();
		
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

	public String getUserIdAttribute() {
		// TODO Auto-generated method stub
		return null;
	}

	

	public UserBean getUserBean() {
		// TODO Auto-generated method stub
		return null;
	}

	public ContextUserBean getContextUserBean() {
		// TODO Auto-generated method stub
		return null;
	}

	public ContextUserBean getContextUserBean(String userId) throws ErrorException {
		// TODO Auto-generated method stub
		return null;
	}

	public ContextUserBean getContextUserBean(UserProfile userProfile) throws ErrorException {
		// TODO Auto-generated method stub
		return null;
	}

	public UserProfile getUserProfile(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	public UserBean getUserBean(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	public ContextUserBean getContextUserBean(String userId, String contextId) throws ErrorException {
		/* Get context */
		Context context = myChannel.getContext(contextId);
		if (context == null) {
			throw new ErrorException("Context "+contextId+" is not defined in this channel");
		}
		/* Get Managed category profiles with their categories */ 
		Set<ManagedCategoryProfile> fullManagedCategoryProfiles =  context.getFullManagedCategoryProfiles();
		
		/* Get user profile and customContext */
		UserProfile userProfile = myChannel.getUserProfile(userId);
		CustomContext customContext = userProfile.getCustomContext(contextId);
		//evaluateVisibilityOnCategories(fullManagedCategoryProfiles,customContext);
		ContextUserBean contextUserBean = new ContextUserBean();
		contextUserBean.setName(context.getName());
		contextUserBean.setDescription(context.getDescription());
		contextUserBean.setId(contextId);
		contextUserBean.setTest(customContext.test);
	
		/* recuperer les categories à afficher */
		// TODO afficher les categories
		Enumeration enumeration= customContext.getCustomCategories();
		while (enumeration.hasMoreElements()) {
			CustomManagedCategory element = (CustomManagedCategory) enumeration.nextElement();
			CategoryUserBean categoryUserBean = new CategoryUserBean();
			categoryUserBean.setName(element.getCategoryProfile().getName());
			contextUserBean.addCategoryUserBean(categoryUserBean) ;
		}
		
		
		//return contextUserBean;		
		return null;
	}
	
	
	private void evaluateVisibilityOnCategories(
			Set<ManagedCategoryProfile> fullManagedCategoryProfiles,
			CustomContext customContext) {
			
			PortletService portletService = facadeService.getPortletService();
			
			Iterator iterator = fullManagedCategoryProfiles.iterator();
			while (iterator.hasNext()) {
				ManagedCategoryProfile mcp = (ManagedCategoryProfile) iterator.next();
				mcp.evaluateVisibilityAndUpdateCustomContext(portletService,customContext);
			}
		}

	/**
	 * @return Returns the myChannel.
	 */
	public Channel getMyChannel() {
		return myChannel;
	}

	/**
	 * @param myChannel The myChannel to set.
	 */
	public void setMyChannel(Channel myChannel) {
		this.myChannel = myChannel;
	}

	public FacadeService getFacadeService() {
		return facadeService;
	}

	/**
	 * @param facadeService The portletService to set.
	 */
	public void setFacadeService(FacadeService facadeService) {
		this.facadeService = facadeService;
	}

}
