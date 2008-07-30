/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.DomainTools;
import org.esupportail.lecture.domain.ExternalService;
import org.esupportail.lecture.exceptions.dao.InfoDaoException;
import org.esupportail.lecture.exceptions.domain.CategoryNotLoadedException;
import org.esupportail.lecture.exceptions.domain.CategoryProfileNotFoundException;
import org.esupportail.lecture.exceptions.domain.InfoDomainException;


/**
 * Managed source profile element. It refers a source and is defined in a managedCategory
 * @author gbouteil
 * @see org.esupportail.lecture.domain.model.SourceProfile
 * @see org.esupportail.lecture.domain.model.ManagedElementProfile
 *
 */
public class ManagedSourceProfile extends SourceProfile implements ManagedElementProfile {
	
	/*
	 ************************** PROPERTIES ******************************** */	
	/**
	 * Log instance.
	 */
	protected static final Log LOG = LogFactory.getLog(ManagedSourceProfile.class); 

// no used anymore	
//	/**
//	 * Ttl of the remote source reloading.
//	 * Using depends on trustCategory parameter
//	 */
//	private int ttl;
	
	/**
	 * Specific user content parameter.
	 * Indicates source multiplicity :
	 * - true : source is specific to a user, it is loaded in user profile => source is a SingleSource
	 * - false : source is global to users, it is loaded in channel environnement => source is a GlobalSource
	 */
	private boolean specificUserContent;
	
	/**
	 * parent category of this managed source profile. 
	 */
	private ManagedCategory category;

	/**
	 * profile of the parent category of this managed source profile. 
	 */
	private ManagedCategoryProfile categoryProfile;
	
	/**
	 * source profile Id.
	 * Defined in the xml file : interne Id of the source Profile
	 */
	private String fileId;
	
	/**
	 * Inner features declared in XML file.
	 */
	private InnerFeatures inner;
	/**
	 * Inheritance rules are applied on feature (take care of inner features).
	 */
	private boolean featuresAreComputed = false;
	/**
	 * Access mode on the Source.
	 */	
	private Accessibility access;
	/**
	 * Visibility rights for groups on the managed element
	 * Its values depends on trustCategory parameter. 
	 */
	private VisibilitySets visibility;
	/**
	 * timeOut to get the Source.
	 */	
	private int timeOut;

	/*
	 ************************** INIT ******************************** */	
	
	/**
	 * Constructor.
	 * @param mc managedCategory parent of this ManagedSourceProfile
	 */
	@SuppressWarnings("synthetic-access")
	public ManagedSourceProfile(final ManagedCategory mc) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("ManagedSourceProfile(" + mc.getProfileId() + ")");
		}
		category = mc;
		categoryProfile = mc.getProfile();
		inner = new InnerFeatures();
	}


	/*
	 *************************** METHODS ******************************** */	
	
	/** 
	 * Update CustomCategory with this ManagedSourceProfile. 
	 * It evaluates visibility for user profile and subscribe it 
	 * or not to customCategory.
	 * @param customManagedCategory the customManagedCategory to update
	 * @param ex access to externalService
	 * @return true if the source is visible by the userProfile
	 * @throws CategoryNotLoadedException 
	 * @throws CategoryProfileNotFoundException 
	 */
	public VisibilityMode updateCustomCategory(
			final CustomManagedCategory customManagedCategory, final ExternalService ex) 
		throws CategoryNotLoadedException, CategoryProfileNotFoundException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("id = " + this.getId() + " - updateCustomCategory("
					+ customManagedCategory.getElementId() + "externalService)");
		}
		// no loadSource(ex) is needed here
		return setUpCustomCategoryVisibility(customManagedCategory, ex);	
	}

	/**
	 * Load the source referenced by this ManagedSourceProfile.
	 * @param ex access to externalService
	 * @throws InfoDomainException 
	 * @see org.esupportail.lecture.domain.model.SourceProfile#loadSource(
	 *   org.esupportail.lecture.domain.ExternalService)
	 */
	@Override
	protected void loadSource(final ExternalService ex) throws InfoDomainException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("id = " + this.getId() + " - loadSource(externalService)");
		}
			
		Accessibility accessibility = getAccess();
		try {
			if (Accessibility.PUBLIC.equals(accessibility)) {
				// managed Source Profile => single or globalSource
				Source source;
				source = DomainTools.getDaoService().getSource(this);
				setElement(source);

			} else if (Accessibility.CAS.equals(accessibility)) {
				String ptCas = ex.getUserProxyTicketCAS(getSourceURL());
				Source source = DomainTools.getDaoService().getSource(this, ptCas);
				setElement(source);

			}
		} catch (InfoDaoException e) {
			String errorMsg = "Impossible to load source with ID: " + this.getId();
			LOG.error(errorMsg);
			throw new InfoDomainException(errorMsg, e);
		}
		
	}

	
	/**
	 * Evaluate visibility of current user for this managed source profile.
	 * Update customManagedCategory (belongs to user) if needed :
	 * add or remove customManagedSources associated with this ManagedSourceProfile
	 * @param ex access to externalService to evaluate visibility
	 * @param customManagedCategory customManagedCategory to set up
	 * @return true if sourceProfile is visible by user (in Obliged or in autoSubscribed, or in Allowed)
	 * @throws CategoryNotLoadedException 
	 * @throws CategoryProfileNotFoundException 
	 */
	
	private VisibilityMode setUpCustomCategoryVisibility(
			final CustomManagedCategory customManagedCategory,
			final ExternalService ex) 
	throws CategoryNotLoadedException, CategoryProfileNotFoundException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("id = " + this.getId() + " - setUpCustomCategoryVisibility(" 
					+ customManagedCategory.getElementId() + ",externalService)");
		}
		/*
		 * Algo pour gerer les customSourceProfiles :
		 * ------------------------------------
		 * user app. obliged => enregistrer la source dans le user profile + sortir
		 * user app. autoSub => enregistrer la source dans le user profile si c'est la première fois + sortir
		 * user app.allowed => rien à faire + sortir
		 * user n'app. rien => effacer la custom source .
		 * 
		 */
		
		// get visibilitySets of the current sourceProfile
		VisibilitySets visibilitySets = getVisibility();
		
