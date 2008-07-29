/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;

import java.util.ArrayList;
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
import org.esupportail.lecture.exceptions.domain.CategoryOutOfReachException;
import org.esupportail.lecture.exceptions.domain.CategoryProfileNotFoundException;
import org.esupportail.lecture.exceptions.domain.CategoryTimeOutException;
import org.esupportail.lecture.exceptions.domain.InternalDomainException;
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

	/**
	 * ID string for log.
	 */
	private static final String ID = "id=";

	/*
	 ************************** PROPERTIES *********************************/	
	/**
	 * logger.
	 */
	private static final Log LOG = LogFactory.getLog(CustomManagedCategory.class);

	/**
	 * Subscriptions of CustomManagedSources.
	 */
	private Map<String, CustomManagedSource> subscriptions = new Hashtable<String, CustomManagedSource>();
	
	/**
	 * CategoryProfile associated to this customManagedCategory.
	 */
	private ManagedCategoryProfile categoryProfile;
	
	/*
	 ************************** INIT *********************************/	

	/**
	 * @param profileId 
	 * @param user
	 */
	protected CustomManagedCategory(final String profileId, final UserProfile user) {
		super(profileId, user);
		if (LOG.isDebugEnabled()) {
			LOG.debug("CustomManagedCategory(" + profileId + "," + user.getUserId() + ")");
		}
	}

	/**
	 * default constructor.
	 */
	protected CustomManagedCategory() {
		super();
		if (LOG.isDebugEnabled()) {
			LOG.debug("CustomManagedCategory()");
		}
	}

	/*
	 ************************** METHODS *********************************/	
	
	/**
	 * Return the list of sorted customSources displayed by this customCategory.
	 * and update it
	 * @param ex access to external service 
	 * @return the list of customSource
	 * @throws CategoryProfileNotFoundException
	 * @throws CategoryNotVisibleException
	 * @throws InternalDomainException 
	 * @throws CategoryTimeOutException 
	 * @throws CategoryOutOfReachException 
	 * @see org.esupportail.lecture.domain.model.CustomCategory#getSortedCustomSources(
	 *   org.esupportail.lecture.domain.ExternalService)
	 */
	@Override
	public List<CustomSource> getSortedCustomSources(final ExternalService ex) 
	throws CategoryProfileNotFoundException, CategoryNotVisibleException, InternalDomainException, 
	CategoryTimeOutException, CategoryOutOfReachException {
		if (LOG.isDebugEnabled()) {
			LOG.debug(ID + super.getElementId() + " - getSortedCustomSources(externalService)");
		}
		// TODO (GB later) redéfinir avec les custom personnal 
		// category : en fonction de l'ordre d'affichage peut etre.
		
		ManagedCategoryProfile profile = getProfile();
		try {
			profile.updateCustom(this, ex);
		} catch (CategoryNotLoadedException e1) {
			// Dans ce cas : la mise à jour du customContext n'a pas été effectué
			try {
				userProfile.updateCustomContextsForOneManagedCategory(getElementId(), ex);
				profile.updateCustom(this, ex);
			} catch (CategoryNotLoadedException e2) {
				// Dans ce cas : la managedCategory n'est pointée par aucun 
				// context correspondant à des customContext du userProfile => supression ?
				userProfile.removeCustomManagedCategoryIfOrphan(getElementId());
				throw new CategoryOutOfReachException("ManagedCategory " + getElementId()
					+ "is not refered by any customContext in userProfile " 
					+ userProfile.getUserId());
			}
		}
		
		List<CustomSource> listSources = new Vector<CustomSource>();
		for (CustomSource customSource : subscriptions.values()){
			listSources.add(customSource);
			LOG.trace("Add source");
		}
	
		return listSources;
	}
	
	/**
	 * Return a list of "SourceProfile, AvailabilityMode" corresponding to visible sources for user, 
	 * in this customCategory and update it.
	 * @param ex access to external service 
	 * @return list of ProfileAvailability
	 * @throws CategoryProfileNotFoundException 
	 * @throws CategoryNotVisibleException 
	 * @throws CategoryOutOfReachException 
	 * @throws InternalDomainException 
	 * @throws CategoryTimeOutException 
	 * @see org.esupportail.lecture.domain.model.CustomCategory#getVisibleSources(
	 *   org.esupportail.lecture.domain.ExternalService)
	 */
	@Override
	public List<ProfileAvailability> getVisibleSources(final ExternalService ex) 
	throws CategoryProfileNotFoundException, CategoryNotVisibleException, CategoryOutOfReachException, 
	InternalDomainException, CategoryTimeOutException {
		if (LOG.isDebugEnabled()) {
			LOG.debug(ID + super.getElementId() + " - getVisibleSources(ex)");
		}
		// TODO (GB later) redéfinir avec les custom personnal 
		// category : en fonction de l'ordre d'affichage peut etre.
		ManagedCategoryProfile profile = getProfile();
		List<ProfileVisibility> couplesVisib;
		try {
			couplesVisib = profile.getVisibleSourcesAndUpdateCustom(this, ex);
		} catch (CategoryNotLoadedException e1) {
			// Dans ce cas : la mise à jour du customContext n'a pas été effectuée
			try {
				// Dans ce cas : la managedCategory n'est pointée par aucun 
				// context correspondant à des customContext du userProfile => supression ?
				userProfile.updateCustomContextsForOneManagedCategory(getElementId(), ex);
				couplesVisib = profile.getVisibleSourcesAndUpdateCustom(this, ex);
			} catch (CategoryNotLoadedException e2) {
				throw new CategoryOutOfReachException("ManagedCategory " + getElementId()
					+ "is not refered by any customContext in userProfile " 
					+ userProfile.getUserId());
			}
		}
		
		List<ProfileAvailability> couplesAvail = new Vector<ProfileAvailability>();
		for (ProfileVisibility coupleV : couplesVisib) {
			// Every couple is not NOTVISIBLE (= visible)
			ProfileAvailability coupleA;
			SourceProfile sourceProfile = (SourceProfile) coupleV.getProfile();
			
			if (coupleV.getMode() == VisibilityMode.OBLIGED ) {
				coupleA = new ProfileAvailability(sourceProfile, AvailabilityMode.OBLIGED);
			} else { 
				// It must be ALLOWED OR AUTOSUBSRIBED
				if (subscriptions.containsKey(sourceProfile.getId())) {
					coupleA = new ProfileAvailability(sourceProfile, AvailabilityMode.SUBSCRIBED);
				} else {
					coupleA = new ProfileAvailability(sourceProfile, 
						AvailabilityMode.NOTSUBSCRIBED);
				}
			}
			couplesAvail.add(coupleA);
			LOG.trace("Add source and availability");
		}
		return couplesAvail;
	}
	
	/**
	 * after checking visibility rights, subcribe user to the source sourceId in this CustomMAnagedCategory.
	 * @param sourceId source ID
	 * @param ex access to externalService
	 * @throws CategoryProfileNotFoundException 
	 * @throws SourceProfileNotFoundException 
	 * @throws CategoryOutOfReachException 
	 * @throws SourceNotVisibleException 
	 * @throws InternalDomainException 
	 * @throws CategoryTimeOutException 
	 * @throws CategoryNotVisibleException 
	 * @see org.esupportail.lecture.domain.model.CustomCategory#subscribeToSource(
	 *   java.lang.String, org.esupportail.lecture.domain.ExternalService)
	 */
	@Override
	public void subscribeToSource(final String sourceId, final ExternalService ex) 
	throws CategoryProfileNotFoundException, CategoryOutOfReachException, SourceProfileNotFoundException, SourceNotVisibleException, 
	CategoryNotVisibleException, CategoryTimeOutException, InternalDomainException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("subscribeToSource(" + sourceId + ",externalService)");
		}
		ManagedCategoryProfile catProfile = getProfile();
		ManagedSourceProfile soProfile = null;
		VisibilityMode mode = null;
		try {
			soProfile = catProfile.getSourceProfileById(sourceId);
			mode = soProfile.updateCustomCategory(this, ex);
		} catch (CategoryNotLoadedException e1) {
			// Dans ce cas : la mise à jour du customContext n'a pas été effectuée
			try {
				userProfile.updateCustomContextsForOneManagedCategory(getElementId(), ex);
				soProfile = catProfile.getSourceProfileById(sourceId);
				mode = soProfile.updateCustomCategory(this, ex);
			} catch (CategoryNotLoadedException e2) {
				// Dans ce cas : la managedCategory n'est pointée par aucun 
				// context correspondant à des customContext du userProfile => supression ?
				userProfile.removeCustomManagedCategoryIfOrphan(getElementId());
				throw new CategoryOutOfReachException("ManagedCategory " + getElementId()
					+ "is not refered by any customContext in userProfile "
					+ userProfile.getUserId());
			}
		}
		
		if (mode == VisibilityMode.ALLOWED || mode == VisibilityMode.AUTOSUBSCRIBED) {
			if (subscriptions.containsKey(sourceId)) {
				LOG.warn("Nothing is done for SubscribeToSource requested on source " + sourceId
					+ " in category " + this.getElementId() + "\nfor user " 
					+ getUserProfile().getUserId() 
					+ " because this source is already in subscriptions");
			} else {
				addSubscription(soProfile);
				LOG.debug("addSubscription to source " + sourceId);
			}
			
		} else if (mode == VisibilityMode.OBLIGED) {
			LOG.warn("Nothing is done for SubscribeToSource requested on source " + sourceId 
					+ " in category " + this.getElementId() + "\nfor user "
					+ getUserProfile().getUserId()
					+ " because this source is OBLIGED in this case");
			
		} else if (mode == VisibilityMode.NOVISIBLE) {
			String errorMsg = "SubscribeToSource(" + sourceId
			 	+ ") is impossible because this source is NOT VISIBLE for user "
				+ getUserProfile().getUserId() + "in category " + getElementId();
			LOG.error(errorMsg);
			throw new SourceNotVisibleException(errorMsg);
		}
		
	}
	
	
	/**
	 * after checking visibility rights, unsubcribe user to the source sourceId in this CustomMAnagedCategory.
	 * @param sourceId source ID
	 * @param ex access to externalService
	 * @throws CategoryProfileNotFoundException 
	 * @throws CategoryOutOfReachException 
	 * @throws SourceObligedException 
	 * @see org.esupportail.lecture.domain.model.CustomCategory#unsubscribeToSource(
	 *   java.lang.String, org.esupportail.lecture.domain.ExternalService)
	 */
	@Override
	public void unsubscribeToSource(final String sourceId, final ExternalService ex) 
	throws CategoryProfileNotFoundException, CategoryOutOfReachException, CategoryNotVisibleException, 
	CategoryTimeOutException, SourceObligedException, InternalDomainException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("unsubscribeToSource(" + sourceId + ",externalService)");
		}
		try {
			ManagedCategoryProfile catProfile = getProfile();
			ManagedSourceProfile soProfile = null;
			VisibilityMode mode = null;
			try {
				soProfile = catProfile.getSourceProfileById(sourceId);
				mode = soProfile.updateCustomCategory(this, ex);
			} catch (CategoryNotLoadedException e1) {
				// Dans ce cas : la mise à jour du customContext n'a pas été effectuée
				try {
					userProfile.updateCustomContextsForOneManagedCategory(getElementId(), ex);
					soProfile = catProfile.getSourceProfileById(sourceId);
					mode = soProfile.updateCustomCategory(this, ex);
				} catch (CategoryNotLoadedException e2) {
					// Dans ce cas : la managedCategory n'est pointée par aucun 
					// context correspondant à des customContext du userProfile => supression ?
					userProfile.removeCustomManagedCategoryIfOrphan(getElementId());
					throw new CategoryOutOfReachException("ManagedCategory " + getElementId()
						+ "is not refered by any customContext in userProfile "
						+ userProfile.getUserId());
				}
			}			
			if (mode == VisibilityMode.ALLOWED || mode == VisibilityMode.AUTOSUBSCRIBED) {
				if (!subscriptions.containsKey(sourceId)) {
					LOG.warn("Nothing is done for UnsubscribeToSource requested on source " 
						+ sourceId + " in category " + this.getElementId() + "\nfor user "
						+ getUserProfile().getUserId() 
						+ " because this source is not in subscriptions");
				} else {
					removeCustomManagedSourceFromProfile(sourceId);
					LOG.trace("removeCustomManagedSource to source " + sourceId);
				}
				
			} else if (mode == VisibilityMode.OBLIGED) {
				String errorMsg = "UnsubscribeToSource(" + sourceId
					+ ") is impossible because this source is OBLIGED for user "
					+ getUserProfile().getUserId() + "in category " + getElementId();
				LOG.error(errorMsg);
				throw new SourceObligedException(errorMsg);
			} else if (mode == VisibilityMode.NOVISIBLE) {
				LOG.warn("Nothing is done for UnsubscribeToSource requested on source "
					+ sourceId + " in category " + this.getElementId() + "\nfor user " 
					+ getUserProfile().getUserId()
					+ " because this source is NOVISIBLE in this case");
			}
		} catch (SourceProfileNotFoundException e) {
			removeCustomManagedSourceFromProfile(sourceId);
			LOG.trace("removeCustomManagedSource to source " + sourceId);
		}
	}
	
	/* ADD ELEMENTS */
	
	/**
	 * Add a subscription source to this custom category (if no exists, else do nothing). 
	 * and creates the corresponding customManagedSource with the given managedSourceProfile
	 * This new customManagedSource is also added to the userProfile (owner of all customElements)
	 * @param managedSourceProfile the source profile to subscribe
	 */
	protected void addSubscription(final ManagedSourceProfile managedSourceProfile) {
		if (LOG.isDebugEnabled()) {
			LOG.debug(ID + super.getElementId() + " - addSubscription(" 
				+ managedSourceProfile.getId() + ")");
		}
		String profileId = managedSourceProfile.getId();
		if (!subscriptions.containsKey(profileId)) {
			CustomManagedSource customManagedSource = 
				new CustomManagedSource(managedSourceProfile, getUserProfile());
			subscriptions.put(profileId, customManagedSource);
//			DomainTools.getDaoService().updateCustomCategory(this);
			getUserProfile().addCustomSource(customManagedSource);
		}
	}
	
	/* REMOVE ELEMENTS */

	/**
	 * remove a CustomManagedSource displayed in this CustomManagedCategory
	 * and also removes every occcurence in userProfile.
	 * Used to remove a subscription or an importation indifferently
	 * @param profileId the managedSourceProfile ID associated to the CustomManagedSource to remove
	 */
	@Override
	protected void removeCustomManagedSourceFromProfile(final String profileId) {
		if (LOG.isDebugEnabled()) {
			LOG.debug(ID + super.getElementId()
				+ " - removeCustomManagedSourceFromProfile(" + profileId + ")");
		}
		getUserProfile().removeCustomManagedSourceFromProfile(profileId);
	}
	
	/**
	 * Remove the customSource sourceId in this customManagedCategory only.
	 * @param sourceId ID for customManagedSource
	 * @see org.esupportail.lecture.domain.model.CustomCategory#removeCustomSource(java.lang.String)
	 */
	@Override
	public void removeCustomSource(final String sourceId) {
		if (LOG.isDebugEnabled()) {
			LOG.debug(ID + getElementId() + " - removeCustomSource(" + sourceId + ")");
		}
		removeCustomManagedSource(sourceId);
		// TODO (GB later) ajouter pour les personnal
	}
	
	/**
	 * Remove the customManagedSource sourceId in this customManagedCategory only.
	 * @param sourceId ID for customManagedSource
	 * @see org.esupportail.lecture.domain.model.CustomCategory#removeCustomManagedSource(java.lang.String)
	 */
	@Override
	public void removeCustomManagedSource(final String sourceId) {
		if (LOG.isDebugEnabled()) {
			LOG.debug(ID + getElementId() + " - removeCustomManagedSource(" + sourceId + ")");
		}
		CustomSource cs = subscriptions.get(sourceId);
		if (cs != null) {
			subscriptions.remove(sourceId);
//			DomainTools.getDaoService().updateCustomCategory(this);
		}
	}
	
	
	/**
	 * Remove every subscriptions (customManagedSources) of this customManagedCategory.
	 * @see org.esupportail.lecture.domain.model.CustomCategory#removeSubscriptions()
	 */
	@Override
	public void removeSubscriptions() {
		if (LOG.isDebugEnabled()) {
			LOG.debug(ID + this.getElementId() + " - removeSubscriptions()");
		}
		// TODO (GB <-- RB) chercher ce bug partout ailleurs. 
		// Ici : cloner keySet (a cause des ConcurrentModificationException)
		List<String> sids = new ArrayList<String>();
		for (String sourceId : subscriptions.keySet()) {
			sids.add(sourceId);
		}
		for (String sourceId : sids) {
			removeCustomManagedSourceFromProfile(sourceId);
		}
		
	}

	/* MISCELLANEOUS */

	/**
	 * Returns the ManagedCategoryProfile associated to this CustomManagedCategory.
	 * @see org.esupportail.lecture.domain.model.CustomCategory#getProfile()
	 */
	@Override
	public ManagedCategoryProfile getProfile() throws CategoryProfileNotFoundException {
		if (LOG.isDebugEnabled()) {
			LOG.debug(ID + super.getElementId() + " - getProfile()");
		}
		if (categoryProfile == null) {
			Channel channel = DomainTools.getChannel();
			categoryProfile = channel.getManagedCategoryProfile(getElementId());
		}
		return categoryProfile;		
	}
	
	/** 
	 * @param sourceId Id for customManagedSource
	 * @return true if this customCategory has a reference on customManagedSource sourceId
	 * @see org.esupportail.lecture.domain.model.CustomCategory#containsCustomManagedSource(java.lang.String)
	 */
	@Override
	public boolean containsCustomManagedSource(final String sourceId) {
		if (LOG.isDebugEnabled()) {
			LOG.debug(ID + getElementId() + " - containsCustomManagedSource(" + sourceId + ")");
		}
		return subscriptions.containsKey(sourceId);
		
	}
		
	/** 
	 * @param sourceId Id for customSource
	 * @return true if this customCategory has a reference on customSource sourceId
	 * @see org.esupportail.lecture.domain.model.CustomCategory#containsCustomSource(java.lang.String)
	 */
	@Override
	public boolean containsCustomSource(final String sourceId) {
		if (LOG.isDebugEnabled()) {
			LOG.debug(ID + getElementId() + " - containsCustomSource(" + sourceId + ")");
		}
		return containsCustomManagedSource(sourceId);
		// TODO (GB later) ajouter pour les personnal
		
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer string = new StringBuffer(getClass().getSimpleName() + "#" + hashCode() 
				+ "[elementId=[" + getElementId()
				+ "], customCategoryPK=[" + getCustomCategoryPK() 
				+ "], userProfile.id=[" + userProfile.getUserId() + "],");
		// subscriptions
		string.append("\n subscriptions=[");
		for (String key : subscriptions.keySet()) {
			string.append(key).append(", ");
		}
		string.append("]\n]");
		return string.toString();
	}
	
	/*
	 ************************** ACCESSORS *********************************/	


	/**
	 * @return source subcription of this category
	 */
	protected Map<String, CustomManagedSource> getSubscriptions() {
		return subscriptions;
	}

	/**
	 * @param subscriptions
	 */
	@SuppressWarnings("unused")
	private void setSubscriptions(final Map<String, CustomManagedSource> subscriptions) {
		this.subscriptions = subscriptions;
		//Needed by Hibernate 
	}


	
}
