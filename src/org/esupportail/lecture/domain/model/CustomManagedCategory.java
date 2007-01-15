/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.DomainTools;
import org.esupportail.lecture.domain.ExternalService;
import org.esupportail.lecture.exceptions.domain.CategoryNotLoadedException;
import org.esupportail.lecture.exceptions.domain.CategoryNotVisibleException;
import org.esupportail.lecture.exceptions.domain.CategoryProfileNotFoundException;

/**
 * Customizations on a managedCategoryProfile for a user Profile
 * @author gbouteil
 * @see CustomCategory
 *
 */
/**
 * @author gbouteil
 *
 */
public class CustomManagedCategory extends CustomCategory {

	/*
	 ************************** PROPERTIES *********************************/	
	/**
	 * logger
	 */
	protected static final Log log = LogFactory.getLog(CustomManagedCategory.class);

	/**
	 * Subscriptions of CustomManagedSources
	 */
	private Map<String,CustomManagedSource> subscriptions = new Hashtable<String,CustomManagedSource>();
	
	
	/**
	 * CategoryProfile associated to this customManagedCategory
	 */
	private ManagedCategoryProfile categoryProfile;
	
	/*
	 ************************** INIT *********************************/	

	/**
	 * @param profileId 
	 * @param user
	 */
	protected CustomManagedCategory(String profileId,UserProfile user){
		super(profileId,user);
		if (log.isDebugEnabled()){
			log.debug("CustomManagedCategory("+profileId+","+user.getUserId()+")");
		}
	}

	/**
	 * default constructor
	 */
	protected CustomManagedCategory(){
		super();
		if (log.isDebugEnabled()){
			log.debug("CustomManagedCategory()");
		}
	}

	/*
	 ************************** METHODS *********************************/	
	
	/**
	 * Return the list of sorted customSources displayed by this customCategory
	 * @param ex access to external service 
	 * @return the list of customSource
	 * @throws CategoryProfileNotFoundException
	 * @throws CategoryNotVisibleException
	 * @throws CategoryNotLoadedException
	 * @see org.esupportail.lecture.domain.model.CustomCategory#getSortedCustomSources(org.esupportail.lecture.domain.ExternalService)
	 */
	@Override
	public List<CustomSource> getSortedCustomSources(ExternalService ex) throws CategoryProfileNotFoundException, CategoryNotVisibleException, CategoryNotLoadedException {
		if (log.isDebugEnabled()){
			log.debug("id="+super.getElementId()+" - getSortedCustomSources(externalService)");
		}
	// TODO (GB later) à redéfinir avec les custom personnal category : en fonction de l'ordre d'affichage peut etre.
		
		ManagedCategoryProfile profile = getProfile();
		try {
			profile.updateCustom(this,ex);
		} catch (CategoryNotLoadedException e) {
			userProfile.updateCustomContextsForOneManagedCategory(getElementId(),ex);
			profile.updateCustom(this,ex);
		}
		
		DomainTools.getDaoService().updateCustomCategory(this);
		DomainTools.getDaoService().updateUserProfile(super.getUserProfile());
		
		List<CustomSource> listSources = new Vector<CustomSource>();
		for(CustomSource customSource : subscriptions.values()){
			listSources.add(customSource);
			log.trace("Add source");
		}
	
		return listSources;
	}
	

	/**
	 * Add a subscription source to this custom category (if no exists, else do nothing) 
	 * and creates the corresponding customManagedSource with the given managedSourceProfile
	 * This new customManagedSource is also added to the userProfile (owner of all customElements)
	 * @param managedSourceProfile the source profile to subscribe
	 */
	protected void addSubscription(ManagedSourceProfile managedSourceProfile) {
		if (log.isDebugEnabled()){
			log.debug("id="+super.getElementId()+" - addSubscription("+managedSourceProfile.getId()+")");
		}
		String profileId = managedSourceProfile.getId();
		
		if (!subscriptions.containsKey(profileId)){
			CustomManagedSource customManagedSource = new CustomManagedSource(managedSourceProfile, getUserProfile());
			subscriptions.put(profileId,customManagedSource);
			getUserProfile().addCustomSource(customManagedSource);
		}
	}

	/**
	 * remove a CustomManagedSource displayed in this CustomManagedCategory
	 * and also removes it from the userProfile
	 * Used to remove a subscription or an importation indifferently
	 * @param profile the managedSourceProfile associated to the CustomManagedSource to remove
	 */
	@Override
	protected void removeCustomManagedSource(ManagedSourceProfile profile) {
		if (log.isDebugEnabled()){
			log.debug("id="+super.getElementId()+" - removeCustomManagedSource("+profile.getId()+")");
		}
		String profileId = profile.getId();
		CustomSource cs = subscriptions.get(profileId);
		if (cs != null) {
			subscriptions.remove(profile.getId());
			getUserProfile().removeCustomSource(profile.getId());
			// TODO (gb later) il faudra supprimer toutes les références à cette cmc
			// (importations dans d'autre customContext)
		}
	}
	
	/**
	 * Returns the ManagedCategoryProfile associated to this CustomManagedCategory
	 * @see org.esupportail.lecture.domain.model.CustomCategory#getProfile()
	 */
	@Override
	public ManagedCategoryProfile getProfile() throws CategoryProfileNotFoundException {
		if (log.isDebugEnabled()){
			log.debug("id="+super.getElementId()+" - getProfile()");
		}
		if (categoryProfile == null){
			Channel channel = DomainTools.getChannel();
			categoryProfile = channel.getManagedCategoryProfile(getElementId());
		}
		return categoryProfile;		
	}
	
	/*
	 ************************** ACCESSORS *********************************/	


	/**
	 * @return source subcription of this category
	 */
	private Map<String, CustomManagedSource> getSubscriptions() {
		return subscriptions;
	}

	/**
	 * @param subscriptions
	 */
	private void setSubscriptions(
			Map<String, CustomManagedSource> subscriptions) {
		this.subscriptions = subscriptions;
	}
	



	
}
