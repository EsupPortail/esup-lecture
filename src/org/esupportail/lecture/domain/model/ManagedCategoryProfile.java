/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;



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

	/*
	 ************************** PROPERTIES ******************************** */	
	/**
	 * Log instance.
	 */
	protected static final Log LOG = LogFactory.getLog(ManagedCategoryProfile.class); 

	/**
	 * URL of the remote managed category.
	 */
	private String categoryURL;
	
	/**
	 * Access mode on the remote managed category.
	 */
	private Accessibility access;
	
	/**
	 * trustCategory parameter : indicates between managed category and category profile, which one to trust
	 * True : category is trusted. 
	 * False : category is not trusted, only parameters profile are good 
	 * parameters interested : edit, visibility
	 */
	private boolean trustCategory;
	
	/**
	 * Resolve feature values (edit, visibility) from :
	 * - managedCategoryProfile features
	 * - managedCategory features
	 * - trustCategory parameter.
	 */
	private ManagedCategoryFeatures features;
		
	// Later
//	/**
//	 * Remote managed category edit mode : not used for the moment
//	 * Using depends on trustCategory parameter
//	 */	
//	private Editability edit;
	
	/**
	 * Visibility rights for groups on the remote managed category.
	 * Value is one defined in channel config. This value not must be
	 * exploited directly. It must be used by attribute "features" 
	 * (take care of trustCategory parameter)
	 */
	private VisibilitySets visibility;

	/**
	 * Ttl of the remote managed category reloading.
	 */
	private int ttl;
	
	/**
	 * timeOut to get the remote managedCategory.
	 */
	private int timeOut;

	/**
	 * Contexts where these profiles category are referenced.
	 */
	private Set<Context> contextsSet = new HashSet<Context>();


	
	
	/*
	 ************************** INITIALIZATION ******************************** */	
	
	
	/**
	 * Constructor. 
	 */
	protected ManagedCategoryProfile() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("ManagedCategoryProfile()");
		}
		features = new ManagedCategoryFeatures(this);
	}
	
	/*
	 ************************** METHODS ******************************** */	
	
	

	/**
	 * Update CustomContext with this ManagedCategoryProfile. 
	 * It evaluates visibility for user profile and subscribe it 
	 * or not to customContext.
	 * @param customContext the customContext to update
	 * @param ex access to externalService to evaluate visibility
	 * @return true if the category is visible by the userProfile
	 * @throws InfoDomainException 
	 */
	protected VisibilityMode updateCustomContext(final CustomContext customContext,
			final ExternalService ex) 
		throws InfoDomainException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("id = " + this.getId() + " - updateCustomContext("
					+ customContext.getElementId() + "externalService)");
		}
		loadCategory(ex);
		return setUpCustomContextVisibility(customContext, ex);
		
	}

	/**
	 *  Load the category referenced by this ManagedCategoryProfile.
	 *  @param ex access to externalservice to get proxy ticket CAS
	 * @throws InfoDomainException 
	 */
	@Override
	protected synchronized void loadCategory(final ExternalService ex) throws InfoDomainException {
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
				throw new InfoDomainException(errorMsg, e);
			}
						
		} else if (Accessibility.CAS.equals(accessibility)) {
			String url = getUrlCategory();
			String ptCas = ex.getUserProxyTicketCAS(url);
			try {
				setElement(DomainTools.getDaoService().getManagedCategory(this, ptCas));
			} catch (InfoDaoException e) {
				String errorMsg = "The managedCategory " + this.getId()
				+ " is impossible to load.";
				LOG.error(errorMsg);
				throw new InfoDomainException(errorMsg, e);
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
		 * user app.allowed => rien � faire + sortir
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
	 * @param ex access to external service for visibility evaluation
	 * @throws CategoryNotLoadedException
	 * @throws CategoryProfileNotFoundException 
	 */
	protected void updateCustom(final CustomManagedCategory customManagedCategory,
			final ExternalService ex) 
		throws CategoryNotLoadedException, CategoryProfileNotFoundException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("id = " + this.getId() + " - updateCustom(" + customManagedCategory.getElementId()
					+ ",externalService)");
		}
		ManagedCategory category = (ManagedCategory) getElement();
		category.updateCustom(customManagedCategory, ex);
	}
	
	/**
	 * Return a list of (SourceProfile,VisibilityMode). 
	 * Corresponding to visible sources for user, 
	 * in this ManagedCategory and update its related custom (like methode updateCustom): 
	 * It sets up subscriptions of customManagedCategory on managedSourcesProfiles
	 * defined in ManagedCategory of this Profile, according to managedSourceProfiles visibility
	 * @param customManagedCategory custom to update
	 * @param ex access to externalService
	 * @return list of (ProfileVisibility)
	 * @throws CategoryNotLoadedException 
	 * @throws CategoryProfileNotFoundException 
	 * @see ManagedCategoryProfile#updateCustom(CustomManagedCategory, ExternalService)
	 */
	protected List<CoupleProfileVisibility> getVisibleSourcesAndUpdateCustom(
			final CustomManagedCategory customManagedCategory, final ExternalService ex) 
		throws CategoryNotLoadedException, CategoryProfileNotFoundException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("id = " + this.getId() + " - getVisibleSourcesAndUpdateCustom("
					+ this.getId() + ",externalService)");
		}
		ManagedCategory category = (ManagedCategory) getElement();
		return category.getVisibleSourcesAndUpdateCustom(customManagedCategory, ex);
	}
	
	
	
	/** 
	 * Computes rights.
	 * On parameters shared between a ManagedCategoryProfile and its
	 * ManagedCategory (edit, visibility)
	 * @throws CategoryNotLoadedException 
	 */
	public void computeFeatures() throws CategoryNotLoadedException  {
		if (LOG.isDebugEnabled()) {
			LOG.debug("id=" + this.getId() + " - computeFeatures()");
		}
		
		ManagedCategory managedCategory = (ManagedCategory) getElement();
		//Editability setEdit;
		VisibilitySets setVisib = visibility;
		
		if (trustCategory) {		
			//setEdit = managedCategory.getEdit();
			setVisib = managedCategory.getVisibility();
			
//			if (setEdit == null) {
//				setEdit = this.edit;
//			}
			if (setVisib == null) {
				setVisib = this.visibility;
			}
		}
		/* else {
				Already done during channel config loading 
		} */
		features.update(setVisib);
	}

	/**
	 * Compute visibility value from features and returns it.
	 * @return Visibility
	 * @throws CategoryNotLoadedException 
	 * @see ManagedCategoryProfile#visibility
	 */
	public VisibilitySets getVisibility() throws CategoryNotLoadedException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("id=" + this.getId() + " - getVisibility()");
		}
		return features.getVisibility();
	}
	
	/**
	 * @param visibility
	 */
	public void setVisibility(final VisibilitySets visibility) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("id=" + this.getId() + " - setVisibility(visibility)");
		}
		this.visibility = visibility;
		features.setIsComputed(false);
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
//		 TODO (GB later) on pourrait faire un loadCategory ou autre chose ou ailleurs ?
		return (ManagedSourceProfile) getElement().getSourceProfileById(id);

	}

	
	
	
	/*
	 *************************** ACCESSORS ******************************** */	


	/**
	 * Returns the URL of the remote managed category.
	 * @return urlCategory
	 */
	public String getUrlCategory() {
		return categoryURL;
	}
	
	/** 
	 * Sets the URL of the remote managed category.
	 * @param categoryURL the URL to set
	 */
	public void setUrlCategory(final String categoryURL) {
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
	protected void setTrustCategory(final boolean trustCategory) {
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
	
	// utile plus tard
//	protected Editability getEdit() {
//		return features.getEdit();
//	}
//	protected void setEdit(Editability edit) {
//		this.edit = edit;
//	features.setComputed(false);
//	}
	
	

//	/**
//	 * @return allowed visibility group 
//	 */
//	public DefinitionSets getVisibilityAllowed() {
//		return getVisibility().getAllowed();
//	}
//
//
//	/** 
//	 * @return autoSubscribed group visibility
//	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#getVisibilityAutoSubscribed()
//	 */
//	public DefinitionSets getVisibilityAutoSubscribed() {
//		return getVisibility().getAutoSubscribed();
//	}
//
//	
//	/**
//	 * @return obliged group visibility
//	 * @throws ElementNotLoadedException 
//	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#getVisibilityObliged()
//	 */
//	public DefinitionSets getVisibilityObliged()  {
//		return getVisibility().getObliged();
//		
//	}

	/**
	 * @return ttl
	 * @see ManagedCategoryProfile#ttl
	 */
	public int getTtl() {
		return ttl;
	}
	
	/**
	 * @param ttl 
	 * @see ManagedCategoryProfile#ttl
	 */
	public void setTtl(final int ttl) {
		this.ttl = ttl;
	}

	/**
	 * @return timeOut
	 * @see ManagedCategoryProfile#timeOut
	 */
	public int getTimeOut() {
		return timeOut;
	}
	
	/**
	 * @param timeOut
	 * @see ManagedCategoryProfile#timeOut
	 */
	public void setTimeOut(final int timeOut) {
		this.timeOut = timeOut;
	}
	/**
	 * @return contextSets
	 */
	protected Set<Context> getContextsSet() {
		return contextsSet;
	}

}
