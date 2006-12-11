package org.esupportail.lecture.domain.model;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.DomainServiceImpl;
import org.esupportail.lecture.domain.DomainTools;
import org.esupportail.lecture.domain.ExternalService;
import org.esupportail.lecture.exceptions.CategoryNotLoadedException;
import org.esupportail.lecture.exceptions.CategoryProfileNotFoundException;
import org.esupportail.lecture.exceptions.ElementNotLoadedException;
import org.esupportail.lecture.exceptions.ManagedCategoryProfileNotFoundException;

/**
 * Customizations on a managedCategory for a user Profile
 * @author gbouteil
 *
 */
public class CustomManagedCategory extends CustomCategory {

	/*
	 ************************** PROPERTIES *********************************/	
	protected static final Log log = LogFactory.getLog(CustomManagedCategory.class);


	/**
	 * The map of subscribed CustomManagedSource
	 */
	private Hashtable<String,CustomManagedSource> subscriptions;
	
	/*
	 ************************** INIT *********************************/	


	/**
	 * @param catId
	 * @param user
	 */
	public CustomManagedCategory(String catId,UserProfile user){
		super(catId,user);
		subscriptions = new Hashtable<String,CustomManagedSource>();
		
	}
	
	/*
	 ************************** METHODS *********************************/	

	/**
	 * @see org.esupportail.lecture.domain.model.CustomCategory#getSortedCustomSources(org.esupportail.lecture.domain.ExternalService)
	 */
	@Override
	public List<CustomSource> getSortedCustomSources(ExternalService externalService) throws CategoryProfileNotFoundException, ElementNotLoadedException{
	// TODO (GB later) à redéfinir avec les custom personnal category : en fonction de l'ordre d'affichage peut etre.
		
		ManagedCategoryProfile profile = getProfile();
		profile.updateCustom(this,externalService);
		
		List<CustomSource> listSources = new Vector<CustomSource>();
		for(CustomSource customSource : subscriptions.values()){
			listSources.add(customSource);
			log.debug("Add source");
		}
	
		return listSources;
	}
	

	/**
	 * @see org.esupportail.lecture.domain.model.CustomCategory#addManagedCustomSource(org.esupportail.lecture.domain.model.ManagedSourceProfile)
	 */
	public void addSubscription(ManagedSourceProfile managedSourceProfile) {
		String profileId = managedSourceProfile.getId();
		
		if (!subscriptions.containsKey(profileId)){
			CustomManagedSource customManagedSource = new CustomManagedSource(managedSourceProfile, getUserProfile());
			subscriptions.put(profileId,customManagedSource);
			getUserProfile().addCustomSource(customManagedSource);
		}
	}
	

	/**
	 * @see org.esupportail.lecture.domain.model.CustomCategory#removeManagedCustomSource(org.esupportail.lecture.domain.model.ManagedSourceProfile)
	 */
	@Override
	public void removeCustomManagedSource(ManagedSourceProfile profile) {
		//		 TODO (GB) tester avec la BDD
		subscriptions.remove(profile.getId());
		getUserProfile().removeCustomSource(profile.getId());
		// TODO (GB later) on purrait aussi le laisser dans le user profile
		
	}
	
	/**
	 * @see org.esupportail.lecture.domain.model.CustomCategory#getProfile()
	 */
	@Override
	public ManagedCategoryProfile getProfile() throws CategoryProfileNotFoundException {
		Channel channel = DomainTools.getChannel();
		return channel.getManagedCategoryProfile(getElementId());
	}
	

	
	
	/*
	 ************************** ACCESSORS *********************************/	





	
}
