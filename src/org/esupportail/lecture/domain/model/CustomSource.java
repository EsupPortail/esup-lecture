package org.esupportail.lecture.domain.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

	/**
	 * Id of the source refered by this
	 */
	private String sourceId;
	/**
	 * The userprofile parent 
	 */
	private UserProfile userProfile;
	/**
	 * Set of read item by User
	 */
	private Set<String> readItems;
	
	/**
	 * id used by database
	 */
	private int id;
	
	/* 
	 ************************** INIT **********************************/
	
	/**
	 * Constructor
	 * @param user owner of this customSource
	 */
	public CustomSource(SourceProfile profile,UserProfile user){
		readItems = new HashSet<String>();
		userProfile = user;
		sourceId = profile.getId();
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
		return getProfile().getName();
	}
	
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
		return sourceId;
	}
	
//	 TODO (GB) à retirer : pour les tests	
	public String getItemXPath() throws SourceNotLoadedException {
		return getProfile().getElement().getItemXPath();
	}
//	 TODO (GB) à retirer : pour les tests	
	public String getXslt() throws SourceNotLoadedException {
		return getProfile().getElement().getXsltURL();
	}
	
	/**
	 * @param externalService
	 * @return a list of Items contained in source refered by this
	 * @throws ElementNotLoadedException
	 * @throws SourceNotLoadedException
	 */
	public List<Item> getItems(ExternalService externalService) throws ElementNotLoadedException, SourceNotLoadedException {
		return getProfile().getItems(externalService);
	}
	
	/**
	 * @param itemId id of the item to set read
	 */
	public void setItemAsRead(String itemId) {
		readItems.add(itemId);	
	}
	/**
	 * @param itemId id to the item to set unread
	 */
	public void setItemAsUnRead(String itemId) {
		readItems.remove(itemId);
	}
	/**
	 * @param itemId id of the item
	 * @return true if the item is read
	 */
	public boolean isItemRead(String itemId) {
		return readItems.contains(itemId);
	}

	/**
	 * @see org.esupportail.lecture.domain.model.CustomElement#getId()
	 */
	public int getId(){
		return id;
	}
	
	/**
	 * @see org.esupportail.lecture.domain.model.CustomElement#setId(int)
	 */
	public void setId(int id){
		this.id = id;
	}
	
}
