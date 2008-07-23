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
	
	/**
	 * Access mode on the remote source.
	 */
	private Accessibility access;
	
	/**
	 * Visibility rights for groups on the remote source.
	 */
	private VisibilitySets visibility;

	/**
	 * Ttl of the remote source reloading.
	 * Using depends on trustCategory parameter
	 */
	private int ttl;
	
	/**
	 * Specific user content parameter.
	 * Indicates source multiplicity :
	 * - true : source is specific to a user, it is loaded in user profile => source is a SingleSource
	 * - false : source is global to users, it is loaded in channel environnement => source is a GlobalSource
	 */
	private boolean specificUserContent;
	
	/**
	 * Resolve feature values (access, visibility).
	 * From :
	 * - managedSourceProfile 
	 * - source 
	 * - trustCategory parameter 
	 */
	private ManagedSourceFeatures features;

	/**
	 * profile of the parent category of this managed source profile. 
	 */
	private ManagedCategoryProfile categoryProfile;
	
	/**
	 * source profile Id.
	 * Defined in the xml file : interne Id of the source Profile
	 */
	private String fileId;

	/*
	 ************************** INIT ******************************** */	
	
	/**
	 * Constructor.
	 * @param mcp profile of the managedCategory parent of this ManagedSourceProfile
	 */
	public ManagedSourceProfile(final ManagedCategoryProfile mcp) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("ManagedSourceProfile(" + mcp.getId() + ")");
		}
		categoryProfile = mcp;
		features = new ManagedSourceFeatures(this);
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
	 * Computes rights on parameters shared between parent ManagedCategory and managedSourceProfile.
	 * ManagedCategory (edit, visibility,access)
	 * @throws CategoryNotLoadedException 
	 */
	public void computeFeatures() throws CategoryNotLoadedException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("id = " + this.getId() + " - computeFeatures()");
		}
	
		/* Features that can be herited by the managedCategoryProfile */
		Accessibility setAccess;
		VisibilitySets setVisib;
		int setTimeOut;
		
		if (categoryProfile.getTrustCategory()) {		
			setAccess = access;
			setVisib = visibility;
			setTimeOut = super.timeOut;
			
			if (setAccess == null) {
				setAccess = categoryProfile.getAccess();
			}
			if (setVisib == null) {
				setVisib = categoryProfile.getVisibility();
			}
			if (setTimeOut == 0) {
				setTimeOut = categoryProfile.getTimeOut();
			}
		} else {
			setAccess = categoryProfile.getAccess();
			setVisib = categoryProfile.getVisibility();
			setTimeOut = categoryProfile.getTimeOut();
			
		}
				
		features.update(setVisib, setAccess, setTimeOut);
		
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
		
		//features.compute();
		//features.setIsComputed(false); // TODO (GB later) � optimiser
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
		 * user app. autoSub => enregistrer la source dans le user profile si c'est la premi�re fois + sortir
		 * user app.allowed => rien � faire + sortir
		 * user n'app. rien => effacer la custom source .
		 * 
		 * RB : En plus on doit v�rifier si la Source a une visibilit�. 
		 * Si ce n'est pas le cas on regarde ce qui est au niveau de la cat�gorie 
		 * afin de le prendre comme valeur par d�faut. 
		 */
		
		// get visibilitySets of the current sourceProfile
		VisibilitySets visibilitySets = getVisibility();
		
		VisibilityMode mode = VisibilityMode.NOVISIBLE;
		//if visibilitySets is NOT defined on sourceProfile
		if (visibilitySets.isEmpty()) {
			//we get, as default, the VisibilityMode from the CategoryProfile containing the sourceProfile.
			//Please note that in case of CategoryProfile with TrustedCategory=yes attribute then the 
			//visibilitySets of CategoryProfile was replaced by the CategoryProfile of the trusted Category.
			visibilitySets = customManagedCategory.getProfile().getVisibility();
		} 
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
	 * Compute access value from feature and returns it.
	 * @return access
	 * @throws CategoryNotLoadedException 
	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#getAccess()
	 */
	public Accessibility getAccess() throws CategoryNotLoadedException  {
		if (LOG.isDebugEnabled()) {
			LOG.debug("id = " + this.getId() + " - getAccess()");
		}
		return features.getAccess();
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
		this.access = access;
		features.setIsComputed(false);
	}

	/**
	 * Compute visibility value from features and returns it.
	 * @return visibility
	 * @throws CategoryNotLoadedException 
	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#getVisibility()
	 */
	public VisibilitySets getVisibility() throws CategoryNotLoadedException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("id=" + this.getId() + " - getVisibility()");
		}
		return features.getVisibility();
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
		this.visibility = visibility;
		features.setIsComputed(false);
	}
	
	/**
	 * Compute timeOut value from features and returns it.
	 * @return timeOut
	 * @throws CategoryNotLoadedException 
	 */
	@Override
	public int getTimeOut() throws CategoryNotLoadedException  {
		if (LOG.isDebugEnabled()) {
			LOG.debug("id=" + this.getId() + " - getTimeOut()");
		}
		if (LOG.isTraceEnabled()) {
			LOG.trace("timeOut : " + features.getTimeOut());
		}
		return features.getTimeOut();
	}


	
	/**
	 * @see org.esupportail.lecture.domain.model.SourceProfile#setTimeOut(int)
	 */
	@Override
	public void setTimeOut(final int timeOut) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("id=" + this.getId() + " - setTimeOut(" + timeOut + ")");
		}
		super.timeOut = timeOut;
		features.setIsComputed(false);
	}
	
	/* 
	 *************************** ACCESSORS ******************************** */	
	
	/**
	 * Returns ttl.
	 * @see ManagedSourceProfile#ttl
	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#getTtl()
	 */
	@Override
	public int getTtl()  {
		return ttl;
	}

	/**
	 * @see ManagedSourceProfile#ttl
	 * @see org.esupportail.lecture.domain.model.ManagedElementProfile#setTtl(int)
	 */
	@Override
	public void setTtl(final int ttl) {
		this.ttl = ttl;
	}

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
