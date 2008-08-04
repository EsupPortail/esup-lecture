/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;



import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.DomainTools;
import org.esupportail.lecture.domain.ExternalService;
import org.esupportail.lecture.exceptions.dao.InfoDaoException;
import org.esupportail.lecture.exceptions.domain.CategoryNotLoadedException;
import org.esupportail.lecture.exceptions.domain.CategoryProfileNotFoundException;
import org.esupportail.lecture.exceptions.domain.InfoDomainException;
import org.esupportail.lecture.exceptions.domain.SourceProfileNotFoundException;

/**
 * Managed category profile element. It references a managedCategory
 * It is defined in a context.
 * @author gbouteil
 * @see CategoryProfile
 * @see ManagedElementProfile
 */
public class ManagedCategoryProfile extends CategoryProfile implements ManagedElementProfile {
	/**
	 * Log instance.
	 */
	protected static final Log LOG = LogFactory.getLog(ManagedCategoryProfile.class); 
	
	/*
	 ************************** PROPERTIES ******************************** */	
	
	/**
	 * Contexts where these category profiles are referenced.
	 */
	private Set<Context> contextsSet = new HashSet<Context>();	
	/**
	 * Referrenced category.
	 */
	private ManagedCategory category;
	/**
	 * URL of the remote managed category.
	 */
	private String categoryURL;
	/**
	 * Access mode on the remote managed category.
	 */
	private Accessibility access;
	/**
	 * Ttl of remote reloading.
	 */
	private int ttl;
	/**
	 * TimeOut of remote reloading.
	 */
	private int timeOut;
	
	/* FEATURES */
	
