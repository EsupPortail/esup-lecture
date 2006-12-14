package org.esupportail.lecture.domain.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.DomainTools;
import org.esupportail.lecture.domain.ExternalService;
import org.esupportail.lecture.exceptions.CategoryProfileNotFoundException;
import org.esupportail.lecture.exceptions.ElementNotLoadedException;
import org.esupportail.lecture.exceptions.SourceNotLoadedException;
/**
 * Customizations on a Source 
 * @author gbouteil
 *
 */
public abstract class CustomSource implements CustomElement {

	protected static final Log log = LogFactory.getLog(CustomSource.class);
	/**
	 * Id of the source refered by this
	 */
	private String profileId;
	/**
	 * The userprofile parent 
	 */
	private UserProfile userProfile;
	/**
	 * Set of read item by User
	 */
	private Set<String> readItems;
	
	/* 
	 ************************** INIT **********************************/
	
	/**
	 * Constructor
	 * @param user owner of this customSource
	 */
	public CustomSource(SourceProfile profile,UserProfile user){
		if (log.isDebugEnabled()){
			log.debug("CustomSource("+profile.getId()+","+user.getUserId()+")");
		}
		readItems = new HashSet<String>();
		userProfile = user;
		profileId = profile.getId();
	}

	/* 
	 ************************** METHODS **********************************/
	
	/**
	 * @return profile of element refered by this
	 */
	public abstract SourceProfile getProfile();
	

	/**
	 * @see org.esupportail.lecture.domain.model.CustomElement#getName()
	 */
	public String getName(){
		if (log.isDebugEnabled()){
			log.debug("getName()");
		}
		return getProfile().getName();
	}
	
	/**
	 * @param externalService
	 * @return a list of Items contained in source refered by this
	 * @throws ElementNotLoadedException
	 * @throws SourceNotLoadedException
	 */
	public List<Item> getItems(ExternalService externalService) throws ElementNotLoadedException, SourceNotLoadedException {
		if (log.isDebugEnabled()){
			log.debug("getItems(externalService)");
		}
		return getProfile().getItems(externalService);
	}
	
	/**
	 * @param itemId id of the item to set read
	 */
	public void setItemAsRead(String itemId) {
		if (log.isDebugEnabled()){
			log.debug("setItemAsRead("+itemId+")");
		}
		readItems.add(itemId);	
		DomainTools.getDaoService().updateCustomSource(this);
	}
	/**
	 * @param itemId id to the item to set unread
	 */
	public void setItemAsUnRead(String itemId) {
		if (log.isDebugEnabled()){
			log.debug("setItemAsUnRead("+itemId+")");
		}
		readItems.remove(itemId);
		DomainTools.getDaoService().updateCustomSource(this);
	}
	/**
	 * @param itemId id of the item
	 * @return true if the item is read
	 */
	public boolean isItemRead(String itemId) {
		if (log.isDebugEnabled()){
			log.debug("isItemRead("+itemId+")");
		}
		return readItems.contains(itemId);
	}

	
	/* 
	 ************************** ACCESSORS **********************************/
	/**
	 * @see org.esupportail.lecture.domain.model.CustomElement#getUserProfile()
	 */
	public UserProfile getUserProfile(){
		return userProfile;
	}
	
	/**
	 * @see org.esupportail.lecture.domain.model.CustomElement#getElementId()
	 */
	public String getElementId(){
		return profileId;
	}
	
//	
//	public String getItemXPath() throws SourceNotLoadedException {
//		return getProfile().getElement().getItemXPath();
//	}
//	
//	public String getXslt() throws SourceNotLoadedException {
//		return getProfile().getElement().getXsltURL();
//	}
	

	
}
