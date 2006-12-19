package org.esupportail.lecture.domain.model;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.DomainTools;
import org.esupportail.lecture.domain.ExternalService;
import org.esupportail.lecture.exceptions.CategoryProfileNotFoundException;
import org.esupportail.lecture.exceptions.CategoryNotVisibleException;
import org.esupportail.lecture.exceptions.CustomContextNotFoundException;
import org.esupportail.lecture.exceptions.ElementNotLoadedException;

/**
 * Customizations on a managedCategory for a user Profile
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
	 * The map of subscribed CustomManagedSource
	 */
	private Hashtable<String,CustomManagedSource> subscriptions;
	
	/*
	 ************************** INIT *********************************/	


	/**
	 * @param profileId 
	 * @param user
	 */
	public CustomManagedCategory(String profileId,UserProfile user){
		super(profileId,user);
		if (log.isDebugEnabled()){
			log.debug("CustomManagedCategory("+profileId+","+user.getUserId()+")");
		}
		subscriptions = new Hashtable<String,CustomManagedSource>();
	}

	/**
	 * default constructor
	 */
	public CustomManagedCategory(){
		super();
		if (log.isDebugEnabled()){
			log.debug("CustomManagedCategory()");
		}
		subscriptions = new Hashtable<String,CustomManagedSource>();
	}

	//TODO (RB) remove ?
//	/**
//	 * @param catId
//	 * @param user
//	 */
//	public CustomManagedCategory() {
//		super();
//		if (log.isDebugEnabled()){
//			log.debug("CustomManagedCategory()");
//		}
//		subscriptions = new Hashtable<String,CustomManagedSource>();
//	}
	
	/*
	 ************************** METHODS *********************************/	

	/**
	 * @throws CategoryNotVisibleException 
	 * @throws CustomContextNotFoundException 
	 * @see org.esupportail.lecture.domain.model.CustomCategory#getSortedCustomSources(org.esupportail.lecture.domain.ExternalService)
	 */
	@Override
	public List<CustomSource> getSortedCustomSources(ExternalService externalService) 
		throws CategoryProfileNotFoundException, ElementNotLoadedException, CategoryNotVisibleException, CustomContextNotFoundException {
		if (log.isDebugEnabled()){
			log.debug("getSortedCustomSources(externalService)");
		}
	// TODO (GB later) à redéfinir avec les custom personnal category : en fonction de l'ordre d'affichage peut etre.
		
		ManagedCategoryProfile profile = getProfile();
		try {
			profile.updateCustom(this,externalService);
		} catch (ElementNotLoadedException e) {	
			userProfile.updateCustomContextsForOneManagedCategory(getElementId(),externalService);
			// TODO (GB) !!!  et le dao ?
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
	 * @param managedSourceProfile
	 */
	public void addSubscription(ManagedSourceProfile managedSourceProfile) {
		if (log.isDebugEnabled()){
			log.debug("addSubscription("+managedSourceProfile.getId()+")");
		}
		String profileId = managedSourceProfile.getId();
		
		if (!subscriptions.containsKey(profileId)){
			CustomManagedSource customManagedSource = new CustomManagedSource(managedSourceProfile, getUserProfile());
			subscriptions.put(profileId,customManagedSource);
			getUserProfile().addCustomSource(customManagedSource);
		}
	}
	

	/**
	 * @see org.esupportail.lecture.domain.model.CustomCategory#removeCustomManagedSource(org.esupportail.lecture.domain.model.ManagedSourceProfile)
	 */
	public void removeCustomManagedSource(ManagedSourceProfile profile) {
		if (log.isDebugEnabled()){
			log.debug("removeCustomManagedSource("+profile.getId()+")");
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
	 * @see org.esupportail.lecture.domain.model.CustomCategory#getProfile()
	 */
	@Override
	public ManagedCategoryProfile getProfile() throws CategoryProfileNotFoundException {
		if (log.isDebugEnabled()){
			log.debug("getProfile()");
		}
		Channel channel = DomainTools.getChannel();
		return channel.getManagedCategoryProfile(getElementId());
	}
	

	
	
	/*
	 ************************** ACCESSORS *********************************/	





	
}
