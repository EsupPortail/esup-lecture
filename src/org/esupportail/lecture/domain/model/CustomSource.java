package org.esupportail.lecture.domain.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.DomainTools;
import org.esupportail.lecture.domain.ExternalService;
import org.esupportail.lecture.exceptions.domain.CategoryProfileNotFoundException;
import org.esupportail.lecture.exceptions.domain.ComputeItemsException;
import org.esupportail.lecture.exceptions.domain.ElementNotLoadedException;
import org.esupportail.lecture.exceptions.domain.MappingNotFoundException;
import org.esupportail.lecture.exceptions.domain.SourceNotLoadedException;
import org.esupportail.lecture.exceptions.domain.Xml2HtmlException;
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
	/**
	 * Database Primary Key
	 */
	private long customSourcePK;
	
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

	/**
	 * Constructor
	 * @param user owner of this customSource
	 */
	public CustomSource(){
		if (log.isDebugEnabled()){
			log.debug("CustomSource()");
		}
		readItems = new HashSet<String>();
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
	 * @throws Xml2HtmlException 
	 * @throws ComputeItemsException 
	 * @throws MappingNotFoundException 
	 * @throws SourceNotLoadedException 
	 * @throws ElementNotLoadedException
	 * @throws SourceNotLoadedException
	 */
	public List<Item> getItems(ExternalService ex) 
		throws SourceNotLoadedException, MappingNotFoundException, ComputeItemsException, Xml2HtmlException  {
		if (log.isDebugEnabled()){
			log.debug("getItems(externalService)");
		}
		return getProfile().getItems(ex);
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

	/**
	 * @return
	 */
	public long getCustomSourcePK() {
		return customSourcePK;
	}

	/**
	 * @param customSourcePK - - database Primary Key
	 */
	public void setCustomSourcePK(long customSourcePK) {
		this.customSourcePK = customSourcePK;
	}

	/**
	 * @param userProfile
	 */
	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}

	/**
	 * @return profileID os this custom source
	 */
	public String getProfileId() {
		return profileId;
	}

	/**
	 * @param profileId
	 */
	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}

	/**
	 * @return a set of read items ID
	 */
	public Set<String> getReadItems() {
		return readItems;
	}

	/**
	 * @param readItems
	 */
	public void setReadItems(Set<String> readItems) {
		this.readItems = readItems;
	}
	

	
}
