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
import org.esupportail.lecture.exceptions.domain.ComputeFeaturesException;
import org.esupportail.lecture.exceptions.domain.SourceNotVisibleException;
import org.esupportail.lecture.exceptions.domain.SourceObligedException;
import org.esupportail.lecture.exceptions.domain.SourceProfileNotFoundException;

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
	 * and update it
	 * @param ex access to external service 
	 * @return the list of customSource
	 * @throws CategoryProfileNotFoundException
	 * @throws CategoryNotVisibleException
	 * @throws CategoryNotLoadedException
	 * @see org.esupportail.lecture.domain.model.CustomCategory#getSortedCustomSources(org.esupportail.lecture.domain.ExternalService)
	 */
	@Override
	public List<CustomSource> getSortedCustomSources(ExternalService ex) 
		throws CategoryProfileNotFoundException, CategoryNotVisibleException, CategoryNotLoadedException {
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
			// TODO (GB) il y a encore un CategoryNotLoaded à catcher : que fait on ?
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
	 * Return a list of <SourceProfile,AvailabilityMode> corresponding to visible sources for user, 
	 * in this customCategory and update it
	 * @param ex access to external service 
	 * @return list of ProfileAvailability
	 * @throws CategoryProfileNotFoundException 
	 * @throws CategoryNotVisibleException 
	 * @throws CategoryNotLoadedException 
	 * @see org.esupportail.lecture.domain.model.CustomCategory#getVisibleSources(org.esupportail.lecture.domain.ExternalService)
	 */
	@Override
	public List<ProfileAvailability> getVisibleSources(ExternalService ex) 
		throws CategoryProfileNotFoundException, CategoryNotVisibleException, CategoryNotLoadedException {
		if (log.isDebugEnabled()) {
			log.debug("id="+super.getElementId()+" - getVisibleSources(ex)");
		}
//		 TODO (GB later) à redéfinir avec les custom personnal category : en fonction de l'ordre d'affichage peut etre.
		ManagedCategoryProfile profile = getProfile();
		List<ProfileVisibility> couplesVisib;
		try {
			couplesVisib = profile.getVisibleSourcesAndUpdateCustom(this,ex);
		} catch (CategoryNotLoadedException e) {
			userProfile.updateCustomContextsForOneManagedCategory(getElementId(),ex);
			couplesVisib = profile.getVisibleSourcesAndUpdateCustom(this,ex);
			// TODO (GB) il y a encore un CategoryNotLoaded à catcher : que fait on ?
		}
		
		DomainTools.getDaoService().updateCustomCategory(this);
		DomainTools.getDaoService().updateUserProfile(super.getUserProfile());
		
		List<ProfileAvailability> couplesAvail = new Vector<ProfileAvailability>();
		for(ProfileVisibility coupleV : couplesVisib){
			// Every couple is not NOTVISBLE
			ProfileAvailability coupleA;
			SourceProfile sourceProfile = (SourceProfile) coupleV.getProfile();
			
			if (coupleV.getMode() == VisibilityMode.OBLIGED ) {
				coupleA = new ProfileAvailability(sourceProfile,AvailabilityMode.OBLIGED);
			
			} else { // It must be ALLOWED OR AUTOSUBSRIBED
				if (subscriptions.containsKey(sourceProfile.getId())){
					coupleA = new ProfileAvailability(sourceProfile,AvailabilityMode.SUBSCRIBED);
				} else {
					coupleA = new ProfileAvailability(sourceProfile,AvailabilityMode.NOTSUBSCRIBED);
				}
			}
			couplesAvail.add(coupleA);
			log.trace("Add source and availability");
		}
		
		return couplesAvail;
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
	 * and also removes every occcurence in userProfile
	 * Used to remove a subscription or an importation indifferently
	 * @param profile the managedSourceProfile associated to the CustomManagedSource to remove
	 */
	@Override
	protected void removeCustomManagedSourceFromProfile(ManagedSourceProfile profile) {
		if (log.isDebugEnabled()){
			log.debug("id="+super.getElementId()+" - removeCustomManagedSourceFromProfile("+profile.getId()+")");
		}
		getUserProfile().removeCustomManagedSourceFromProfile(profile.getId());
		
	}
	
	/** 
	 * @param sourceId Id for customManagedSource
	 * @return true if this customCategory has a reference on customManagedSource sourceId
	 * @see org.esupportail.lecture.domain.model.CustomCategory#isThereCustomManagedSource(java.lang.String)
	 */
	@Override
	public boolean containsCustomManagedSource(String sourceId) {
		if (log.isDebugEnabled()){
			log.debug("id="+getElementId()+" - containsCustomManagedSource("+sourceId+")");
		}
		return subscriptions.containsKey(sourceId);
		
	}
	
	/**
	 * Remove the customManagedSource sourceId in this customManagedCategory only
	 * @param sourceId ID for customManagedSource
	 * @see org.esupportail.lecture.domain.model.CustomCategory#removeCustomManagedSource(java.lang.String)
	 */
	@Override
	public void removeCustomManagedSource(String sourceId) {
		if (log.isDebugEnabled()){
			log.debug("id="+getElementId()+" - removeCustomManagedSource("+sourceId+")");
		}
		CustomSource cs = subscriptions.get(sourceId);
		if (cs != null) {
			subscriptions.remove(sourceId);
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
	

	/**
	 * after checking visibility rights, subcribe user to the source sourceId in this CustomMAnagedCategory
	 * @param sourceId source ID
	 * @param ex access to externalService
	 * @throws CategoryProfileNotFoundException 
	 * @throws SourceProfileNotFoundException 
	 * @throws CategoryNotLoadedException 
	 * @throws SourceNotVisibleException 
	 * @throws ComputeFeaturesException 
	 * @see org.esupportail.lecture.domain.model.CustomCategory#subscribeToSource(java.lang.String, org.esupportail.lecture.domain.ExternalService)
	 */
	@Override
	public void subscribeToSource(String sourceId, ExternalService ex) 
		throws CategoryProfileNotFoundException, CategoryNotLoadedException, SourceProfileNotFoundException, SourceNotVisibleException, ComputeFeaturesException {
		if (log.isDebugEnabled()){
			log.debug("subscribeToSource("+sourceId+",externalService)");
		}
		ManagedCategoryProfile catProfile = getProfile();
		ManagedSourceProfile soProfile = catProfile.getSourceProfileById(sourceId);
		VisibilityMode mode = soProfile.updateCustomCategory(this, ex);
		
		if (mode == VisibilityMode.ALLOWED || mode == VisibilityMode.AUTOSUBSCRIBED) {
			if (subscriptions.containsKey(sourceId)) {
				log.warn("Nothing is done for SubscribeToSource requested on source "+sourceId+
					" in category "+this.getElementId()+"\nfor user "+getUserProfile().getUserId()+" because this source is already in subscriptions");
			} else {
				addSubscription(soProfile);
				DomainTools.getDaoService().updateCustomCategory(this);
				DomainTools.getDaoService().updateUserProfile(userProfile);
				log.debug("addSubscription to source "+sourceId);
			}
			
		} else if (mode == VisibilityMode.OBLIGED){
			log.warn("Nothing is done for SubscribeToSource requested on source "+sourceId+
					" in category "+this.getElementId()+"\nfor user "+getUserProfile().getUserId()+" because this source is OBLIGED in this case");
			
		} else if (mode == VisibilityMode.NOVISIBLE) {
			String errorMsg = "SubscribeToSource("+sourceId+") is impossible because this source is NOT VISIBLE for user "
				+getUserProfile().getUserId()+"in category "+getElementId();
			log.error(errorMsg);
			throw new SourceNotVisibleException(errorMsg);
		}
		
	}
	
	
	/**
	 * after checking visibility rights, unsubcribe user to the source sourceId in this CustomMAnagedCategory
	 * @param sourceId source ID
	 * @param ex access to externalService
	 * @throws CategoryProfileNotFoundException 
	 * @throws SourceProfileNotFoundException 
	 * @throws CategoryNotLoadedException 
	 * @throws ComputeFeaturesException 
	 * @throws SourceObligedException 
	 * @see org.esupportail.lecture.domain.model.CustomCategory#unsubscribeToSource(java.lang.String, org.esupportail.lecture.domain.ExternalService)
	 */
	@Override
	public void unsubscribeToSource(String sourceId, ExternalService ex) 
		throws CategoryProfileNotFoundException, CategoryNotLoadedException, SourceProfileNotFoundException, ComputeFeaturesException, 
		SourceObligedException {
		if (log.isDebugEnabled()){
			log.debug("unsubscribeToSource("+sourceId+",externalService)");
		}

		ManagedCategoryProfile catProfile = getProfile();
		ManagedSourceProfile soProfile = catProfile.getSourceProfileById(sourceId);
		VisibilityMode mode = soProfile.updateCustomCategory(this, ex);
		
		if (mode == VisibilityMode.ALLOWED || mode == VisibilityMode.AUTOSUBSCRIBED) {
			if (!subscriptions.containsKey(sourceId)) {
				log.warn("Nothing is done for UnsubscribeToSource requested on source "+sourceId+
					" in category "+this.getElementId()+"\nfor user "+getUserProfile().getUserId()+" because this source is not in subscriptions");
			} else {
				removeCustomManagedSourceFromProfile(soProfile);
				DomainTools.getDaoService().updateCustomCategory(this);
				DomainTools.getDaoService().updateUserProfile(userProfile);
				log.debug("removeCustomManagedSource to source "+sourceId);
			}
			
		} else if (mode == VisibilityMode.OBLIGED){
			String errorMsg = "UnsubscribeToSource("+sourceId+") is impossible because this source is OBLIGED for user "
				+getUserProfile().getUserId()+"in category "+getElementId();
			log.error(errorMsg);
			throw new SourceObligedException(errorMsg);
			
			
			
		} else if (mode == VisibilityMode.NOVISIBLE) {
			log.warn("Nothing is done for UnsubscribeToSource requested on source "+sourceId+
					" in category "+this.getElementId()+"\nfor user "+getUserProfile().getUserId()+" because this source is NOVISIBLE in this case");
		}
		
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
