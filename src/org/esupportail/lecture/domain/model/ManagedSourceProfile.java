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
import org.esupportail.lecture.exceptions.domain.InfoExternalException;
import org.esupportail.lecture.exceptions.domain.ManagedCategoryNotLoadedException;
import org.esupportail.lecture.exceptions.domain.SourceNotLoadedException;


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
	private boolean featuresComputed;
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
	public ManagedSourceProfile(final ManagedCategory mc) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("ManagedSourceProfile(" + mc.getProfileId() + ")");
		}
		category = mc;
		categoryProfile = mc.getProfile();
		inner = new InnerFeatures();
		featuresComputed = false;
	}


	/*
	 *************************** METHODS ******************************** */	
	/**	
	 * Return access of the source, taking care of inheritance regulars.
	 * @return access
	 */
	public Accessibility getAccess() {
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
		inner.setAccess(access);
		featuresComputed = false;
	}

	/**
	 * @param fileId sourceProfileId defined in xml category file
	 */
	public void setFileId(final String fileId) {
		this.fileId = fileId;
		super.setId(super.makeId("m", categoryProfile.getId(), fileId));
	}
	
	/**
	 * @see org.esupportail.lecture.domain.model.SourceProfile#getTtl()
	 */
	@Override
	public int getTtl() {
		return getParent().getTtl();
	}
	
	
	/**
	 * Return visibility of the source, taking care of inheritance regulars.
	 * @return visibility
	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#getVisibility() 
	 */
	public VisibilitySets getVisibility() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("id=" + this.getId() + " - getVisibility()");
		}
		computeFeatures();
		return visibility;
	}

	/**
	 * Sets visibility of source profile (value defined in XML file).
	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#setVisibility(
	 * org.esupportail.lecture.domain.model.VisibilitySets)
	 */
	public void setVisibility(final VisibilitySets visibility) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("id=" + this.getId() + " - setVisibility(visibility)");
		}
		inner.setVisibility(visibility);
		featuresComputed = false;
	}

	/**
	 * Return timeOut of the source, taking care of inheritance regulars.
	 * @return timeOut
	 */
	@Override
	public int getTimeOut() {
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
		inner.setTimeOut(timeOut);
		featuresComputed = false;
	}

	/**
	 * Computes rights on parameters shared between parent ManagedCategory and managedSourceProfile.
	 * (timeOut, visibility,access)
	 */
	private void computeFeatures()  {
		if (LOG.isDebugEnabled()) {
			LOG.debug("id = " + this.getId() + " - computeFeatures()");
		}
		
		try {
			if (!featuresComputed) {
				if (categoryProfile.getTrustCategory()) {		
					access = inner.getAccess();
					visibility = inner.getVisibility();
					timeOut = inner.getTimeOut();
						
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
				featuresComputed = true;
			}
		} catch (ManagedCategoryNotLoadedException e) {
			String errorMsg = "A ManagedCategoryNotLoadedException is thrown whereas code "
					+ "is caught by a managedSourceProfile (defined in managedCategory file,"
					+ "please contact developper)";
			LOG.warn(errorMsg);
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
		private Accessibility access;
		/**
		 * Visibility rights for groups on the remote source.
		 */
		private VisibilitySets visibility;
		/**
		 * timeOut to get the remote source.
		 */
		private int timeOut;
		
		/**
		 * Constructor. 
		 */
		protected InnerFeatures() {
			// Nothing to do
		}

		/**
		 * @return access
		 */
		protected Accessibility getAccess() {
			return access;
		}
		/**
		 * @param access
		 */
		protected void setAccess(final Accessibility access) {
			this.access = access;
		}
		/**
		 * @return visibility
		 */
		protected VisibilitySets getVisibility() {
			return visibility;
		}
		/**
		 * @param visibility
		 */
		protected void setVisibility(final VisibilitySets visibility) {
			this.visibility = visibility;
		}
		/**
		 * @return timeOut
		 */
		protected int getTimeOut() {
			return timeOut;
		}
		/**
		 * @param timeOut
		 */
		protected void setTimeOut(final int timeOut) {
			this.timeOut = timeOut;
		}
				
	}
	
	/* UPDATING */
	
	/** 
	 * Update CustomCategory with this ManagedSourceProfile. 
	 * It evaluates visibility for user profile and subscribe it 
	 * or not to customCategory.
	 * @param customManagedCategory the customManagedCategory to update
	 * @return true if the source is visible by the userProfile
	 */
	protected VisibilityMode updateCustomCategory(final CustomManagedCategory customManagedCategory) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("id = " + this.getId() + " - updateCustomCategory("
					+ customManagedCategory.getElementId() + ")");
		}
		return setUpCustomCategoryVisibility(customManagedCategory);	
	}

	/**
	 * Load the source referenced by this ManagedSourceProfile.
	 * @throws SourceNotLoadedException 
	 * @see org.esupportail.lecture.domain.model.SourceProfile#loadSource()
	 */
	@Override
	protected void loadSource() throws SourceNotLoadedException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("id = " + this.getId() + " - loadSource()");
		}
		try {
			Accessibility accessibility = getAccess();
			if (Accessibility.PUBLIC.equals(accessibility)) {
				Source source;
				source = DomainTools.getDaoService().getSource(this);
				setElement(source);
			} else if (Accessibility.CAS.equals(accessibility)) {
				ExternalService ex = DomainTools.getExternalService();
				String sourceUrl = getSourceURL();
				String ptCas = ex.getUserProxyTicketCAS(sourceUrl);
				Source source = DomainTools.getDaoService().getSource(this, ptCas);
				setElement(source);
			} 
		} catch (InfoDaoException e) {
			String errorMsg = "The source " + this.getId() 
				+ " is impossible to be loaded because of DaoException.";
			LOG.error(errorMsg);
			throw new SourceNotLoadedException(errorMsg, e);
		} catch (InfoExternalException e) {
			String errorMsg = "The source " + this.getId() + " is impossible to be loaded.";
			LOG.error(errorMsg);
			throw new SourceNotLoadedException(errorMsg, e);
		}
		
	}

	
	/**
	 * Evaluate visibility of current user for this managed source profile.
	 * Update customManagedCategory (belongs to user) if needed :
	 * add or remove customManagedSources associated with this ManagedSourceProfile
	 * @param customManagedCategory customManagedCategory to set up
	 * @return true if sourceProfile is visible by user (in Obliged or in autoSubscribed, or in Allowed)
	 */
	
	private VisibilityMode setUpCustomCategoryVisibility(final CustomManagedCategory customManagedCategory) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("id = " + this.getId() + " - setUpCustomCategoryVisibility(" 
					+ customManagedCategory.getElementId() + ")");
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

		mode = visibilitySets.whichVisibility();
		
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



	
	/* 
	 *************************** ACCESSORS ******************************** */	
	
	
	/**
	 * @return the parent of this managed source profile
	 */
	protected ManagedCategory getParent() {
		return category;
	}

	/**
	 * Returns specificUserContent value.
	 * @return specificUserContent
	 * @see ManagedSourceProfile#specificUserContent
	 */
	protected boolean isSpecificUserContent() {
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
	protected String getFileId() {
		return fileId;
	}


	
}