// 		GB : J'ai tout commenté car c'est géré par la méthode computeFeatures()
//		//if visibilitySets is NOT defined on sourceProfile
//		if (visibilitySets.isEmpty()) {
//			//we get, as default, the VisibilityMode from the CategoryProfile containing the sourceProfile.
//			//Please note that in case of CategoryProfile with TrustedCategory=yes attribute then the 
//			//visibilitySets of CategoryProfile was replaced by the CategoryProfile of the trusted Category.
//			visibilitySets = customManagedCategory.getProfile().getVisibility();
//		} 
		
		VisibilityMode mode = VisibilityMode.NOVISIBLE;

		mode = visibilitySets.whichVisibility(ex);
		
		if (mode == VisibilityMode.OBLIGED) {
			if (LOG.isTraceEnabled()) {
				LOG.trace("IsInObliged : " + mode);
			}
			customManagedCategory.addSubscription(this);
			return mode;
		}
		
		if (mode == VisibilityMode.AUTOSUBSCRIBED) {
			if (LOG.isTraceEnabled()) {
				LOG.trace("IsInAutoSubscribed : " + mode);
			}
			// TODO (GB later) l'ajouter dans le custom category si c'est la premiere fois
			//customManagedCategory.addSubscription(this);
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
		customManagedCategory.removeCustomManagedSourceFromProfile(this.getId());
		mode = VisibilityMode.NOVISIBLE;
		return mode;
	}

	/**	
	 * Return access of the source, taking care of inheritance regulars.
	 * @return access
	 * @throws CategoryNotLoadedException 
	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#getAccess()
	 */
	public Accessibility getAccess() throws CategoryNotLoadedException  {
		if (LOG.isDebugEnabled()) {
			LOG.debug("id = " + this.getId() + " - getAccess()");
		}
		computeFeatures();
		return access;
	}

	/**
	 * @see ManagedSourceProfile#access
	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#setAccess(
	 * org.esupportail.lecture.domain.model.Accessibility)
	 */
	public void setAccess(final Accessibility access) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("id=" + this.getId() + " - setAccess()");
		}
		inner.access = access;
		featuresAreComputed = false;
	}

	/**
	 * Return visibility of the source, taking care of inheritance regulars.
	 * @return visibility
	 * @throws CategoryNotLoadedException 
	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#getVisibility()
	 */
	public VisibilitySets getVisibility() throws CategoryNotLoadedException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("id=" + this.getId() + " - getVisibility()");
		}
		computeFeatures();
		return visibility;
	}

	/**
	 * @see ManagedSourceProfile#visibility
	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#setVisibility(
	 * org.esupportail.lecture.domain.model.VisibilitySets)
	 */
	public void setVisibility(final VisibilitySets visibility) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("id=" + this.getId() + " - setVisibility(visibility)");
		}
		inner.visibility = visibility;
		featuresAreComputed = false;
	}

	/**
	 * Return timeOut of the source, taking care of inheritance regulars.
	 * @return timeOut
	 * @throws CategoryNotLoadedException 
	 */
	@Override
	public int getTimeOut() throws CategoryNotLoadedException  {
		if (LOG.isDebugEnabled()) {
			LOG.debug("id=" + this.getId() + " - getTimeOut()");
		}
		computeFeatures();
		return timeOut;
	}

	/**
	 * @see org.esupportail.lecture.domain.model.SourceProfile#setTimeOut(int)
	 */
	@Override
	public void setTimeOut(final int timeOut) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("id=" + this.getId() + " - setTimeOut(" + timeOut + ")");
		}
		inner.timeOut = timeOut;
		featuresAreComputed = false;
	}

	/**
	 * Computes rights on parameters shared between parent ManagedCategory and managedSourceProfile.
	 * (timeOut, visibility,access)
	 * @throws CategoryNotLoadedException 
	 */
	private void computeFeatures() throws CategoryNotLoadedException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("id = " + this.getId() + " - computeFeatures()");
		}
		
		if (!featuresAreComputed) {
			try {
				if (categoryProfile.getTrustCategory()) {		
					access = inner.access;
					visibility = inner.visibility;
					timeOut = inner.timeOut;
					
					if (access == null) {
						access = category.getAccess();
					}
					if (visibility == null) {
						visibility = category.getVisibility();
					} else if (visibility.isEmpty()) {
						visibility = category.getVisibility();
					}
					if (timeOut == 0) {
						timeOut = category.getTimeOut();
					}
				} else {
					// No trust => features of categoryProfile 
					access = categoryProfile.getAccess();
					visibility = categoryProfile.getVisibility();
					timeOut = categoryProfile.getTimeOut();
				}
				featuresAreComputed = true;
			} catch (CategoryNotLoadedException e) {
				String errorMsg = "Impossible to compute features on element " 
					+ this.getId() + "because Category is not loaded";
				LOG.error(errorMsg);
				throw e;
			}
		}
	}
	
	/* 
	 *************************** INNER CLASS ******************************** */	
	
	/**
	 * Inner Features (accessibility, visibility, timeOut) declared in xml file. 
	 * These values are used according to inheritance regulars
	 * @author gbouteil
	 */
	private class InnerFeatures {
		 
		/**
		 * Access mode on the remote source.
		 */
		public Accessibility access;
		/**
		 * Visibility rights for groups on the remote source.
		 */
		public VisibilitySets visibility;
		/**
		 * timeOut to get the remote source.
		 */
		public int timeOut;
		
				
	}
	
	/* 
	 *************************** ACCESSORS ******************************** */	
	
	
	/**
	 * @return the parent of this managed source profile
	 */
	public ManagedCategory getParent() {
		return category;
	}

	