	/**
	 * trustCategory parameter : indicates between managed category and category profile, which one to trust
	 * (used for inheritance regulars in computeFeatures())
	 * True : category is trusted. 
	 * False : category is not trusted, only parameters profile are good 
	 */
	private boolean trustCategory;
	/**
	 * Inner features declared in XML file (used by inheritance regulars).
	 */
	private InnerFeatures inner;
	/**
	 * Inheritance rules are applied on features (used by computeFeatures()).
	 */
	private boolean featuresComputed;
	/**
	 * Editability mode on the category (takes care of inheritance regulars).
	 */	
	private Editability edit;
	/**
	 * Visibility rights on the managed category (takes care of inheritance regulars).
	 */
	private VisibilitySets visibility;
	
	
	/*
	 ************************** INITIALIZATION ******************************** */	
	
	
	/**
	 * Constructor. 
	 */
	@SuppressWarnings("synthetic-access")
	public ManagedCategoryProfile() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("ManagedCategoryProfile()");
		}
		inner = new InnerFeatures();
		featuresComputed = false;
	}
	
	/*
	 ************************** METHODS ******************************** */	
	
	/**
	 * @return Returns the category referenced by this CategoryProfile
	 * @throws CategoryNotLoadedException 
	 */
	@Override
	protected ManagedCategory getElement() throws CategoryNotLoadedException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("id=" + getId() + " - getElement()");
		}
		category = (ManagedCategory) super.getElement(); 
		if (category == null) {
			loadCategory();
		}
		return category;
	}
	
	/**
	 * @param category The category to set.
	 */
	@Override
	protected void setElement(final Category category) {
		ManagedCategory cat = (ManagedCategory) category;
		super.setElement(cat);
	}
	
	/**
	 * Return the name of the referenced category. When the category is not loaded, it returns
	 * the name of this CategoryProfile.
	 * @return name
	 */
	@Override
	public String getName() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("id=" + getId() + " - getName()");
		}
		String currentName;
		
		try {
			currentName = getElement().getName();
		} catch (CategoryNotLoadedException e) {
			LOG.warn("Category " + getId() + " is not loaded");
			currentName = super.getName();
		}
		return currentName;
	}
	
	/**
	 * Return the name of the referenced category. When the category is not loaded, it returns
	 * the name of this CategoryProfile.
	 * @return name
	 */
	@Override
	public String getDescription() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("id=" + getId() + " - getDescription()");
		}
		String currentDesc;
		
		try {
			currentDesc = getElement().getDescription();
		} catch (CategoryNotLoadedException e) {
			LOG.warn("Category " + getId() + " is not loaded");
			currentDesc = super.getDescription();
		}
		return currentDesc;
	}
	

	/**
	 * Add a context to the set of context of this managed category profile.
	 * This means that this managedCategoryProfile is declared in context c
	 * @param c context to add
	 */
	protected void addContext(final Context c) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("id=" + this.getId() + " - addContext(" + c.getId() + ")");
		}
		contextsSet.add(c);
	}
	
	/**
	 * Returns the managedSourceProfile identified by id, accessible by this ManagedCategoryProfile.
	 * (Defined in ManagedCategory referred by this ManagedCategoryProfile)
	 * @param id id of the sourceProfile to get
	 * @return the sourceProfile
	 * @throws CategoryNotLoadedException 
	 * @throws SourceProfileNotFoundException 
	 */
	@Override
	protected ManagedSourceProfile getSourceProfileById(final String id) 
			throws CategoryNotLoadedException, SourceProfileNotFoundException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("id=" + this.getId() + " - getSourceProfileById(" + id + ")");
		}
		return (ManagedSourceProfile) getElement().getSourceProfileById(id);

	}

	/**
	 * Returns the list of contexts referencing this managedCategoryProfile.
	 * @return the list of contexts
	 */
	protected List<Context> getAdoptiveParents() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("getAdoptiveParents(" + this.getId() + ")");
		}
		
		List<Context> parents = new ArrayList<Context>();
		
		// For all contexts refered by this managedCategoryProfile
		for (Context context : contextsSet) {
			parents.add(context);
		}
		return parents;
	}

	/**
	 * Return visibility of the category (or categoryProfile), taking care of inheritance regulars.
	 * @return visibility
	 * @throws CategoryNotLoadedException 
	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#getVisibility()
	 */
	public VisibilitySets getVisibility() throws CategoryNotLoadedException {
		computeFeatures();
		return visibility;
	}

	/**
	 * Sets visibility of category profile (value defined in XML file).
	 * @param visibility 
	 */
	public void setVisibility(final VisibilitySets visibility) {
		inner.visibility = visibility;
		featuresComputed = false;
	}
	
	/**
	 * Return editability mode of the category, taking care of inheritance regulars.
	 * @return edit
	 * @throws CategoryNotLoadedException 
	 */
	protected Editability getEdit() throws CategoryNotLoadedException {
		computeFeatures();
		return edit;
	}

	/**
	 * @param edit 
	 */
	public void setEdit(final Editability edit) {
		inner.edit = edit;
		featuresComputed = false;
	}
	
	/**
	 * Computes rights on parameters shared between parent ManagedCategory and managedCategoryProfile.
	 * taking care of inheritance rules (on param visibility,edit)
	 * @throws CategoryNotLoadedException 
	 */
	private void computeFeatures() throws CategoryNotLoadedException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("id = " + this.getId() + " - computeFeatures()");
		}
		
		if (!featuresComputed) {

			if (trustCategory) {
				ManagedCategory cat = getElement();
				
				visibility = cat.inner.visibility;
				edit = cat.inner.edit;
					
				// Inutile : edit obligatoire le XML d'une category				
				if (edit == null) {
					edit = inner.edit;
				}
				if (visibility == null) {
					visibility = inner.visibility;
				} else if (visibility.isEmpty()) {
					visibility = inner.visibility;
				}
			} else {
				// No trust => features of categoryProfile 
				edit = inner.edit;
				visibility = inner.visibility;
			}
			featuresComputed = true;
		}
	}
	
	
	/* 
	 *************************** INNER CLASS ******************************** */	
	
	/**
	 * Inner Features (editability,visibility) declared in xml file. 
	 * These values are used according to inheritance regulars
	 * @author gbouteil
	 */
	private class InnerFeatures {
		 
 		/** 
		 * Managed category edit mode .
		*/
		protected Editability edit;
		/**
		 * Visibility rights for groups on the remote source.
		 */
		protected VisibilitySets visibility;
	}

	/* UPDATING */
	
	/**
	 * Update CustomContext with this ManagedCategoryProfile. 
	 * It evaluates visibility for user profile and subscribe it 
	 * or not to customContext.
	 * @param customContext the customContext to update
	 * @return true if the category is visible by the userProfile
	 * @throws InfoDomainException 
	 */
	protected VisibilityMode updateCustomContext(final CustomContext customContext) 
		throws InfoDomainException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("id = " + this.getId() + " - updateCustomContext("
					+ customContext.getElementId() + "externalService)");
		}
		loadCategory(); // TODO (GB) a retirer certainement
		return setUpCustomContextVisibility(customContext, DomainTools.getExternalService());
		
	}

	/**
	 *  Load the category referenced by this ManagedCategoryProfile.
	 *  @throws CategoryNotLoadedException 
	 */
	private synchronized void loadCategory() throws CategoryNotLoadedException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("id = " + this.getId() + " - loadCategory(externalService)");
		}
		Accessibility accessibility = getAccess();
		if (Accessibility.PUBLIC.equals(accessibility)) {
			try {
				setElement(DomainTools.getDaoService().getManagedCategory(this));
			} catch (InfoDaoException e) {
				String errorMsg = "The managedCategory " + this.getId()
					+ " is impossible to load.";
				LOG.error(errorMsg);
				throw new CategoryNotLoadedException(errorMsg, e);
			}
						
		} else if (Accessibility.CAS.equals(accessibility)) {
			String url = getCategoryURL();
			String ptCas;
			String errorMsg = "The managedCategory " + this.getId()
			+ " is impossible to load.";
			try {
				ptCas = DomainTools.getExternalService().getUserProxyTicketCAS(url);
			} catch (InfoDomainException e1) {
				LOG.error(errorMsg);
				throw new CategoryNotLoadedException(errorMsg, e1);
			}
			try {
				setElement(DomainTools.getDaoService().getManagedCategory(this, ptCas));
			} catch (InfoDaoException e2) {
				LOG.error(errorMsg);
				throw new CategoryNotLoadedException(errorMsg, e2);
			}
		}
	}
	
	/**
	 * Evaluate visibility of current user for this managed category.
	 * Update customContext (belongs to user) if needed :
	 * add or remove subscription associated with this managedCategoryProfile
	 * (true if in Obliged or in autoSubscribed, or in Allowed)
	 * @param ex access to externalService to evaluate visibility
	 * @param customContext customContext to set up
	 * @return true if the mcp is visible by the user of the customContext else return false 
	 * @throws CategoryNotLoadedException 
	 */
	private VisibilityMode setUpCustomContextVisibility(final CustomContext customContext,
			final ExternalService ex) 
		throws CategoryNotLoadedException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("id = " + this.getId() + " - setUpCustomContextVisibility("
					+ customContext.getElementId() + ",externalService)");
		}
		/*
		 * Algo pour gerer les customCategories :
		 * ------------------------------------
		 * user app. obliged => enregistrer la cat dans le user profile + sortir
		 * user app. autoSub => enregistrer la cat dans le user profile si c'est la premi�re fois + sortir
		 * user app.allowed => rien à faire + sortir
		 * user n'app. rien => effacer la cat.
		 */

		VisibilityMode mode = getVisibility().whichVisibility(ex);
		
		if (mode == VisibilityMode.OBLIGED) {
			if (LOG.isTraceEnabled()) {
				LOG.trace("IsInObliged : " + mode);
			}
			customContext.addSubscription(this);
			return mode;
		}
		
		if (mode == VisibilityMode.AUTOSUBSCRIBED) {
			if (LOG.isTraceEnabled()) {
				LOG.trace("IsInAutoSubscribed : " + mode);
			}
			// TODO (GB later) l'ajouter dans le custom context si c'est la premiere fois
			//customContext.addSubscription(this);
			return mode;
		}
		
		if (mode == VisibilityMode.ALLOWED) {
			if (LOG.isTraceEnabled()) {
				LOG.trace("IsInAllowed : " + mode);
			}
			// Nothing to do
			return mode;
		} 
		
		// ELSE not Visible
		customContext.removeCustomManagedCategoryFromProfile(this.getId());

		mode = VisibilityMode.NOVISIBLE;
		return mode;
	}
	
		
	/**
	 * Update the CustomManagedCategory linked to this ManagedCategoryProfile.
	 * It sets up subscriptions of customManagedCategory on managedSourcesProfiles
	 * defined in ManagedCategory of this Profile, according to managedSourceProfiles visibility
	 * (there is not any loading of source at this time)
	 * @param customManagedCategory customManagedCategory to update
	 * @throws CategoryNotLoadedException
	 * @throws CategoryProfileNotFoundException 
	 */
	protected void updateCustom(final CustomManagedCategory customManagedCategory) 
		throws CategoryNotLoadedException, CategoryProfileNotFoundException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("id = " + this.getId() + " - updateCustom(" + customManagedCategory.getElementId()
					+ ",externalService)");
		}
		ManagedCategory cat = getElement();
		cat.updateCustom(customManagedCategory, DomainTools.getExternalService());
	}
	
	/**
	 * Return a list of (SourceProfile,VisibilityMode). 
	 * Corresponding to visible sources for user, 
	 * in this ManagedCategory and update its related custom (like methode updateCustom): 
	 * It sets up subscriptions of customManagedCategory on managedSourcesProfiles
	 * defined in ManagedCategory of this Profile, according to managedSourceProfiles visibility
	 * @param customManagedCategory custom to update
	 * @return list of (ProfileVisibility)
	 * @throws CategoryNotLoadedException 
	 * @throws CategoryProfileNotFoundException 
	 * @see ManagedCategoryProfile#updateCustom(CustomManagedCategory)
	 */
	protected List<CoupleProfileVisibility> getVisibleSourcesAndUpdateCustom(
			final CustomManagedCategory customManagedCategory) 
		throws CategoryNotLoadedException, CategoryProfileNotFoundException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("id = " + this.getId() + " - getVisibleSourcesAndUpdateCustom("
					+ this.getId() + ",externalService)");
		}
		ManagedCategory cat = getElement();
		return cat.getVisibleSourcesAndUpdateCustom(customManagedCategory, DomainTools.getExternalService());
	}
	
	
	
	/*
	 *************************** SIMPLE ACCESSORS ******************************** */	

	
	/**
	 * Returns the URL of the remote managed category.
	 * @return categoryURL
	 */
	public String getCategoryURL() {
		return categoryURL;
	}
	
	/** 
	 * Sets the URL of the remote managed category.
	 * @param categoryURL the URL to set
	 */
	public void setCategoryURL(final String categoryURL) {
		//RB categoryURL = DomainTools.replaceWithUserAttributes(categoryURL);
		this.categoryURL = categoryURL;
	}

	/**
	 * Returns the state (true or false) of the trust category parameter.
	 * @return trustCategory
	 */
	protected boolean getTrustCategory() {
		return trustCategory;
	}
	
	/**
	 * Sets the trust category parameter?
	 * @param trustCategory 
	 */
	public void setTrustCategory(final boolean trustCategory) {
		this.trustCategory = trustCategory;
	}

	/**
	 * @return access
	 */
	public Accessibility getAccess() {
		return access;
	}
	
	/**
	 * @param access 
	 */
	public void setAccess(final Accessibility access) {
		this.access = access;
	}
	
	/**
	 * @return contextSets
	 */
	protected Set<Context> getContextsSet() {
		return contextsSet;
	}

	/**
	 * @param featuresComputed
	 */
	protected void setFeaturesComputed(final boolean featuresComputed) {
			this.featuresComputed = featuresComputed;
		}
	/**
	 * @return ttl
	 */
	public int getTtl() {
		return ttl;
	}
	
	/**
	 * @param ttl 
	 */
	public void setTtl(final int ttl) {
		this.ttl = ttl;
	}
	/**
	 * @return timeOut
	 */
	public int getTimeOut() {
		return timeOut;
	}
	/**
	 * @param timeOut
	 */
	public void setTimeOut(final int timeOut) {
		this.timeOut = timeOut;
	}
	
}
