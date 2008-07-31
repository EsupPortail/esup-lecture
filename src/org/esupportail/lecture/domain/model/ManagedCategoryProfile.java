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

	/*
	 ************************** PROPERTIES ******************************** */	
	/**
	 * Log instance.
	 */
	protected static final Log LOG = LogFactory.getLog(ManagedCategoryProfile.class); 
	/**
	 * Contexts where these profiles category are referenced.
	 */
	private Set<Context> contextsSet = new HashSet<Context>();
	
	/**
	 * URL of the remote managed category.
	 */
	private String categoryURL;
	/**
	 * Ttl of the remote managed category reloading.
	 */
	private int ttl;
	
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
	
// used later	
//	/**
//	 * Editability mode on the category 
//	 */	
//	private Editability edit;
	/**
	 * Visibility rights for groups on the managed element
	 * Its values depends on trustCategory parameter. 
	 */
	private VisibilitySets visibility;
	/**
	 * timeOut to get the Source.
	 */	
	private int timeOut;
	/**
	 * Referrenced category.
	 */
	private ManagedCategory category;
	
	/*
	 ************************** INITIALIZATION ******************************** */	
	
	
	/**
	 * Constructor. 
	 */
	protected ManagedCategoryProfile() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("ManagedCategoryProfile()");
		}
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
		category = (ManagedCategory) super.getCategory(); 
		if (category == null) {
			String errorMsg = "Category " + getId() + " is not loaded in profile";
			LOG.error(errorMsg);
			throw new CategoryNotLoadedException(errorMsg);
		}
		return  category;
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
		ManagedCategory category = getElement();
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
		ManagedCategory category = getElement();
		return category.getVisibleSourcesAndUpdateCustom(customManagedCategory, ex);
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

	/**
	 * Returns the list of contexts referencing this managedCategoryProfile.
	 * @return the list of contexts
	 */
	public List<Context> getAdoptiveParents() {
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
	
	/**
	 * @return visibility
	 */
	public VisibilitySets getVisibility() {
		return visibility;
	}

	/**
	 * @param visibility 
	 */
	public void setVisibility(final VisibilitySets visibility) {
		this.visibility = visibility;
	}


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