// 	No used anymore	
//	/**
//	 * Returns ttl.
//	 * @see ManagedSourceProfile#ttl
//	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#getTtl()
//	 */
//	@Override
//	public int getTtl()  {
//		return ttl;
//	}

// 	No used anymore	
//	/**
//	 * @see ManagedSourceProfile#ttl
//	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#setTtl(int)
//	 */
//	@Override
//	public void setTtl(final int ttl) {
//		this.ttl = ttl;
//	}

	/**
	 * Returns specificUserContent value.
	 * @return specificUserContent
	 * @see ManagedSourceProfile#specificUserContent
	 */
	public boolean isSpecificUserContent() {
		return specificUserContent;
	}

	/**
	 * Sets specificUserContent.
	 * @param specificUserContent
	 * @see ManagedSourceProfile#specificUserContent
	 */
	public void setSpecificUserContent(final boolean specificUserContent) {
		this.specificUserContent = specificUserContent;
	}


	/**
	 * @return fileId : sourceProfileId defined in xml file category
	 */
	public String getFileId() {
		return fileId;
	}


	/**
	 * @param fileId sourceProfileId defined in xml category file
	 */
	public void setFileId(final String fileId) {
		this.fileId = fileId;
		super.setId(super.makeId("m", categoryProfile.getId(), fileId));
	}
	
}
