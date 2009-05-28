/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.DomainTools;
import org.esupportail.lecture.exceptions.domain.CategoryNotVisibleException;
import org.esupportail.lecture.exceptions.domain.CategoryProfileNotFoundException;
import org.esupportail.lecture.exceptions.domain.CategoryTimeOutException;
import org.esupportail.lecture.exceptions.domain.InternalDomainException;
import org.esupportail.lecture.exceptions.domain.ManagedCategoryNotLoadedException;
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
	 * Set of unsubscribed AutoSubscribed Sources.
	 */
	private Map<String, UnsubscribeAutoSubscribedSourceFlag> unsubscribedAutoSubscribedSources = new Hashtable<String, UnsubscribeAutoSubscribedSourceFlag>();

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
	 * @return the list of customSource
	 * @throws InternalDomainException 
	 * @throws CategoryTimeOutException 
	 * @throws CategoryNotVisibleException 
	 * @see org.esupportail.lecture.domain.model.CustomCategory#getSortedCustomSources()
	 */
	@Override
	public List<CustomSource> getSortedCustomSources() 
	throws InternalDomainException, CategoryNotVisibleException, 
	CategoryTimeOutException {
		if (LOG.isDebugEnabled()) {
			LOG.debug(ID + super.getElementId() + " - getSortedCustomSources()");
		}
		// TODO (GB later) redéfinir avec les custom personnal 
		// category : en fonction de l'ordre d'affichage peut etre.
		
		UserProfile userProfile = super.getUserProfile();
		ManagedCategoryProfile profile;
		try {
			profile = getProfile();
		} catch (CategoryProfileNotFoundException e) {
			String errorMsg = "Unable to getSortedCustomSources because of categoryProfile is not found";
			LOG.error(errorMsg);
			throw new InternalDomainException(errorMsg, e);
		}
		try {
			profile.updateCustom(this);
		} catch (ManagedCategoryNotLoadedException e1) {
			// Dans ce cas : la mise à jour du customContext n'a pas été effectué
			try {
				userProfile.updateCustomContextsForOneManagedCategory(getElementId());
				profile.updateCustom(this);
			} catch (ManagedCategoryNotLoadedException e2) {
				// Dans ce cas : la managedCategory n'est pointée par aucun 
				// context correspondant à des customContext du userProfile => suppression ?
				userProfile.removeCustomManagedCategoryIfOrphan(getElementId());
				String errorMsg = "ManagedCategory " + getElementId()
					+ "is not refered by any customContext in userProfile";
				LOG.error(errorMsg);
				throw new InternalDomainException(errorMsg, e2);
			}
		}
		
		List<CustomSource> listSources = new Vector<CustomSource>();
		for (CustomSource customSource : subscriptions.values()) {
			listSources.add(customSource);
			LOG.trace("Add source");
		}
	
		return listSources;
	}
	
	/**
	 * Return a list of "SourceProfile, AvailabilityMode" corresponding to visible sources for user, 
	 * in this customCategory and update it.
	 * @return list of CoupleProfileAvailability
	 * @throws InternalDomainException 
	 * @throws InternalDomainException 
	 * @throws CategoryTimeOutException 
	 * @throws CategoryNotVisibleException
	 * @see org.esupportail.lecture.domain.model.CustomCategory#getVisibleSources()
	 */
	@Override
	public List<CoupleProfileAvailability> getVisibleSources() 
	throws InternalDomainException, CategoryNotVisibleException, CategoryTimeOutException {
		if (LOG.isDebugEnabled()) {
			LOG.debug(ID + super.getElementId() + " - getVisibleSources(ex)");
		}
		// TODO (GB later) redéfinir avec les custom personnal 
		// category : en fonction de l'ordre d'affichage peut etre.
		UserProfile userProfile = super.getUserProfile();
		ManagedCategoryProfile profile;
		try {
			profile = getProfile();
		} catch (CategoryProfileNotFoundException e) {
			String errorMsg = "Unable to getVisibleSources because of categoryProfile is not found";
			LOG.error(errorMsg);
			throw new InternalDomainException(errorMsg, e);
		}
		List<CoupleProfileVisibility> couplesVisib;
		try {
			couplesVisib = profile.getVisibleSourcesAndUpdateCustom(this);
		} catch (ManagedCategoryNotLoadedException e1) {
			// Dans ce cas : la mise à jour du customContext n'a pas été effectuée
			try {
				// Dans ce cas : la managedCategory n'est pointée par aucun 
				// context correspondant à des customContext du userProfile => supression ?
				userProfile.updateCustomContextsForOneManagedCategory(getElementId());
				couplesVisib = profile.getVisibleSourcesAndUpdateCustom(this);
			} catch (ManagedCategoryNotLoadedException e2) {
				String errorMsg = "ManagedCategory " + getElementId()
					+ "is not refered by any customContext in userProfile.";
				LOG.error(errorMsg);
				throw new InternalDomainException(errorMsg, e2);
			}
		}
		
		List<CoupleProfileAvailability> couplesAvail = new Vector<CoupleProfileAvailability>();
		for (CoupleProfileVisibility coupleV : couplesVisib) {
			// Every couple is not NOTVISIBLE (= visible)
			CoupleProfileAvailability coupleA;
			SourceProfile sourceProfile = (SourceProfile) coupleV.getProfile();
			
			if (coupleV.getMode() == VisibilityMode.OBLIGED ) {
				coupleA = new CoupleProfileAvailability(sourceProfile, AvailabilityMode.OBLIGED);
			} else { 
				// It must be ALLOWED OR AUTOSUBSRIBED
				if (subscriptions.containsKey(sourceProfile.getId())) {
					coupleA = new CoupleProfileAvailability(sourceProfile,
						AvailabilityMode.SUBSCRIBED);
				} else {
					coupleA = new CoupleProfileAvailability(sourceProfile, 
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
	 * @throws InternalDomainException 
	 * @throws CategoryTimeOutException 
	 * @throws CategoryNotVisibleException 
	 * @throws SourceNotVisibleException 
	 * @see org.esupportail.lecture.domain.model.CustomCategory#subscribeToSource(java.lang.String)
	 */
	@Override
	public void subscribeToSource(final String sourceId) 
	throws InternalDomainException, CategoryNotVisibleException, CategoryTimeOutException, 
	SourceNotVisibleException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("subscribeToSource(" + sourceId + ")");
		}
		UserProfile userProfile = super.getUserProfile();
		ManagedCategoryProfile catProfile;
		try {
			catProfile = getProfile();
		} catch (CategoryProfileNotFoundException e) {
			String errorMsg = "Unable to subscribeToSource because of categoryProfile is not found";
			LOG.error(errorMsg);
			throw new InternalDomainException(errorMsg, e);
		}
		ManagedSourceProfile soProfile = null;
		VisibilityMode mode = null;
		try {
			soProfile = catProfile.getSourceProfileById(sourceId);
			mode = soProfile.updateCustomCategory(this);
		} catch (ManagedCategoryNotLoadedException e1) {
			// Dans ce cas : la mise à jour du customContext n'a pas été effectuée
			try {
				userProfile.updateCustomContextsForOneManagedCategory(getElementId());
				soProfile = catProfile.getSourceProfileById(sourceId);
				mode = soProfile.updateCustomCategory(this);
			} catch (ManagedCategoryNotLoadedException e2) {
				// Dans ce cas : la managedCategory n'est pointée par aucun 
				// context correspondant à des customContext du userProfile => supression ?
				userProfile.removeCustomManagedCategoryIfOrphan(getElementId());
				String errorMsg = "ManagedCategory " + getElementId()
					+ "is not refered by any customContext in userProfile.";
				LOG.error(errorMsg);
				throw new InternalDomainException(errorMsg, e2);
			} catch (SourceProfileNotFoundException e) {
				String errorMsg = 
					"Unable to subscribeToSource because of sourceProfile is not found";
				LOG.error(errorMsg);
				throw new InternalDomainException(errorMsg, e);
			}
		} catch (SourceProfileNotFoundException e) {
			String errorMsg = "Unable to subscribeToSource because of sourceProfile is not found";
			LOG.error(errorMsg);
			throw new InternalDomainException(errorMsg, e);
		}
		
		if (mode == VisibilityMode.ALLOWED) {
			if (subscriptions.containsKey(sourceId)) {
				LOG.warn("Nothing is done for SubscribeToSource requested on source " + sourceId
					+ " in category " + this.getElementId() + "\nfor user " 
					+ getUserProfile().getUserId() 
					+ " because this source is already in subscriptions");
			} else {
				addSubscription(soProfile);
				LOG.debug("addSubscription to source " + sourceId);
			}
			
		} else if (mode == VisibilityMode.AUTOSUBSCRIBED) {
			if (subscriptions.containsKey(sourceId)) {
				LOG.warn("Nothing is done for SubscribeToSource requested on source " + sourceId
						+ " in category " + this.getElementId() + "\nfor user " 
						+ getUserProfile().getUserId() 
						+ " because this source is already in subscriptions");
			} else {
				addSubscription(soProfile);
				subscribeToAutoSubscribedSource(sourceId);
				LOG.debug("addAutoSubscription to source " + sourceId);
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
	 * @throws InternalDomainException 
	 * @throws CategoryTimeOutException 
	 * @throws CategoryNotVisibleException 
	 * @throws SourceObligedException 
	 * @see org.esupportail.lecture.domain.model.CustomCategory#unsubscribeToSource(String)
	 */
	@Override
	public void unsubscribeToSource(final String sourceId) 
	throws InternalDomainException, CategoryNotVisibleException, 
	CategoryTimeOutException, SourceObligedException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("unsubscribeToSource(" + sourceId + ")");
		}
		UserProfile userProfile = super.getUserProfile();
		ManagedCategoryProfile catProfile;
		try {
			catProfile = getProfile();
		} catch (CategoryProfileNotFoundException e) {
			String errorMsg = "Unable to subscribeToSource because of categoryProfile is not found";
			LOG.error(errorMsg);
			throw new InternalDomainException(errorMsg, e);
		}
		ManagedSourceProfile soProfile = null;
		VisibilityMode mode = null;
		try {
			soProfile = catProfile.getSourceProfileById(sourceId);
			mode = soProfile.updateCustomCategory(this);
		} catch (ManagedCategoryNotLoadedException e1) {
			// Dans ce cas : la mise à jour du customContext n'a pas été effectuée
			try {
				userProfile.updateCustomContextsForOneManagedCategory(getElementId());
				soProfile = catProfile.getSourceProfileById(sourceId);
				mode = soProfile.updateCustomCategory(this);
			} catch (ManagedCategoryNotLoadedException e2) {
				// Dans ce cas : la managedCategory n'est pointée par aucun 
				// context correspondant à des customContext du userProfile => supression ?
				userProfile.removeCustomManagedCategoryIfOrphan(getElementId());
				String errorMsg = "ManagedCategory " + getElementId()
					+ "is not refered by any customContext in userProfile.";
				LOG.error(errorMsg);
				throw new InternalDomainException(errorMsg, e2);
			} catch (SourceProfileNotFoundException e) {
				String errorMsg = 
					"Unable to unsubscribeToSource because of sourceProfile is not found";
				LOG.error(errorMsg);
				throw new InternalDomainException(errorMsg, e);
			}
		} catch (SourceProfileNotFoundException e) {
			String errorMsg = 
				"Unable to unsubscribeToSource because of sourceProfile is not found";
			LOG.error(errorMsg);
			throw new InternalDomainException(errorMsg, e);
		}			
		if (mode == VisibilityMode.ALLOWED) {
			if (!subscriptions.containsKey(sourceId)) {
				LOG.warn("Nothing is done for UnsubscribeToSource requested on source " 
					+ sourceId + " in category " + this.getElementId() + "\nfor user "
					+ getUserProfile().getUserId() 
					+ " because this source is not in subscriptions");
			} else {
				removeCustomManagedSourceFromProfile(sourceId);
				LOG.trace("removeCustomManagedSource to source " + sourceId);
			}

		} else if (mode == VisibilityMode.AUTOSUBSCRIBED) {
			if (!subscriptions.containsKey(sourceId)) {
				LOG.warn("Nothing is done for UnsubscribeToSource requested on source " 
					+ sourceId + " in category " + this.getElementId() + "\nfor user "
					+ getUserProfile().getUserId() 
					+ " because this source is not in subscriptions");
			} else {
				LOG.trace("removeCustomManagedSource (autoSubscribed) to source " + sourceId);
				removeCustomManagedSourceFromProfile(sourceId);
				unsubscribeToAutoSubscribedSource(sourceId);
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
				+ "], userProfile.id=[" + super.getUserProfile().getUserId() + "],");
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

	/**
	 * @return the unsubscribedAutoSubscribedSources
	 */
	public Map<String, UnsubscribeAutoSubscribedSourceFlag> getUnsubscribedAutoSubscribedSources() {
		return unsubscribedAutoSubscribedSources;
	}

	/**
	 * @param unsubscribedAutoSubscribedSources the unsubscribedAutoSubscribedSources to set
	 */
	public void setUnsubscribedAutoSubscribedSources(
			Map<String, UnsubscribeAutoSubscribedSourceFlag> unsubscribedAutoSubscribedSources) {
		this.unsubscribedAutoSubscribedSources = unsubscribedAutoSubscribedSources;
	}

	/**
	 * Return true if the customCategory is unsubscribed in this customContext.
	 * @param profile
	 * @return if category is unsubscribed or not
	 */
	protected boolean isUnsubscribedAutoSubscribedSource(final String sourceId) {
		if (LOG.isDebugEnabled()) {
			LOG.debug(ID + getElementId() + " - isUnsubscribedAutoSubscribedSource(" + sourceId + ")");
		}
		return unsubscribedAutoSubscribedSources.containsKey(sourceId);
	}

	/**
	 * remove a autoSubscribed customCategory contained in this CustomContext from unsubscribedAutoSubscribedCategories.
	 * @param catId id of the profile category associated to the customCategory
	 */
	public void subscribeToAutoSubscribedSource(final String sourceId) {
		if (LOG.isDebugEnabled()) {
			LOG.debug(ID + getElementId() + " - subscribeToAutoSubscribedSource(" + sourceId + ")");
		}
		UnsubscribeAutoSubscribedSourceFlag cat =  unsubscribedAutoSubscribedSources.get(sourceId);
		if (cat != null) {
			unsubscribedAutoSubscribedSources.remove(sourceId);
		} else {
			LOG.warn("subscribeToAutoSubscribedSource(" + sourceId + ") is called for category " + getElementId() 
					+ " but this source is not in ");
		}
	}
	
	/**
	 * mark a autoSubscribed customCategory contained in this CustomContext as unsubscribed.
	 * @param catId id of the profile category associated to the customCategory
	 */
	public void unsubscribeToAutoSubscribedSource(final String sourceId) {
		if (LOG.isDebugEnabled()) {
			LOG.debug(ID + getElementId() + " - unsubscribeToAutoSubscribedSource(" + sourceId + ")");
		}
		if (!unsubscribedAutoSubscribedSources.containsKey(sourceId)) {
			UnsubscribeAutoSubscribedSourceFlag cat = new UnsubscribeAutoSubscribedSourceFlag(this, sourceId);
			Date datejour = new Date();
			cat.setDate(datejour);
			unsubscribedAutoSubscribedSources.put(sourceId, cat);
		} else {
			LOG.warn("unsubscribeToAutoSubscribedCategory(" + sourceId + ") is called for category " + getElementId() 
					+ " but this source is not in ");
		}
	}
	
}
